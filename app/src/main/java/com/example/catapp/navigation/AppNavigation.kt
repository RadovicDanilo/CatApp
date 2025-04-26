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

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController, startDestination = "details/abys"
    ) {
        composable(route = "list") {

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
    }
}

private fun NavGraphBuilder.breadDetails(
    route: String,
    arguments: List<NamedNavArgument>,
    navController: NavController,
) = composable(route = route, arguments = arguments) { navBackStackEntry ->
    val viewModel = hiltViewModel<BreadDetailsViewModel>()
    BreadDetailScreen(
        viewModel = viewModel, onClose = {
            navController.navigateUp()
        })
}

const val BREAD_ID_ARG = "breadId"

val SavedStateHandle.breadIdOrThrow: String
    get() = this.get<String>(BREAD_ID_ARG) ?: error("$BREAD_ID_ARG not found.")
