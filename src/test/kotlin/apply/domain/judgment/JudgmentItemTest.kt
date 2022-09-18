package apply.domain.judgment

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue

internal class JudgmentItemTest : StringSpec({
    "evaluationItemId가 null이면 유효하지 않다" {
        val judgmentItem = JudgmentItem(1L, null, "testName", ProgrammingLanguage.KOTLIN, 1L)

        val isValid = judgmentItem.isValid()

        isValid.shouldBeFalse()
    }

    "testName이 null이면 유효하지 않다" {
        val judgmentItem = JudgmentItem(1L, 1L, null, ProgrammingLanguage.KOTLIN, 1L)

        val isValid = judgmentItem.isValid()

        isValid.shouldBeFalse()
    }

    "ProgrammingLanguage가 null이면 유효하지 않다" {
        val judgmentItem = JudgmentItem(1L, 1L, "testName", null, 1L)

        val isValid = judgmentItem.isValid()

        isValid.shouldBeFalse()
    }

    "evaluationItemId, testName, ProgrammingLanguage 모두 null이 아니면 유효하다" {
        val judgmentItem = JudgmentItem(1L, 1L, "testName", ProgrammingLanguage.KOTLIN, 1L)

        val isValid = judgmentItem.isValid()

        isValid.shouldBeTrue()
    }
})
