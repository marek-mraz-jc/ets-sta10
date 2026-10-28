package org.opengis.cite.sta10.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONObject;
import org.junit.Test;

/**
 * Verifies that ids of any JSON type (15-078r6 Table 2) make the same paths and bodies.
 */
public class VerifyServiceURLBuilder {

	@Test
	public void numericIdsKeepTheirPaths() {
		Object id = new JSONObject("{\"@iot.id\": 42}").get("@iot.id");
		assertEquals("http://x/v1.0/Things(42)",
				ServiceURLBuilder.buildURLString("http://x/v1.0", EntityType.THING, id, null, null));
		assertEquals("42", ServiceURLBuilder.json(id));
		assertEquals(42L, ServiceURLBuilder.idOf("42"));
	}

	@Test
	public void stringIdsAreQuotedLiterals() {
		Object id = new JSONObject("{\"@iot.id\": \"urn:x:O'Brien/1\"}").get("@iot.id");
		assertEquals("http://x/v1.0/Things('urn:x:O''Brien/1')/Locations",
				ServiceURLBuilder.buildURLString("http://x/v1.0", EntityType.THING, id, EntityType.LOCATION, null));
		assertEquals("\"urn:x:O'Brien/1\"", ServiceURLBuilder.json(id));
		assertEquals(id, ServiceURLBuilder.idOf("'urn:x:O''Brien/1'"));
	}

	@Test
	public void aChainTakesBothKinds() {
		List<Object> ids = new ArrayList<>(Arrays.asList("a-1", 7));
		assertEquals("http://x/v1.0/Things('a-1')/Datastreams(7)/Observations", ServiceURLBuilder
			.buildURLString("http://x/v1.0", Arrays.asList("Things", "Datastreams", "Observations"), ids, null));
	}

	@Test
	public void minusOneAndNullAreNoEntity() {
		assertTrue(ServiceURLBuilder.isNone(-1));
		assertTrue(ServiceURLBuilder.isNone(null));
		assertFalse(ServiceURLBuilder.isNone("-1"));
		assertFalse(ServiceURLBuilder.isNone(0L));
	}

}
