package moe.ore.util;
import java.nio.ByteBuffer;
import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class DesECBUtil {
    /**
     * 加密数据
     * 注意：这里的数据长度只能为8的倍数
     */
    public static String encryptDES(String encryptString, String encryptKey) throws Exception {
        SecretKeySpec key = new SecretKeySpec(getKey(encryptKey), "DES");
        Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedData = cipher.doFinal(setStuff(encryptString.getBytes()));
        return HexUtil.Bin2Hex(encryptedData);
    }

    /**
     * 自定义一个key
     */
    public static byte[] getKey(String keyRule) {
        Key key;
        byte[] keyByte = keyRule.getBytes();
        // 创建一个空的八位数组,默认情况下为0
        byte[] byteTemp = new byte[8];
        // 将用户指定的规则转换成八位数组
        for (int i = 0; i < byteTemp.length && i < keyByte.length; i++) {
            byteTemp[i] = keyByte[i];
        }
        key = new SecretKeySpec(byteTemp, "DES");
        return key.getEncoded();
    }

    /***
     * 解密数据
     */
    public static String decryptDES(String decryptString, String decryptKey) throws Exception {
        SecretKeySpec key = new SecretKeySpec(getKey(decryptKey), "DES");
        Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte decryptedData[] = cipher.doFinal(HexUtil.Hex2Bin(decryptString));
        return new String(decryptedData);
    }

    /***
     * 填充数据
     */
    public static byte[] setStuff(byte[] msg){
        int i=msg.length % 8;
        if(i == 0){
            return msg;
        }else {
            int a=8-i;
            StringBuffer sb=new StringBuffer();
            for (int s=0;s<a;s++){
                sb.append(" ");
            }
            byte[] data=sb.toString().getBytes();
            data=ByteBuffer.allocate(msg.length + data.length).put(msg).put(data).array();
            return data;
        }
    }
}
