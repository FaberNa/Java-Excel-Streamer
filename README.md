[![codecov](https://codecov.io/github/FaberNa/Java-Excel-Streamer/graph/badge.svg?token=FS6PESV0XV)](https://codecov.io/github/FaberNa/Java-Excel-Streamer) [![Maven Central](https://img.shields.io/maven-central/v/io.github.faberna/java-excel-streamer.svg)](https://central.sonatype.com/artifact/io.github.faberna/java-excel-streamer) ![Java](https://img.shields.io/badge/Java-25-blue)
# Java Excel Streamer

Purpose
- A lightweight library to stream large `.xlsx` files using SAX and minimize memory usage.
- Target: batch processing of rows per sheet with pluggable handlers.
- Supports reading and writing of `.xlsx` files.
- Compatible with Java 25 and above.

---

## Concepts: Model, RowMapper, Handler

This library focuses on **streaming Excel reading and batching**.  
You provide the domain mapping logic.

### 1) Model (e.g. `MyCustomModel`)

The model represents the **final structure of a single Excel row** after mapping.

In other words:

> “What does one row of this sheet mean in my application?”

Typically, you define **one model per sheet** (or per row layout).

### 2) RowMapper (e.g. `MyCustomRowMapper`)
The `RowMapper` is responsible for converting a raw `BatchRow`
(list of cell values as `String`) into your domain model.

> “How map the value from xlsx  to my model ?”

Typically, you define **one mapper per sheet** .


### 3) Handler (e.g. `MyCustomSheetBatchHandler`)
The Handler defines what happens when a batch of rows is read from a sheet.

It receives raw BatchRow objects, maps them using the RowMapper,
and forwards the result to your business logic.

> “What you want to do when parsing one row of your sheet ?”

You can map your BatchRow to your domain model and then, for example, save it to a database, send it to another service, or perform any other processing.


### Design Principle

- **One sheet → One Model**
- **One sheet → One RowMapper**
- **One sheet → One Handler**

Each Excel sheet should have its own mapping and behavior layer.
This keeps the library generic and your domain logic clean, explicit, and testable.


## Easy example (from the test case)

Below is a minimal, copy-paste friendly example inspired by `XlsxSaxBatchReaderTest#reader_ShouldMappingCorrectly_WhenReadFirstLine`.

It shows how to:
1) define a **Model** that represents one Excel row
2) implement a **RowMapper** with the mapping rules
3) implement a **Handler** that receives batches and forwards mapped DTOs
4) wire everything with **SheetHandlerRegistry** + **DispatchingSheetBatchHandler**

---

```java
import org.github.faberNa.excelbatcher.config.ExcelBatchOptions;
import org.github.faberNa.excelbatcher.handler.DispatchingSheetBatchHandler;
import org.github.faberNa.excelbatcher.handler.SheetBatchHandler;
import org.github.faberNa.excelbatcher.handler.SheetHandlerRegistry;
import org.github.faberNa.excelbatcher.handler.UnknownSheetLoggingHandler;
import org.github.faberNa.excelbatcher.poi.XlsxSaxBatchReader;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ExampleApp {

 public static void main(String[] args) throws Exception {

  var reader = new XlsxSaxBatchReader();

  var options = new ExcelBatchOptions(
          200,   // batchSize
          false, // includeHeaderRow
          true   // trimStrings
  );

  // Where we collect batches (in real apps you might write to DB, Kafka, etc.)
  List<List<MyCustomModel>> capturedBatches = new ArrayList<>();

  var registry = new SheetHandlerRegistry()
          // route sheet named "custom" (same name used in the tests)
          .on(name -> name.equalsIgnoreCase("custom"),
                  () -> new CustomHandler(capturedBatches::add))
          .fallback(UnknownSheetLoggingHandler::new);

  SheetBatchHandler dispatcher = new DispatchingSheetBatchHandler(registry);

  try (InputStream is = ExampleApp.class.getResourceAsStream("/oneline.xlsx")) {
   if (is == null) throw new IllegalArgumentException("Resource /oneline.xlsx not found");
   reader.readXlsx(is, options, dispatcher);
  }

  // Example: access first mapped row
  MyCustomModel firstRow = capturedBatches.getFirst().getFirst();
  System.out.println(firstRow);
 }
}
```
