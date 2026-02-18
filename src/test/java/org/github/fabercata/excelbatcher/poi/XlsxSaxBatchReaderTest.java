package org.github.fabercata.excelbatcher.poi;

import org.github.fabercata.excelbatcher.config.ExcelBatchOptions;
import org.github.fabercata.excelbatcher.exception.FailedToReadException;
import org.github.fabercata.excelbatcher.exception.SaxFactoryException;
import org.github.fabercata.excelbatcher.handler.*;
import org.github.fabercata.excelbatcher.model.MyCustomModel;
import org.junit.jupiter.api.Test;
import org.xml.sax.XMLReader;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class XlsxSaxBatchReaderTest {

    @Test
    void reader_ShouldMappingCorrectly_WhenReadFirstLine() {


        var reader = new XlsxSaxBatchReader();

        var options = new ExcelBatchOptions(
                200,   // batchSize: number of rows for batch
                false, // includeHeaderRow: includes first row?
                true   // trimStrings: trim on strings
        );

        //this represents the batches of mapped DTOs produced by CustomHandler.onBatch
        List<List<MyCustomModel>> capturedBatches = new ArrayList<>();
        // Capture what CustomHandler produces (each batch of mapped DTOs)
        var customHandlerSupplier = (java.util.function.Supplier<SheetBatchHandler>) () ->
                new CustomHandler(capturedBatches::add);

        SheetHandlerRegistry registry = new SheetHandlerRegistry()
                .on(name -> name.endsWith("custom"), customHandlerSupplier)
                .fallback(UnknownSheetLoggingHandler::new);


        SheetBatchHandler facade = new DispatchingSheetBatchHandler(registry);
        String fileName = "/oneline.xlsx";
        try (InputStream is = XlsxSaxBatchReaderTest.class.getResourceAsStream(fileName)) {
            reader.readXlsx(is, options, facade);


            // Basic verification: CustomHandler.onBatch was invoked and produced at least one batch
            assertFalse(capturedBatches.isEmpty(), "Expected at least one batch from CustomHandler");
            // getFirst on capturedBatches give u the firstBatch round of batch
            List<MyCustomModel> firstBatch = capturedBatches.getFirst();
            assertFalse(firstBatch.isEmpty(), "Expected at least one mapped row in the firstBatch batch");
            // this gives u the first row of the first batch of mapped DTOs produced by CustomHandler.onBatchq
            assertThat(firstBatch.getFirst().getAmount()).isEqualTo(BigDecimal.valueOf(125));
            assertThat(firstBatch.getFirst().getDate()).isEqualTo(LocalDate.of(2025, 10, 28));
            assertThat(firstBatch.getFirst().isActive()).isFalse();
            assertThat(firstBatch.getFirst().getQuantity()).isEqualTo(13);
            assertThat(firstBatch.getFirst().getName()).isEqualTo("Jose");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void reader_ShouldReadinBatch_whenReadMoreLines() {

        var reader = new XlsxSaxBatchReader();

        var options = new ExcelBatchOptions(
                200,   // batchSize: number of rows for batch
                false, // includeHeaderRow: includes first row?
                true   // trimStrings: trim on strings
        );

        //this represents the batches of mapped DTOs produced by CustomHandler.onBatch
        List<List<MyCustomModel>> capturedBatches = new ArrayList<>();
        // Capture what CustomHandler produces (each batch of mapped DTOs)
        var customHandlerSupplier = (java.util.function.Supplier<SheetBatchHandler>) () ->
                new CustomHandler(capturedBatches::add);

        SheetHandlerRegistry registry = new SheetHandlerRegistry()
                .on(name -> name.endsWith("custom"), customHandlerSupplier)
                .fallback(UnknownSheetLoggingHandler::new);


        SheetBatchHandler facade = new DispatchingSheetBatchHandler(registry);
        String fileName = "/400rows.xlsx";
        try (InputStream is = XlsxSaxBatchReaderTest.class.getResourceAsStream(fileName)) {
            reader.readXlsx(is, options, facade);

            // Basic verification: CustomHandler.onBatch was invoked and produced at least one batch
            assertFalse(capturedBatches.isEmpty(), "Expected at least one batch from CustomHandler");
            // getFirst on capturedBatches give u the firstBatch round of batch
            List<MyCustomModel> firstBatch = capturedBatches.getFirst();
            assertFalse(firstBatch.isEmpty(), "Expected at least one mapped row in the firstBatch batch");
            // this gives u the first row of the first batch of mapped DTOs produced by CustomHandler.onBatchq
            assertThat(firstBatch.size()).isEqualTo(200);
            assertThat(firstBatch.getFirst().getName()).isEqualTo("Maria");
            assertThat(capturedBatches.size()).isEqualTo(2);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    void reader_ShouldThrowException_whenFileNotFound() throws Exception {


        // given: xlsx valido preso dalle risorse di test
        try (InputStream xlsx = createMinimalXlsx()) {
            assertNotNull(xlsx, "Test resource /xlsx/minimal.xlsx not found");

            ExcelBatchOptions options = mock(ExcelBatchOptions.class);
            SheetBatchHandler handler = mock(SheetBatchHandler.class);

            Supplier<XMLReader> failingSupplier = () -> {
                throw new SaxFactoryException("Unable to create secure SAX parser", new RuntimeException("Error creating XMLReader"));
            };

            var reader = new XlsxSaxBatchReader(failingSupplier);

            // when
            FailedToReadException ex = assertThrows(FailedToReadException.class, () ->
                    reader.readXlsx(xlsx, options, handler)
            );

            // then
            assertTrue(ex.getCause() instanceof SaxFactoryException);
            assertEquals("Failed to read xlsx in batch mode", ex.getMessage());
        }
    }

    private InputStream createMinimalXlsx() throws Exception {
        try (org.apache.poi.xssf.usermodel.XSSFWorkbook workbook =
                     new org.apache.poi.xssf.usermodel.XSSFWorkbook()) {

            workbook.createSheet("Sheet1")
                    .createRow(0)
                    .createCell(0)
                    .setCellValue("test");

            java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
            workbook.write(out);
            return new java.io.ByteArrayInputStream(out.toByteArray());
        }
    }
}
