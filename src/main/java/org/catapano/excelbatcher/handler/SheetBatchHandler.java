package org.catapano.excelbatcher.handler;


import org.catapano.excelbatcher.config.BatchRow;

import java.util.List;

public interface SheetBatchHandler {
    /** start sheet */
    default void onSheetStart(String sheetName) {}

    /**
     * bath of rows read from sheet
     * sheetRowStart/sheetRowEnd index of first/last row in batch
     */
    void onBatch(String sheetName, long sheetRowStart, long sheetRowEnd, List<BatchRow> rows);

    /** end sheet */
    default void onSheetEnd(String sheetName, long totalRowsRead) {}
}