package org.ga4gh.starterkit.dataconnect.model;

import com.fasterxml.jackson.annotation.JsonFilter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;

@Setter
@Getter
@NoArgsConstructor
@JsonFilter("tableInfoFilter")
public class TableProperties {

    private String name;
    private String description;
    private DataModelRef dataModelRef;
    private HashMap dataModel;

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
        this.dataModelRef = new DataModelRef(ref);
    }

    public TableProperties(String name, String description, HashMap dataModel) {
        this.name = name;
        this.description= description;
        this.dataModel = dataModel;
    }
}