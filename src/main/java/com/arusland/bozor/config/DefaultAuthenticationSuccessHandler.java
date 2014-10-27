package com.arusland.bozor.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by ruslan on 27.10.2014.
 */
public class DefaultAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private static final int SECONDS_IN_DAY = 60 * 60 * 24;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest req,
                                        HttpServletResponse resp,
                                        Authentication authentication) throws IOException, ServletException {
        req.getSession().setMaxInactiveInterval(SECONDS_IN_DAY * 31);
        resp.sendRedirect("/");
    }
}
