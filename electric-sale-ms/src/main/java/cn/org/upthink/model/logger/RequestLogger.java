package cn.org.upthink.model.logger;

import cn.org.upthink.converter.String2MapConverter;
import cn.org.upthink.util.UserContext;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Getter@Setter@ToString
public class RequestLogger {

    @JsonIgnore
    private final HttpServletRequest request = UserContext.getRequest();

    @JsonProperty("url")
    private String url;

    @JsonProperty("method")
    private String method;

    @JsonProperty("params_map")
    @JsonSerialize(using = String2MapConverter.class)
    private Map<String,Object> paramsMap;

    @JsonProperty("headers")
    @JsonSerialize(using = String2MapConverter.class)
    private Map<String,Object> headers;

    @JsonProperty("api_desc")
    private String apiDesc;

    @JsonProperty("request_body")
    @JsonSerialize(using = String2MapConverter.class)
    private String requestBody;

    @JsonProperty("request_time")
    @JsonFormat(pattern = "yyyy-MM-dd hh:ss:mm")
    private Date requestTime;

    @JsonProperty("response_time")
    @JsonFormat(pattern = "yyyy-MM-dd hh:ss:mm")
    private Date responseTime;

    @JsonProperty("character_encoding")
    private String characterEncoding;

    @JsonProperty("content_length")
    private long contentLength;

    @JsonProperty("remote_host")
    private String remoteHost;

    @JsonProperty("remote_port")
    private int remotePort;

    private Map fetchHttpHeaders(){
        Map<String, Object> map = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        String headerName;
        String header;
        while (headerNames.hasMoreElements()) {
            headerName = headerNames.nextElement();
            header = request.getHeader(headerName);
            map.put(headerName, header);
        }
        return map;
    }

    private Map fetchHttpRequestParams(){
        Map<String, Object> map = new HashMap<>();
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            String parameter = request.getParameter(paramName);
            map.put(paramName, parameter);
        }
        return map;
    }

    public RequestLogger(String apiDesc, Date responseTime){
        this.url = request.getRequestURL().toString();
        this.method = request.getMethod();
        this.requestBody = null;
        this.paramsMap = fetchHttpRequestParams();
        this.headers = fetchHttpHeaders();
        this.requestTime = new Date();
        this.characterEncoding = request.getCharacterEncoding();
        this.contentLength = request.getContentLength();
        this.remoteHost = request.getRemoteHost();
        this.remotePort = request.getRemotePort();
        this.apiDesc = apiDesc;
        this.responseTime = responseTime;
    }


}
