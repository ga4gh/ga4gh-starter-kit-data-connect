package org.ga4gh.starterkit.dataconnect.utils.sql;

import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SimpleSqlQuery {

    private String[] fields; // requestedFields

    private String tableName;

    private ArrayList<SimpleSqlWhereClause> whereClauses;

    public SimpleSqlQuery() {
        whereClauses = new ArrayList<>();
    }

    public boolean allFieldsRequested() {
        return getFields().length == 1 && getFields()[0].equals("*");
    }

    public boolean singleFieldRequested() {
        return getFields().length == 1 && getFields()[0].equals("*") == false;
    }
}
