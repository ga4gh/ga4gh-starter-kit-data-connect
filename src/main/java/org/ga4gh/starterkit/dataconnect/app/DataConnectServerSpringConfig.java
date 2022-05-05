package org.ga4gh.starterkit.dataconnect.app;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.catalina.connector.Connector;
import org.apache.commons.cli.Options;
import org.ga4gh.starterkit.common.config.DatabaseProps;
import org.ga4gh.starterkit.common.config.ServerProps;
import org.ga4gh.starterkit.common.hibernate.HibernateEntity;
import org.ga4gh.starterkit.common.util.CliYamlConfigLoader;
import org.ga4gh.starterkit.common.util.DeepObjectMerger;
import org.ga4gh.starterkit.common.util.webserver.AdminEndpointsConnector;
import org.ga4gh.starterkit.common.util.webserver.AdminEndpointsFilter;
import org.ga4gh.starterkit.common.util.webserver.CorsFilterBuilder;
import org.ga4gh.starterkit.common.util.webserver.TomcatMultiConnectorServletWebServerFactoryCustomizer;
import org.ga4gh.starterkit.dataconnect.model.DataConnectServiceInfo;
import org.ga4gh.starterkit.dataconnect.model.ListTablesResponse;
import org.ga4gh.starterkit.dataconnect.model.OneThousandGenomesSample;
import org.ga4gh.starterkit.dataconnect.model.TableProperties;
import org.ga4gh.starterkit.dataconnect.utils.hibernate.DataConnectHibernateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.filter.CorsFilter;

@Configuration
@ConfigurationProperties
public class DataConnectServerSpringConfig {

    /* ******************************
     * TOMCAT SERVER
     * ****************************** */

    @Value("${server.admin.port:4501}")
    private String serverAdminPort;

    @Bean
    public WebServerFactoryCustomizer servletContainer() {
        Connector[] additionalConnectors = AdminEndpointsConnector.additionalConnector(serverAdminPort);
        ServerProperties serverProperties = new ServerProperties();
        return new TomcatMultiConnectorServletWebServerFactoryCustomizer(serverProperties, additionalConnectors);
    }

    @Bean
    public FilterRegistrationBean<AdminEndpointsFilter> adminEndpointsFilter() {
        return new FilterRegistrationBean<AdminEndpointsFilter>(new AdminEndpointsFilter(Integer.valueOf(serverAdminPort)));
    }

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter(
        @Autowired ServerProps serverProps
    ) {
        return new CorsFilterBuilder(serverProps).buildFilter();
    }

    /* ******************************
     * YAML CONFIG
     * ****************************** */

    @Bean
    public Options getCommandLineOptions() {
        final Options options = new Options();
        options.addOption("c", "config", true, "Path to YAML config file");
        return options;
    }

    @Bean
    @Scope(DataConnectServerConstants.PROTOTYPE)
    @Qualifier(DataConnectServerConstants.EMPTY_CONFIG_CONTAINER)
    public DataConnectServerYamlConfigContainer emptyConfigContainer() {
        return new DataConnectServerYamlConfigContainer();
    }

    @Bean
    @Qualifier(DataConnectServerConstants.DEFAULT_CONFIG_CONTAINER)
    public DataConnectServerYamlConfigContainer defaultConfigContainer() {
        return new DataConnectServerYamlConfigContainer();
    }

    @Bean
    @Qualifier(DataConnectServerConstants.USER_CONFIG_CONTAINER)
    public DataConnectServerYamlConfigContainer userConfigContainer(
        @Autowired ApplicationArguments args,
        @Autowired() Options options,
        @Qualifier(DataConnectServerConstants.EMPTY_CONFIG_CONTAINER) DataConnectServerYamlConfigContainer configContainer
    ) {
        DataConnectServerYamlConfigContainer userConfigContainer = CliYamlConfigLoader.load(DataConnectServerYamlConfigContainer.class, args, options, "config");
        if (userConfigContainer != null) {
            return userConfigContainer;
        }
        return configContainer;
    }

    @Bean
    @Qualifier(DataConnectServerConstants.FINAL_CONFIG_CONTAINER)
    public DataConnectServerYamlConfigContainer finalConfigContainer(
        @Qualifier(DataConnectServerConstants.DEFAULT_CONFIG_CONTAINER) DataConnectServerYamlConfigContainer defaultContainer,
        @Qualifier(DataConnectServerConstants.USER_CONFIG_CONTAINER) DataConnectServerYamlConfigContainer userContainer
    ) {
        DeepObjectMerger merger = new DeepObjectMerger();
        merger.merge(userContainer, defaultContainer);
        return defaultContainer;
    }

    @Bean
    public ServerProps getServerProps(
        @Qualifier(DataConnectServerConstants.FINAL_CONFIG_CONTAINER) DataConnectServerYamlConfigContainer configContainer
    ) {
        return configContainer.getDataConnectConfig().getServerProps();
    }

    @Bean
    public DatabaseProps getDatabaseProps(
        @Qualifier(DataConnectServerConstants.FINAL_CONFIG_CONTAINER) DataConnectServerYamlConfigContainer configContainer
    ) {
        return configContainer.getDataConnectConfig().getDatabaseProps();
    }

    @Bean
    public DataConnectServiceInfo getServiceInfo(
        @Qualifier(DataConnectServerConstants.FINAL_CONFIG_CONTAINER) DataConnectServerYamlConfigContainer configContainer
    ) {
        return configContainer.getDataConnectConfig().getServiceInfo();
    }

    /* ******************************
     * HIBERNATE CONFIG
     * ****************************** */

    @Bean
    public DataConnectHibernateUtil getHibernateUtil(
        @Autowired DatabaseProps databaseProps
    ) {
        List<Class<? extends HibernateEntity<? extends Serializable>>> annotatedClasses = new ArrayList<>() {{
            add(OneThousandGenomesSample.class);
        }};
        DataConnectHibernateUtil hibernateUtil = new DataConnectHibernateUtil();
        hibernateUtil.setAnnotatedClasses(annotatedClasses);
        hibernateUtil.setDatabaseProps(databaseProps);
        return hibernateUtil;
    }

    @Bean
    public TableProperties tableProperties(){
        TableProperties tableProperties = new TableProperties();
        return tableProperties;
    }

    @Bean
    public ListTablesResponse listTablesResponse(){
        ListTablesResponse listTablesResponse= new ListTablesResponse();
        return listTablesResponse;
    }
}
