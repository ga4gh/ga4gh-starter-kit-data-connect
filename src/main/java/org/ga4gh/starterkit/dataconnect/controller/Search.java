package org.ga4gh.starterkit.dataconnect.controller;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.ga4gh.starterkit.common.exception.BadRequestException;
import org.ga4gh.starterkit.dataconnect.model.SearchRequest;
import org.ga4gh.starterkit.dataconnect.model.TableData;
import org.ga4gh.starterkit.dataconnect.utils.hibernate.DataConnectHibernateUtil;
import org.ga4gh.starterkit.dataconnect.utils.sql.SimpleSqlQuery;
import org.ga4gh.starterkit.dataconnect.utils.sql.SimpleSqlQueryParser;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.apache.commons.lang3.StringUtils;

@RestController
@RequestMapping("/search")
public class Search {

    @Autowired
    DataConnectHibernateUtil hibernateUtil;

    @PostMapping
    public TableData search(
        @RequestBody SearchRequest searchRequest
    ) {
        try {
            // Parse and process input query
            String parameterizedQuery = parameterizeQuery(searchRequest);
            SimpleSqlQuery simpleSqlQuery = SimpleSqlQueryParser.parse(parameterizedQuery);
            String jsonifiedQuery = jsonifyQuery(simpleSqlQuery);

            // Execute processed query
            Session session = hibernateUtil.newTransaction();
            NativeQuery<String> query = session.createSQLQuery(jsonifiedQuery);
            List<String> rawRecords = query.getResultList();
            hibernateUtil.endTransaction(session);

            // Process results
            ObjectMapper mapper = new ObjectMapper();
            ArrayNode processedRecords = mapper.createArrayNode();
            for (String rawRecord : rawRecords) {
                // simple case, full object was requested
                if (simpleSqlQuery.allFieldsRequested()) {
                    JsonNode processedRecord = mapper.readTree(rawRecord);
                    processedRecords.add(processedRecord);
                // simple case, a single field was requested
                } else if (simpleSqlQuery.singleFieldRequested()) {
                    ObjectNode processedRecord = mapper.createObjectNode();
                    processedRecord.put(simpleSqlQuery.getFields()[0], rawRecord);
                    processedRecords.add(processedRecord);
                // complex case, assign field names and values for each
                // requested field
                } else {
                    ObjectNode processedRecord = mapper.createObjectNode();
                    ArrayNode rawRecordArray = (ArrayNode) mapper.readTree(rawRecord);

                    for (int i = 0; i < simpleSqlQuery.getFields().length; i++) {
                        processedRecord.put(simpleSqlQuery.getFields()[i], rawRecordArray.get(i));
                    }
                    processedRecords.add(processedRecord);
                }
            }

            // Return results
            TableData tableData = new TableData();
            tableData.setData(processedRecords);
            return tableData;
        } catch (InputMismatchException | JsonProcessingException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    private String parameterizeQuery(SearchRequest searchRequest) {
        String parameterizedQuery = searchRequest.getQuery();
        int nQuestionMarks = StringUtils.countMatches(searchRequest.getQuery(), "?");
        if (nQuestionMarks != searchRequest.getParameters().size()) {
            throw new InputMismatchException("Incorrect number of parameters specified for the provided query.");
        }

        for (Object parameter : searchRequest.getParameters()) {
            String replacementString = null;

            switch (parameter.getClass().getSimpleName()) {
                case "String":
                    replacementString = "'" + parameter + "'";
                    break;
                case "Integer":
                    replacementString = parameter.toString();
                    break;
                default:
                    throw new InputMismatchException("Unable to populate query string for parameter of this data type: " + parameter.getClass().getSimpleName());
            }

            parameterizedQuery = StringUtils.replaceOnce(parameterizedQuery, "?", replacementString);
        }
        return parameterizedQuery;
    }

    private String jsonifyQuery(SimpleSqlQuery simpleSqlQuery) {
        ArrayList<String> jsonExtractFields = new ArrayList<>();

        if (simpleSqlQuery.allFieldsRequested()) {
            jsonExtractFields.add("'$'");
        } else {
            for (String requestedField : simpleSqlQuery.getFields()) {
                jsonExtractFields.add("'$." + requestedField + "'");
            }
        }
        String requestedFieldsJsonExtract = "json_extract(json_data, " + String.join(",", jsonExtractFields)  + ")";

        return "select " + requestedFieldsJsonExtract + " from " + simpleSqlQuery.getTableName() + ";";
    }
}
