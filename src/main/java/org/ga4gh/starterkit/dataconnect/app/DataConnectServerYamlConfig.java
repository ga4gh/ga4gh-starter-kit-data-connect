package org.ga4gh.starterkit.dataconnect.app;

import org.ga4gh.starterkit.common.config.ServerProps;
import org.ga4gh.starterkit.dataconnect.config.DataConnectDatabaseProps;
import org.ga4gh.starterkit.dataconnect.model.DataConnectServiceInfo;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DataConnectServerYamlConfig {

    private ServerProps serverProps;
    private DataConnectDatabaseProps databaseProps;
    private DataConnectServiceInfo serviceInfo;

    public DataConnectServerYamlConfig() {
        serverProps = new ServerProps();
        databaseProps = new DataConnectDatabaseProps();
        serviceInfo = new DataConnectServiceInfo();
    }
}
