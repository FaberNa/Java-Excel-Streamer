package org.github.fabercata.excelbatcher.handler;


import org.github.fabercata.excelbatcher.config.BatchRow;

import java.util.List;

/**
 * Read rows handler  .
 */
public interface SheetBatchHandler {
    /** start sheet */
    default void onSheetStart(String sheetName) {}

    /**
     * batch of rows read from sheet
     * sheetRowStart/sheetRowEnd index of first/last row in batch
     */
    void onBatch(String sheetName, long sheetRowStart, long sheetRowEnd, List<BatchRow> rows);

    /** end sheet */
    default void onSheetEnd(String sheetName, long totalRowsRead) {}
}