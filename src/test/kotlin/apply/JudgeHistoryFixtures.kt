package apply

import apply.domain.judgehistory.Commit
import apply.domain.judgehistory.Commits
import apply.domain.judgehistory.JudgeHistory
import apply.domain.judgehistory.JudgeResult
import apply.domain.judgehistory.JudgeStatusCode
import apply.domain.judgehistory.JudgeTestType
import java.time.LocalDateTime

const val COMMIT_ID = "commit-id"
const val LAST_COMMIT_ID = "last-commit-id"

fun createJudgeHistory(
    userId: Long = 1L,
    missionId: Long = 1L,
    requestKey: String = "default-request-key",
    commitId: String = COMMIT_ID,
    testType: JudgeTestType = JudgeTestType.EXAMPLE,
    result: JudgeResult = createJudgeHistoryResult(),
    id: Long = 0L
): JudgeHistory {
    return JudgeHistory(userId, missionId, requestKey, commitId, testType, result, id)
}

fun createJudgeHistoryResult(
    correctCount: Int = 10,
    totalCount: Int = 10,
    statusCode: JudgeStatusCode = JudgeStatusCode.OK
): JudgeResult {
    return JudgeResult(correctCount, totalCount, statusCode)
}

fun createCommits(
    commits: List<Commit> = listOf(
        createCommit(),
        createCommit(LAST_COMMIT_ID, LocalDateTime.now().plusDays(1))
    )
): Commits {
    return Commits(commits)
}

fun createCommit(
    commitId: String = COMMIT_ID,
    date: LocalDateTime = LocalDateTime.now()
): Commit {
    return Commit(commitId, date)
}
