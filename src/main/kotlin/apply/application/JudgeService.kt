package apply.application

import apply.application.github.GithubApi
import apply.domain.assignment.Assignment
import apply.domain.assignment.AssignmentRepository
import apply.domain.assignment.getByUserIdAndMissionId
import apply.domain.judgehistory.Commit
import apply.domain.judgehistory.Commits
import apply.domain.judgehistory.JudgeHistory
import apply.domain.judgehistory.JudgeHistoryRepository
import apply.domain.judgehistory.JudgeType
import apply.domain.judgehistory.findLastByUserIdAndMissionId
import apply.domain.mission.Mission
import apply.domain.mission.MissionRepository
import apply.domain.mission.getById
import org.springframework.stereotype.Service

private const val TEMPORARY_REQUEST_KEY = "temporary-request-key"

@Service
class JudgeService(
    private val missionRepository: MissionRepository,
    private val assignmentRepository: AssignmentRepository,
    private val judgeHistoryRepository: JudgeHistoryRepository,
    private val githubApi: GithubApi
) {
    fun runExampleTest(missionId: Long, userId: Long): JudgeHistoryResponse {
        val mission = missionRepository.getById(missionId)
        check(mission.isSubmitting) { "예제 테스트 실행 가능 시간이 아닙니다." }

        val assignment = assignmentRepository.getByUserIdAndMissionId(userId, missionId)
        val latestCommit = findLatestCommitFromGithub(mission, assignment)
        val lastHistory = judgeHistoryRepository.findLastByUserIdAndMissionId(userId, missionId)

        // TODO : implement api request to judge system
        return (
                lastHistory?.takeIf { it.isCompleted(latestCommit) }
                    ?: JudgeHistory(userId, missionId, TEMPORARY_REQUEST_KEY, latestCommit.hash, JudgeType.EXAMPLE)
                ).let { JudgeHistoryResponse(it, assignment) }
    }

    private fun findLatestCommitFromGithub(mission: Mission, assignment: Assignment): Commit {
        return githubApi.requestCommits(assignment)
            .map { Commit(it.hash, it.date) }
            .let(::Commits)
            .findLatestUntilEndDateTime(mission.period.endDateTime)
    }
}
