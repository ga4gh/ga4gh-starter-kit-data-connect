package org.ga4gh.starterkit.dataconnect.controller;

import org.ga4gh.starterkit.dataconnect.model.DataConnectServiceInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/service-info")
public class ServiceInfo {

    @Autowired
    private DataConnectServiceInfo serviceInfo;

    @GetMapping
    public DataConnectServiceInfo getServiceInfo() {
        return serviceInfo;
    }
}
