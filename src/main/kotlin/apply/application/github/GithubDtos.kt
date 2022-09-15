package apply.application.github

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.annotation.JsonDeserialize

private class CommitDeserializer : JsonDeserializer<CommitResponse>() {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): CommitResponse {
        val jsonNode: JsonNode = p.codec.readTree(p)
        return CommitResponse(
            jsonNode["sha"].asText(),
            jsonNode["commit"]["committer"]["date"].asText()
        )
    }
}

@JsonDeserialize(using = CommitDeserializer::class)
data class CommitResponse(val hash: String, val date: String)
