package easysqoop;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

import com.hardthing.easysqoop.Configration;
import com.hardthing.easysqoop.CreateHiveTableTool;
import com.hardthing.easysqoop.Main;
import com.hardthing.easysqoop.manager.ColManager;

public class TestCreateHiveTableTool {

	private Configration cfg;
	private ColManager colManager;
	@Before
	public void before(){
		String argstr = "-dc com.mysql.jdbc.Driver -conn jdbc:mysql://192.168.73.130:3306/test "
		          + "-u root -p root -inpt user -outt user -columns id  -hdb source "
		          + "-partk dt,pf -loc /user/root/ -external";
				argstr = "-dc com.mysql.jdbc.Driver -conn jdbc:mysql://192.168.73.130:3306/test "
				          + "-u root -p root -inpt user -outt user1 ";
				
				//argstr = "-dc com.mysql.jdbc.Driver -conn jdbc:mysql://192.168.73.130:3306/test "
				//          + "-u root -p root -inpt user -columns id  -hdb data_raw";
		
		String[] args = argstr.split("[ ]+");
		cfg = Main.newConfigration(args);
		colManager  = Main.newColManager(cfg);
	}
	
	@Test
	public void testGenerateCreateHiveTable() throws SQLException, IOException{
		CreateHiveTableTool tool = new CreateHiveTableTool(cfg,colManager);
		String ddl = tool.generateCreateHiveTable();
		System.out.println(ddl);
	}
	
	
	@Test
	public void testGenerateDropHiveTable() throws SQLException, IOException{
		CreateHiveTableTool tool = new CreateHiveTableTool(cfg,colManager);
		String ddl = tool.generateDropHiveTable();
		System.out.println(ddl);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
