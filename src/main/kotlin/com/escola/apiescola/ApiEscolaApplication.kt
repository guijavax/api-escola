package com.escola.apiescola

import com.escola.apiescola.config.ConfigProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(ConfigProperties::class)
class ApiEscolaApplication

fun main(args: Array<String>) {
	runApplication<ApiEscolaApplication>(*args)
}
