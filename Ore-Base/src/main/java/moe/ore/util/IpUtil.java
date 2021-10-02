package moe.ore.util;

public class IpUtil {
    public static String getNumConvertIp(long ipLong) {
        long[] mask = { 0x000000FF, 0x0000FF00, 0x00FF0000, 0xFF000000 };
        long num = 0;
        StringBuilder ipInfo = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            num = (ipLong & mask[i]) >> (i * 8);
            if (i > 0)
                ipInfo.insert(0, ".");
            ipInfo.insert(0, Long.toString(num, 10));
        }
        return ipInfo.toString();
    }

    public static long getIpConvertNum(String ipAddress) {
        String[] ip = ipAddress.split("\\.");
        long a = Integer.parseInt(ip[0]);
        long b = Integer.parseInt(ip[1]);
        long c = Integer.parseInt(ip[2]);
        long d = Integer.parseInt(ip[3]);
        return a * 256 * 256 * 256 + b * 256 * 256 + c * 256 + d;
    }

    /**
     * 逆序转换
     */
    public static int ip_to_int(String str) {
        byte[] bArr = new byte[4];
        try {
            String[] split = str.split("\\.");
            bArr[0] = (byte) (Integer.parseInt(split[0]) & 255);
            bArr[1] = (byte) (Integer.parseInt(split[1]) & 255);
            bArr[2] = (byte) (Integer.parseInt(split[2]) & 255);
            bArr[3] = (byte) (Integer.parseInt(split[3]) & 255);
            return (bArr[3] & 255) | ((bArr[2] << 8) & 65280) | ((bArr[1] << 16) & 16711680) | ((bArr[0] << 24) & -16777216);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 逆序转换
     */
    public static String int_to_ip(long j) {
        j = j & 4294967295L;
        StringBuilder builder = new StringBuilder(16);
        for (int i = 3; i >= 0; i--) {
            builder.append(255 & (j % 256));
            j /= 256;
            if (i != 0) {
                builder.append('.');
            }
        }
        return builder.toString();
    }


}
