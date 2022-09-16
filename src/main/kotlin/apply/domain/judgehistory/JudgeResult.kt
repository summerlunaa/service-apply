package apply.domain.judgehistory

import javax.persistence.Column
import javax.persistence.Embeddable
import javax.persistence.EnumType
import javax.persistence.Enumerated

@Embeddable
class JudgeResult(
    @Column
    val passCount: Int,

    @Column
    val totalCount: Int,

    @Column
    @Enumerated(EnumType.STRING)
    val statusCode: JudgeStatusCode
) {
    val isOK: Boolean
        get() = !statusCode.isError()
}
