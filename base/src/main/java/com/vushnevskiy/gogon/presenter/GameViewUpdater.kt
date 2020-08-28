package com.vushnevskiy.gogon.presenter

import com.vushnevskiy.gogon.model.GoGameController
import com.vushnevskiy.gogon.proto.PlayGameData.GameData.Phase.*
import com.vushnevskiy.gogon.view.GameView
import com.vushnevskiy.gogon.viewmodel.ConfigurationViewModels
import com.vushnevskiy.gogon.viewmodel.InGameViewModels
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.properties.Delegates

@Singleton
open class GameViewUpdater @Inject constructor(
        private val configurationViewModels: ConfigurationViewModels,
        private val inGameViewModels: InGameViewModels,
        private val goGameController: GoGameController) {

    var view: GameView by Delegates.notNull()

    fun update() = goGameController.let {
        if (isConfigured(it)) view.setInGameViewModel(inGameViewModels.from(it))
        else view.setConfigurationViewModel(configurationViewModels.from(it))
    }

    private fun isConfigured(goGameController: GoGameController): Boolean = with(goGameController) {
        when (phase) {
            INITIAL, CONFIGURATION -> false
            IN_GAME, DEAD_STONE_MARKING, FINISHED -> true
            UNKNOWN -> throw IllegalArgumentException("Invalid phase for game: " + phase)
        }
    }

}