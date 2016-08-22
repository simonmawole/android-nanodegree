package com.simonmawole.app.androidnanodegree.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by simon on 31-Jul-16.
 */
public class MovieTrailerModel {

    public int id;
    public List<TrailerResult> results = new ArrayList<TrailerResult>();

    public class TrailerResult {

        public String id;
        public String iso_639_1;
        public String iso_3166_1;
        public String key;
        public String name;
        public String site;
        public int size;
        public String type;

    }

}
