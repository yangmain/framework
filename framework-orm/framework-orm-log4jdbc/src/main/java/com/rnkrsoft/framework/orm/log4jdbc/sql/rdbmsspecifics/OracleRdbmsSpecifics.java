package com.rnkrsoft.framework.orm.log4jdbc.sql.rdbmsspecifics;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OracleRdbmsSpecifics extends RdbmsSpecifics {
    public OracleRdbmsSpecifics() {
        super();
    }

    @Override
    public String formatParameterObject(Object object) {
        if (object instanceof Timestamp) {
            return "to_timestamp('" + new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS").format(object) + "', 'mm/dd/yyyy hh24:mi:ss.ff3')";
        } else if (object instanceof Date) {
            return "to_date('" + new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(object) + "', 'mm/dd/yyyy hh24:mi:ss')";
        } else {
            return super.formatParameterObject(object);
        }
    }
}