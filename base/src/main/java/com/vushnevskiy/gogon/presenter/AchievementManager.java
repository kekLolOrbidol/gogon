package com.vushnevskiy.gogon.presenter;


import com.vushnevskiy.gogon.model.GoGameController;

import org.jetbrains.annotations.NotNull;

public interface AchievementManager {
  void updateAchievements(@NotNull GoGameController goGameController);
}
