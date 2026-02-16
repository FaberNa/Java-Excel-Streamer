package org.github.fabercata.excelbatcher.config;

import java.util.List;

public record BatchRow(long rowIndex, List<String> cells) { }