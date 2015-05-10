package easysqoop;

import java.sql.SQLException;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.hardthing.easysqoop.manager.ColManager;
import com.hardthing.easysqoop.manager.ConnManager;
import com.hardthing.easysqoop.manager.ConnUrl;

public class TestColManager {
	
	private ColManager colManager;
	@Before
	public void before(){
		final String driverClass = "com.mysql.jdbc.Driver";
		final ConnUrl connUrl = new ConnUrl();
		connUrl.setConnectString("jdbc:mysql://192.168.73.130:3306/test");
		connUrl.setUserName("root");
		connUrl.setPassword("root");
		ConnManager connManager = new ConnManager(driverClass,connUrl);
		colManager = new ColManager(connManager);
	}
	
	@Test
	public void testGetColumnTypes() throws SQLException{
		Map<String, Integer> map = colManager.getColumnTypes("user");
		System.out.println(map);
	}
	
	@Test
	public void testGetColumnNames() throws SQLException{
		String[]  strs = colManager.getColumnNames("user");
		for (String string : strs) {
			System.out.println(string);
		}
	}
	
	@Test
	public void testGetColumnTypesForQuery() throws SQLException{
		Map<String, Integer> map = colManager.getColumnTypesForQuery("select * from user where $CONDITIONS");
		System.out.println(map);
	}
	
	@Test
	public void testGetColumnNamesForQuery() throws SQLException{
		String[]  strs = colManager.getColumnNamesForQuery("select id as idd from user where $CONDITIONS");
		for (String string : strs) {
			System.out.println(string);
		}
	}
	

}
