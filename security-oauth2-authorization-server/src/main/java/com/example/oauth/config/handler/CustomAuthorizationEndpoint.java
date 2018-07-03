package com.example.oauth.config.handler;

import org.springframework.security.oauth2.provider.endpoint.AuthorizationEndpoint;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.View;

import java.security.Principal;
import java.util.Map;

//@Component
public class CustomAuthorizationEndpoint extends AuthorizationEndpoint {
    @Override
    public View approveOrDeny(Map<String, String> approvalParameters, Map<String, ?> model, SessionStatus sessionStatus, Principal principal) {
        System.out.println(" ======================= ");
        return super.approveOrDeny(approvalParameters, model, sessionStatus, principal);
    }
}
