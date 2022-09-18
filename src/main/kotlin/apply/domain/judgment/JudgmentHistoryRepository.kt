package apply.domain.judgment

import org.springframework.data.jpa.repository.JpaRepository

fun JudgmentHistoryRepository.findLastByUserIdAndMissionIdAndJudgmentType(
    userId: Long,
    missionId: Long,
    judgmentType: JudgmentType
): JudgmentHistory? =
    findFirstByUserIdAndMissionIdAndJudgmentTypeOrderByIdDesc(userId, missionId, judgmentType)

interface JudgmentHistoryRepository : JpaRepository<JudgmentHistory, Long> {
    fun findFirstByUserIdAndMissionIdAndJudgmentTypeOrderByIdDesc(
        userId: Long,
        missionId: Long,
        judgmentType: JudgmentType
    ): JudgmentHistory?
}
