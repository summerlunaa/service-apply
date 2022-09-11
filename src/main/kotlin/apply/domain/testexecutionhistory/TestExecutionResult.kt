package apply.domain.testexecutionhistory

import javax.persistence.Column

class TestExecutionResult(
    @Column
    val correctCount: Int,

    @Column
    val totalCount: Int,

    @Column
    val message: String
)
