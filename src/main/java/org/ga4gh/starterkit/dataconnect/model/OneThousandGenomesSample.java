package org.ga4gh.starterkit.dataconnect.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import org.ga4gh.starterkit.common.hibernate.HibernateEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "one_thousand_genomes_sample")
@Setter
@Getter
@NoArgsConstructor
public class OneThousandGenomesSample implements HibernateEntity<String> {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @Column(name = "json_data", updatable = false, nullable = false)
    private String jsonData;

    public void loadRelations() {

    }
}