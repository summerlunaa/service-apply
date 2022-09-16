package apply.application.github

import apply.domain.assignment.Assignment
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToFlux

private fun Regex.findOrThrow(pullRequestUrl: String): MatchNamedGroupCollectionWrapper {
    return MatchNamedGroupCollectionWrapper(
        find(pullRequestUrl)
            ?: throw IllegalArgumentException("pull Request URL 의 형식이 올바르지 않습니다. URL: $pullRequestUrl")
    )
}

private fun WebClient.ResponseSpec.handleOnError(pullRequestUrl: String): WebClient.ResponseSpec {
    val message = "깃허브 API 요청에 실패했습니다. pullRequestUrl : $pullRequestUrl, statusCode = %s"
    return onStatus(HttpStatus::is4xxClientError) { throw IllegalArgumentException(message.format(it.statusCode())) }
        .onStatus(HttpStatus::is5xxServerError) { throw GithubApiException(message.format(it.statusCode())) }
}

private class MatchNamedGroupCollectionWrapper(matchResult: MatchResult) {
    private val matchNamedGroupCollection: MatchNamedGroupCollection

    init {
        matchNamedGroupCollection = matchResult.groups as MatchNamedGroupCollection
    }

    operator fun get(name: String): String {
        return matchNamedGroupCollection[name]?.value
            ?: throw IllegalArgumentException("pull Request URL 에서 $name 을 찾을 수 없습니다.")
    }
}

@Component
class GithubApi {
    private val client = WebClient.builder()
        .baseUrl(BASE_API_URL)
        .build()

    fun requestCommits(assignment: Assignment): List<CommitResponse> {
        val groups = PULL_REQUEST_URL_PATTERN.findOrThrow(assignment.pullRequestUrl)

        return client.get()
            .uri("${groups["organization"]}/${groups["repository"]}/pulls/${groups["pullRequestNumber"]}/commits?per_page=$PAGE_SIZE")
            .retrieve()
            .handleOnError(assignment.pullRequestUrl)
            .bodyToFlux<CommitResponse>()
            .toIterable()
            .toList()
    }

    companion object {
        private const val PAGE_SIZE = 100
        private const val BASE_API_URL = "https://api.github.com/repos/"
        private val PULL_REQUEST_URL_PATTERN =
            Regex("https://github[.]com/(?<organization>.+)/(?<repository>.+)/pull/(?<pullRequestNumber>[0-9]+)")
    }
}
