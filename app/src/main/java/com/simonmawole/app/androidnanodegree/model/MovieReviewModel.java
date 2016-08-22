package com.simonmawole.app.androidnanodegree.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by simon on 31-Jul-16.
 */
public class MovieReviewModel {

    public int id;
    public int page;
    public List<ReviewResult> results = new ArrayList<ReviewResult>();
    public int total_pages;
    public int total_results;

    public class ReviewResult {

        public String id;
        public String author;
        public String content;
        public String url;

    }
}