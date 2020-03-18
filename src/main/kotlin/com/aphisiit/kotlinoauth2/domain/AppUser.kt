package com.aphisiit.kotlinoauth2.domain

import javax.persistence.*

@Entity
@Table(name = "app_user")
data class AppUser (
        @Id @GeneratedValue(strategy = GenerationType.TABLE) var id: Long? = null,
        var username: String,
        var password: String,
        var firstName: String,
        var lastName: String,
//        var age: Int? = null,
        var active: String,

        /**
         * Roles are being eagerly loaded here because
         * they are a fairly small collection of items for this example.
         */
        @ManyToMany(fetch = FetchType.EAGER)
        @JoinTable(name = "user_role", joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")], inverseJoinColumns = [JoinColumn(name = "role_id", referencedColumnName = "id")])
        var roles: List<AppRole>? = null

) {
}