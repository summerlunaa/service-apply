package apply.ui.api

import apply.application.JudgeService
import apply.createJudgeHistoryResponse
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.test.web.servlet.post
import support.test.web.servlet.bearer

@WebMvcTest(JudgeRestController::class)
internal class JudgeControllerTest : RestControllerTest() {
    @MockkBean
    private lateinit var judgeService: JudgeService

    @Test
    fun `예제 테스트를 실행한다`() {
        val response = createJudgeHistoryResponse()
        every { judgeService.runExampleTestCase(any(), any()) } returns response

        mockMvc.post("/api/recruitments/{recruitmentId}/missions/{missionId}/judgement", 1L, 1L) {
            bearer("valid_token")
        }.andExpect {
            status { isOk }
            content { success(response) }
        }.andDo {
            handle(document("judgement-post"))
        }
    }
}
