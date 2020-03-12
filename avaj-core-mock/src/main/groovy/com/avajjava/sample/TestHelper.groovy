package com.avajjava.sample


import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import jaemisseo.man.VariableMan
import org.junit.AfterClass
import org.junit.BeforeClass
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler
import org.springframework.restdocs.snippet.Snippet
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.ResultHandler
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap

import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters

//MockMvc
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
//RestDocs
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*

/****************************************************************************************************
 *
 * Created by sujkim on 2017-09-07.
 *  - Edited by sujkim on 2018-03-22
 *  - Edited by sujkim on 2019-10-30
 *
 ****************************************************************************************************/
@TestPropertySource(properties=[
        "avaj.app.enabled=false",
        "avaj.test.enabled=true",
        "app.scheduling.enable=false"
])
@AutoConfigureRestDocs(uriScheme="http", uriHost="0.0.0.0", uriPort=0)
abstract class TestHelper extends AbstractTest{

    @Autowired
    MockMvc mvc

//    @Rule
//    public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();

//    @Autowired
//    WebApplicationContext context;

//    @Autowired
//    RestDocumentationResultHandler restDocument;

//    static SecurityContext testSecurityContext
//    static Authentication testAuthentication
//    static UserDetails testUserDetails
//    static UserSession testUserSession

    TestHelper() {
    }



    /**************************************************
     *
     * Print
     *
     **************************************************/
    @BeforeClass
    static void beforeClass(){
        println "\n\n===================================================================================================="
        println "=========================================================================== Start MockMvc Test"
        println "===================================================================================================="
    }

    @AfterClass
    static void afterClass(){
        lastReport.each{ println it }
        println "===================================================================================================="
        println "=========================================================================== Finish MockMvc Test"
        println "====================================================================================================\n\n"
    }

    static void printStart(){
        printStart('')
    }

    static void printStart(String title){
        println "\n================================================== ${title}"
    }

    static void printEnd(){
        printEnd(null)
    }

    static void printEnd(def resultObject){
        if (resultObject)
            sizeChecker(resultObject)
        println "==================================================\n"
    }



    /**************************************************
     *
     * Document
     *
     **************************************************/
    static RestDocumentationResultHandler documentAuto(){
        return documentAuto(
                requestParameters(
                        parameterWithName("___nu_ga_iron_girl_using_ha_get_sir").description("인자없는 곳의 도큐멘트를 만들기위한 Dummy").optional()
                ),
        )
    }

    static RestDocumentationResultHandler documentAuto(Snippet... snippets){
        return document(
                '{class-name}/{method-name}',
                preprocessRequest(prettyPrint(), removeHeaders('Content-Length', 'Host')),
                preprocessResponse(prettyPrint(), removeHeaders('Content-Length')),
                snippets
        )
    }



    /**************************************************
     *
     * Values
     *
     **************************************************/
    class RequestValuesHandler{
        RequestValuesHandler(){
        }
        RequestValuesHandler(RequestValues... values){
            for (int i=0; i<values.length; i++){
                RequestValues valueObject = values[i]
                if (valueObject instanceof PathValues)
                    this.pathValues = valueObject
                else if (valueObject instanceof ParameterValues)
                    this.paramValues = valueObject
                else if (valueObject instanceof MultipartValues)
                    this.multipartValues = valueObject
                else if (valueObject instanceof BodyValues)
                    this.bodyValues = valueObject
                else if (valueObject instanceof HeaderValues)
                    this.headerValues = valueObject
                else if (valueObject instanceof ResponseHandler)
                    this.responseHandler = valueObject
                else if (valueObject instanceof OptionValues)
                    this.optionValues = valueObject
            }
        }
        PathValues pathValues
        ParameterValues paramValues
        MultipartValues multipartValues
        BodyValues bodyValues
        HeaderValues headerValues
        ResponseHandler responseHandler
        OptionValues optionValues
        Object[] getPathArray(){ return (this.pathValues?.values ?: []).toArray() }
        MultiValueMap<String, String> getParams(){ return generateParams(this.paramValues?.values ?: [:]) }
        String getBodyString(){ return toJsonString(this.bodyValues?.values ?: [:]) }
        List<MockMultipartFile> getMultipartFileList(){ return this.multipartValues?.values ?: [] }
        MultiValueMap<String, String> getHeaders(){ return generateParams(this.headerValues?.values ?: [:]) }
        Closure getClosureForResponse(){ return responseHandler?.closureForResponse }
    }
    class RequestValues {}
    class PathValues extends RequestValues { List<Object> values }
    class ParameterValues extends RequestValues { Object values }
    class MultipartValues extends RequestValues { List<Object> values }
    class BodyValues  extends RequestValues { Object values }
    class HeaderValues extends RequestValues { Object values }
    class ResponseHandler extends RequestValues { Closure closureForResponse }
    class OptionValues extends RequestValues { boolean modeReturnByteArrays }

