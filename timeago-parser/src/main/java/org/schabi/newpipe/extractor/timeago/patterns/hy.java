/**/// DO NOT MODIFY THIS FILE MANUALLY
/**/// This class was automatically generated by "GeneratePatternClasses.java",
/**/// modify the "unique_patterns.json" and re-generate instead.

package org.schabi.newpipe.extractor.timeago.patterns;

import org.schabi.newpipe.extractor.timeago.PatternsHolder;

public class hy extends PatternsHolder {
    private static final String WORD_SEPARATOR = " ";
    private static final String[]
            SECONDS  /**/ = {"վայրկյան"},
            MINUTES  /**/ = {"րոպե"},
            HOURS    /**/ = {"ժամ"},
            DAYS     /**/ = {"օր"},
            WEEKS    /**/ = {"շաբաթ"},
            MONTHS   /**/ = {"ամիս"},
            YEARS    /**/ = {"տարի"};

    private static final hy INSTANCE = new hy();

    public static hy getInstance() {
        return INSTANCE;
    }

    private hy() {
        super(WORD_SEPARATOR, SECONDS, MINUTES, HOURS, DAYS, WEEKS, MONTHS, YEARS);
    }
}