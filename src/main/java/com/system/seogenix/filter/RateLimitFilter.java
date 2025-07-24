package com.system.seogenix.filter;

import com.system.seogenix.services.RateLimiterService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@AllArgsConstructor
public class RateLimitFilter implements Filter {

    private final RateLimiterService rateLimiterService;
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String ip = request.getRemoteAddr();

        if (rateLimiterService.isRateLimited(ip)) {
            HttpServletResponse httpResp = (HttpServletResponse) response;
            httpResp.setStatus(429);
            httpResp.getWriter().write("Rate limit exceeded. Try again later.");
            return;
        }

        chain.doFilter(request, response);
    }





}
