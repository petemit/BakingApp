package com.petemit.example.android.bakingapp.db;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Created by Peter on 7/22/2017.
 */

@ContentProvider(
        authority = BakingProvider.AUTHORITY,
        database = BakingDatabase.class)
public final  class BakingProvider {

        public static final String AUTHORITY = "android.example.petemit.com.db.provider";


        @TableEndpoint(table = BakingDatabase.JSONSource)
        public static class JSONSource {

            @ContentUri(
                    path = "json",
                    type = "vnd.android.cursor.dir/json")
            public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/json");
        }
}

