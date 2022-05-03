/**/// DO NOT MODIFY THIS FILE MANUALLY
/**/// This class was automatically generated by "GeneratePatternClasses.java",
/**/// modify the "unique_patterns.json" and re-generate instead.

package org.schabi.newpipe.extractor.timeago.patterns;

import org.schabi.newpipe.extractor.timeago.PatternsHolder;

public class sw extends PatternsHolder {
    private static final String WORD_SEPARATOR = " ";
    private static final String[]
            SECONDS  /**/ = {"sekunde"},
            MINUTES  /**/ = {"dakika"},
            HOURS    /**/ = {"saa"},
            DAYS     /**/ = {"siku"},
            WEEKS    /**/ = {"wiki"},
            MONTHS   /**/ = {"Mwezi", "miezi"},
            YEARS    /**/ = {"Miaka", "Mwaka"};

    private static final sw INSTANCE = new sw();

    public static sw getInstance() {
        return INSTANCE;
    }

    private sw() {
        super(WORD_SEPARATOR, SECONDS, MINUTES, HOURS, DAYS, WEEKS, MONTHS, YEARS);
    }
}