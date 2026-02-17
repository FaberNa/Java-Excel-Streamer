package org.github.fabercata.excelbatcher.poi;

import org.github.fabercata.excelbatcher.config.BatchRow;
import org.github.fabercata.excelbatcher.config.ExcelBatchOptions;
import org.github.fabercata.excelbatcher.handler.SheetBatchHandler;
import org.apache.poi.xssf.model.Styles;
import org.apache.poi.xssf.model.SharedStrings;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;


import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


class SaxSheetHandlerTest {
    private static final class CapturingHandler implements SheetBatchHandler {

        long lastStart;
        long lastEnd;
        List<BatchRow> lastRows;
        int batchCalls = 0;

        @Override public void onSheetStart(String sheetName) {}

        @Override
        public void onBatch(String sheetName, long start, long end, List<BatchRow> rows) {
            batchCalls++;
            lastStart = start;
            lastEnd = end;
            lastRows = List.copyOf(rows);
        }

        @Override public void onSheetEnd(String sheetName, long totalRowsRead) {}
    }

    @Test
    void endRow_shouldFlush_whenBatchSizeReached() {

        Styles styles = Mockito.mock(Styles.class);
        SharedStrings sst = Mockito.mock(SharedStrings.class);

        var options = new ExcelBatchOptions(2, true, true);
        var handler = new CapturingHandler();

        var sut = new SaxSheetHandler("custom", styles, sst, options, handler);

        // first row
        sut.startRow(1);
        sut.cell("A2", " a ", null);
        sut.endRow(1);

        // second row -> should flush
        sut.startRow(2);
        sut.cell("A3", " b ", null);
        sut.endRow(2);

        assertThat(handler.batchCalls).isEqualTo(1);
        assertThat(handler.lastStart).isEqualTo(1);
        assertThat(handler.lastEnd).isEqualTo(2);
        assertThat(handler.lastRows).hasSize(2);
        assertThat(handler.lastRows.get(0).cells()).containsExactly("a");
        assertThat(handler.lastRows.get(1).cells()).containsExactly("b");
    }

    @Test
    void endRow_shouldSkipHeader_whenIncludeHeaderFalse() {

        Styles styles = Mockito.mock(Styles.class);
        SharedStrings sst = Mockito.mock(SharedStrings.class);

        var options = new ExcelBatchOptions(1, false, true);
        var handler = new CapturingHandler();

        var sut = new SaxSheetHandler("custom", styles, sst, options, handler);

        // header row -> skipped
        sut.startRow(0);
        sut.cell("A1", "header", null);
        sut.endRow(0);

        assertThat(handler.batchCalls).isZero();

        // next row -> should flush immediately (batchSize=1)
        sut.startRow(1);
        sut.cell("A2", "value", null);
        sut.endRow(1);

        assertThat(handler.batchCalls).isEqualTo(1);
        org.assertj.core.api.Assertions.assertThat(handler.lastRows.get(0).rowIndex()).isEqualTo(1L);
    }
}