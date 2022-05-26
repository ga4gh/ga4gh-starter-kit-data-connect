package org.ga4gh.starterkit.dataconnect.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.ga4gh.starterkit.common.config.ServerProps;
import org.ga4gh.starterkit.common.exception.BadRequestException;
import org.ga4gh.starterkit.common.util.logging.LoggingUtil;
import org.ga4gh.starterkit.dataconnect.model.ListTablesResponse;
import org.ga4gh.starterkit.dataconnect.model.TableProperties;
import org.ga4gh.starterkit.dataconnect.utils.hibernate.DataConnectHibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@RestController
public class Tables {

    @Autowired
    private DataConnectHibernateUtil hibernateUtil;

    @Autowired
    private LoggingUtil loggingUtil;

    @GetMapping(path="/tables")
    public MappingJacksonValue listTables() {
        try {
            loggingUtil.debug("Get request to /tables endpoint");
            List<String> tableNames = hibernateUtil.getEntityNames();
            ArrayList<TableProperties> tablePropertiesArrayList = new ArrayList<>();
            ServerProps serverProps = new ServerProps();
            for (String tableName : tableNames) {
                loggingUtil.debug(String.format("retrieving properties for %s table",tableName));
                try(InputStream descriptionInputStream=Thread.currentThread().getContextClassLoader().getResourceAsStream(String.format("description/%s.json",tableName))){
                    ObjectMapper descriptionMapper = new ObjectMapper();
                    Map<?, ?> descriptionMap = descriptionMapper.readValue(descriptionInputStream, Map.class);
                    tablePropertiesArrayList.add (
                            new TableProperties (
                                    tableName,
                                    (String) descriptionMap.get("description"),
                                    String.format("%s://%s:%s/table/%s/info", serverProps.getScheme(), serverProps.getHostname() , serverProps.getPublicApiPort(),tableName)));
                }

            }
            ListTablesResponse listTablesResponse = new ListTablesResponse(tablePropertiesArrayList);
            MappingJacksonValue mapping = new MappingJacksonValue(listTablesResponse);
            // filter out unwanted fields from the list of table infos
            SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.
                    filterOutAllExcept(new HashSet<String>(Arrays.asList(
                            "tables",
                            "name",
                            "description",
                            "data_model")));
            FilterProvider filters = new SimpleFilterProvider().addFilter("tablePropertiesFilter", filter);
            mapping.setFilters(filters);
            return mapping;
        } catch (Exception e) {
            loggingUtil.error( e.getMessage());
            throw new BadRequestException(e.getMessage());
        }
    }

    @GetMapping(path = "table/{table_name}/info")
    public MappingJacksonValue getTableInfo(
        @PathVariable(name = "table_name") String tableName
    ) {
        try(
                InputStream dataModelInputStream=Thread.currentThread().getContextClassLoader().getResourceAsStream(String.format("data_model/%s.json",tableName));
                InputStream descriptionInputStream=Thread.currentThread().getContextClassLoader().getResourceAsStream(String.format("description/%s.json",tableName))
        ){
            loggingUtil.debug(String.format("Get request to /table/%s/info endpoint",tableName));

            // read the table metadata from json file
            ObjectMapper dataModelMapper = new ObjectMapper();
            ObjectMapper descriptionMapper = new ObjectMapper();
            Map<?, ?> dataModelMap = dataModelMapper.readValue(dataModelInputStream, Map.class);
            Map<?, ?> descriptionMap = descriptionMapper.readValue(descriptionInputStream, Map.class);
            TableProperties tableProperties = new TableProperties(
                    tableName,
                    (String) descriptionMap.get("description"),
                    (HashMap) dataModelMap.get("data_model"));
            MappingJacksonValue mapping = new MappingJacksonValue(tableProperties);
            SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("name","description","data_model");
            FilterProvider filters = new SimpleFilterProvider().addFilter("tablePropertiesFilter", filter);
            mapping.setFilters(filters);
            return mapping;
        } catch (IOException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @GetMapping(path = "table/{table_name}/data")
    public MappingJacksonValue getTableData(
        @PathVariable(name = "table_name") String tableName
    ) {
        try (InputStream dataModelInputStream=Thread.currentThread().getContextClassLoader().getResourceAsStream(String.format("data_model/%s.json",tableName))){
            loggingUtil.debug(String.format("Get request to /table/%s/data endpoint",tableName));

            // Execute sql query
            Session session = hibernateUtil.newTransaction();
            NativeQuery<String> query = session.createSQLQuery(String.format("Select json_data from %s;",tableName));
            List<String> rawRecords = query.getResultList();
            hibernateUtil.endTransaction(session);

            // Process results
            ObjectMapper dataModelMapper = new ObjectMapper();
            ArrayNode processedRecords = dataModelMapper.createArrayNode();
            for (String rawRecord : rawRecords) {
                JsonNode processedRecord = dataModelMapper.readTree(rawRecord);
                processedRecords.add(processedRecord);
            }
            Map<?, ?> map = dataModelMapper.readValue(dataModelInputStream, Map.class);
            TableProperties tableProperties = new TableProperties(
                    (HashMap) map.get("data_model"),
                    processedRecords);
            MappingJacksonValue mapping = new MappingJacksonValue(tableProperties);
            SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("data_model","data");
            FilterProvider filters = new SimpleFilterProvider().addFilter("tablePropertiesFilter", filter);
            mapping.setFilters(filters);
            return mapping;
        } catch (IOException e) {
            throw new BadRequestException(e.getMessage());
        }

    }
}