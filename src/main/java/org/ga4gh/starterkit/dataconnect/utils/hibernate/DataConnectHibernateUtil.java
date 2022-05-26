package org.ga4gh.starterkit.dataconnect.utils.hibernate;

import org.ga4gh.starterkit.common.hibernate.HibernateEntity;
import org.ga4gh.starterkit.common.hibernate.HibernateUtil;
import org.hibernate.Session;

import javax.persistence.metamodel.EntityType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DataConnectHibernateUtil extends HibernateUtil {

    public <I extends Serializable, T extends HibernateEntity<I>> List<String> getEntityNames() {
        // return the list of entity names
        Session session = newTransaction();
        try {
            Set<EntityType<?>> entityNames = session.getMetamodel().getEntities();
            List<String> tablesList = new ArrayList<String>();
            for (EntityType e : entityNames) {
                tablesList.add(e.getName());
            }
            return tablesList;
        } catch (Exception ex) {
            // any errors need to be caught so the transaction can be closed
            endTransaction(session);
            throw ex;
        } finally {
            endTransaction(session);
        }
    }

}
