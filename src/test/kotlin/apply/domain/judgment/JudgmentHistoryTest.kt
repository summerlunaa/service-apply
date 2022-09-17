package apply.domain.judgment

import apply.createCommit
import apply.createJudgmentHistory
import apply.createJudgmentHistoryResult
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue

class JudgmentHistoryTest : StringSpec({
    "커밋 해쉬가 다르면 자동 채점이 가능하다" {
        val judgmentHistory = createJudgmentHistory()
        val commit = createCommit(commitHash = "different-commit-hash")

        val isCompleted = judgmentHistory.isCompleted(commit)

        isCompleted.shouldBeFalse()
    }

    "이전 자동채점 결과가 에러였다면 커밋 해쉬가 같아도 자동 채점이 가능하다" {
        val judgmentHistory = createJudgmentHistory(
            result = createJudgmentHistoryResult(
                statusCode = JudgmentStatusCode.INTERNAL_SERVER_ERROR
            )
        )
        val commit = createCommit()

        val isCompleted = judgmentHistory.isCompleted(commit)

        isCompleted.shouldBeFalse()
    }

    "커밋 해쉬가 같고, 예제 테스트인 경우 이전 자동 채점 결과가 정상이었다면 자동 채점이 불가능하다" {
        val judgmentHistory = createJudgmentHistory()
        val commit = createCommit()

        val isCompleted = judgmentHistory.isCompleted(commit)

        isCompleted.shouldBeTrue()
    }

    "본 테스트인 경우에는 항상 자동채점이 가능하다" {
        val judgmentHistory = createJudgmentHistory(
            judgmentType = JudgmentType.REAL
        )
        val commit = createCommit()

        val isCompleted = judgmentHistory.isCompleted(commit)

        isCompleted.shouldBeFalse()
    }
})
