package org.github.fabercata.excelbatcher.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CellParsersTest {


    // ---------- parseDate ----------

    @Test
    void parseDate_shouldReturnEmpty_whenNullOrBlank() {
        var fmt = DateTimeFormatter.ISO_LOCAL_DATE;
        assertThat(CellParsers.parseDate(null, fmt)).isEmpty();
        assertThat(CellParsers.parseDate("", fmt)).isEmpty();
        assertThat(CellParsers.parseDate("   ", fmt)).isEmpty();
    }

    @Test
    void parseDate_shouldParseTrimmedDate_withGivenFormatter() {
        var fmt = DateTimeFormatter.ISO_LOCAL_DATE;

        assertThat(CellParsers.parseDate(" 2026-02-16 ", fmt))
                .contains(LocalDate.of(2026, 2, 16));
    }

    @Test
    void parseDate_shouldReturnEmpty_whenInvalidForFormatter() {
        var fmt = DateTimeFormatter.ISO_LOCAL_DATE;

        assertThat(CellParsers.parseDate("16/02/2026", fmt)).isEmpty();
        assertThat(CellParsers.parseDate("not-a-date", fmt)).isEmpty();
    }

    // ---------- parseBoolean ----------

    @Test
    void parseBoolean_shouldReturnEmpty_whenNullOrBlank() {
        assertThat(CellParsers.parseBoolean(null)).isEmpty();
        assertThat(CellParsers.parseBoolean("")).isEmpty();
        assertThat(CellParsers.parseBoolean("   ")).isEmpty();
    }

    @Test
    void parseBoolean_shouldParseTrueValues_caseInsensitive() {
        assertThat(CellParsers.parseBoolean("true")).contains(true);
        assertThat(CellParsers.parseBoolean("YES")).contains(true);
        assertThat(CellParsers.parseBoolean(" y ")).contains(true);
        assertThat(CellParsers.parseBoolean("1")).contains(true);
        assertThat(CellParsers.parseBoolean("si")).contains(true);
        assertThat(CellParsers.parseBoolean("S")).contains(true);
    }

    @Test
    void parseBoolean_shouldParseFalseValues_caseInsensitive() {
        assertThat(CellParsers.parseBoolean("false")).contains(false);
        assertThat(CellParsers.parseBoolean("NO")).contains(false);
        assertThat(CellParsers.parseBoolean(" n ")).contains(false);
        assertThat(CellParsers.parseBoolean("0")).contains(false);
    }

    @Test
    void parseBoolean_shouldReturnEmpty_whenUnknownToken() {
        assertThat(CellParsers.parseBoolean("maybe")).isEmpty();
        assertThat(CellParsers.parseBoolean("2")).isEmpty();
        assertThat(CellParsers.parseBoolean("ok")).isEmpty();
    }
    // ---------- parseBigDecimal ----------

    @Test
    void parseBigDecimal_shouldReturnEmpty_whenNullOrBlank() {
        assertThat(CellParsers.parseBigDecimal(null)).isEmpty();
        assertThat(CellParsers.parseBigDecimal("")).isEmpty();
        assertThat(CellParsers.parseBigDecimal("   ")).isEmpty();
    }

    @Test
    void parseBigDecimal_shouldParseTrimmedDecimal() {
        assertThat(CellParsers.parseBigDecimal("  12.30 ")).contains(new BigDecimal("12.30"));
        assertThat(CellParsers.parseBigDecimal("-0.01")).contains(new BigDecimal("-0.01"));
        assertThat(CellParsers.parseBigDecimal("1E3")).contains(new BigDecimal("1E3"));
    }

    @Test
    void parseBigDecimal_shouldReturnEmpty_whenInvalid() {
        assertThat(CellParsers.parseBigDecimal("12,30")).isEmpty(); // virgola non valida per BigDecimal default
        assertThat(CellParsers.parseBigDecimal("abc")).isEmpty();
    }

    @Test
    @DisplayName("null -> Optional.empty")
    void nullInput_returnsEmpty() {
        assertTrue(CellParsers.parseInt(null).isEmpty());
    }

    @Test
    @DisplayName("empty string -> Optional.empty")
    void emptyString_returnsEmpty() {
        assertTrue(CellParsers.parseInt("").isEmpty());
    }

    @Test
    @DisplayName("blank string (spaces) -> Optional.empty")
    void blankSpaces_returnsEmpty() {
        assertTrue(CellParsers.parseInt("   ").isEmpty());
    }

    @Test
    @DisplayName("blank string (tabs/newlines) -> Optional.empty")
    void blankWhitespace_returnsEmpty() {
        assertTrue(CellParsers.parseInt("\t\n\r").isEmpty());
    }

    @Test
    @DisplayName("valid integer -> Optional.of(value)")
    void validInteger_returnsValue() {
        Optional<Integer> result = CellParsers.parseInt("42");
        assertEquals(Optional.of(42), result);
    }

    @Test
    @DisplayName("valid integer with leading/trailing spaces -> trimmed and parsed")
    void validIntegerWithSpaces_isTrimmedAndParsed() {
        assertEquals(Optional.of(7), CellParsers.parseInt("  7  "));
    }

    @Test
    @DisplayName("valid + sign -> parsed")
    void plusSign_isParsed() {
        assertEquals(Optional.of(12), CellParsers.parseInt("+12"));
    }

    @Test
    @DisplayName("valid negative -> parsed")
    void negative_isParsed() {
        assertEquals(Optional.of(-5), CellParsers.parseInt("-5"));
    }

    @Test
    @DisplayName("non-numeric -> Optional.empty")
    void nonNumeric_returnsEmpty() {
        assertTrue(CellParsers.parseInt("abc").isEmpty());
    }

    @Test
    @DisplayName("numeric with trailing text -> Optional.empty")
    void numericWithTrailingText_returnsEmpty() {
        assertTrue(CellParsers.parseInt("12abc").isEmpty());
    }

    @Test
    @DisplayName("decimal number -> Optional.empty")
    void decimal_returnsEmpty() {
        assertTrue(CellParsers.parseInt("3.14").isEmpty());
    }

    @Test
    @DisplayName("overflow (> Integer.MAX_VALUE) -> Optional.empty")
    void overflow_returnsEmpty() {
        // Integer.MAX_VALUE = 2147483647
        assertTrue(CellParsers.parseInt("2147483648").isEmpty());
    }

    // ---------- parseLong ----------

    @Test
    void parseLong_shouldReturnEmpty_whenNullOrBlank() {
        assertThat(CellParsers.parseLong(null)).isEmpty();
        assertThat(CellParsers.parseLong("")).isEmpty();
        assertThat(CellParsers.parseLong("   ")).isEmpty();
    }

    @Test
    void parseLong_shouldParseTrimmedLong() {
        assertThat(CellParsers.parseLong("  123  ")).contains(123L);
        assertThat(CellParsers.parseLong("0")).contains(0L);
        assertThat(CellParsers.parseLong("-7")).contains(-7L);
    }

    @Test
    void parseLong_shouldReturnEmpty_whenInvalid() {
        assertThat(CellParsers.parseLong("12.3")).isEmpty();
        assertThat(CellParsers.parseLong("abc")).isEmpty();
    }


    @Test
    @DisplayName("underflow (< Integer.MIN_VALUE) -> Optional.empty")
    void underflow_returnsEmpty() {
        // Integer.MIN_VALUE = -2147483648
        assertTrue(CellParsers.parseInt("-2147483649").isEmpty());
    }

    @Test
    @DisplayName("exact bounds are parsed")
    void bounds_areParsed() {
        assertEquals(Optional.of(Integer.MAX_VALUE), CellParsers.parseInt("2147483647"));
        assertEquals(Optional.of(Integer.MIN_VALUE), CellParsers.parseInt("-2147483648"));
    }
}