package com.wizneylabs.freestyle.composables

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Help
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.wizneylabs.freestyle.FreestyleEditorViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

/********************************************************************
 ************************* Navigation Routes ************************
 ********************************************************************/

@Serializable
object HomeRoute;

@Serializable
data class FreestyleEditorRoute(val shaderID: String);

/********************************************************************
 *************************** Composables ****************************
 ********************************************************************/

@Composable
fun FreestyleEditorApp() {

    val viewModel: FreestyleEditorViewModel = viewModel();

    val navController = rememberNavController();

    DetailedDrawerExample(navController) { innerPadding ->

        NavHost(
            modifier = Modifier
                .padding(innerPadding),
            navController = navController,
            startDestination = HomeRoute
        ) {
            composable<HomeRoute>() {
                MainMenu(navController, viewModel);
            }
            composable<FreestyleEditorRoute>() { backStackEntry ->

                val route: FreestyleEditorRoute = backStackEntry.toRoute();

                viewModel.editShader(route.shaderID);

                FreestyleEditor(navController, viewModel);
            }
        }
    }
}

@Composable
fun DeleteShaderDialog(title: String, text: String, resultHandler: ((Boolean) -> Unit)) {

    var openDialog by remember { mutableStateOf(true) };

    if (openDialog)
    {
        AlertDialog(
            onDismissRequest = { openDialog = false },
            title = { Text(title) },
            text = { Text(text) },
            confirmButton = {
                TextButton(
                    onClick = {
                        resultHandler(true);
                    }
                ) {
                    Text("Delete");
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    resultHandler(false);
                }) {
                    Text("Cancel");
                }
            }
        )
    }
}

@Composable
fun MainMenu(navController: NavHostController, viewModel: FreestyleEditorViewModel) {

    var expandedDropdownId by remember { mutableStateOf("") };

    var showDeleteShaderDialog by remember { mutableStateOf(false) };

    if (!viewModel.isFullyLoaded.value)
    {
        FreestyleEditorLoadingScreen();
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailedDrawerExample(
    navController: NavHostController,
    content: @Composable (PaddingValues) -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Spacer(Modifier.height(12.dp))
                    Text("Drawer Title", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleLarge)
                    HorizontalDivider()

                    Text("Section 1", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleMedium)
                    NavigationDrawerItem(
                        label = { Text("Main Menu") },
                        selected = false,
                        onClick = { /* Handle click */
                            navController.navigate(HomeRoute);
                            scope.launch { drawerState.close() }
                        }
                    )
                    NavigationDrawerItem(
                        label = { Text("Item 2") },
                        selected = false,
                        onClick = { /* Handle click */
                            scope.launch { drawerState.close() }
                        }
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                    Text("Section 2", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleMedium)
                    NavigationDrawerItem(
                        label = { Text("Settings") },
                        selected = false,
                        icon = { Icon(Icons.Outlined.Settings, contentDescription = null) },
                        badge = { Text("20") }, // Placeholder
                        onClick = { /* Handle click */
                            scope.launch { drawerState.close() }
                        }
                    )
                    NavigationDrawerItem(
                        label = { Text("Help and feedback") },
                        selected = false,
                        icon = { Icon(Icons.AutoMirrored.Outlined.Help, contentDescription = null) },
                        onClick = { /* Handle click */
                            scope.launch { drawerState.close() }
                        },
                    )
                    Spacer(Modifier.height(12.dp))
                }
            }
        },
        drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Freestyle Shader Editor") },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                if (drawerState.isClosed) {
                                    drawerState.open()
                                } else {
                                    drawerState.close()
                                }
                            }
                        }) {
                            Icon(Icons.Default.Menu,
                                contentDescription = "Menu");
                        };
                    }
                )
            }
        ) { innerPadding ->
            content(innerPadding);
        }
    }
}

