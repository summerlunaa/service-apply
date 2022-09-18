package apply.domain.judgment

import org.springframework.data.jpa.repository.JpaRepository

fun JudgmentItemRepository.getByMissionId(missionId: Long): JudgmentItem = findByMissionId(missionId)
    ?: throw IllegalArgumentException("자동채점 항목이 존재하지 않습니다. missionId : $missionId")

interface JudgmentItemRepository : JpaRepository<JudgmentItem, Long> {
    fun findByMissionId(missionId: Long): JudgmentItem?
}
