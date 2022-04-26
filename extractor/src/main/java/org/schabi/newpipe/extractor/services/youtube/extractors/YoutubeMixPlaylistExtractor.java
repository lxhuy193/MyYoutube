package org.schabi.newpipe.extractor.services.youtube.extractors;

import static org.schabi.newpipe.extractor.services.youtube.YoutubeParsingHelper.YOUTUBEI_V1_URL;
import static org.schabi.newpipe.extractor.services.youtube.YoutubeParsingHelper.addClientInfoHeaders;
import static org.schabi.newpipe.extractor.services.youtube.YoutubeParsingHelper.extractCookieValue;
import static org.schabi.newpipe.extractor.services.youtube.YoutubeParsingHelper.getKey;
import static org.schabi.newpipe.extractor.services.youtube.YoutubeParsingHelper.getValidJsonResponseBody;
import static org.schabi.newpipe.extractor.services.youtube.YoutubeParsingHelper.prepareDesktopJsonBuilder;
import static org.schabi.newpipe.extractor.utils.Utils.UTF_8;
import static org.schabi.newpipe.extractor.utils.Utils.getQueryValue;
import static org.schabi.newpipe.extractor.utils.Utils.isNullOrEmpty;
import static org.schabi.newpipe.extractor.utils.Utils.stringToURL;

import com.grack.nanojson.JsonArray;
import com.grack.nanojson.JsonBuilder;
import com.grack.nanojson.JsonObject;
import com.grack.nanojson.JsonWriter;

import org.schabi.newpipe.extractor.ListExtractor;
import org.schabi.newpipe.extractor.Page;
import org.schabi.newpipe.extractor.StreamingService;
import org.schabi.newpipe.extractor.downloader.Downloader;
import org.schabi.newpipe.extractor.downloader.Response;
import org.schabi.newpipe.extractor.exceptions.ExtractionException;
import org.schabi.newpipe.extractor.exceptions.ParsingException;
import org.schabi.newpipe.extractor.linkhandler.ListLinkHandler;
import org.schabi.newpipe.extractor.localization.Localization;
import org.schabi.newpipe.extractor.localization.TimeAgoParser;
import org.schabi.newpipe.extractor.playlist.PlaylistExtractor;
import org.schabi.newpipe.extractor.services.youtube.YoutubeParsingHelper;
import org.schabi.newpipe.extractor.stream.StreamInfoItem;
import org.schabi.newpipe.extractor.stream.StreamInfoItemsCollector;
import org.schabi.newpipe.extractor.utils.JsonUtils;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A {@link YoutubePlaylistExtractor} for a mix (auto-generated playlist).
 * It handles URLs in the format of
 * {@code youtube.com/watch?v=videoId&list=playlistId}
 */
public class YoutubeMixPlaylistExtractor extends PlaylistExtractor {

    /**
     * YouTube identifies mixes based on this cookie. With this information it can generate
     * continuations without duplicates.
     */
    public static final String COOKIE_NAME = "VISITOR_INFO1_LIVE";

    private JsonObject initialData;
    private JsonObject playlistData;
    private String cookieValue;

    public YoutubeMixPlaylistExtractor(final StreamingService service,
                                       final ListLinkHandler linkHandler) {
        super(service, linkHandler);
    }

    @Override
    public void onFetchPage(@Nonnull final Downloader downloader)
            throws IOException, ExtractionException {
        final Localization localization = getExtractorLocalization();
        final URL url = stringToURL(getUrl());
        final String mixPlaylistId = getId();
        final String videoId = getQueryValue(url, "v");
        final String playlistIndexString = getQueryValue(url, "index");

        final JsonBuilder<JsonObject> jsonBody = prepareDesktopJsonBuilder(localization,
                getExtractorContentCountry()).value("playlistId", mixPlaylistId);
        if (videoId != null) {
            jsonBody.value("videoId", videoId);
        }
        if (playlistIndexString != null) {
            jsonBody.value("playlistIndex", Integer.parseInt(playlistIndexString));
        }

        final byte[] body = JsonWriter.string(jsonBody.done()).getBytes(UTF_8);

        final Map<String, List<String>> headers = new HashMap<>();
        addClientInfoHeaders(headers);

        final Response response = getDownloader().post(YOUTUBEI_V1_URL + "next?key=" + getKey(),
                headers, body, localization);

        initialData = JsonUtils.toJsonObject(getValidJsonResponseBody(response));
        playlistData = initialData.getObject("contents").getObject("twoColumnWatchNextResults")
                .getObject("playlist").getObject("playlist");
        if (isNullOrEmpty(playlistData)) throw new ExtractionException(
                "Could not get playlistData");
        cookieValue = extractCookieValue(COOKIE_NAME, response);
    }

    @Nonnull
    @Override
    public String getName() throws ParsingException {
        final String name = YoutubeParsingHelper.getTextAtKey(playlistData, "title");
        if (isNullOrEmpty(name)) {
            throw new ParsingException("Could not get playlist name");
        }
        return name;
    }

    @Override
    public String getThumbnailUrl() throws ParsingException {
        try {
            return getThumbnailUrlFromPlaylistId(playlistData.getString("playlistId"));
        } catch (final Exception e) {
            try {
                // Fallback to thumbnail of current video. Always the case for channel mix
                return getThumbnailUrlFromVideoId(initialData.getObject("currentVideoEndpoint")
                        .getObject("watchEndpoint").getString("videoId"));
            } catch (final Exception ignored) {
            }
            throw new ParsingException("Could not get playlist thumbnail", e);
        }
    }

