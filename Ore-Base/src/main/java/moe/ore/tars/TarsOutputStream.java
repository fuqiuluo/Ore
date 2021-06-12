/*
 * English :
 *  The project is protected by the MPL open source agreement.
 * Open source agreement warning that prohibits deletion of project source code files.
 * The project is prohibited from acting in illegal areas.
 * All illegal activities arising from the use of this project are the responsibility of the second author, and the original author of the project is not responsible
 *
 *  中文：
 *  该项目由MPL开源协议保护。
 *  禁止删除项目源代码文件的开源协议警告内容。
 * 禁止使用该项目在非法领域行事。
 * 使用该项目产生的违法行为，由使用者或第二作者全责，原作者免责
 *
 * 日本语：
 * プロジェクトはMPLオープンソース契約によって保護されています。
 *  オープンソース契約プロジェクトソースコードファイルの削除を禁止する警告。
 * このプロジェクトは違法地域の演技を禁止しています。
 * このプロジェクトの使用から生じるすべての違法行為は、2番目の著者の責任であり、プロジェクトの元の著者は責任を負いません。
 *
 */

/*
 * English :
 *  The project is protected by the MPL open source agreement.
 * Open source agreement warning that prohibits deletion of project source code files.
 * The project is prohibited from acting in illegal areas.
 * All illegal activities arising from the use of this project are the responsibility of the second author, and the original author of the project is not responsible
 *
 *  中文：
 *  该项目由MPL开源协议保护。
 *  禁止删除项目源代码文件的开源协议警告内容。
 * 禁止使用该项目在非法领域行事。
 * 使用该项目产生的违法行为，由使用者或第二作者全责，原作者免责
 *
 * 日本语：
 * プロジェクトはMPLオープンソース契約によって保護されています。
 *  オープンソース契約プロジェクトソースコードファイルの削除を禁止する警告。
 * このプロジェクトは違法地域の演技を禁止しています。
 * このプロジェクトの使用から生じるすべての違法行為は、2番目の著者の責任であり、プロジェクトの元の著者は責任を負いません。
 *
 */

package moe.ore.tars;

