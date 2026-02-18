package org.github.faberNa.excelbatcher.config;

import java.util.List;

public record BatchRow(long rowIndex, List<String> cells) { }