package com.example.devicesms.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class JWTFilter extends OncePerRequestFilter {

    Logger logger = Logger.getLogger(JWTFilter.class.getName());

    @Value("${jwt.secret}")
    private String secret;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        logger.info("am intrat man ");
        //Get authorization header and validate
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(!StringUtils.hasText(header) || (StringUtils.hasText(header) && !header.startsWith("Bearer "))){
            chain.doFilter(request,response);
            return;
        }
        //Get jwt token
        final String token = header.split(" ")[1].trim();
        logger.info("token: " + token);
        // Validate and parse the token
        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();



        //Get user identity and set it on the spring security context
        String userNameFromToken =  claims.getSubject();

        logger.info("username: " + userNameFromToken);

        //and validate
        if (userNameFromToken != null) {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userNameFromToken, null, null);

            // Set the authenticated user in the SecurityContext
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        chain.doFilter(request,response);

    }
}
