package org.github.fabercata.excelbatcher.config;

public record ExcelBatchOptions(
        int batchSize,
        boolean includeHeaderRow,
        boolean trimStrings
) {
    public ExcelBatchOptions {
        if (batchSize <= 0) throw new IllegalArgumentException("batchSize must be > 0");
    }

    public static ExcelBatchOptions defaults() {
        return new ExcelBatchOptions(500, false, true);
    }
}
