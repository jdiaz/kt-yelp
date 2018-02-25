# kt-yelp

An async [yelp fushion API](https://www.yelp.com/developers/documentation/v3) client library written in kotlin.

## Usage

kt-yelp supports async HTTP GET requests by employing the [kotlin-futures](https://github.com/vjames19/kotlin-futures) library, resulting in a clean API.

```kotlin
 import io.github.jdiaz.yelp.Yelp
 
 fun main(args: Array<String>) {
    // You can provide an instance of ExecutorService of your choosing.
    // Alternatively you can pass null instead i.e Yelp(key, null)
    // kotlin-futures lib will default to ForkJoinPool.commonPool(),
    // consequently kt-yelp will do to.
    val executor = Executor.getCachedExecutor
    val yelp = Yelp("<YOUR_API_KEY>", executor)
    val params = mapOf("term" to "food", "latitude" to "30.307182", "longitude" to "-97.755996")
    // By virtue of kotlin-futures
    val searchFuture = yelp.search(params).onComplete(
            onFailure = { throwable ->
                // handle error
                println("Darn... We should log this!")
            },
            onSuccess = { json ->
                // handle success
                println("Restaurant data:\n$json")
            }
    ).handle { data, error -> data ?: "Woops, there was an error:\n\t$error"}

    println(searchFuture.get())
 }
```

Tests are require the -Dyelpkey jvm property to run.
 `gradle -Dyelpkey=<YOUR_API_KEY test`