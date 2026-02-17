package org.github.fabercata.excelbatcher.handler;

import org.github.fabercata.excelbatcher.config.BatchRow;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class UnknownSheetLoggingHandlerTest {

    @Test
    void unknownHandler_shouldThrow_onUnknownSheet() {
        var h = new UnknownSheetLoggingHandler();

        assertThatThrownBy(() -> h.onSheetStart("custom"))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessageContaining("Not implemented");
    }

    @Test
    void unknownHandler_shouldThrow_onBatch() {
        var h = new UnknownSheetLoggingHandler();

        assertThatThrownBy(() -> h.onBatch("custom",1,1, List.of(new BatchRow(1, List.of("a", "b")))))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessageContaining("Not implemented");
    }
}