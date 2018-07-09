package com.rnkrsoft.framework.orm.log4jdbc.sql;

/**
 * 监视接口
 */
public interface Spy {

    /**
     * Get the type of class being spied upon.  For example, "Statement", "ResultSet", etc.
     *
     * @return a description of the type of class being spied upon.
     */
    String getClassType();

    /**
     * Get the connection number.  In general, this is used to track which underlying connection is being
     * used from the database.  The number will be incremented each time a new Connection is retrieved from the
     * real underlying jdbc driver.  This is useful for debugging and tracking down problems with connection pooling.
     *
     * @return the connection instance number.
     */
    Integer getConnectionNumber();
}
