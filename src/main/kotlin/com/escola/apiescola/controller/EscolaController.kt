package com.escola.apiescola.controller

import com.amazonaws.http.HttpMethodName
import com.escola.apiescola.config.JsonApiGatewayCaller
import com.escola.apiescola.entitie.AlunoEntity
import com.escola.apiescola.service.EscolaService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class EscolaController(val escolaService : EscolaService) {

    @PostMapping("/save")
    fun save(@RequestParam("id_aluno") idAluno : Long, @RequestParam nome : String) : ResponseEntity<String> {

        val response = escolaService.create(AlunoEntity(idAluno, nome))
        return ResponseEntity.ok(response)
    }
}