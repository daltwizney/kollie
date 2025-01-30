package com.wizneylabs.examples.scenes

import android.util.Log
import androidx.compose.ui.geometry.Offset
import com.wizneylabs.kollie.pathfinder.GridRenderer
import com.wizneylabs.kollie.core.Scene

import com.wizneylabs.examples.components.BasicComponentExample
import com.wizneylabs.kollie.pathfinder.Maze
import com.wizneylabs.kollie.pathfinder.MazeRenderer
import com.wizneylabs.kollie.physics.GridCollisionQueryInput

class PathfinderDemoScene: Scene() {

    val TAG = PathfinderDemoScene::class.simpleName;

    val cellSize = 100;
    val horizontalWalks = 3;
    val verticalWalks = 6;

    init {
        Log.d(TAG, "creating new pathfinder demo scene!");
    }

    override fun Start() {

        Log.d(TAG, "pathfinder demo scene initializing!");

        val game = this.Game!!;

        val screenWidth = game.ScreenWidth;
        val screenHeight = game.ScreenHeight;

        // entity A
        val entityA = this.AddEntity("basicComponent");

        val componentA = entityA.AddComponent { BasicComponentExample() };

        componentA.name = "bob";

        // entity B
        val entityB = this.AddEntity("basicComponent");

        entityB.AddComponent { BasicComponentExample() };

        val componentB: BasicComponentExample? =
            entityB.GetComponent(BasicComponentExample::class.simpleName);

        componentB?.name = "jane";

        // create maze
        val rows = screenHeight / cellSize;
        val columns = screenWidth / cellSize;

        // TODO: change the maze interface so it accepts rows and columns instead
        val maze = Maze(columns, rows);

        maze.generateDrunkenCrawl(horizontalWalks, verticalWalks);

        // grid renderer
        val grid = this.AddEntity("grid");

        grid.AddComponent { GridRenderer() };

        val gridRenderer: GridRenderer? = grid.GetComponent(GridRenderer::class.simpleName);
        gridRenderer?.rows = rows;
        gridRenderer?.columns = columns;
        gridRenderer?.cellSize = cellSize;

        val mazeRenderer = grid.AddComponent { MazeRenderer() };
        mazeRenderer.gridRenderer = gridRenderer;
        mazeRenderer.maze = maze;

        // setup input handlers
        this.Game?.Input?.onTap?.add(this::handleTapInput);
    }

    fun handleTapInput(offset: Offset) {

        // TODO: here for testing - remove before flight!
        val mazeRenderer = this.Game?.CurrentScene?.
            FindComponentByType<MazeRenderer>(MazeRenderer::class.simpleName);

        var queryInput = GridCollisionQueryInput();
        queryInput.pointX = offset.x.toInt();
        queryInput.pointY = offset.y.toInt();
        queryInput.cellSize = mazeRenderer?.gridRenderer?.cellSize ?: 100;

        val queryResult = this.Game?.Physics?.gridCollisionQuery(queryInput) ?: null;

        val row = queryResult?.row ?: -1;
        val column = queryResult?.column ?: -1;

        Log.d(TAG + "TapInput", "clicked cell: (${row}, ${column})");
    }
}