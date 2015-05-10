package com.hardthing.easysqoop.utils;

import com.hardthing.easysqoop.Configration;
import com.hardthing.easysqoop.manager.ConnUrl;

public class CLIUtils {
	Configration cfg = null;
	
	// -dc com.mysql.jdbc.Driver -conn jdbc:mysql://localhost:3306/test 
	// -u root -p root -inpt user -outt user -hdb source -partk dt,pf -loc -external
	public static Configration parseArgs(String[] args) {
		if(args == null) return null;
		Configration cfg = new Configration();
		ConnUrl connUrl = new ConnUrl();
		for (int i = 0; i < args.length; i++) {
			if(args[i].equals("-dc")){
				cfg.setDriverClass(args[++i]);
			}else if(args[i].equals("-conn")){
				connUrl.parseConnUrl(args[++i]);
			}else if(args[i].equals("-u")){
				connUrl.setUserName(args[++i]);
			}else if(args[i].equals("-p")){
				connUrl.setPassword(args[++i]);
			}else if(args[i].equals("-inpt")){
				cfg.setInputTableName(args[++i]);
			}else if(args[i].equals("-outt")){
				cfg.setOutputTableName(args[++i]);
			}else if(args[i].equals("-hdb")){
				cfg.setHiveDB(args[++i]);
			}else if(args[i].equals("-qry")){
				cfg.setQuery(args[++i].toUpperCase());
				//TO DO
			}else if(args[i].equals("-columns")){
				cfg.setColumns(args[++i]);
			}else if(args[i].equals("-where")){
				cfg.setWhere(args[++i]);
			}else if(args[i].equals("-partk")){
				String parkeys = args[++i];
				cfg.setPartitionKeys(parkeys.split(","));
			}else if(args[i].equals("-loc")){
				cfg.setHdfsLocation(args[++i]);
			}else if(args[i].equals("-external")){
				cfg.setExternal(true);
			}
		}
		cfg.setConnUrl(connUrl);
		
		
		//validate args
		//==================
		validateArgs(cfg);
		
		String colWhereQuery = cfg.getColumnWhereQuery();
		if(colWhereQuery!=null){
			cfg.setQuery(colWhereQuery);
		}
		if(cfg.getOutputTableName() == null){
			cfg.setOutputTableName(cfg.getInputTableName());
		}
		//==================
		return cfg;
	}

	private static void validateArgs(Configration cfg) {
		if(cfg.getDriverClass() == null){
			printHelp();
			throw new RuntimeException("ARGS NOT CORRECT");
		}
		if(cfg.getConnUrl().getConnectString() == null){
			printHelp();
			throw new RuntimeException("ARGS NOT CORRECT");
		}
		if(cfg.getConnUrl().getUserName() == null){
			printHelp();
			throw new RuntimeException("ARGS NOT CORRECT");
		}
		if(cfg.getConnUrl().getPassword() == null){
			printHelp();
			throw new RuntimeException("ARGS NOT CORRECT");
		}
		if(cfg.getInputTableName() == null){
			printHelp();
			throw new RuntimeException("ARGS NOT CORRECT");
		}
	}

	private static void printHelp() {
		String argstr = "-dc <com.mysql.jdbc.Driver> -conn <jdbc:mysql://192.168.73.130:3306/test> "
		          + "-u <root> -p <root> -inpt <user> [-outt user] [-columns id] [-where 'id>1'] [-hdb source] "
		          + "[-partk dt,pf] [-loc /user/root/] [-external]";
		System.err.println("Usage: Mian " + argstr);
	}

	public static void main(String[] args) {
		System.out.println(CLIUtils.parseArgs(args));
	}
}
