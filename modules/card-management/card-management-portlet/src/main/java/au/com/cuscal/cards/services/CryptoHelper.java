package au.com.cuscal.cards.services;

import au.com.cuscal.cards.domain.Key;

import java.math.BigInteger;

import java.nio.charset.Charset;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.Base64;

/**
 * Helper for Cryptographic functions
 */
public class CryptoHelper {

	public static String encrypt(Key key, String payload) throws Exception {
		final String publicKeyDecoded = new String(
			Base64.decodeBase64(key.getPublicKey()), Charset.forName("UTF-8"));
		final RSAPublicKey rsaPublicKey = getPublicKeyFromXML(publicKeyDecoded);
		final byte[] encryptedPayload = rsaEncyrpt(
			payload.getBytes(), rsaPublicKey);
		final String eAccount = Base64.encodeBase64String(encryptedPayload);

		return eAccount;
		//return URLEncoder.encode(eAccount, Charset.forName("UTF-8").name());
	}

	/**
	 * @param rsaKeyValue the XML string containing Modulus and Exponent
	 * @return the RSAPublicKey
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	public static RSAPublicKey getPublicKeyFromXML(String rsaKeyValue)
		throws InvalidKeySpecException, NoSuchAlgorithmException {

		String mod = extractTag(rsaKeyValue, "Modulus");
		String exp = extractTag(rsaKeyValue, "Exponent");

		if ((mod == null) || (exp == null)) {
			return null;
		}

		// Convert the base64 values to BigIntegers

		BigInteger modulus = new BigInteger(1, Base64.decodeBase64(mod));
		BigInteger pubExp = new BigInteger(1, Base64.decodeBase64(exp));

		// get the RSA keyFactory

		KeyFactory factory = KeyFactory.getInstance("RSA");

		// Load the key

		RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(modulus, pubExp);

		// Generate the public Key

		RSAPublicKey key = (RSAPublicKey)factory.generatePublic(pubKeySpec);

		// return the key

		return key;
	}

	/**
	 * Perform RSA encryption
	 *
	 * @param data      data to encrypt
	 * @param publicKey the publicKey to encrypt with
	 * @return the encrypted data
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 */
	public static byte[] rsaEncyrpt(byte[] data, RSAPublicKey publicKey)
		throws BadPaddingException, IllegalBlockSizeException,
			   InvalidKeyException, NoSuchAlgorithmException,
			   NoSuchPaddingException {

		Cipher c = Cipher.getInstance("RSA/ECB/PKCS1Padding");

		c.init(Cipher.ENCRYPT_MODE, publicKey);

		return c.doFinal(data);
	}

	private static String extractTag(String s, String tag) {
		final String startTag = "<" + tag + ">";
		final String endTag = "</" + tag + ">";
		int ix1 = s.indexOf(startTag);

		if (ix1 < 0)

			return null;
		int ix2 = s.indexOf(endTag, ix1);

		if (ix2 < 0)

			return null;

		return s.substring(ix1 + startTag.length(), ix2);
	}

}