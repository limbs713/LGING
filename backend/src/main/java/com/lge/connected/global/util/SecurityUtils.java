package com.lge.connected.global.util;

import com.lge.connected.global.jwt.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    public static Long getCurrentMemberId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            return userDetails.getId();
        }
        throw new IllegalStateException("User not authenticated");
    }
}
