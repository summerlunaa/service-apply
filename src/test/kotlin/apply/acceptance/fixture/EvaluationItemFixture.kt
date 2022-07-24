package apply.acceptance.fixture

class EvaluationItem(
    val title: String,
    val maximumScore: Int,
    val position: Int,
    val description: String
)

class EvaluationItemBuilder {
    var title: String = "README.md 파일에 기능 목록이 추가되어 있는가?"
    var maximumScore: Int = 10
    var position: Int = 1
    var description: String = "[리뷰 절차]\n" +
        "https://github.com/woowacourse/woowacourse-docs/tree/master/precourse"

    fun build(): EvaluationItem {
        return EvaluationItem(title, maximumScore, position, description)
    }
}

class EvaluationItemsBuilder {
    private var evaluationItems = mutableListOf<EvaluationItem>()

    fun evaluationItem(builder: EvaluationItemBuilder.() -> Unit) {
        evaluationItems.add(EvaluationItemBuilder().apply(builder).build())
    }

    fun evaluationItem() {
        evaluationItems.add(EvaluationItemBuilder().build())
    }

    fun build(): List<EvaluationItem> {
        return evaluationItems
    }
}

fun evaluationItems(builder: EvaluationItemsBuilder.() -> Unit): List<EvaluationItem> {
    return EvaluationItemsBuilder().apply(builder).build()
}
