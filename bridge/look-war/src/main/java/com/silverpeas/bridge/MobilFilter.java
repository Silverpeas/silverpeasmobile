package com.silverpeas.bridge;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MobilFilter implements Filter {

  @Override
  public void destroy() {
  }

  @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

    String userAgent = ((HttpServletRequest) req).getHeader("User-Agent");

    if (userAgent.contains("Android") || userAgent.contains("iPhone")) {

      

      ((HttpServletResponse) res).sendRedirect("/spmobile");
      return;
    } else {
      chain.doFilter(req, res);
    }
  }

  @Override
  public void init(FilterConfig arg0) throws ServletException {
  }

}
