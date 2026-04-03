package org.keycloak.mapper;

import org.keycloak.models.*;
import org.keycloak.protocol.oidc.mappers.*;
import org.keycloak.representations.IDToken;
import org.keycloak.provider.ProviderConfigProperty;

import java.util.*;

public class ClientIpMapper extends AbstractOIDCProtocolMapper
        implements OIDCAccessTokenMapper, OIDCIDTokenMapper, UserInfoTokenMapper {

    public static final String PROVIDER_ID = "client-ip-mapper";

    private static final List<ProviderConfigProperty> configProperties = new ArrayList<>();

    static {
        OIDCAttributeMapperHelper.addTokenClaimNameConfig(configProperties);
        OIDCAttributeMapperHelper.addIncludeInTokensConfig(configProperties, ClientIpMapper.class);
    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }

    @Override
    public String getDisplayType() {
        return "Client IP Mapper";
    }

    @Override
    public String getDisplayCategory() {
        return "Token Mapper";
    }

    @Override
    public String getHelpText() {
        return "Adds client IP address to token";
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return configProperties;
    }

    @Override
    protected void setClaim(IDToken token, ProtocolMapperModel mappingModel,
                            UserSessionModel userSession, KeycloakSession session,
                            ClientSessionContext clientSessionCtx) {

        String ip = userSession.getIpAddress();
        OIDCAttributeMapperHelper.mapClaim(token, mappingModel, ip);
    }
}


