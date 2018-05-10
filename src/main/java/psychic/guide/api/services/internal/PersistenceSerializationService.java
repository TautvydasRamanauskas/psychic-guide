package psychic.guide.api.services.internal;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class PersistenceSerializationService<T extends Serializable> implements PersistenceService<T> {
	private static final String FILE_NAME_FORMAT = "data/%s.ser";
	private static final String CIPHER = "Blowfish";
	private static final SecretKey KEY = new SecretKeySpec(new byte[]{0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07}, CIPHER);
	private final String fileName;

	public PersistenceSerializationService(String fileName) {
		this.fileName = String.format(FILE_NAME_FORMAT, fileName);
	}

	@Override
	public synchronized void save(T object) {
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

	@Override
	public T read() {
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

	@Override
	public T readOrDefault(T defaultResult) {
		T read = read();
		if (read == null) {
			return defaultResult;
		}
		return read;
	}

	private static Cipher getCipher(int encryptMode) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
		Cipher cipher = Cipher.getInstance(CIPHER);
		cipher.init(encryptMode, KEY);
		return cipher;
	}
}
