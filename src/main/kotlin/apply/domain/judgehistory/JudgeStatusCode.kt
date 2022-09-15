package apply.domain.judgehistory

enum class JudgeStatusCode(val code: Int, val message: String) {
    OK(0, "ok"),
    INTERNAL_SERVER_ERROR(-1, "서버 내부 오류입니다");

    fun isError(): Boolean {
        return code < 0
    }
}
