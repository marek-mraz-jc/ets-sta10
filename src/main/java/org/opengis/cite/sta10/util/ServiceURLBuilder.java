package org.opengis.cite.sta10.util;

import java.util.List;

import org.testng.Assert;

/**
 * Utility class that helps preparing the URL string for the targeted entity.
 */
public class ServiceURLBuilder {

	/**
	 * Build the URL String based on the entityType, parent EntityType and id, and the
	 * targeted property or query
	 * @param rootURI The base URL of the SensorThings Service
	 * @param parentEntityType The entity type of the parent entity
	 * @param parentId The parent entity id
	 * @param relationEntityType The entity type of the targeted entity
	 * @param property The targeted property or the query string
	 * @return The URL String created based on the input parameters
	 */
	public static String buildURLString(String rootURI, EntityType parentEntityType, Object parentId,
			EntityType relationEntityType, String property) {
		String urlString = rootURI;
		if (relationEntityType == null) {
			switch (parentEntityType) {
				case THING:
					urlString += "/Things";
					break;
				case LOCATION:
					urlString += "/Locations";
					break;
				case HISTORICAL_LOCATION:
					urlString += "/HistoricalLocations";
					break;
				case DATASTREAM:
					urlString += "/Datastreams";
					break;
				case SENSOR:
					urlString += "/Sensors";
					break;
				case OBSERVATION:
					urlString += "/Observations";
					break;
				case OBSERVED_PROPERTY:
					urlString += "/ObservedProperties";
					break;
				case FEATURE_OF_INTEREST:
					urlString += "/FeaturesOfInterest";
					break;
				default:
					Assert.fail("Entity type is not recognized in SensorThings API : " + parentEntityType);
					return null;
			}
			if (!isNone(parentId)) {
				urlString += "(" + key(parentId) + ")";
			}
		}
		else {
			switch (parentEntityType) {
				case THING:
					urlString += "/Things(" + key(parentId) + ")";
					switch (relationEntityType) {
						case LOCATION:
							urlString += "/Locations";
							break;
						case HISTORICAL_LOCATION:
							urlString += "/HistoricalLocations";
							break;
						case DATASTREAM:
							urlString += "/Datastreams";
							break;
						default:
							Assert.fail("Entity type relation is not recognized in SensorThings API : "
									+ parentEntityType + " and " + relationEntityType);
					}
					break;
				case LOCATION:
					urlString += "/Locations(" + key(parentId) + ")";
					switch (relationEntityType) {
						case THING:
							urlString += "/Things";
							break;
						case HISTORICAL_LOCATION:
							urlString += "/HistoricalLocations";
							break;
						default:
							Assert.fail("Entity type relation is not recognized in SensorThings API : "
									+ parentEntityType + " and " + relationEntityType);
					}
					break;
				case HISTORICAL_LOCATION:
					urlString += "/HistoricalLocations(" + key(parentId) + ")";
					switch (relationEntityType) {
						case THING:
							urlString += "/Thing";
							break;
						case LOCATION:
							urlString += "/Locations";
							break;
						default:
							Assert.fail("Entity type relation is not recognized in SensorThings API : "
									+ parentEntityType + " and " + relationEntityType);
					}
					break;
				case DATASTREAM:
					urlString += "/Datastreams(" + key(parentId) + ")";
					switch (relationEntityType) {
						case THING:
							urlString += "/Thing";
							break;
						case SENSOR:
							urlString += "/Sensor";
							break;
						case OBSERVATION:
							urlString += "/Observations";
							break;
						case OBSERVED_PROPERTY:
							urlString += "/ObservedProperty";
							break;
						default:
							Assert.fail("Entity type relation is not recognized in SensorThings API : "
									+ parentEntityType + " and " + relationEntityType);
					}
					break;
				case SENSOR:
					urlString += "/Sensors(" + key(parentId) + ")";
					switch (relationEntityType) {
						case DATASTREAM:
							urlString += "/Datastreams";
							break;
						default:
							Assert.fail("Entity type relation is not recognized in SensorThings API : "
									+ parentEntityType + " and " + relationEntityType);
					}
					break;
				case OBSERVATION:
					urlString += "/Observations(" + key(parentId) + ")";
					switch (relationEntityType) {
						case THING:
						case DATASTREAM:
							urlString += "/Datastream";
							break;
						case FEATURE_OF_INTEREST:
							urlString += "/FeatureOfInterest";
							break;
						default:
							Assert.fail("Entity type relation is not recognized in SensorThings API : "
									+ parentEntityType + " and " + relationEntityType);
					}
					break;
				case OBSERVED_PROPERTY:
					urlString += "/ObservedProperties(" + key(parentId) + ")";
					switch (relationEntityType) {
						case DATASTREAM:
							urlString += "/Datastreams";
							break;
						default:
							Assert.fail("Entity type relation is not recognized in SensorThings API : "
									+ parentEntityType + " and " + relationEntityType);
					}
					break;
				case FEATURE_OF_INTEREST:
					urlString += "/FeaturesOfInterest(" + key(parentId) + ")";
					switch (relationEntityType) {
						case OBSERVATION:
							urlString += "/Observations";
							break;
						default:
							Assert.fail("Entity type relation is not recognized in SensorThings API : "
									+ parentEntityType + " and " + relationEntityType);
					}
					break;
				default:
					Assert.fail("Entity type is not recognized in SensorThings API : " + parentEntityType);
			}
		}
		if (property != null) {
			if (property.indexOf('?') >= 0) {
				urlString += property;
			}
			else {
				urlString += "/" + property;
			}
		}
		return urlString;
	}

