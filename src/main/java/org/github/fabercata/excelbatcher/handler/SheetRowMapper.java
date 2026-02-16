package org.github.fabercata.excelbatcher.handler;


import org.github.fabercata.excelbatcher.config.BatchRow;

import java.util.List;
import java.util.Optional;

public interface SheetRowMapper<T> {
    T map(BatchRow row);


    default Optional<String> cell(List<String> cells, int idx) {
        return Optional.ofNullable(cells)
                .filter(c -> idx >= 0 && idx < c.size())
                .map(c -> c.get(idx))
                .map(s -> s == null ? null : s.trim())
                .filter(s -> !s.isEmpty());
    }
}

