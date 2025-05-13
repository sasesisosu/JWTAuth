package com.jwtauth.assignment.common;

import com.jwtauth.assignment.model.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authorization = request.getHeader("Authorization");
        // 로그인 요청은 필터를 건너뜀
        if (request.getRequestURI().equals("/api/users/login") || request.getRequestURI().equals("/api/users/signup")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Header 검증
        if(authorization == null || !authorization.startsWith("Bearer ")) {
            sendUnauthorizedResponse(response);
            response.getWriter().write("토큰이 비어있거나 형식이 잘못되었습니다.");
            return;
        }

        String token = authorization.split(" ")[1];
        String userId = jwtUtil.getUserId(token);

        // 토큰 소멸 시간 검증
        if(jwtUtil.isExpired(token)) {
            sendUnauthorizedResponse(response);
            response.getWriter().write("토큰 시간이 소멸되었습니다.");
            return;
        }

        if("N".equals(userId)) {
            sendUnauthorizedResponse(response);
            response.getWriter().write("토큰이 비어있거나 형식이 잘못되었습니다.");
            return;
        }

        List<GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
        User user = new User(userId, "tempPw", "tempNm", "tempEmail", "ROLE_USER");


        Authentication auth = new UsernamePasswordAuthenticationToken(user, null, authorities);

        SecurityContextHolder.getContext().setAuthentication(auth);
        filterChain.doFilter(request,response);


    }

    private void sendUnauthorizedResponse(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding("UTF-8");
    }
}