    @Override
    public String getBannerUrl() {
        return "";
    }

    @Override
    public String getUploaderUrl() {
        // YouTube mixes are auto-generated by YouTube
        return "";
    }

    @Override
    public String getUploaderName() {
        // YouTube mixes are auto-generated by YouTube
        return "YouTube";
    }

    @Override
    public String getUploaderAvatarUrl() {
        // YouTube mixes are auto-generated by YouTube
        return "";
    }

    @Override
    public boolean isUploaderVerified() throws ParsingException {
        return false;
    }

    @Override
    public long getStreamCount() {
        // Auto-generated playlists always start with 25 videos and are endless
        return ListExtractor.ITEM_COUNT_INFINITE;
    }

    @Nonnull
    @Override
    public InfoItemsPage<StreamInfoItem> getInitialPage() throws IOException,
            ExtractionException {
        final StreamInfoItemsCollector collector = new StreamInfoItemsCollector(getServiceId());
        collectStreamsFrom(collector, playlistData.getArray("contents"));

        final Map<String, String> cookies = new HashMap<>();
        cookies.put(COOKIE_NAME, cookieValue);

        return new InfoItemsPage<>(collector, getNextPageFrom(playlistData, cookies));
    }

    private Page getNextPageFrom(final JsonObject playlistJson,
                                 final Map<String, String> cookies) throws IOException,
            ExtractionException {
        final JsonObject lastStream = ((JsonObject) playlistJson.getArray("contents")
                .get(playlistJson.getArray("contents").size() - 1));
        if (lastStream == null || lastStream.getObject("playlistPanelVideoRenderer") == null) {
            throw new ExtractionException("Could not extract next page url");
        }

        final JsonObject watchEndpoint = lastStream.getObject("playlistPanelVideoRenderer")
                .getObject("navigationEndpoint").getObject("watchEndpoint");
        final String playlistId = watchEndpoint.getString("playlistId");
        final String videoId = watchEndpoint.getString("videoId");
        final int index = watchEndpoint.getInt("index");
        final String params = watchEndpoint.getString("params");
        final byte[] body = JsonWriter.string(prepareDesktopJsonBuilder(getExtractorLocalization(),
                getExtractorContentCountry())
                .value("videoId", videoId)
                .value("playlistId", playlistId)
                .value("playlistIndex", index)
                .value("params", params)
                .done())
                .getBytes(UTF_8);

        return new Page(YOUTUBEI_V1_URL + "next?key=" + getKey(), null, null, cookies, body);
    }

    @Override
    public InfoItemsPage<StreamInfoItem> getPage(final Page page) throws IOException,
            ExtractionException {
        if (page == null || isNullOrEmpty(page.getUrl())) {
            throw new IllegalArgumentException("Page doesn't contain an URL");
        }
        if (!page.getCookies().containsKey(COOKIE_NAME)) {
            throw new IllegalArgumentException("Cookie '" + COOKIE_NAME + "' is missing");
        }

        final StreamInfoItemsCollector collector = new StreamInfoItemsCollector(getServiceId());
        final Map<String, List<String>> headers = new HashMap<>();
        addClientInfoHeaders(headers);

        final Response response = getDownloader().post(page.getUrl(), headers, page.getBody(),
                getExtractorLocalization());
        final JsonObject ajaxJson = JsonUtils.toJsonObject(getValidJsonResponseBody(response));
        final JsonObject playlistJson = ajaxJson.getObject("contents")
                .getObject("twoColumnWatchNextResults").getObject("playlist").getObject("playlist");
        final JsonArray allStreams = playlistJson.getArray("contents");
        // Sublist because YouTube returns up to 24 previous streams in the mix
        // +1 because the stream of "currentIndex" was already extracted in previous request
        final List<Object> newStreams =
                allStreams.subList(playlistJson.getInt("currentIndex") + 1, allStreams.size());

        collectStreamsFrom(collector, newStreams);
        return new InfoItemsPage<>(collector, getNextPageFrom(playlistJson, page.getCookies()));
    }

    private void collectStreamsFrom(@Nonnull final StreamInfoItemsCollector collector,
                                    @Nullable final List<Object> streams) {

        if (streams == null) {
            return;
        }

        final TimeAgoParser timeAgoParser = getTimeAgoParser();

        for (final Object stream : streams) {
            if (stream instanceof JsonObject) {
                final JsonObject streamInfo = ((JsonObject) stream)
                        .getObject("playlistPanelVideoRenderer");
                if (streamInfo != null) {
                    collector.commit(new YoutubeStreamInfoItemExtractor(streamInfo,
                            timeAgoParser));
                }
            }
        }
    }

    private String getThumbnailUrlFromPlaylistId(final String playlistId) throws ParsingException {
        final String videoId;
        if (playlistId.startsWith("RDMM")) {
            videoId = playlistId.substring(4);
        } else if (playlistId.startsWith("RDCMUC")) {
            throw new ParsingException("This playlist is a channel mix");
        } else {
            videoId = playlistId.substring(2);
        }
        if (videoId.isEmpty()) {
            throw new ParsingException("videoId is empty");
        }
        return getThumbnailUrlFromVideoId(videoId);
    }

    private String getThumbnailUrlFromVideoId(final String videoId) {
        return "https://i.ytimg.com/vi/" + videoId + "/hqdefault.jpg";
    }

    @Nonnull
    @Override
    public String getSubChannelName() {
        return "";
    }

    @Nonnull
    @Override
    public String getSubChannelUrl() {
        return "";
    }

    @Nonnull
    @Override
    public String getSubChannelAvatarUrl() {
        return "";
    }
}
