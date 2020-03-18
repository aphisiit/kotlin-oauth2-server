package com.aphisiit.kotlinoauth2.data

import com.aphisiit.kotlinoauth2.repository.AppUserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

//@Component
class DataInitail : CommandLineRunner{

    @Autowired lateinit var appUserRepository: AppUserRepository

    override fun run(vararg args: String?) {

    }

}