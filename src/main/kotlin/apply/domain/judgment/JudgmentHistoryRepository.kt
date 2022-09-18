package apply.domain.judgment

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

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

    @Query("select jh from JudgmentHistory jh where jh.missionId = :missionId and jh.judgmentType = 'REAL' " +
        "and jh.id in (select max(jh2.id) from JudgmentHistory jh2 group by jh2.userId)")
    fun findAllLastOfRealsByMissionId(
        missionId: Long,
    ): List<JudgmentHistory>
}
