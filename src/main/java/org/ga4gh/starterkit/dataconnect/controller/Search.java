package org.ga4gh.starterkit.dataconnect.controller;

import java.util.InputMismatchException;
import java.util.List;

import org.ga4gh.starterkit.common.exception.BadRequestException;
import org.ga4gh.starterkit.dataconnect.model.SearchRequest;
import org.ga4gh.starterkit.dataconnect.utils.hibernate.DataConnectHibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search")
public class Search {

    @Autowired
    DataConnectHibernateUtil hibernateUtil;

    @PostMapping
    public String search(
        @RequestBody SearchRequest searchRequest
    ) {
        try {
            String parameterizedQuery = searchRequest.parameterizeQuery();
            Session session = hibernateUtil.newTransaction();

            System.out.println("---");
            System.out.println(hibernateUtil.getDatabaseProps().getAllProperties());
            System.out.println("---");

            System.out.println("A");
            // NativeQuery query = session.createSQLQuery("select json_data from one_thousand_genomes_sample");
            NativeQuery query = session.createSQLQuery("select json_extract(json_data, '$.sample_name') from one_thousand_genomes_sample");
            System.out.println("B");
            List list = query.getResultList();
            System.out.println("C");
            System.out.println(list);
            System.out.println(list.size());
            

            
        } catch (InputMismatchException e) {
            throw new BadRequestException(e.getMessage());
        }
        
        return "You hit the search endpoint. This method is a stub.";
    }
}
