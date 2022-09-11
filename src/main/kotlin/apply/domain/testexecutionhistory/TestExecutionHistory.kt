package apply.domain.testexecutionhistory

import support.domain.BaseEntity
import javax.persistence.Column
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated

@Entity
class TestExecutionHistory(
    @Column(nullable = false)
    val userId: Long,

    @Column(nullable = false)
    val missionId: Long,

    @Column(nullable = false)
    val requestKey: Long,

    @Column(nullable = false)
    val commitId: String,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val testType: TestExecutionType,

    @Column
    @Embedded
    val result: TestExecutionResult,

    id: Long = 0L
) : BaseEntity(id) {

    fun isCommitIdEqual(commit: Commit?): Boolean {
        return this.commitId == commit?.commitId
    }
}
