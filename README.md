# SpringBoot + MockMVC + RestDocs + Groovy 

## ⁉

### Environment 
- JDK 1.8
- SpringBoot 2.1.7 (Spring 5.1.9)
- SpringBootRestDocs 2.0.4 
- Gradle 4.10  
- Groovy 2.3.11

  
-------------------------  
### 1. Use this custom test module
`:avaj-core-mock`, It provides RestDocs and MockMVC.

1. You can check and test sample here: 
    - target controller:
        - `avaj-core-mock/src/test/groovy/com/avajjava/sample/test/controller/MockAndRestDocsTestController.groovy`
    - sample test:
        - `avaj-core-mock/src/test/groovy/com/avajjava/sample/test/controller/MockAndRestDocsTestControllerTest.groovy`
    
2. It will be parsed to HTML file for documents when it builds. 
    - `avaj-core-mock/src/main/asciidoc/mock-and-rest-docs-test.adoc`

3. Run
    - `/run.sh` to `gradle bootRun` to run WebApplication 
    
4. You can open documents !
    - http://localhost:8080/tester/docs/mock-and-rest-docs-test
    
    
-------------------------
### 2. Let's set it up in another module
It can be applied to other modules using `:avaj-core-mock`.

You can check how to setup other modules in `:avaj-engine-service` 

1. Setup MockMVC and RestDocs 
    - `avaj-engine-service/build.gradle`
        ```groovy
        plugins {
            id "org.asciidoctor.convert" version "1.5.9.2"
        }
        
        ext {
            snippetsDir = file('build/generated-snippets')
        }
        
        test {
            outputs.dir snippetsDir
        }
        
        asciidoctor {
            dependsOn test
            inputs.dir snippetsDir
            sourceDir = file('src/main/asciidoc')
            sources { include '*.adoc' }
            outputDir = file('build/docs')
        }
        
        jar {
            dependsOn asciidoctor
            from("${asciidoctor.outputDir}/html5"){
                into 'templates/docs'
            }
        }
        
        dependencies {        
            testCompile project(':avaj-core-mock')
            asciidoctor "org.springframework.restdocs:spring-restdocs-asciidoctor:${springRestDocs_version}"  
        }
        ```
      
2. Check your controller 
    - target controller: 
        - `avaj-engine-service/src/main/groovy/com/avajjava/sample/controller/ApiDataRestController.groovy`
    - test controller: 
        - `avaj-engine-service/src/test/groovy/com/avajjava/sample/controller/ApiDataRestControllerTest.groovy`

3. It will be parsed to HTML file for documents when build packages. 
    - `avaj-engine-service/src/main/asciidoc/other-module-test.adoc`

4. Run
    - `/run.sh` to `gradle bootRun` to run WebApplication 
       
5. You can open documents !
    - http://localhost:8080/tester/docs/other-module-test
    