package com.escola.apiescola.service

import com.amazonaws.http.HttpMethodName
import com.escola.apiescola.config.JsonApiGatewayCaller
import com.escola.apiescola.entitie.AlunoEntity
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service
import java.io.ByteArrayInputStream

@Service
class EscolaService(val apiCaller : JsonApiGatewayCaller) {

    fun create(aluno : AlunoEntity) : String{
        val ow = ObjectMapper().writer().withDefaultPrettyPrinter()
        val json = ow.writeValueAsString(aluno)
        val response = apiCaller.execute(HttpMethodName.POST, "/api", ByteArrayInputStream(json.toByteArray()) )
        println(response.body)
        return response.body!!
    }

}