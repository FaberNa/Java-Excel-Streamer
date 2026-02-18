package org.github.faberNa.excelbatcher.config;

import org.github.faberNa.excelbatcher.handler.SheetBatchHandler;

import java.io.InputStream;

public interface ExcelBatchReader {
    void readXlsx(InputStream xlsx, ExcelBatchOptions options, SheetBatchHandler handler);
}