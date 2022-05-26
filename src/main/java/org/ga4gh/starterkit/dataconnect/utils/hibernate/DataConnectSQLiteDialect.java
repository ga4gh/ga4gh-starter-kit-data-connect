package org.ga4gh.starterkit.dataconnect.utils.hibernate;

import org.ga4gh.starterkit.common.hibernate.dialect.SQLiteDialect;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.StandardBasicTypes;

public class DataConnectSQLiteDialect extends SQLiteDialect {

    public DataConnectSQLiteDialect() {
        super();
        registerFunction("json_extract",
            new StandardSQLFunction(
                "json_extract",
                StandardBasicTypes.STRING
            )
        );
    }
    
}
