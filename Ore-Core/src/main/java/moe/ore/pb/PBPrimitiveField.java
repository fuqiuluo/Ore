package moe.ore.pb;

import moe.ore.util.DebugUtil;

public abstract class PBPrimitiveField<T> extends PBField<T> {
	private boolean hasFlag = false;

	public final boolean has() {
		return this.hasFlag;
	}

	public final T setHasFlag(boolean z) {
		this.hasFlag = z;
		return DebugUtil.forcedConvert(null, this);
	}
}
