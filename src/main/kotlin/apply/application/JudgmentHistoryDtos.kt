package apply.application

import apply.domain.assignment.Assignment
import apply.domain.judgment.JudgmentHistory

data class JudgmentHistoryResponse(
    val testStatus: TestStatus,
    val pullRequestUrl: String,
    val commitUrl: String,
    val passCount: Int?,
    val totalCount: Int?,
    val message: String?
) {
    constructor(judgmentHistory: JudgmentHistory, assignment: Assignment) : this(
        TestStatus.PENDING,
        assignment.pullRequestUrl,
        toCommitUrl(assignment.pullRequestUrl, judgmentHistory.commitHash),
        judgmentHistory.result?.passCount,
        judgmentHistory.result?.totalCount,
        judgmentHistory.result?.statusCode?.message
    )

    companion object {
        fun toCommitUrl(pullRequestUrl: String, commitHash: String): String = "$pullRequestUrl/commits/$commitHash"
    }
}

enum class TestStatus {
    NONE, PENDING, COMPLETED
}