@Composable
fun FreestyleEditor(navController: NavHostController,
                    viewModel: FreestyleEditorViewModel) {

    val editorText = viewModel.editorText.collectAsState();

    if (!viewModel.isFullyLoaded.value)
    {
        FreestyleEditorLoadingScreen();
    }
    else
    {
        TextField(
            value = editorText.value,
            onValueChange = { viewModel.onEditorTextChanged(it) },
            modifier = Modifier
                .fillMaxSize(),
            textStyle = MaterialTheme.typography.bodyLarge,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color(0xFF6482AD),
                focusedContainerColor = Color(0xFF6482AD),
                disabledContainerColor = Color(0xFF6482AD)
            ),
            visualTransformation = SyntaxHighlightVisualTransformation()
        )
    }
}

@Composable
fun FreestyleEditorLoadingScreen() {

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {

        Text("loading...");
    }
}

class SyntaxHighlightVisualTransformation() : VisualTransformation {

    private val keywords = mapOf(
        "in" to SpanStyle(color = Color.Blue),
        "vec2" to SpanStyle(color = Color.Green),
        "float" to SpanStyle(color = Color.Green),
        "return" to SpanStyle(color = Color.Magenta)
    );

    // styles for comments
    private val stringStyle = SpanStyle(color = Color.Yellow);
    private val commentStyle = SpanStyle(color = Color.Yellow);

    // comment regex patterns
    private val singleLineComment = Regex("""//.*$""", RegexOption.MULTILINE)
    private val multiLineComment = Regex("""/\*\*.*?\*/""", RegexOption.DOT_MATCHES_ALL)

    // base token regex to match words, whitespace, and special characters
    private val tokenPattern = Regex("""\w+|\s+|[-=+*/(){};,]|"[^"]*"""");

    private fun highlightSyntax(input: String): AnnotatedString {
        return buildAnnotatedString {
            var remainingText = input
            var lastProcessedIndex = 0

            // Process comments first (they take precedence)
            while (remainingText.isNotEmpty()) {
                val singleLineMatch = singleLineComment.find(remainingText)
                val multiLineMatch = multiLineComment.find(remainingText)

                // Find the earliest match
                val earliestMatch = when {
                    singleLineMatch == null -> multiLineMatch
                    multiLineMatch == null -> singleLineMatch
                    else -> if (singleLineMatch.range.first <= multiLineMatch.range.first)
                        singleLineMatch else multiLineMatch
                }

                if (earliestMatch == null) {
                    // No more comments, process remaining tokens
                    processTokens(remainingText, 0)
                    break
                }

                val matchStart = earliestMatch.range.first
                val matchEnd = earliestMatch.range.last + 1

                // Process tokens before the comment
                if (matchStart > 0) {
                    processTokens(remainingText.substring(0, matchStart), lastProcessedIndex)
                }

                // Append the comment with style
                withStyle(commentStyle) {
                    append(earliestMatch.value)
                }

                // Update remaining text and index
                lastProcessedIndex += matchEnd
                remainingText = if (matchEnd < remainingText.length) {
                    remainingText.substring(matchEnd)
                } else {
                    ""
                }
            }
        }
    }

    // Helper to process non-comment tokens
    private fun AnnotatedString.Builder.processTokens(text: String, offset: Int) {
        val matches = tokenPattern.findAll(text)
        var lastEnd = 0

        matches.forEach { match ->
            if (match.range.first > lastEnd) {
                append(text.substring(lastEnd, match.range.first))
            }

            val token = match.value
            when {
                token.startsWith("\"") -> withStyle(stringStyle) { append(token) }
                token in keywords -> withStyle(keywords[token]!!) { append(token) }
                else -> append(token)
            }

            lastEnd = match.range.last + 1
        }

        if (lastEnd < text.length) {
            append(text.substring(lastEnd))
        }
    }

    override fun filter(text: AnnotatedString): TransformedText {

        return TransformedText(
            text = highlightSyntax(text.text),
            offsetMapping = OffsetMapping.Identity
        )
    }
}