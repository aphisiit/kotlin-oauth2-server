package com.aphisiit.kotlinoauth2.repository

import com.aphisiit.kotlinoauth2.domain.AppUser
import org.springframework.data.jpa.repository.JpaRepository

interface AppUserRepository : JpaRepository<AppUser,Long> {
    fun findByUsername(userName: String) : AppUser
    fun findByUsernameAndActive(userName: String, active: String) : AppUser?
}