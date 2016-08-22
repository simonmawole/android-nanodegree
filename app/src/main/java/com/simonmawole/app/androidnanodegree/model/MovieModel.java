package com.simonmawole.app.androidnanodegree.model;

import java.util.ArrayList;
import java.util.List;

public class MovieModel {

    public int page;
    public List<MovieResult> results = new ArrayList<MovieResult>();
    public int total_results;
    public int total_pages;

    public class MovieResult {

        public String poster_path;
        public boolean adult;
        public String overview;
        public String release_date;
        public List<Integer> genre_ids = new ArrayList<Integer>();
        public int id;
        public String original_title;
        public String original_language;
        public String title;
        public String backdrop_path;
        public float popularity;
        public int vote_count;
        public boolean video;
        public float vote_average;

    }

}

