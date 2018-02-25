package io.github.jdiaz.yelp

import io.github.vjames19.futures.jdk8.Future
import java.util.concurrent.CompletableFuture
import khttp.get
import java.util.concurrent.Executor

class Yelp(apiKey: String, val pool: Executor?) {

    val API_HOST = "https://api.yelp.com"
    val API_VERSION = "/v3"
    val BASE_URL = "$API_HOST$API_VERSION"

    val BUSINESS_PATH = "/businesses"
    val SEARCH_PATH = "/search"
    val PHONE_PATH = "/phone"

    val headers = mapOf("Authorization" to "Bearer $apiKey")

    fun search(params: Map<String, String>): CompletableFuture<String> {
        val queryParams = queryParams(params)
        val url = "$BASE_URL$BUSINESS_PATH$SEARCH_PATH?$queryParams"
        return request(url, headers, pool)
    }

    fun business(id: String): CompletableFuture<String> {
        val url = "$BASE_URL$BUSINESS_PATH/$id"
        return request(url, headers, pool)
    }

    fun phone(params: Map<String, String>): CompletableFuture<String> {
        val queryParams = queryParams(params)
        val url = "$BASE_URL$BUSINESS_PATH$SEARCH_PATH$PHONE_PATH?$queryParams"
        return request(url, headers, pool)
    }

    private fun request(url: String, headers: Map<String, String>, pool: Executor?): CompletableFuture<String> {
        return if(pool == null) {
            Future {
                get(url, headers).jsonObject.toString()
            }
        } else {
            Future(pool) {
                get(url, headers).jsonObject.toString()
            }
        }
    }

    private fun queryParams(params: Map<String, String>): String {
        val queryParams = StringBuilder()
        var numberOfParams = params.size
        for ((key, value) in params) {
            queryParams.append("$key=$value")
            if (numberOfParams != 1) queryParams.append("&")
            numberOfParams--
        }
        return queryParams.toString()
    }
}