    ParameterValues parameterValues(Map<String, Object> valueMap){
        ParameterValues o = new ParameterValues()
        o.values = valueMap
        return o
    }

    BodyValues bodyValues(Map<String, Object> valueMap){
        BodyValues o = new BodyValues()
        o.values = valueMap
        return o
    }

    HeaderValues headerValues(Map<String, Object> valueMap){
        HeaderValues o = new HeaderValues()
        o.values = valueMap
        return o
    }

    PathValues pathValues(Object... values){
        PathValues o = new PathValues()
        o.values = Arrays.asList(values)
        return o
    }

    MultipartValues multipartValues(Object... values){
        MultipartValues o = new MultipartValues()
        o.values = Arrays.asList(values)
        return o
    }

    OptionValues optionValues(boolean modeReturnByteArrays){
        OptionValues o = new OptionValues()
        o.modeReturnByteArrays = true
        return o
    }

    ResponseHandler responseHandler(Closure closureForResponse){
        ResponseHandler o = new ResponseHandler()
        o.closureForResponse = closureForResponse
        return o
    }




    /**************************************************
     *
     * GET
     *
     **************************************************/
    Object requestGet(String url){
        return requestGet(url, null, new RequestValuesHandler())
    }

    Object requestGet(String url, List<Object> pathParameterList){
        return requestGet(url, null, new RequestValuesHandler(pathValues(pathParameterList)))
    }

    Object requestGet(String url, Map<String, Object> parameterMap){
        return requestGet(url, null, new RequestValuesHandler(parameterValues(parameterMap)))
    }

    Object requestGet(String url, List<Object> pathParameterList, Map<String, Object> parameterMap){
        return requestGet(url, null, new RequestValuesHandler(pathValues(pathParameterList), parameterValues(parameterMap)))
    }

    Object requestGet(String url, RequestValues... values){
        return requestGet(url, null, new RequestValuesHandler(values))
    }

    Object requestGet(String url, ResultHandler resultHandlerForDocument){
        return requestGet(url, resultHandlerForDocument, new RequestValuesHandler())
    }

    Object requestGet(String url, ResultHandler resultHandlerForDocument, List<Object> pathParameterList){
        return requestGet(url, resultHandlerForDocument, new RequestValuesHandler(pathValues(pathParameterList)))
    }

    Object requestGet(String url, ResultHandler resultHandlerForDocument, Map<String, Object> parameterMap){
        return requestGet(url, resultHandlerForDocument, new RequestValuesHandler(parameterValues(parameterMap)))
    }

    Object requestGet(String url, ResultHandler resultHandlerForDocument, List<Object> pathParameterList, Map<String, Object> parameterMap){
        return requestGet(url, resultHandlerForDocument, new RequestValuesHandler(pathValues(pathParameterList), parameterValues(parameterMap)))
    }

    Object requestGet(String url, ResultHandler resultHandlerForDocument, RequestValues... values){
        return requestGet(url, resultHandlerForDocument, new RequestValuesHandler(values))
    }