import io.netty.buffer.ByteBuf;
import kotlinx.io.core.BytePacketBuilder;
import moe.ore.helper.BytePacketExtKt;
import moe.ore.helper.StringExtKt;
import moe.ore.tars.exc.TarsEncodeException;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class TarsOutputStream {
    protected Charset sServerEncoding = StandardCharsets.UTF_8;

    public void setServerEncoding(Charset se) {
        this.sServerEncoding = se;
    }

    private final BytePacketBuilder bs = BytePacketExtKt.createBuilder();

    public BytePacketBuilder getByteBuilder() {
        return bs;
    }

    public byte[] toByteArray() {
        //byte[] newBytes = new byte[bs.position()];
        return BytePacketExtKt.toByteArray(this.bs);
    }


    public void writeHead(byte type, int tag) {
        if (tag < 15) {
            byte b = (byte) ((tag << 4) | type);
            //bs.put(b);
            bs.writeByte(b);
        } else if (tag < 256) {
            byte b = (byte) ((15 << 4) | type);
            //bs.put(b);
            bs.writeByte(b);
            //bs.put((byte) tag);
            bs.writeByte((byte) tag);
        } else {
            throw new TarsEncodeException("tag is too large: " + tag);
        }
    }

    public void write(boolean b, int tag) {
        byte by = (byte) (b ? 0x01 : 0);
        write(by, tag);
    }

    public void write(byte b, int tag) {
        if (b == 0) {
            writeHead(TarsStructBase.ZERO_TAG, tag);
        } else {
            writeHead(TarsStructBase.BYTE, tag);
            //bs.put(b);
            bs.writeByte(b);
        }
    }

    public void write(short n, int tag) {
        if (n >= Byte.MIN_VALUE && n <= Byte.MAX_VALUE) {
            write((byte) n, tag);
        } else {
            writeHead(TarsStructBase.SHORT, tag);
            bs.writeShort(n);
        }
    }

    public void write(int n, int tag) {
        if (n >= Short.MIN_VALUE && n <= Short.MAX_VALUE) {
            write((short) n, tag);
        } else {
            writeHead(TarsStructBase.INT, tag);
            bs.writeInt(n);
        }
    }

    public void write(long n, int tag) {
        if (n >= Integer.MIN_VALUE && n <= Integer.MAX_VALUE) {
            write((int) n, tag);
        } else {
            writeHead(TarsStructBase.LONG, tag);
            bs.writeLong(n);
        }
    }

    public void write(float n, int tag) {
        writeHead(TarsStructBase.FLOAT, tag);
        bs.writeFloat(n);
    }

    public void write(double n, int tag) {
        writeHead(TarsStructBase.DOUBLE, tag);
        bs.writeDouble(n);
    }

    public void writeStringByte(String s, int tag) {
        byte[] by = StringExtKt.hex2ByteArray(s);
        if (by.length > 255) {
            writeHead(TarsStructBase.STRING4, tag);
            bs.writeInt(by.length);
        } else {
            writeHead(TarsStructBase.STRING1, tag);
            bs.writeByte((byte) by.length);
        }
        BytePacketExtKt.writeBytes(this.bs, by);
    }

    public void writeByteString(String s, int tag) {
        byte[] by = StringExtKt.hex2ByteArray(s);
        if (by.length > 255) {
            writeHead(TarsStructBase.STRING4, tag);
            bs.writeInt(by.length);
        } else {
            writeHead(TarsStructBase.STRING1, tag);
            bs.writeByte((byte) by.length);
        }
        BytePacketExtKt.writeBytes(this.bs, by);
    }

    public void write(String s, int tag) {
        byte[] by;
        by = s.getBytes(sServerEncoding);
        if (by.length > 255) {
            writeHead(TarsStructBase.STRING4, tag);
            bs.writeInt(by.length);
        } else {
            writeHead(TarsStructBase.STRING1, tag);
            bs.writeByte((byte) by.length);
        }
        BytePacketExtKt.writeBytes(this.bs, by);
    }

    public <K, V> void write(Map<K, V> m, int tag) {
        writeHead(TarsStructBase.MAP, tag);
        write(m == null ? 0 : m.size(), 0);
        if (m != null) {
            for (Map.Entry<K, V> en : m.entrySet()) {
                write(en.getKey(), 0);
                write(en.getValue(), 1);
            }
        }
    }

    public void write(boolean[] l, int tag) {
        writeHead(TarsStructBase.LIST, tag);
        write(l.length, 0);
        for (boolean e : l)
            write(e, 0);
    }

    public void write(byte[] l, int tag) {
        writeHead(TarsStructBase.SIMPLE_LIST, tag);
        writeHead(TarsStructBase.BYTE, 0);
        write(l.length, 0);
        BytePacketExtKt.writeBytes(this.bs, l);
    }

    public void write(ByteBuf l, int tag) {
        writeHead(TarsStructBase.SIMPLE_LIST, tag);
        writeHead(TarsStructBase.BYTE, 0);
        write(l.readableBytes(), 0);
        byte[] bytes = new byte[l.readableBytes()];
        l.readBytes(bytes);
        BytePacketExtKt.writeBytes(this.bs, bytes);
    }
    public void writBodyHead(ByteBuf l, int tag) {
        writeHead(TarsStructBase.SIMPLE_LIST, tag);
        writeHead(TarsStructBase.BYTE, 0);
        write(l.readableBytes(), 0);
    }


    public void write(short[] l, int tag) {
        writeHead(TarsStructBase.LIST, tag);
        write(l.length, 0);
        for (short e : l)
            write(e, 0);
    }

    public void write(int[] l, int tag) {
        writeHead(TarsStructBase.LIST, tag);
        write(l.length, 0);
        for (int e : l)
            write(e, 0);
    }

    public void write(long[] l, int tag) {
        writeHead(TarsStructBase.LIST, tag);
        write(l.length, 0);
        for (long e : l)
            write(e, 0);
    }

    public void write(float[] l, int tag) {
        writeHead(TarsStructBase.LIST, tag);
        write(l.length, 0);
        for (float e : l)
            write(e, 0);
    }

    public void write(double[] l, int tag) {
        writeHead(TarsStructBase.LIST, tag);
        write(l.length, 0);
        for (double e : l)
            write(e, 0);
    }

    public <T> void write(T[] l, int tag) {
        writeArray(l, tag);
    }

    private void writeArray(Object[] l, int tag) {
        writeHead(TarsStructBase.LIST, tag);
        write(l.length, 0);
        for (Object e : l)
            write(e, 0);
    }

    public <T> void write(Collection<T> l, int tag) {
        writeHead(TarsStructBase.LIST, tag);
        write(l == null ? 0 : l.size(), 0);
        if (l != null) {
            for (T e : l)
                write(e, 0);
        }
    }

    public void write(TarsStructBase o, int tag) {
        writeHead(TarsStructBase.STRUCT_BEGIN, tag);
        o.writeTo(this);
        // o.writeTo(new com.qq.tars.protocol.tars.TarsOutputStream());
        writeHead(TarsStructBase.STRUCT_END, 0);
    }

    public void write(Byte o, int tag) {
        write(o.byteValue(), tag);
    }

    public void write(Boolean o, int tag) {
        write(o.booleanValue(), tag);
    }

    public void write(Short o, int tag) {
        write(o.shortValue(), tag);
    }

    public void write(Integer o, int tag) {
        write(o.intValue(), tag);
    }

    public void write(Long o, int tag) {
        write(o.longValue(), tag);
    }

    public void write(Float o, int tag) {
        write(o.floatValue(), tag);
    }

    public void write(Double o, int tag) {
        write(o.doubleValue(), tag);
    }

    public void write(Object o, int tag) {
        if (o instanceof Byte) {
            write(((Byte) o).byteValue(), tag);
        } else if (o instanceof Boolean) {
            write(((Boolean) o).booleanValue(), tag);
        } else if (o instanceof Short) {
            write(((Short) o).shortValue(), tag);
        } else if (o instanceof Integer) {
            write(((Integer) o).intValue(), tag);
        } else if (o instanceof Long) {
            write(((Long) o).longValue(), tag);
        } else if (o instanceof Float) {
            write(((Float) o).floatValue(), tag);
        } else if (o instanceof Double) {
            write(((Double) o).doubleValue(), tag);
        } else if (o instanceof String) {
            write((String) o, tag);
        } else if (o instanceof Map) {
            write((Map<?, ?>) o, tag);
        } else if (o instanceof List) {
            write((List<?>) o, tag);
        } else if (o instanceof TarsStructBase) {
            write((TarsStructBase) o, tag);
        } else if (o instanceof byte[]) {
            write((byte[]) o, tag);
        } else if (o instanceof boolean[]) {
            write((boolean[]) o, tag);
        } else if (o instanceof short[]) {
            write((short[]) o, tag);
        } else if (o instanceof int[]) {
            write((int[]) o, tag);
        } else if (o instanceof long[]) {
            write((long[]) o, tag);
        } else if (o instanceof float[]) {
            write((float[]) o, tag);
        } else if (o instanceof double[]) {
            write((double[]) o, tag);
        } else if (o.getClass().isArray()) {
            writeArray((Object[]) o, tag);
        } else if (o instanceof Collection) {
            write((Collection<?>) o, tag);
        } else {
            throw new RuntimeException("Unknown type cannot be written!");
        }
    }

    public void close() {
        this.bs.close();
    }
}
