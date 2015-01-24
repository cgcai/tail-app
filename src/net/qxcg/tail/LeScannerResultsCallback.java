package net.qxcg.tail;

import java.util.Set;

public interface LeScannerResultsCallback {
	public void onScanComplete(Set<LeDeviceInfo> devices);
}
