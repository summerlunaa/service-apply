package apply.domain.judgment

import org.springframework.data.jpa.repository.JpaRepository

fun JudgmentHistoryRepository.findLastByUserIdAndMissionId(userId: Long, missionId: Long): JudgmentHistory? =
    findFirstByUserIdAndMissionIdOrderByIdDesc(userId, missionId)

interface JudgmentHistoryRepository : JpaRepository<JudgmentHistory, Long> {
    fun findFirstByUserIdAndMissionIdOrderByIdDesc(userId: Long, missionId: Long): JudgmentHistory?
}
