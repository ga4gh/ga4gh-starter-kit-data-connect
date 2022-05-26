package org.ga4gh.starterkit.dataconnect.app;

import org.ga4gh.starterkit.common.config.ContainsServerProps;
import org.ga4gh.starterkit.common.config.ServerProps;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DataConnectServerYamlConfigContainer implements ContainsServerProps {

    private DataConnectServerYamlConfig dataConnectConfig;

    public DataConnectServerYamlConfigContainer() {
        dataConnectConfig = new DataConnectServerYamlConfig();
    }

    public ServerProps getServerProps() {
        return getDataConnectConfig().getServerProps();
    }
}
