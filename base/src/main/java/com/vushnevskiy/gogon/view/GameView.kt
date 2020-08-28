package com.vushnevskiy.gogon.view


import com.vushnevskiy.gogon.presenter.ConfigurationEventListener
import com.vushnevskiy.gogon.viewmodel.ConfigurationViewModel
import com.vushnevskiy.gogon.viewmodel.InGameViewModel

interface GameView {
    fun setConfigurationViewModel(configurationViewModel: ConfigurationViewModel?)
    fun setConfigurationViewListener(configurationEventListener: ConfigurationEventListener?)

    fun setInGameViewModel(inGameViewModel: InGameViewModel)
    fun setInGameActionListener(inGameEventListener: InGameView.InGameEventListener?)
}
