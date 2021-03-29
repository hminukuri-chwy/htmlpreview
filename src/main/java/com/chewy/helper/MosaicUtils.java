package com.chewy.helper;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.List;

/**
 * Utils for processing Mosaic URLs.
 */
public class MosaicUtils {
    public static final String JPG = "jpg";

    /**
     * Generate a URL for use with Mosaic Image Server. This method will gracefully handle assetIds
     * which contain a version number.
     *
     * @param assetId    path to asset on the image server (ex: /is/catalog/49066)
     * @param operations list of operations (ex: AC, SS100)
     * @param format     image output format (ex: jpg, png, gif)
     * @return mosaic image server URL
     */
    public static String generateURL(String assetId, List<String> operations, String format) {
        Preconditions.checkNotNull(assetId);
        Preconditions.checkNotNull(format);


        String[] assetIdSplit = assetId.split(",");
        if (assetIdSplit.length > 1) {
            String version = assetIdSplit[1];
            // Must make a copy of operations in case the one passed in is immutable
            operations = operations != null ? new ArrayList<>(operations) : new ArrayList<>();
            operations.add("V" + version);
        }

        // todo prepend the image CDN hostname when configured
        // StringBuilder sb = new StringBuilder(Config.get("cdn.baseURL.images", ""));
        StringBuilder sb = new StringBuilder();
        sb.append(assetIdSplit[0]);

        if (operations != null && !operations.isEmpty()) {
            sb.append("._");
            sb.append(Joiner.on("_").join(operations));
            sb.append("_");
        }
        sb.append(".").append(format);

        return sb.toString();
    }
}
