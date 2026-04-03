package org.keycloak.authenticator.higherlevel;

import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.Authenticator;
import org.keycloak.sessions.AuthenticationSessionModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.jboss.logging.Logger;

public class HigherLevelAnalyser implements Authenticator {

    private static final Logger logger = Logger.getLogger(HigherLevelAnalyser.class);

    @Override
    public void authenticate(AuthenticationFlowContext context) {
        UserModel user = context.getUser();
        
        if (user == null) {
            logger.warn("No user found in authentication context");
            context.success();
            return;
        }

        String authLevelStr = user.getFirstAttribute(Constants.AUTH_LEVEL_ATTRIBUTE);
        int authLevel = 0;
        if (authLevelStr != null && !authLevelStr.isEmpty()) {
            try {
                authLevel = Integer.parseInt(authLevelStr);
            } catch (NumberFormatException e) {
                logger.warnf("Invalid authLevel value for user %s: %s", user.getUsername(), authLevelStr);
            }
        }

        AuthenticationSessionModel authSession = context.getAuthenticationSession();
        String loaStr = authSession.getAuthNote(Constants.LEVEL_OF_AUTHENTICATION);
        int loa = 0;
        if (loaStr != null && !loaStr.isEmpty()) {
            try {
                loa = Integer.parseInt(loaStr);
            } catch (NumberFormatException e) {
                logger.warnf("Invalid LoA value in auth session: %s", loaStr);
            }
        }

        int higherLevel = Math.max(authLevel, loa);
        
        // 存储到UserSession中
        authSession.setUserSessionNote(Constants.FINAL_AUTH_LEVEL, String.valueOf(higherLevel));
        
        logger.infof("User %s: authLevel=%d, LoA=%d, finalLevel=%d", 
            user.getUsername(), authLevel, loa, higherLevel);

        context.success();
    }

    @Override
    public void action(AuthenticationFlowContext context) {
        context.success();
    }

    @Override
    public boolean requiresUser() {
        return true;
    }

    @Override
    public boolean configuredFor(KeycloakSession session, RealmModel realm, UserModel user) {
        return true;
    }

    @Override
    public void setRequiredActions(KeycloakSession session, RealmModel realm, UserModel user) {
    }

    @Override
    public void close() {
    }
}
