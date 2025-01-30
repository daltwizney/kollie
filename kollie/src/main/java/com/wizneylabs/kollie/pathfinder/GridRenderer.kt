package com.wizneylabs.kollie.pathfinder

import android.util.Log
import androidx.compose.ui.graphics.Color
import com.wizneylabs.kollie.core.Component
import com.wizneylabs.kollie.core.Entity

class GridRenderer(entity: Entity):
    Component(entity) {

    val TAG = GridRenderer::class.simpleName;

    val IsInitialized: Boolean
        get() = _isInitialized;

    var rows = 2;
    var columns = 2;
    var cellSize = 100;
    var depth = 0;

    private var _isInitialized = false;

    private var _colors = arrayOf<Array<Color>>();

    private val _defaultCellColor = Color.Black;

    override fun Awake() {

        super.Awake();

        if (rows < 1 || columns < 1 || cellSize < 1)
        {
            throw RuntimeException("Invalid grid renderer shape!");
        }

        _colors = Array(rows) { Array(columns) { _defaultCellColor } };

        Log.d(TAG, "renderer initialized!")

        _isInitialized = true;
    }

    override fun Start() {
        super.Start();
    }

    override fun Update(t: Float, dt: Float) {
        super.Update(t, dt);
    }

    fun isValidCell(row: Int, column: Int): Boolean {

        return (row > 0 || row < rows - 1)
                && (column > 0 || column < columns - 1);
    }

    fun setColor(row: Int, column: Int, color: Color) {

        if (!isValidCell(row, column))
        {
            Log.e(TAG, "invalid grid indices! row = $row, column = $column, this.rows = $rows, this.columns = $columns");
            return;
        }

        _colors[row][column] = color;
    }

    fun getColor(row: Int, column: Int): Color {

        if (!isValidCell(row, column))
        {
            Log.e(TAG, "invalid grid indices! row = $row, column = $column, this.rows = $rows, this.columns = $columns");
            return Color.Magenta;
        }

        return _colors[row][column];
    }
}