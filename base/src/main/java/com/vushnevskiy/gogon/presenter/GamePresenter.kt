package com.vushnevskiy.gogon.presenter

import com.vushnevskiy.gogon.model.*
import com.vushnevskiy.gogon.proto.PlayGameData
import com.vushnevskiy.gogon.view.GameView
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GamePresenter @Inject constructor(private val gameRepository: GameRepository,
                                        private val achievementManager: AchievementManager,
                                        private val updater: GameViewUpdater,
                                        private val configurationViewEventProcessor: ConfigurationViewEventProcessor,
                                        private val inGameViewEventProcessor: InGameViewEventProcessor,
                                        goGameController: GoGameController) : GameEventProcessor(goGameController, updater, gameRepository), GameSelectionListener, GameChangeListener {

  private var view_: GameView? = null
  var view: GameView
    get() = view_!!
    set(value) {
      view_ = value
      updater.view = value
      gameRepository.apply {
        addGameSelectionListener(this@GamePresenter)
        addGameChangeListener(this@GamePresenter)
      }
      view.apply {
        setConfigurationViewListener(configurationViewEventProcessor)
        setInGameActionListener(inGameViewEventProcessor)
      }
      gameUpdated()
    }

  override fun gameSelected(gameData: PlayGameData.GameData?) {
    if (gameData != null) {
      goGameController.gameData = gameData
      updater.update()
    }
  }

  override fun gameChanged(gameData: PlayGameData.GameData) {
    goGameController.let {
      if (gameData.matchId == it.matchId) {
        if (gameData.gameConfiguration.gameType == PlayGameData.GameType.REMOTE) {
          it.gameData = gameData
        }
        gameUpdated()
      }
    }
  }

  private fun gameUpdated() {
    achievementManager.updateAchievements(goGameController)
    updateView()
  }

  fun clear() {
    gameRepository.removeGameSelectionListener(this)
    gameRepository.removeGameChangeListener(this)
    view.setConfigurationViewListener(null)
    view.setInGameActionListener(null)
  }

}
