package apply.ui.api

import apply.application.JudgeHistoryResponse
import apply.application.JudgeService
import apply.domain.user.User
import apply.security.LoginUser
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/recruitments/{recruitmentId}/missions/{missionId}/judgement")
@RestController
class JudgeRestController(
    private val judgeService: JudgeService
) {
    @PostMapping
    fun runExampleTestCase(
        @PathVariable recruitmentId: String,
        @PathVariable missionId: Long,
        @LoginUser user: User,
    ): ResponseEntity<ApiResponse<JudgeHistoryResponse>> {
        val response = judgeService.runExampleTestCase(missionId, user.id)
        return ResponseEntity.ok(ApiResponse.success(response))
    }
}
