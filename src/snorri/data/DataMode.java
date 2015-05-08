package snorri.data;

public enum DataMode {
	
	SESSION("session"),
	ALL_TIME("all-time"),
	OFFLINE("offline");
	
	private final String value;
	
	private DataMode(String value) {
		this.value = value;
	}
	
	public String toString() {
		return value;
	}

}
