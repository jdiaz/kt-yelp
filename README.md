# kt-yelp

A minimalistic [yelp fushion API](https://www.yelp.com/developers/documentation/v3) async client written in kotlin.

## Usage

kt-yelp supports async HTTP GET requests by employing the [kotlin-futures](https://github.com/vjames19/kotlin-futures) library, resulting in a clean API.

```kotlin
 import io.github.jdiaz.yelp
 
 fun main(args: Array<String>) {
    // You can provide an instance of ExecutorService of your choosing.
    // If null is provided, kotlin futures defaults to ForkJoinPool.
    val yelp = Yelp("<YOUR_API_KEY>", null)
    val searchFuture = yelp.search(mapOf("term" to "food"))
    // By virtue of kotlin-futures
    searchFuture.onComplete({
        onFailure = { throwable -> 
            // handle error
        },
        onSuccess = { json -> {
            // handle success
        }
    })
 }
```
