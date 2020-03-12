package pl.zbucki.generators.storage.modes;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pl.zbucki.generators.GeneratorsPlugin;
import pl.zbucki.generators.manager.GeneratorManager;
import pl.zbucki.generators.storage.Store;
import pl.zbucki.generators.storage.StoreMode;
import pl.zbucki.generators.util.Util;

@RequiredArgsConstructor
public class StoreMongoDB implements Store {

	private final GeneratorsPlugin plugin;
	private final GeneratorManager manager;

	private MongoClient mongoClient;
	private MongoDatabase mongoDatabase;
	@Getter
	private MongoCollection<Document> generatorsCollection;
	private int loadedData;

	@Override
	public void connect() {
		try {
                	this.mongoClient = new MongoClient(plugin.getSettings().MONGODB_HOST, plugin.getSettings().MONGODB_PORT);
                } catch (UnknownHostException e) {
                	System.out.println("Could not connect to database!");
                	e.printStackTrace();
	        	return;
                }
		this.mongoDatabase = this.mongoClient.getDatabase(plugin.getSettings().MONGODB_DATABASE);
		this.generatorsCollection = this.mongoDatabase.getCollection("generators");
		manager.getDataExecutor().execute(() -> {
			for (Document doc : this.generatorsCollection.find()) {
				manager.addGenerator(Util.locationFromString(doc.getString("location")),
						manager.getGeneratorData(doc.getString("generatorId")), false);
				loadedData++;
			}
		});
	}

	@Override
	public void disconnect() {
		manager.getPlacedGenerators().forEach((k, v) -> {
			manager.getDataExecutor().execute(() -> {
				Document doc = new Document("location", Util.locationToString(k));
				doc.append("generatorId", v.getGenerator().getId());
				((StoreMongoDB) plugin.getStore()).getGeneratorsCollection().replaceOne(
						Filters.eq("location", Util.locationToString(k)), doc,
						new ReplaceOptions().upsert(true));

			});
		});
		if (isConnected()) {
			mongoClient.close();
		}
	}

	@Override
	public boolean isConnected() {
		boolean connect;
		try {
			mongoClient.getAddress();
			connect = true;
		} catch (Exception e) {
			System.out.println("Database unavailable");
			mongoClient.close();
			connect = false;
		}
		return connect;
	}

	@Override
	public StoreMode getMode() {
		return StoreMode.MONGODB;
	}

	@Override
	public int loadedData() {
		return loadedData;
	}

}
