package com.hardthing.easysqoop.manager;

import java.util.Properties;

public class ConnUrl {
	
	private String connectString;
	private String userName;
	private String password;
	private Properties connectionParams;
	
	public Properties parseConnUrl(String connectStr){
		Properties pros = new Properties();
		if(connectStr == null) return pros;
		int questMarkIdx = connectStr.indexOf("?");
		if(questMarkIdx!=-1){
			connectString = connectStr.substring(0,questMarkIdx);
			String connParamsStr = connectStr.substring(questMarkIdx+1);
			String[] strArray = connParamsStr.split("&");
			for (int i = 0; i < strArray.length; i++) {
				String str = strArray[i];
				String[] keyVal = str.split("=");
				if(keyVal.length>=2)
				  pros.put(keyVal[0], keyVal[1]);
			}
		}else{
			connectString = connectStr;
		}
		return pros;
	}

	public String getConnectString() {
		return connectString;
	}

	public void setConnectString(String connectString) {
		this.connectString = connectString;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Properties getConnectionParams() {
		return connectionParams;
	}

	public void setConnectionParams(Properties connectionParams) {
		this.connectionParams = connectionParams;
	}

	@Override
	public String toString() {
		return "ConnUrl [connectString=" + connectString + ", userName="
				+ userName + ", password=" + password + ", connectionParams="
				+ connectionParams + "]";
	}
	
}
