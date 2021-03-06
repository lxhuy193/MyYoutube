package org.schabi.newpipe.extractor.utils;

import org.schabi.newpipe.extractor.exceptions.ParsingException;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class Utils {

    public static final String HTTP = "http://";
    public static final String HTTPS = "https://";
    public static final String UTF_8 = "UTF-8";
    public static final String EMPTY_STRING = "";
    private static final Pattern M_PATTERN = Pattern.compile("(https?)?:\\/\\/m\\.");
    private static final Pattern WWW_PATTERN = Pattern.compile("(https?)?:\\/\\/www\\.");

    private Utils() {
        // no instance
    }

    /**
     * Remove all non-digit characters from a string.<p>
     * Examples:<p>
     * <ul><li>1 234 567 views -&gt; 1234567</li>
     * <li>$31,133.124 -&gt; 31133124</li></ul>
     *
     * @param toRemove string to remove non-digit chars
     * @return a string that contains only digits
     */
    public static String removeNonDigitCharacters(final String toRemove) {
        return toRemove.replaceAll("\\D+", "");
    }

    /**
     * <p>Convert a mixed number word to a long.</p>
     * <p>Examples:</p>
     * <ul>
     *     <li>123 -&gt; 123</li>
     *     <li>1.23K -&gt; 1230</li>
     *     <li>1.23M -&gt; 1230000</li>
     * </ul>
     *
     * @param numberWord string to be converted to a long
     * @return a long
     * @throws NumberFormatException
     * @throws ParsingException
     */
    public static long mixedNumberWordToLong(final String numberWord) throws NumberFormatException,
            ParsingException {
        String multiplier = "";
        try {
            multiplier = Parser.matchGroup("[\\d]+([\\.,][\\d]+)?([KMBkmb])+", numberWord, 2);
        } catch (ParsingException ignored) {
        }
        double count = Double.parseDouble(Parser.matchGroup1("([\\d]+([\\.,][\\d]+)?)", numberWord)
                .replace(",", "."));
        switch (multiplier.toUpperCase()) {
            case "K":
                return (long) (count * 1e3);
            case "M":
                return (long) (count * 1e6);
            case "B":
                return (long) (count * 1e9);
            default:
                return (long) (count);
        }
    }

    /**
     * Check if the url matches the pattern.
     *
     * @param pattern the pattern that will be used to check the url
     * @param url     the url to be tested
     */
    public static void checkUrl(final String pattern, final String url) throws ParsingException {
        if (isNullOrEmpty(url)) {
            throw new IllegalArgumentException("Url can't be null or empty");
        }

        if (!Parser.isMatch(pattern, url.toLowerCase())) {
            throw new ParsingException("Url don't match the pattern");
        }
    }

    public static void printErrors(List<Throwable> errors) {
        for (Throwable e : errors) {
            e.printStackTrace();
            System.err.println("----------------");
        }
    }

    public static String replaceHttpWithHttps(final String url) {
        if (url == null) return null;

        if (!url.isEmpty() && url.startsWith(HTTP)) {
            return HTTPS + url.substring(HTTP.length());
        }
        return url;
    }

    /**
     * Get the value of a URL-query by name.
     * If a url-query is give multiple times, only the value of the first query is returned
     *
     * @param url           the url to be used
     * @param parameterName the pattern that will be used to check the url
     * @return a string that contains the value of the query parameter or null if nothing was found
     */
    public static String getQueryValue(final URL url, final String parameterName) {
        String urlQuery = url.getQuery();

        if (urlQuery != null) {
            for (String param : urlQuery.split("&")) {
                String[] params = param.split("=", 2);

                String query;
                try {
                    query = URLDecoder.decode(params[0], UTF_8);
                } catch (final UnsupportedEncodingException e) {
                    System.err.println(
                            "Cannot decode string with UTF-8. using the string without decoding");
                    e.printStackTrace();
                    query = params[0];
                }

                if (query.equals(parameterName)) {
                    try {
                        return URLDecoder.decode(params[1], UTF_8);
                    } catch (final UnsupportedEncodingException e) {
                        System.err.println(
                                "Cannot decode string with UTF-8. using the string without decoding");
                        e.printStackTrace();
                        return params[1];
                    }
                }
            }
        }

        return null;
    }

    /**
     * converts a string to a URL-Object.
     * defaults to HTTP if no protocol is given
     *
     * @param url the string to be converted to a URL-Object
     * @return a URL-Object containing the url
     */
    public static URL stringToURL(final String url) throws MalformedURLException {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            // if no protocol is given try prepending "https://"
            if (e.getMessage().equals("no protocol: " + url)) {
                return new URL(HTTPS + url);
            }

            throw e;
        }
    }

    public static boolean isHTTP(final URL url) {
        // make sure its http or https
        String protocol = url.getProtocol();
        if (!protocol.equals("http") && !protocol.equals("https")) {
            return false;
        }

        boolean usesDefaultPort = url.getPort() == url.getDefaultPort();
        boolean setsNoPort = url.getPort() == -1;

        return setsNoPort || usesDefaultPort;
    }

    public static String removeMAndWWWFromUrl(final String url) {
        if (M_PATTERN.matcher(url).find()) {
            return url.replace("m.", "");
        }
        if (WWW_PATTERN.matcher(url).find()) {
            return url.replace("www.", "");
        }
        return url;
    }

    public static String removeUTF8BOM(String s) {
        if (s.startsWith("\uFEFF")) {
            s = s.substring(1);
        }
        if (s.endsWith("\uFEFF")) {
            s = s.substring(0, s.length() - 1);
        }
        return s;
    }

    public static String getBaseUrl(final String url) throws ParsingException {
        try {
            final URL uri = stringToURL(url);
            return uri.getProtocol() + "://" + uri.getAuthority();
        } catch (final MalformedURLException e) {
            final String message = e.getMessage();
            if (message.startsWith("unknown protocol: ")) {
                // return just the protocol (e.g. vnd.youtube)
                return message.substring("unknown protocol: ".length());
            }

            throw new ParsingException("Malformed url: " + url, e);
        }
    }

    /**
     * If the provided url is a Google search redirect, then the actual url is extracted from the
     * {@code url=} query value and returned, otherwise the original url is returned.
     *
     * @param url the url which can possibly be a Google search redirect
     * @return an url with no Google search redirects
     */
    public static String followGoogleRedirectIfNeeded(final String url) {
        // if the url is a redirect from a Google search, extract the actual url
        try {
            final URL decoded = Utils.stringToURL(url);
            if (decoded.getHost().contains("google") && decoded.getPath().equals("/url")) {
                return URLDecoder.decode(Parser.matchGroup1("&url=([^&]+)(?:&|$)", url),
                        UTF_8);
            }
        } catch (final Exception ignored) {
        }

        // url is not a google search redirect
        return url;
    }

    public static boolean isNullOrEmpty(final String str) {
        return str == null || str.isEmpty();
    }

    // can be used for JsonArrays
    public static boolean isNullOrEmpty(final Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    // can be used for JsonObjects
    public static boolean isNullOrEmpty(final Map map) {
        return map == null || map.isEmpty();
    }

    public static boolean isWhitespace(final int c) {
        return c == ' ' || c == '\t' || c == '\n' || c == '\f' || c == '\r';
    }

    public static boolean isBlank(final String string) {
        if (isNullOrEmpty(string)) {
            return true;
        }

        final int length = string.length();
        for (int i = 0; i < length; i++) {
            if (!isWhitespace(string.codePointAt(i))) {
                return false;
            }
        }

        return true;
    }

    public static String join(final CharSequence delimiter,
                              final Iterable<? extends CharSequence> elements) {
        final StringBuilder stringBuilder = new StringBuilder();
        final Iterator<? extends CharSequence> iterator = elements.iterator();
        while (iterator.hasNext()) {
            stringBuilder.append(iterator.next());
            if (iterator.hasNext()) {
                stringBuilder.append(delimiter);
            }
        }
        return stringBuilder.toString();
    }

    public static String join(final String delimiter, final String mapJoin,
                              final Map<? extends CharSequence, ? extends CharSequence> elements) {
        final List<String> list = new LinkedList<>();
        for (final Map.Entry<? extends CharSequence, ? extends CharSequence> entry : elements
                .entrySet()) {
            list.add(entry.getKey() + mapJoin + entry.getValue());
        }
        return join(delimiter, list);
    }

    /**
     * Concatenate all non-null, non-empty and strings which are not equal to <code>"null"</code>.
     */
    public static String nonEmptyAndNullJoin(final CharSequence delimiter,
                                             final String[] elements) {
        final List<String> list = new ArrayList<>(Arrays.asList(elements));
        list.removeIf(s -> isNullOrEmpty(s) || s.equals("null"));
        return join(delimiter, list);
    }
}
