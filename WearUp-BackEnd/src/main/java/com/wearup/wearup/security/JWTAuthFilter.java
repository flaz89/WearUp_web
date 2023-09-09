package com.wearup.wearup.security;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.wearup.wearup.brand.Brand_Service;
import com.wearup.wearup.exception.UnauthorizedException;
import com.wearup.wearup.product.Product_Service;
import com.wearup.wearup.user.User;
import com.wearup.wearup.user.User_Service;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTAuthFilter extends OncePerRequestFilter {
	
	@Autowired
	private JWTTools jwttools;
	
	@Autowired
	private User_Service userSrv;
	
	@Autowired
	@Lazy
	private Brand_Service brandSrv;
	
	

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String authHeader = request.getHeader("Authorization");

		if (authHeader == null || !authHeader.startsWith("Bearer "))
			throw new UnauthorizedException("Per favore passa il token nell'authorization header");
		String token = authHeader.substring(7);
		System.out.println("TOKEN -------> " + token);

		jwttools.verifyToken(token);

		String id = jwttools.extractSubject(token);
		
		
		// ---------------------------
		String entityType = jwttools.extractEntityType(token);  // ipotetico metodo per ottenere il tipo di entità
		System.out.println(entityType);
	    AuthenticatedEntity authenticatedEntity = null;

	    if ("User".equals(entityType)) {
	        authenticatedEntity = userSrv.findById(UUID.fromString(id));
	    } else if ("Brand".equals(entityType)) {
	        authenticatedEntity = brandSrv.findById(Long.parseLong(id));
	    } else {
	        throw new UnauthorizedException("Tipo di entità non valido");
	    }
		// ---------------------------
		
		//User currentUser = userSrv.findById(UUID.fromString(id));
		
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(authenticatedEntity, null,
				authenticatedEntity.getAuthorities());

		SecurityContextHolder.getContext().setAuthentication(authToken);

		filterChain.doFilter(request, response);
	}

//	@Override
//	protected boolean shouldNotFilter(HttpServletRequest request) {
//		System.out.println("Path: " + request.getServletPath());
//		return new AntPathMatcher().match("/auth/**", request.getServletPath());
//	}
	
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
	    String path = request.getServletPath();
	    AntPathMatcher matcher = new AntPathMatcher();
	    
	    System.out.println("Path: " + path);
	    
	    return matcher.match("/auth/**", path) || matcher.match("/products/home", path);
	}

}
