package com.tets.manhwa.ui.screens.explore

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.tets.manhwa.data.repository.TetsRepository
import com.tets.manhwa.domain.model.Comic

@Composable
fun ExploreScreen(navController: NavController, repository: TetsRepository) {
    var query by remember { mutableStateOf(TextFieldValue("")) }
    var results by remember { mutableStateOf<List<Comic>>(emptyList()) }
    var error by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(false) }

    LaunchedEffect(query.text) {
        if (query.text.isBlank()) {
            results = emptyList()
            return@LaunchedEffect
        }
        loading = true
        repository.search(query.text).fold(
            onSuccess = { results = it; error = null },
            onFailure = { error = it.message ?: "Search failed" }
        )
        loading = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Search manhwa") }
        )

        if (loading) {
            Text("Loading...")
        } else if (error != null) {
            Text(error ?: "Error", color = MaterialTheme.colorScheme.error)
        } else if (results.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No results yet")
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                state = rememberLazyGridState()
            ) {
                items(results, key = { it.id }) { comic ->
                    ComicCard(comic = comic, onClick = {
                        navController.navigate("detail/${comic.source}/${comic.id}")
                    })
                }
            }
        }
    }
}

@Composable
private fun ComicCard(comic: Comic, onClick: () -> Unit) {
    Column(modifier = androidx.compose.ui.Modifier.clickable { onClick() }) {
        AsyncImage(
            model = comic.cover,
            contentDescription = comic.title,
            modifier = Modifier.height(220.dp).fillMaxWidth().clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )
        Text(comic.title, maxLines = 2)
    }
}
