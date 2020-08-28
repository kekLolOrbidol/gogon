package com.vushnevskiy.gogon.viewmodel

import com.vushnevskiy.gogon.model.GoGameController
import com.vushnevskiy.gogon.presenter.GameMessageGenerator
import com.vushnevskiy.gogon.proto.PlayGameData.GameData.Phase.INITIAL
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConfigurationViewModels @Inject constructor(private val gameMessageGenerator: GameMessageGenerator) {

    fun from(goGameController: GoGameController) = with(goGameController.gameConfiguration) {
        ConfigurationViewModel(
                komi = komi,
                boardSize = boardSize,
                handicap = handicap,
                blackPlayerName = black.name,
                whitePlayerName = white.name,
                message = getMessage(goGameController),
                interactionsEnabled = goGameController.isLocalTurn)
    }

    private fun getMessage(goGameController: GoGameController) = when {
        goGameController.phase == INITIAL -> gameMessageGenerator.configurationMessageInitial
        goGameController.isLocalTurn -> gameMessageGenerator.configurationMessageAcceptOrChange
        else -> gameMessageGenerator.configurationMessageWaitingForOpponent
    }
}