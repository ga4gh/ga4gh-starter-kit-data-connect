package org.ga4gh.starterkit.dataconnect.app;

import org.ga4gh.starterkit.common.config.DatabaseProps;
import org.ga4gh.starterkit.common.config.ServerProps;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DataConnectServerYamlConfig {

    private ServerProps serverProps;
    private DatabaseProps databaseProps;

    public DataConnectServerYamlConfig() {
        serverProps = new ServerProps();
        databaseProps = new DatabaseProps();
    }
}
