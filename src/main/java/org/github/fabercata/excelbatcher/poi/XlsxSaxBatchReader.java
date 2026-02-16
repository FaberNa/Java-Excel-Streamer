package org.github.fabercata.excelbatcher.poi;

import org.apache.poi.xssf.model.SharedStrings;
import org.apache.poi.xssf.model.Styles;
import org.github.fabercata.excelbatcher.config.ExcelBatchOptions;
import org.github.fabercata.excelbatcher.config.ExcelBatchReader;
import org.github.fabercata.excelbatcher.handler.SheetBatchHandler;
import org.github.fabercata.excelbatcher.exception.FailedToReadException;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;

import java.io.InputStream;

public class XlsxSaxBatchReader implements ExcelBatchReader {

    @Override
    public void readXlsx(InputStream xlsx, ExcelBatchOptions options, SheetBatchHandler handler) {
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

                    XMLReader parser = SecureSaxParserFactory.newXmlReader();

                    SaxSheetHandler saxHandler = new SaxSheetHandler(sheetPartName, styles, sst, options, handler);
                    parser.setContentHandler(saxHandler);
                    handler.onSheetStart(sheetPartName);
                    parser.parse(new InputSource(sheetStream));
                    handler.onSheetEnd(sheetPartName, saxHandler.totalRowsRead());
                }
            }
        } catch (Exception e) {
            throw new FailedToReadException("Failed to read xlsx in batch mode", e);
        }
    }
}
