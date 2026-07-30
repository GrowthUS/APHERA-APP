package com.example.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ui.screens.*
import com.example.ui.viewmodel.SkinViewModel

object DermAiroutes {
    const val HOME = "home"
    const val SCAN = "scan"
    const val DETAIL = "detail/{scanId}"
    const val HISTORY = "history"
    const val ROUTINE = "routine"
    const val GUIDE = "guide"

    fun detailRoute(scanId: Long): String = "detail/$scanId"
}

@Composable
fun DermAINavGraph(
    viewModel: SkinViewModel,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = DermAiroutes.HOME
    ) {
        composable(DermAiroutes.HOME) {
            HomeScreen(
                viewModel = viewModel,
                onNavigateToScan = {
                    viewModel.resetScanState()
                    navController.navigate(DermAiroutes.SCAN)
                },
                onNavigateToDetail = { scanId ->
                    navController.navigate(DermAiroutes.detailRoute(scanId))
                },
                onNavigateToRoutine = {
                    navController.navigate(DermAiroutes.ROUTINE)
                },
                onNavigateToGuide = {
                    navController.navigate(DermAiroutes.GUIDE)
                },
                onNavigateToHistory = {
                    navController.navigate(DermAiroutes.HISTORY)
                }
            )
        }

        composable(DermAiroutes.SCAN) {
            ScanScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToDetail = { scanId ->
                    navController.popBackStack()
                    navController.navigate(DermAiroutes.detailRoute(scanId))
                }
            )
        }

        composable(
            route = DermAiroutes.DETAIL,
            arguments = listOf(navArgument("scanId") { type = NavType.LongType })
        ) { backStackEntry ->
            val scanId = backStackEntry.arguments?.getLong("scanId") ?: 0L
            AnalysisDetailScreen(
                scanId = scanId,
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToRoutine = { navController.navigate(DermAiroutes.ROUTINE) }
            )
        }

        composable(DermAiroutes.HISTORY) {
            HistoryScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToDetail = { scanId ->
                    navController.navigate(DermAiroutes.detailRoute(scanId))
                }
            )
        }

        composable(DermAiroutes.ROUTINE) {
            RoutineTrackerScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(DermAiroutes.GUIDE) {
            SkinGuideScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
