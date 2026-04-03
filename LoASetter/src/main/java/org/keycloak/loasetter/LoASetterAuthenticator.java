package org.keycloak.loasetter;

import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.Authenticator;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.sessions.AuthenticationSessionModel;
import org.keycloak.models.Constants;

public class LoASetterAuthenticator implements Authenticator {

    public static final String CONFIG_LOA_LEVEL = "loa.level";

    @Override
    public void authenticate(AuthenticationFlowContext context) {

        int level = 1;

        if (context.getAuthenticatorConfig() != null) {
            String levelStr = context.getAuthenticatorConfig().getConfig().get(CONFIG_LOA_LEVEL);
            if (levelStr != null) {
                level = Integer.parseInt(levelStr);
            }
        }

        AuthenticationSessionModel authSession = context.getAuthenticationSession();

        // 设置 LoA
        authSession.setAuthNote(Constants.LEVEL_OF_AUTHENTICATION, String.valueOf(level));

        context.success();
    }

    @Override
    public void action(AuthenticationFlowContext context) {
    }

    @Override
    public boolean requiresUser() {
        return false;
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