    Object requestGet(String url, ResultHandler resultHandlerForDocument, RequestValuesHandler valuesHandler){
        printStart("[GET] ${url}")
        //- Request
        ResultActions resultActions = mvc
                .perform(
                        RestDocumentationRequestBuilders
                                .get(url, valuesHandler.getPathArray())
                                .params(valuesHandler.getParams())
                                .headers(new HttpHeaders(valuesHandler.getHeaders()))
                                .contentType(MediaType.APPLICATION_JSON)
//                                .contentType(MediaType.APPLICATION_OCTET_STREAM)
//                                .accept(MediaType.APPLICATION_OCTET_STREAM)
//                                .accept(MediaType.APPLICATION_JSON)
//                .with(authentication(testAuthentication))
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM));
        //- Document or Something
        if (resultHandlerForDocument){
            resultActions.andDo(resultHandlerForDocument)
        }
        //- Response
        MvcResult mvcResult = resultActions.andReturn()
        if (valuesHandler.getClosureForResponse())
            valuesHandler.getClosureForResponse()(mvcResult.getResponse())
        //- Return response data
        def resultObject
        OptionValues optionValues = valuesHandler.getOptionValues()
        if (optionValues && optionValues.modeReturnByteArrays){
            resultObject = mvcResult.getResponse().getContentAsByteArray()
        }else{
            String jsonString = mvcResult.getResponse().getContentAsString()
            resultObject = toObject(jsonString)
            printEnd(resultObject)
        }
        return resultObject
    }





    /**************************************************
     *
     * POST
     *
     **************************************************/
    Object requestPost(String url){
        return requestPost(url, null, new RequestValuesHandler())
    }

    Object requestPost(String url, List<Object> pathParameterList){
        return requestPost(url, null, new RequestValuesHandler(pathValues(pathParameterList)))
    }

    Object requestPost(String url, Map<String, Object> bodyMap){
        return requestPost(url, null, new RequestValuesHandler(bodyValues(bodyMap)))
    }

    Object requestPost(String url, List<Object> pathParameterList, Map<String, Object> bodyMap){
        return requestPost(url, null, new RequestValuesHandler(pathValues(pathParameterList), bodyValues(bodyMap)))
    }

    Object requestPost(String url, RequestValues... values){
        return requestPost(url, null, new RequestValuesHandler(values))
    }

    Object requestPost(String url, ResultHandler resultHandlerForDocument){
        return requestPost(url, resultHandlerForDocument, new RequestValuesHandler())
    }

    Object requestPost(String url, ResultHandler resultHandlerForDocument, List<Object> pathParameterList){
        return requestPost(url, resultHandlerForDocument, new RequestValuesHandler(pathValues(pathParameterList)))
    }

    Object requestPost(String url, ResultHandler resultHandlerForDocument, Map<String, Object> bodyMap){
        return requestPost(url, resultHandlerForDocument, new RequestValuesHandler(bodyValues(bodyMap)))
    }

    Object requestPost(String url, ResultHandler resultHandlerForDocument, List<Object> pathParameterList, Map<String, Object> bodyMap){
        return requestPost(url, resultHandlerForDocument, new RequestValuesHandler(pathValues(pathParameterList), bodyValues(bodyMap)))
    }

    Object requestPost(String url, ResultHandler resultHandlerForDocument, RequestValues... values){
        return requestPost(url, resultHandlerForDocument, new RequestValuesHandler(values))
    }

    Object requestPost(String url, ResultHandler resultHandlerForDocument, RequestValuesHandler valuesHandler){
        printStart("[POST] ${url}")
        //Request
        ResultActions resultActions = mvc
                .perform(
                    post(url, valuesHandler.getPathArray())
                    .params(valuesHandler.getParams())
                    .content(valuesHandler.getBodyString())
                    .headers(new HttpHeaders(valuesHandler.getHeaders()))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
//                    .with(securityContext(secc))
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
        //- Document or Something
        if (resultHandlerForDocument){
            resultActions.andDo(resultHandlerForDocument)
        }
        //Response
        MvcResult mvcResult = resultActions.andReturn()
        String jsonString = mvcResult.getResponse().getContentAsString()
        def resultObject = toObject(jsonString)
        printEnd(resultObject)
        return resultObject
    }





    /**************************************************
     *
     * MultiPartFile
     *
     **************************************************/
    /**
     *  Ex)
     *          MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain", "some xml".getBytes());
     *          MockMultipartFile secondFile = new MockMultipartFile("data", "other-file-name.data", "text/plain", "some other type".getBytes());
     *          MockMultipartFile jsonFile = new MockMultipartFile("json", "", "application/json", "{\"json\": \"someValue\"}".getBytes());
     */
    Object requestPostWithMultipart(String url, List<MockMultipartFile> multipartFileList){
        return requestPostWithMultipart(url, new RequestValuesHandler(new MultipartValues(values:multipartFileList)))
    }

    Object requestPostWithMultipart(String url, List<Object> pathParameterList, List<MockMultipartFile> multipartFileList){
        return requestPostWithMultipart(url, new RequestValuesHandler(pathValues(pathParameterList), new MultipartValues(values:multipartFileList)))
    }

    Object requestPostWithMultipart(String url, RequestValues... values){
        return requestPostWithMultipart(url, null, new RequestValuesHandler(values))
    }

    Object requestPostWithMultipart(String url, ResultHandler resultHandlerForDocument, List<MockMultipartFile> multipartFileList){
        return requestPostWithMultipart(url, resultHandlerForDocument, new RequestValuesHandler(new MultipartValues(values:multipartFileList)))
    }

    Object requestPostWithMultipart(String url, ResultHandler resultHandlerForDocument, List<MockMultipartFile> multipartFileList, List<Object> pathParameterList){
        return requestPostWithMultipart(url, resultHandlerForDocument, new RequestValuesHandler(pathValues(pathParameterList), new MultipartValues(values:multipartFileList)))
    }

    Object requestPostWithMultipart(String url, ResultHandler resultHandlerForDocument, RequestValues... values){
        return requestPostWithMultipart(url, resultHandlerForDocument, new RequestValuesHandler(values))
    }

    Object requestPostWithMultipart(String url, ResultHandler resultHandlerForDocument, RequestValuesHandler valuesHandler){
        printStart("[MultiPartFile] ${url}")
        //Request
        ResultActions resultActions = mvc
                .perform(
                    fileUploadWithFiles(url, valuesHandler.getMultipartFileList(), valuesHandler.getPathArray())
                            .params(valuesHandler.getParams())
                            .headers(new HttpHeaders(valuesHandler.getParams()))
                            .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                            .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
        //- Document or Something
        if (resultHandlerForDocument){
            resultActions.andDo(resultHandlerForDocument)
        }
        //Response
        MvcResult mvcResult = resultActions.andReturn()
        String jsonString = mvcResult.getResponse().getContentAsString()
        def resultObject = toObject(jsonString)
        printEnd(resultObject)
        return resultObject
    }

    static  MockMultipartHttpServletRequestBuilder fileUploadWithFiles(String url, List<MockMultipartFile> multipartFileList, Object[] pathArray){
        MockMultipartHttpServletRequestBuilder mockBuilder = RestDocumentationRequestBuilders.fileUpload(url, pathArray)
        multipartFileList.each{
            mockBuilder.file(it)
        }
        return mockBuilder
    }





    /**************************************************
     *
     * PUT
     *
     **************************************************/
    Object requestPut(String url){
        return requestPut(url, null, new RequestValuesHandler())
    }

    Object requestPut(String url, List<Object> pathParameterList){
        return requestPut(url, null, new RequestValuesHandler(pathValues(pathParameterList)))
    }

    Object requestPut(String url, Map<String, Object> bodyMap){
        return requestPut(url, null, new RequestValuesHandler(bodyValues(bodyMap)))
    }

    Object requestPut(String url, List<Object> pathParameterList, Map<String, Object> bodyMap){
        return requestPut(url, null, new RequestValuesHandler(pathValues(pathParameterList), bodyValues(bodyMap)))
    }

    Object requestPut(String url, RequestValues... values){
        return requestPut(url, null, new RequestValuesHandler(values))
    }

    Object requestPut(String url, ResultHandler resultHandlerForDocument){
        return requestPut(url, resultHandlerForDocument, new RequestValuesHandler())
    }

    Object requestPut(String url, ResultHandler resultHandlerForDocument, List<Object> pathParameterList){
        return requestPut(url, resultHandlerForDocument, new RequestValuesHandler(pathValues(pathParameterList)))
    }

    Object requestPut(String url, ResultHandler resultHandlerForDocument, Map<String, Object> bodyMap){
        return requestPut(url, resultHandlerForDocument, new RequestValuesHandler(bodyValues(bodyMap)))
    }

    Object requestPut(String url, ResultHandler resultHandlerForDocument, List<Object> pathParameterList, Map<String, Object> bodyMap){
        return requestPut(url, resultHandlerForDocument, new RequestValuesHandler(pathValues(pathParameterList), bodyValues(bodyMap)))
    }

    Object requestPut(String url, ResultHandler resultHandlerForDocument, RequestValues... values){
        return requestPut(url, resultHandlerForDocument, new RequestValuesHandler(values))
    }

    Object requestPut(String url, ResultHandler resultHandlerForDocument, RequestValuesHandler valuesHandler){
        printStart("[PUT] ${url}")
        //- Request
        ResultActions resultActions = mvc
                .perform(
                    put(url, valuesHandler.getPathArray())
                    .params(valuesHandler.getParams())
                    .content(valuesHandler.getBodyString())
                    .headers(new HttpHeaders(valuesHandler.getHeaders()))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                )
                .andDo( MockMvcResultHandlers.print() )
                .andExpect( status().isOk() )
        //- Document or Something
        if (resultHandlerForDocument){
            resultActions.andDo(resultHandlerForDocument)
        }
        //- Response
        MvcResult mvcResult = resultActions.andReturn()
        String jsonString = mvcResult.getResponse().getContentAsString()
        printEnd()
        return toObject(jsonString)
    }





    /**************************************************
     *
     * DELETE
     *
     **************************************************/
    Object requestDelete(String url){
        return requestDelete(url, null, new RequestValuesHandler())
    }

    Object requestDelete(String url, List<Object> pathParameterList){
        return requestDelete(url, null, new RequestValuesHandler(pathValues(pathParameterList)))
    }

    Object requestDelete(String url, Map<String, Object> parameterMap){
        return requestDelete(url, null, new RequestValuesHandler(parameterValues(parameterMap)))
    }

    Object requestDelete(String url, List<Object> pathParameterList, Map<String, Object> parameterMap){
        return requestDelete(url, null, new RequestValuesHandler(pathValues(pathParameterList), parameterValues(parameterMap)))
    }

    Object requestDelete(String url, RequestValues... values){
        return requestDelete(url, null, new RequestValuesHandler(values))
    }

    Object requestDelete(String url, ResultHandler resultHandlerForDocument){
        return requestDelete(url, resultHandlerForDocument, new RequestValuesHandler())
    }

    Object requestDelete(String url, ResultHandler resultHandlerForDocument, List<Object> pathParameterList){
        return requestDelete(url, resultHandlerForDocument, new RequestValuesHandler(pathValues(pathParameterList)))
    }

    Object requestDelete(String url, ResultHandler resultHandlerForDocument, Map<String, Object> parameterMap){
        return requestDelete(url, resultHandlerForDocument, new RequestValuesHandler(parameterValues(parameterMap)))
    }

    Object requestDelete(String url, ResultHandler resultHandlerForDocument, List<Object> pathParameterList, Map<String, Object> parameterMap){
        return requestDelete(url, resultHandlerForDocument, new RequestValuesHandler(pathValues(pathParameterList), parameterValues(parameterMap)))
    }

    Object requestDelete(String url, ResultHandler resultHandlerForDocument, RequestValues... values){
        return requestDelete(url, resultHandlerForDocument, new RequestValuesHandler(values))
    }

    Object requestDelete(String url, ResultHandler resultHandlerForDocument, RequestValuesHandler valuesHandler){
        printStart("[DELETE] ${url}")
        //- Reqeust
        ResultActions resultActions = mvc
                .perform(
                    RestDocumentationRequestBuilders.
                    delete(url, valuesHandler.getPathArray())
                    .params(valuesHandler.getParams())
                    .content(valuesHandler.getBodyString())
                    .headers(new HttpHeaders(valuesHandler.getHeaders()))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
        //- Document or Something
        if (resultHandlerForDocument){
            resultActions.andDo(resultHandlerForDocument)
        }
        //- Response
        MvcResult mvcResult = resultActions.andReturn()
        String jsonString = mvcResult.getResponse().getContentAsString()
        def resultObject = toObject(jsonString)
        printEnd(resultObject)
        return resultObject
    }










    /**************************************************
     *
     * with UserSession
     *
     **************************************************/
//    void withUser(){
//        UserSession defaultTestUser = new UserSession(
//                userId: "deokbae",
//                userName: "김덕배",
//                userAdminAuth: "Y",
//                userAuthGroup: "DA",
//                userAuthGroupCode: "1",
//                userObjectId: (long) 5953,
//                userGroupObjectId: (long) 170,
//        )
//        withUser(defaultTestUser)
//    }
//
//    void withAdmin(){
//        withUser('Administrator')
//    }
//
//    void withUserById(String id){
//        List userDatas = requestGet("/privacy/test/getUsers", [
//                id: id
//        ])
//        UserSession userSession = Option.put(userDatas[0], new UserSession())
//        withUser(userSession)
//    }
//
//    void withUserByGroup(String groupName){
//        List userDatas = requestGet("/privacy/test/getUsers", [
//                group: groupName
//        ])
//        UserSession userSession = Option.put(userDatas[0], new UserSession())
//        withUser(userSession)
//    }
//
//    void withUser(String metaUserName){
//        List userDatas = requestGet("/privacy/test/getUsers", [
//                name: metaUserName
//        ])
//        UserSession userSession = Option.put(userDatas[0], new UserSession())
//        withUser(userSession)
//    }
//
//    void withUser(UserSession userSession){
//        //필요 객체
//        SecurityContext testSecurityContext
//        Authentication testAuthentication
//        UserDetails testUserDetails
//        //UserDetails
//        List<MenuItem> menuList = Collections.emptyList();
//        testUserDetails = new UserDetails(userSession, menuList);
//        //Authentication
//        testAuthentication = new TestingAuthenticationToken(testUserDetails, 'Avaj$', "ROLE_ADMIN", "ROLE_DA");
//        //SecurityContext
//        testSecurityContext = new SecurityContextImpl();
//        testSecurityContext.setAuthentication(testAuthentication);
//        SecurityContextHolder.setContext(testSecurityContext);
//    }



    /**************************************************
     *
     * UTIL
     *
     **************************************************/
    /*************************
     * Map<String, String>
     *  => Map<String, List<String>>
     *  => LinkedMultiValueMap<String, String>
     *************************/
    static MultiValueMap<String, String> generateParams(Map parameters) {
        Map multiValueMap = new LinkedMultiValueMap<String, String>()
        // Map<String, String>
        // => Map<String, List<String>>
//        Map<String, List<String>> listValueMap = [:]
//        parameters.each {
//            listValueMap[it.key] = (it.value instanceof List) ? it.value : [it.value]
//        }
        if (parameters){
            Map<String, List<String>> listValueMap = generateConcatKeyParams(parameters)
            // Map<String, List<String>>
            // => LinkedMultiValueMap<String, String>
            multiValueMap.putAll(listValueMap)
        }
        return multiValueMap
    }

    static Map<String, List<String>> generateConcatKeyParams(Map parameters){
        Map<String, List<String>> concatKeyListValueMap = [:]
        return generateConcatKeyParams(parameters, '', concatKeyListValueMap)
    }

    static Map<String, List<String>> generateConcatKeyParams(Map parameters, String prevKey, Map<String, List<String>> concatKeyListValueMap){
        parameters.keySet().each{ String key ->
            String concatKey = (prevKey) ? prevKey + '.' + key : key
            def value = parameters[key]
            if (value instanceof Map)
                generateConcatKeyParams(value, concatKey, concatKeyListValueMap)
            else{
                List<String> valueList = (value instanceof List) ? value : [value]
                concatKeyListValueMap[concatKey] = valueList.collect{ String.valueOf(it) }
            }
        }
        return concatKeyListValueMap
    }

    /*************************
     * Object Instance => JSON String
     *************************/
    static String toJsonString(def object) {
        return (object) ? JsonOutput.toJson(object) : ''
    }

    /*************************
     * JSON String => Object Instance
     *************************/
    static Object toObject(String jsonString){
        try{
            if (jsonString)
                return new JsonSlurper().parseText(jsonString)
        }catch(e){
            return jsonString
        }
        return null
    }

    static void sizeChecker(def object) {
        switch (object) {
            case { it instanceof Map }:
                println "SIZE: ${object.size()}"
                break
            case { it instanceof List }:
                println "SIZE: ${object.size()}"
                break
            default:
                println "VALUE: ${object}"
                break
        }
    }

    /*************************
     * Object Instance => JSON String
     *************************/
//    public static void objectToPostParams(final String key, final Object value, final Map<String, String> map) throws IllegalAccessException {
//        if ((value instanceof Number) || (value instanceof Enum) || (value instanceof String)) {
//            map.put(key, value.toString());
//        } else if (value instanceof Date) {
//            map.put(key, new SimpleDateFormat("yyyy-MM-dd HH:mm").format((Date) value));
//        } else if (value instanceof GenericDTO) {
//            final Map<String, Object> fieldsMap = ReflectionUtils.getFieldsMap((GenericDTO) value);
//            for (final Map.Entry<String, Object> entry : fieldsMap.entrySet()) {
//                final StringBuilder sb = new StringBuilder();
//                if (!GenericValidator.isEmpty(key)) {
//                    sb.append(key).append('.');
//                }
//                sb.append(entry.getKey());
//                objectToPostParams(sb.toString(), entry.getValue(), map);
//            }
//        } else if (value instanceof List) {
//            for (int i = 0; i < ((List) value).size(); i++) {
//                objectToPostParams(key + '[' + i + ']', ((List) value).get(i), map);
//            }
//        }
//    }






    /**************************************************
     *
     * Custom Assertion by sujkim
     *
     **************************************************/
    void makeTestDataWithExcel(String... resourcePath){

    }




    /**************************************************
     *
     * Custom Assertion by sujkim
     *
     **************************************************/
    static List<String> lastReport = []
    enum REQUEST_METHOD{ GET, POST, PUT, DELETE }
    enum FIND_METHOD{ BY_ONE, ALL_MATCHING }

    Object requestGetAndAssertSearch(String url, def parameters){
        return requestTestFind(url, parameters, parameters, REQUEST_METHOD.GET, FIND_METHOD.ALL_MATCHING)
    }

    Object requestGetAndAssertSearch(String url, def matchingParameters, def parameters){
        return requestTestFind(url, matchingParameters, parameters, REQUEST_METHOD.GET, FIND_METHOD.ALL_MATCHING)
    }

    Object requestGetAndAssertSearchByOne(String url, def matchingParameters, def requestParameters){
        return requestTestFind(url,  matchingParameters, requestParameters, REQUEST_METHOD.GET, FIND_METHOD.BY_ONE)
    }


    Object requestPostAndAssertSearch(String url, def parameters){
        return requestTestFind(url, parameters, parameters, REQUEST_METHOD.POST, FIND_METHOD.ALL_MATCHING)
    }

    Object requestPostAndAssertSearch(String url, def matchingParameters, def parameters){
        return requestTestFind(url, matchingParameters, parameters, REQUEST_METHOD.POST, FIND_METHOD.ALL_MATCHING)
    }

    Object requestPosAndAssertSearchByOne(String url, def matchingParameters, def requestParameters){
        return requestTestFind(url, matchingParameters, requestParameters, REQUEST_METHOD.POST, FIND_METHOD.BY_ONE)
    }


    Object requestTestFind(String url, def matchingParameters, def requestParameters, REQUEST_METHOD requestMethodCode, FIND_METHOD findMethodCode){
        def result
        int result_size
        boolean ok
        String okString
        def exception

        try{
            /** Request **/
            if (requestMethodCode == REQUEST_METHOD.GET)
                result = requestGet(url, requestParameters)
            if (requestMethodCode == REQUEST_METHOD.POST)
                result = requestPost(url, requestParameters)

            if (result instanceof List){
                /** Assert **/
                matchingParameters = new VariableMan(requestParameters).setModeMustExistCodeRule(false).parse(toCopyValueObject(matchingParameters))
                result_size = result.size()
                switch (findMethodCode){
                    case FIND_METHOD.ALL_MATCHING:
                        //결과리스트는 검증용필터로 검사해도 같아야 한다.
                        assert result_size == result.findAll{
                            boolean valid = checkEveryContains(it, matchingParameters)
                            if (!valid)
                                reportln it.toString()
                            return valid
                        }.size()
                        //결과리스트가 0개 이상일때, 검증용필터로 반대검사결과는 0개 and 결과리스트와 달라야 한다.
                        if (0 < result_size){
                            assert 0 < result.findAll{ checkEveryContains(it, matchingParameters) }.size()
                            assert 0 == result.findAll{ !checkEveryContains(it, matchingParameters) }.size()
                            assert result_size != result.findAll{ !checkEveryContains(it, matchingParameters) }.size()
                        }
                        break

                    case FIND_METHOD.BY_ONE:
                        String search = requestParameters.search
                        //결과리스트는 검증용필터로 검사해도 같아야 한다.
                        assert result_size == result.findAll{ checkAnyContains(it, matchingParameters, search) }.size()
                        //결과리스트가 0개 이상일때, 검증용필터로 반대검사결과는 0개 and 결과리스트와 달라야 한다.
                        if (0 < result_size){
                            assert 0 < result.findAll{ checkAnyContains(it, matchingParameters, search) }.size()
                            assert 0 == result.findAll{ !checkAnyContains(it, matchingParameters, search) }.size()
                            assert result_size != result.findAll{ !checkAnyContains(it, matchingParameters, search) }.size()
                        }
                        break
                    default:
                        break
                }

            }
            ok = true

        }catch(AssertionError e) {
            /** Assert Failed **/
            ok = false
            exception = e
        }catch(e){
            /** Failed **/
            ok = false
            exception = e
        }finally{
            /** Log **/
            okString = (ok) ? "\u001B[46m\u001B[30m  OK!  \u001B[0m" :  "\u001B[41m\u001B[30m  FAILED!  \u001B[0m"
//            sleep(1)
            reportln "-------------------------------------------------- TEST FIND - ${okString}"
            reportln "URL        : [${requestMethodCode.name()}] ${url}"
            reportln "PARAMETER  : ${requestParameters}"
            reportln "RESULT SIZE: ${result_size}"
            if (result){
                if (result.size() > 5){
                    reportln "RESULT     : ${result}"
                    for (int i=0; i<result.size() && i<5; i++){
                        reportln "  - ${result[i]}"
                    }
                    reportln "  ..."
                }else{
                    reportln "RESULT     :"
                    for (int i=0; i<result.size() && i<5; i++){
                        reportln "  - ${result[i]}"
                    }
                }


            }

            reportln "--------------------------------------------------\n"
            if (!ok)
                throw exception
        }
        return result
    }



    boolean checkEveryContains(def dataObject, Map conditionParameters){
        return checkEveryContains(dataObject, conditionParameters, null)
    }

    boolean checkEveryContains(def dataObject, Map conditionParameters, def searchValue){
        return conditionParameters.keySet().every{ String key ->
            def conditionValue = conditionParameters[key]
            def dataValue = dataObject[key]
            switch (conditionValue){
                case {it instanceof Map}:
                    return checkEveryContains(dataValue, conditionValue, searchValue)
                    break

                case {it instanceof List}:
                    def secondConditionValue = conditionValue[0]
                    if (secondConditionValue){
                        dataValue = dataValue ?: [toEmptyStringValueObject(secondConditionValue)]
                        return dataValue.any{ def secondDataValue ->
                            return checkEveryContains(secondDataValue, secondConditionValue, searchValue)
                        }

                    }else{
                        return true
                    }
                    break

                default:
                    def compareValue = (searchValue) ?: conditionValue
                    boolean valid = checkValue(dataValue, compareValue)
                    if (!valid)
                        reportln "===== [${key}] ${dataValue}(DATA) and ${compareValue}(CONDITION) do not match."
                    return valid
                    break
            }
        }
    }

    def toEmptyStringValueObject(def object){
        def newEmptyStringValueObject
        switch (object){
            case {it instanceof Map}:
                newEmptyStringValueObject = [:]
                object.each{ key, item ->
                    newEmptyStringValueObject[key] = toEmptyStringValueObject(item)
                }
                break

            case {it instanceof List}:
                newEmptyStringValueObject = []
                object.each{ item ->
                    newEmptyStringValueObject << toEmptyStringValueObject(item)
                }
                break

            default:
                newEmptyStringValueObject = ''
                break
        }
        return newEmptyStringValueObject
    }

    def toCopyValueObject(def object){
        def newCopyValueObject
        switch (object){
            case {it instanceof Map}:
                newCopyValueObject = [:]
                object.each{ key, item ->
                    newCopyValueObject[key] = toCopyValueObject(item)
                }
                break

            case {it instanceof List}:
                newCopyValueObject = []
                object.each{ item ->
                    newCopyValueObject << toCopyValueObject(item)
                }
                break

            default:
                newCopyValueObject = object
                break
        }
        return newCopyValueObject
    }

    boolean checkValue(def dataValue, def compareValue){
        switch (compareValue){
            //String
            case {it instanceof String}:
                return isMatchedValue(dataValue, compareValue)
                break
            //true: null
            //false: not null
            case {it instanceof Boolean}:
                return (compareValue) ? (!!dataValue) : (!dataValue)
                break
        }
        //null
        return true
    }

    static boolean isMatchedValue(String dataValue, String compareValue){
        String regexpStr = compareValue
                                .replace('(', '\\(').replace(')', '\\)')
                                .replace('[', '\\[').replace(']', '\\]')
                                .replace('.', '\\.').replace('$', '\\$')
                                .replace('*',".*")
//                                .replace('[^\\/\\\\]*[^\\/\\\\]*',"\\S*")
        dataValue = dataValue ?: ''
        return dataValue.matches(regexpStr)
    }

    boolean checkAnyContains(def dataObject, Map conditionParameters){
        return checkAnyContains(dataObject, conditionParameters, null)
    }

    boolean checkAnyContains(def dataObject, Map conditionParameters, def searchValue){
        if (!searchValue)
            return true
        return conditionParameters.keySet().any{ String key ->
            def conditionValue = conditionParameters[key]
            def dataValue = dataObject[key]
            if (conditionValue instanceof Map){
                return checkAnyContains(dataValue, conditionValue, searchValue)

            }else if (conditionValue instanceof List){
                return checkAnyContains(dataValue, conditionValue, searchValue)

            }else{
                String compareValue = (searchValue) ?: conditionValue
                return (dataValue && dataValue.indexOf(compareValue) != -1)
            }
        }
    }



    static File byteArrayToFile(byte[] buff, String filePath, String fileName) {
        if ((filePath == null || "".equals(filePath))
                || (fileName == null || "".equals(fileName))) { return null; }

        FileOutputStream fos = null;

        File fileDir = new File(filePath);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        File destFile = new File(filePath + fileName);

        try {
            fos = new FileOutputStream(destFile);
            fos.write(buff);
            fos.close();
        } catch (IOException e) {
            System.out.println("Exception position : FileUtil - binaryToFile(String binaryFile, String filePath, String fileName)");
        }

        return destFile;
    }



    /*************************
     * 테스트 후, 간단한 리포트를 위한 로그
     *************************/
    static void reportln(String log){
        lastReport << log
    }

    static void titleln(String log){
        lastReport << "=========================================================================== $log"
    }

}
