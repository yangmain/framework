package com.rnkrsoft.framework.orm.log4jdbc.log;

import com.rnkrsoft.framework.orm.log4jdbc.sql.Spy;
import com.rnkrsoft.framework.orm.log4jdbc.sql.resultsetcollector.ResultSetCollector;


public interface SpyLogDelegator {
    /**
     * Determine if any of the jdbc or sql loggers are turned on.
     *
     * @return true if any of the jdbc or sql loggers are enabled at error level or higher.
     */
     boolean isJdbcLoggingEnabled();

    /**
     * Called when a spied upon method throws an Exception.
     *
     * @param spy        the Spy wrapping the class that threw an Exception.
     * @param methodCall a description of the name and call parameters of the method generated the Exception.
     * @param e          the Exception that was thrown.
     * @param sql        optional sql that occured just before the exception occured.
     * @param execTime   optional amount of time that passed before an exception was thrown when sql was being executed.
     *                   caller should pass -1 if not used
     */
     void exceptionOccurred(Spy spy, String methodCall, Exception e, String sql, long execTime);

    /**
     * Called when spied upon method call returns.
     *
     * @param spy        the Spy wrapping the class that called the method that returned.
     * @param methodCall a description of the name and call parameters of the method that returned.
     * @param returnMsg  return value converted to a String for integral types, or String representation for Object
     *                   return types this will be null for void return types.
     */
     void methodReturned(Spy spy, String methodCall, String returnMsg);

    /**
     * Called when a spied upon object is constructed.
     *
     * @param spy              the Spy wrapping the class that called the method that returned.
     * @param constructionInfo information about the object construction
     */
     void constructorReturned(Spy spy, String constructionInfo);

    /**
     * Special call that is called only for JDBC method calls that contain SQL.
     *
     * @param spy        the Spy wrapping the class where the SQL occurred.
     * @param methodCall a description of the name and call parameters of the method that generated the SQL.
     * @param sql        sql that occurred.
     */
     void sqlOccurred(Spy spy, String methodCall, String sql);

    /**
     * Similar to sqlOccured, but reported after SQL executes and used to report timing stats on the SQL
     *
     * @param spy the    Spy wrapping the class where the SQL occurred.
     * @param execTime   how long it took the sql to run, in msec.
     * @param methodCall a description of the name and call parameters of the method that generated the SQL.
     * @param sql        sql that occurred.
     */
     void sqlTimingOccurred(Spy spy, long execTime, String methodCall, String sql);


    /**
     * Called whenever a new connection spy is created.
     *
     * @param spy ConnectionSpy that was created.
     * @param execTime  A <code>long</code> defining the time elapsed to open the connection in ms
     *          (useful information, as a connection might take some time to be opened sometimes). 
     *                    Caller should pass -1 if not used or unknown.
     */
     void connectionOpened(Spy spy, long execTime);

    /**
     * Called whenever a connection spy is closed.
     *
     * @param spy     <code>ConnectionSpy</code> that was closed.
     * @param execTime  A <code>long</code> defining the time elapsed to close the connection in ms
     *          (useful information, as a connection might take some time to be closed sometimes). 
     *                    Caller should pass -1 if not used or unknown.
     */
     void connectionClosed(Spy spy, long execTime);

    /**
     * Called whenever a connection spy is aborted.
     *
     * @param spy     <code>ConnectionSpy</code> that was aborted.
     * @param execTime  A <code>long</code> defining the time elapsed to abort the connection in ms
     *          (useful information, as a connection might take some time to be aborted sometimes). 
     *                    Caller should pass -1 if not used or unknown.
     */
     void connectionAborted(Spy spy, long execTime);

    /**
     * Log a Setup and/or administrative log message for log4jdbc.
     *
     * @param msg message to log.
     */
     void debug(String msg);

    /**
     * Determine whether the logger is expecting results sets to be collected
     *
     * @return true if the logger is expecting results sets to be collected
     */
     boolean isResultSetCollectionEnabled();

    /**
     * Determine whether the logger is expecting results sets to be collected
     * AND any unread result set values read explicitly  
     *
     * @return true if the logger is expecting results sets to be collected
     */
     boolean isResultSetCollectionEnabledWithUnreadValueFillIn();

    /**
     * Called whenever result set has been collected.
     *
     * This method will be actually called
     * when the <code>next()</code> method of the spied <code>ResultSet</code>
     * return <code>false</code> meaning that its end is reached.
     * It will be also called if the <code>ResultSet</code> is closed. 
     *
     * ResultSetSpy
     * DefaultResultSetCollector

     */
     void resultSetCollected(ResultSetCollector resultSetCollector);

}