package com.example.catapp.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.catapp.features.breed_details.BreedDetailScreen
import com.example.catapp.features.breed_details.BreedDetailsViewModel
import com.example.catapp.features.breed_gallery.BreedGalleryScreen
import com.example.catapp.features.breed_gallery.BreedGalleryViewModel
import com.example.catapp.features.breed_list.BreedListScreen
import com.example.catapp.features.breed_list.BreedListViewModel
import com.example.catapp.features.leaderboard.LeaderboardScreen
import com.example.catapp.features.leaderboard.LeaderboardViewModel
import com.example.catapp.features.quiz.QuizScreen
import com.example.catapp.features.quiz.QuizViewModel
import com.example.catapp.features.register.RegisterScreen
import com.example.catapp.features.register.RegisterViewModel
import com.example.catapp.features.user_info.UserInfoScreen
import com.example.catapp.features.user_info.UserInfoViewModel

sealed class BottomNavItem(
    val route: String, val label: String, val icon: ImageVector
) {
    object BreedList : BottomNavItem("list", "Breeds", Icons.AutoMirrored.Filled.List)
    object Leaderboard : BottomNavItem("leaderboard", "Leaderboard", Icons.Default.EmojiEvents)
    object Account : BottomNavItem("account", "Account", Icons.Default.AccountCircle)
}

@Composable
fun AppNavigation(startDestination: String) {
    val navController = rememberNavController()
    val bottomNavRoutes = listOf("list", "leaderboard", "account")

    Scaffold(
        bottomBar = {
            val currentRoute =
                navController.currentBackStackEntryAsState().value?.destination?.route
            if (currentRoute in bottomNavRoutes) {
                BottomNavigationBar(navController = navController)
            }
        }) { innerPadding ->
        innerPadding
        NavHost(navController = navController, startDestination = startDestination) {
            composable("register") {
                RegisterScreen(
                    viewModel = hiltViewModel<RegisterViewModel>(), onRegisterSuccess = {
                        navController.navigate("list") {
                            popUpTo("register") { inclusive = true }
                            launchSingleTop = true
                        }
                    })
            }

            composable(BottomNavItem.BreedList.route) {
                BreedListScreen(
                    viewModel = hiltViewModel<BreedListViewModel>(),
                    onBreedClick = { id -> navController.navigateToDetails(id) },
                    startQuiz = { navController.navigate("quiz") })
            }

            composable(BottomNavItem.Leaderboard.route) {
                LeaderboardScreen(viewModel = hiltViewModel<LeaderboardViewModel>())
            }

            composable(BottomNavItem.Account.route) {
                UserInfoScreen(viewModel = hiltViewModel<UserInfoViewModel>())
            }

            composable("quiz") {
                QuizScreen(
                    viewModel = hiltViewModel<QuizViewModel>(),
                    onClose = { navController.navigateUp() })
            }

            breedDetails(
                arguments = listOf(
                    navArgument(name = Breed_ID_ARG) {
                        type = NavType.StringType
                        nullable = false
                    }), navController = navController
            )

            breedGallery(
                arguments = listOf(
                    navArgument(name = Breed_ID_ARG) {
                        type = NavType.StringType
                        nullable = false
                    }), navController = navController
            )
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem.BreedList, BottomNavItem.Leaderboard, BottomNavItem.Account
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(selected = currentRoute == item.route, onClick = {
                if (currentRoute != item.route) {
                    navController.navigate(item.route) {
                        launchSingleTop = true
                        restoreState = true
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                    }
                }
            }, icon = {
                Icon(
                    imageVector = item.icon, contentDescription = item.label
                )
            }, label = { Text(item.label) })
        }
    }
}

private fun NavGraphBuilder.breedGallery(
    arguments: List<NamedNavArgument>,
    navController: NavController,
) = composable(route = "gallery/{$Breed_ID_ARG}", arguments = arguments) {
    val viewModel = hiltViewModel<BreedGalleryViewModel>()
    BreedGalleryScreen(viewModel = viewModel, onClose = {
        navController.navigateUp()
    })
}

private fun NavGraphBuilder.breedDetails(
    arguments: List<NamedNavArgument>,
    navController: NavController,
) = composable(route = "details/{$Breed_ID_ARG}", arguments = arguments) {
    val viewModel = hiltViewModel<BreedDetailsViewModel>()
    BreedDetailScreen(
        viewModel = viewModel,
        onClose = { navController.navigateUp() },
        navigateToGallery = { id -> navController.navigateToGallery(id) })
}

private fun NavController.navigateToDetails(id: String) {
    this.navigate(route = "details/$id")
}

private fun NavController.navigateToGallery(id: String) {
    this.navigate(route = "gallery/$id")
}

const val Breed_ID_ARG = "BreedId"

val SavedStateHandle.BreedIdOrThrow: String
    get() = this.get<String>(Breed_ID_ARG) ?: error("$Breed_ID_ARG not found.")
