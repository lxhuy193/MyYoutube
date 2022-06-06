package org.schabi.newpipe.extractor.services.youtube.extractors;

import static org.schabi.newpipe.extractor.services.youtube.YoutubeParsingHelper.getJsonPostResponse;
import static org.schabi.newpipe.extractor.services.youtube.YoutubeParsingHelper.getTextAtKey;
import static org.schabi.newpipe.extractor.services.youtube.YoutubeParsingHelper.prepareDesktopJsonBuilder;
import static org.schabi.newpipe.extractor.utils.Utils.UTF_8;
import static org.schabi.newpipe.extractor.utils.Utils.isNullOrEmpty;

import android.util.Log;

import com.grack.nanojson.JsonArray;
import com.grack.nanojson.JsonObject;
import com.grack.nanojson.JsonWriter;

import org.schabi.newpipe.extractor.Page;
import org.schabi.newpipe.extractor.StreamingService;
import org.schabi.newpipe.extractor.downloader.Downloader;
import org.schabi.newpipe.extractor.exceptions.ExtractionException;
import org.schabi.newpipe.extractor.exceptions.ParsingException;
import org.schabi.newpipe.extractor.kiosk.KioskExtractor;
import org.schabi.newpipe.extractor.linkhandler.ListLinkHandler;
import org.schabi.newpipe.extractor.localization.TimeAgoParser;
import org.schabi.newpipe.extractor.stream.StreamInfoItem;
import org.schabi.newpipe.extractor.stream.StreamInfoItemsCollector;

import java.io.IOException;

import javax.annotation.Nonnull;

public class YoutubeTrendingExtractor extends KioskExtractor<StreamInfoItem> {
    private JsonObject initialData;

    public YoutubeTrendingExtractor(final StreamingService service,
                                    final ListLinkHandler linkHandler,
                                    final String kioskId) {
        super(service, linkHandler, kioskId);
    }

    @Override
    public void onFetchPage(@Nonnull final Downloader downloader) throws IOException, ExtractionException {
        // @formatter:off
        final byte[] body = JsonWriter.string(prepareDesktopJsonBuilder(getExtractorLocalization(),
                getExtractorContentCountry())
                .value("browseId", "FEtrending")
                .value("params","4gINGgt5dG1hX2NoYXJ0cw%3D%3D")
                .done())
                .getBytes(UTF_8);
        // @formatter:on

        initialData = getJsonPostResponse("browse", body, getExtractorLocalization());
        Log.d("cach22", "initiaData: " + initialData);
    }

    @Override
    public InfoItemsPage<StreamInfoItem> getPage(final Page page) {
        return InfoItemsPage.emptyPage();
    }

    @Nonnull
    @Override
    public String getName() throws ParsingException {
        final JsonObject header = initialData.getObject("header");
        String name = null;
        if (header.has("feedTabbedHeaderRenderer")) {
            name = getTextAtKey(header.getObject("feedTabbedHeaderRenderer"), "title");
        } else if (header.has("c4TabbedHeaderRenderer")) {
            name = getTextAtKey(header.getObject("c4TabbedHeaderRenderer"), "title");
        }

        if (isNullOrEmpty(name)) {
            throw new ParsingException("Could not get Trending name");
        }
        return name;
    }

    @Nonnull
    @Override
    public InfoItemsPage<StreamInfoItem> getInitialPage() {
        StreamInfoItemsCollector collector = new StreamInfoItemsCollector(getServiceId());
        final TimeAgoParser timeAgoParser = getTimeAgoParser();
        JsonArray itemSectionRenderers = initialData.getObject("contents")
                .getObject("twoColumnBrowseResultsRenderer").getArray("tabs").getObject(1)
                .getObject("tabRenderer").getObject("content").getObject("sectionListRenderer")
                .getArray("contents");

        for (final Object itemSectionRenderer : itemSectionRenderers) {
            JsonObject expandedShelfContentsRenderer = ((JsonObject) itemSectionRenderer)
                    .getObject("itemSectionRenderer").getArray("contents").getObject(0)
                    .getObject("shelfRenderer").getObject("content")
                    .getObject("expandedShelfContentsRenderer");
            for (final Object ul : expandedShelfContentsRenderer.getArray("items")) {
                final JsonObject videoInfo = ((JsonObject) ul).getObject("videoRenderer");
                collector.commit(new YoutubeStreamInfoItemExtractor(videoInfo, timeAgoParser));
            }
        }

        System.out.println("colector::: " + collector.getItems());
        return new InfoItemsPage<>(collector, null);
    }
}
