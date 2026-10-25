package com.tets.manhwa.ui.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.tets.manhwa.data.repository.TetsRepository
import com.tets.manhwa.ui.screens.detail.DetailScreen
import com.tets.manhwa.ui.screens.explore.ExploreScreen
import com.tets.manhwa.ui.screens.home.HomeScreen
import com.tets.manhwa.ui.screens.library.LibraryScreen
import com.tets.manhwa.ui.screens.profile.ProfileScreen
import com.tets.manhwa.ui.screens.reader.ReaderScreen
import com.tets.manhwa.ui.screens.search.SearchScreen

sealed class Screen(val route: String, val title: String) {
    data object Home : Screen("home", "Home")
    data object Explore : Screen("explore", "Explore")
    data object Search : Screen("search", "Search")
    data object Library : Screen("library", "Library")
    data object Profile : Screen("profile", "Profile")
    data object Detail : Screen("detail", "Detail")
    data object Reader : Screen("reader", "Reader")
}

@Composable
fun TetsNavHost(navController: NavHostController, repository: TetsRepository) {
    val bottomItems = listOf(
        Screen.Home,
        Screen.Explore,
        Screen.Library,
        Screen.Profile
    )

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.onBackground
            ) {
                bottomItems.forEach { screen ->
                    val selected = currentRoute == screen.route
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            if (!selected) navController.navigate(screen.route) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            val icon = when (screen) {
                                Screen.Home -> Icons.Default.Home
                                Screen.Explore -> Icons.Default.Search
                                Screen.Library -> Icons.Default.LibraryBooks
                                Screen.Profile -> Icons.Default.Person
                                else -> Icons.Default.Star
                            }
                            Icon(icon, contentDescription = screen.title)
                        },
                        label = { Text(screen.title) }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = androidx.compose.ui.Modifier.padding(bottom = paddingValues.calculateBottomPadding())
        ) {
            composable(Screen.Home.route) {
                HomeScreen(navController = navController, repository = repository)
            }
            composable(Screen.Explore.route) {
                ExploreScreen(navController = navController, repository = repository)
            }
            composable(Screen.Search.route) {
                SearchScreen(navController = navController, repository = repository)
            }
            composable(Screen.Library.route) {
                LibraryScreen(navController = navController, repository = repository)
            }
            composable(Screen.Profile.route) {
                ProfileScreen(navController = navController)
            }
            composable("detail/{source}/{id}") { backStackEntry ->
                val source = backStackEntry.arguments?.getString("source") ?: ""
                val id = backStackEntry.arguments?.getString("id") ?: ""
                DetailScreen(
                    navController = navController,
                    repository = repository,
                    source = source,
                    id = id
                )
            }
            composable("reader/{source}/{id}/{chapter}") { backStackEntry ->
                val source = backStackEntry.arguments?.getString("source") ?: ""
                val id = backStackEntry.arguments?.getString("id") ?: ""
                val chapter = backStackEntry.arguments?.getString("chapter") ?: ""
                ReaderScreen(navController = navController, repository = repository, source = source, id = id, chapter = chapter)
            }
        }
    }
}
