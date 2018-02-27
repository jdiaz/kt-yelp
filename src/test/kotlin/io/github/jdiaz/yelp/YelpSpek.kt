package io.github.jdiaz.yelp

import com.fasterxml.jackson.module.kotlin.*
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.given
import org.amshove.kluent.*
import java.util.concurrent.Executors

data class SearchSuccessAPIResult(
    val total: Long,
    val businesses: List<Map<String, Any>>,
    val region: Map<String, Map<String, String>>
)
data class ErrorAPIResult(val error: Map<String, String>)
data class PhoneSuccessAPIResult(val total: Int, val businesses: List<Map<String, Any>>)

object YelpSpek : Spek({

    val pool = Executors.newCachedThreadPool()
    val yelp = Yelp(System.getProperty("yelpkey"), pool)
    val mapper = jacksonObjectMapper()

    describe("yelp client") {

        val badYelp = Yelp("XXX", pool)
        val tokenInvalid = "TOKEN_INVALID"

        given("an invalid API key") {
            it("should return $tokenInvalid when attempting to fetch data") {
                val jsonStr = badYelp.search(mapOf("term" to "food")).get()
                val json = mapper.readValue<ErrorAPIResult>(jsonStr)
                val errorMap = json.error
                errorMap["code"] shouldEqual tokenInvalid
            }
        }
    }

    describe("search") {

        val lat = "30.307182"
        val long = "-97.755996"
        val params = mapOf("term" to "food", "latitude" to lat, "longitude" to long)

        given("params $params") {

            val jsonStr = yelp.search(params).get()
            val json = mapper.readValue<SearchSuccessAPIResult>(jsonStr)

            it("should return more than one result describing restaurants in Austin, TX") {
                val total = json.total
                 total `should be greater or equal to` 1
            }
        }
    }

    describe("business") {

        val id = "gary-danko-san-francisco"
        val expectedName = "Gary Danko"

        given("a business id=$id") {
            it("should return json data describing that business $id") {
                val jsonStr = yelp.business(id).get()
                val json = mapper.readValue<Map<String, Any>>(jsonStr)
                val name = json["name"]
                name shouldEqual expectedName
            }
        }
    }

    describe("phone") {

        val expectedPhone = "+14159083801"
        val param = mapOf("phone" to expectedPhone)

        given("a phone number $param") {

            val jsonStr = yelp.phone(param).get()
            val json = mapper.readValue<PhoneSuccessAPIResult>(jsonStr)

            it("should find 1 business corresponding with phone number $expectedPhone") {
                val total = json.total
                total `should be greater or equal to` 1
            }

            it("should find have a matching phone number $expectedPhone") {
                val phone = json.businesses[0].get("phone")
                phone shouldEqual expectedPhone
            }
        }
    }

})
