package apply.ui.api

import apply.application.JudgmentHistoryResponse
import apply.application.JudgmentService
import apply.domain.user.User
import apply.security.LoginUser
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/recruitments/{recruitmentId}/missions/{missionId}/judgment")
@RestController
class JudgmentRestController(
    private val judgmentService: JudgmentService
) {
    @PostMapping
    fun judgeExample(
        @PathVariable recruitmentId: String,
        @PathVariable missionId: Long,
        @LoginUser user: User,
    ): ResponseEntity<ApiResponse<JudgmentHistoryResponse>> {
        val response = judgmentService.judgeExample(missionId, user.id)
        return ResponseEntity.ok(ApiResponse.success(response))
    }
}
