package com.avajjava.sample.config.filter

import com.avajjava.sample.util.Util
import jaemisseo.man.AnsiMan
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

import javax.servlet.*
import javax.servlet.http.HttpServletRequest

@Component('userAccessLogFilter')
class UserAccessLogFilter implements Filter{

    static final Logger logger = LoggerFactory.getLogger(this)



    @Override
    void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        /** Logger가 Debug Level일 때 이쁘게 Logging을 위한 Logic이다. **/
        try{
            boolean isMultipart = ServletFileUpload.isMultipartContent(servletRequest)
            boolean isDebugLevel = logger.isDebugEnabled()
            if (isDebugLevel){
                //※ Multipart로 들어올 경우 RequestData를 보여주는 것을 제거해둠..
                //TODO: 개발을 위해서 호출시 사용하는 Data를 이쁘게 Logging하기위해
                //TODO: servletRequest.getReader()을 사용하여 Data를 빼오는데, 이 Method를 한 번 호출하면 Spring 이놈은 그 Data를 못 가져가게 되어있다 (1회만 호출가능하기 때문)
                //TODO: 이를 보완할 수 있는 MultiReadRequestWrapper Class를 이용하였지만, Multipart의 경우는 보완이 안됨.
                //TODO: ==> 때문에 Multipart의 경우는 RequestData를 보여주지 않도록함. ==> 추후 여유가 되어 해결이 가능하다면 좋겠네.
                if (isMultipart){
                    logWhenDevelop(servletRequest, '[MULTIPART]')
                    filterChain.doFilter(servletRequest, servletResponse)
                }else{
                    HttpServletRequest httpServletRequest = new MultiReadRequestWrapper((HttpServletRequest) servletRequest)
                    logWhenDevelop(httpServletRequest)
                    /** Controller 실행~ :D **/
                    filterChain.doFilter(httpServletRequest, servletResponse)
                }
            }
        }catch(e){
//            logger.warn('Error during parsing request data to debug', e)
            throw e
        }
    }

    @Override
    void destroy(){
    }



    /****************************************************************************************************
     *
     * Log for Developer
     *
     ****************************************************************************************************/
    void logWhenDevelop(ServletRequest servletRequest){
        logWhenDevelop(servletRequest, null)
    }

    void logWhenDevelop(ServletRequest servletRequest, String optionLog){
        if (logger.isDebugEnabled()){
            String contextPath = ((HttpServletRequest)servletRequest).contextPath
            String requestURI = ((HttpServletRequest)servletRequest).requestURI
            String method = 'UNKNOWN'
            try{
                method = servletRequest?.request?.request?.coyoteRequest?.methodMB?.strValue
            }catch(e){
                try{
                    method = servletRequest?.request?.coyoteRequest?.methodMB?.strValue
                }catch(e2){
                }
            }

            boolean modeControllerPath = !['.', '?'].find{ requestURI.contains(it) }
            boolean modeDevelopUri = ['/development/', '/admin/maintenance'].find{ requestURI.contains(it) }
            if (modeDevelopUri){
                if (!logger.isTraceEnabled())
                    return;
            }
            if (modeControllerPath){
                String logForRequestURI = (modeDevelopUri) ? AnsiMan.testRainbowBg("  ${requestURI}  ") : AnsiMan.testCyanBg("  ${requestURI}  ")
                String logForRequestData = (optionLog != null) ? optionLog : makeRequestDataText(servletRequest)
                //Log
                println ""
                logger.debug "${method} ${logForRequestURI} \n  ${logForRequestData}"
            }
        }
    }

    String makeRequestDataText(ServletRequest servletRequest){
        String dataType = ''
        String dataText = 'null'
        Map parameterMap = servletRequest.getParameterMap()
        String requestBody = servletRequest.getReader().readLine()
        try{
            if (parameterMap){
                dataText = Util.syntaxHighlightForConsole(parameterMap)
                dataType = 'PARAMETER'
            }else if (requestBody){
                dataText = Util.syntaxHighlightForConsole(requestBody)
                dataType = 'JSON'
            }
        }catch(e){
            dataText += AnsiMan.testRedBg('!! Has Error during making highlight !!')
        }
        return "[${dataType}] <== ${dataText}"
    }

}
