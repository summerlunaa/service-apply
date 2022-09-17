package apply

import apply.application.JudgmentHistoryResponse
import apply.application.TestStatus
import apply.application.github.CommitResponse
import apply.domain.judgment.Commit
import apply.domain.judgment.JudgmentHistory
import apply.domain.judgment.JudgmentResult
import apply.domain.judgment.JudgmentStatusCode
import apply.domain.judgment.JudgmentType
import java.time.LocalDateTime

const val COMMIT_HASH = "commit-hash"
const val COMMIT_URL =
    "https://github.com/woowacourse/service-apply/pull/367/commits/eeb43de3f53f4bec08e7d63f07badb66c12dfa31"

fun createJudgmentHistory(
    userId: Long = 1L,
    missionId: Long = 1L,
    requestKey: String = "default-request-key",
    commitHash: String = COMMIT_HASH,
    judgmentType: JudgmentType = JudgmentType.EXAMPLE,
    result: JudgmentResult = createJudgmentHistoryResult(),
    id: Long = 0L
): JudgmentHistory {
    return JudgmentHistory(userId, missionId, requestKey, commitHash, judgmentType, result, id)
}

fun createJudgmentHistoryResult(
    correctCount: Int = 10,
    totalCount: Int = 10,
    statusCode: JudgmentStatusCode = JudgmentStatusCode.OK
): JudgmentResult {
    return JudgmentResult(correctCount, totalCount, statusCode)
}

fun createJudgmentHistoryResponse(
    testStatus: TestStatus = TestStatus.PENDING,
    pullRequestUrl: String = PULL_REQUEST_URL,
    commitUrl: String = COMMIT_URL,
    passCount: Int? = null,
    totalCount: Int? = null,
    message: String? = null
): JudgmentHistoryResponse {
    return JudgmentHistoryResponse(testStatus, pullRequestUrl, commitUrl, passCount, totalCount, message)
}

fun createCommit(
    commitHash: String = COMMIT_HASH,
    date: LocalDateTime = LocalDateTime.now()
): Commit {
    return Commit(commitHash, date)
}

fun createCommitResponse(
    hash: String = COMMIT_HASH,
    date: LocalDateTime = LocalDateTime.now()
): CommitResponse {
    return CommitResponse(hash, date.toString())
}
