package com.escola.apiescola.service

import com.amazonaws.http.HttpResponse
import com.amazonaws.util.IOUtils

class ApiGatewayResponse(response : HttpResponse) {

    var body : String? = ""
    init {
       body =  if(response.content != null) {
            IOUtils.toString(response.content)
        } else {
            null
       }
    }
}