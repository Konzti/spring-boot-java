package demo.com.jwtdemo.security;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import static java.util.Arrays.stream;


@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    private JWTUtils jwtUtils;


    public CustomAuthorizationFilter(JWTUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().equals("/login")) {
            filterChain.doFilter(request, response);
        } else {
            try {
               String token = WebUtils.getCookie(request, "auth").getValue();
               JWTVerifier verifier = jwtUtils.getVerifier();
               DecodedJWT decodedJWT = verifier.verify(token);
               String username = decodedJWT.getSubject();
               String[] roles = jwtUtils.getClaimsFromToken(token);
               Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
               stream(roles).forEach(role -> {
                   authorities.add(new SimpleGrantedAuthority(role));
               });
                UsernamePasswordAuthenticationToken userToken =
                        new UsernamePasswordAuthenticationToken(username, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(userToken);
                filterChain.doFilter(request, response);
           } catch (Exception e) {
                if (e.getMessage() == null){
                    log.error("no token found");
                    response.sendError(403,"No token found");
                } else {
                log.error("Error logging in: {}" , e.getMessage());
                response.setHeader("error", e.getMessage());
                response.sendError(403,"token not valid: " + e.getMessage());
                }
           }
        }
    }
}
