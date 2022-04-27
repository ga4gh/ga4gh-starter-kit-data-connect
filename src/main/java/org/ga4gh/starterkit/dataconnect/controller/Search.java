package org.ga4gh.starterkit.dataconnect.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search")
public class Search {

    @PostMapping
    public String search() {
        return "You hit the search endpoint. This method is a stub.";
    }
}
