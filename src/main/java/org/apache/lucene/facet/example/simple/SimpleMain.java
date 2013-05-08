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

        List<String> searches = new ArrayList<String>();
        searches.add("blue");
        searches.add("finch");

        String commonString = "oak";
        for (String searchString : searches) {
            search(searchString, commonString);
            ExampleUtils.log("----");
//            List<FacetResult> facetResults = new SimpleMain().search(searchString).getFacetResults();
//        ExampleUtils.log(facetResults);
        }
    }

//    public List<FacetResult> search(String searchString) throws Exception {
    public static void search(String searchString, String commonString) throws Exception {
//        ExampleUtils.log("searching for " + searchString + " and " + commonString);
        // create Directories for the search index and for the taxonomy index
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







        Query q = new TermQuery(new Term(SimpleUtils.TEXT, searchString));
        BooleanQuery booleanQuery = new BooleanQuery();
        booleanQuery.add(q, BooleanClause.Occur.MUST);
        Query searchQuery = new TermQuery(new Term(SimpleUtils.TEXT, commonString));
        booleanQuery.add(searchQuery, BooleanClause.Occur.MUST);
        q = booleanQuery;
        searcher.search(q, MultiCollector.wrap(topDocsCollector, facetsCollector));

        ScoreDoc[] hits = topDocsCollector.topDocs().scoreDocs;
        ExampleUtils.log(hits.length + " documents found with query " + "\"" + q + "\"");
        for (int i = 0; i < hits.length; i++) {
            ScoreDoc scoreDoc = hits[i];
            Document d = searcher.doc(scoreDoc.doc);
//            ExampleUtils.log("- title " + i + ": " + d.get("title"));
            ExampleUtils.log("- text " + i + ": " + d.get("text"));
        }
        List<FacetResult> res = facetsCollector.getFacetResults();
//        ExampleUtils.log(res);

        for (int i = 0; i < res.size(); i++) {
            FacetResult facetResult = res.get(i);
//            ExampleUtils.log("- category " + i + ": " + facetResult.getFacetResultNode().getLabel());
            for (FacetResultNode n : facetResult.getFacetResultNode().getSubResults()) {
                CategoryPath label = n.getLabel();
                String last = n.getLabel().lastComponent().toString();
                Double hits2 = n.getValue();
//                ExampleUtils.log("  - expect " + hits2.intValue() + " hit(s) from a faceted search for \"" + label + "\"");
            }
        }


        taxo.close();
        indexReader.close();

    }
}
