package org.ga4gh.starterkit.dataconnect.utils.sql;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class SimpleSqlQuery {

    private String[] fields; // requestedFields

    private String tableName;

    public boolean allFieldsRequested() {
        return getFields().length == 1 && getFields()[0].equals("*");
    }

    public boolean singleFieldRequested() {
        return getFields().length == 1 && getFields()[0].equals("*") == false;
    }
}
