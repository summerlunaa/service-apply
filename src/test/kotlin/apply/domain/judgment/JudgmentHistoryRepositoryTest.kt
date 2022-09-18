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

    context("쟈동채점 실행 이력") {
        judgmentHistoryRepository.save(createJudgmentHistory(id = 1L))
        judgmentHistoryRepository.save(createJudgmentHistory(id = 2L))
        judgmentHistoryRepository.save(createJudgmentHistory(id = 3L))
        expect("userId와 missionId로 마지막 예제 테스트 실행 이력을 찾는다") {
            val lastHistory =
                judgmentHistoryRepository.findLastByUserIdAndMissionIdAndJudgmentType(1L, 1L, JudgmentType.EXAMPLE)

            lastHistory!!.id shouldBe 3L
        }

        expect("userId와 missionId로 마지막 본 테스트 실행 이력을 찾는다") {
            judgmentHistoryRepository.save(createJudgmentHistory(id = 4L, judgmentType = JudgmentType.REAL))
            judgmentHistoryRepository.save(createJudgmentHistory(id = 5L, judgmentType = JudgmentType.EXAMPLE))

            val lastHistory =
                judgmentHistoryRepository.findLastByUserIdAndMissionIdAndJudgmentType(1L, 1L, JudgmentType.REAL)

            lastHistory!!.id shouldBe 4L
        }
    }
})
