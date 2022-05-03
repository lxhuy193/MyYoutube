/**/// DO NOT MODIFY THIS FILE MANUALLY
/**/// This class was automatically generated by "GeneratePatternClasses.java",
/**/// modify the "unique_patterns.json" and re-generate instead.

package org.schabi.newpipe.extractor.timeago.patterns;

import org.schabi.newpipe.extractor.timeago.PatternsHolder;

public class fa extends PatternsHolder {
    private static final String WORD_SEPARATOR = " ";
    private static final String[]
            SECONDS  /**/ = {"ثانیه"},
            MINUTES  /**/ = {"دقیقه"},
            HOURS    /**/ = {"ساعت"},
            DAYS     /**/ = {"روز"},
            WEEKS    /**/ = {"هفته"},
            MONTHS   /**/ = {"ماه"},
            YEARS    /**/ = {"سال"};

    private static final fa INSTANCE = new fa();

    public static fa getInstance() {
        return INSTANCE;
    }

    private fa() {
        super(WORD_SEPARATOR, SECONDS, MINUTES, HOURS, DAYS, WEEKS, MONTHS, YEARS);
    }
}