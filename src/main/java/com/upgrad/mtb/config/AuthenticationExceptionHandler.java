package com.upgrad.mtb.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;


@Component
public class AuthenticationExceptionHandler implements AuthenticationEntryPoint {
  @Override
  public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
      AuthenticationException e) throws IOException, ServletException {
    httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());

    Map<String, Object> data = new HashMap<>();
    data.put("timestamp", new Date());
    data.put("status", HttpStatus.FORBIDDEN.value());
    data.put("message", "Invalid token");
    data.put("path", httpServletRequest.getRequestURL().toString());

    OutputStream out = httpServletResponse.getOutputStream();
    ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(out, data);
    out.flush();
  }
}
