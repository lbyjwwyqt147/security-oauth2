package com.example.oauth.controller;

import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.common.exceptions.*;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/***
 * 自定义 oauth2 授权页面
 */
@Controller
@SessionAttributes("authorizationRequest")
public class OAuth2ApprovalController {

    @RequestMapping("/oauth/my_approval_page")
    public String getAccessConfirmation(Map<String, Object> model, HttpServletRequest request) throws Exception {
        System.out.println(" ================= 进入跳转授权页  ======================= ");
        Map<String, String> scopes = (Map<String, String>) (model.containsKey("scopes") ? model.get("scopes") : request.getAttribute("scopes"));
        List<String> scopeList = new ArrayList<String>();
        /*for (String scope : scopes.keySet()) {
            scopeList.add(scope);
        }*/
        model.put("scopeList", scopeList);
        return "oauth_approval";
    }

    @RequestMapping({ "/oauth/my_approval_error_page" })
    public String handleError(Map<String, Object> model, HttpServletRequest request) {
        System.out.println(" ================= 进入授权异常  ======================= ");
        Object error = request.getAttribute("error");
        String errorSummary;
        if (error instanceof OAuth2Exception) {
            OAuth2Exception oauthError = (OAuth2Exception) error;
            errorSummary = HtmlUtils.htmlEscape(oauthError.getSummary());
        } else {
            errorSummary = "Unknown error";
        }
        model.put("errorSummary", errorSummary);
        System.out.println(errorSummary);
        return "oauth_error";
    }



    @RequestMapping(
            value = {"/oauth/authorize"},
            method = {RequestMethod.POST},
            params = {"user_oauth_approval"}
    )
    public View approveOrDeny(@RequestParam Map<String, String> approvalParameters, Map<String, ?> model, SessionStatus sessionStatus, Principal principal,HttpServletRequest request) {
        System.out.println( SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        SecurityContextImpl securityContextImpl = (SecurityContextImpl) request
                .getSession().getAttribute("SPRING_SECURITY_CONTEXT");
        System.out.println("Username:"
                + securityContextImpl.getAuthentication().getName());
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        //principal = (Principal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof Authentication)) {
            sessionStatus.setComplete();
            throw new InsufficientAuthenticationException("User must be authenticated with Spring Security before authorizing an access token.");
        } else {
            AuthorizationRequest authorizationRequest = (AuthorizationRequest)model.get("authorizationRequest");
            if (authorizationRequest == null) {
                sessionStatus.setComplete();
                throw new InvalidRequestException("Cannot approve uninitialized authorization request.");
            } else {
                View var8 = null;
           /*     try {
                    Set<String> responseTypes = authorizationRequest.getResponseTypes();
                    authorizationRequest.setApprovalParameters(approvalParameters);
                    authorizationRequest = this.userApprovalHandler.updateAfterApproval(authorizationRequest, (Authentication)principal);
                    boolean approved = this.userApprovalHandler.isApproved(authorizationRequest, (Authentication)principal);
                    authorizationRequest.setApproved(approved);
                    if (authorizationRequest.getRedirectUri() == null) {
                        sessionStatus.setComplete();
                        throw new InvalidRequestException("Cannot approve request when no redirect URI is provided.");
                    }

                    if (!authorizationRequest.isApproved()) {
                        RedirectView var12 = new RedirectView(this.getUnsuccessfulRedirect(authorizationRequest, new UserDeniedAuthorizationException("User denied access"), responseTypes.contains("token")), false, true, false);
                        return var12;
                    }

                    if (responseTypes.contains("token")) {
                        var8 = this.getImplicitGrantResponse(authorizationRequest).getView();
                        return var8;
                    }

                    var8 = this.getAuthorizationCodeResponse(authorizationRequest, (Authentication)principal);
                } finally {
                    sessionStatus.setComplete();
                }*/

                return var8;
            }
        }
    }
}
