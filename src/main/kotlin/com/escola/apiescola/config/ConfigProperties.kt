package com.escola.apiescola.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties
data class ConfigProperties(
     val fileLocation : String = "",

     val region : String = "",

     val uriBase : String = "",

     val serviceKey : String = ""
)
