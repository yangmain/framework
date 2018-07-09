package com.rnkrsoft.framework.orm.log4jdbc.sql.jdbcapi;


import com.rnkrsoft.framework.orm.log4jdbc.Properties;
import com.rnkrsoft.framework.orm.log4jdbc.log.SpyLogDelegator;
import com.rnkrsoft.framework.orm.log4jdbc.log.SpyLogFactory;
import com.rnkrsoft.framework.orm.log4jdbc.sql.Spy;
import com.rnkrsoft.framework.orm.log4jdbc.sql.rdbmsspecifics.MySqlRdbmsSpecifics;
import com.rnkrsoft.framework.orm.log4jdbc.sql.rdbmsspecifics.OracleRdbmsSpecifics;
import com.rnkrsoft.framework.orm.log4jdbc.sql.rdbmsspecifics.RdbmsSpecifics;
import com.rnkrsoft.framework.orm.log4jdbc.sql.rdbmsspecifics.SqlServerRdbmsSpecifics;

import java.sql.*;
import java.util.*;
import java.util.logging.Logger;


public class DriverSpy implements Driver {
    /**
     * The last actual, underlying driver that was requested via a URL.
     */
    private Driver lastUnderlyingDriverRequested;

    /**
     * Maps driver class names to RdbmsSpecifics objects for each kind of
     * database.
     */
    private final static Map<String, RdbmsSpecifics> rdbmsSpecifics;

    /**
     * Default <code>RdbmsSpecifics</code>.
     */
    static final RdbmsSpecifics defaultRdbmsSpecifics = new RdbmsSpecifics();

    /**
     * A <code>SpyLogDelegator</code> used here for logs internal to log4jdbc
     * (see <code>debug(String)</code> method of <code>SpyLogDelegator</code>).
     */
    static final SpyLogDelegator log = SpyLogFactory.getSpyLogDelegator();

    static final private String log4jdbcUrlPrefix = "log4jdbc:";

    /**
     * Default constructor.
     */
    public DriverSpy() {

    }

