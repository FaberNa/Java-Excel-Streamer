package org.github.fabercata.excelbatcher.config;

import org.github.fabercata.excelbatcher.handler.SheetBatchHandler;

import java.io.InputStream;

public interface ExcelBatchReader {
    void readXlsx(InputStream xlsx, ExcelBatchOptions options, SheetBatchHandler handler);
}