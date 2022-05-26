package org.ga4gh.starterkit.dataconnect.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.ga4gh.starterkit.common.model.ServiceInfo;
import static org.ga4gh.starterkit.dataconnect.constant.DataConnectServiceInfoDefaults.ID;
import static org.ga4gh.starterkit.dataconnect.constant.DataConnectServiceInfoDefaults.NAME;
import static org.ga4gh.starterkit.dataconnect.constant.DataConnectServiceInfoDefaults.DESCRIPTION;
import static org.ga4gh.starterkit.dataconnect.constant.DataConnectServiceInfoDefaults.CONTACT_URL;
import static org.ga4gh.starterkit.dataconnect.constant.DataConnectServiceInfoDefaults.DOCUMENTATION_URL;
import static org.ga4gh.starterkit.dataconnect.constant.DataConnectServiceInfoDefaults.CREATED_AT;
import static org.ga4gh.starterkit.dataconnect.constant.DataConnectServiceInfoDefaults.UPDATED_AT;
import static org.ga4gh.starterkit.dataconnect.constant.DataConnectServiceInfoDefaults.ENVIRONMENT;
import static org.ga4gh.starterkit.dataconnect.constant.DataConnectServiceInfoDefaults.VERSION;
import static org.ga4gh.starterkit.dataconnect.constant.DataConnectServiceInfoDefaults.ORGANIZATION_NAME;
import static org.ga4gh.starterkit.dataconnect.constant.DataConnectServiceInfoDefaults.ORGANIZATION_URL;
import static org.ga4gh.starterkit.dataconnect.constant.DataConnectServiceInfoDefaults.SERVICE_TYPE_GROUP;
import static org.ga4gh.starterkit.dataconnect.constant.DataConnectServiceInfoDefaults.SERVICE_TYPE_ARTIFACT;
import static org.ga4gh.starterkit.dataconnect.constant.DataConnectServiceInfoDefaults.SERVICE_TYPE_VERSION;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class DataConnectServiceInfo extends ServiceInfo {

    public DataConnectServiceInfo() {
        super();
        setAllDefaults();
    }

    private void setAllDefaults() {
        setId(ID);
        setName(NAME);
        setDescription(DESCRIPTION);
        setContactUrl(CONTACT_URL);
        setDocumentationUrl(DOCUMENTATION_URL);
        setCreatedAt(CREATED_AT);
        setUpdatedAt(UPDATED_AT);
        setEnvironment(ENVIRONMENT);
        setVersion(VERSION);
        getOrganization().setName(ORGANIZATION_NAME);
        getOrganization().setUrl(ORGANIZATION_URL);
        getType().setGroup(SERVICE_TYPE_GROUP);
        getType().setArtifact(SERVICE_TYPE_ARTIFACT);
        getType().setVersion(SERVICE_TYPE_VERSION);
    }
}
