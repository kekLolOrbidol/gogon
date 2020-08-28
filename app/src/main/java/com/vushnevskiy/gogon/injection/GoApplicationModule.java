package com.vushnevskiy.gogon.injection;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.annotation.Nullable;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.games.AchievementsClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.Player;
import com.google.android.gms.games.PlayersClient;
import com.google.android.gms.games.TurnBasedMultiplayerClient;
import com.google.common.base.Preconditions;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.vushnevskiy.gogon.R;
import com.vushnevskiy.gogon.analytics.AnalyticsEventLogger;
import com.vushnevskiy.gogon.analytics.FirebaseAnalyticsSender;
import com.vushnevskiy.gogon.logger.EventLogger;
import com.vushnevskiy.gogon.model.Analytics;
import com.vushnevskiy.gogon.model.AvatarManager;
import com.vushnevskiy.gogon.model.GameRepository;
import com.vushnevskiy.gogon.model.GoogleAccountManager;
import com.vushnevskiy.gogon.presenter.AchievementManager;
import com.vushnevskiy.gogon.presenter.FeedbackSender;
import com.vushnevskiy.gogon.presenter.GameMessageGenerator;
import com.vushnevskiy.gogon.ui.AchievementManagerAndroid;
import com.vushnevskiy.gogon.ui.AndroidFeedbackSender;
import com.vushnevskiy.gogon.ui.AndroidGameRepository;
import com.vushnevskiy.gogon.ui.GameMessageGeneratorAndroid;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * Module to configure dependency injection.
 */
@Module(includes = GoApplicationModule.Bindings.class)
public class GoApplicationModule {

  @Module
  public interface Bindings {
    @Binds
    @Singleton
    Analytics getAnalytics(FirebaseAnalyticsSender analytics);

    @Binds
    @Singleton
    GameRepository provideGameRepository(AndroidGameRepository androidGameRepository);

    @Binds
    @Singleton
    GameMessageGenerator getGameMessageGenerator(GameMessageGeneratorAndroid gameMessageGeneratorAndroid);

    @Binds
    @Singleton
    AchievementManager getAchievementManager(AchievementManagerAndroid achievementManagerAndroid);

    @Binds
    @Singleton
    FeedbackSender getFeedbackSender(AndroidFeedbackSender androidFeedbackSender);
  }

  private Application application;

  public GoApplicationModule(Application application) {
    this.application = application;
  }

  @Provides
  @Nullable
  public GoogleSignInAccount getSignedInAccount(Context context) {
    return GoogleSignIn.getLastSignedInAccount(context);
  }

  @Provides
  @Named("Not Null")
  public GoogleSignInAccount getNotNullableSignedInAccount(@Nullable GoogleSignInAccount googleSignInAccount) {
    return Preconditions.checkNotNull(googleSignInAccount);
  }

  @Provides
  public TurnBasedMultiplayerClient getTurnBasedMultiplayerClient(Context context, @Named("Not Null") GoogleSignInAccount account) {
    return Games.getTurnBasedMultiplayerClient(context, account);
  }

  @Provides
  public PlayersClient getPlayerClient(Context context, @Named("Not Null") GoogleSignInAccount account) {
    return Games.getPlayersClient(context, account);
  }

  @Provides
  public AchievementsClient getAchievementsClient(Context context, @Named("Not Null") GoogleSignInAccount account) {
    return Games.getAchievementsClient(context, account);
  }

  @Provides
  @Named("PlayerOneDefaultName")
  public String providePlayerOneDefaultName(GoogleAccountManager googleAccountManager,
      AvatarManager avatarManager) {
    if (googleAccountManager.getSignInComplete()) {
      Player currentPlayer = googleAccountManager.getCurrentPlayer();
      avatarManager.setAvatarUri(currentPlayer.getDisplayName(), currentPlayer.getIconImageUri());
      return currentPlayer.getDisplayName();
    } else {
      return application.getString(R.string.player_one_default_name);
    }
  }

  @Provides
  @Named("PlayerTwoDefaultName")
  public String providePlayerTwoDefaultName() {
    return application.getString(R.string.player_two_default_name);
  }

  @Provides
  @Singleton
  public Context getApplicationContext() {
    return application;
  }

  @Provides
  @Singleton
  public SharedPreferences getSharedPreferences(Context context) {
    return PreferenceManager.getDefaultSharedPreferences(context);
  }

  @Provides
  @Singleton
  public FirebaseAnalytics getFireBaFirebaseAnalytics(Context context) {
    return FirebaseAnalytics.getInstance(context);
  }

  @Provides
  @Singleton
  public EventLogger getFEventLogger(FirebaseAnalytics firebaseAnalytics) {
    return new AnalyticsEventLogger(firebaseAnalytics);
  }

}
