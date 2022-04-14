package com.models

class GroupMessages {
    var fromUser: String? = null
    var toGroup: String? = null
    var message: String? = null
    var date: String? = null


    constructor(fromUser: String?, toGroup: String?, message: String?, date: String?) {
        this.fromUser = fromUser
        this.toGroup = toGroup
        this.message = message
        this.date = date
    }

    constructor()
}