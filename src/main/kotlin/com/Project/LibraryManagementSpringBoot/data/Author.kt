package com.Project.LibraryManagementSpringBoot.data

import java.io.Serializable
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class Author(@Id @GeneratedValue(strategy = GenerationType.IDENTITY) var authorId:Int,
                  var authorName:String,
                  var writtenBooks: ArrayList<String>
): Serializable {
    constructor():this(0,"", ArrayList())
}