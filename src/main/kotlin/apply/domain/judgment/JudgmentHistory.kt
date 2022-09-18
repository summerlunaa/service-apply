package apply.domain.judgment

import support.domain.BaseEntity
import javax.persistence.Column
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated

@Entity
class JudgmentHistory(
    @Column(nullable = false)
    val userId: Long,

    @Column(nullable = false)
    val missionId: Long,

    @Column(nullable = false, columnDefinition = "char(36)")
    val requestKey: String,

    @Column(nullable = false, columnDefinition = "char(40)")
    val commitHash: String,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val judgmentType: JudgmentType,

    @Column
    @Embedded
    val result: JudgmentResult? = null,

    id: Long = 0L
) : BaseEntity(id) {
    fun isCompleted(commit: Commit): Boolean =
        isCommitHashEqual(commit) && isResultOK() && isExampleType()

    private fun isCommitHashEqual(commit: Commit) = this.commitHash == commit.hash

    private fun isResultOK(): Boolean = result?.isOK ?: false

    private fun isExampleType() = judgmentType == JudgmentType.EXAMPLE
}