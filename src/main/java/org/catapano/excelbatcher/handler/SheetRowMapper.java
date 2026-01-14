package org.catapano.excelbatcher.handler;


import org.catapano.excelbatcher.config.BatchRow;

public interface SheetRowMapper<T> {
    T map(BatchRow row);
}

