package org.ga4gh.starterkit.dataconnect.config;

import java.util.Properties;

import org.ga4gh.starterkit.common.config.DatabaseProps;
import org.ga4gh.starterkit.common.config.DatabaseType;
import static org.ga4gh.starterkit.common.constant.DatabasePropsConstants.DEFAULT_CURRENT_SESSION_CONTEXT_CLASS;
import static org.ga4gh.starterkit.common.constant.DatabasePropsConstants.SQLITE_DRIVER_CLASS;
import static org.ga4gh.starterkit.dataconnect.constant.DataConnectDatabasePropsConstants.SQLITE_DIALECT;
import static org.ga4gh.starterkit.common.constant.DatabasePropsConstants.SQLITE_DATE_CLASS;
import static org.ga4gh.starterkit.common.constant.DatabasePropsConstants.POSTGRES_DRIVER_CLASS;
import static org.ga4gh.starterkit.common.constant.DatabasePropsConstants.POSTGRES_DIALECT;

public class DataConnectDatabaseProps extends DatabaseProps {

    public Properties getAllProperties() {
        Properties props = new Properties();

        // set common properties across any db type: url, username, password,
        // pool_size, show_sql, 
        props.setProperty("hibernate.connection.url", getUrl());

        if (!getUsername().equals("")) {
            props.setProperty("hibernate.connection.username", getUsername());
        }

        if (!getPassword().equals("")) {
            props.setProperty("hibernate.connection.password", getPassword());
        }

        props.setProperty("hibernate.connection.pool_size", getPoolSize());
        props.setProperty("hibernate.show_sql", getShowSQL());

        // set hardcoded properties: current_session_context_class
        props.setProperty("hibernate.current_session_context_class", DEFAULT_CURRENT_SESSION_CONTEXT_CLASS);
        
        // infer database type (ie sqlite or postgresql) from the db connection url
        DatabaseType dbtype = getDatabaseTypeFromUrl(getUrl());

        switch(dbtype) {
            case sqlite:
                assignSqliteProperties(props);
                break;
            case postgres:
                assignPostgresProperties(props);
                break;
        }

        return props;
    }

    private DatabaseType getDatabaseTypeFromUrl(String url) {

        if (url.startsWith("jdbc:sqlite")) {
            return DatabaseType.sqlite;
        }

        if (url.startsWith("jdbc:postgresql")) {
            return DatabaseType.postgres;
        }
        
        throw new IllegalArgumentException("Invalid JDBC URL: MUST be a valid 'sqlite' or 'postgresql' JDBC URL");
    }

    private void assignSqliteProperties(Properties props) {
        props.setProperty("hibernate.connection.driver_class", SQLITE_DRIVER_CLASS);
        props.setProperty("hibernate.dialect", SQLITE_DIALECT);
        props.setProperty("hibernate.connection.date_class", SQLITE_DATE_CLASS);
    }

    private void assignPostgresProperties(Properties props) {
        props.setProperty("hibernate.connection.driver_class", POSTGRES_DRIVER_CLASS);
        props.setProperty("hibernate.dialect", POSTGRES_DIALECT);
    }
}
