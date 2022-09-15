package apply.domain.judgehistory

import com.fasterxml.jackson.annotation.JsonAlias
import java.time.LocalDateTime

class Commit(
    @JsonAlias("sha")
    val commitHash: String,
    private val commit: InnerCommit,
) {
    val date: LocalDateTime
        get() = commit.committer.date

    constructor(commitHash: String, date: LocalDateTime) : this(commitHash, InnerCommit(Committer(date)))
}

class InnerCommit(
    val committer: Committer
)

class Committer(
    val date: LocalDateTime
)
