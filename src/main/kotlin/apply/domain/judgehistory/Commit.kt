package apply.domain.judgehistory

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Commit(
    val hash: String,
    val date: LocalDateTime
) {
    constructor(hash: String, date: String) : this(
        hash, LocalDateTime.parse(date, DateTimeFormatter.ISO_DATE_TIME)
    )
}
