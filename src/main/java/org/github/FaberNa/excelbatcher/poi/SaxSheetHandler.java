package org.github.faberNa.excelbatcher.poi;


import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.model.SharedStrings;
import org.apache.poi.xssf.model.Styles;
import org.github.faberNa.excelbatcher.config.BatchRow;
import org.github.faberNa.excelbatcher.config.ExcelBatchOptions;
import org.github.faberNa.excelbatcher.handler.SheetBatchHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

class SaxSheetHandler extends DefaultHandler implements XSSFSheetXMLHandler.SheetContentsHandler {

    private static final Logger log = LoggerFactory.getLogger(SaxSheetHandler.class);


    private final String sheetName;
    private final ExcelBatchOptions options;
    private final SheetBatchHandler handler;

    private final List<BatchRow> buffer = new ArrayList<>();
    private long totalRowsRead = 0;

    private long currentRow = -1;
    private int currentCol = -1;

    private List<String> currentCells;

    private long firstRowInBatch = -1;
    private boolean headerSkipped = false;

    SaxSheetHandler(String sheetName,
                    Styles styles,
                    SharedStrings sst,
                    ExcelBatchOptions options,
                    SheetBatchHandler handler) {

        this.sheetName = sheetName;
        this.options = options;
        this.handler = handler;

        // collega questa classe al parser POI SAX
        DataFormatter formatter = new DataFormatter();
        // trick: usiamo xssfHandler come delegato SAX
        this.delegate = new XSSFSheetXMLHandler(
                styles, null, sst, this, formatter, false
        );
    }

    private final XSSFSheetXMLHandler delegate;

    @Override
    public void startElement(String uri, String localName, String qName, org.xml.sax.Attributes attributes) {
        try {
            delegate.startElement(uri, localName, qName, attributes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        try {
            delegate.endElement(uri, localName, qName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        try {
            delegate.characters(ch, start, length);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void startRow(int rowNum) {
        log.debug("{} startRow {}", sheetName, rowNum);
        currentRow = rowNum;
        currentCol = -1;
        currentCells = new ArrayList<>();
    }

    @Override
    public void endRow(int rowNum) {
        log.debug("{} endRow {}", sheetName, rowNum);
        if (!options.includeHeaderRow() && !headerSkipped) {
            headerSkipped = true;
            return;
        }

        List<String> cells = options.trimStrings()
                ? currentCells.stream().map(s -> s == null ? "" : s.trim()).toList()
                : List.copyOf(currentCells);

        BatchRow row = new BatchRow(rowNum, cells);
        if (buffer.isEmpty()) firstRowInBatch = rowNum;

        buffer.add(row);
        totalRowsRead++;

        if (buffer.size() >= options.batchSize()) {
            flush(rowNum);
        }
    }

    @Override
    public void cell(String cellReference, String formattedValue, org.apache.poi.xssf.usermodel.XSSFComment comment) {
        // Adding calue same order of columns, but cellReference can be "A1", "C1" (missing B) so we need to track current column index.
        int thisCol = new CellReference(cellReference).getCol();
        // fill empty columns
        for (int i = currentCol + 1; i < thisCol; i++) {
            currentCells.add(""); // oppure null
        }
        currentCells.add(formattedValue);
        currentCol = thisCol;
    }

    @Override
    public void headerFooter(String text, boolean isHeader, String tagName) {
        // ignore
        log.debug("{} headerFooter {}", sheetName, tagName);
    }

    void flush(long lastRowIndex) {
        if (buffer.isEmpty()) return;
        handler.onBatch(sheetName, firstRowInBatch, lastRowIndex, List.copyOf(buffer));
        buffer.clear();
        firstRowInBatch = -1;
    }

    long totalRowsRead() {
        // flush finale
        flush(currentRow);
        return totalRowsRead;
    }
}