package com.aphisiit.kotlinoauth2.configuration

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer
import org.springframework.security.oauth2.provider.token.DefaultTokenServices
import org.springframework.security.oauth2.provider.token.TokenStore
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter
import javax.sql.DataSource


@Configuration
@EnableAuthorizationServer
class OAuthConfiguration : AuthorizationServerConfigurerAdapter() {

    @Autowired
    @Qualifier("authenticationManagerBean")
    private lateinit var authenticationManager: AuthenticationManager

    @Autowired lateinit var userDetailsService: UserDetailsService

    @Autowired
    @Qualifier("bCryptPasswordEncoder") lateinit var passwordEncoder: PasswordEncoder

    @Autowired lateinit var dataSource: DataSource

    @Throws(Exception::class)
    override fun configure(oauthServer: AuthorizationServerSecurityConfigurer) {
        oauthServer.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()")
    }

    @Throws(Exception::class)
    override fun configure(clients: ClientDetailsServiceConfigurer) {
        clients.inMemory()
                .withClient("fooClientId").secret(passwordEncoder.encode("secret"))
                .authorizedGrantTypes("password", "authorization_code", "refresh_token").scopes("read", "write")
                .authorities("ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "USER", "ADMIN")
                .autoApprove(true)
                .accessTokenValiditySeconds(180) //Access token is only valid for 3 minutes.
                .refreshTokenValiditySeconds(600) //Refresh token is only valid for 10 minutes.;
    }

    @Throws(Exception::class)
    override fun configure(endpoints: AuthorizationServerEndpointsConfigurer) {
        endpoints.tokenStore(tokenStore()).authenticationManager(authenticationManager).accessTokenConverter(defaultAccessTokenConverter())
                .userDetailsService(userDetailsService)
    }

    // Todo : for in memory database (H2)
//    @Bean
//    fun tokenStore(): TokenStore? {
//        return JwtTokenStore(defaultAccessTokenConverter())
//    }

    @Bean
    fun tokenStore(): TokenStore? {
        return JdbcTokenStore(dataSource)
    }

    // Todo : Use for now config database in application.properties file
//    @Bean
//    fun dataSource(): DataSource? {
//        val dataSourceBuilder = DataSourceBuilder.create()
//        dataSourceBuilder.driverClassName("org.postgresql.Driver")
//        dataSourceBuilder.url("jdbc:postgresql://localhost:5432/postgres")
//        dataSourceBuilder.username("postgres")
//        dataSourceBuilder.password("password")
//        return dataSourceBuilder.build()
//    }

    @Bean
    fun defaultAccessTokenConverter(): JwtAccessTokenConverter? {
        val converter = JwtAccessTokenConverter()
        converter.setSigningKey("123")
        return converter
    }

    @Bean
    @Primary
    fun tokenServices(): DefaultTokenServices? {
        val defaultTokenServices = DefaultTokenServices()
        defaultTokenServices.setTokenStore(tokenStore())
        defaultTokenServices.setSupportRefreshToken(true)
        return defaultTokenServices
    }


}