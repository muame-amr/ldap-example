package com.example.ldapexample;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.ContextSource;
import org.springframework.ldap.core.support.BaseLdapPathContextSource;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.security.ldap.authentication.BindAuthenticator;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.security.ldap.authentication.LdapAuthenticator;
import org.springframework.security.ldap.server.UnboundIdContainer;
import org.springframework.security.ldap.userdetails.PersonContextMapper;

@Configuration
public class SecurityConfig {
    @Bean
    UnboundIdContainer unboundIdContainer() {
        UnboundIdContainer container = new UnboundIdContainer("dc=springframework,dc=org", "classpath:users.ldif");
        container.setPort(0);
        return container;
    }

    @Bean
    ContextSource contextSource(UnboundIdContainer unboundIdContainer) {
        int port = unboundIdContainer.getPort();
        return new DefaultSpringSecurityContextSource("ldap://localhost:" + port + "/dc=springframework,dc=org");
    }

    @Bean
    BindAuthenticator bindAuthenticator(BaseLdapPathContextSource contextSource) {
        BindAuthenticator bindAuthenticator = new BindAuthenticator(contextSource);
        bindAuthenticator.setUserDnPatterns(new String[]{"uid={0},ou=people"});
        return bindAuthenticator;
    }

    @Bean
    LdapAuthenticationProvider authenticationProvider(LdapAuthenticator ldapAuthenticator) {
        LdapAuthenticationProvider ldapAuthenticationProvider = new LdapAuthenticationProvider(ldapAuthenticator);
        ldapAuthenticationProvider.setUserDetailsContextMapper(new PersonContextMapper());
        return ldapAuthenticationProvider;
    }
}
