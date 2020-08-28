package com.vushnevskiy.gogon.viewmodel

import com.vushnevskiy.gogon.proto.PlayGameData

data class PlayerViewModel(val playerName: String,
                           val playerColor: PlayGameData.Color)
