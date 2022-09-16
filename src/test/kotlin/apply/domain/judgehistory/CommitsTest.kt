package apply.domain.judgehistory

import apply.createCommit
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime

class CommitsTest : StringSpec({
    "가장 최신 commit을 가져온다" {
        val commits = Commits(
            listOf(
                createCommit("hello-1", LocalDateTime.now().plusMinutes(1)),
                createCommit("hello-2", LocalDateTime.now().plusMinutes(2))
            )
        )
        val latestCommit: Commit = commits.findLatestUntilEndDateTime(
            LocalDateTime.now().plusMinutes(10)
        )

        latestCommit.hash shouldBe "hello-2"
    }

    "종료 시간 이전의 가장 최신 commit을 가져온다" {
        val commits = Commits(
            listOf(
                createCommit("hello-1", LocalDateTime.now().plusMinutes(1)),
                createCommit("hello-2", LocalDateTime.now().plusMinutes(2)),
                createCommit("hello-3", LocalDateTime.now().plusMinutes(15))
            )
        )

        val latestCommit = commits.findLatestUntilEndDateTime(
            LocalDateTime.now().plusMinutes(10)
        )

        latestCommit.hash shouldBe "hello-2"
    }
})
