package com.vushnevskiy.gogon.presenter

import com.vushnevskiy.gogon.model.Analytics
import com.vushnevskiy.gogon.model.GameDatas
import com.vushnevskiy.gogon.model.GameRepository
import com.vushnevskiy.gogon.model.GoGameController
import com.vushnevskiy.gogon.view.InGameView
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InGameViewEventProcessor @Inject constructor(private val gameDatas: GameDatas,
                                                   private val feedbackSender: FeedbackSender,
                                                   private val analytics: Analytics,
                                                   goGameController: GoGameController,
                                                   updater: GameViewUpdater,
                                                   gameRepository: GameRepository) : GameEventProcessor(goGameController, updater, gameRepository), InGameView.InGameEventListener {

    override fun onIntersectionSelected(x: Int, y: Int) {
        goGameController.run {
            if (isLocalTurn) {
                val played = playMoveOrToggleDeadStone(gameDatas.createMove(x, y))

                if (played) {
                    commitGameChanges()
                } else {
                    feedbackSender.invalidMove()
                }
            }
        }
    }

    override fun onPass() {
        goGameController.pass()
        commitGameChanges()
    }

    override fun onDone() {
        goGameController.deadStoneMarkingDone()
        commitGameChanges()
    }

    override fun onUndo() {
        if (goGameController.undo()) {
            commitGameChanges()
            analytics.undo()
        }
    }

    override fun onRedo() {
        if (goGameController.redo()) {
            analytics.redo()
            commitGameChanges()
        }
    }

    override fun onResign() {
        goGameController.resign()
        commitGameChanges()
        analytics.resign()
    }
}