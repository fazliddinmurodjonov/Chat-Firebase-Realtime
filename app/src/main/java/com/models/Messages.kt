package com.models

class Messages {
    var fromUser: String? = null
    var toUser: String? = null
    var message: String? = null
    var date: String? = null

    constructor(fromUser: String?, toUser: String?, message: String?, date: String?) {
        this.fromUser = fromUser
        this.toUser = toUser
        this.message = message
        this.date = date
    }

    constructor()

}