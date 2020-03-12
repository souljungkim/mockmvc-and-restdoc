package com.avajjava.sample.test.controller

import com.avajjava.sample.AvajCoreMockTestHelper
import jaemisseo.man.FileMan
import org.junit.Before
import org.junit.Test
import org.springframework.mock.web.MockMultipartFile
import org.springframework.restdocs.payload.JsonFieldType

import static org.springframework.restdocs.headers.HeaderDocumentation.*
import static org.springframework.restdocs.payload.PayloadDocumentation.*
import static org.springframework.restdocs.request.RequestDocumentation.*

class MockAndRestDocsTestControllerTest extends AvajCoreMockTestHelper {

    /***************************************************************************
     *
     * Setup
     *
     ***************************************************************************/
    @Before
    void before() {
    }

    String baseFilePath = "com/avajjava/sample/test"



    /***************************************************************************
     *
     * Test - GET
     *
     ***************************************************************************/
    @Test
    void testGet(){
        String returnData = requestGet('/test/mock/test-get',
                documentAuto(
                        requestParameters(
                                parameterWithName("hi").description("아이디"),
                                parameterWithName("dd").description("디디디디디").optional()
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("결과코드"),
                        ),
                ),
                parameterValues([
                        hi: 'hi get',
                        dd: ['dddd', 'ddd']
                ]),
        )
        println returnData
    }

    @Test
    void testGetWithPathParameters(){
        requestGet('/test/mock/test-get-with-path-parameters/{group}/{id}',
                documentAuto(
                        pathParameters(
                                parameterWithName("group").description("그룹명"),
                                parameterWithName("id").description("ID"),
                        ),
                        requestParameters(
                                parameterWithName("hi").description("아이디"),
                                parameterWithName("dd").description("디디디디디").optional()
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("결과코드"),
                        ),
                ),
                pathValues(
                        'IQ160_CLUB', 'ekj'
                ),
                parameterValues([
                        hi: 'hi get',
                        dd: ['dddd', 'ddd']
                ]),
        )
    }

    @Test
    void testGetWithHeader() {
        String returnData = requestGet('/test/mock/test-get-with-header/sujung',
                documentAuto(
                        requestParameters(
                                parameterWithName("hi").description("아이디"),
                                parameterWithName("dd").description("아이디").optional()
                        ),
                        requestHeaders(
                                headerWithName("test-secret-code").description("Test Request-Header")
                        ),
                        responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("결과코드"),
                        ),
                        responseHeaders(
                                headerWithName("test-secret-code-return").description("Test Response-Header"),
                        )
                ),
                parameterValues([
                        hi: 'hi get',
                        dd: ['dddd', 'ddd']
                ]),
                headerValues([
                        'test-secret-code': 12345
                ])
        )
        println returnData
    }



    /***************************************************************************
     *
     * Test - POST
     *
     ***************************************************************************/
    @Test
    void testPost() {
        String returnData = requestPost('/test/mock/test-post',
                documentAuto(
                        requestFields(
                                fieldWithPath("hi").description("아이디"),
                                fieldWithPath("dd").description("띠리띠리").optional()
                        ),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.BOOLEAN).description("결과코드"),
                        ),
                ),
                bodyValues([
                        hi: 'hi post',
                        dd: ['dddd', 'ddd']
                ])
        )
        println returnData
    }



    /***************************************************************************
     *
     * Test - PUT
     *
     ***************************************************************************/
    @Test
    void testPut() {
        String returnData = requestPut('/test/mock/test-put',
                documentAuto(
                        requestFields(
                                fieldWithPath("hi").description("아이디"),
                                fieldWithPath("dd").description("띠리띠리").optional()
                        ),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.BOOLEAN).description("결과코드"),
                        ),
                ),
                bodyValues([
                    hi: 'hi put',
                    dd: ['dddd', 'ddd']
                ])
        )
        println returnData
    }



    /***************************************************************************
     *
     * Test - DELETE
     *
     ***************************************************************************/
    @Test
    void testDelete() {
        String returnData = requestDelete('/test/mock/test-delete',
                documentAuto(
                        requestParameters(
                                parameterWithName("hi").description("아이디"),
                                parameterWithName("dd").description("아이디").optional()
                        ),
                        responseFields(
                                fieldWithPath("result").type(JsonFieldType.BOOLEAN).description("결과코드"),
                        ),
                ),
                parameterValues([
                    hi: 'hi delete',
                    dd: ['dddd', 'ddd']
                ])
        )
        println returnData
    }



    /***************************************************************************
     *
     * Test
     *  - Upload with MULTIPART
     *  - Download
     *
     ***************************************************************************/
    @Test
    void testUploadWithMultipart(){
        String fileName = 'test-upload-multipart.txt'
        String filePath = "${baseFilePath}/upload/${fileName}"
        String returnData = requestPostWithMultipart('/test/mock/test-upload-with-multipart',
                documentAuto(
                        requestParts(
                                partWithName("data").description("The file to upload"),
                                partWithName("metadata").description("The version of the image")
                        ),
//                        requestPartBody("metadata"),
                        requestPartFields("metadata",
                                fieldWithPath("version").description("The version of the image")
                        ),
                        requestParameters(
                                parameterWithName("paramtest").description("as parameter"),
                                parameterWithName("hi").description("hello"),
                                parameterWithName("dd").description("ddiddi dddddi"),
                        )
                ),
                multipartValues(
                        new MockMultipartFile('data', 'test.txt', 'text/plain', 'hihihi hihihi'.getBytes()),
                        new MockMultipartFile('data', 'test2.txt', 'text/plain', 'hihihi2 hihihi2'.getBytes()),
                        new MockMultipartFile('data', fileName, 'text/plain', new FileInputStream(FileMan.getFileFromResource(filePath))),
                        new MockMultipartFile("data", "image.png", "image/png", "<<png data>>".getBytes()),
                        new MockMultipartFile("metadata", "", "application/json", "{ \"version\": \"1.0\"}".getBytes())
                ),
                parameterValues([
                        paramtest: 'hello param value :D',
                        hi: 'hi get',
                        dd: ['dddd', 'ddd']
                ])
        )
        println returnData
    }

    @Test
    void testDownload(){
        String contentsToCompare = FileMan.getFileFromResource("${baseFilePath}/download/test-download.txt").getText()

        /** Method 1 - As string data **/
        String returnData = requestGet('/test/mock/test-download',
                documentAuto(
                        //None
                )
        )
        assert contentsToCompare.length() == returnData.length()
        assert contentsToCompare.equals(returnData)

        /** Method 2 - As byte arrays data **/
        byte[] bytes = requestGet('/test/mock/test-download',
                documentAuto(
                        //None
                ),
                optionValues(true)
        )
        String downloadData = new String(bytes)
        assert contentsToCompare.length() == downloadData.length()
        assert contentsToCompare.equals(downloadData)

    }



}