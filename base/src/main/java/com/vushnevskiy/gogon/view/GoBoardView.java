package com.vushnevskiy.gogon.view;

import com.vushnevskiy.gogon.viewmodel.BoardViewModel;

public interface GoBoardView {
  void setBoardEventListener(BoardEventListener boardEventListener);
  void setBoard(BoardViewModel boardViewModel);

  interface BoardEventListener {
    void onIntersectionSelected(int x, int y);
  }
}
