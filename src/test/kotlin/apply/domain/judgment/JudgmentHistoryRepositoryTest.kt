package apply.domain.judgment

import apply.createJudgmentHistory
import io.kotest.core.spec.style.ExpectSpec
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode
import io.kotest.matchers.shouldBe
import support.test.RepositoryTest

@RepositoryTest
class JudgmentHistoryRepositoryTest(
    private val judgmentHistoryRepository: JudgmentHistoryRepository
) : ExpectSpec({
    extensions(SpringTestExtension(SpringTestLifecycleMode.Root))

    context("실행 이력") {
        expect("user id와 mission id 중 가장 마지막을 찾는다.") {
            judgmentHistoryRepository.save(createJudgmentHistory(id = 1L))
            judgmentHistoryRepository.save(createJudgmentHistory(id = 2L))
            judgmentHistoryRepository.save(createJudgmentHistory(id = 3L))

            val lastHistory = judgmentHistoryRepository.findLastByUserIdAndMissionId(1L, 1L)

            lastHistory!!.id shouldBe 3L
        }
    }
})
