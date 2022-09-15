package apply

import apply.application.JudgeHistoryResponse
import apply.application.TestStatus
import apply.application.github.CommitResponse
import apply.domain.judgehistory.Commit
import apply.domain.judgehistory.JudgeHistory
import apply.domain.judgehistory.JudgeResult
import apply.domain.judgehistory.JudgeStatusCode
import apply.domain.judgehistory.JudgeType
import java.time.LocalDateTime

const val COMMIT_HASH = "commit-hash"
const val COMMIT_URL =
    "https://github.com/woowacourse/service-apply/pull/367/commits/eeb43de3f53f4bec08e7d63f07badb66c12dfa31"

fun createJudgeHistory(
    userId: Long = 1L,
    missionId: Long = 1L,
    requestKey: String = "default-request-key",
    commitHash: String = COMMIT_HASH,
    judgeType: JudgeType = JudgeType.EXAMPLE,
    result: JudgeResult = createJudgeHistoryResult(),
    id: Long = 0L
): JudgeHistory {
    return JudgeHistory(userId, missionId, requestKey, commitHash, judgeType, result, id)
}

fun createJudgeHistoryResult(
    correctCount: Int = 10,
    totalCount: Int = 10,
    statusCode: JudgeStatusCode = JudgeStatusCode.OK
): JudgeResult {
    return JudgeResult(correctCount, totalCount, statusCode)
}

fun createJudgeHistoryResponse(
    testStatus: TestStatus = TestStatus.PENDING,
    pullRequestUrl: String = PULL_REQUEST_URL,
    commitUrl: String = COMMIT_URL,
    passCount: Int? = null,
    totalCount: Int? = null,
    message: String? = null
): JudgeHistoryResponse {
    return JudgeHistoryResponse(testStatus, pullRequestUrl, commitUrl, passCount, totalCount, message)
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
