package moe.ore.core.client;

import kotlinx.io.core.BytePacketBuilder;
import moe.ore.helper.BytePacketExtKt;

import java.nio.ByteBuffer;

public class PowValue {
    public int version;
    public int checkType;
    public int algorithmType;
    public int hasHashResult;

    public int baseCount;
    public int[] filling;

    public int originSize;
    public byte[] origin;

    public int cpSize;
    public byte[] cp;

    public int sm3Size;
    public byte[] sm;

    public int hashResultSize;
    public byte[] hashResult;

    public int cost;
    public int cnt;

    public int init(byte[] tlv546) {
        int pos = 0;
        this.version = tlv546[pos]; // 0 [a]
        pos++;
        this.checkType = tlv546[pos]; // 1 [b]
        pos++;
        this.algorithmType = tlv546[pos]; // 2 [c]
        pos++;
        this.hasHashResult = tlv546[pos]; // 3 [d]
        pos += 2;
        this.baseCount = buf16ToShort(tlv546[pos - 1], tlv546[pos]); // 4 , 5
        // System.out.println("BaseCount : " + baseCount);
        this.filling = new int[2];
        for (int m = 0; m < 2; m++) {
            pos++;
            this.filling[m] = tlv546[pos]; // 6, 7
        }
        // System.out.println("filling : " + Arrays.toString(filling));

        //========================================================================================
        pos += 2;
        this.originSize = buf16ToShort(tlv546[pos - 1], tlv546[pos]); // 8, 9
        if(this.originSize > 0) {
            this.origin = new byte[originSize];
            pos++;
            for (int m = 0; m < this.originSize; m++) {
                this.origin[m] = tlv546[pos]; // start pos : 11
                pos++;
            }
            // System.out.println("original end pos : " + pos);
        }
        //========================================================================================

        pos++;
        this.cpSize = buf16ToShort(tlv546[pos - 1], tlv546[pos]); // 11, 12
        if(this.cpSize > 0) {
            this.cp = new byte[this.cpSize];
            pos++;
            for (int m = 0; m < this.cpSize; m++) {
                this.cp[m] = tlv546[pos]; // start pos : 13
                pos++;
            }
        }
        //========================================================================================

        pos++;
        this.sm3Size = buf16ToShort(tlv546[pos - 1], tlv546[pos]); // 13, 14
        if(this.sm3Size > 0) {
            this.sm = new byte[this.sm3Size];
            pos++;
            for (int m = 0; m < this.sm3Size; m++) {
                this.sm[m] = tlv546[pos]; // start pos : 15
                pos++;
            }
        }
        //========================================================================================

        if (this.hasHashResult == 1) {
            pos++;
            this.hashResultSize = buf16ToShort(tlv546[pos - 1], tlv546[pos]); // 15, 16
            if(this.hashResultSize > 0) {
                this.hashResult = new byte[this.hashResultSize];
                pos++;
                for (int m = 0; m < this.hashResultSize; m++) {
                    this.hashResult[m] = tlv546[pos]; // start pos : 17
                    pos++;
                }
            }
            this.cost = buf32ToInt(tlv546, pos);
            this.cnt = buf32ToInt(tlv546, pos + 4);
        }
        return 0;
    }

    public int buf16ToShort(byte b2, byte b3) {
        return ByteBuffer.wrap(new byte[]{b2, b3}).getShort();
    }

    public int buf32ToInt(byte[] bArr, int i2) {
        return ByteBuffer.wrap(new byte[]{bArr[i2], bArr[i2 + 1], bArr[i2 + 2], bArr[i2 + 3]}).getInt();
    }

    public byte[] array() {
        BytePacketBuilder builder = BytePacketExtKt.newBuilder();
        builder.writeByte((byte) this.version);
        builder.writeByte((byte) this.checkType);
        builder.writeByte((byte) this.algorithmType);
        builder.writeByte((byte) this.hasHashResult);
        builder.writeShort((short) this.baseCount);
        for (int fill : this.filling) {
            builder.writeByte((byte) fill);
        }

        builder.writeShort((short) this.originSize);
        if (this.originSize > 0) {
            builder.writeFully(this.origin, 0, this.originSize);
        }
        builder.writeShort((short) this.cpSize);
        if (this.cpSize > 0) {
            builder.writeFully(this.cp, 0, this.cpSize);
        }
        builder.writeShort((short) this.sm3Size);
        if (this.sm3Size > 0) {
            builder.writeFully(this.sm, 0, this.sm3Size);
        }
        if (this.hasHashResult == 1) {
            builder.writeShort((short) this.hashResultSize);
            builder.writeFully(this.hashResult, 0, this.hashResultSize);
            builder.writeInt(this.cost);
            builder.writeInt(this.cnt);
        }
        return BytePacketExtKt.toByteArray(builder);
    }
}
