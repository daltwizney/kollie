package com.wizneylabs.freestyle.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.wizneylabs.freestyle.FreestyleAppViewModel

/********************************************************************
 *************************** Composables ****************************
 ********************************************************************/

@Composable
fun FreestyleApp() {

    val viewModel: FreestyleAppViewModel = viewModel();

    val navController = rememberNavController();

    AppNavigationDrawer(viewModel, navController) { innerPadding ->

        NavHost(
            modifier = Modifier
                .padding(innerPadding),
            navController = navController,
            startDestination = HomeRoute
        ) {
            composable<HomeRoute>() {

                viewModel.endEditingShader();

                MainMenu(navController, viewModel);
            }
            composable<FreestyleEditorRoute>() { backStackEntry ->

                val route: FreestyleEditorRoute = backStackEntry.toRoute();

                viewModel.editShader(route.shaderID);

                CodeEditor(navController, viewModel);
            }
        }
    }
}
