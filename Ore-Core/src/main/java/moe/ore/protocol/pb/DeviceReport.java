package moe.ore.protocol.pb;

import moe.ore.pb.ByteStringMicro;
import moe.ore.pb.MessageMicro;
import moe.ore.pb.PBBytesField;
import moe.ore.pb.PBField;

public class DeviceReport extends MessageMicro<DeviceReport> {
    static final MessageMicro.FieldMap __fieldMap__ = MessageMicro.initFieldMap(new int[]{10, 18, 26, 34, 42, 50, 58, 66, 74},
            new String[]{"bytes_bootloader", "bytes_version", "bytes_codename", "bytes_incremental", "bytes_fingerprint", "bytes_boot_id", "bytes_android_id", "bytes_baseband", "bytes_inner_ver"},
            new Object[]{ByteStringMicro.EMPTY, ByteStringMicro.EMPTY, ByteStringMicro.EMPTY, ByteStringMicro.EMPTY, ByteStringMicro.EMPTY, ByteStringMicro.EMPTY, ByteStringMicro.EMPTY, ByteStringMicro.EMPTY, ByteStringMicro.EMPTY},
            DeviceReport.class);

    public final PBBytesField bytes_bootloader = PBField.initBytes("");

    public final PBBytesField bytes_version = PBField.initBytes("");
    public final PBBytesField bytes_codename = PBField.initBytes("");
    public final PBBytesField bytes_incremental = PBField.initBytes("");
    public final PBBytesField bytes_fingerprint = PBField.initBytes("");
    public final PBBytesField bytes_boot_id = PBField.initBytes("");
    public final PBBytesField bytes_android_id = PBField.initBytes("");
    public final PBBytesField bytes_baseband = PBField.initBytes("");
    public final PBBytesField bytes_inner_ver = PBField.initBytes("");
}
