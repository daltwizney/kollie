package com.wizneylabs.freestyle.composables

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.wizneylabs.freestyle.FreestyleAppViewModel

@Composable
fun MainMenu(navController: NavHostController, viewModel: FreestyleAppViewModel) {

    var expandedDropdownId by remember { mutableStateOf("") };

    var showDeleteShaderDialog by remember { mutableStateOf(false) };

    if (!viewModel.isFullyLoaded.value)
    {
        AppLoadingScreen();
    }
    else
    {
        Box(modifier = Modifier.fillMaxSize()) {

            Column(modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)) {

                val shaderIDs = viewModel.shaderIDFlow.collectAsState();

                if (shaderIDs.value.size == 0)
                {
                    Text("No shaders available!");
                }
                else
                {
                    shaderIDs.value.forEach { id ->

                        Button(
                            onClick = {

                                Log.d("Freestyle Route Test", "shader ID chosen = $id");

                                navController.navigate(FreestyleEditorRoute(id));
                            },
                            modifier = Modifier
                                .pointerInput(Unit) {
                                }) {
                            Text(id);

                            Spacer(modifier = Modifier.width(8.dp));

                            IconButton(
                                onClick = {
                                    expandedDropdownId = id;
                                    Log.d("Button", "3 dot button clicked! id = $id") },
                                modifier = Modifier.height(24.dp).width(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = "More options"
                                )

                                if (showDeleteShaderDialog &&
                                    id == expandedDropdownId)
                                {
                                    DeleteShaderDialog(
                                        "Confirm Shader Deletion",
                                        "Delete shader ID?\n ${id}"
                                    ) { doDelete ->

                                        if (doDelete)
                                        {
                                            viewModel.deleteShader(id);
                                        }

                                        showDeleteShaderDialog = false;
                                        expandedDropdownId = "";
                                    }
                                }
                                else
                                {
                                    DropdownMenu(
                                        expanded = expandedDropdownId == id,
                                        onDismissRequest = { expandedDropdownId = "" },
                                    ) {
                                        DropdownMenuItem(
                                            text = { Text("Delete") },
                                            onClick = {
                                                showDeleteShaderDialog = true;
                                            });
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Button(
                onClick = { viewModel.createNewShader() },
                modifier = Modifier.align(Alignment.BottomEnd)
                    .padding(horizontal = 20.dp),
            ) {
                Text("+", fontSize = 16.sp);
            }
        }
    }
}
