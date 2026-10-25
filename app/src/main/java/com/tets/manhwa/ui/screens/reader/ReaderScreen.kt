package com.tets.manhwa.ui.screens.reader

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.tets.manhwa.data.repository.TetsRepository
import com.tets.manhwa.domain.model.Chapter

@Composable
fun ReaderScreen(navController: NavController, repository: TetsRepository, source: String, id: String, chapter: String) {
    var current by remember { mutableStateOf<Chapter?>(null) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(source, id, chapter) {
        repository.getChapter(source, id, chapter).fold(
            onSuccess = { current = it },
            onFailure = { error = it.message ?: "Unable to load chapter" }
        )
    }

    val chapterData = current
    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Text("Chapter ${chapterData?.number ?: chapter}")
            IconButton(onClick = { /* next */ }) {
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next")
            }
        }

        if (error != null) {
            Text(error ?: "Error", color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(16.dp))
        } else if (chapterData == null) {
            Text("Loading chapter...", modifier = Modifier.padding(16.dp))
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                itemsIndexed(chapterData.images) { _, page ->
                    Box(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).clip(RoundedCornerShape(8.dp))) {
                        AsyncImage(
                            model = page.image,
                            contentDescription = "Chapter page ${page.index}",
                            modifier = Modifier.fillMaxWidth().heightIn(min = 220.dp),
                            contentScale = ContentScale.FillWidth
                        )
                    }
                }
                if (chapterData.novelContent != null && chapterData.novelContent.paragraphs.isNotEmpty()) {
                    item {
                        chapterData.novelContent.paragraphs.forEach { paragraph ->
                            Text(paragraph, modifier = Modifier.padding(16.dp))
                        }
                    }
                }
            }
        }
    }
}
