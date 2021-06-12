package moe.ore.helper.bytes


@PublishedApi
internal fun Long.checkSizeOrError(max: Long): Long =
    if (this >= max) error("value $this is greater than its expected maximum value $max")
    else this