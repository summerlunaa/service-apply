package apply.application.github

import apply.domain.assignment.Assignment

interface GithubApi {
    fun requestCommits(assignment: Assignment): List<CommitResponse>
}
