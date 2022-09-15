package apply.application

import apply.LAST_COMMIT_HASH
import apply.application.github.GithubApi
import apply.createAssignment
import apply.createCommit
import apply.createCommits
import apply.createJudgeHistory
import apply.createMission
import apply.domain.assignment.AssignmentRepository
import apply.domain.assignment.getByUserIdAndMissionId
import apply.domain.judgehistory.JudgeHistoryRepository
import apply.domain.judgehistory.findLastByUserIdAndMissionId
import apply.domain.mission.MissionRepository
import apply.domain.mission.getById
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDateTime

class JudgeServiceTest : BehaviorSpec({
    val missionRepository = mockk<MissionRepository>()
    val assignmentRepository = mockk<AssignmentRepository>()
    val judgeHistoryRepository = mockk<JudgeHistoryRepository>()
    val githubApi = mockk<GithubApi>()

    val judgeService = JudgeService(
        missionRepository, assignmentRepository, judgeHistoryRepository, githubApi
    )

    Given("과제 제출 시간이 아닐 때") {
        every { missionRepository.getById(any()) } returns createMission(
            startDateTime = LocalDateTime.now().minusMinutes(2),
            endDateTime = LocalDateTime.now().minusMinutes(1)
        )
        every { githubApi.requestCommits(any()) } returns createCommits()
        every { assignmentRepository.getByUserIdAndMissionId(any(), any()) } returns createAssignment()
        every { judgeHistoryRepository.findLastByUserIdAndMissionId(any(), any()) } returns null

        When("예제 테스트 케이스를 실행하면") {
            Then("예제 테스트가 실행되지 않는다.") {
                shouldThrow<IllegalStateException> { judgeService.runExampleTestCase(1L, 1L) }
            }
        }
    }

    Given("과제 제출 마감 시간 이전의 가장 최신 커밋이 존재하지 않을 때") {
        every { missionRepository.getById(any()) } returns createMission()
        every { githubApi.requestCommits(any()) } returns createCommits(
            listOf(
                createCommit(date = LocalDateTime.now().plusDays(8))
            )
        )
        every { assignmentRepository.getByUserIdAndMissionId(any(), any()) } returns createAssignment()
        every { judgeHistoryRepository.findLastByUserIdAndMissionId(any(), any()) } returns null

        When("예제 테스트 케이스를 실행하면") {
            Then("예제 테스트가 실행되지 않는다.") {
                shouldThrow<IllegalArgumentException> { judgeService.runExampleTestCase(1L, 1L) }
            }
        }
    }

    Given("테스트 실행 이력이 존재할 때") {
        val assignment = createAssignment()
        every { missionRepository.getById(any()) } returns createMission()
        every { githubApi.requestCommits(any()) } returns createCommits()
        every { assignmentRepository.getByUserIdAndMissionId(any(), any()) } returns assignment

        When("제출한 commit id와 최신 이력의 commit id가 같은 예제 테스트 케이스를 실행하면") {
            val existHistory = createJudgeHistory(commitHash = LAST_COMMIT_HASH)
            every { judgeHistoryRepository.findLastByUserIdAndMissionId(any(), any()) } returns existHistory
            Then("기존의 최신 테스트 실행 결과를 반환한다.") {
                val executionResponse = judgeService.runExampleTestCase(1L, 1L)
                executionResponse shouldBe JudgeHistoryResponse(existHistory, assignment)
            }
        }

        When("제출한 commit id와 최신 이력의 commit id가 다른 예제 테스트 케이스를 실행하면") {
            val existHistory = createJudgeHistory(commitHash = "old-commit")
            every { judgeHistoryRepository.findLastByUserIdAndMissionId(any(), any()) } returns existHistory
            Then("테스트를 실행한 뒤 새로운 테스트 실행 결과를 반환한다.") {
                val executionResponse = judgeService.runExampleTestCase(1L, 1L)
                executionResponse shouldNotBe JudgeHistoryResponse(existHistory, assignment)
            }
        }
    }
})
