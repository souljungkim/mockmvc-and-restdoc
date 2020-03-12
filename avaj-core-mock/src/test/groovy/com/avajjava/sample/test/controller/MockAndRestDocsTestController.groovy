package com.avajjava.sample.test.controller

import jaemisseo.man.FileMan
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

import javax.servlet.ServletOutputStream
import javax.servlet.http.HttpServletResponse
import java.nio.charset.Charset

@RestController
@RequestMapping("/test/mock/*")
class MockAndRestDocsTestController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());



    /**************************************************
     *
     *  GET
     *
     **************************************************/
    @GetMapping("test-get")
    def testGet(String hi, String[] dd) {
        logger.debug 'test-get'
        println hi
        println dd
        return [code:'hellotest123']
    }

    @GetMapping("test-get-with-path-parameters/{group}/{id}")
    def testGetWithPathParameters(@PathVariable String group, @PathVariable String id, String name, String hi, String dd){
        logger.debug "test-get-with-path-parameter:  /${group}/${id}"
        println hi
        println dd
        return [code:'hellotest123']
    }

    @GetMapping("test-get-with-header/{name}")
    def testGetWithHeader(HttpServletResponse response, String name, String hi, String dd) {
        logger.debug 'test-get-with-header'
        println hi
        println dd
        response.addHeader("test-secret-code-return", "Sinna Jaemisseo");
        return [code:'hellotest123']
    }



    /**************************************************
     *
     *  POST
     *
     **************************************************/
    @PostMapping("test-post")
    def testPost(@RequestBody Map<String, Object> testMap, String name, String hi, String[] dd) {
        logger.debug 'test-post'
        println testMap
        println hi
        println dd
        return [result: true]
    }



    /**************************************************
     *
     *  PUT
     *
     **************************************************/
    @PutMapping("test-put")
    def testPut(@RequestBody Map<String, Object> testMap, String name, String hi, String[] dd) {
        logger.debug 'putReturn'
        println testMap
        println hi
        println dd
        return [result: true]
    }



    /**************************************************
     *
     *  DELETE
     *
     **************************************************/
    @DeleteMapping("test-delete")
    def testDelete(String name, String hi, String[] dd) {
        logger.debug 'deleteReturn'
        println hi
        println dd
        return [result: true]
    }



    /***************************************************************************
     *
     * with File
     *  - Upload with MULTIPART
     *  - Download
     *
     ***************************************************************************/
    @PostMapping("test-upload-with-multipart")
    def testUploadWithMultipart(@RequestParam('data') List<MultipartFile> data, @RequestPart(name='metadata', required=false) Map<String, Object> metadata, @RequestParam(name='paramtest', required=false) String paramtest, String hi, String[] dd) {
        logger.debug "test-upload-with-multipart"
        data.eachWithIndex{ it, index ->
            logger.debug "${index}) ${it.originalFilename} (${it.contentType}):"
            logger.debug "${new String(it.content)}"
        }
        println metadata
        println paramtest
        println hi
        println dd
        //Handle Files...
        return [result: true]
    }

    @GetMapping('test-download')
    void testDownload(HttpServletResponse res){
        logger.debug 'test-download'
        //- Load File
        String fileName = 'test-download.txt'
        String filePath = "com/avajjava/sample/test/download/${fileName}"
//        String fileContent = 'Hello Download Text'
        String fileContent = FileMan.getFileFromResource(filePath).getText("UTF-8")
        byte[] fileContentAsByte = fileContent.getBytes(Charset.forName("UTF-8"))
        //- Download File
        try{
            ServletOutputStream out = setHeaderForDownloadTxt(res, fileName).getOutputStream()
            out.write(fileContentAsByte)
//            out.println(fileContent);
            out.flush()
            out.close()
        }catch(e){
            logger.error("Error on Downloading User Error Datas with Excel", e)
            throw e
        }
    }

    static HttpServletResponse setHeaderForDownloadTxt(HttpServletResponse response, String fileName) {
        response.setHeader("Content-disposition","attachment;filename=" +fileName);
        response.setHeader("Content-Type", "text/plain");
        response.setHeader("Content-Description", "JSP Generated Data");
        response.setHeader("Content-Transfer-Encoding", "binary;");
        response.setHeader("Pragma", "no-cache;");
        response.setHeader("Expires", "-1;");
        return response
    }

    static HttpServletResponse setHeaderForDownloadExcel(HttpServletResponse response, String fileName) {
        response.setHeader("Content-disposition","attachment;filename=" +fileName);
        response.setHeader("Content-Type", "application/vnd.ms-excel; charset=MS949");
        response.setHeader("Content-Description", "JSP Generated Data");
        response.setHeader("Content-Transfer-Encoding", "binary;");
        response.setHeader("Pragma", "no-cache;");
        response.setHeader("Expires", "-1;");
        return response
    }

}
