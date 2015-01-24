package net.qxcg.tail;

import java.util.Set;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends Activity {
	// Delays (ms)
	private final static int INITIAL_DELAY = 1000;
	private final static int SCAN_INTERVAL = 5000;
	private final static int SCAN_DURATION = 1000;

	private ScheduledThreadPoolExecutor threadPool = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		assertBLESupported();

		threadPool = new ScheduledThreadPoolExecutor(1 /* Pool Size */);
		schedulePeriodicLeScan();

	}

	private void assertBLESupported() {
		if (!getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_BLUETOOTH_LE)) {
			Log.e("BLE", "BLE not supported");

			finish();
		}
	}

	private void schedulePeriodicLeScan() {
		threadPool.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				LeScanner.getInstance().doScan(SCAN_DURATION, new LeScannerResultsCallback() {
					@Override
					public void onScanComplete(Set<LeDeviceInfo> devices) {
						processDiscoveredDevices(devices);
					}
				});
			}
		}, INITIAL_DELAY, SCAN_INTERVAL, TimeUnit.MILLISECONDS);
	}

	private void processDiscoveredDevices(Set<LeDeviceInfo> devices) {
		StringBuilder sb = new StringBuilder();
		for (LeDeviceInfo dev : devices) {
			sb.append(dev.toString());
			sb.append("\n");
		}
		final String text = sb.toString();

		new Handler(Looper.getMainLooper()).post(new Runnable() {
			@Override
			public void run() {
				TextView tv = (TextView) findViewById(R.id.textView1);
				tv.setText(text);
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
