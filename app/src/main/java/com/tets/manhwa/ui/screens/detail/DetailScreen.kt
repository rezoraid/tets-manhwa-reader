package com.tets.manhwa.ui.screens.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.tets.manhwa.data.repository.TetsRepository
import com.tets.manhwa.domain.model.Chapter
import com.tets.manhwa.domain.model.Comic

@Composable
fun DetailScreen(navController: NavController, repository: TetsRepository, source: String, id: String) {
    var comic by remember { mutableStateOf<Comic?>(null) }
    var chapters by remember { mutableStateOf<List<Chapter>>(emptyList()) }
    var bookmarked by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(source, id) {
        repository.getDetail(source, id).fold(
            onSuccess = { comic = it; bookmarked = false },
            onFailure = { error = it.message }
        )
        repository.getChapters(source, id).fold(
            onSuccess = { chapters = it },
            onFailure = { error = it.message }
        )
    }

    val current = comic
    if (current == null) {
        Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background), contentAlignment = Alignment.Center) {
            Text(error ?: "Loading...")
        }
        return
    }

    LazyColumn(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        item {
            Box(modifier = Modifier.fillMaxWidth().height(320.dp)) {
                AsyncImage(
                    model = current.cover,
                    contentDescription = current.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color.Transparent, Color(0xCC000000)))))
                Row(
                    modifier = Modifier.align(Alignment.BottomStart).fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(current.title, color = Color.White, style = MaterialTheme.typography.headlineSmall)
                        Text(current.genres.joinToString(" • "), color = Color(0xFFD8D8D8))
                    }
                    IconButton(onClick = {
                        bookmarked = repository.toggleBookmark(current)
                    }) {
                        Icon(
                            if (bookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "Bookmark",
                            tint = Color.White
                        )
                    }
                }
            }
        }
        item {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = {
                    val firstChapter = chapters.firstOrNull()?.number ?: "1"
                    navController.navigate("reader/${current.source}/${current.id}/${firstChapter}")
                }) { Text("Read") }
                Text(current.synopsis ?: "No synopsis available.", color = MaterialTheme.colorScheme.onSurface)
                Text("Author: ${current.author ?: "Unknown"}")
                Text("Status: ${current.status ?: "Unknown"}")
                Text("Type: ${current.type ?: "Unknown"}")
            }
        }
        item {
            Text("Chapters", modifier = Modifier.padding(start = 16.dp, top = 12.dp, bottom = 8.dp), style = MaterialTheme.typography.titleMedium)
        }
        items(chapters) { chapter ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp).clip(RoundedCornerShape(10.dp)).background(MaterialTheme.colorScheme.surface).padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Chapter ${chapter.number}")
                Button(onClick = { navController.navigate("reader/${current.source}/${current.id}/${chapter.number}") }) {
                    Text("Open")
                }
            }
        }
    }
}
