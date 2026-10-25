package com.tets.manhwa.data.repository

import com.tets.manhwa.data.local.BookmarkEntity
import com.tets.manhwa.data.local.HistoryEntity
import com.tets.manhwa.data.remote.*
import com.tets.manhwa.domain.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TetsRepository(
    private val api: TetsApiService,
    private val db: com.tets.manhwa.data.local.AppDatabase
) {
    fun observeBookmarks(): Flow<List<Comic>> = db.bookmarkDao().observe().map { list ->
        list.map {
            Comic(
                id = it.id,
                source = it.source,
                title = it.title,
                cover = it.cover,
                author = it.author,
                genres = emptyList()
            )
        }
    }

    fun observeHistory(): Flow<List<HistoryEntity>> = db.historyDao().observe()

    suspend fun toggleBookmark(comic: Comic): Boolean {
        val entity = BookmarkEntity(
            id = comic.id,
            source = comic.source,
            title = comic.title,
            cover = comic.cover,
            author = comic.author,
            updatedAt = comic.updatedAt
        )
        val exists = db.bookmarkDao().isBookmarked(comic.id)
        if (exists) db.bookmarkDao().remove(comic.id) else db.bookmarkDao().insert(entity)
        return !exists
    }

    suspend fun saveHistory(comic: Comic, chapterNumber: String, progress: Int) {
        db.historyDao().insert(
            HistoryEntity(
                comicId = comic.id,
                title = comic.title,
                source = comic.source,
                cover = comic.cover,
                chapterNumber = chapterNumber,
                progress = progress
            )
        )
    }

    suspend fun clearHistory() = db.historyDao().clear()

    suspend fun getHome(): Result<List<ComicSection>> = runCatching {
        val response = api.home(source = "all", refresh = false)
        val home = response.data ?: HomeResponse()

        listOf(
            ComicSection("Trending", home.popular.map(::toDomain)),
            ComicSection("Latest Updates", home.latest.map(::toDomain)),
            ComicSection("New Arrivals", home.new.map(::toDomain))
        )
    }

    suspend fun search(query: String): Result<List<Comic>> = runCatching {
        val response = api.search(query = query, source = "all", page = 1)
        response.data?.map(::toDomain) ?: emptyList()
    }

    suspend fun getGenres(): Result<List<String>> = runCatching {
        api.genres().data ?: emptyList()
    }

    suspend fun getGenreList(genre: String): Result<List<Comic>> = runCatching {
        val response = api.genre(genre = genre, source = "all", page = 1)
        response.data?.map(::toDomain) ?: emptyList()
    }

    suspend fun getTypeList(type: String): Result<List<Comic>> = runCatching {
        val response = api.type(type = type, source = "all", page = 1)
        response.data?.map(::toDomain) ?: emptyList()
    }

    suspend fun getDetail(source: String, id: String): Result<Comic> = runCatching {
        val response = api.comicDetail(source, id)
        val detail = response.data ?: throw IllegalStateException("Empty detail")
        toDomain(detail)
    }

    suspend fun getChapters(source: String, id: String): Result<List<Chapter>> = runCatching {
        val response = api.chapters(source, id)
        response.data?.map(::toDomain) ?: emptyList()
    }

    suspend fun getChapter(source: String, id: String, chapter: String): Result<Chapter> = runCatching {
        val response = api.chapter(source, id, chapter)
        val dto = response.data ?: throw IllegalStateException("Empty chapter")
        toDomain(dto)
    }

    private fun toDomain(dto: ComicDto): Comic = Comic(
        id = dto.id,
        source = dto.source,
        title = dto.title,
        type = dto.type,
        status = dto.status,
        author = dto.author,
        artist = dto.artist,
        cover = dto.cover,
        rawCover = dto.rawCover,
        genres = dto.genres,
        synopsis = dto.synopsis,
        alternativeTitles = dto.alternativeTitles,
        latestChapter = dto.latestChapter,
        rating = dto.rating?.let { Rating(it.value, it.count) },
        updatedAt = dto.updatedAt
    )

    private fun toDomain(dto: ComicDetailDto): Comic = Comic(
        id = dto.id,
        source = dto.source,
        title = dto.title,
        type = dto.type,
        status = dto.status,
        author = dto.author,
        artist = dto.artist,
        cover = dto.cover,
        rawCover = dto.rawCover,
        genres = dto.genres,
        synopsis = dto.synopsis,
        alternativeTitles = dto.alternativeTitles,
        latestChapter = dto.latestChapter,
        rating = dto.rating?.let { Rating(it.value, it.count) },
        updatedAt = dto.updatedAt
    )

    private fun toDomain(dto: ChapterDto): Chapter = Chapter(
        id = dto.number,
        number = dto.number,
        title = dto.title,
        type = dto.type,
        totalChapters = dto.totalChapters,
        prevChapter = dto.prevChapter,
        nextChapter = dto.nextChapter,
        images = dto.images.map { ChapterPage(it.index, it.image, it.rawUrl) },
        novelContent = dto.novelContent?.let { NovelContent(it.paragraphs) }
    )
}
