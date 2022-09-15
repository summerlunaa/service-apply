package apply.application.github

import apply.domain.assignment.Assignment
import apply.domain.judgehistory.Commit
import apply.domain.judgehistory.Commits
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToFlux

@Component
class ListGithubApi : GithubApi {

    private val client = WebClient.builder()
        .baseUrl("https://api.github.com/repos/")
        .build()

    override fun requestCommits(assignment: Assignment): Commits {
        val groups = parse(assignment.pullRequestUrl)

        val commits = client.get()
            .uri("${groups["organization"]?.value}/${groups["repository"]?.value}/pulls/${groups["pullRequestNumber"]?.value}/commits?per_page=$PAGE_SIZE")
            .retrieve()
            .bodyToFlux<Commit>()
            .collectList()
            .block()!!
            .toList()

        return Commits(commits)
    }

    companion object {
        const val PAGE_SIZE = 100

        private val PULL_REQUEST_URL_PATTERN =
            Regex("https://github[.]com/(?<organization>.+)/(?<repository>.+)/pull/(?<pullRequestNumber>[0-9]+)")

        fun parse(pullRequestUrl: String): MatchNamedGroupCollection {
            val matchResult = PULL_REQUEST_URL_PATTERN.find(pullRequestUrl)
                ?: throw IllegalArgumentException("pr url invalid")
            return matchResult.groups as? MatchNamedGroupCollection
                ?: throw IllegalArgumentException("failed to casting")
        }
    }
}
