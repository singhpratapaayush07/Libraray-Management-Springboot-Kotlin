package com.Project.LibraryManagementSpringBoot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.ComponentScan
import springfox.documentation.swagger2.annotations.EnableSwagger2

@SpringBootApplication
@EnableCaching
@EnableSwagger2
class LibraryManagementSpringBootApplication

fun main(args: Array<String>) {
	runApplication<LibraryManagementSpringBootApplication>(*args)
}
