package apply.application.github

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import support.parseBy
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private class CommitDeserializer : JsonDeserializer<CommitResponse>() {
    override fun deserialize(parser: JsonParser, context: DeserializationContext): CommitResponse {
        val jsonNode: JsonNode = parser.codec.readTree(parser)
        return CommitResponse(
            jsonNode["sha"].asText(),
            jsonNode["commit"]["committer"]["date"].asText().parseBy(DateTimeFormatter.ISO_DATE_TIME)
        )
    }
}

@JsonDeserialize(using = CommitDeserializer::class)
data class CommitResponse(val hash: String, val date: LocalDateTime)
