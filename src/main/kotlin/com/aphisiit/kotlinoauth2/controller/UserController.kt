package com.aphisiit.kotlinoauth2.controller

import com.aphisiit.kotlinoauth2.domain.AppUser
import com.aphisiit.kotlinoauth2.service.AppUserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices
import org.springframework.security.oauth2.provider.token.TokenStore
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.annotation.Resource


@RestController
class UserController {

    @Autowired lateinit var appUserService: AppUserService

    @Resource(name = "tokenStore")
    @Autowired lateinit var tokenStore: TokenStore

    @Resource(name = "tokenServices")
    @Autowired lateinit var tokenServices: ConsumerTokenServices

    @GetMapping("/user")
    fun findMe(@RequestHeader requestHeader: HttpHeaders) : List<AppUser> {
        return appUserService.findAllUsers()
    }

    @RequestMapping(method = [RequestMethod.GET], value = ["/tokens"])
    @ResponseBody
    fun getTokens(): List<String>? {
        val tokenValues: MutableList<String> = ArrayList()
        val tokens = tokenStore!!.findTokensByClientId("sampleClientId")
        if (tokens != null) {
            for (token in tokens) {
                tokenValues.add(token.value)
            }
        }
        return tokenValues
    }

    @RequestMapping(method = [RequestMethod.POST], value = ["/tokens/revoke/{tokenId:.*}"])
    @ResponseBody
    fun revokeToken(@PathVariable tokenId: String?): String? {
        tokenServices.revokeToken(tokenId)
        return tokenId
    }

    @RequestMapping(method = [RequestMethod.POST], value = ["/tokens/revokeRefreshToken/{tokenId:.*}"])
    @ResponseBody
    fun revokeRefreshToken(@PathVariable tokenId: String?): String? {
        if (tokenStore is JdbcTokenStore) {
            (tokenStore as JdbcTokenStore).removeRefreshToken(tokenId)
        }
        return tokenId
    }
}