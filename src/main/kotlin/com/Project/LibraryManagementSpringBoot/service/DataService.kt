package com.Project.LibraryManagementSpringBoot.service

import com.Project.LibraryManagementSpringBoot.data.Author
import com.Project.LibraryManagementSpringBoot.data.Book
import com.Project.LibraryManagementSpringBoot.dataSource.AuthorDataSource
import com.Project.LibraryManagementSpringBoot.dataSource.BookDataSource
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service


@Service
@Repository
class Dataservice(val authorDataSource: AuthorDataSource, val bookDataSource: BookDataSource){
    var logger = LoggerFactory.getLogger(this.javaClass)


    fun isAuthorPresent(name:String):Boolean{
        return authorDataSource.findAll().any { it.authorName == name }
    }

    fun isBookPresent(bName:String):Boolean{
        try {
            return bookDataSource.findAll().any{it.bookName==bName}
        }
        catch (e:Exception){
            throw Exception("Database found error while finding/n $e")
        }

    }

    fun getAuthor(AuthName:String): Author?{
        for(author in authorDataSource.findAll()){
            if(author.authorName==AuthName){
                return author
            }
        }
        return null
    }

    fun addAuthor(authorName:String,bookName: String):Boolean{
        var bookList= ArrayList<String>()

        try {
            if (isAuthorPresent(authorName) == true) {
                var authorObj: Author = Author()
                for (author in authorDataSource.findAll()) {
                    if (author.authorName == authorName) {
                        bookList = author.writtenBooks
                        authorObj = author
                        break
                    }
                }
                authorDataSource.delete(authorObj)
            }

            bookList.add(bookName)
            authorDataSource.save(Author(0, authorName, bookList))
            return true
        }
        catch (e:Exception){
            throw Exception(e)
        }
    }

    fun addBook(book: Book): ResponseEntity<Map<String, Book>> {
        var message:String=""
        var response:MutableMap<String,Book> =  mutableMapOf()
        if(isBookPresent(book.bookName)) {
            message="Book with ${book.bookName} already present"
            return ResponseEntity(mutableMapOf(Pair(message,book)), HttpStatus.ALREADY_REPORTED)
        }

        addAuthor(book.author,book.bookName)
        bookDataSource.save(book)

        message="Book with ${book.bookName} added successfully"
        return ResponseEntity(mutableMapOf(Pair(message,book)), HttpStatus.ACCEPTED)
    }


    fun showAllBooks(pageable: Pageable): Page<Book> {
        return bookDataSource.findAll(pageable)
    }

    @Cacheable("Books")
    fun searchBook(bName:String?,author:String?,pageable: Pageable): Page<Book> {
        logger.info("Fetching from database...")

        try {
            if (bName != null && author != null) {
                return bookDataSource.findByBookAndAuthor(bName, author, pageable)
            } else if (bName != null) {
                println("Book name is not NULL!!")
                return bookDataSource.findByBookName(bName, pageable)
            } else if (author != null) {
                return bookDataSource.findByAuthor(author, pageable)
            } else {
                return showAllBooks(pageable)
            }
        }
        catch (e:Exception){
            throw Exception(e)
        }
    }

    fun getAllAuthors():List<String>{
        try {
            return authorDataSource.getAuthorList()
        }
        catch (e:Exception){
            throw Exception(e)
        }
    }

    fun deleteAuthor(author:Author,bookName: String):Unit{
        //delete author -> delete old author, add updated one
        authorDataSource.delete(author)

        if(author.writtenBooks.size>1){
            author.writtenBooks.remove(bookName)
            authorDataSource.save(author)
        }

    }

    @CacheEvict("Books", allEntries = true)
    fun deleteBook(bookName:String, bookAuthor:String): ResponseEntity<String> {
        val author:Author?= getAuthor(bookAuthor)
        if(!isBookPresent(bookName) || author==null){
            return ResponseEntity("$bookName not present!", HttpStatus.NOT_FOUND)
        }
        //delete book
        bookDataSource.deleteByname(bookName)

        //delete Author
        deleteAuthor(author,bookName)
        return ResponseEntity("$bookName delete Successful", HttpStatus.OK)
    }

    @Cacheable("Books")
    fun getWrittenBooksByAuthor(authName: String,pageable: Pageable): Page<Book> {
        logger.info("Fetching Books from database!!")
        return bookDataSource.getAllWrittenBooksByAuthor(authName,pageable)
    }

    @Cacheable("Books")
    fun filterBygenreAndAuthor(category:String?,authorName:String?):List<Book>{
        logger.info("Fetching data for 1st time by DataBase...")
        try {
            if (category != null && authorName != null) {
                return bookDataSource.findAll().filter { it.genre.contains(category) && it.author == authorName }
            } else if (category != null) {
                return bookDataSource.findAll().filter { it.genre.contains(category) }
            } else if (authorName != null) {
                return bookDataSource.findAll().filter { it.author == authorName }
            } else {
                return bookDataSource.findAll()
            }
        }
        catch (e:Exception){
            throw Exception(e)
        }
    }

}