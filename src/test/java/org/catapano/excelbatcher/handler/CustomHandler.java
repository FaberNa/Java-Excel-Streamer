package org.catapano.excelbatcher.handler;

import org.catapano.excelbatcher.config.BatchRow;
import org.catapano.excelbatcher.model.MyCustomModel;

import java.util.List;
import java.util.function.Consumer;

public class CustomHandler  implements SheetBatchHandler{

    private final Consumer<List<MyCustomModel>> out;
    public CustomHandler(Consumer<List<MyCustomModel>> out) { this.out = out; }

    private final CustomRowMapper mapper = new CustomRowMapper(1, 2, 3, 5,7);

    @Override
    public void onSheetStart(String sheetName) {
        SheetBatchHandler.super.onSheetStart(sheetName);
    }

    @Override
    public void onBatch(String sheetName, long sheetRowStart, long sheetRowEnd, List<BatchRow> rows) {
        // mappo ciascuna BatchRow in MyCustomModel
        List<MyCustomModel> dtos = rows.stream()
                .map(mapper::map)
                .toList();

        dtos.stream().limit(5).forEach(dc -> System.out.println("  dto=" + dc));
        out.accept(dtos);
    }

    @Override
    public void onSheetEnd(String sheetName, long totalRowsRead) {
        SheetBatchHandler.super.onSheetEnd(sheetName, totalRowsRead);
    }
}
