[![Build Status](https://travis-ci.org/jdiaz/kt-yelp.svg?branch=master)](https://travis-ci.org/jdiaz/kt-yelp) [![Release](https://jitpack.io/v/jdiaz/kt-yelp.svg)](https://jitpack.io/#jdiaz/kt-yelp)

# kt-yelp

An async [yelp fushion API](https://www.yelp.com/developers/documentation/v3) client library written in kotlin.

## How to import
This library is published in [jitpack](https://jitpack.io/#jdiaz/kt-yelp). Click on the latest version's `Get It` button. Depending on which build system you use in your project you can use either one of the following shown as shown in the link above.

**Gradle**
```groovy
repositories {
  ...
  maven { url 'https://jitpack.io' }
}

dependencies {
  compile 'com.github.jdiaz:kt-yelp:0.2.0'
}
```

**Maven**
```xml
<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
  </repository>
</repositories>

<dependency>
  <groupId>com.github.jdiaz</groupId>
  <artifactId>kt-yelp</artifactId>
  <version>0.2.0</version>
</dependency>

```

## Usage

kt-yelp supports async HTTP GET requests by employing the [kotlin-futures](https://github.com/vjames19/kotlin-futures) 
library, resulting in a clean API. The client support business search by 
parameters, business id, and phone. Each call returns a compossible Future
from the `kotlin-futures` library.

```kotlin
import io.github.jdiaz.yelp.Yelp
 
fun main(args: Array<String>) {
    // You can provide an instance of ExecutorService of your choosing.
    val executor = Executors.newCachedThreadPool()
    val yelp = Yelp(System.getProperty("yelpkey"), executor)
    val params = mapOf(
        "term" to "food",
        "latitude" to "30.307182",
        "longitude" to "-97.755996"
    )
    // By virtue of kotlin-futures
    val searchFuture = yelp.search(params).onComplete(
        onFailure = { throwable ->
            // Handle error asynchronously
           
            // However, this function wont suppress the exception when 
            // attempting to retrieve the value in the main thread later
            // with .get(). For such purposes use either myFuture.handle()
            // or surround the myFuture.get() call with try catch.

            // Yelp returns valid json when wrong parameters are used.
            // It also returns valid JSON When wrong api key is provided.
            // So, it will not trigger a onFailure lambda.
            val error = throwable.message
            println(
                "If you triggered this lambda its probably network IO error: $error"
            )
        },
        onSuccess = { jsonResp ->
            // Handle success asynchronously

            // Careful could just be a Yelp API error message are valid 
            // JSON messages.
            println("response:\n$jsonResp")
        }
    )
    
    Thread.sleep(5000)

    // Error handling when retrieving the data back to the main thread.

    // Try Catching exception
    val outcomeTryCatch = try { searchFuture.get() } catch(ex: Exception) { print("foo") }
    // .Handling the exception
    val outcomeHandle = searchFuture.handle { data, error ->
        data ?: "Woops, there was an error:\n\t$error"
    }.get()

    // Do stuff with outcome here in the main thread, later.
}
```

## Tests

Testing locally requires the `-Dyelpkey`jvm property set to run.
 `gradle -Dyelpkey=<YOUR_API_KEY> test`
 
Remote testing done via travis CI. Builds fetch the encrypted key from
 the `.travis.yml` file.
