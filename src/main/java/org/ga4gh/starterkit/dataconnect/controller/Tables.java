package org.ga4gh.starterkit.dataconnect.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.ga4gh.starterkit.dataconnect.model.ListTablesResponse;
import org.ga4gh.starterkit.dataconnect.model.TableProperties;
import org.ga4gh.starterkit.dataconnect.utils.hibernate.DataConnectHibernateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class Tables {

    @Autowired
    private DataConnectHibernateUtil hibernateUtil;

    @GetMapping(path="/tables")
    public MappingJacksonValue listTables() throws IOException {
        List<String> tableNames =  hibernateUtil.getEntityNames();
        ArrayList<TableProperties> tablePropertiesArrayList= new ArrayList<>();
        for (String tableName : tableNames) {
            ObjectMapper mapper = new ObjectMapper();
            // read the table metadata from json file
            // TODO: handle cases where metadata is not available for a table
            Map<?, ?> map = mapper.readValue(Paths.get(String.format("./tables/%s.json",tableName)).toFile(), Map.class);
            tablePropertiesArrayList.add(new TableProperties(tableName, (String)map.get("description"),String.format("table/%s/info",tableName)));
        }
        ListTablesResponse listTablesResponse = new ListTablesResponse(tablePropertiesArrayList);
        MappingJacksonValue mapping = new MappingJacksonValue(listTablesResponse);
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("tables");
        FilterProvider filters = new SimpleFilterProvider().addFilter("tableResponseFilter", filter);
        mapping.setFilters(filters);
        return mapping;
    }

    @GetMapping(path = "table/{table_name}/info")
    public String getTableInfo(
        @PathVariable(name = "table_name") String tableName
    ) {
        return "You hit the /tables/{table_name}/info endpoint."
            + " This method is a stub."
            + " You requested the table: '" + tableName + "'";
    }

    @GetMapping(path = "table/{table_name}/data")
    public String getTableData(
        @PathVariable(name = "table_name") String tableName
    ) {
        return "You hit the /tables/{table_name}/data endpoint."
            + " This method is a stub."
            + " You requested the table: '" + tableName + "'";
    }
}