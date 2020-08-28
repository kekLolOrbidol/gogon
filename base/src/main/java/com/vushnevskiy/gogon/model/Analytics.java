package com.vushnevskiy.gogon.model;


import com.vushnevskiy.gogon.proto.PlayGameData;

public interface Analytics {
  void gameCreated(PlayGameData.GameData localGame);

  void configurationChanged(PlayGameData.GameData gameData);

  void undo();

  void redo();

  void resign();

  void movePlayed(PlayGameData.GameConfiguration gameConfiguration, PlayGameData.Move move);

  void deadStoneToggled(PlayGameData.GameConfiguration gameConfiguration);

  void invalidMovePlayed(PlayGameData.GameConfiguration gameConfiguration);

  void gameFinished(PlayGameData.GameConfiguration gameConfiguration, PlayGameData.Score score);
}
