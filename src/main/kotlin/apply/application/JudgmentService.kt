package apply.application

import apply.application.github.GithubApi
import apply.domain.assignment.Assignment
import apply.domain.assignment.AssignmentRepository
import apply.domain.assignment.getByUserIdAndMissionId
import apply.domain.judgment.Commit
import apply.domain.judgment.Commits
import apply.domain.judgment.JudgmentHistory
import apply.domain.judgment.JudgmentHistoryRepository
import apply.domain.judgment.JudgmentType
import apply.domain.judgment.findLastByUserIdAndMissionId
import apply.domain.mission.Mission
import apply.domain.mission.MissionRepository
import apply.domain.mission.getById
import org.springframework.stereotype.Service

private const val TEMPORARY_REQUEST_KEY = "temporary-request-key"

@Service
class JudgmentService(
    private val missionRepository: MissionRepository,
    private val assignmentRepository: AssignmentRepository,
    private val judgmentHistoryRepository: JudgmentHistoryRepository,
    private val githubApi: GithubApi
) {
    fun judgeExample(missionId: Long, userId: Long): JudgmentHistoryResponse {
        val mission = missionRepository.getById(missionId)
        check(mission.isSubmitting) { "예제 테스트 실행 가능 시간이 아닙니다." }

        val assignment = assignmentRepository.getByUserIdAndMissionId(userId, missionId)
        val latestCommit = findLatestCommitFromGithub(mission, assignment)
        val lastHistory = judgmentHistoryRepository.findLastByUserIdAndMissionId(userId, missionId)

        // TODO : implement api request to judge system
        return (
            lastHistory?.takeIf { it.isCompleted(latestCommit) }
                ?: JudgmentHistory(userId, missionId, TEMPORARY_REQUEST_KEY, latestCommit.hash, JudgmentType.EXAMPLE)
            ).let { JudgmentHistoryResponse(it, assignment) }
    }

    private fun findLatestCommitFromGithub(mission: Mission, assignment: Assignment): Commit {
        return githubApi.requestCommits(assignment)
            .map { Commit(it.hash, it.date) }
            .let(::Commits)
            .findLatestUntilEndDateTime(mission.period.endDateTime)
    }
}
