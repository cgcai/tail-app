package net.qxcg.tail;

/**
 * Represents information about a discovered Bluetooth LE device.
 * @author cgcai
 *
 */
public class LeDeviceInfo {
	private String name;
	private String macAddress;
	private long lastSeen;
	private int rssi;

	public LeDeviceInfo(String name, String macAddress, long lastSeen, int rssi) {
		super();
		this.name = name;
		this.macAddress = macAddress;
		this.lastSeen = lastSeen;
		this.rssi = rssi;
	}

	/**
	 * Gets the discovered name of the LE device.
	 * @return discovered name, or null
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the MAC address of the LE device.
	 * @return MAC address
	 */
	public String getMacAddress() {
		return macAddress;
	}

	/**
	 * Gets the time at which the LE device was last seen as a time stamp in milliseconds.
	 * @return last seen timestamp in milliseconds
	 */
	public long getLastSeen() {
		return lastSeen;
	}

	/**
	 * Gets the RSSI (signal strength) of the LE device when it was last seen.
	 * @return RSSI value
	 */
	public int getRssi() {
		return rssi;
	}
	
	/**
	 * Gets a metric representing the proximity of the LE device to this radio.
	 * 
	 * This metric is correlated with distance, but is _not_ a measure of distance.
	 * 
	 * Larger values => further.
	 * @return
	 */
	public int getProximityMetric() {
		// Negate RSSI value so that larger values represent more distant LE devices.
		return -1 * rssi;
	}

	@Override
	public String toString() {
		return "LeDeviceInfo [name=" + name + ", macAddress=" + macAddress
				+ ", lastSeen=" + lastSeen + ", rssi=" + rssi + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((macAddress == null) ? 0 : macAddress.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LeDeviceInfo other = (LeDeviceInfo) obj;
		if (macAddress == null) {
			if (other.macAddress != null)
				return false;
		} else if (!macAddress.equals(other.macAddress))
			return false;
		return true;
	}
}
