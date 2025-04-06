package com.example.todoappusingktor.data.client

import com.example.todoappusingktor.data.model.Todo
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.URLProtocol
import io.ktor.http.headers
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class ktorclient {
    val httpclient = HttpClient {
        install(ContentNegotiation) {
            json(json = Json {
                ignoreUnknownKeys = true
            })
        }
        install(HttpTimeout) {
            socketTimeoutMillis = 3000
            connectTimeoutMillis = 3000
            requestTimeoutMillis = 3000
        }
        install(DefaultRequest) {
            url {
                host = "10.0.2.2:8080"
                protocol = URLProtocol.HTTP
                headers {
                    append(HttpHeaders.ContentType, "application/json")
                }
            }
        }
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
    }

    suspend fun getalltodos(): List<Todo> {
        return httpclient.get("/todos").body<List<Todo>>()
    }
    suspend fun deletetodo(id:Int) : Boolean{
        return httpclient.delete("/todos/$id").status == HttpStatusCode.OK

    }

    suspend fun addtodo(todo: Todo) : Boolean{
        return httpclient.post("/todos"){
            setBody(todo)
        }.status == HttpStatusCode.OK
    }
    suspend fun updatetodo(id: Int,todo: Todo) : Boolean{
        return httpclient.put("todos/$id"){
            setBody(todo)
        }.status == HttpStatusCode.OK
    }
}