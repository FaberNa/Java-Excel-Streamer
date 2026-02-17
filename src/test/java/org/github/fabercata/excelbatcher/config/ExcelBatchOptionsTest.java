package org.github.fabercata.excelbatcher.config;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class ExcelBatchOptionsTest {

    @Test
    void shouldCreateOptions_whenBatchSizeIsPositive() {
        var opt = new ExcelBatchOptions(200, false, true);

        assertThat(opt.batchSize()).isEqualTo(200);
        assertThat(opt.includeHeaderRow()).isFalse();
        assertThat(opt.trimStrings()).isTrue();
    }

    @Test
    void defaults_shouldReturnExpectedValues() {
        var opt = ExcelBatchOptions.defaults();

        assertThat(opt.batchSize()).isEqualTo(500);
        assertThat(opt.includeHeaderRow()).isFalse();
        assertThat(opt.trimStrings()).isTrue();
    }

    @Test
    void shouldThrow_whenBatchSizeIsZero() {
        assertThatThrownBy(() -> new ExcelBatchOptions(0, false, true))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("batchSize must be > 0");
    }

    @Test
    void shouldThrow_whenBatchSizeIsNegative() {
        assertThatThrownBy(() -> new ExcelBatchOptions(-1, true, false))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("batchSize must be > 0");
    }
}