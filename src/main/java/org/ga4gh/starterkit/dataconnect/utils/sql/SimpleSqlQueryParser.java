package org.ga4gh.starterkit.dataconnect.utils.sql;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimpleSqlQueryParser {

    public static SimpleSqlQuery parse(String queryString) {
        SimpleSqlQuery simpleSqlQuery = new SimpleSqlQuery();

        parseFieldsAndTableName(queryString.toLowerCase(), simpleSqlQuery);
        parseWhereClauses(queryString, simpleSqlQuery);

        return simpleSqlQuery;
    }

    public static void parseFieldsAndTableName(String sqlQuery, SimpleSqlQuery simpleSqlQuery) {
        String regex = "^select (.+?) from ([^\\s]+).*;$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(sqlQuery);
        if (! matcher.matches()) {
            // TODO throw error here
        }

        String[] fields = matcher.group(1).replaceAll(" ", "").split(",");
        String tableName = matcher.group(2);
        simpleSqlQuery.setFields(fields);
        simpleSqlQuery.setTableName(tableName);
    }

    public static void parseWhereClauses(String sqlQuery, SimpleSqlQuery simpleSqlQuery) {
        String regex = "([^\\s]+?)\\s*?(=|>|<|>=|<=|<>|between|like|in)\\s*?('.+?'|[^\\s]+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(sqlQuery);
        while (matcher.find()) {
            SimpleSqlWhereClause whereClause = new SimpleSqlWhereClause();
            whereClause.setPosition(matcher.start());
            whereClause.setFieldName(matcher.group(1));
            whereClause.setOperation(matcher.group(2));
            whereClause.setFieldValue(matcher.group(3));
            whereClause.setFullClause(matcher.group(0));
            simpleSqlQuery.getWhereClauses().add(whereClause);
        }
    }
    
}
