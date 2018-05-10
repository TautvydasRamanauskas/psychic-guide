package psychic.guide.api;

import org.junit.Assert;
import org.junit.Test;

public class SearchPropertiesTest {
	private static final String TEST_PROPERTIES_PATH = "src/test/java/psychic/guide/test.properties";
	private static final String KEY_STRING = "property.test";
	private static final String KEY_BYTE_ARRAY = "property.test.byte.array";

	@Test
	public void testGet() {
		SearchProperties searchProperties = new SearchProperties(TEST_PROPERTIES_PATH);
		String property = searchProperties.get(KEY_STRING);
		Assert.assertEquals("Test", property);
	}

	@Test
	public void testGetNonExisting() {
		SearchProperties searchProperties = new SearchProperties(TEST_PROPERTIES_PATH);
		String property = searchProperties.get("property.non.existing");
		Assert.assertNull(property);
	}

	@Test
	public void testGetByteArrayByUsingGetString() {
		SearchProperties searchProperties = new SearchProperties(TEST_PROPERTIES_PATH);
		String property = searchProperties.get(KEY_BYTE_ARRAY);
		Assert.assertEquals("0x11, 0x21, 0x31", property);
	}

	@Test
	public void testGetByteArray() {
		SearchProperties searchProperties = new SearchProperties(TEST_PROPERTIES_PATH);
		byte[] byteArray = searchProperties.getByteArray(KEY_BYTE_ARRAY);
		Assert.assertArrayEquals(new byte[]{0x11, 0x21, 0x31}, byteArray);
	}

	@Test
	public void testGetByteArrayNonExisting() {
		SearchProperties searchProperties = new SearchProperties(TEST_PROPERTIES_PATH);
		byte[] byteArray = searchProperties.getByteArray("property.test.byte.array.non.existing");
		Assert.assertNull(byteArray);
	}

	@Test(expected = NumberFormatException.class)
	public void testGetStringUsingGetByteArray() {
		SearchProperties searchProperties = new SearchProperties(TEST_PROPERTIES_PATH);
		searchProperties.getByteArray(KEY_STRING);
	}
}