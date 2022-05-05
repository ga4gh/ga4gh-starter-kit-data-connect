package org.ga4gh.starterkit.dataconnect.utils.sql;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class SimpleSqlWhereClause {

    private int position;
    private String fieldName;
    private String operation;
    private String fieldValue;
    private String fullClause;
}
