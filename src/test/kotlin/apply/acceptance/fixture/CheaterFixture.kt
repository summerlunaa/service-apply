package apply.acceptance.fixture

import io.restassured.RestAssured
import io.restassured.http.ContentType

data class Cheater(val email: String, val description: String)

class CheaterBuilder {
    val email: String = "a@email.com"
    val description: String = "부정 행위자입니다."

    fun build(): Cheater {
        val cheater = Cheater(email, description)
        postCheater(cheater)
        return cheater
    }

    private fun postCheater(cheater: Cheater) {
        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(cheater)
            .`when`()
            .post("/api/cheaters")
    }
}

fun cheater(builder: CheaterBuilder.() -> Unit): Cheater {
    return CheaterBuilder().apply(builder).build()
}

fun cheater(): Cheater {
    return CheaterBuilder().build()
}
