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
import com.example.catapp.features.breed_details.BreedDetailScreen
import com.example.catapp.features.breed_details.BreedDetailsViewModel
import com.example.catapp.features.breed_gallery.BreedGalleryScreen
import com.example.catapp.features.breed_gallery.BreedGalleryViewModel
import com.example.catapp.features.breed_list.BreedListScreen
import com.example.catapp.features.breed_list.BreedListViewModel
import com.example.catapp.features.register.RegisterScreen
import com.example.catapp.features.register.RegisterViewModel

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
            BreedListScreen(viewModel = hiltViewModel<BreedListViewModel>(), onBreedClick = { id ->
                navController.navigateToDetails(id = id)
            })
        }

        breedDetails(
            arguments = listOf(
                navArgument(name = Breed_ID_ARG) {
                    type = NavType.StringType
                    nullable = false
                }),
            navController = navController,
        )

        breedGallery(
            arguments = listOf(
                navArgument(name = Breed_ID_ARG) {
                    type = NavType.StringType
                    nullable = false
                }),
            navController = navController,
        )
    }
}

private fun NavGraphBuilder.breedGallery(
    arguments: List<NamedNavArgument>,
    navController: NavController,
) = composable(route = "gallery/{$Breed_ID_ARG}", arguments = arguments) { navBackStackEntry ->
    val viewModel = hiltViewModel<BreedGalleryViewModel>()
    BreedGalleryScreen(viewModel = viewModel, onClose = {
        navController.navigateUp()
    })
}

private fun NavGraphBuilder.breedDetails(
    arguments: List<NamedNavArgument>,
    navController: NavController,
) = composable(route = "details/{$Breed_ID_ARG}", arguments = arguments) { navBackStackEntry ->
    val viewModel = hiltViewModel<BreedDetailsViewModel>()
    BreedDetailScreen(viewModel = viewModel, onClose = {
        navController.navigateUp()
    }, navigateToGallery = { id ->
        navController.navigateToGallery(id)
    })
}

const val Breed_ID_ARG = "BreedId"

val SavedStateHandle.BreedIdOrThrow: String
    get() = this.get<String>(Breed_ID_ARG) ?: error("$Breed_ID_ARG not found.")
