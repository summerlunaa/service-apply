package apply.domain.testexecutionhistory

import java.time.LocalDateTime

class Commits(private val commits: List<Commit>) {

    fun findLatestUntilEndDateTime(endDateTime: LocalDateTime): Commit {
        return commits.filter { it.date.isBefore(endDateTime) }
            .maxByOrNull { it.date } ?: throw IllegalArgumentException("")
    }
}
