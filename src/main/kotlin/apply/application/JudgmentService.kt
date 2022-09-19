package apply.application

import apply.application.github.GithubApi
import apply.application.judgmentserver.JudgmentRequest
import apply.domain.assignment.Assignment
import apply.domain.assignment.AssignmentRepository
import apply.domain.assignment.getByUserIdAndMissionId
import apply.domain.judgment.Commit
import apply.domain.judgment.JudgmentHistory
import apply.domain.judgment.JudgmentHistoryRepository
import apply.domain.judgment.JudgmentItemRepository
import apply.domain.judgment.JudgmentType
import apply.domain.judgment.findLastByUserIdAndMissionIdAndJudgmentType
import apply.domain.judgment.getByMissionId
import apply.domain.judgmentserver.JudgmentServer
import apply.domain.mission.Mission
import apply.domain.mission.MissionRepository
import apply.domain.mission.getById
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class JudgmentService(
    private val missionRepository: MissionRepository,
    private val assignmentRepository: AssignmentRepository,
    private val judgmentItemRepository: JudgmentItemRepository,
    private val judgmentHistoryRepository: JudgmentHistoryRepository,
    private val githubApi: GithubApi,
    private val judgmentServer: JudgmentServer
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
        val lastHistory = judgmentHistoryRepository
            .findLastByUserIdAndMissionIdAndJudgmentType(mission.id, assignment.userId, judgmentType)

        val latestCommit = findLatestCommit(assignment.pullRequestUrl, mission.period.endDateTime)

        return (
            lastHistory?.takeIf { it.isCompleted(latestCommit.hash) }
                ?: requestAndSave(assignment, latestCommit, judgmentType)
            ).let { JudgmentHistoryResponse(it, assignment.pullRequestUrl) }
    }

    private fun requestAndSave(assignment: Assignment, commit: Commit, judgmentType: JudgmentType): JudgmentHistory {
        val requestKey = requestJudgment(assignment.missionId, commit, judgmentType)
        return judgmentHistoryRepository.save(
            JudgmentHistory(
                assignment.userId,
                assignment.missionId,
                requestKey,
                commit.hash,
                judgmentType
            )
        )
    }

    private fun requestJudgment(missionId: Long, commit: Commit, judgmentType: JudgmentType): String {
        val item = judgmentItemRepository.getByMissionId(missionId)
            .also { if (!it.isValid()) throw IllegalArgumentException("자동채점 항목이 유효하지 않습니다.") }

        return judgmentServer.requestJudgement(
            JudgmentRequest(
                item.testName!!,
                judgmentType,
                commit.pullRequestUrl,
                commit.hash,
                item.programmingLanguage!!
            )
        )
    }

    private fun findLatestCommit(pullRequestUrl: String, endDateTime: LocalDateTime): Commit {
        return githubApi.requestLatestCommit(pullRequestUrl, endDateTime)
            .let { Commit(it.hash, pullRequestUrl) }
    }
}