    /**
     * Static initializer.
     */
    static {
        log.debug("DriverSpy intialization...");

        // The Set of drivers that the log4jdbc driver will preload at instantiation
        // time.  The driver can spy on any driver type, it's just a little bit
        // easier to configure log4jdbc if it's one of these types!
        Set<String> subDrivers = new TreeSet<String>();

        if (Properties.isAutoLoadPopularDrivers()) {
            subDrivers.add("oracle.jdbc.driver.OracleDriver");
            subDrivers.add("oracle.jdbc.OracleDriver");
            subDrivers.add("com.sybase.jdbc2.jdbc.SybDriver");
            subDrivers.add("net.sourceforge.jtds.jdbc.Driver");

            // MS driver for Sql Server 2000
            subDrivers.add("com.microsoft.jdbc.sqlserver.SQLServerDriver");

            // MS driver for Sql Server 2005
            subDrivers.add("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            subDrivers.add("weblogic.jdbc.sqlserver.SQLServerDriver");
            subDrivers.add("com.informix.jdbc.IfxDriver");
            subDrivers.add("org.apache.derby.jdbc.ClientDriver");
            subDrivers.add("org.apache.derby.jdbc.EmbeddedDriver");
            subDrivers.add("com.mysql.jdbc.Driver");
            subDrivers.add("org.postgresql.Driver");
            subDrivers.add("org.hsqldb.jdbcDriver");
            subDrivers.add("org.h2.Driver");
        }

        // look for additional driver specified in properties
        subDrivers.addAll(Properties.getAdditionalDrivers());

        try {
            DriverManager.registerDriver(new DriverSpy());
        } catch (SQLException s) {
            // this exception should never be thrown, JDBC just defines it
            // for completeness
            throw (RuntimeException) new RuntimeException("could not register log4jdbc driver!").initCause(s);
        }

        // instantiate all the supported drivers and remove
        // those not found
        String driverClass;
        for (Iterator<String> i = subDrivers.iterator(); i.hasNext(); ) {
            driverClass = i.next();
            try {
                Class.forName(driverClass);
                log.debug("  FOUND DRIVER " + driverClass);
            } catch (Throwable c) {
                i.remove();
            }
        }

        if (subDrivers.size() == 0) {
            log.debug("WARNING! log4jdbc couldn't find any underlying jdbc drivers.");
        }

        SqlServerRdbmsSpecifics sqlServer = new SqlServerRdbmsSpecifics();
        OracleRdbmsSpecifics oracle = new OracleRdbmsSpecifics();
        MySqlRdbmsSpecifics mySql = new MySqlRdbmsSpecifics();

        /** create lookup Map for specific rdbms formatters */
        rdbmsSpecifics = new HashMap<String, RdbmsSpecifics>();
        rdbmsSpecifics.put("oracle.jdbc.driver.OracleDriver", oracle);
        rdbmsSpecifics.put("oracle.jdbc.OracleDriver", oracle);
        rdbmsSpecifics.put("net.sourceforge.jtds.jdbc.Driver", sqlServer);
        rdbmsSpecifics.put("com.microsoft.jdbc.sqlserver.SQLServerDriver", sqlServer);
        rdbmsSpecifics.put("weblogic.jdbc.sqlserver.SQLServerDriver", sqlServer);
        rdbmsSpecifics.put("com.mysql.jdbc.Driver", mySql);

        log.debug("DriverSpy intialization done.");
    }

    /**
     * Get the RdbmsSpecifics object for a given Connection.
     *
     * @param conn JDBC connection to get RdbmsSpecifics for.
     * @return RdbmsSpecifics for the given connection.
     */
    static RdbmsSpecifics getRdbmsSpecifics(Connection conn) {
        String driverName = "";
        try {
            DatabaseMetaData dbm = conn.getMetaData();
            driverName = dbm.getDriverName();
        } catch (SQLException s) {
            // silently fail
        }

        log.debug("driver name is " + driverName);

        RdbmsSpecifics r = rdbmsSpecifics.get(driverName);

        if (r == null) {
            return defaultRdbmsSpecifics;
        }
        return r;
    }

    /**
     * Get the major version of the driver.  This call will be delegated to the
     * underlying driver that is being spied upon (if there is no underlying
     * driver found, then 1 will be returned.)
     *
     * @return the major version of the JDBC driver.
     */
    @Override
    public int getMajorVersion() {
        if (lastUnderlyingDriverRequested == null) {
            return 1;
        }
        return lastUnderlyingDriverRequested.getMajorVersion();
    }

    /**
     * Get the minor version of the driver.  This call will be delegated to the
     * underlying driver that is being spied upon (if there is no underlying
     * driver found, then 0 will be returned.)
     *
     * @return the minor version of the JDBC driver.
     */
    @Override
    public int getMinorVersion() {
        if (lastUnderlyingDriverRequested == null) {
            return 0;
        }
        return lastUnderlyingDriverRequested.getMinorVersion();
    }

    /**
     * Report whether the underlying driver is JDBC compliant.  If there is no
     * underlying driver, false will be returned, because the driver cannot
     * actually do any work without an underlying driver.
     *
     * @return <code>true</code> if the underlying driver is JDBC Compliant;
     *         <code>false</code> otherwise.
     */
    @Override
    public boolean jdbcCompliant() {
        return lastUnderlyingDriverRequested != null &&
                lastUnderlyingDriverRequested.jdbcCompliant();
    }

    /**
     * Returns true if this is a <code>jdbc:log4</code> URL and if the URL is for
     * an underlying driver that this DriverSpy can spy on.
     *
     * @param url JDBC URL.
     *
     * @return true if this Driver can handle the URL.
     *
     * @throws SQLException if a database access error occurs
     */
    @Override
    public boolean acceptsURL(String url) throws SQLException {
        Driver d = getUnderlyingDriver(url);
        if (d != null) {
            lastUnderlyingDriverRequested = d;
            return true;
        }
        return false;
    }

    /**
     * JDBC URL为jdbc:log4jdbc
     * @param url jdbc:log4jdbc:mysql://localhost:80/
     * @return 具体驱动实现
     * @throws SQLException
     */
     Driver getUnderlyingDriver(String url) throws SQLException {
        if (url.startsWith(log4jdbcUrlPrefix)) {
            url = this.getRealUrl(url);

            Enumeration<Driver> e = DriverManager.getDrivers();

            Driver d;
            while (e.hasMoreElements()) {
                d = e.nextElement();

                if (d.acceptsURL(url)) {
                    return d;
                }
            }
        }
        return null;
    }

    /**
     * 获取真实的驱动链接
     * 例如：log4jdbc:jdbc:mysql://localhost:80 获取真实的为jdbc:mysql://localhost:80
     * @param url log4jdbc链接
     * @return jdbc链接
     */
    private String getRealUrl(String url) {
        return url.substring(log4jdbcUrlPrefix.length());
    }


    @Override
    public Connection connect(String url, java.util.Properties info) throws SQLException {
        Driver driver = getUnderlyingDriver(url);
        if (driver == null) {
            return null;
        }

        url = this.getRealUrl(url);

        lastUnderlyingDriverRequested = driver;
        //连接开始时间
        long beginTime = System.currentTimeMillis();
        //获取连接
        Connection connection = driver.connect(url, info);
        //无效的驱动创建连接失败
        if (connection == null) {
            throw new SQLException("invalid or unknown driver url: " + url);
        }
        //如果启动了
        ConnectionSpy connectionSpy = new ConnectionSpy(connection, System.currentTimeMillis() - beginTime, log);
        RdbmsSpecifics rdbmsSpecifics = null;
        //获取真实驱动对应的细节配置
        String driveClassName = driver.getClass().getName();
        if (driveClassName != null && driveClassName.length() > 0) {
            rdbmsSpecifics = DriverSpy.rdbmsSpecifics.get(driveClassName);
        }

        if (rdbmsSpecifics == null) {
            rdbmsSpecifics = defaultRdbmsSpecifics;
        }
        connectionSpy.setRdbmsSpecifics(rdbmsSpecifics);
        return connectionSpy;
    }

    /**
     *
     * @param url
     * @param info
     * @return
     * @throws SQLException
     */
    @Override
    public DriverPropertyInfo[] getPropertyInfo(String url, java.util.Properties info)throws SQLException {
        Driver driver = getUnderlyingDriver(url);
        if (driver == null) {
            return new DriverPropertyInfo[0];
        }

        lastUnderlyingDriverRequested = driver;
        return driver.getPropertyInfo(url, info);
    }

    protected void reportException(String methodCall, SQLException exception) {
        log.exceptionOccurred((Spy) this, methodCall, exception, null, -1L);
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        String methodCall = "getParentLogger()";
        try {
            return lastUnderlyingDriverRequested.getParentLogger();
        } catch (SQLFeatureNotSupportedException s) {
            reportException(methodCall, s);
            throw s;
        }
    }
}
