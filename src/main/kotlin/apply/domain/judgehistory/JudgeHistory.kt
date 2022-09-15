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
    val testType: JudgeTestType,

    @Column
    @Embedded
    val result: JudgeResult? = null,

    id: Long = 0L
) : BaseEntity(id) {
    fun isCommitIdEqual(commit: Commit): Boolean {
        return this.commitHash == commit.commitHash
    }
}
