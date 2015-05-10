package com.hardthing.easysqoop;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hardthing.easysqoop.manager.ColManager;

public class CreateHiveTableTool {
	public static final Log LOG = LogFactory.getLog(
		      CreateHiveTableTool.class.getName());
	
	private Configration cfg;
	private ColManager colManager;
	
	public CreateHiveTableTool(Configration cfg,ColManager colManager){
		this.cfg = cfg;
		this.colManager = colManager;
	}
	
	public String generateDropHiveTable() throws IOException{
		StringBuilder sb = new StringBuilder();
		sb.append("DROP TABLE ");
		String hiveDB = cfg.getHiveDB();
	    if(hiveDB!=null && !"".equals(hiveDB)){
	    	sb.append(cfg.getHiveDB());
			sb.append(".");
	    }
		sb.append(cfg.getOutputTableName());
		return sb.toString();
	}
	public String generateCreateHiveTable() throws IOException{
		StringBuilder sb = new StringBuilder();
		try {
			String query = cfg.getQuery();
			Map<String, Integer> columnTypes = null;
			String[] colNames = null;
			if(query!=null && !"".equals(query)){
				columnTypes = colManager.getColumnTypesForQuery(query);
				colNames = colManager.getColumnNamesForQuery(query);
			}else{
				columnTypes = colManager.getColumnTypes(cfg.getInputTableName());
				colNames = colManager.getColumnNames(cfg.getInputTableName());
			}
			
			if(cfg.isExternal()){
				sb.append("CREATE EXTERNAL TABLE IF NOT EXISTS `");
			}else{
				sb.append("CREATE TABLE IF NOT EXISTS `");
			}

		    String hiveDB = cfg.getHiveDB();
		    if(hiveDB!=null && !"".equals(hiveDB)){
		    	sb.append(hiveDB).append(".");
		    }
		    sb.append(cfg.getOutputTableName()).append("` ( ");
		    String[] partitionKeys = cfg.getPartitionKeys();
		    boolean first = true;
		    for (String col : colNames) {
		    	if (partitionKeys!=null && Arrays.asList(partitionKeys).contains(col)) {
		            throw new IllegalArgumentException("Partition key " + col + " cannot "
		                + "be a column to import.");
		          }
		    	
		      if (!first) {
		        sb.append(", ");
		      }
		      first = false;
		      Integer colType = columnTypes.get(col);
		      String hiveColType = HiveTypes.toHiveType(colType);
		      if (null == hiveColType) {
		        throw new IOException("Hive does not support the SQL type for column "
		            + col);
		      }

		      sb.append('`').append(col).append("` ").append(hiveColType);

		      if (HiveTypes.isHiveTypeImprovised(colType)) {
		        LOG.warn(
		            "Column " + col + " had to be cast to a less precise type in Hive");
		      }
		    }

		    sb.append(") ");
		    
		    if (partitionKeys != null && partitionKeys.length>0) {
		    	sb.append("PARTITIONED BY (");
		    	for (int i = 0; i < partitionKeys.length; i++) {
		    		sb.append(partitionKeys[i])
			        .append(" STRING ");
		    		if(i<partitionKeys.length-1) sb.append(",");
				}
		    	sb.append(")");
		     }
		    sb.append(" STORED AS TEXTFILE");
		    String hdfsLocation = cfg.getHdfsLocation();
		    if(hdfsLocation!=null && !"".equals(hdfsLocation)){
		    	sb.append(" LOCATION '" + hdfsLocation + "'");
		    }

		    LOG.debug("Create statement: " + sb.toString());
		    return sb.toString();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return sb.toString();
	}
}
