package apply.application

import apply.application.github.GithubApi
import apply.domain.assignment.Assignment
import apply.domain.assignment.AssignmentRepository
import apply.domain.assignment.getByUserIdAndMissionId
import apply.domain.judgehistory.Commit
import apply.domain.judgehistory.JudgeHistory
import apply.domain.judgehistory.JudgeHistoryRepository
import apply.domain.judgehistory.JudgeType
import apply.domain.judgehistory.findLastByUserIdAndMissionId
import apply.domain.mission.Mission
import apply.domain.mission.MissionRepository
import apply.domain.mission.getById
import apply.domain.mission.isBetween
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

private const val TEMPORARY_REQUEST_KEY = "temporary-request-key"

@Service
@Transactional
class JudgeService(
    private val missionRepository: MissionRepository,
    private val assignmentRepository: AssignmentRepository,
    private val judgeHistoryRepository: JudgeHistoryRepository,
    private val githubApi: GithubApi
) {
    fun runExampleTestCase(missionId: Long, userId: Long): JudgeHistoryResponse {
        val mission = missionRepository.getById(missionId)
        check(LocalDateTime.now().isBetween(mission.period)) { "제출 가능 시간이 아닙니다." }

        val assignment = assignmentRepository.getByUserIdAndMissionId(userId, mission.id)
        val latestCommit = findLatestCommitFromGithub(mission, assignment)
        val lastHistory = judgeHistoryRepository.findLastByUserIdAndMissionId(userId, missionId)

        return (
            lastHistory?.takeUnless { it.isJudgeable(latestCommit) }
                ?: JudgeHistory(userId, missionId, TEMPORARY_REQUEST_KEY, latestCommit.commitHash, JudgeType.EXAMPLE)
            ).let { JudgeHistoryResponse(it, assignment) }
    }

    private fun findLatestCommitFromGithub(mission: Mission, assignment: Assignment): Commit {
        return githubApi.requestCommits(assignment)
            .findLatestUntilEndDateTime(mission.period.endDateTime)
    }
}
