package zookeeper;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class DataMonitorTest  {
	private static final String ZOOKEEPER_ADDR = "localhost:2181";

	@Before
	public void setUp() {
		
	}
	
	@Test
	public void getAndPut() {
		SideMap map = new SideMap(ZOOKEEPER_ADDR, "/tea");
		
		map.put("a", "data");
		assertTrue(map.containsKey("a"));
		assertEquals("data", map.get("a"));
	}
	
	@Test
	public void getAndPut2() {
		SideMap map = new SideMap(ZOOKEEPER_ADDR, "/tea");
		
		map.put("xx", "yyy");
		assertTrue(map.containsKey("xx"));
		assertEquals("yyy", map.get("xx"));
	}
}
