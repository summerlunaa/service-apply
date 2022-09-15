package apply.application.github

import apply.PULL_REQUEST_URL
import apply.createAssignment
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldNotBe
import support.test.IntegrationTest

@IntegrationTest
class ListGithubApiTest(
    private val githubApi: GithubApi
) : StringSpec({

    "github의 'List commits on a pull request' API를 요청한다".config(enabled = false) {
        val commits = githubApi.requestCommits(
            createAssignment(pullRequestUrl = PULL_REQUEST_URL)
        )

        commits shouldNotBe null
    }
})
