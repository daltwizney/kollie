package com.wizneylabs.kollie.pathfinder

import android.util.Log

// TODO:  update this to accept rows and columns, instead of width/height
class Maze(width: Int, height: Int) {

    private val _width = width;
    private val _height = height;

    val Width: Int
        get() = _width;

    val Height: Int
        get() = _height;

    private val _maze: Array<Array<Int>>;

    init {
        /**
         *  0 = wall cell
         *  1 = walkable cell
         */
        _maze = Array(_height) { Array(_width) { 0 } };
    }

    fun getValue(i: Int, j: Int): Int {

        return _maze[i][j];
    }

    fun generateDrunkenCrawl(horizontalWalks: Int, verticalWalks: Int) {

        for (i in 0..horizontalWalks)
        {
            doHorizontalDrunkenCrawl();
        }

        for (i in 0..verticalWalks)
        {
            doVerticalDrunkenCrawl();
        }
    }

    fun doHorizontalDrunkenCrawl() {

        var i = (0.._height - 1).random();
        var j = 0;

        while (j < _width)
        {
            _maze[i][j] = 1;

            var deltaI: Int;

            do {
                deltaI = (-1..1).random();

            } while (!isValidCell(i + deltaI, j))

            i += deltaI;
            j += (0..1).random();
        }
    }

    fun doVerticalDrunkenCrawl() {

        var i = 0;
        var j = (0.._width - 1).random();

        while (i < _height)
        {
            _maze[i][j] = 1;

            var deltaJ: Int;

            do {
                deltaJ = (-1..1).random();

            } while (!isValidCell(i, j + deltaJ))

            i += (0..1).random();
            j += deltaJ;
        }
    }

    fun isValidCell(i: Int, j: Int): Boolean {

        return i >= 0 && i < _height && j >= 0 && j < _width;
    }

    fun isWalkable(i: Int, j: Int): Boolean {

        return isValidCell(i, j) && _maze[i][j] == 1;
    }
}