package com.Project.LibraryManagementSpringBoot.data



import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class Person(@Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id:Int,
                  val name:String,val age:Int) {
    constructor():this(1,"",2)
}