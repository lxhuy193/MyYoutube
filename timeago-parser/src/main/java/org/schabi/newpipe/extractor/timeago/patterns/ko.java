/**/// DO NOT MODIFY THIS FILE MANUALLY
/**/// This class was automatically generated by "GeneratePatternClasses.java",
/**/// modify the "unique_patterns.json" and re-generate instead.

package org.schabi.newpipe.extractor.timeago.patterns;

import org.schabi.newpipe.extractor.timeago.PatternsHolder;

public class ko extends PatternsHolder {
    private static final String WORD_SEPARATOR = "";
    private static final String[]
            SECONDS  /**/ = {"초"},
            MINUTES  /**/ = {"분"},
            HOURS    /**/ = {"시간"},
            DAYS     /**/ = {"일"},
            WEEKS    /**/ = {"주"},
            MONTHS   /**/ = {"개월"},
            YEARS    /**/ = {"년"};

    private static final ko INSTANCE = new ko();

    public static ko getInstance() {
        return INSTANCE;
    }

    private ko() {
        super(WORD_SEPARATOR, SECONDS, MINUTES, HOURS, DAYS, WEEKS, MONTHS, YEARS);
    }
}