package org.ga4gh.starterkit.dataconnect.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.ga4gh.starterkit.common.exception.BadRequestException;
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
import java.nio.file.Paths;
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

    @GetMapping(path="/tables")
    public MappingJacksonValue listTables() {
        try {
            List<String> tableNames = hibernateUtil.getEntityNames();
            ArrayList<TableProperties> tablePropertiesArrayList = new ArrayList<>();
            for (String tableName : tableNames) {
                ObjectMapper mapper = new ObjectMapper();
                // TODO: handle cases where metadata is not available for a table
                Map<?, ?> map = mapper.readValue(Paths.get(String.format("./tables/%s.json", tableName)).toFile(), Map.class);

                tablePropertiesArrayList.add (
                        new TableProperties (
                                tableName,
                                (String) map.get("description"),
                                String.format("table/%s/info", tableName)));
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
        } catch (IOException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @GetMapping(path = "table/{table_name}/info")
    public MappingJacksonValue getTableInfo(
        @PathVariable(name = "table_name") String tableName
    ) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            // read the table metadata from json file
            Map<?, ?> map = mapper.readValue(Paths.get(String.format("./tables/%s.json", tableName)).toFile(), Map.class);
            TableProperties tableProperties = new TableProperties (
                    tableName,
                    (String) map.get("description"),
                    (HashMap) map.get("data_model"));
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
        try {
            // Execute sql query
            Session session = hibernateUtil.newTransaction();
            NativeQuery<String> query = session.createSQLQuery(String.format("Select json_data from %s;",tableName));
            List<String> rawRecords = query.getResultList();
            hibernateUtil.endTransaction(session);

            // Process results
            ObjectMapper mapper = new ObjectMapper();
            ArrayNode processedRecords = mapper.createArrayNode();
            for (String rawRecord : rawRecords) {
                JsonNode processedRecord = mapper.readTree(rawRecord);
                processedRecords.add(processedRecord);
            }

            Map<?, ?> map = mapper.readValue(Paths.get(String.format("./tables/%s.json", tableName)).toFile(), Map.class);
            TableProperties tableProperties = new TableProperties (
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