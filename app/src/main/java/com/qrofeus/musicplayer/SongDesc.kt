package com.qrofeus.musicplayer

import java.sql.Blob

class SongDesc{
    var path: String? = null
    var title: String? = null
    var artist: String? = null

    constructor(path: String?, title: String?, artist: String?) {
        this.path = path
        this.title = title
        this.artist = artist
    }
}