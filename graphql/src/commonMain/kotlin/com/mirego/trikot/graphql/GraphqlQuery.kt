package com.mirego.trikot.graphql

import com.mirego.trikot.foundation.CommonJSExport
import kotlinx.serialization.DeserializationStrategy

@CommonJSExport
interface GraphqlQuery<T> {
    val deserializer: DeserializationStrategy<T>
    val requestBody: String
}

@CommonJSExport
abstract class AbstractGraphqlQuery<T>(override val deserializer: DeserializationStrategy<T>) :
    GraphqlQuery<T> {
    abstract val query: String
    open val variables: Map<String, Any>? = null
    private val escapedQuery: String
        get() {
            return query
                .trimIndent()
                .jsonEscape()
        }

    override val requestBody: String
        get() {
            val stringBuilder = StringBuilder()
            stringBuilder.append("{")
            stringBuilder.append("\"query\": \"$escapedQuery\"")
            variables?.let {
                stringBuilder.append(",\"variables\": {")
                stringBuilder.append(
                    it.map { entry -> "\"${entry.key}\":${entryJson(entry)}" }.joinToString(
                        ","
                    )
                )
                stringBuilder.append("}")
            }
            stringBuilder.append("}")
            return stringBuilder.toString()
        }

    private fun entryJson(entry: Map.Entry<String, Any>): String {
        return anyToJson(entry.value)
    }

    private fun anyToJson(any: Any): String {
        return when (any) {
            is List<*> -> listJson(any)
            is Int,
            is Boolean -> "$any"
            is GraphqlJsonObject -> any.body
            else -> "\"${any.toString().jsonEscape()}\""
        }
    }

    private fun listJson(list: List<*>): String {
        return "[${list.filterNotNull().joinToString { anyToJson(it) }}]"
    }
}

@CommonJSExport
fun String.jsonEscape(): String {
    return this.replace("\\", "\\\\")
        .replace("\"", "\\\"")
        .replace("\n", " ")
        .replace("\b", " ")
        .replace("\r", " ")
        .replace("\t", " ")
}
