package com.simonmawole.app.androidnanodegree.data;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.DefaultValue;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;
import net.simonvt.schematic.annotation.Unique;

/**
 * Created by simon on 01-Aug-16.
 */
public interface MovieColumns {

    @DataType(DataType.Type.INTEGER) @PrimaryKey @AutoIncrement String _ID = "_id";

    @DataType(DataType.Type.TEXT) @NotNull String MOVIE_ID = "movie_id";

    @DataType(DataType.Type.TEXT) @NotNull String ORIGINAL_LANGUAGE = "original_language";

    @DataType(DataType.Type.TEXT) @NotNull String ORIGINAL_TITLE = "original_title";

    @DataType(DataType.Type.TEXT) @NotNull String OVERVIEW = "overview";

    @DataType(DataType.Type.TEXT) @NotNull String POSTER_PATH = "poster_path";

    @DataType(DataType.Type.TEXT) @NotNull String RELEASE_DATE = "release_date";

    @DataType(DataType.Type.TEXT) @NotNull String VOTE_AVERAGE = "vote_average";

    @DataType(DataType.Type.INTEGER) @NotNull @DefaultValue("0") String FAVORITE = "favorite";

    @DataType(DataType.Type.INTEGER) @NotNull @DefaultValue("0") String POPULAR = "popular";

    @DataType(DataType.Type.INTEGER) @NotNull @DefaultValue("0") String TOP_RATED = "top_rated";
}
