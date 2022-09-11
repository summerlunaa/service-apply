package apply.domain.assignment

import org.springframework.data.jpa.repository.JpaRepository

fun AssignmentRepository.getByUserIdAndMissionId(userId: Long, missionId: Long): Assignment {
    return findByUserIdAndMissionId(userId, missionId)
        ?: throw IllegalArgumentException("해당 과제 제출물이 존재하지 않습니다. user id: $userId, mission id: $missionId")
}

interface AssignmentRepository : JpaRepository<Assignment, Long> {
    fun existsByUserIdAndMissionId(userId: Long, missionId: Long): Boolean
    fun findByUserIdAndMissionId(userId: Long, missionId: Long): Assignment?
    fun findAllByUserId(userId: Long): List<Assignment>
    fun findAllByMissionId(missionId: Long): List<Assignment>
}
