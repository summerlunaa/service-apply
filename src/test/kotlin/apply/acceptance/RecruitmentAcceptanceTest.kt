package apply.acceptance

import apply.acceptance.fixture.recruitment
import apply.acceptance.fixture.recruitmentItems
import apply.acceptance.fixture.term
import io.restassured.RestAssured
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.web.server.LocalServerPort
import java.time.LocalDateTime

@SpringBootTest(webEnvironment = RANDOM_PORT)
class RecruitmentAcceptanceTest {

    @LocalServerPort
    var port: Int = 0

    @BeforeEach
    fun setUp() {
        RestAssured.port = port
    }

    @Test
    fun dslRecruitment() {
        val now = LocalDateTime.now()
        val recruitment = recruitment {
            title = "웹 백엔드 3기"
            // termId = 1L
            // 직접 지정도 가능
            term = term {
                name = "단독 모집"
            }
            startDateTime = now.minusYears(1)
            endDateTime = now.plusYears(1)
            recruitable = true
            hidden = true
            recruitmentItems = recruitmentItems {
                recruitmentItem()
                recruitmentItem {
                    position = 2
                    maximumLength = 1500
                }
                recruitmentItem {
                    title = "프로그래밍 학습 과정과 현재 자신이 생각하는 역량은?"
                    position = 3
                    maximumLength = 2000
                    description = "우아한테크코스는..."
                }
            }
        }
    }

    @Test
    fun dslRecruitmentTermId() {
        val now = LocalDateTime.now()
        val recruitment = recruitment {
            title = "웹 백엔드 3기"
            termId = 1L
            startDateTime = now.minusYears(1)
            endDateTime = now.plusYears(1)
            recruitable = true
            hidden = true
            recruitmentItems = recruitmentItems {
                recruitmentItem()
                recruitmentItem {
                    position = 2
                    maximumLength = 1500
                }
                recruitmentItem {
                    title = "프로그래밍 학습 과정과 현재 자신이 생각하는 역량은?"
                    position = 3
                    maximumLength = 2000
                    description = "우아한테크코스는..."
                }
            }
        }
    }
}
