package moe.ore.util

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser

object JsonUtil {
    private val parser = JsonParser()

    fun fromObject(string: String) : JsonObject {
        return parser.parse(string) as JsonObject
    }

    fun fromArray(string: String) : JsonArray {
        return parser.parse(string) as JsonArray
    }

    fun from(string: String) : JsonElement {
        return parser.parse(string)
    }
}