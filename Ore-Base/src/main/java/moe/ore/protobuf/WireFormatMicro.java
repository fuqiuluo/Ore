package moe.ore.protobuf;

public final class WireFormatMicro {
	public static int getTagFieldNumber(int i) {
		return i >>> 3;
	}

	static int getTagWireType(int i) {
		return i & 7;
	}

	static int makeTag(int i, int i2) {
		return (i << 3) | i2;
	}
}
