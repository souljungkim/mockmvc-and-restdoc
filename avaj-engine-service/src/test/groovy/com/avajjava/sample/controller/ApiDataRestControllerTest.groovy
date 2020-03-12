package com.avajjava.sample.controller

import com.avajjava.sample.AvajEngineTestHelper
import org.junit.Test
import org.springframework.restdocs.payload.JsonFieldType

import static org.springframework.restdocs.payload.PayloadDocumentation.*
import static org.springframework.restdocs.request.RequestDocumentation.*

class ApiDataRestControllerTest extends AvajEngineTestHelper{

    @Test
    void searchSomething(){
        def result = requestGet('/data/search-something',
                documentAuto(
                        requestParameters(
                                parameterWithName("query").description("검색문"),
                                parameterWithName("from").description("검색된 문서의 표출시작기준 번호").optional(),
                                parameterWithName("size").description("검색된 문서의 표출갯수").optional(),
                                parameterWithName("scoreOrder").description("검색된 문서의 연관도 기준 정렬").optional(),
                                parameterWithName("createdDateOrder").description("검색된 문서의 등록일 기준 정").optional()
                        ),
                        responseFields(
                                fieldWithPath("totalCount").type(JsonFieldType.NUMBER).description("총 문서 수"),
                                fieldWithPath("count").type(JsonFieldType.NUMBER).description("검색된 문서 수"),
                                fieldWithPath("from").type(JsonFieldType.NUMBER).description("검색된 문서의 시작번호"),
                                fieldWithPath("size").type(JsonFieldType.NUMBER).description("검색된 문서의 양"),
                                fieldWithPath("resultList[]").type(JsonFieldType.ARRAY).description("검색된 상품 리스트").optional(),
                                fieldWithPath("resultList[].id").type(JsonFieldType.STRING).description("상품 ID").optional(),
                                fieldWithPath("resultList[].nameEng").type(JsonFieldType.STRING).description("상품 이름(영문)").optional(),
                                fieldWithPath("resultList[].nameKor").type(JsonFieldType.STRING).description("상품 이름").optional(),
                                fieldWithPath("resultList[].description").type(JsonFieldType.STRING).description("상품 설명").optional(),
                                fieldWithPath("resultList[].createdDate").type(JsonFieldType.STRING).description("상품 생성 날자").optional(),
                                fieldWithPath("resultList[].category").type(JsonFieldType.OBJECT).description("카테고리").optional(),
                                fieldWithPath("resultList[].category.id").type(JsonFieldType.STRING).description("카테고리 ID").optional(),
                                fieldWithPath("resultList[].category.name").type(JsonFieldType.STRING).description("카테고리 명").optional(),
                                fieldWithPath("resultList[].category.description").type(JsonFieldType.STRING).description("카테고리 설명").optional(),
                                fieldWithPath("resultList[].category.count").type(JsonFieldType.NUMBER).description("카테고리 수").optional(),
                        ),
                ),
                parameterValues([
                        query          : '손전등',
                        from           : 0,
                        size           : 10,
                        scoreOrder     : "desc",
                        createdDateOrder: "desc"
                ])
        )
        println result
    }

    @Test
    void getDataListForSomething(){
        requestGet('/data/get-data-list-for-something',
                documentAuto(
                        requestParameters(
                                parameterWithName("type").description("데이터맵 종류"),
                                parameterWithName("id").description("기준데이터 ID (type의 값에 따라서 해당 데이터의 ID값)").optional(),
                                parameterWithName("size").description("관련 Node의 연결선 수").optional(),
                        ),
                        responseFields(
                                fieldWithPath("[]").type(JsonFieldType.ARRAY).description("데이터 리스트").optional(),
                                fieldWithPath("[].id").type(JsonFieldType.STRING).description("데이터 ID").optional(),
                                fieldWithPath("[].parentId").type(JsonFieldType.STRING).description("부모데이터 ID").optional(),
                                fieldWithPath("[].group").type(JsonFieldType.NUMBER).description("Node 단계 (연관관계 깊이에 따라 단계별 수치 증가)").optional(),
                                fieldWithPath("[].label").type(JsonFieldType.STRING).description("Node 제목").optional(),
                                fieldWithPath("[].type").type(JsonFieldType.NUMBER).description("Node 종류").optional(),
                                fieldWithPath("[].weight").type(JsonFieldType.NUMBER).description("연관관계 분석을 위한 가중치").optional(),
                                fieldWithPath("[].relevance").type(JsonFieldType.NUMBER).description("연관관계율").optional(),
                                fieldWithPath("[].categoryId").type(JsonFieldType.STRING).description("카테고리ID").optional(),
                                fieldWithPath("[].categoryName").type(JsonFieldType.STRING).description("카테고리명").optional(),
                                fieldWithPath("[].customData").type(JsonFieldType.OBJECT).description("User Defined Properties").optional(),
                                fieldWithPath("[].customData.KEY_1").type(JsonFieldType.STRING).description("User Defined Property - Name").optional(),
                                fieldWithPath("[].customData.VAL_1").type(JsonFieldType.STRING).description("User Defined Property - Value").optional(),
                                fieldWithPath("[].customData.KEY_2").type(JsonFieldType.STRING).description("User Defined Property - Name").optional(),
                                fieldWithPath("[].customData.VAL_2").type(JsonFieldType.STRING).description("User Defined Property - Value").optional(),
                                fieldWithPath("[].customData.KEY_3").type(JsonFieldType.STRING).description("User Defined Property - Name").optional(),
                                fieldWithPath("[].customData.VAL_3").type(JsonFieldType.STRING).description("User Defined Property - Value").optional(),
                                fieldWithPath("[].customData.KEY_4").type(JsonFieldType.STRING).description("User Defined Property - Name").optional(),
                                fieldWithPath("[].customData.VAL_4").type(JsonFieldType.STRING).description("User Defined Property - Value").optional(),
                                fieldWithPath("[].customData.KEY_5").type(JsonFieldType.STRING).description("User Defined Property - Name").optional(),
                                fieldWithPath("[].customData.VAL_5").type(JsonFieldType.STRING).description("User Defined Property - Value").optional(),
                                fieldWithPath("[].customData.KEY_6").type(JsonFieldType.STRING).description("User Defined Property - Name").optional(),
                                fieldWithPath("[].customData.VAL_6").type(JsonFieldType.STRING).description("User Defined Property - Value").optional(),
                                fieldWithPath("[].customData.KEY_7").type(JsonFieldType.STRING).description("User Defined Property - Name").optional(),
                                fieldWithPath("[].customData.VAL_7").type(JsonFieldType.STRING).description("User Defined Property - Value").optional(),
                        ),
                ),
                parameterValues([
                        type:2,
                        id:0,
                        size:10
                ]),
        )
    }



}
