package apply.domain.judgehistory

import org.springframework.data.jpa.repository.JpaRepository

fun JudgeHistoryRepository.findLastByUserIdAndMissionId(userId: Long, missionId: Long): JudgeHistory? =
    findFirstByUserIdAndMissionIdOrderByIdDesc(userId, missionId)

interface JudgeHistoryRepository : JpaRepository<JudgeHistory, Long> {
    fun findFirstByUserIdAndMissionIdOrderByIdDesc(userId: Long, missionId: Long): JudgeHistory?
}
