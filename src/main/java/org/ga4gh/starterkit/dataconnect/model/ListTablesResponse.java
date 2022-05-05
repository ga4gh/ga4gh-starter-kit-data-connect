package org.ga4gh.starterkit.dataconnect.model;

import com.fasterxml.jackson.annotation.JsonFilter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@JsonFilter("tableResponseFilter")
public class ListTablesResponse {
    private List<TableProperties> tables;

    // constructor
    public ListTablesResponse(List<TableProperties> tables) {
        this.tables = tables;
    }

}