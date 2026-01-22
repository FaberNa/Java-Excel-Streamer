package org.catapano.excelbatcher;

import org.catapano.excelbatcher.config.ExcelBatchOptions;
import org.catapano.excelbatcher.handler.*;
import org.catapano.excelbatcher.model.MyCustomModel;
import org.catapano.excelbatcher.poi.XlsxSaxBatchReader;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class XslsSaxBatchReaderTest {

    @Test
    public void test() {

        var reader = new XlsxSaxBatchReader();

        var options = new ExcelBatchOptions(
                200,   // batchSize: quante righe per batch
                false, // includeHeaderRow: includere la prima riga?
                true   // trimStrings: trim sulle stringhe
        );
        List<List<MyCustomModel>> capturedBatches = new ArrayList<>();
        // Capture what CustomHandler produces (each batch of mapped DTOs)
        var customHandlerSupplier = (java.util.function.Supplier<SheetBatchHandler>) () ->
                new CustomHandler(capturedBatches::add);

        SheetHandlerRegistry registry = new SheetHandlerRegistry()
                .on(name -> name.endsWith("datiContratto"), DatiContrattoHandler::new)
                .on(name -> name.endsWith("custom"), customHandlerSupplier)
                .fallback(UnknownSheetLoggingHandler::new);



        SheetBatchHandler facade = new DispatchingSheetBatchHandler(registry);
        String fileName = "/test.xlsx";
        try (InputStream is = XslsSaxBatchReaderTest.class.getResourceAsStream(fileName)) {
            if (is == null) {
                throw new RuntimeException(
                        String.format("Resource not found on classpath: %s", fileName)
                );
            }
            reader.readXlsx(is, options, facade);



            // Basic verification: CustomHandler.onBatch was invoked and produced at least one batch
            assertFalse(capturedBatches.isEmpty(), "Expected at least one batch from CustomHandler");
            assertFalse(capturedBatches.getFirst().isEmpty(), "Expected at least one mapped row in the first batch");
            assertThat(capturedBatches.getFirst().getFirst().getAmount()).isEqualTo(BigDecimal.valueOf(125));
            assertThat(capturedBatches.getFirst().getFirst().getDate()).isEqualTo(LocalDate.of(2025,10,28));
            assertThat(capturedBatches.getFirst().getFirst().isActive()).isFalse();
            assertThat(capturedBatches.getFirst().getFirst().getQuantity()).isEqualTo(13);
            assertThat(capturedBatches.getFirst().getFirst().getName()).isEqualTo("Jose");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
