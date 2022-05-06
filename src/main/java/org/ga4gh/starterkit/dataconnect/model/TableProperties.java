package org.ga4gh.starterkit.dataconnect.model;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;

@Setter
@Getter
@NoArgsConstructor
@JsonFilter("tablePropertiesFilter")
@JsonPropertyOrder({ "name", "description", "data_model", "data" })
public class TableProperties {

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("data_model")
    private Object dataModel;

    @JsonProperty("data")
    private Object data;

    public class DataModelRef {
        private String $ref;

        public DataModelRef(String $ref) {
            this.$ref = $ref;
        }

        public String get$ref() {
            return $ref;
        }

        public void set$ref(String $ref) {
            this.$ref = $ref;
        }
    }

    // Constructors
    public TableProperties(String name, String description, String ref) {
        this.name = name;
        this.description= description;
        this.dataModel = new DataModelRef(ref);
    }

    public TableProperties(String name, String description, HashMap dataModel) {
        this.name = name;
        this.description= description;
        this.dataModel = dataModel;
    }

    public TableProperties(HashMap dataModel, Object data) {
        this.dataModel = dataModel;
        this.data = data;
    }
}