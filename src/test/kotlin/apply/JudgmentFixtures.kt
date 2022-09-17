package apply

import apply.application.judgmentserver.JudgmentRequest
import apply.domain.judgment.JudgmentType
import apply.domain.judgment.ProgrammingLanguage

fun createJudgmentRequest(
    testName: String = "kotlin-baseball-precourse",
    judgmentType: JudgmentType = JudgmentType.EXAMPLE,
    pullRequestUrl: String = PULL_REQUEST_URL,
    commitHash: String = COMMIT_HASH,
    programmingLanguage: ProgrammingLanguage = ProgrammingLanguage.KOTLIN
): JudgmentRequest {
    return JudgmentRequest(testName, judgmentType, pullRequestUrl, commitHash, programmingLanguage)
}
