package com.example.dailyplanner.modules

class UserModel (
    val id: String = "",
    var name: String = "",
    var email: String = "",
    var image_profile_url: String = "empty"
    ) {
        override fun equals(other: Any?): Boolean {
            return (other as CommonModel).id == id
        }
}