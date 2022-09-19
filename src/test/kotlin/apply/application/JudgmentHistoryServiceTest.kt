package apply.application

import apply.COMMIT_HASH
import apply.PULL_REQUEST_URL
import apply.REQUEST_KEY
import apply.createAssignment
import apply.createCommit
import apply.createJudgmentHistory
import apply.createJudgmentItem
import apply.domain.judgment.JudgmentHistoryRepository
import apply.domain.judgment.JudgmentItemRepository
import apply.domain.judgment.JudgmentType
import apply.domain.judgment.findLastByUserIdAndMissionIdAndJudgmentType
import apply.domain.judgment.getByMissionId
import apply.domain.judgmentserver.JudgmentServer
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk

class JudgmentHistoryServiceTest : BehaviorSpec({
    val judgmentHistoryRepository = mockk<JudgmentHistoryRepository>()
    val judgmentItemRepository = mockk<JudgmentItemRepository>()
    val judgmentServer = mockk<JudgmentServer>()

    val judgmentHistoryService = JudgmentHistoryService(
        judgmentHistoryRepository,
        judgmentItemRepository,
        judgmentServer
    )

    Given("테스트 이력이 존재하고 제출한 commit id와 최신 이력의 commit id가 같은 경우") {
        val assignment = createAssignment()
        val commit = createCommit()

        val existHistory = createJudgmentHistory(commitHash = COMMIT_HASH)
        every {
            judgmentHistoryRepository.findLastByUserIdAndMissionIdAndJudgmentType(any(), any(), any())
        } returns existHistory

        When("예제 테스트를 실행하면") {
            Then("기존의 최신 테스트 실행 결과를 반환한다") {
                val historyResponse =
                    judgmentHistoryService.findOrCreateHistory(assignment, JudgmentType.EXAMPLE, commit)
                historyResponse shouldBe JudgmentHistoryResponse(existHistory, PULL_REQUEST_URL)
            }
        }

        // todo: 추후 정책 수정될 수도 있음
        When("본 테스트를 실행하면") {
            Then("기존의 최신 테스트 실행 결과를 반환한다") {
                val historyResponse =
                    judgmentHistoryService.findOrCreateHistory(assignment, JudgmentType.REAL, commit)
                historyResponse shouldBe JudgmentHistoryResponse(existHistory, PULL_REQUEST_URL)
            }
        }
    }

    Given("테스트 이력이 존재하고 제출한 commit id와 최신 이력의 commit id가 다른 경우") {
        val assignment = createAssignment()
        val commit = createCommit()

        val existHistory = createJudgmentHistory(commitHash = "old-commit")
        every {
            judgmentHistoryRepository.findLastByUserIdAndMissionIdAndJudgmentType(any(), any(), any())
        } returns existHistory
        every { judgmentItemRepository.getByMissionId(any()) } returns createJudgmentItem()
        every { judgmentServer.requestJudgement(any()) } returns REQUEST_KEY
        every { judgmentHistoryRepository.save(any()) } returns createJudgmentHistory()

        When("예제 테스트를 실행하면") {
            Then("테스트를 실행한 뒤 새로운 테스트 실행 결과를 반환한다") {
                val historyResponse =
                    judgmentHistoryService.findOrCreateHistory(assignment, JudgmentType.EXAMPLE, commit)
                historyResponse shouldNotBe JudgmentHistoryResponse(existHistory, PULL_REQUEST_URL)
            }
        }

        When("본 테스트를 실행하면") {
            Then("테스트를 실행한 뒤 새로운 테스트 실행 결과를 반환한다") {
                val historyResponse =
                    judgmentHistoryService.findOrCreateHistory(assignment, JudgmentType.REAL, commit)
                historyResponse shouldNotBe JudgmentHistoryResponse(existHistory, PULL_REQUEST_URL)
            }
        }
    }

    Given("테스트 실행 이력이 존재하지 않는 경우") {
        val assignment = createAssignment()
        val commit = createCommit()

        val existHistory = createJudgmentHistory(commitHash = "old-commit")
        every {
            judgmentHistoryRepository.findLastByUserIdAndMissionIdAndJudgmentType(any(), any(), any())
        } returns null
        every { judgmentItemRepository.getByMissionId(any()) } returns createJudgmentItem()
        every { judgmentServer.requestJudgement(any()) } returns REQUEST_KEY
        every { judgmentHistoryRepository.save(any()) } returns createJudgmentHistory()

        When("예제 테스트를 실행하면") {
            Then("테스트를 실행한 뒤 새로운 테스트 실행 결과를 반환한다") {
                val historyResponse =
                    judgmentHistoryService.findOrCreateHistory(assignment, JudgmentType.EXAMPLE, commit)
                historyResponse shouldNotBe JudgmentHistoryResponse(existHistory, PULL_REQUEST_URL)
            }
        }

        When("본 테스트를 실행하면") {
            Then("테스트를 실행한 뒤 새로운 테스트 실행 결과를 반환한다") {
                val historyResponse =
                    judgmentHistoryService.findOrCreateHistory(assignment, JudgmentType.REAL, commit)
                historyResponse shouldNotBe JudgmentHistoryResponse(existHistory, PULL_REQUEST_URL)
            }
        }
    }

})
