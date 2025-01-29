package com.wizneylabs.examples.scenes

import android.util.Log
import com.wizneylabs.kollie.pathfinder.GridRenderer
import com.wizneylabs.kollie.core.Scene

import com.wizneylabs.examples.components.BasicComponentExample
import com.wizneylabs.kollie.pathfinder.Maze
import com.wizneylabs.kollie.pathfinder.MazeRenderer

class PathfinderDemoScene: Scene() {

    val TAG = PathfinderDemoScene::class.simpleName;

    val cellSize = 100;
    val horizontalWalks = 3;
    val verticalWalks = 6;

    override fun Start() {

        Log.d(TAG, "pathfinder demo scene initializing!");

        val game = this.Game!!;

        val screenWidth = game.ScreenWidth;
        val screenHeight = game.ScreenHeight;

        // entity A
        val entityA = this.AddEntity("basicComponent");

        val componentA = entityA.AddComponent<BasicComponentExample>();

        componentA.name = "bob";

        // entity B
        val entityB = this.AddEntity("basicComponent");

        val componentB = entityB.AddComponent<BasicComponentExample>();

        componentB.name = "jane";

        Log.d(TAG, "about to create maze!");

        // create maze
        val rows = screenHeight / cellSize;
        val columns = screenWidth / cellSize;

        // TODO: change the maze interface so it accepts rows and columns instead
        val maze = Maze(columns, rows);

        maze.generateDrunkenCrawl(horizontalWalks, verticalWalks);

        // grid renderer
        val grid = this.AddEntity("grid");

        val gridRenderer = grid.AddComponent<GridRenderer>();
        gridRenderer.rows = rows;
        gridRenderer.columns = columns;
        gridRenderer.cellSize = cellSize;

        val mazeRenderer = grid.AddComponent<MazeRenderer>();
        mazeRenderer.gridRenderer = gridRenderer;
        mazeRenderer.maze = maze;
    }
}