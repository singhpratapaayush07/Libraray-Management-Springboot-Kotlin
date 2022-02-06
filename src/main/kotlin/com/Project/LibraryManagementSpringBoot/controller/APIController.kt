package com.Project.LibraryManagementSpringBoot.controller

import com.Project.LibraryManagementSpringBoot.data.Book
import com.Project.LibraryManagementSpringBoot.service.Dataservice
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile


@Controller

@RestController
@RequestMapping("/api")
class APIController (val dataservice: Dataservice){

    @ExceptionHandler(NoSuchElementException::class)
    fun handleNotFoundError(e:NoSuchElementException): ResponseEntity<String> =
        ResponseEntity(e.message, HttpStatus.NOT_FOUND)

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleNotFoundError(e:IllegalArgumentException): ResponseEntity<String> =
        ResponseEntity(e.message, HttpStatus.BAD_REQUEST)

    @ExceptionHandler(Exception::class)
    fun allExceptions(e:Exception): ResponseEntity<String> =
        ResponseEntity(e.message, HttpStatus.I_AM_A_TEAPOT)

    @RequestMapping(method = [RequestMethod.POST], path = ["/books/add"], consumes = ["multipart/form-data"])
    @ResponseBody
    fun addBook(@RequestParam bookName:String, @RequestParam noOfPages:Int, @RequestParam addedOn:String,
                @RequestParam file: MultipartFile, @RequestParam genre:ArrayList<String>, @RequestParam author:String): ResponseEntity<Map<String, Book>> {
        return dataservice.addBook(Book(0,bookName,noOfPages,addedOn,file.bytes,genre,author))
    }

    @GetMapping("/books")
    fun showAll(pageable: Pageable): Page<Book> {
        return dataservice.showAllBooks(pageable)
    }

    @GetMapping("/books/filter")
    @ResponseBody
    fun filters(@RequestParam bName:String?, @RequestParam authorName:String?, pageable: Pageable): Page<Book> {
        return dataservice.searchBook(bName,authorName,pageable)
    }

    @GetMapping("/books/filters")
    @ResponseBody
    fun filterBygenreAndAuthor(@RequestParam category:String?, @RequestParam authorName:String?): List<Book> {
        return dataservice.filterBygenreAndAuthor(category,authorName)
    }



    @GetMapping("/authors")
    fun getAllAuthorsList():List<String>{
        return dataservice.getAllAuthors()
    }

    @GetMapping("/books/authors")
    @ResponseBody
    fun getWrittenBooksByAuthor(@RequestParam authName:String, pageable: Pageable): Page<Book> {
        return dataservice.getWrittenBooksByAuthor(authName,pageable)
    }

    @DeleteMapping("/books/delete")
    @ResponseBody
    fun deleteBook(@RequestParam BookName:String, @RequestParam AuthorName:String): ResponseEntity<String> {
        return dataservice.deleteBook(BookName,AuthorName)
    }

}