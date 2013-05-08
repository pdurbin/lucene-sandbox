package org.apache.lucene.facet.example.simple;

import java.util.List;

class DvnQuery {

    private String queryString;
//    private String collection;
    private List<String> collections;

    public List<String> getCollections() {
        return collections;
    }

    public void setCollections(List<String> collections) {
        this.collections = collections;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public DvnQuery() {
    }
}
