package zookeeper;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class DataMonitorTest  {
	private static final String ZOOKEEPER_ADDR = "137.112.89.141:2181";

	@Before
	public void setUp() {
		
	}
	
	@Test
	public void getAndPut() {
		SideMap map = new SideMap(ZOOKEEPER_ADDR, "/test/childfa");
		
		assertFalse(map.containsKey("a"));
		
		map.put("a", "data");
		assertTrue(map.containsKey("a"));
		assertEquals("data", map.get("a"));
	}
}
