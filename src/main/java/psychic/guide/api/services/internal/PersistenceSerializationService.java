package psychic.guide.api.services.internal;

import psychic.guide.api.SearchProperties;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PersistenceSerializationService<T extends Serializable> implements PersistenceService<T> {
	private static final String FILE_NAME_FORMAT = "data/%s.ser";
	private final String fileName;
	private final ExecutorService saveExecutor;
	private final Object accessLock;

	public PersistenceSerializationService(String fileName) {
		this.fileName = getFileNameFormat(fileName);
		this.saveExecutor = Executors.newSingleThreadExecutor();
		this.accessLock = new Object();
	}

	@Override
	public void saveOnThread(T object) {
		saveExecutor.execute(() -> save(object));
	}

	@Override
	public void save(T object) {
		synchronized (accessLock) {
			try {
				Cipher cipher = getCipher(Cipher.ENCRYPT_MODE);
				SealedObject sealedObject = new SealedObject(object, cipher);
				try (ObjectOutputStream outputStream =
							 new ObjectOutputStream(
									 new CipherOutputStream(
											 new BufferedOutputStream(
													 new FileOutputStream(fileName)), cipher))) {
					outputStream.writeObject(sealedObject);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public T read() {
		synchronized (accessLock) {
			try {
				Cipher cipher = getCipher(Cipher.DECRYPT_MODE);
				try (ObjectInputStream inputStream =
							 new ObjectInputStream(
									 new CipherInputStream(
											 new BufferedInputStream(
													 new FileInputStream(fileName)), cipher))) {
					SealedObject sealedObject = (SealedObject) inputStream.readObject();
					//noinspection unchecked
					return (T) sealedObject.getObject(cipher);
				}
			} catch (EOFException | FileNotFoundException ignored) {

			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	@Override
	public T readOrDefault(T defaultResult) {
		T read = read();
		if (read == null) {
			return defaultResult;
		}
		return read;
	}

	public static String getFileNameFormat(String fileName) {
		return String.format(FILE_NAME_FORMAT, fileName);
	}

	private static Cipher getCipher(int encryptMode) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
		String cipherType = SearchProperties.getInstance().get("persistence.cipher");
		byte[] key = SearchProperties.getInstance().getByteArray("persistence.key");
		Cipher cipher = Cipher.getInstance(cipherType);
		if (key != null) {
			cipher.init(encryptMode, new SecretKeySpec(key, cipherType));
		}
		return cipher;
	}
}
