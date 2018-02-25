package io.github.jdiaz.yelp

import io.github.vjames19.futures.jdk8.onFailure
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.given
import org.amshove.kluent.*

data class ApiResult(val total: Int, val businesses: List<Map<String, String>>, val region: Map<String, String>)

object YelpSpek : Spek({

    val yelp = Yelp(System.getProperty("yelpkey"), null)

    describe("yelp client") {
        val badYelp = Yelp("XXX", null)
        given("an invalid API key") {
            it("should throw when attempting to fetch data") {
                { badYelp.search(mapOf("term" to "food")).get() } shouldThrow AnyException
            }
        }

        given("a valid API key") {
            it("should successfully fetch data json data") {
                val jsonStr = yelp.search(mapOf("term" to "food"))
            }
        }
    }

    describe("search") {
        val params = mapOf("term" to "food", "latitude" to "30.307182", "longitude" to "-97.755996")
        given("params $params") {
            it("should return json data describing restaurants in Austin, TX") {
                 "hello" shouldEqual "hello"
            }
        }
    }

    describe("business") {
        val id = "something"
        given("a business id=$id") {
            it("should return json data describing that business") {
            }
        }
    }

    describe("phone") {
        val param = mapOf("phone" to "123-567-8888")
        given("a phone number $param") {
            it("should find corresponding business") {
            }
        }
    }

})
