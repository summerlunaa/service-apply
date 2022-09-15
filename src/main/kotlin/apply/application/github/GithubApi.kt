package apply.application.github

import apply.domain.assignment.Assignment
import apply.domain.judgehistory.Commits

interface GithubApi {
    fun requestCommits(assignment: Assignment): Commits
}
