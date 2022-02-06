package com.Project.LibraryManagementSpringBoot.data



import java.io.Serializable
import javax.persistence.*

@Entity
data class Book(@Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id:Int=0,
                var bookName:String,
                var noOfPages:Int,
                var addedOn:String,
                @Lob var pdf: ByteArray,
                var genre: ArrayList<String>,
                var author: String
):Serializable {
    constructor():this(
        0,"",0,"",
        ByteArray(Int.SIZE_BYTES),
        ArrayList() ,
        ""
    )
}