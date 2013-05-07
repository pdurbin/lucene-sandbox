package org.apache.lucene.facet.example.simple;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.WhitespaceAnalyzer;

import org.apache.lucene.facet.example.ExampleUtils;
import org.apache.lucene.facet.taxonomy.CategoryPath;

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
 * Some definitions for the Simple Sample.
 *
 * @lucene.experimental
 */
public class SimpleUtils {

    /**
     * Documents text field.
     */
    public static final String TEXT = "text";
    /**
     * Documents title field.
     */
    public static final String TITLE = "title";
    /**
     * sample documents titles (for the title field).
     */
    public static String[] docTitles = {
        "doc1",
        "doc2",
        "doc3",
        "doc4",
        "doc5",
        "doc6",
        "doc7",
        "doc8",
        "doc9",
        "doc10",
        "doc11",
        "doc12",
        "doc13",
        "doc14",
        "doc15",
        "doc16",
        "doc17",
        "doc18",
        "doc19",
        "doc20",
        "doc21",
        "doc22",
        "doc23",
        "doc24",
        "doc25",
        "doc26",
        "doc27",};
    /**
     * sample documents text (for the text field).
     */
    public static String[] docTexts = {
        "a blue sparrow on a spruce",
        "a blue sparrow on a oak",
        "a blue sparrow on a maple",
        "a blue finch on a spruce",
        "a blue finch on a oak",
        "a blue finch on a maple",
        "a blue wren on a spruce",
        "a blue wren on a oak",
        "a blue wren on a maple",
        "a green sparrow on a spruce",
        "a green sparrow on a oak",
        "a green sparrow on a maple",
        "a green finch on a spruce",
        "a green finch on a oak",
        "a green finch on a maple",
        "a green wren on a spruce",
        "a green wren on a oak",
        "a green wren on a maple",
        "a yellow sparrow on a spruce",
        "a yellow sparrow on a oak",
        "a yellow sparrow on a maple",
        "a yellow finch on a spruce",
        "a yellow finch on a oak",
        "a yellow finch on a maple",
        "a yellow wren on a spruce",
        "a yellow wren on a oak",
        "a yellow wren on a maple",
    
    };
    /**
     * Categories: categories[D][N] == category-path no. N for document no. D.
     */
    public static CategoryPath[][] categories = {
        {
            //            new CategoryPath("author", "Simpson, Homer"),
            //            new CategoryPath("author", "Wiggum, Clancy"),
            new CategoryPath("productionDate", "2013-03-09"),
            //            new CategoryPath("keyword", "butter"),
            //            new CategoryPath("keyword", "pastries"),
            //            new CategoryPath("keyword", "food"),
            //            new CategoryPath("topicClassification", "Cooking"),
            new CategoryPath("dvName", "Homer Simpson Dataverse"),},
        {
            //            new CategoryPath("author", "Simpson, Homer"),
            new CategoryPath("productionDate", "2013-03-09"),
            //            new CategoryPath("keyword", "sports"),
            //            new CategoryPath("keyword", "football"),
            //            new CategoryPath("topicClassification", "TV"),
            new CategoryPath("dvName", "Homer Simpson Dataverse"),},
        {
            //            new CategoryPath("author", "Flanders, Ned"),
            new CategoryPath("productionDate", "2013-03-10"),
            //            new CategoryPath("keyword", "scripture"),
            //            new CategoryPath("keyword", "prayer"),
            //            new CategoryPath("topicClassification", "Religion"),
            new CategoryPath("dvName", "Ned Flanders Dataverse"),},
        {
            //            new CategoryPath("author", "Wiggum, Clancy"),
            new CategoryPath("productionDate", "2013-03-09"),
            //            new CategoryPath("keyword", "sports"),
            //            new CategoryPath("keyword", "softball"),
            //            new CategoryPath("topicClassification", "Recreation"),
            new CategoryPath("dvName", "Clancy Wiggum Dataverse"),},
        {
            //            new CategoryPath("author", "Wiggum, Clancy"),
            new CategoryPath("productionDate", "2013-03-10"),
            //            new CategoryPath("keyword", "pastries"),
            //            new CategoryPath("keyword", "donuts"),
            //            new CategoryPath("keyword", "food"),
            //            new CategoryPath("topicClassification", "Cooking"),
            new CategoryPath("dvName", "Clancy Wiggum Dataverse"),},};
    /**
     * Analyzer used in the simple sample.
     */
    public static final Analyzer analyzer = new WhitespaceAnalyzer(ExampleUtils.EXAMPLE_VER);

    /**
     * Utility method: List of category paths out of an array of them...
     *
     * @param categoryPaths input array of category paths.
     */
    public static List<CategoryPath> categoryPathArrayToList(CategoryPath... categoryPaths) {
        ArrayList<CategoryPath> res = new ArrayList<CategoryPath>();
        for (CategoryPath categoryPath : categoryPaths) {
            res.add(categoryPath);
        }
        return res;
    }
}
