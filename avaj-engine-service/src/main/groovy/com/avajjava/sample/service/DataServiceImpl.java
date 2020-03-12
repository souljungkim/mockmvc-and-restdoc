package com.avajjava.sample.service;

import com.avajjava.sample.model.EsProduct;
import com.avajjava.sample.model.NodeData;
import com.avajjava.sample.model.SearchOption;
import com.avajjava.sample.model.SearchResult;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class DataServiceImpl implements DataService{

    @Override
    public String greeting() {
        return "Hello";
    }

    @Override
    public SearchResult fakeSearchSomething(SearchOption searchOption) {
        SearchResult searchResult = new SearchResult();
        searchResult.setTotalCount(1004L);
        searchResult.setCount(10L);
        searchResult.setFrom(120);
        searchResult.setSize(10);

        searchResult.setResultList(Arrays.asList(
                new EsProduct("1", "MaSilKkeo", "SomeDrink", "C1", "water", "Kkul Kkeok Kkul Kkeok"),
                new EsProduct("2", "Meo ggl kkeo", "SomeFood", "C1", "food", "Jjeop Jjeop")
        ));
        return searchResult;
    }

    @Override
    public List<NodeData> fakeFindSomething(Integer type, String id, Integer size) {
        return Arrays.asList(
            new NodeData("1", 1, "hello"),
            new NodeData("2", 1, "nanisitemasu"),
            new NodeData("3", 2, "mohaseyo")
        );
    }

}
