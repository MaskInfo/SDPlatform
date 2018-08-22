package cn.org.upthink.web.filter;

import cn.org.upthink.model.logger.RequestLoggerAttribute;
import com.google.common.base.Charsets;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author mask
 * 获取请求体保存起来  log生成需要
 */
public class RequestLoggerFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        logger.info("reuqestFilter...");
        ResettableStreamHttpServletRequest wrapperRequest = ResettableStreamHttpServletRequest.wrapper(request);
        byte[] requestBody = wrapperRequest.getRequestBody();
        String requestBodyStr = new String(requestBody, Charsets.UTF_8);
        if(request != null){
            request.setAttribute(RequestLoggerAttribute.REQUEST_BODY_ID,requestBodyStr);
        }
        //filterChain.doFilter(request,response);
        super.doFilter(wrapperRequest,response,filterChain);
        //todo   aspect insert logger
    }
}
