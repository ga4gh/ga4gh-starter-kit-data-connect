package org.ga4gh.starterkit.dataconnect.model;

import java.util.ArrayList;
import java.util.InputMismatchException;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import org.apache.commons.lang3.StringUtils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
@Setter
@Getter
@NoArgsConstructor
public class SearchRequest {

    private String query;

    private ArrayList<Object> parameters;

    public String parameterizeQuery() {
        String parameterizedQuery = getQuery();
        int nQuestionMarks = StringUtils.countMatches(getQuery(), "?");
        if (nQuestionMarks != getParameters().size()) {
            throw new InputMismatchException("Incorrect number of parameters specified for the provided query.");
        }

        for (Object parameter : parameters) {
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
}
