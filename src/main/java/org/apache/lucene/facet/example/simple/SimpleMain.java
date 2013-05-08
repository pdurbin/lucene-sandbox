package org.apache.lucene.facet.example.simple;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.lucene.document.Document;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import org.apache.lucene.facet.example.ExampleResult;
import org.apache.lucene.facet.example.ExampleUtils;
import org.apache.lucene.facet.index.params.DefaultFacetIndexingParams;
import org.apache.lucene.facet.search.FacetsCollector;
import org.apache.lucene.facet.search.params.CountFacetRequest;
import org.apache.lucene.facet.search.params.FacetRequest;
import org.apache.lucene.facet.search.params.FacetSearchParams;
import org.apache.lucene.facet.search.results.FacetResult;
import org.apache.lucene.facet.search.results.FacetResultNode;
import org.apache.lucene.facet.taxonomy.CategoryPath;
import org.apache.lucene.facet.taxonomy.TaxonomyReader;
import org.apache.lucene.facet.taxonomy.directory.DirectoryTaxonomyReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MultiCollector;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopScoreDocCollector;

/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
/**
 * Driver for the simple sample.
 *
 * @lucene.experimental
 */
public class SimpleMain {

    /**
     * Driver for the simple sample.
     *
     * @throws Exception on error (no detailed exception handling here for
     * sample simplicity
     */
    public static void main(String[] args) throws Exception {

        List<DvnQuery> queries = new ArrayList<DvnQuery>();

        DvnQuery dvnQuery1 = new DvnQuery();
        dvnQuery1.setQueryString("finch");
        queries.add(dvnQuery1);

        DvnQuery dvnQuery2 = new DvnQuery();
        dvnQuery2.setQueryString("finch");
        List<String> collections2 = new ArrayList<String>();
        collections2.add("yellow");
        collections2.add("spruce");
        dvnQuery2.setCollections(collections2);
        queries.add(dvnQuery2);

        for (DvnQuery query : queries) {
            search(query);
            ExampleUtils.log("---");
        }

    }

    public static void search(DvnQuery dvnQuery) throws Exception {
        String searchString = dvnQuery.getQueryString();
        ExampleUtils.log("searching for " + searchString);
        Directory indexDir = new RAMDirectory();
        Directory taxoDir = new RAMDirectory();
        SimpleIndexer.index(indexDir, taxoDir);
        TaxonomyReader taxo = new DirectoryTaxonomyReader(taxoDir);
        IndexReader indexReader = IndexReader.open(indexDir, true);
        IndexSearcher searcher = new IndexSearcher(indexReader);
        TopScoreDocCollector topDocsCollector = TopScoreDocCollector.create(10, true);
        List<CountFacetRequest> countFacetRequestsList = new ArrayList<CountFacetRequest>();
        countFacetRequestsList.add(new CountFacetRequest(new CategoryPath("author"), 10));
        countFacetRequestsList.add(new CountFacetRequest(new CategoryPath("productionDate"), 10));
        countFacetRequestsList.add(new CountFacetRequest(new CategoryPath("keyword"), 10));
        countFacetRequestsList.add(new CountFacetRequest(new CategoryPath("topicClassification"), 10));
        countFacetRequestsList.add(new CountFacetRequest(new CategoryPath("dvName"), 10));
        FacetSearchParams facetSearchParams = new FacetSearchParams();
        for (FacetRequest frq : countFacetRequestsList) {
            facetSearchParams.addFacetRequest(frq);
        }
        FacetsCollector facetsCollector = new FacetsCollector(facetSearchParams, indexReader, taxo);

        Query limitingQuery = null;
        if (dvnQuery.getCollections() != null) {
            for (String col : dvnQuery.getCollections()) {
                ExampleUtils.log("collection found: " + col + "... search will be limited");
                Query q = new TermQuery(new Term(SimpleUtils.TEXT, col));
                limitingQuery = q;
                searcher.search(q, MultiCollector.wrap(topDocsCollector, facetsCollector));
                ScoreDoc[] hits = topDocsCollector.topDocs().scoreDocs;
                for (int i = 0; i < hits.length; i++) {
                    ScoreDoc scoreDoc = hits[i];
                    Document d = searcher.doc(scoreDoc.doc);
                    ExampleUtils.log("- fav " + i + ": " + d.get("text"));
                }
                topDocsCollector = TopScoreDocCollector.create(10, true);
            }
        } else {
            ExampleUtils.log("no collection found... search will be global");
        }

        Query finalQuery;
        Query submittedQuery = new TermQuery(new Term(SimpleUtils.TEXT, searchString));
        if (dvnQuery.getCollections() != null) {
            BooleanQuery queryAcrossAllCollections = new BooleanQuery();
            for (String collection : dvnQuery.getCollections()) {
                BooleanQuery submittedAndInCollection = new BooleanQuery();
                Query collectionQuery = new TermQuery(new Term(SimpleUtils.TEXT, collection));
                submittedAndInCollection.add(submittedQuery, BooleanClause.Occur.MUST);
                submittedAndInCollection.add(collectionQuery, BooleanClause.Occur.MUST);
                queryAcrossAllCollections.add(submittedAndInCollection, BooleanClause.Occur.SHOULD);
            }
            finalQuery = queryAcrossAllCollections;
        } else {
            finalQuery = submittedQuery;
        }
        searcher.search(finalQuery, MultiCollector.wrap(topDocsCollector, facetsCollector));

        ScoreDoc[] hits = topDocsCollector.topDocs().scoreDocs;
        ExampleUtils.log(hits.length + " documents found with query " + "\"" + finalQuery + "\"");
        for (int i = 0; i < hits.length; i++) {
            ScoreDoc scoreDoc = hits[i];
            Document d = searcher.doc(scoreDoc.doc);
            ExampleUtils.log("- text " + i + ": " + d.get("text"));
        }

        taxo.close();
        indexReader.close();

    }
}
