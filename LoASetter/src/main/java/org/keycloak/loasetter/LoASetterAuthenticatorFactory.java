package org.keycloak.loasetter;

import org.keycloak.authentication.Authenticator;
import org.keycloak.authentication.AuthenticatorFactory;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.models.AuthenticationExecutionModel;

import java.util.ArrayList;
import java.util.List;

public class LoASetterAuthenticatorFactory implements AuthenticatorFactory {

    public static final String PROVIDER_ID = "loa-setter-authenticator";

    private static final List<ProviderConfigProperty> configProperties = new ArrayList<>();

    static {

        ProviderConfigProperty loaLevel = new ProviderConfigProperty();
        loaLevel.setName("loa.level");
        loaLevel.setLabel("LoA Level");
        loaLevel.setType(ProviderConfigProperty.INTEGER_TYPE);
        loaLevel.setHelpText("Set Level of Authentication (LoA) for this session");
        loaLevel.setDefaultValue(1);

        configProperties.add(loaLevel);
    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }

    @Override
    public String getDisplayType() {
        return "Set Authentication LoA";
    }

    @Override
    public String getReferenceCategory() {
        return "Set Authentication LoA";
    }

    @Override
    public String getHelpText() {
        return "Set Level of Authentication (LoA) for current authentication session";
    }

    @Override
    public Authenticator create(KeycloakSession session) {
        return new LoASetterAuthenticator();
    }

    @Override
    public void init(org.keycloak.Config.Scope config) {
    }

    @Override
    public void postInit(KeycloakSessionFactory factory) {
    }

    @Override
    public void close() {
    }

    @Override
    public boolean isConfigurable() {
        return true;
    }

    @Override
    public AuthenticationExecutionModel.Requirement[] getRequirementChoices() {
        return new AuthenticationExecutionModel.Requirement[] {
                AuthenticationExecutionModel.Requirement.REQUIRED,
                AuthenticationExecutionModel.Requirement.ALTERNATIVE,
                AuthenticationExecutionModel.Requirement.DISABLED
        };
    }

    @Override
    public boolean isUserSetupAllowed() {
        return false;
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return configProperties;
    }
}
