package moe.ore.core.client;

import oicq.wlogin_sdk.tools.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class ClientPow {
    // boolean hasSoLib = false;

    // public native byte[] nativeGetPow(byte[] bArr);

    // public native byte[] nativeGetTestData();

    public ClientPow() {
        try {
            // System.loadLibrary("pow");
            // this.hasSoLib = true;
        } catch (UnsatisfiedLinkError e) {
            // So Lib 载入失败
        }
    }

    public byte[] calc(byte[] t546) {
        byte[] ret = new byte[0];
        if (t546 == null || t546.length == 0) {
            return ret;
        }
        /*
        if (this.hasSoLib) {
            try {
                ret = nativeGetPow(t546);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/
        try {
            ret = javaGetPow(t546);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    public byte[] javaGetPow(byte[] t546) {
        byte[] ret = new byte[0];
        PowValue value = new PowValue();
        if (value.init(t546) != 0) {
            return ret;
        }
        long st = System.currentTimeMillis();
        value.cnt = 0;
        value.cost = 0;
        int count;
        switch (value.checkType) {
            case 1 :
                count = checkOrigin(value);
                break;
            case 2 :
                count = calcCostCount(value);
                break;
            default :
                throw new RuntimeException("not support algorithm=" + value.checkType);
        }
        if (count < 0) {
            return ret;
        }
        value.cost = (int) (System.currentTimeMillis() - st);
        value.hasHashResult = 1;
        value.cnt = count;
        byte[] result = value.array();
        if (result != null && result.length > 0) {
            return result;
        } else {
            return ret;
        }
    }

    private int hashSha(byte[] content, byte[] dst) {
        if (content == null || content.length == 0) {
            return 1;
        }
        try {
            MessageDigest instance = MessageDigest.getInstance("SHA-256");
            instance.update(content);
            byte[] digest = instance.digest();
            System.arraycopy(digest, 0, dst, 0, digest.length);
            return 0;
        } catch (NoSuchAlgorithmException e) {
            return 2;
        }
    }

    private int calcCostCount(PowValue value) {
        byte[] hashTmp = new byte[32];
        byte[] copy = new byte[value.origin.length];
        System.arraycopy(value.origin, 0, copy, 0, value.origin.length);
        BigInteger bInt = new BigInteger(copy);
        while (true) {
            if (value.algorithmType == 1) {
                hashSha(copy, hashTmp);
                if (Arrays.equals(hashTmp, value.cp)) {
                    byte[] result = Arrays.copyOf(copy, copy.length);
                    value.hashResult = result;
                    value.hashResultSize = result.length;
                    return value.cnt;
                }
                value.cnt++;
                bInt = bInt.add(BigInteger.ONE);
                byte[] byteArray = bInt.toByteArray();
                if (byteArray.length > copy.length) {
                    return -1;
                }
                System.arraycopy(byteArray, 0, copy, 0, byteArray.length);
            } else if (value.algorithmType == 2) {
                sm3(copy);
                return -1;
            } else {
                return -1;
            }
        }
    }

    private int checkOrigin(PowValue value) {
        byte[] tmp = new byte[32];
        byte[] copy = new byte[value.origin.length];
        System.arraycopy(value.origin, 0, copy, 0, value.origin.length);
        BigInteger bInt = new BigInteger(copy);
        while (value.algorithmType == 1) {
            hashSha(copy, tmp);
            if (check(tmp, value.baseCount) == 0) {
                byte[] result = Arrays.copyOf(copy, copy.length);
                value.hashResult = result;
                value.hashResultSize = result.length;
                return value.cnt;
            }
            value.cnt++;
            bInt = bInt.add(BigInteger.ONE);
            byte[] byteArray = bInt.toByteArray();
            if (byteArray.length > copy.length) {
                util.LOGI("big number too large len:" + byteArray.length, "");
                return -1;
            }
            System.arraycopy(byteArray, 0, copy, 0, byteArray.length);
        }
        if (value.algorithmType == 2) {
            sm3(copy);
            util.LOGI("hash func not support sm3", "");
            return -1;
        }
        util.LOGI("error hash func", "");
        return -1;
    }

    /**
     * SM3加密算法 [正在开发]
     */
    private byte[] sm3(byte[] bArr) {
        return new byte[0];
    }

    int check(byte[] in, int count) {
        if (count > 32) {
            return 1;
        }
        int iv = 255;
        int i = 0;
        while (iv >= 0 && i < count) {
            if ((in[iv / 8] & (1 << (iv % 8))) != 0) {
                return 2;
            }
            iv--;
            i++;
        }
        return 0;
    }
}
