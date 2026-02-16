package org.github.fabercata.excelbatcher.handler;

import org.github.fabercata.excelbatcher.config.BatchRow;

public final class DispatchingSheetBatchHandler implements SheetBatchHandler {

    private final SheetHandlerRegistry registry;

    // cache per non risolvere ogni batch
    private final java.util.Map<String, SheetBatchHandler> resolved = new java.util.HashMap<>();

    public DispatchingSheetBatchHandler(SheetHandlerRegistry registry) {
        this.registry = java.util.Objects.requireNonNull(registry);
    }

    private SheetBatchHandler delegateFor(String sheetName) {
        return resolved.computeIfAbsent(sheetName, registry::resolve);
    }

    @Override
    public void onSheetStart(String sheetName) {
        delegateFor(sheetName).onSheetStart(sheetName);
    }

    @Override
    public void onBatch(String sheetName, long start, long end, java.util.List<BatchRow> rows) {
        delegateFor(sheetName).onBatch(sheetName, start, end, rows);
    }

    @Override
    public void onSheetEnd(String sheetName, long totalRowsRead) {
        delegateFor(sheetName).onSheetEnd(sheetName, totalRowsRead);
    }
}
