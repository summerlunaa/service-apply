package apply

import apply.domain.testexecutionhistory.TestExecutionHistory
import apply.domain.testexecutionhistory.TestExecutionResult
import apply.domain.testexecutionhistory.TestExecutionType
import apply.domain.testexecutionhistory.Commit
import java.time.LocalDateTime

const val COMMIT_ID = "commit id"

fun createTestExecutionHistory(
    userId: Long = 1L,
    missionId: Long = 1L,
    requestKey: Long = 1L,
    commitId: String = COMMIT_ID,
    testType: TestExecutionType = TestExecutionType.EXAMPLE,
    result: TestExecutionResult = createTestExecutionResult(),
    id: Long = 0L
): TestExecutionHistory {
    return TestExecutionHistory(userId, missionId, requestKey, commitId, testType, result, id)
}

fun createTestExecutionResult(
    correctCount: Int = 10,
    totalCount: Int = 10,
    message: String = "성공"
): TestExecutionResult {
    return TestExecutionResult(correctCount, totalCount, message)
}

fun createCommit(
    commitId: String = COMMIT_ID,
    date: LocalDateTime = LocalDateTime.now()
): Commit {
    return Commit(commitId, date)
}
