package com.simonmawole.app.androidnanodegree.data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;
import net.simonvt.schematic.annotation.References;
import net.simonvt.schematic.annotation.Unique;

/**
 * Created by simon on 01-Aug-16.
 */
public interface MovieTrailerColumns {

    @DataType(DataType.Type.INTEGER) @PrimaryKey @AutoIncrement String _ID = "_id";

    @DataType(DataType.Type.TEXT) @NotNull String TRAILER_ID = "trailer_id";

    @DataType(DataType.Type.TEXT) @NotNull String KEY = "key";

    @DataType(DataType.Type.TEXT) @NotNull String NAME = "name";

    @DataType(DataType.Type.TEXT) @NotNull String SITE = "site";

    @DataType(DataType.Type.TEXT) @NotNull String TYPE = "type";

    @DataType(DataType.Type.INTEGER) @NotNull
    @References(table = MovieDatabase.MOVIE,
            column = MovieColumns.MOVIE_ID) String MOVIE_ID = "movie_id";
}
