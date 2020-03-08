package pl.zbucki.generators.storage;

public interface Store {

	public void connect();

	public boolean isConnected();

	public StoreMode getMode();
	
	public void disconnect();
	
	public int loadedData();

}
