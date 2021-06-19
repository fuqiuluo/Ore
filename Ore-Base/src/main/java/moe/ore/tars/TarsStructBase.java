package moe.ore.tars;

import java.io.Serializable;
import java.nio.charset.Charset;

public class TarsStructBase implements Serializable {
    public static final byte BYTE = 0;
    public static final byte SHORT = 1;
    public static final byte INT = 2;
    public static final byte LONG = 3;
    public static final byte FLOAT = 4;
    public static final byte DOUBLE = 5;
    public static final byte STRING1 = 6;
    public static final byte STRING4 = 7;
    public static final byte MAP = 8;
    public static final byte LIST = 9;
    public static final byte STRUCT_BEGIN = 10;
    public static final byte STRUCT_END = 11;
    public static final byte ZERO_TAG = 12;
    public static final byte SIMPLE_LIST = 13;

    public static final int MAX_STRING_LENGTH = 100 * 1024 * 1024;

    public String servantName() {
        return "";
    }

    public String funcName() {
        return "";
    }

    public String reqName() {
        return "";
    }

    public String respName() {
        return "";
    }

    // 非必要实现
    public void writeTo(TarsOutputStream output) {

    }

    // 非必要实现
    public void readFrom(TarsInputStream input) {

    }

    public void display(StringBuilder sb, int level) {
    }

    public void displaySimple(StringBuilder sb, int level) {
    }

    public TarsStructBase newInit() {
        return null;
    }

    public void recycle() {

    }

    public boolean containField(String name) {
        return false;
    }

    public Object getFieldByName(String name) {
        return null;
    }

    public void setFieldByName(String name, Object value) {
    }

    public byte[] toByteArray() {
        TarsOutputStream os = new TarsOutputStream();
        writeTo(os);
        byte[] out = os.toByteArray();
        os.close();
        return out;
    }

    public byte[] toByteArray(Charset encoding) {
        TarsOutputStream os = new TarsOutputStream();
        os.setServerEncoding(encoding);
        writeTo(os);
        byte[] out = os.toByteArray();
        os.close();
        return out;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        display(sb, 0);
        return sb.toString();
    }

    public static String toDisplaySimpleString(TarsStructBase struct) {
        if (struct == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        struct.displaySimple(sb, 0);
        return sb.toString();
    }
}