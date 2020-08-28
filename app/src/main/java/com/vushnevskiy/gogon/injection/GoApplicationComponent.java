package com.vushnevskiy.gogon.injection;

import com.vushnevskiy.gogon.ui.GameFragment;
import com.vushnevskiy.gogon.ui.InGameViewAndroid;
import com.vushnevskiy.gogon.ui.MainActivity;
import com.vushnevskiy.gogon.ui.PlayerChoiceFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Dagger Component to instantiate injected objects.
 */
@Singleton
@Component (
    modules= GoApplicationModule.class
)
public interface GoApplicationComponent {
  void inject(MainActivity mainActivity);
  void inject(GameFragment gameFragment);
  void inject(InGameViewAndroid inGameViewAndroid);
  void inject(PlayerChoiceFragment playerChoiceFragment);
}
