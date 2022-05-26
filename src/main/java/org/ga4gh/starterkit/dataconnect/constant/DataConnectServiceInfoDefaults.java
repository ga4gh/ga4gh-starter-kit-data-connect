package org.ga4gh.starterkit.dataconnect.constant;

import java.time.LocalDateTime;
import org.ga4gh.starterkit.common.constant.DateTimeConstants;

public class DataConnectServiceInfoDefaults {
    public static final String ID = "org.ga4gh.starterkit.dataconnect";
    public static final String NAME = "GA4GH Starter Kit Data Connect Service";
    public static final String DESCRIPTION = "Starter Kit implementation of the "
        + " Data Connect API specification. Gives researchers access to the data "
        + " model of given datasets/tables, and enables them to perform search "
        + " queries on the datasets using the model.";
    public static final String CONTACT_URL = "mailto:info@ga4gh.org";
    public static final String DOCUMENTATION_URL = "https://github.com/ga4gh/ga4gh-starter-kit-data-connect";
    public static final LocalDateTime CREATED_AT = LocalDateTime.parse("2022-04-27T09:00:00Z", DateTimeConstants.DATE_FORMATTER);
    public static final LocalDateTime UPDATED_AT = LocalDateTime.parse("2022-04-27T09:00:00Z", DateTimeConstants.DATE_FORMATTER);
    public static final String ENVIRONMENT = "test";
    public static final String VERSION = "0.1.0";
    public static final String ORGANIZATION_NAME = "Global Alliance for Genomics and Health";
    public static final String ORGANIZATION_URL = "https://ga4gh.org";
    public static final String SERVICE_TYPE_GROUP = "org.ga4gh";
    public static final String SERVICE_TYPE_ARTIFACT = "data-connect";
    public static final String SERVICE_TYPE_VERSION = "1.0.0";
}
