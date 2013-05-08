package org.apache.lucene.facet.example.simple;

class DvnQuery {

    private String queryString;
    private String collection;

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
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
