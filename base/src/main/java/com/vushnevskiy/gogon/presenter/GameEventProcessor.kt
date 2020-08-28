package com.vushnevskiy.gogon.presenter

import com.vushnevskiy.gogon.model.GameRepository
import com.vushnevskiy.gogon.model.GoGameController

abstract class GameEventProcessor(protected val goGameController: GoGameController,
                                  private val updater: GameViewUpdater,
                                  private val gameRepository: GameRepository) {

    protected fun updateView() {
        updater.update()
    }

    protected fun commitGameChanges() {
        with(goGameController) {
            gameRepository.commitGameChanges(gameData)
            updateView()
        }
    }
}