package com.aphisiit.kotlinoauth2.controller

import com.aphisiit.kotlinoauth2.domain.AppUser
import com.aphisiit.kotlinoauth2.service.AppUserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController {

    @Autowired lateinit var appUserService: AppUserService

    @GetMapping("/user")
    fun findMe(@RequestHeader requestHeader: HttpHeaders) : List<AppUser> {
        return appUserService.findAllUsers()
    }
}