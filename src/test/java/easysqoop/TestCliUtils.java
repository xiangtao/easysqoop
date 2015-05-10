package easysqoop;

import org.junit.Test;

import com.hardthing.easysqoop.Configration;
import com.hardthing.easysqoop.utils.CLIUtils;

public class TestCliUtils {
	
	
	@Test
	public void testParseArgs(){
		String argstr = "-dc com.mysql.jdbc.Driver -conn jdbc:mysql://192.168.73.130:3306/test "
			          + "-u root -p root -inpt user -outt user -columns id  -hdb source "
			          + "-partk dt,pf -loc /user/root/ -external";
		argstr = "-dc com.mysql.jdbc.Driver -conn jdbc:mysql://192.168.73.130:3306/test "
		          + "-u root -p root -inpt user -outt user1 -columns id  -hdb data_raw";
		
		argstr = "-dc com.mysql.jdbc.Driver -conn jdbc:mysql://192.168.73.130:3306/test "
		          + "-u root -p root -inpt user -columns id  -hdb data_raw";
	    String[] args = argstr.split("[ ]+");
	    Configration cfg = CLIUtils.parseArgs(args);
	    System.out.println(cfg);
	}

}
