package apply.domain.testexecutionhistory

import apply.createCommit
import apply.createTestExecutionHistory
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeTrue

class TestExecutionHistoryTest : StringSpec({

    "commit hash 값이 같은지 비교한다" {
        val testExecution = createTestExecutionHistory()
        val commit = createCommit()

        val isEqual = testExecution.isCommitIdEqual(commit)

        isEqual.shouldBeTrue()
    }
})
