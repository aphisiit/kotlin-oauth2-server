package com.aphisiit.kotlinoauth2.domain

import javax.persistence.*


@Entity
@Table(name = "app_role")
data class AppRole (
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    var id: Long? = null,

    @Column(name = "role_name")
    var roleName: String? = null,

    @Column(name = "description")
    var description: String? = null
)