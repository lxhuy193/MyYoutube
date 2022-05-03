/**/// DO NOT MODIFY THIS FILE MANUALLY
/**/// This class was automatically generated by "GeneratePatternClasses.java",
/**/// modify the "unique_patterns.json" and re-generate instead.

package org.schabi.newpipe.extractor.timeago.patterns;

import org.schabi.newpipe.extractor.timeago.PatternsHolder;

public class ja extends PatternsHolder {
    private static final String WORD_SEPARATOR = "";
    private static final String[]
            SECONDS  /**/ = {"秒前"},
            MINUTES  /**/ = {"分前"},
            HOURS    /**/ = {"時間前"},
            DAYS     /**/ = {"日前"},
            WEEKS    /**/ = {"週間前"},
            MONTHS   /**/ = {"か月前"},
            YEARS    /**/ = {"年前"};

    private static final ja INSTANCE = new ja();

    public static ja getInstance() {
        return INSTANCE;
    }

    private ja() {
        super(WORD_SEPARATOR, SECONDS, MINUTES, HOURS, DAYS, WEEKS, MONTHS, YEARS);
    }
}