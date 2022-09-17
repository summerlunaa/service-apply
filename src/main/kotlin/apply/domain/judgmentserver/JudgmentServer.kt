package apply.domain.judgmentserver

import apply.application.judgmentserver.JudgmentRequest

interface JudgmentServer {
    fun requestJudgement(judgmentRequest: JudgmentRequest): String
}
