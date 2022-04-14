package com.models

class Group {
    var groupUnique: String? = null
    var groupName: String? = null

    constructor()
    constructor(groupUnique: String?, groupName: String?) {
        this.groupUnique = groupUnique
        this.groupName = groupName
    }

}