package net.smartplan.fitness;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import java.security.spec.KeySpec;
import java.util.Base64;

@SpringBootTest
class FitnessApplicationTests {

	private static final String UNICODE_FORMAT = "UTF8";
	public static final String DEEDED_ENCRYPTION_SCHEME = "DESede";
	public static final String ACTIVE = "ACTIVE";
	public static final String DISABLED = "DISABLED";
	public static final String EXPIRED = "EXPIRED";
	private Cipher cipher;
	byte[] arrayBytes;
	SecretKey key;

	@Test
	void contextLoads() {

		try {
			String myEncryptionKey = "H.D.SACHIN DILSHAN NANDANA";
			arrayBytes = myEncryptionKey.getBytes(UNICODE_FORMAT);
			KeySpec ks = new DESedeKeySpec(arrayBytes);
			SecretKeyFactory skf = SecretKeyFactory.getInstance(DEEDED_ENCRYPTION_SCHEME);
			cipher = Cipher.getInstance(DEEDED_ENCRYPTION_SCHEME);
			key = skf.generateSecret(ks);
		} catch (Exception exception) {
			exception.printStackTrace();
		}


		String s = encryptPassword("admin@123#");
		System.out.println(s);
	}

	public String encryptPassword(String unencryptedString) {
		String encryptedString = null;
		try {
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] plainText = unencryptedString.getBytes(UNICODE_FORMAT);
			byte[] encryptedText = cipher.doFinal(plainText);
			encryptedString = Base64.getEncoder().encodeToString(encryptedText);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return encryptedString;
	}

}
