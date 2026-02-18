package org.github.faberNa.excelbatcher.handler;

import org.github.faberNa.excelbatcher.config.BatchRow;
import org.github.faberNa.excelbatcher.model.MyCustomModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.Consumer;

public class CustomHandler  implements SheetBatchHandler{

    private static final Logger log = LoggerFactory.getLogger(CustomHandler.class);

    private final Consumer<List<MyCustomModel>> out;
    public CustomHandler(Consumer<List<MyCustomModel>> out) { this.out = out; }

    private final CustomRowMapper mapper = new CustomRowMapper(1, 2, 3, 5,7);

    @Override
    public void onSheetStart(String sheetName) {
        SheetBatchHandler.super.onSheetStart(sheetName);
    }

    @Override
    public void onBatch(String sheetName, long sheetRowStart, long sheetRowEnd, List<BatchRow> rows) {
        log.debug("{} onBatch rows: {}", sheetName, rows.size());
        // mapping each ciascuna BatchRow in MyCustomModel
        List<MyCustomModel> dtos = rows.stream()
                .map(mapper::map)
                .toList();

        out.accept(dtos);
    }

    @Override
    public void onSheetEnd(String sheetName, long totalRowsRead) {
        SheetBatchHandler.super.onSheetEnd(sheetName, totalRowsRead);
    }
}
