package com.Project.LibraryManagementSpringBoot.dataSource

import com.Project.LibraryManagementSpringBoot.data.Author
import com.Project.LibraryManagementSpringBoot.data.Book
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

//interface dataSource {
//}


@Repository
interface BookDataSource: JpaRepository<Book, Int> {

    @Transactional
    @Modifying
    @Query("Delete from Book b where b.bookName = :bookName")
    fun deleteByname(bookName:String):Int




    @Query("from Book b where b.bookName= :bName and b.author = :authorName")
    fun findByBookAndAuthor(bName: String, authorName: String,pageable: Pageable): Page<Book>

    @Query("from Book b where b.author= :authorName")
    abstract fun findByAuthor(authorName: String,pageable: Pageable): Page<Book>

    @Query("from Book b where b.bookName= :bName")
    abstract fun findByBookName(bName: String,pageable: Pageable): Page<Book>

    @Query("from Book b where b.author = :authName")
    fun getAllWrittenBooksByAuthor(authName: String,pageable: Pageable): Page<Book>

    //Pagination


}

@Repository
interface AuthorDataSource: JpaRepository<Author, Int> {

    @Query("select a.authorName from Author a")
    abstract fun getAuthorList(): List<String>


}