package org.github.fabercata.excelbatcher.handler;


import org.github.fabercata.excelbatcher.config.BatchRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Read rows handler  .
 */
public interface SheetBatchHandler {

     Logger log = LoggerFactory.getLogger(SheetBatchHandler.class);

    /** start sheet */
    default void onSheetStart(String sheetName) {
        log.info("Start reading sheet: " + sheetName);
    }

    /**
     * batch of rows read from sheet
     * sheetRowStart/sheetRowEnd index of first/last row in batch
     */
    void onBatch(String sheetName, long sheetRowStart, long sheetRowEnd, List<BatchRow> rows);

    /** end sheet */
    default void onSheetEnd(String sheetName, long totalRowsRead) {
        log.info("Finished reading sheet: {} total rows read={}", sheetName, totalRowsRead);
    }
}