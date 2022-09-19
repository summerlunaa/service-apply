package apply.application

import apply.application.github.GithubApi
import apply.createAssignment
import apply.createCommitResponse
import apply.createJudgmentHistoryResponse
import apply.createMission
import apply.domain.assignment.AssignmentRepository
import apply.domain.assignment.getByUserIdAndMissionId
import apply.domain.mission.MissionRepository
import apply.domain.mission.getById
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDateTime

class JudgmentServiceTest : BehaviorSpec({
    val judgmentHistoryService = mockk<JudgmentHistoryService>()
    val missionRepository = mockk<MissionRepository>()
    val assignmentRepository = mockk<AssignmentRepository>()
    val githubApi = mockk<GithubApi>()

    val judgmentService = JudgmentService(
        judgmentHistoryService,
        missionRepository,
        assignmentRepository,
        githubApi
    )

    Given("과제 제출 시간이 아닌 경우") {
        every { missionRepository.getById(any()) } returns createMission(
            startDateTime = LocalDateTime.now().minusMinutes(2),
            endDateTime = LocalDateTime.now().minusMinutes(1)
        )
        every { assignmentRepository.getByUserIdAndMissionId(any(), any()) } returns createAssignment()

        When("해당 과제 제출물의 예제 테스트를 실행하면") {
            Then("예외가 발생한다") {
                shouldThrow<IllegalStateException> {
                    judgmentService.judgeExample(1L, 1L)
                }
            }
        }

        When("해당 과제 제출물의 본 테스트를 실행하면") {
            every { githubApi.requestLatestCommit(any(), any()) } returns
                createCommitResponse(
                    date = LocalDateTime.now().minusMinutes(3)
                )
            val expected = createJudgmentHistoryResponse()
            every {
                judgmentHistoryService.findOrCreateHistory(any(), any(), any())
            } returns expected

            Then("정상적으로 채점을 진행한다") {
                val actual = judgmentService.judgeReal(1L, 1L)

                actual shouldBe expected
            }
        }
    }
})
