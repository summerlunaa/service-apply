package apply.domain.judgehistory

import apply.createCommit
import apply.createJudgeHistory
import apply.createJudgeHistoryResult
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue

class JudgeHistoryTest : StringSpec({
    "커밋 해쉬가 다르면 자동 채점이 가능하다" {
        val judgeHistory = createJudgeHistory()
        val commit = createCommit("different-commit-hash")

        val isCompleted = judgeHistory.isCompleted(commit)

        isCompleted.shouldBeFalse()
    }

    "이전 자동채점 결과가 에러였다면 커밋 해쉬가 같아도 자동 채점이 가능하다" {
        val judgeHistory = createJudgeHistory(
            result = createJudgeHistoryResult(
                statusCode = JudgeStatusCode.INTERNAL_SERVER_ERROR
            )
        )
        val commit = createCommit()

        val isEqual = judgeHistory.isCompleted(commit)

        isEqual.shouldBeFalse()
    }

    "커밋 해쉬가 같고, 이전 자동 채점 결과가 정상이었다면 자동 채점이 불가능하다" {
        val judgeHistory = createJudgeHistory()
        val commit = createCommit()

        val isEqual = judgeHistory.isCompleted(commit)

        isEqual.shouldBeTrue()
    }
})
