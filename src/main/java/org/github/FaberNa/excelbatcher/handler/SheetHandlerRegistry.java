package org.github.faberNa.excelbatcher.handler;

import org.github.faberNa.excelbatcher.config.BatchRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public final class SheetHandlerRegistry {

    private static final Logger log = LoggerFactory.getLogger(SheetHandlerRegistry.class);


    private final java.util.List<Rule> rules = new java.util.ArrayList<>();
    private SheetBatchHandler fallback = new SheetBatchHandler() {
        @Override
        public void onBatch(String sheetName, long sheetRowStart, long sheetRowEnd, List<BatchRow> rows) {

        }
    };

    public SheetHandlerRegistry on(java.util.function.Predicate<String> sheetMatcher,
                                   java.util.function.Supplier<SheetBatchHandler> handlerSupplier) {
        log.debug("Registering handler for sheets matching: {}", sheetMatcher);
        rules.add(new Rule(sheetMatcher, handlerSupplier));
        return this;
    }

    public SheetHandlerRegistry fallback(java.util.function.Supplier<SheetBatchHandler> handlerSupplier) {
        this.fallback = handlerSupplier.get();
        return this;
    }

    public SheetBatchHandler resolve(String sheetName) {
        for (Rule r : rules) {
            if (r.matcher.test(sheetName)) return r.supplier.get(); // handler “per sheet”
        }
        return fallback;
    }

    private record Rule(java.util.function.Predicate<String> matcher,
                        java.util.function.Supplier<SheetBatchHandler> supplier) {}
}