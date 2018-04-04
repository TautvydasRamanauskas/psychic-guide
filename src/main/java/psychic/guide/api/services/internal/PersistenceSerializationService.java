package psychic.guide.api.services.internal;

import java.io.*;

public class PersistenceSerializationService<T> implements PersistenceService<T> {
	private static final String FILE_NAME_FORMAT = "data/%s.ser";
	private final String fileName;

	public PersistenceSerializationService(String fileName) {
		this.fileName = String.format(FILE_NAME_FORMAT, fileName);
	}

	@Override
	public synchronized void save(T object) {
		try (FileOutputStream fileOutputStream = new FileOutputStream(fileName);
			 ObjectOutputStream outputStream = new ObjectOutputStream(fileOutputStream)) {
			outputStream.writeObject(object);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public T read() {
		try (FileInputStream fileInputStream = new FileInputStream(fileName);
			 ObjectInputStream inputStream = new ObjectInputStream(fileInputStream)) {
			//noinspection unchecked
			return (T) inputStream.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
