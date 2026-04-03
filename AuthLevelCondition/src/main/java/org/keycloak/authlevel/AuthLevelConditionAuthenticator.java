package org.keycloak.authlevel;

import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.authenticators.conditional.ConditionalAuthenticator;
import org.keycloak.models.AuthenticatorConfigModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;

import org.jboss.logging.Logger;

public class AuthLevelConditionAuthenticator implements ConditionalAuthenticator {
    private Logger logger = Logger.getLogger(AuthLevelConditionAuthenticator.class);
    @Override
    public boolean matchCondition(AuthenticationFlowContext authenticationFlowContext) {
        UserModel user = authenticationFlowContext.getUser();
        if (user == null) {
            logger.info("No user found in authentication context");
            return false;
        }
        String userAuthLevel = user.getFirstAttribute("authLevel");
        AuthenticatorConfigModel authenticatorConfig = authenticationFlowContext.getAuthenticatorConfig();
        String need_authLevels = authenticatorConfig.getConfig().get("set.authLevel");
        if (need_authLevels == null) {
            logger.info("No set authLevel found in authentication context");
            return false;
        }
        String[] authLevels = need_authLevels.split("##");
        for (String authLevel : authLevels)
            if (userAuthLevel.equals(authLevel))
                return true;
        return false;
    }

    @Override
    public void action(AuthenticationFlowContext authenticationFlowContext) {

    }

    @Override
    public boolean requiresUser() {
        return false;
    }

    @Override
    public void setRequiredActions(KeycloakSession keycloakSession, RealmModel realmModel, UserModel userModel) {

    }

    @Override
    public void close() {

    }
}
