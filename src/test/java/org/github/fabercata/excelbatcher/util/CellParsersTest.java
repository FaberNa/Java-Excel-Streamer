package org.github.fabercata.excelbatcher.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CellParsersTest {

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