package net.qxcg.tail;

import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.bluetooth.BluetoothDevice;

/**
 * Bluetooth LE scanner singleton
 * 
 * Abstracts the process of scanning for Bluetooth LE devices.
 * 
 * Scanning: Call startScan(), then call stopScan() after an interval.
 * 
 * Querying discovered devices at any point in time: Call
 * getDiscoveredDevices().
 * 
 * @author cgcai
 *
 */
public class LeScanner {
	private static LeScanner instance;

	private BluetoothAdapter bta;
	private boolean btaScanning;
	private Set<LeDeviceInfo> seen;

	static {
		instance = null;
	}

	public static synchronized LeScanner getInstance() {
		if (instance == null) {
			instance = new LeScanner();
		}

		return instance;
	}

	private LeScanner() {
		bta = BluetoothAdapter.getDefaultAdapter();
		seen = new HashSet<>();

		btaScanning = false;
	}

	public synchronized boolean doScan(long duration,
			final LeScannerResultsCallback callback) {
		if (btaScanning) {
			// Cannot initiate a new scan when while an existing scan is
			// running.
			return false;
		}

		Timer terminationTimer = new Timer();

		btaScanning = true;
		bta.startLeScan(new LeScanCallback() {
			@Override
			public void onLeScan(BluetoothDevice device, int rssi,
					byte[] scanRecord) {
				LeDeviceInfo dev = new LeDeviceInfo(device.getName(), device
						.getAddress(), System.currentTimeMillis(), rssi);
				updateSeen(dev);
			}
		});

		terminationTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				instance.stopScan();
				callback.onScanComplete(instance.getDiscoveredDevices());
			}
		}, duration);

		return true;
	}

	private synchronized void updateSeen(LeDeviceInfo dev) {
		if (seen.contains(dev)) {
			seen.remove(dev);
		}
		seen.add(dev);
	}

	private synchronized boolean stopScan() {
		if (!btaScanning) {
			return false;
		}

		bta.stopLeScan(new LeScanCallback() {
			@Override
			public void onLeScan(BluetoothDevice device, int rssi,
					byte[] scanRecord) {
				// Do nothing.
			}
		});
		btaScanning = false;

		return true;
	}

	public synchronized boolean isScanning() {
		return btaScanning;
	}

	public synchronized Set<LeDeviceInfo> getDiscoveredDevices() {
		return new HashSet<>(seen);
	}
}
