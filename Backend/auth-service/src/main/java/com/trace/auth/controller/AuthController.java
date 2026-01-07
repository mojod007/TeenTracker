package com.trace.auth.controller;

import com.trace.auth.dto.AuthRequest;
import com.trace.auth.security.JwtUtils;
import com.trace.auth.service.CustomUserDetailsService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtUtils jwtUtils;

    @GetMapping("/login")
    public String login(@org.springframework.web.bind.annotation.RequestParam(value = "redirect", required = false) String redirect, 
            org.springframework.ui.Model model) {
        model.addAttribute("redirect", redirect);
        return "login";
    }

    @PostMapping("/api/auth/login")
    public String createAuthenticationToken(@ModelAttribute AuthRequest authRequest,
            HttpServletResponse response) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        } catch (BadCredentialsException e) {
            return "redirect:/login?error=true";
        } catch (Exception e) {
            return "redirect:/login?error=unknown";
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
        final String jwt = jwtUtils.generateToken(userDetails);

        // Set JWT in cookie
        Cookie cookie = new Cookie("jwt", jwt);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(86400); // 24 hours
        response.addCookie(cookie);

        // Redirect back to original location or default dashboard
        String redirectUrl = authRequest.getRedirect();
        if (redirectUrl == null || redirectUrl.isEmpty() || redirectUrl.equals("null") || redirectUrl.contains("/login")) {
            redirectUrl = "/dashboard";
        }
        
        return "redirect:" + redirectUrl;
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("jwt", null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/login";
    }
}
