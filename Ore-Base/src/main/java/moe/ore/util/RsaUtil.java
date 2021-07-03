package moe.ore.util;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Random;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class RsaUtil {
    public static final String ECB_PKCS1_PADDING = "RSA/ECB/PKCS1Padding";
    public static final String EXPONENT = "10001";
    public static final String KEY_ALGORITHM = "RSA";
    public static final String MODULD = "B293AAC4108FF8637D9ECE9E064E8CE8F7728F914BBB2A3C5343733C63B7C51D98F7B2A8B5734E87E8F006F364CB54A80857783694F6515EB948871477EC4090EA11B7DF048016369ABD983256ACB9E80940727813B617FC7F3385415954682BB5E163FF8F3C62C10AE61FAB2980DDA4301BDA6DAA82307CC1D8D4A585F57D05";
    public static final String TRANSFORMATION = "RSA/ECB/NoPadding";

    public RsaUtil() {
    }

    public static String encode(byte[] bArr) {
        try {
            RSAPublicKey publicKey = getPublicKey("B293AAC4108FF8637D9ECE9E064E8CE8F7728F914BBB2A3C5343733C63B7C51D98F7B2A8B5734E87E8F006F364CB54A80857783694F6515EB948871477EC4090EA11B7DF048016369ABD983256ACB9E80940727813B617FC7F3385415954682BB5E163FF8F3C62C10AE61FAB2980DDA4301BDA6DAA82307CC1D8D4A585F57D05", "10001");
            Cipher instance = Cipher.getInstance("RSA/ECB/NoPadding");
            instance.init(1, publicKey);
            return HexUtil.Bin2Hex(instance.doFinal(bArr)).replace(" ", "");
        } catch (InvalidKeyException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static RSAPublicKey getPublicKey(String str, String str2) {
        try {
            return (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new RSAPublicKeySpec(new BigInteger(str, 16), new BigInteger(str2, 16)));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static final byte[] getRandomByte(int i) {
        byte[] bArr = new byte[i];
        new Random().nextBytes(bArr);
        return bArr;
    }
}
