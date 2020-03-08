package pl.zbucki.generators.storage.modes;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
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

	private MongoClient mongoDatabase;
	@Getter
	private MongoCollection<Document> generatorsCollection;
	private int loadedData;

	@Override
	public void connect() {
		this.generatorsCollection = this.mongoDatabase.getDatabase(plugin.getSettings().MONGODB_DATABASE)
				.getCollection("generators");
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
			mongoDatabase.close();
		}
	}

	@Override
	public boolean isConnected() {
		boolean connect;
		try {
			mongoDatabase.getAddress();
			connect = true;
		} catch (Exception e) {
			System.out.println("Database unavailable");
			mongoDatabase.close();
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
