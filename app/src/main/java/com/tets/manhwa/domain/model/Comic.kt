package com.tets.manhwa.domain.model

import com.google.gson.annotations.SerializedName

data class Comic(
    val id: String = "",
    val source: String = "",
    val title: String = "",
    val type: String? = null,
    val status: String? = null,
    val author: String? = null,
    val artist: String? = null,
    val cover: String? = null,
    val rawCover: String? = null,
    val genres: List<String> = emptyList(),
    val synopsis: String? = null,
    val alternativeTitles: List<String> = emptyList(),
    val latestChapter: String? = null,
    val rating: Rating? = null,
    val updatedAt: String? = null
)

data class Rating(
    val value: Double? = null,
    val count: Int? = null
)

data class Chapter(
    val id: String = "",
    val number: String = "",
    val title: String? = null,
    val type: String? = null,
    val totalChapters: Int? = null,
    val prevChapter: String? = null,
    val nextChapter: String? = null,
    val images: List<ChapterPage> = emptyList(),
    val novelContent: NovelContent? = null
)

data class ChapterPage(
    val index: Int = 0,
    val image: String = "",
    val rawUrl: String? = null
)

data class NovelContent(
    val paragraphs: List<String> = emptyList()
)

data class ComicSection(
    val title: String,
    val items: List<Comic>
)
