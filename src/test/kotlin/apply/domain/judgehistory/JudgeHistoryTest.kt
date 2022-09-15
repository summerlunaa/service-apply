package apply.domain.judgehistory

import apply.createCommit
import apply.createJudgeHistory
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeTrue

class JudgeHistoryTest : StringSpec({

    "commit hash 값이 같은지 비교한다" {
        val judgeHistory = createJudgeHistory()
        val commit = createCommit()

        val isEqual = judgeHistory.isCommitIdEqual(commit)

        isEqual.shouldBeTrue()
    }
})
