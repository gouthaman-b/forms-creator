package com.example.formscreator.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.formscreator.service.JWTService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    
	@Autowired
    private UserDetailsService userDetailsService;
	@Autowired
    private JWTService jwtService;

    private List<String> AUTH_WHITE_LIST = Arrays.asList(
        "/api/v1/auth/login",
            "/api/v1/auth/register",
            "/swagger-ui/",
            "/v2/api-docs", 
            "/swagger-resources/configuration/ui", 
            "/swagger-resources", 
            "/swagger-resources/configuration/security", 
            "/swagger-ui.html", 
            "/webjars/",
            "/api/v1/demo",
            "/swagger-ui/index.html",
            "/swagger-ui/swagger-ui.css?v=3.0.0",
            "/swagger-ui/springfox.css?v=3.0.0"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        if(AUTH_WHITE_LIST.contains(request.getRequestURI())){
            logger.info("White Listed");
            filterChain.doFilter(request, response);
            return;
        }

        logger.info("Auth Header: "+authHeader);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(400);
            response.getWriter().write("No Auth Token");
            return;
        }

        jwt = authHeader.substring(7);
        try{
            userEmail = jwtService.extractUserEmail(jwt);
        }catch(Exception e){
            response.setStatus(400);
            response.getWriter().write("Invalid Token");
            return;
        }
        
        if(userEmail!=null && SecurityContextHolder.getContext().getAuthentication()==null){
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
            try{
            if (jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    null
                );
                authToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }}
            catch(Exception e){
                response.setStatus(400);
                response.getWriter().write("Invalid Token");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

}