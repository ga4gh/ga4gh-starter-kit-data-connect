package org.ga4gh.starterkit.dataconnect.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.ga4gh.starterkit.common.hibernate.HibernateEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "one_thousand_genomes_sample")
@Setter
@Getter
@NoArgsConstructor
public class OneThousandGenomesSample implements HibernateEntity<String> {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    public void loadRelations() {

    }
}
