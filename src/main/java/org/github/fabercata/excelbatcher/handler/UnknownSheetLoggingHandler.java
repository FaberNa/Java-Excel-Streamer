package org.github.fabercata.excelbatcher.handler;

import org.github.fabercata.excelbatcher.config.BatchRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public final class UnknownSheetLoggingHandler implements SheetBatchHandler {
    private static final Logger log = LoggerFactory.getLogger(UnknownSheetLoggingHandler.class);


    @Override public void onSheetStart(String sheetName) {
        log.info("Sheet not mapped: " + sheetName);
    }
    @Override public void onBatch(String sheetName, long start, long end, List<BatchRow> rows) {
        log.info("Sheet not mapped: " + sheetName + " batch rows " + start + ".." + end + " size=" + rows.size());
    }
    @Override public void onSheetEnd(String sheetName, long totalRowsRead) {
        log.info("Sheet not mapped: " + sheetName + " total rows read=" + totalRowsRead);
    }
}