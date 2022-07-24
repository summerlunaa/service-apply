package apply.acceptance
//
// import apply.acceptance.fixture.cheater
// import apply.acceptance.fixture.evaluationItems
// import apply.acceptance.fixture.recruitment
// import apply.domain.user.Gender
// import io.restassured.RestAssured
// import org.junit.jupiter.api.BeforeEach
// import org.junit.jupiter.api.Test
// import org.springframework.boot.test.context.SpringBootTest
// import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
// import org.springframework.boot.web.server.LocalServerPort
// import java.time.LocalDateTime
//
// @SpringBootTest(webEnvironment = RANDOM_PORT)
// class AcceptanceTestFixture {
//
//     @LocalServerPort
//     var port: Int = 0
//
//     @BeforeEach
//     fun setUp() {
//         RestAssured.port = port
//     }
//
//     @Test
//     fun evaluation() {
//         val evaluationDefault = evaluation()
//
//         val evaluation = evaluation {
//             title = "1주차 - 숫자야구게임"
//             description = "[리뷰 절차]\n" +
//                 "https://github.com/woowacourse/woowacourse-docs/tree/master/precourse"
//             recruitmentId = 1L
//             recruitment = recruitment {
//                 ...
//             }
//             beforeEvaluationId = 1L
//             beforeEvaluation = evaluationSelect {
//                 title = ""
//                 id = 1L
//             }
//             evaluationItems {
//                 evaluationItem()
//                 evaluationItem {
//                     title = "README.md 파일에 기능 목록이 추가되어 있는가?"
//                     maximumScore = 10
//                     position = 1
//                     description = "[리뷰 절차]\n" +
//                     "https://github.com/woowacourse/woowacourse-docs/tree/master/precourse"
//                 }
//             }
//         }
//     }
//
//     @Test
//     fun cheater() {
//         val cheater1 = cheater {
//             email = "a@email.com" // service에서 email로 유저를 찾는데 없으면 예외 발생
//             description = "설명입니다."
//         }
//
//         val cheater2 = cheater {
//             user = user { // email만 필요한데 유저를 만들어야 하나? ㄹㅇㅋㅋ
//                 name = "이름"
//                 email = "a@email.com"
//                 phoneNumber = "010-1234-5678"
//                 gender = Gender.FEMALE
//                 birthday = LocalDateTime.now()
//                 isCheater = false
//                 applicationForm = applicationForm {
//                     ....
//                 }
//             }
//             description = "설명입니다."
//         }
//     }
//
//     @Test
//     fun mission() {
//         val mission1 = mission {
//             title = "미션 제목"
//             evaluation = evaluationSelect {
//                 title = "평가 제목"
//                 id = 1L
//             }
//             startDateTime = LocalDateTime.now().minusYears(1L)
//             endDateTime = LocalDateTime.now().plusYears(1L)
//             description = "설명설명"
//             submittable = false
//             hidden = false
//         }
//
//         val mission2 = mission {
//             title = "미션 제목"
//             evaluationId = 1L
//             evaluation = evaluation {
//                 ...
//             }
//             startDateTime = LocalDateTime.now().minusYears(1L)
//             endDateTime = LocalDateTime.now().plusYears(1L)
//             description = "설명설명"
//             submittable = false
//             hidden = false
//         }
//     }
// }
