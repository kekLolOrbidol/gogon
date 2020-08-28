package com.vushnevskiy.gogon.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.common.base.Objects;

/**
 * A spinner menu entry for a match.
 */
public abstract class MatchMenuItem {
  private final String matchId;

  public abstract String getFirstLine(Context context);
  public abstract String getSecondLine(Context context);
  public abstract Drawable getIcon(Context context);

  public MatchMenuItem(String matchId) {
    this.matchId = matchId;
  }

  public String getMatchId() {
    return matchId;
  }

  public boolean isValid() {
    return true;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof MatchMenuItem)) return false;

    MatchMenuItem that = (MatchMenuItem) o;

    return Objects.equal(this.matchId, that.matchId);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(matchId);
  }
}
