package apply.domain.judgmentserver

import apply.application.judgmentserver.JudgmentRequest
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile("local", "test")
class FakeJudgmentServer : JudgmentServer {
    override fun requestJudgement(judgmentRequest: JudgmentRequest): String {
        return "request-key"
    }
}
