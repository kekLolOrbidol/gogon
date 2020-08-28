package com.vushnevskiy.gogon.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

import com.vushnevskiy.gogon.R;
import com.vushnevskiy.gogon.model.GameDatas;
import com.vushnevskiy.gogon.proto.PlayGameData.GameData;

/**
 * {@link MatchMenuItem} for a Google Play games turn based match.
 */
public class GameMatchMenuItem extends MatchMenuItem {
  private final GameDatas gameDatas;
  private final GameData gameData;

  public GameMatchMenuItem(GameDatas gameDatas, GameData gameData) {
    super(gameData.getMatchId());
    this.gameDatas = gameDatas;
    this.gameData = gameData;
  }

  @Override
  public String getFirstLine(Context context) {
    return context.getString(R.string.match_label_remote_first_line_format,
        gameData.getGameConfiguration().getBlack().getName(),
        gameData.getGameConfiguration().getWhite().getName());
  }

  @Override
  public String getSecondLine(Context context) {
    return context.getString(R.string.match_label_remote_second_line_format,
        gameData.getGameConfiguration().getBoardSize(),
        context.getString(getGameType()));
  }

  @StringRes
  private int getGameType() {
    switch (gameData.getGameConfiguration().getGameType()) {
      case LOCAL:
        return R.string.game_type_local_label;
      case REMOTE:
        return R.string.game_type_remote_label;
    }
    throw new RuntimeException("Invalid game type " + gameData.getGameConfiguration().getGameType());
  }

  @Override
  public Drawable getIcon(Context context) {
    int iconResId = gameDatas.isLocalTurn(gameData) ? R.drawable.ic_match_your_turn : R.drawable.ic_match_their_turn;
    return ContextCompat.getDrawable(context, iconResId);
  }

  @Override
  public boolean isValid() {
    return !needsApplicationUpdate();
  }

  private boolean needsApplicationUpdate() {
    return gameData.getVersion() > GameDatas.VERSION;
  }
}
