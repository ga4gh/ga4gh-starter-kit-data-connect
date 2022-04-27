package org.ga4gh.starterkit.dataconnect.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/service-info")
public class ServiceInfo {

    @GetMapping
    public String getServiceInfo() {
        return "You hit the /service-info endpoint. This method is a stub.";
    }
}
