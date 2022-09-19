package apply.application.github

import apply.PULL_REQUEST_URL
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import support.test.IntegrationTest
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private const val THRESHOLD_DATE_TIME = "2021-10-10T23:20:00"

@IntegrationTest
class GithubApiTest(
    private val githubApi: GithubApi
) : StringSpec({
    /**
     * see : https://docs.github.com/en/rest/pulls/pulls#list-commits-on-a-pull-request
     */
    "github의 API를 요청해 기준 시간의 마지막 커밋을 가져온다".config(enabled = false) {
        val thresholdDateTime = LocalDateTime.parse(THRESHOLD_DATE_TIME, DateTimeFormatter.ISO_DATE_TIME)

        val latestCommit = githubApi.requestLatestCommit(PULL_REQUEST_URL, thresholdDateTime)

        latestCommit.hash shouldBe "8c2d61313838d9220848bd38a5a5adc34efc5169"
    }
})
