package com.models

class User {
    var email: String? = null
    var displayName: String? = null
    var photoUrl: String? = null
    var uid: String? = null
    var userState: String? = null
    var lastTime: String? = null
    var lastMessage: String? = null

    constructor(
        email: String?,
        displayName: String?,
        photoUrl: String?,
        uid: String?,
        userState: String?,
        lastTime: String?,
        lastMessage: String?,
    ) {
        this.email = email
        this.displayName = displayName
        this.photoUrl = photoUrl
        this.uid = uid
        this.userState = userState
        this.lastTime = lastTime
        this.lastMessage = lastMessage
    }

    constructor()
}