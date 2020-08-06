package com.upgrad.mtb.security.jwt;

import com.upgrad.mtb.config.AuthenticationExceptionHandler;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


public class JwtConfigurer extends SecurityConfigurerAdapter<org.springframework.security.web.DefaultSecurityFilterChain, HttpSecurity> {

  private JwtTokenProvider jwtTokenProvider;

  public JwtConfigurer(JwtTokenProvider jwtTokenProvider) {
    this.jwtTokenProvider = jwtTokenProvider;
  }

  @Override
  public void configure(HttpSecurity http) throws Exception {
    JwtTokenFilter customFilter = new JwtTokenFilter(jwtTokenProvider);
    ExceptionTranslationFilter exceptionTranslationFilter= new ExceptionTranslationFilter(new AuthenticationExceptionHandler());

    //http.addFilterAfter(customFilter, UsernamePasswordAuthenticationFilter.class);
    http.addFilterAfter(customFilter, ExceptionTranslationFilter.class);
  }
}