package moe.ore.protobuf;

import moe.ore.util.DebugUtil;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;

/**
 * 移除了效率低下的反射
 */
public abstract class MessageMicro<T extends MessageMicro<T>> extends PBPrimitiveField<T> {
    private static final HashMap<Class<?>, FieldMap> CacheMap = new HashMap<>();

    private FieldMap _fields = null;

    private FieldMap getFieldMap() {
        if (this._fields == null) {
            try {
                /*
                  优化腾讯Protobuf添加反射缓存机制
                 */
                Class<?> clazz = getClass();
                if(CacheMap.containsKey(clazz)) {
                    this._fields = DebugUtil.forcedConvert(null, CacheMap.get(clazz));
                } else {
                    Field declaredField = clazz.getDeclaredField("__fieldMap__");
                    declaredField.setAccessible(true);
                    this._fields = DebugUtil.forcedConvert(null, declaredField.get(this));
                    CacheMap.put(clazz, this._fields);
                }
            } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return this._fields;
    }

    public static FieldMap initFieldMap(int[] tags, String[] fields, Object[] defaultValue, Class<?> cls) {
        return new FieldMap(tags, fields, defaultValue, cls);
    }

    public static <T extends MessageMicro<T>> T mergeFrom(T t, byte[] bArr) {
        return t.mergeFrom(bArr);
    }

    public static byte[] toByteArray(MessageMicro<?> messageMicro) {
        return messageMicro.toByteArray();
    }

    public void clear(Object obj) {
        try {
            getFieldMap().clear(this);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        setHasFlag(false);
    }

    public int computeSize(int i) {
        if (has()) {
            return CodedOutputStreamMicro.computeMessageSize(i, this);
        }
        return 0;
    }

    protected int computeSizeDirectly(int i, T t) {
        return CodedOutputStreamMicro.computeMessageSize(i, t);
    }

    protected void copyFrom(PBField<T> sourceField) {
        try {
            getFieldMap().copyFields(this, (MessageMicro<T>) sourceField);
            setHasFlag( ((MessageMicro<T>) sourceField).has() );
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public T get() {
        return DebugUtil.forcedConvert(null, this);
    }

    public final int getCachedSize() {
        return getSerializedSize();
    }

    public final int getSerializedSize() {
        int size = -1;
        try {
            size = getFieldMap().getSerializedSize(this);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return size;
    }

    public final T mergeFrom(CodedInputStreamMicro codedInputStreamMicro) {
        FieldMap fieldMap = getFieldMap();
        setHasFlag(true);
        while (true) {
            int tag = codedInputStreamMicro.readTag();
            try {
                if (!fieldMap.readFieldFrom(codedInputStreamMicro, tag, this) &&
                        (tag == 0 || !parseUnknownField(codedInputStreamMicro, tag))) {
                    return get();
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                break;
            }
        }
        return get();
    }

    public final T mergeFrom(byte[] bArr) {
        return mergeFrom(bArr, 0, bArr.length);
    }

    public final T mergeFrom(byte[] bArr, int i, int i2) {
        CodedInputStreamMicro newInstance = CodedInputStreamMicro.newInstance(bArr, i, i2);
        mergeFrom(newInstance);
        newInstance.checkLastTagWas(0);
        return get();
    }

    protected boolean parseUnknownField(CodedInputStreamMicro codedInputStreamMicro, int i) {
        return codedInputStreamMicro.skipField(i);
    }

    public void readFrom(CodedInputStreamMicro codedInputStreamMicro) {
        codedInputStreamMicro.readMessage(this);
    }

    protected T readFromDirectly(CodedInputStreamMicro codedInputStreamMicro) {
        try {
            T t = DebugUtil.forcedConvert(null, getClass().newInstance());
            codedInputStreamMicro.readMessage(t);
            return t;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void set(T t) {
        set(t, true);
    }

    public void set(T t, boolean z) {
        copyFrom(t);
        setHasFlag(z);
    }

    public final void toByteArray(byte[] bArr, int i, int i2) {
        CodedOutputStreamMicro newInstance = CodedOutputStreamMicro.newInstance(bArr, i, i2);
        writeTo(newInstance);
        newInstance.checkNoSpaceLeft();
    }

    public byte[] toByteArray() {
        byte[] bArr = new byte[getSerializedSize()];
        toByteArray(bArr, 0, bArr.length);
        return bArr;
    }

    public final void writeTo(CodedOutputStreamMicro codedOutputStreamMicro) {
        try {
            getFieldMap().writeTo(codedOutputStreamMicro, this);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public void writeTo(CodedOutputStreamMicro codedOutputStreamMicro, int i) {
        if (has()) {
            codedOutputStreamMicro.writeMessage(i, this);
        }
    }

    protected void writeToDirectly(CodedOutputStreamMicro codedOutputStreamMicro, int i, T t) {
        codedOutputStreamMicro.writeMessage(i, t);
    }

    public static final class FieldMap {
        private final Object[] defaultValues;
        private final Field[] fields;
        private final int[] tags;

        public FieldMap(int[] tags, String[] fields, Object[] defaultValue, Class<?> cls) {
            this.tags = tags;
            this.defaultValues = defaultValue;
            this.fields = new Field[tags.length];
            for (int i = 0; i < tags.length; i++) {
                try {
                    // 使用 try 跳过因为错误构建不存在的field
                    this.fields[i] = cls.getField(fields[i]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        public void clear(MessageMicro<?> micro) {
            for (int i = 0; i <this.tags.length; i++) {
                this.fields[i].setAccessible(true);
                try {
                    ( (PBField<?>) this.fields[i].get(micro) ).clear(this.defaultValues[i]);
                } catch(IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        public <U> void copyFields(U u, MessageMicro<?> uu) {
            for (int i = 0; i < this.tags.length; i++) {
                Field field = this.fields[i];
                try {
                    field.setAccessible(true);
                    ((PBField<?>) field.get(u)).copyFrom(
                            DebugUtil.forcedConvert(null, field.get(uu))
                    );
                } catch(IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        private static <T> void copyFrom(PBField<T> field, PBField<T> formField) {
            field.copyFrom(formField);
        }

        public <U extends MessageMicro<U>> void copyFields(U u, U uu) {
            for (int i = 0; i < this.tags.length; i++) {
                Field field = this.fields[i];
                try {
                    field.setAccessible(true);
                    ((PBField<?>) field.get(u)).copyFrom(DebugUtil.forcedConvert(null, field.get(uu)));
                } catch(IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        public Field get(int i) {
            int binarySearch = Arrays.binarySearch(this.tags, i);
            if (binarySearch < 0) {
                return null;
            }
            return this.fields[binarySearch];
        }

        public int getSerializedSize(MessageMicro<?> messageMicro) {
            int i = 0;
            for (int j = 0; j < this.tags.length; j++) {
                this.fields[j].setAccessible(true);
                try {
                    i += ((PBField<?>) this.fields[j].get(messageMicro))
                            .computeSize(
                                    WireFormatMicro.getTagFieldNumber(this.tags[j])
                            );
                } catch(IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            return i;
        }

        public boolean readFieldFrom(CodedInputStreamMicro codedInputStreamMicro, int i, MessageMicro<?> messageMicro) {
            int binarySearch = Arrays.binarySearch(this.tags, i);
            if (binarySearch < 0) {
                return false;
            }
            this.fields[binarySearch].setAccessible(true);
            try {
                ((PBField<?>) this.fields[binarySearch].get(messageMicro)).readFrom(codedInputStreamMicro);
            } catch(IllegalAccessException e) {
                e.printStackTrace();
            }
            return true;
        }

        public void writeTo(CodedOutputStreamMicro codedOutputStreamMicro, MessageMicro<?> messageMicro) {
            int i = 0;
            while (true) {
                if (i < this.tags.length) {
                    try {
                        this.fields[i].setAccessible(true);
                        ((PBField<?>) this.fields[i].get(messageMicro)).writeTo(codedOutputStreamMicro, WireFormatMicro.getTagFieldNumber(this.tags[i]));
                    } catch(IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    i++;
                } else {
                    return;
                }
            }
        }
    }
}
