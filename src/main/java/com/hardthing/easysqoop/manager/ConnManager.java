package com.hardthing.easysqoop.manager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * manan
 * @author taox
 */
public class ConnManager {
	public static final Log LOG = LogFactory.getLog(
			ConnManager.class.getName());

		  private String jdbcDriverClass;
		  private Connection connection;
		  private ConnUrl connUrl;

		  public ConnManager(final String driverClass, final ConnUrl connUrl) {
		    this.jdbcDriverClass = driverClass;
		    this.connUrl = connUrl;
		  }
		  
		  public Connection getConnection() throws SQLException {
		    if (null == this.connection) {
		      this.connection = makeConnection();
		    }

		    return this.connection;
		  }

		  protected boolean hasOpenConnection() {
		    return this.connection != null;
		  }

		  /**
		   * Any reference to the connection managed by this manager is nulled.
		   * If doClose is true, then this method will attempt to close the
		   * connection first.
		   * @param doClose if true, try to close the connection before forgetting it.
		   */
		  public void discardConnection(boolean doClose) {
		    if (doClose && hasOpenConnection()) {
		      try {
		        this.connection.close();
		      } catch(SQLException sqe) {
		      }
		    }

		    this.connection = null;
		  }

		  public void close() throws SQLException {
		    discardConnection(true);
		  }

		  public String getDriverClass() {
		    return jdbcDriverClass;
		  }
		  
		  protected Connection makeConnection() throws SQLException {
			    Connection connection;
			    String driverClass = getDriverClass();
			    try {
			      Class.forName(driverClass);
			    } catch (ClassNotFoundException cnfe) {
			      throw new RuntimeException("Could not load db driver class: "
			          + driverClass);
			    }
			    String username = connUrl.getUserName();
			    String password = connUrl.getPassword();
			    String connectString = connUrl.getConnectString();
			    Properties connectionParams = connUrl.getConnectionParams();
			    if (connectionParams != null && connectionParams.size() > 0) {
			      LOG.debug("User specified connection params. "
			              + "Using properties specific API for making connection.");

			      Properties props = new Properties();
			      if (username != null) {
			        props.put("user", username);
			      }
			      if (password != null) {
			        props.put("password", password);
			      }
			      props.putAll(connectionParams);
			      connection = DriverManager.getConnection(connectString, props);
			    } else {
			      LOG.debug("No connection paramenters specified. "
			              + "Using regular API for making connection.");
			      if (username == null) {
			        connection = DriverManager.getConnection(connectString);
			      } else {
			        connection = DriverManager.getConnection(
			                        connectString, username, password);
			      }
			    }
			    // We only use this for metadata queries. Loosest semantics are okay.
			    connection.setTransactionIsolation(getMetadataIsolationLevel());
			    connection.setAutoCommit(false);
			    return connection;
			  }
		  
		  protected int getMetadataIsolationLevel() {
			    return Connection.TRANSACTION_READ_COMMITTED;
			  }
}
