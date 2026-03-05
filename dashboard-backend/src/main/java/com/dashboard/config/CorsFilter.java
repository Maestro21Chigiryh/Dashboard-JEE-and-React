package com.dashboard.config;

import java.io.IOException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebFilter("/*")
public class CorsFilter implements Filter {
    
// Update the CORS filter to allow your frontend origin
@Override
public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
    HttpServletResponse httpResponse = (HttpServletResponse) response;
    httpResponse.setHeader("Access-Control-Allow-Origin", "https://dashboard-frontend.vercel.app"); // Allow all origins for now
    // Or specifically set it to your frontend URL:
    // httpResponse.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");
    httpResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
    httpResponse.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
    httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
    httpResponse.setHeader("Access-Control-Max-Age", "3600");
    
    // Handle preflight OPTIONS requests
    if (((HttpServletRequest) request).getMethod().equals("OPTIONS")) {
        httpResponse.setStatus(HttpServletResponse.SC_OK);
        return;
    }
    
    chain.doFilter(request, response);
}
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // No initialization needed
    }
    
    @Override
    public void destroy() {
        // No cleanup needed
    }
}