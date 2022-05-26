package org.ga4gh.starterkit.dataconnect.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.ga4gh.starterkit.common.hibernate.HibernateEntity;

@Entity(name = "phenopacket_v1")
@Setter
@Getter
@NoArgsConstructor
public class PhenopacketV1 implements HibernateEntity<String> {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @Column(name = "json_data", updatable = false, nullable = false)
    private String jsonData;

    public void loadRelations() {

    }

}