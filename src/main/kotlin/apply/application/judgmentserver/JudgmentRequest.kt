package apply.application.judgmentserver

import apply.domain.judgment.JudgmentType
import apply.domain.judgment.ProgrammingLanguage

class JudgmentRequest(
    val testName: String,
    val judgmentType: JudgmentType,
    val pullRequestUrl: String,
    val commitHash: String,
    val programmingLanguage: ProgrammingLanguage
)
