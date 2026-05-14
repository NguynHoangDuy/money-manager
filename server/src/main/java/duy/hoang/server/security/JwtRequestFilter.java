package duy.hoang.server.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import duy.hoang.server.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {
     private final UserDetailsService userDetailsService;
     private final JwtUtil jwtUtil;

     @Override
     protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
          final String authHeader = request.getHeader("Authorization");
          String email = null;
          String jwt = null;

          if (authHeader != null && authHeader.startsWith("Bearer ")) {
               jwt = authHeader.substring((7));
               email = jwtUtil.extractUserName(jwt);
          }

          if (email != null & SecurityContextHolder.getContext().getAuthentication() == null) {
               UserDetails userDetails = this.userDetailsService.loadUserByUsername(email);
               if (jwtUtil.validateToken(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
               }
          }

          filterChain.doFilter(request, response);
     }
}
