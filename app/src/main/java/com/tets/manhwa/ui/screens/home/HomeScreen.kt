package com.tets.manhwa.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.tets.manhwa.data.repository.TetsRepository
import com.tets.manhwa.domain.model.Comic
import com.tets.manhwa.domain.model.ComicSection
import com.tets.manhwa.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, repository: TetsRepository) {
    var sections by remember { mutableStateOf<List<ComicSection>>(emptyList()) }
    var error by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        loading = true
        repository.getHome().fold(
            onSuccess = { sections = it; error = null },
            onFailure = { error = it.message ?: "Unable to load home" }
        )
        loading = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        TopAppBar(
            title = {
                Text("Tets Reader", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            },
            actions = {
                IconButton(onClick = { navController.navigate(Screen.Search.route) }) {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                }
                IconButton(onClick = { /* unsupported */ }) {
                    Icon(Icons.Default.Notifications, contentDescription = "Notifications")
                }
            }
        )

        if (loading) {
            Box(modifier = Modifier.fillMaxWidth().height(320.dp).padding(16.dp).clip(RoundedCornerShape(18.dp)).background(Color(0xFF1A1A1A)))
        } else if (error != null) {
            Text(
                text = error ?: "Something went wrong",
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.error
            )
        } else if (sections.isNotEmpty()) {
            val featured = sections.first().items.firstOrNull()
            if (featured != null) {
                FeaturedHero(comic = featured, onRead = {
                    navController.navigate("detail/${featured.source}/${featured.id}")
                })
            }

            sections.forEach { section ->
                if (section.items.isNotEmpty()) {
                    SectionHeader(title = section.title, onViewAll = { navController.navigate(Screen.Explore.route) })
                    ComicStrip(items = section.items, onClick = { comic ->
                        navController.navigate("detail/${comic.source}/${comic.id}")
                    })
                }
            }
        }
    }
}

@Composable
private fun FeaturedHero(comic: Comic, onRead: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(340.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(Color.DarkGray)
    ) {
        AsyncImage(
            model = comic.cover,
            contentDescription = comic.title,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(colors = listOf(Color.Transparent, Color(0xAA000000))))
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(20.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(text = "Featured", color = Color(0xFFE7E7E7), style = MaterialTheme.typography.labelMedium)
            Text(text = comic.title, color = Color.White, style = MaterialTheme.typography.headlineSmall, maxLines = 2)
            Text(text = comic.genres.joinToString(" • "), color = Color(0xFFD3D3D3), style = MaterialTheme.typography.bodyMedium)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = onRead) { Text("Read") }
                Button(onClick = { /* detail */ }, modifier = Modifier) { Text("Details") }
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String, onViewAll: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Text("View All", modifier = Modifier.clickable(onClick = onViewAll), color = MaterialTheme.colorScheme.primary)
    }
}

@Composable
private fun ComicStrip(items: List<Comic>, onClick: (Comic) -> Unit) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(items, key = { it.id }) { comic ->
            Column(
                modifier = Modifier.width(120.dp).clickable { onClick(comic) },
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AsyncImage(
                    model = comic.cover,
                    contentDescription = comic.title,
                    modifier = Modifier.height(170.dp).fillMaxWidth().clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
                Text(comic.title, maxLines = 2, style = MaterialTheme.typography.bodyMedium)
                Text((comic.latestChapter ?: "Updated").toString(), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
