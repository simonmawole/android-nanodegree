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
public interface MovieReviewColumns {

    @DataType(DataType.Type.INTEGER) @PrimaryKey @AutoIncrement String _ID = "_id";

    @DataType(DataType.Type.TEXT) @NotNull @Unique String REVIEW_ID = "review_id";

    @DataType(DataType.Type.TEXT) @NotNull String AUTHOR = "author";

    @DataType(DataType.Type.TEXT) @NotNull String CONTENT = "content";

    @DataType(DataType.Type.TEXT) @NotNull String URL = "";

    @DataType(DataType.Type.INTEGER) @NotNull @References(table = MovieDatabase.MOVIE,
            column = MovieColumns.MOVIE_ID) String MOVIE_ID = "movie_id";

}
