// package apply.acceptance.fixture
//
// import apply.ui.api.ApiResponse
// import io.restassured.RestAssured
// import io.restassured.http.ContentType
// import io.restassured.mapper.TypeRef
//
// data class Evaluation(
//     var title: String,
//     var description: String = "",
//     var recruitment: Recruitment = ,
//     var beforeEvaluation: BeforeEvaluation,
//     var evaluationItems: List<EvaluationItem>,
//     var id: Long = 0L
// )
//
// data class BeforeEvaluation(
//     val title: String,
//     val id: Long
// )
//
// class EvaluationBuilder {
//     var title: String = ""
//     var description: String = ""
//     var recruitmentId: Long = 0L
//     lateinit var recruitment: Recruitment
//     var beforeEvaluation: Evaluation? = Evaluation(
//         title = "이전 평가 없음",
//         id = 0L
//     )
//     var evaluationItems: List<EvaluationItem> = evaluationItems {
//         evaluationItem()
//         evaluationItem {
//
//         }
//     }
//     var id: Long = 0L
//
//     fun build(): Evaluation {
//         if (!::recruitment.isInitialized) {
//             recruitment = getRecruitmentById()
//         }
//
//         val evaluation = evaluation(
//
//         )
//
//         postEvaluation(evaluation)
//         return evaluation
//     }
//
//     private fun postEvaluation(evaluation: Evaluation) {
//         RestAssured.given()
//             .contentType(ContentType.JSON)
//             .body(evaluation)
//             .`when`()
//             .post("/api/recruitments/{recruitmentId}/evaluations", recruitmentId)
//     }
//
//     private fun getRecruitmentById(): Recruitment {
//         return RestAssured.given()
//             .get("/api/recruitments/$recruitmentId")
//             .then()
//             .extract()
//             .`as`(object : TypeRef<ApiResponse<Recruitment>>() {})
//             .body as Recruitment
//     }
// }
//
// fun evaluation(builder: EvaluationBuilder.() -> Unit): Evaluation {
//     return EvaluationBuilder().apply(builder).build()
// }
//
// fun evaluation(): Evaluation {
//     return EvaluationBuilder().build()
// }
