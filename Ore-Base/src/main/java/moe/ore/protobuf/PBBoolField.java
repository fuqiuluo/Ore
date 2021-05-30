package moe.ore.protobuf;

public final class PBBoolField extends PBPrimitiveField<Boolean> {
	public static final PBBoolField __repeatHelper__ = new PBBoolField(false, false);
	private boolean value = false;

	public PBBoolField(boolean z, boolean has) {
		set(z, has);
	}

	public void clear(Object obj) {
		if (obj instanceof Boolean) {
			this.value = (Boolean) obj;
		} else {
			this.value = false;
		}
		setHasFlag(false);
	}

	public int computeSize(int i) {
		if (has()) {
			return CodedOutputStreamMicro.computeBoolSize(i, this.value);
		}
		return 0;
	}

	protected int computeSizeDirectly(int i, Boolean bool) {
		return CodedOutputStreamMicro.computeBoolSize(i, bool);
	}

	protected void copyFrom(PBField<Boolean> pBField) {
		PBBoolField pBBoolField = (PBBoolField) pBField;
		set(pBBoolField.value, pBBoolField.has());
	}

	public boolean get() {
		return this.value;
	}

	public void readFrom(CodedInputStreamMicro codedInputStreamMicro) {
		this.value = codedInputStreamMicro.readBool();
		setHasFlag(true);
	}

	protected Boolean readFromDirectly(CodedInputStreamMicro codedInputStreamMicro) {
		return codedInputStreamMicro.readBool();
	}

	public void set(boolean z) {
		set(z, true);
	}

	public void set(boolean z, boolean has) {
		this.value = z;
		setHasFlag(has);
	}

	public void writeTo(CodedOutputStreamMicro codedOutputStreamMicro, int i) {
		if (has()) {
			codedOutputStreamMicro.writeBool(i, this.value);
		}
	}

	protected void writeToDirectly(CodedOutputStreamMicro codedOutputStreamMicro, int i, Boolean bool) {
		codedOutputStreamMicro.writeBool(i, bool);
	}
}
