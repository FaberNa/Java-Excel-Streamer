package org.catapano.excelbatcher.handler;

import org.catapano.excelbatcher.config.BatchRow;

import java.util.List;

public final class UnknownSheetLoggingHandler implements SheetBatchHandler {
    @Override public void onSheetStart(String sheetName) {
        System.out.println("⚠️ Foglio non mappato: " + sheetName);
    }
    @Override public void onBatch(String sheetName, long start, long end, List<BatchRow> rows) {}
    @Override public void onSheetEnd(String sheetName, long totalRowsRead) {}
}