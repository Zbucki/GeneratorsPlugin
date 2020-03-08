package pl.zbucki.generators.storage;

public enum StoreMode {
	
	MONGODB,
	FLAT;
	
	public static StoreMode parseMode(String mode) {
		try {
			return valueOf(mode);
		}catch(Exception ex) {
			return FLAT;
		}
	}

}
