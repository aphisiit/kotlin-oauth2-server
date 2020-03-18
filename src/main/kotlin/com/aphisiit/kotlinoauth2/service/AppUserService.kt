package com.aphisiit.kotlinoauth2.service

import com.aphisiit.kotlinoauth2.domain.AppUser
import com.aphisiit.kotlinoauth2.repository.AppUserRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

interface AppUserService {
    fun findAllUsers() : List<AppUser>
    fun findByUsername(username: String) : AppUser?
}

@Service
class AppUserServiceImpl : AppUserService,UserDetailsService {

    @Autowired lateinit var appUserRepository: AppUserRepository

    val logger = LoggerFactory.getLogger(this.javaClass)

    override fun loadUserByUsername(username: String): UserDetails {

        logger.info("load data for user : $username")

        val appUser : AppUser? = appUserRepository.findByUsernameAndActive(username,"Y")
                ?: throw UsernameNotFoundException(String.format("Username : $username is not found !!!"))

        if (null != appUser) {
            logger.info("found username : ${appUser.username}, flag active : ${appUser.active}")

            val authorities = ArrayList<GrantedAuthority>()
            appUser.roles?.forEach { role ->
                authorities.add(SimpleGrantedAuthority(role.roleName))
            }
            logger.info("$username Password authentication successfully")
            return User(appUser.username,appUser.password,authorities)
        }else {
            logger.error("$username does not exits")
            throw BadCredentialsException("$username does not exits")
        }
    }

    override fun findByUsername(username: String): AppUser? {
        return appUserRepository.findByUsername(username)
    }

    override fun findAllUsers(): List<AppUser> {
        return appUserRepository.findAll()
    }
}