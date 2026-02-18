package org.github.faberNa.excelbatcher.util;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

public final class CellParsers {
    private CellParsers() {}

    public static Optional<Long> parseLong(String s) {
        return Optional.ofNullable(s)
                .map(String::trim)
                .filter(str -> !str.isBlank())
                .flatMap(str -> {
                    try {
                        return Optional.of(Long.valueOf(str));
                    } catch (NumberFormatException e) {
                        return Optional.empty();
                    }
                });

    }

    public static Optional<Integer> parseInt(String s) {
        return Optional.ofNullable(s)
                .map(String::trim)
                .filter(str -> !str.isBlank())
                .flatMap(str -> {
                    try {
                        return Optional.of(Integer.valueOf(str));
                    } catch (NumberFormatException e) {
                        return Optional.empty();
                    }
                });
    }

    public static Optional<BigDecimal> parseBigDecimal(String s) {

        return Optional.ofNullable(s)
                .map(String::trim)
                .filter(str -> !str.isBlank())
                .flatMap(str -> {
                    try {
                        return Optional.of(new BigDecimal(str));
                    } catch (NumberFormatException e) {
                        return Optional.empty();
                    }
                });
    }

    public static Optional<LocalDate> parseDate(String s, DateTimeFormatter formatter) {
        return Optional.ofNullable(s)
                .map(String::trim)
                .filter(str -> !str.isBlank())
                .flatMap(str -> {
                    try {
                        return Optional.of(LocalDate.parse(str, formatter));
                    } catch (DateTimeParseException e) {
                        return Optional.empty();
                    }
                });
    }


    public static java.util.Optional<Boolean> parseBoolean(String s) {
        return Optional.ofNullable(s)
                .map(String::trim)
                .filter(str -> !str.isBlank())
                .map(str -> str.toLowerCase(java.util.Locale.ROOT))
                .flatMap(str -> java.util.stream.Stream.of(
                                        java.util.Map.entry(java.util.Set.of("y", "yes", "true", "1",1,"S","SI","s","si"), Boolean.TRUE),
                                        java.util.Map.entry(java.util.Set.of("n", "no", "false", "0",0,"N","NO"), Boolean.FALSE)
                                )
                                .filter(entry -> entry.getKey().contains(str))
                                .findFirst()
                                .map(java.util.Map.Entry::getValue)
                );
    }

}
