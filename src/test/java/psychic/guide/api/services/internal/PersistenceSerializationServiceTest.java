package psychic.guide.api.services.internal;

import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.io.Serializable;
import java.util.UUID;

import static org.junit.Assert.*;

public class PersistenceSerializationServiceTest {
	private static final String TEST_FILE_NAME = "test";
	private static final String TEST_FILE_NAME_FULL = "data/test.ser";

	@Test
	public void testSerialization() {
		TestSerializableObject serializableObject = new TestSerializableObject();
		PersistenceService<TestSerializableObject> service = new PersistenceSerializationService<>(TEST_FILE_NAME);

		service.save(serializableObject);
		TestSerializableObject objectRead = service.read();

		assertNotNull(objectRead);
		assertEquals(serializableObject.id, objectRead.id);
		assertTrue(new File(TEST_FILE_NAME_FULL).exists());
	}

	@After
	public void tearDown() throws Exception {
		File file = new File(TEST_FILE_NAME_FULL);
		if (file.exists()) {
			boolean fileDeleted = file.delete();
			assertTrue(fileDeleted);
		}
	}

	private static class TestSerializableObject implements Serializable {
		private UUID id = UUID.randomUUID();

		public UUID getId() {
			return id;
		}

		public void setId(UUID id) {
			this.id = id;
		}
	}
}