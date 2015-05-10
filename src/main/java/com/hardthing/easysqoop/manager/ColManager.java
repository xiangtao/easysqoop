package com.hardthing.easysqoop.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hardthing.easysqoop.Configration;

public class ColManager {
	
	public static final Log LOG = LogFactory.getLog(
			ColManager.class.getName());
	
	private ConnManager connManager = null;
	
	public ColManager(ConnManager connManager){
		this.connManager = connManager;
	}

	public  Map<String, Integer> getColumnTypes(String tableName) throws SQLException{
		ResultSet rs = this.getConnection().getMetaData()
				            .getColumns(null, "", tableName, null); 
		Map<String, Integer> map = new HashMap<String, Integer>();  
		 while(rs.next()){ 
			 String columnName = rs.getString("COLUMN_NAME");//列名  
             int dataType  = rs.getInt("DATA_TYPE");//字段数据类型(对应java.sql.Types中的常量)  
             map.put(columnName, dataType);  
		 }
		return map;
	}
	
	public Map<String, Integer> getColumnTypesForQuery(String query) throws SQLException {
		Map<String, List<Integer>> colInfo = getColumnInfoForQuery(query);
	    if (colInfo == null) {
	      return null;
	    }
	    Map<String, Integer> colTypes = new HashMap<String, Integer>();
	    for (String s : colInfo.keySet()) {
	      List<Integer> info = colInfo.get(s);
	      colTypes.put(s, info.get(0));
	    }
	    return colTypes;
   }
	
	
	public  String [] getColumnNames(String tableName) throws SQLException{
		ResultSet rs = connManager.getConnection().getMetaData()
	            .getColumns(null, "", tableName, null); 
		List<String> lst = new ArrayList<String>();
		while(rs.next()){ 
		 String columnName = rs.getString("COLUMN_NAME");//列名  
		 lst.add(columnName);
		}
		return lst.toArray(new String[0]);
	}
	
	
	public String [] getColumnNamesForQuery(String query) {
		String rawQuery = query.replace(Configration.SUBSTITUTE_TOKEN, " (1 = 0) ");
	    return getColumnNamesForRawQuery(rawQuery);
	}
	
	public String [] getColumnNamesForRawQuery(String query) {
		ResultSet results;
	    try {
	      results = execute(query);
	    } catch (SQLException sqlE) {
	    	LOG.warn("Error executing statement: " + sqlE.toString(),
	        sqlE);
	      return null;
	    }

	    try {
	      int cols = results.getMetaData().getColumnCount();
	      ArrayList<String> columns = new ArrayList<String>();
	      ResultSetMetaData metadata = results.getMetaData();
	      for (int i = 1; i < cols + 1; i++) {
	        String colName = metadata.getColumnLabel(i);
	        if (colName == null || colName.equals("")) {
	          colName = metadata.getColumnName(i);
	          if (null == colName) {
	            colName = "_RESULT_" + i;
	          }
	        }
	        columns.add(colName);
	      }
	      return columns.toArray(new String[0]);
	    } catch (SQLException sqlException) {
	    	LOG.warn( "Error reading from database: "
	          + sqlException.toString(), sqlException);
	      return null;
	    } finally {
	      try {
	        results.close();
	        getConnection().commit();
	      } catch (SQLException sqlE) {
	    	  LOG.warn( "SQLException closing ResultSet: "
	          + sqlE.toString(), sqlE);
	      }
	    }
	    
	  }
	
	
	protected ResultSet execute(String stmt, Object... args) throws SQLException {
	    return execute(stmt, 1, args);
	}
	
	/**
	   * Executes an arbitrary SQL statement.
	   * @param stmt The SQL statement to execute
	   * @param fetchSize Overrides default or parameterized fetch size
	   * @return A ResultSet encapsulating the results or null on error
	   */
	  protected ResultSet execute(String stmt, Integer fetchSize, Object... args)
	      throws SQLException {
	    // Release any previously-open statement.

	    PreparedStatement statement = null;
	    statement = connManager.getConnection().prepareStatement(stmt,
	        ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
	    if (fetchSize != null) {
	      LOG.debug("Using fetchSize for next query: " + fetchSize);
	      statement.setFetchSize(fetchSize);
	    }
	    if (null != args) {
	      for (int i = 0; i < args.length; i++) {
	        statement.setObject(i + 1, args[i]);
	      }
	    }
	    return statement.executeQuery();
	  }
	  
	  public Map<String, List<Integer>> getColumnInfoForQuery(String query) {
	    // Manipulate the query to return immediately, with zero rows.
		  String rawQuery = query.replace(Configration.SUBSTITUTE_TOKEN, " (1 = 0) ");
	    return getColumnInfoForRawQuery(rawQuery);
	  }

	  protected Map<String, List<Integer>> getColumnInfoForRawQuery(String stmt) {
	    ResultSet results;
	    LOG.debug("Execute getColumnInfoRawQuery : " + stmt);
	    try {
	      results = execute(stmt);
	    } catch (SQLException sqlE) {
	    	LOG.warn( "Error executing statement: " + sqlE.toString(),
	        sqlE);
	      return null;
	    }

	    try {
	      Map<String, List<Integer>> colInfo = 
	          new HashMap<String, List<Integer>>();
	      int cols = results.getMetaData().getColumnCount();
	      ResultSetMetaData metadata = results.getMetaData();
	      for (int i = 1; i < cols + 1; i++) {
	        int typeId = metadata.getColumnType(i);
	        int precision = metadata.getPrecision(i);
	        int scale = metadata.getScale(i);
	        // If we have an unsigned int we need to make extra room by
	        // plopping it into a bigint
	        if (typeId == Types.INTEGER &&  !metadata.isSigned(i)){
	            typeId = Types.BIGINT;
	        }

	        String colName = metadata.getColumnLabel(i);
	        if (colName == null || colName.equals("")) {
	          colName = metadata.getColumnName(i);
	        }
	        List<Integer> info = new ArrayList<Integer>(3);
	        info.add(Integer.valueOf(typeId));
	        info.add(precision);
	        info.add(scale);
	        colInfo.put(colName, info);
	      }

	      return colInfo;
	    } catch (SQLException sqlException) {
	    	LOG.warn("Error reading from database: "
	        + sqlException.toString(), sqlException);
	      return null;
	    } finally {
	      try {
	        results.close();
	        getConnection().commit();
	      } catch (SQLException sqlE) {
	    	  LOG.warn(
	          "SQLException closing ResultSet: " + sqlE.toString(), sqlE);
	      }
	    }
	  }
	  
	 private  Connection getConnection() throws SQLException{
		 try {
			return connManager.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
	  }
	
}
