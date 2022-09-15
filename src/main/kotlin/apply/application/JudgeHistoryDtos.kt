package apply.application

import apply.domain.assignment.Assignment
import apply.domain.judgehistory.JudgeHistory

data class JudgeHistoryResponse(
    val testStatus: TestStatus,
    val pullRequestUrl: String,
    val commitUrl: String,
    val passCount: Int?,
    val totalCount: Int?,
    val message: String?
) {
    constructor(judgeHistory: JudgeHistory, assignment: Assignment) : this(
        TestStatus.PENDING,
        assignment.pullRequestUrl,
        toCommitUrl(assignment.pullRequestUrl, judgeHistory.commitHash),
        judgeHistory.result?.passCount,
        judgeHistory.result?.totalCount,
        judgeHistory.result?.statusCode?.message
    )

    companion object {
        fun toCommitUrl(pullRequestUrl: String, commitHash: String): String = "$pullRequestUrl/commits/$commitHash"
    }
}

enum class TestStatus {
    NONE, PENDING, COMPLETED
}
