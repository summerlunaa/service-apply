package apply.application

import apply.COMMIT_HASH
import apply.application.github.GithubApi
import apply.createAssignment
import apply.createCommitResponse
import apply.createJudgmentHistory
import apply.createMission
import apply.domain.assignment.AssignmentRepository
import apply.domain.assignment.getByUserIdAndMissionId
import apply.domain.judgment.JudgmentHistoryRepository
import apply.domain.judgment.findLastByUserIdAndMissionId
import apply.domain.mission.MissionRepository
import apply.domain.mission.getById
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDateTime

class JudgmentServiceTest : BehaviorSpec({
    val missionRepository = mockk<MissionRepository>()
    val assignmentRepository = mockk<AssignmentRepository>()
    val judgmentHistoryRepository = mockk<JudgmentHistoryRepository>()
    val githubApi = mockk<GithubApi>()

    val judgmentService = JudgmentService(
        missionRepository, assignmentRepository, judgmentHistoryRepository, githubApi
    )

    Given("과제 제출 시간이 아닌 경우") {
        every { missionRepository.getById(any()) } returns createMission(
            startDateTime = LocalDateTime.now().minusMinutes(2),
            endDateTime = LocalDateTime.now().minusMinutes(1)
        )
        every { githubApi.requestCommits(any()) } returns listOf(createCommitResponse())
        every { assignmentRepository.getByUserIdAndMissionId(any(), any()) } returns createAssignment()
        every { judgmentHistoryRepository.findLastByUserIdAndMissionId(any(), any()) } returns null

        When("해당 과제 제출물의 예제 테스트를 실행하면") {
            Then("예외가 발생한다.") {
                shouldThrow<IllegalStateException> {
                    judgmentService.judgeExample(1L, 1L)
                }
            }
        }
    }

    Given("과제 제출 마감 시간 이전의 가장 최신 커밋이 존재하지 않는 경우") {
        every { missionRepository.getById(any()) } returns createMission()
        every { githubApi.requestCommits(any()) } returns listOf(
            createCommitResponse(date = LocalDateTime.now().plusDays(8))
        )
        every { assignmentRepository.getByUserIdAndMissionId(any(), any()) } returns createAssignment()
        every { judgmentHistoryRepository.findLastByUserIdAndMissionId(any(), any()) } returns null

        When("해당 과제 제출물의 예제 테스트를 실행하면") {
            Then("예외가 발생한다.") {
                shouldThrow<IllegalArgumentException> {
                    judgmentService.judgeExample(1L, 1L)
                }
            }
        }
    }

    Given("테스트 실행 이력이 존재하는 경우") {
        val assignment = createAssignment()
        every { missionRepository.getById(any()) } returns createMission()
        every { githubApi.requestCommits(any()) } returns listOf(createCommitResponse())
        every { assignmentRepository.getByUserIdAndMissionId(any(), any()) } returns assignment

        When("제출한 commit id와 최신 이력의 commit id가 같은 예제 테스트를 실행하면") {
            val existHistory = createJudgmentHistory(commitHash = COMMIT_HASH)
            every { judgmentHistoryRepository.findLastByUserIdAndMissionId(any(), any()) } returns existHistory

            Then("기존의 최신 테스트 실행 결과를 반환한다.") {
                val executionResponse = judgmentService.judgeExample(1L, 1L)
                executionResponse shouldBe JudgmentHistoryResponse(existHistory, assignment)
            }
        }

        When("제출한 commit id와 최신 이력의 commit id가 다른 예제 테스트를 실행하면") {
            val existHistory = createJudgmentHistory(commitHash = "old-commit")
            every { judgmentHistoryRepository.findLastByUserIdAndMissionId(any(), any()) } returns existHistory

            Then("테스트를 실행한 뒤 새로운 테스트 실행 결과를 반환한다.") {
                val executionResponse = judgmentService.judgeExample(1L, 1L)
                executionResponse shouldNotBe JudgmentHistoryResponse(existHistory, assignment)
            }
        }
    }
})