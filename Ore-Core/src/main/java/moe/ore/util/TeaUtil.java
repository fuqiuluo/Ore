package moe.ore.util;

public class TeaUtil {
	public static byte[] encrypt(byte[] data, byte[] key) {
		Crypter crypter = new Crypter();
		return crypter.encrypt(data, key);
	}

	public static byte[] decrypt(byte[] data, byte[] key) {
		Crypter crypter = new Crypter();
		byte[] result = crypter.decrypt(data, key);
		if(result == null) {
			return crypter.decrypt(data, new byte[16]);
		} else {
			return result;
		}
	}
}
