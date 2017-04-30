package zookeeper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

public class SideMap implements Watcher {
	static final String ZOOKEEPER_ADDR = "137.112.89.141:2181";
	private static SideMap map;
	private ZooKeeper zookeeper;
	private Map<DataMonitorListener, DataMonitor> listeners;

	private SideMap(String connectStr) {
		try {
			this.zookeeper = new ZooKeeper(connectStr, 2181, this);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		listeners = new HashMap<>();
		this.put("", "-");
	}

	public boolean containsKey(String key) {
		try {
			return this.zookeeper.exists(getChildZnode(key), false) != null;
		} catch (KeeperException | InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	public String get(String key) {
		try {
			if (this.containsKey(key)) {
				byte[] data = this.zookeeper.getData(getChildZnode(key), false, null);
				return new String(data);
			}
			return null;
		} catch (InterruptedException | KeeperException e) {
			throw new RuntimeException(e);
		}
	}

	public void put(String key, String data) {
		try {
			if (!this.containsKey(key)) {
				this.zookeeper.create(getChildZnode(key), data.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			} else {
				this.zookeeper.setData(getChildZnode(key), data.getBytes(),
						this.zookeeper.exists(getChildZnode(key), true).getVersion());
			}
		} catch (InterruptedException | KeeperException e) {
			throw new RuntimeException(e);
		}
	}

	private String getChildZnode(String key) {
		return "/" + key;
	}

	public void remove(String key) {
		try {
			if (this.containsKey(key))
				this.zookeeper.delete(getChildZnode(key), this.zookeeper.exists(getChildZnode(key), true).getVersion());
		} catch (InterruptedException | KeeperException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void process(WatchedEvent event) {
		System.out.println("event: " + event);
		for (DataMonitor m : this.listeners.values()) {
			m.process(event);
		}
	}

	public static SideMap getInstance() {
		if (map == null) {
			synchronized (SideMap.class) {
				if (map == null) {
					map = new SideMap(ZOOKEEPER_ADDR);
				}
			}
		}
		return map;
	}

	public void addListener(String key, DataMonitorListener dataMonitorListener) {
		listeners.put(dataMonitorListener,
				new DataMonitor(this.zookeeper, getChildZnode(key), null, dataMonitorListener));
	}

	public void removeListener(DataMonitorListener dataMonitorListener) {
		listeners.remove(dataMonitorListener);
	}
}
