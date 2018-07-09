package com.rnkrsoft.framework.orm.log4jdbc.sql.rdbmsspecifics;

import java.text.SimpleDateFormat;


public class MySqlRdbmsSpecifics extends RdbmsSpecifics {
    public MySqlRdbmsSpecifics() {
        super();
    }

    @Override
    public String formatParameterObject(Object object) {
        if (object instanceof java.sql.Time) {
            return "'" + new SimpleDateFormat("HH:mm:ss").format(object) + "'";
        } else if (object instanceof java.sql.Date) {
            return "'" + new SimpleDateFormat("yyyy-MM-dd").format(object) + "'";
        } else if (object instanceof java.util.Date){
            return "'" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(object) + "'";
        } else {
            return super.formatParameterObject(object);
        }
    }
}