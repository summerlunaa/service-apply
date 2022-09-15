package apply.domain.judgehistory

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private fun String.parseBy(formatter: DateTimeFormatter) = LocalDateTime.parse(this, formatter)

class Commit(
    val hash: String,
    val dateTime: LocalDateTime
) {
    constructor(hash: String, date: String) : this(
        hash, date.parseBy(DateTimeFormatter.ISO_DATE_TIME)
    )
}
