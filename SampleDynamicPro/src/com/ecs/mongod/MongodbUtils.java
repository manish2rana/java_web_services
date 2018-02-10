package com.ecs.mongod;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

public class MongodbUtils {

	private static MongodbUtils instance;

	private MongodbUtils() {
	}

	public static synchronized MongodbUtils getInstance() {
		if (instance == null) {
			instance = new MongodbUtils();
		}
		return instance;
	}

	public MongoClient getMongoClientInstance() {
		return new MongoClient("localhost", 27017);
	}

	public MongoDatabase getMongoDatabaseInstance(String db) {
		return getMongoClientInstance().getDatabase(db);
	}
}