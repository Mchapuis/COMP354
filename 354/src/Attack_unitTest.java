import static org.junit.Assert.*;

import org.junit.Test;

public class Attack_unitTest {

	@Test
	public void test() {
		// apply a status 
		 
	    Attack attack = new Attack();
	 
	    attack.setApplyStatus("paralyzed"); //cannot enter the status in capital form. 
	 
	  
	 
	    String statusApply = attack.getStatusToApply();
	 
	    
	 
	    assertEquals("PARALYZED", statusApply);
	 
	}

}
