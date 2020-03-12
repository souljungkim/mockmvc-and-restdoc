package com.avajjava.sample.service;

import com.avajjava.sample.model.SearchOption;
import com.avajjava.sample.model.SearchResult;
import com.avajjava.sample.model.NodeData;

import java.util.List;

public interface DataService {

    String greeting();

    SearchResult fakeSearchSomething(SearchOption searchOption);

    List<NodeData> fakeFindSomething(Integer type, String id, Integer size);

}
