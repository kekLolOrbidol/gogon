package com.vushnevskiy.gogon.view


import com.vushnevskiy.gogon.viewmodel.InGameViewModel

interface InGameView {
    fun setInGameModel(inGameViewModel: InGameViewModel)
    fun setInGameEventListener(inGameEventListener: InGameEventListener?)

    interface InGameEventListener : GoBoardView.BoardEventListener {
        fun onPass()
        fun onDone()
        fun onUndo()
        fun onRedo()
        fun onResign()
    }
}
