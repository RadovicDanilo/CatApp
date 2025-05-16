package com.example.catapp.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.catapp.breed_details.BreadDetailScreen
import com.example.catapp.breed_details.BreadDetailsViewModel
import com.example.catapp.breed_gallery.BreadGalleryScreen
import com.example.catapp.breed_gallery.BreadGalleryViewModel
import com.example.catapp.breed_list.BreadListScreen
import com.example.catapp.breed_list.BreadListViewModel
import com.example.catapp.register.RegisterScreen
import com.example.catapp.register.RegisterViewModel

private fun NavController.navigateToDetails(id: String) {
    this.navigate(route = "details/$id")
}

private fun NavController.navigateToGallery(id: String) {
    this.navigate(route = "gallery/$id")
}

@Composable
fun AppNavigation(startDestination: String) {
    val navController = rememberNavController()

    NavHost(
        navController = navController, startDestination = startDestination
    ) {

        composable(route = "register") {
            RegisterScreen(
                viewModel = hiltViewModel<RegisterViewModel>(),
            )
        }

        composable(route = "list") {
            BreadListScreen(viewModel = hiltViewModel<BreadListViewModel>(), onBreadClick = { id ->
                navController.navigateToDetails(id = id)
            })
        }

        breadDetails(
            route = "details/{$BREAD_ID_ARG}",
            arguments = listOf(
                navArgument(name = BREAD_ID_ARG) {
                    type = NavType.StringType
                    nullable = false
                }),
            navController = navController,
        )

        breadGallery(
            route = "gallery/{$BREAD_ID_ARG}",
            arguments = listOf(
                navArgument(name = BREAD_ID_ARG) {
                    type = NavType.StringType
                    nullable = false
                }),
            navController = navController,
        )
    }
}

private fun NavGraphBuilder.breadGallery(
    route: String,
    arguments: List<NamedNavArgument>,
    navController: NavController,
) = composable(route = route, arguments = arguments) { navBackStackEntry ->
    val viewModel = hiltViewModel<BreadGalleryViewModel>()
    BreadGalleryScreen(viewModel = viewModel, onClose = {
        navController.navigateUp()
    }, navigateToGallery = { id ->
        navController.navigateToGallery(id)
    })
}

private fun NavGraphBuilder.breadDetails(
    route: String,
    arguments: List<NamedNavArgument>,
    navController: NavController,
) = composable(route = route, arguments = arguments) { navBackStackEntry ->
    val viewModel = hiltViewModel<BreadDetailsViewModel>()
    BreadDetailScreen(viewModel = viewModel, onClose = {
        navController.navigateUp()
    }, navigateToGallery = { id ->
        navController.navigateToGallery(id)
    })
}

const val BREAD_ID_ARG = "breadId"

val SavedStateHandle.breadIdOrThrow: String
    get() = this.get<String>(BREAD_ID_ARG) ?: error("$BREAD_ID_ARG not found.")
