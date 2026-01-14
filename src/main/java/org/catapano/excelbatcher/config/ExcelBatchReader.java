package org.catapano.excelbatcher.config;

import org.catapano.excelbatcher.handler.SheetBatchHandler;

import java.io.InputStream;

public interface ExcelBatchReader {
    void readXlsx(InputStream xlsx, ExcelBatchOptions options, SheetBatchHandler handler);
}