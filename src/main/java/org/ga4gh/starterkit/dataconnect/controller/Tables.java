package org.ga4gh.starterkit.dataconnect.controller;

import org.ga4gh.starterkit.dataconnect.model.ListTablesResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tables")
public class Tables {

    @GetMapping
    public String listTables() {
        return "You hit the /tables endpoint. This method is a stub.";
    }

    @GetMapping(path = "/{table_name}/info")
    public String getTableInfo(
        @PathVariable(name = "table_name") String tableName
    ) {
        return "You hit the /tables/{table_name}/info endpoint."
            + " This method is a stub."
            + " You requested the table: '" + tableName + "'";
    }

    @GetMapping(path = "/{table_name}/data")
    public String getTableData(
        @PathVariable(name = "table_name") String tableName
    ) {
        return "You hit the /tables/{table_name}/data endpoint."
            + " This method is a stub."
            + " You requested the table: '" + tableName + "'";
    }
}
