package org.ga4gh.starterkit.dataconnect.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class TableProperties {

    private String name;
    private String description;
    private DataModelRef dataModelRef;

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

    // Constructor
    public TableProperties(String name, String description, String ref) {
        this.name = name;
        this.description= description;
        this.dataModelRef = new DataModelRef(ref);
    }

}