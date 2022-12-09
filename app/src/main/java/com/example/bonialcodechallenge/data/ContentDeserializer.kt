package com.example.bonialcodechallenge.data

import com.example.bonialcodechallenge.data.models.Content
import com.example.bonialcodechallenge.data.models.Retailer
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class ContentDeserializer : JsonDeserializer<Content> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Content? {
        val nestedContentElement = json.asJsonObject?.get("content")
        val id = json.asJsonObject?.get("id")?.asLong
        val contentType = json.asJsonObject?.get("contentType")?.asString
        val brochureImage = json.asJsonObject?.get("brochureImage")?.asString
        val distance = json.asJsonObject?.get("distance")?.asDouble
        val retailer = context.deserialize<Retailer>(json.asJsonObject?.get("retailer"), Retailer::class.java)
        nestedContentElement?.let {
            if (nestedContentElement.isJsonArray) {
                val nestedContents = context.deserialize<List<Content>>(nestedContentElement, List::class.java)
                return Content(id, nestedContents, contentType, brochureImage, distance, retailer)
            } else if (nestedContentElement.isJsonObject) {
                val nestedContent = context.deserialize<Content>(nestedContentElement, Content::class.java) // this line has a bug
                return Content(id, nestedContent, contentType, brochureImage, distance, retailer)
            }
        }
        return null
    }
}