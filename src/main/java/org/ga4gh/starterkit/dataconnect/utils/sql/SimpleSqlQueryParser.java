package org.ga4gh.starterkit.dataconnect.utils.sql;

import java.util.ArrayList;

public class SimpleSqlQueryParser {

    public static SimpleSqlQuery parse(String sqlQuery) {
        SimpleSqlQuery simpleSqlQuery = new SimpleSqlQuery();

        String[] queryWords = sqlQuery.toLowerCase().split(" ");

        boolean selectFound = false;
        boolean fromFound = false;
        boolean whereFound = false;

        ArrayList<String> unprocessedRequestedFields = new ArrayList<>();
        String tableName = null;
        for (String queryWord : queryWords) {
            System.out.println(queryWord);
            if (queryWord.equals("select")) {
                selectFound = true;
            } else if (queryWord.equals("from")) {
                fromFound = true;
            } else if (queryWord.equals("where")) {
                whereFound = true;
            } else if (selectFound == true && fromFound == false) {
                unprocessedRequestedFields.add(queryWord);
            } else if (selectFound == true && fromFound == true && whereFound == false) {
                tableName = queryWord;
            }
        }

        String[] fields = String.join("", unprocessedRequestedFields).split(",");

        simpleSqlQuery.setFields(fields);
        simpleSqlQuery.setTableName(tableName);
        return simpleSqlQuery;
    }
    
}
