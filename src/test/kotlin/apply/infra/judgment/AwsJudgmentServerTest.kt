package apply.infra.judgment

import apply.createJudgmentRequest
import apply.domain.judgmentserver.JudgmentServer
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.string.shouldHaveLength

private const val REQUEST_KEY_SIZE = 36

internal class AwsJudgmentServerTest(
    judgmentServer: JudgmentServer
) : StringSpec({
    "요청한다".config(enabled = false) {
        val requestKey = judgmentServer.requestJudgement(createJudgmentRequest())

        requestKey shouldHaveLength REQUEST_KEY_SIZE
    }
})
