package zookeeper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

public class DataMonitorTest  {
	private static final String ZOOKEEPER_ADDR = "localhost:2181";

	@Before
	public void setUp() {
		
	}
	
	@Test
	public void getAndPut() {
		SideMap map = SideMap.getInstance();
		
		map.put("a", "data");
		assertTrue(map.containsKey("a"));
		assertEquals("data", map.get("a"));
	}
	
	@Test
	public void getAndPut2() {
		SideMap map = SideMap.getInstance();
		
		map.put("xx", "yyy");
		assertTrue(map.containsKey("xx"));
		assertEquals("yyy", map.get("xx"));
	}

	
	@Test
	public void remove() {
		SideMap map = SideMap.getInstance();
		
		map.put("uuuuu", "yyy");
		assertTrue(map.containsKey("uuuuu"));
		map.remove("uuuuu");
		assertFalse(map.containsKey("uuuuu"));
	}
	
	@Test
	public void remove2() {
		SideMap map = SideMap.getInstance();
		
		map.put("uuuuu", "yyy");
		assertTrue(map.containsKey("uuuuu"));
		map.remove("uuuuu");
		map.remove("uuuuu");
	}
	
	@Test
	public void addListener() throws InterruptedException {
		SideMap map = SideMap.getInstance();
		
		DataMonitorListener mock = mock(DataMonitorListener.class);
		
		map.put("data", "coolData");
		map.addListener("data", mock);
		Thread.sleep(100);
		verify(mock).exists("coolData");
		verify(mock, never()).closing(1);
	}
	
	
	@Test
	public void addListenerMultiple() throws InterruptedException {
		SideMap map = SideMap.getInstance();
		
		map.remove("data");
		DataMonitorListener mock = mock(DataMonitorListener.class);
		map.addListener("data", mock);
		
		map.put("data", "coolData");
		Thread.sleep(100);
		map.put("data", "coolData2");
		Thread.sleep(100);
		verify(mock).exists("coolData");
		verify(mock).exists("coolData2");
		verify(mock, never()).closing(1);
	}
	
	@Test
	public void addListenerMultipleSame() throws InterruptedException {
		SideMap map = SideMap.getInstance();

		map.remove("data");
		
		DataMonitorListener mock = mock(DataMonitorListener.class);
		map.addListener("data", mock);
		
		map.put("data", "coolData");
		map.put("data", "coolData");
		Thread.sleep(100);
		verify(mock).exists("coolData");
		verify(mock, never()).closing(1);
	}
	
	@Test
	public void testRemoveListener() throws InterruptedException {
		SideMap map = SideMap.getInstance();

		map.remove("data");
		
		DataMonitorListener mock = mock(DataMonitorListener.class);
		map.addListener("data", mock);
		
		map.put("data", "coolData");
		map.removeListener(mock);
		map.put("data", "coolData2");
		
		Thread.sleep(100);
		
		verify(mock).exists("coolData");
		verify(mock, never()).closing(1);
	}
	
	@Test
	public void testListenerChange() throws InterruptedException {
		SideMap map = SideMap.getInstance();

		map.remove("data");
		
		DataMonitorListener mock = mock(DataMonitorListener.class);
		map.addListener("data", mock);
		
		map.put("data", "coolData");
		map.put("data", "coolData2");
		map.put("data", "coolData");
		
		Thread.sleep(100);
		
		verify(mock, times(2)).exists("coolData");
		verify(mock).exists("coolData2");
		verify(mock, never()).closing(1);
	}
}
