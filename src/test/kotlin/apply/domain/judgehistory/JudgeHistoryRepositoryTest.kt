package apply.domain.judgehistory

import apply.createJudgeHistory
import io.kotest.core.spec.style.ExpectSpec
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode
import io.kotest.matchers.shouldBe
import support.test.RepositoryTest

@RepositoryTest
class JudgeHistoryRepositoryTest(
    private val judgeHistoryRepository: JudgeHistoryRepository
) : ExpectSpec({
    extensions(SpringTestExtension(SpringTestLifecycleMode.Root))

    context("실행 이력") {
        expect("user id와 mission id 중 가장 마지막을 찾는다.") {
            judgeHistoryRepository.save(createJudgeHistory(id = 1L))
            judgeHistoryRepository.save(createJudgeHistory(id = 2L))
            judgeHistoryRepository.save(createJudgeHistory(id = 3L))

            val lastHistory = judgeHistoryRepository.findLastByUserIdAndMissionId(1L, 1L)

            lastHistory!!.id shouldBe 3L
        }
    }
})
