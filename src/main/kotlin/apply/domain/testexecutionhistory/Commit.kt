package apply.domain.testexecutionhistory

import com.fasterxml.jackson.annotation.JsonAlias
import java.time.LocalDateTime

class Commit(
    @JsonAlias("sha")
    val commitId: String,
    private val commit: InnerCommit,
) {
    val date: LocalDateTime
        get() = commit.committer.date

    constructor(commitId: String, date: LocalDateTime) : this(commitId, InnerCommit(Committer(date)))
}

class InnerCommit(
    val committer: Committer
)

class Committer(
    val date: LocalDateTime
)
