package com.hardthing.easysqoop;

import java.util.Arrays;

import com.hardthing.easysqoop.manager.ConnUrl;

/**
 * @author taox
 */
public class Configration {
	
	public static final String SUBSTITUTE_TOKEN = "$CONDITIONS";
	
	private String driverClass;
	private ConnUrl connUrl;
	
	private String hiveDB="default";
	private String query;
	private String inputTableName;
	private String columns;
	private String where;
	private String outputTableName;
	public String[] partitionKeys; // comma seperate
	private String hdfsLocation;
	private boolean isExternal = false; //default
	
	
	public String getColumnWhereQuery(){
		StringBuilder sb = new StringBuilder();
		if(columns != null && !"".equals(columns)){
			sb.append("select ")
			.append(columns)
			.append(" from ")
			.append(inputTableName);
			if(where != null && !"".equals(where)){
				sb.append(" where ")
				.append(where)
				.append(" and ")
				.append(SUBSTITUTE_TOKEN);
			}else{
				sb.append(" where ")
				.append(SUBSTITUTE_TOKEN);
			}
			
		}else if(where != null && !"".equals(where)){
			sb.append("select * from ")
			.append(inputTableName)
			.append(" where ")
			.append(where)
			.append(" and ")
			.append(SUBSTITUTE_TOKEN);
		}
		return "".equals(sb.toString())?null:sb.toString();
	}
	
	public String getInputTableName() {
		return inputTableName;
	}
	public void setInputTableName(String inputTableName) {
		this.inputTableName = inputTableName;
	}
	public String getOutputTableName() {
		return outputTableName;
	}
	public void setOutputTableName(String outputTableName) {
		this.outputTableName = outputTableName;
	}
	public String[] getPartitionKeys() {
		return partitionKeys;
	}
	public void setPartitionKeys(String[] partitionKeys) {
		this.partitionKeys = partitionKeys;
	}
	public String getHdfsLocation() {
		return hdfsLocation;
	}
	public void setHdfsLocation(String hdfsLocation) {
		this.hdfsLocation = hdfsLocation;
	}
	public boolean isExternal() {
		return isExternal;
	}
	public void setExternal(boolean isExternal) {
		this.isExternal = isExternal;
	}
	public String getDriverClass() {
		return driverClass;
	}
	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}
	public ConnUrl getConnUrl() {
		return connUrl;
	}
	public void setConnUrl(ConnUrl connUrl) {
		this.connUrl = connUrl;
	}
	public String getHiveDB() {
		return hiveDB;
	}
	public void setHiveDB(String hiveDB) {
		this.hiveDB = hiveDB;
	}
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}

	public String getColumns() {
		return columns;
	}

	public void setColumns(String columns) {
		this.columns = columns;
	}

	public String getWhere() {
		return where;
	}

	public void setWhere(String where) {
		this.where = where;
	}

	@Override
	public String toString() {
		return "Configration [driverClass=" + driverClass + ", connUrl="
				+ connUrl + ", hiveDB=" + hiveDB + ", query=" + query
				+ ", inputTableName=" + inputTableName + ", columns=" + columns
				+ ", where=" + where + ", outputTableName=" + outputTableName
				+ ", partitionKeys=" + Arrays.toString(partitionKeys)
				+ ", hdfsLocation=" + hdfsLocation + ", isExternal="
				+ isExternal + "]";
	}
	

}
