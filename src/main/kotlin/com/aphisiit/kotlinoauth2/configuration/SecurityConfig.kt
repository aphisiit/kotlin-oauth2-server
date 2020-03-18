package com.aphisiit.kotlinoauth2.configuration

import com.aphisiit.kotlinoauth2.service.AppUserServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder


@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
class SecurityConfig : WebSecurityConfigurerAdapter() {


    @Autowired lateinit var userDetailsService: AppUserServiceImpl

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http.csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeRequests().antMatchers(HttpMethod.GET, "/user").hasRole("ADMIN")
                .and().authorizeRequests().antMatchers("/oauth/token")
                .permitAll().anyRequest().authenticated()
//                .and()

    }

    @Bean
    fun authenticationProvider(): DaoAuthenticationProvider {
        val provider = DaoAuthenticationProvider()
        provider.setPasswordEncoder(bCryptPasswordEncoder())
        provider.setUserDetailsService(userDetailsService)
        return provider
    }

    //	@Autowired
    //    public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
    //        auth.inMemoryAuthentication()
    //        .withUser("bill").password("abc123").roles("ADMIN").and()
    //        .withUser("bob").password("abc123").roles("USER");
    //    }
    @Bean
    fun bCryptPasswordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    @Throws(Exception::class)
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }

    @Autowired
    @Throws(Exception::class)
    fun configureGlobal(auth: AuthenticationManagerBuilder) {
        auth.authenticationProvider(authenticationProvider())
    }
}