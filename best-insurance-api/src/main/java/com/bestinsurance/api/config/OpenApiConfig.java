package com.bestinsurance.api.config;

import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.OAuthScope;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@Configuration
@SecurityScheme(
        name = "bestInsurance",
        type = SecuritySchemeType.OAUTH2,
        flows = @OAuthFlows(
                authorizationCode = @OAuthFlow(
                        authorizationUrl = "${springdoc.oauthflow.authorizationurl}",
                        tokenUrl = "${springdoc.oauthflow.tokenurl}",
                        scopes = {
                                @OAuthScope(name = "openid", description = "OpenID scope"),
                                @OAuthScope(name = "profile", description = "User profile info")
                        }
                )
        )
)
public class OpenApiConfig {
}
