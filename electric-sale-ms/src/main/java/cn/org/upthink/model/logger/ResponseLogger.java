package cn.org.upthink.model.logger;

import cn.org.upthink.converter.String2MapConverter;
import cn.org.upthink.util.UserContext;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

@Getter@Setter@ToString
public class ResponseLogger {


    public static ResponseLogger with(Object responseBody){
        return new ResponseLogger(responseBody);
    }
    private ResponseLogger(Object responseBody){
        this.responseBody = responseBody;
    }
    @JsonIgnore
    private final HttpServletResponse request = UserContext.getResponse();
    @JsonProperty("headers")
    private Map<String,Object> headers;//todo
    @JsonProperty("response_body")
    private Object responseBody;

}
