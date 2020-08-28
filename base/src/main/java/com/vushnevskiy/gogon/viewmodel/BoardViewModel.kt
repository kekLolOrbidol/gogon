package com.vushnevskiy.gogon.viewmodel

import com.vushnevskiy.gogon.proto.PlayGameData

data class BoardViewModel(
        val boardSize: Int,
        private val stones: Array<Array<PlayGameData.Color?>>,
        private val territories: Array<Array<PlayGameData.Color?>>,
        private val lastMoveX: Int,
        private val lastMoveY: Int,
        val isInteractive: Boolean) {

    fun getColor(x: Int, y: Int) = stones[y][x]

    fun getTerritory(x: Int, y: Int) = territories[y][x]

    fun isLastMove(x: Int, y: Int) = (x == lastMoveX && y == lastMoveY)

}
