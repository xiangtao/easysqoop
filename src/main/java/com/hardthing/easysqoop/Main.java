package com.hardthing.easysqoop;

import java.io.IOException;

import com.hardthing.easysqoop.manager.ColManager;
import com.hardthing.easysqoop.manager.ConnManager;
import com.hardthing.easysqoop.utils.CLIUtils;
/**
 * startup class 
 * @author taox
 */
public class Main {
	
	public static void main(String[] args) throws IOException {
		Configration cfg = newConfigration(args);
		ColManager colManager  = newColManager(cfg);
		CreateHiveTableTool tool = new CreateHiveTableTool(cfg,colManager);
		String ddl = tool.generateCreateHiveTable();
		System.out.println(ddl);
	}
	
	public static Configration newConfigration(String[] args){
		return CLIUtils.parseArgs(args);
	}
	public static ColManager newColManager(Configration cfg){
		ConnManager connManager = new ConnManager(cfg.getDriverClass(),cfg.getConnUrl());
		ColManager colManager = new ColManager(connManager);
		return colManager;
	}
}
