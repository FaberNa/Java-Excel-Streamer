package org.github.fabercata.excelbatcher.poi;

import org.apache.poi.xssf.model.SharedStrings;
import org.apache.poi.xssf.model.Styles;
import org.github.fabercata.excelbatcher.config.ExcelBatchOptions;
import org.github.fabercata.excelbatcher.config.ExcelBatchReader;
import org.github.fabercata.excelbatcher.handler.SheetBatchHandler;
import org.github.fabercata.excelbatcher.exception.FailedToReadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;

import java.io.InputStream;
import java.util.function.Supplier;

public class XlsxSaxBatchReader implements ExcelBatchReader {
    private static final Logger log = LoggerFactory.getLogger(XlsxSaxBatchReader.class);


    private final Supplier<XMLReader> xmlReaderSupplier;

    public XlsxSaxBatchReader() {
        this(SecureSaxParserFactory::newXmlReader);
    }

    XlsxSaxBatchReader(Supplier<XMLReader> xmlReaderSupplier) {
        this.xmlReaderSupplier = xmlReaderSupplier;
    }


    @Override
    public void readXlsx(InputStream xlsx, ExcelBatchOptions options, SheetBatchHandler handler) {
        log.info("Reading XLSX sheets from {}", xlsx);
        try (OPCPackage pkg = OPCPackage.open(xlsx)) {
            XSSFReader reader = new XSSFReader(pkg);

            SharedStrings sst =reader.getSharedStringsTable();
            Styles styles = reader.getStylesTable();

            XSSFReader.SheetIterator sheets =
                    (XSSFReader.SheetIterator) reader.getSheetsData();

            while (sheets.hasNext()) {
                try (InputStream sheetStream = sheets.next()) {
                    //get sheet name
                    String sheetPartName = sheets.getSheetName();

                    XMLReader parser = xmlReaderSupplier.get();

                    SaxSheetHandler saxHandler = new SaxSheetHandler(sheetPartName, styles, sst, options, handler);
                    parser.setContentHandler(saxHandler);
                    handler.onSheetStart(sheetPartName);
                    parser.parse(new InputSource(sheetStream));
                    handler.onSheetEnd(sheetPartName, saxHandler.totalRowsRead());
                }
            }
        } catch (Exception e) {
            log.error("Error reading XLSX sheets from {}", xlsx, e);
            throw new FailedToReadException("Failed to read xlsx in batch mode", e);
        }
        log.info("Finished reading XLSX sheets from {}", xlsx);
    }
}