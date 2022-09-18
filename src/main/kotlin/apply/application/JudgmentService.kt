package apply.application

import apply.application.github.GithubApi
import apply.application.judgmentserver.JudgmentRequest
import apply.domain.assignment.Assignment
import apply.domain.assignment.AssignmentRepository
import apply.domain.assignment.getByUserIdAndMissionId
import apply.domain.judgment.Commit
import apply.domain.judgment.Commits
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
        return judge(missionId, userId, JudgmentType.EXAMPLE)
    }

    fun judgeReal(missionId: Long, userId: Long): JudgmentHistoryResponse {
        return judge(missionId, userId, JudgmentType.REAL)
    }

    private fun judge(missionId: Long, userId: Long, judgmentType: JudgmentType): JudgmentHistoryResponse {
        val mission = missionRepository.getById(missionId)
        val assignment = assignmentRepository.getByUserIdAndMissionId(userId, missionId)
        val lastHistory =
            judgmentHistoryRepository.findLastByUserIdAndMissionIdAndJudgmentType(userId, missionId, judgmentType)

        if (judgmentType == JudgmentType.EXAMPLE) {
            check(mission.isSubmitting) { "예제 테스트 실행 가능 시간이 아닙니다." }
        }

        val history = getOrCreateNew(assignment, mission, lastHistory, judgmentType)
        return JudgmentHistoryResponse(history, assignment.pullRequestUrl)
    }

    private fun getOrCreateNew(
        assignment: Assignment,
        mission: Mission,
        lastHistory: JudgmentHistory?,
        judgmentType: JudgmentType
    ): JudgmentHistory {
        val latestCommit = findLatestCommit(assignment.pullRequestUrl, mission)
        if (lastHistory != null && lastHistory.isCompleted(latestCommit)) {
            return lastHistory
        }

        val requestKey = requestJudgment(assignment, judgmentType, latestCommit)
        return judgmentHistoryRepository.save(
            JudgmentHistory(assignment.userId, assignment.missionId, requestKey, latestCommit.hash, judgmentType)
        )
    }

    private fun findLatestCommit(pullRequestUrl: String, mission: Mission): Commit {
        return githubApi.requestCommits(pullRequestUrl)
            .map { Commit(it.hash, it.date) }
            .let(::Commits)
            .findLatestUntilEndDateTime(mission.period.endDateTime)
    }

    private fun requestJudgment(
        assignment: Assignment,
        judgmentType: JudgmentType,
        latestCommit: Commit
    ): String {
        val item = judgmentItemRepository.getByMissionId(assignment.missionId)
            .also { if (!it.isValid()) throw IllegalArgumentException("자동채점 항목이 유효하지 않습니다.") }
        return judgmentServer.requestJudgement(
            JudgmentRequest(
                item.testName!!,
                judgmentType,
                assignment.pullRequestUrl,
                latestCommit.hash,
                item.programmingLanguage!!
            )
        )
    }
}
