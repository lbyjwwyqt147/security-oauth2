package com.example.oauth.config.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.security.oauth2.provider.approval.UserApprovalHandler;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CustomApprovalHandler implements UserApprovalHandler {
    @Override
    public boolean isApproved(AuthorizationRequest authorizationRequest, Authentication authentication) {
        System.out.println("=============");
        return false;
    }

    @Override
    public AuthorizationRequest checkForPreApproval(AuthorizationRequest authorizationRequest, Authentication authentication) {
        return null;
    }

    @Override
    public AuthorizationRequest updateAfterApproval(AuthorizationRequest authorizationRequest, Authentication authentication) {
        return null;
    }

    @Override
    public Map<String, Object> getUserApprovalRequest(AuthorizationRequest authorizationRequest, Authentication authentication) {
        return null;
    }
}