	/**
	 * Build the URL String based on a chain of entity type hierarchy and ids, and the
	 * targeted property or query
	 * @param rootURI The base URL of the SensorThings Service
	 * @param entityTypes The list of entity type chain
	 * @param ids The ids for the entity type chain
	 * @param property The targeted property or the query string
	 * @return The URL String created based on the input parameters
	 */
	public static String buildURLString(String rootURI, List<String> entityTypes, List<Object> ids, String property) {
		String urlString = rootURI;
		if (entityTypes.size() != ids.size() && entityTypes.size() != ids.size() + 1) {
			Assert.fail("There is problem with the path of entities!!!");
		}
		if (urlString.charAt(urlString.length() - 1) != '/') {
			urlString += '/';
		}
		for (int i = 0; i < entityTypes.size(); i++) {
			urlString += entityTypes.get(i);
			if (i < ids.size()) {
				if (ids.get(i) == null) {
					urlString += "/";
				}
				else {
					urlString += "(" + key(ids.get(i)) + ")/";
				}
			}
		}
		if (urlString.charAt(urlString.length() - 1) == '/') {
			urlString = urlString.substring(0, urlString.length() - 1);
		}
		if (property != null) {
			if (property.indexOf('?') >= 0) {
				urlString += property;
			}
			else {
				urlString += "/" + property;
			}
		}
		return urlString;
	}

	/**
	 * The key of an entity id in a resource path. SensorThings 1.0 (15-078r6) Table 2
	 * gives {@code @iot.id} the type ANY: a number is written as it is, a string as an
	 * OData string literal, quoted, with a quote inside it doubled (OData 4.0 URL
	 * Conventions 2.1, ets-sta10#40).
	 * @param id The id as the service answered it in {@code @iot.id}
	 * @return The key to write between the parentheses
	 */
	public static String key(Object id) {
		if (id instanceof String) {
			return "'" + ((String) id).replace("'", "''") + "'";
		}
		return String.valueOf(id);
	}

	/**
	 * The id written in a resource path key, as {@link #key(Object)} writes it: a quoted
	 * key is a string id, any other a number.
	 * @param key The text between the parentheses of a resource path, for example the
	 * Location header of a create
	 * @return The id, a String or a Long
	 */
	public static Object idOf(String key) {
		if (key.length() >= 2 && key.startsWith("'") && key.endsWith("'")) {
			return key.substring(1, key.length() - 1).replace("''", "'");
		}
		return Long.parseLong(key);
	}

	/**
	 * Whether an id stands for no entity: the suite passes -1 for a collection.
	 * @param id The id or -1
	 * @return true for null or the number -1
	 */
	public static boolean isNone(Object id) {
		return id == null || (id instanceof Number && ((Number) id).longValue() == -1);
	}

	/**
	 * An id as a JSON value in a request body, for a link by {@code @iot.id}: a string
	 * quoted, a number as it is (15-078r6 Table 2, ets-sta10#40).
	 * @param id The id as the service answered it
	 * @return The JSON text of the id
	 */
	public static String json(Object id) {
		if (id instanceof String) {
			return org.json.JSONObject.quote((String) id);
		}
		return String.valueOf(id);
	}

}
