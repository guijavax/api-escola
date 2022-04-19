package com.escola.apiescola.entitie

import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonValue


data class AlunoEntity(
    @field:JsonProperty(value = "id_aluno")
    val idAluno : Long,
    val nome : String
) {
}