package org.github.faberNa.excelbatcher.handler;

import org.github.faberNa.excelbatcher.config.BatchRow;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class SheetRowMapperTest {

    private static SheetRowMapper<Object> mapper() {
        return new SheetRowMapper<>() {
            @Override
            public Object map(BatchRow row) {
                return null;
            }
        };
    }

    @Test
    void cell_shouldReturnEmpty_whenCellsIsNull() {
        var m = mapper();
        assertThat(m.cell(null, 0)).isEmpty();
    }

    @Test
    void cell_shouldReturnEmpty_whenIndexIsNegative() {
        var m = mapper();
        assertThat(m.cell(List.of("a", "b"), -1)).isEmpty();
    }

    @Test
    void cell_shouldReturnEmpty_whenIndexIsOutOfBounds() {
        var m = mapper();
        assertThat(m.cell(List.of("a", "b"), 2)).isEmpty();
    }

    @Test
    void cell_shouldReturnTrimmedValue_whenPresentAndNonBlank() {
        var m = mapper();
        assertThat(m.cell(List.of("  Mario  ", "Luigi"), 0)).contains("Mario");
    }

    @Test
    void cell_shouldReturnEmpty_whenCellIsBlankAfterTrim() {
        var m = mapper();
        assertThat(m.cell(List.of("   ", "x"), 0)).isEmpty();
    }
}