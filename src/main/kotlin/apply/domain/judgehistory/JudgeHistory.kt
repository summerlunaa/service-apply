package apply.domain.judgehistory

import support.domain.BaseEntity
import javax.persistence.Column
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated

@Entity
class JudgeHistory(
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
    val judgeType: JudgeType,

    @Column
    @Embedded
    val result: JudgeResult? = null,

    id: Long = 0L
) : BaseEntity(id) {
    fun isCompleted(commit: Commit): Boolean =
        isCommitHashEqual(commit) && isResultOK() && isExampleType()

    private fun isCommitHashEqual(commit: Commit) = this.commitHash == commit.hash

    private fun isResultOK(): Boolean = result?.isOK ?: false

    private fun isExampleType() = judgeType == JudgeType.EXAMPLE
}