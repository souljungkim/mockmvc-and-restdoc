package com.avajjava.sample.model

class NodeData {

    NodeData(String id, int group, String label){
        this.id = id
        this.group = group
        this.label = label
    }

    String id           //from node id
    String parentId     //to node id
    int group           //depth
    String label        //id name

    int type
    double weight
    double relevance
    String categoryId
    String categoryName
    Map<String, String> customData = [:]

}
