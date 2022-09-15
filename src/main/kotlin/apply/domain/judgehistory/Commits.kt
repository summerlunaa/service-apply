package apply.domain.judgehistory

import java.time.LocalDateTime

class Commits(private val commits: List<Commit>) {

    fun findLatestUntilEndDateTime(endDateTime: LocalDateTime): Commit {
        return commits.filter { it.date.isBefore(endDateTime) }
            .maxByOrNull { it.date } ?: throw IllegalArgumentException("과제 제출 마감 시간 이전에 제출된 커밋이 없습니다.")
    }
}
