# Java Excel Streamer

Purpose
- A lightweight library to stream large `.xlsx` files using SAX and minimize memory usage.
- Target: batch processing of rows per sheet with pluggable handlers.
- Supports reading and writing of `.xlsx` files.
- Compatible with Java 25 and above.


##  How to use it

1. Add the library to your project dependencies.
2. Implement a `SheetBatchHandler` to process rows per sheet.
3. Use `XslsSaxBatchReader` to read and process the Excel file.
 Example in test folder.