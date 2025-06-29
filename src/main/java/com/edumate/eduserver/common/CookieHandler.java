package com.edumate.eduserver.common;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

@Component
public class CookieHandler {

    private static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";

    public void setRefreshTokenCookie(final HttpServletResponse response, final String refreshToken) {
        Cookie cookie = new Cookie(REFRESH_TOKEN_COOKIE_NAME, refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(14 * 24 * 60 * 60);
        cookie.setAttribute("SameSite", "None");
        response.addCookie(cookie);
    }

    public void clearRefreshTokenCookie(final HttpServletResponse response) {
        Cookie cookie = new Cookie(REFRESH_TOKEN_COOKIE_NAME, null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        cookie.setAttribute("SameSite", "None");
        response.addCookie(cookie);
    }
}
