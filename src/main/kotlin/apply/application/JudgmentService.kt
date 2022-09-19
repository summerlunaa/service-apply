package apply.application

import apply.application.github.GithubApi
import apply.domain.assignment.Assignment
import apply.domain.assignment.AssignmentRepository
import apply.domain.assignment.getByUserIdAndMissionId
import apply.domain.judgment.Commit
import apply.domain.judgment.JudgmentType
import apply.domain.mission.Mission
import apply.domain.mission.MissionRepository
import apply.domain.mission.getById
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class JudgmentService(
    private val judgmentHistoryService: JudgmentHistoryService,
    private val missionRepository: MissionRepository,
    private val assignmentRepository: AssignmentRepository,
    private val githubApi: GithubApi
) {
    fun judgeExample(missionId: Long, userId: Long): JudgmentHistoryResponse {
        val mission = missionRepository.getById(missionId)
        check(mission.isSubmitting) { "예제 테스트 실행 가능 시간이 아닙니다." }
        val assignment = assignmentRepository.getByUserIdAndMissionId(userId, missionId)

        return judge(mission, assignment, JudgmentType.EXAMPLE)
    }

    fun judgeReal(missionId: Long, userId: Long): JudgmentHistoryResponse {
        val mission = missionRepository.getById(missionId)
        val assignment = assignmentRepository.getByUserIdAndMissionId(userId, missionId)

        return judge(mission, assignment, JudgmentType.REAL)
    }

    private fun judge(mission: Mission, assignment: Assignment, judgmentType: JudgmentType): JudgmentHistoryResponse {
        val latestCommit = findLatestCommit(assignment.pullRequestUrl, mission.period.endDateTime)
        return judgmentHistoryService.findOrCreateHistory(assignment, judgmentType, latestCommit)
    }

    private fun findLatestCommit(pullRequestUrl: String, endDateTime: LocalDateTime): Commit {
        val latestCommit = githubApi.requestLatestCommit(pullRequestUrl, endDateTime)
        return Commit(latestCommit.hash, pullRequestUrl)
    }
}
