package com.avajjava.sample.model

class EsProduct {

    EsProduct(String id, String nameKor, String nameEng, String categoryId, String categoryName, String description){
        this.id = id
        this.nameKor = nameKor
        this.nameEng = nameEng
        this.category.id = categoryId
        this.category.name = categoryName
        this.description = description
    }

    String id
    String nameKor
    String nameEng
    CategoryItem category = new CategoryItem()
    String description
    Date createdDate
}
