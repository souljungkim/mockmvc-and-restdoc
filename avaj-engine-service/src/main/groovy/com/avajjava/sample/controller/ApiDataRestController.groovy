package com.avajjava.sample.controller

import com.avajjava.sample.model.SearchOption
import com.avajjava.sample.model.SearchResult
import com.avajjava.sample.service.DataService
import com.sun.corba.se.impl.orbutil.graph.NodeData
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/data")
class ApiDataRestController {

    private static final Logger logger = LoggerFactory.getLogger(this)

    @Autowired
    DataService dataService



    @GetMapping("search-something")
    SearchResult searchSomething(SearchOption searchOption) {
        if (searchOption.from == null)
            searchOption.from = 0
        if (searchOption.size == null)
            searchOption.size = 10
        SearchResult searchResult = dataService.fakeSearchSomething(searchOption)
        return searchResult
    }

    @GetMapping("get-data-list-for-something")
    List<NodeData> getDataListForNetworkGraph(Integer type, String id, Integer size){
        return dataService.fakeFindSomething(type, id, size)
    }

}
