package org.catapano;

import org.catapano.excelbatcher.ExcelBatchOptions;
import org.catapano.excelbatcher.SheetBatchHandler;
import org.catapano.handler.DatiContrattoHandler;
import org.catapano.handler.DispatchingSheetBatchHandler;
import org.catapano.handler.SheetHandlerRegistry;
import org.catapano.handler.UnknownSheetLoggingHandler;
import org.catapano.poi.XlsxSaxBatchReader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        var reader = new XlsxSaxBatchReader();

        var options = new ExcelBatchOptions(
                200,   // batchSize: quante righe per batch
                false, // includeHeaderRow: includere la prima riga?
                true   // trimStrings: trim sulle stringhe
        );

        SheetHandlerRegistry registry = new SheetHandlerRegistry()
                .on(name -> name.endsWith("datiContratto.xml"), DatiContrattoHandler::new)
                .fallback(UnknownSheetLoggingHandler::new);

        SheetBatchHandler facade = new DispatchingSheetBatchHandler(registry);
        try (InputStream is = Files.newInputStream(Path.of("input.xlsx"))) {
            reader.readXlsx(is, options, facade);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}