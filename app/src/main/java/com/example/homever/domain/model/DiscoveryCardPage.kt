package com.example.homever.domain.model

data class DiscoveryCardPage(
    val photo: String?,
    val tags: List<String>,
    val noteTitle: String? = null,
    val note: String? = null
)

fun discoveryCardPages(
    photos: List<String>,
    tags: List<String>,
    notes: List<Pair<String, String>>
): List<DiscoveryCardPage> {
    val cleanPhotos = photos.filter(String::isNotBlank).distinct()
    val cleanTags = tags.filter(String::isNotBlank).distinct()
    val noteParts = notes.flatMap { (title, text) ->
        val parts = mutableListOf<String>()
        var current = ""
        for (word in text.trim().split(Regex("\\s+")).filter(String::isNotBlank)) {
            if (current.isNotEmpty() && current.length + word.length + 1 > 160) {
                parts.add(current)
                current = ""
            }
            current = if (current.isEmpty()) word else "$current $word"
        }
        if (current.isNotEmpty()) parts.add(current)
        parts.map { title to it }
    }
    val pageCount = maxOf(1, cleanPhotos.size, (cleanTags.size + 3) / 4, noteParts.size + if (noteParts.isEmpty()) 0 else 1)
    var tagOffset = 0
    return List(pageCount) { index ->
        val tagCount = cleanTags.size / pageCount + if (index < cleanTags.size % pageCount) 1 else 0
        val pageTags = cleanTags.drop(tagOffset).take(tagCount)
        tagOffset += tagCount
        val note = noteParts.getOrNull(index - 1)
        DiscoveryCardPage(cleanPhotos.getOrNull(index % cleanPhotos.size.coerceAtLeast(1)), pageTags, note?.first, note?.second)
    }
}
