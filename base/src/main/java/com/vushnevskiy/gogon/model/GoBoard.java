package com.vushnevskiy.gogon.model;

import com.vushnevskiy.gogon.proto.PlayGameData.Color;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.primitives.Ints;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Map;

/**
 * Class to represent the state of a Go board, and apply the logic of playing a move.
 */
public class GoBoard implements Serializable {

  private static final int BLACK_GROUP_START = 1;

  private static Map<Integer, int[][]> neighborPositionsByPositionCache = Maps.newHashMap();

  private final int[][] neighborPositionsByPosition;
  private final int size;
  private final int numberOfPositions;
  private final int numberOfGroups;
  private final int whiteGroupStart;
  private int[] groupByPosition;
  private BitSet blackField;
  private BitSet whiteField;
  private BitSet[] stoneFieldByGroup;
  private BitSet[] libertyFieldByGroup;

  public GoBoard(int size) {
    this.size = size;
    numberOfPositions = size * size;
    // Worst case, every other intersection is occupied by a stone of the same color.
    int numberOfGroupsPerColor = numberOfPositions / 2 + 1;
    // One set per color, plus zero which means empty
    numberOfGroups = 2 * numberOfGroupsPerColor + 1;
    whiteGroupStart = BLACK_GROUP_START + numberOfGroupsPerColor;
    neighborPositionsByPosition = getNeighborPositionsByPosition();
    blackField = new BitSet(numberOfPositions);
    whiteField = new BitSet(numberOfPositions);
    groupByPosition = new int[numberOfPositions];
    stoneFieldByGroup = new BitSet[numberOfGroups];
    libertyFieldByGroup = new BitSet[numberOfGroups];
    for (int index = 0; index < numberOfGroups; index++) {
      stoneFieldByGroup[index] = new BitSet();
      libertyFieldByGroup[index] = new BitSet();
    }
  }

  public void clear() {
    blackField.clear();
    whiteField.clear();
    Arrays.fill(groupByPosition, 0);
    for (int i = 0; i < numberOfGroups; i++) {
      stoneFieldByGroup[i].clear();
      libertyFieldByGroup[i].clear();
    }
  }

  private int[][] getNeighborPositionsByPosition() {
    if (neighborPositionsByPositionCache.containsKey(size)) {
      return neighborPositionsByPositionCache.get(size);
    }
    int[][] neighborPositionsByPositions = new int[numberOfPositions][];
    for (int x = 0; x < size; x++) {
      for (int y = 0; y < size; y++) {
        ArrayList<Integer> neighbors = Lists.newArrayList();
        if (x > 0) neighbors.add(getPos(x - 1, y));
        if (y > 0) neighbors.add(getPos(x, y - 1));
        if (x < size - 1) neighbors.add(getPos(x + 1, y));
        if (y < size - 1) neighbors.add(getPos(x, y + 1));
        neighborPositionsByPositions[getPos(x, y)] = Ints.toArray(neighbors);
      }
    }
    neighborPositionsByPositionCache.put(size, neighborPositionsByPositions);
    return neighborPositionsByPositions;
  }

  /**
   * Plays a move.
   *
   * @return whether the move was valid and played (if it was not, this instance can't be used anymore)
   */
  public boolean play(Color color, int move) {
    if (groupByPosition[move] != 0) {
      return false;
    }

    BitSet friendField = getField(color);
    BitSet foeField = getField(getOpponent(color));
    friendField.set(move);

    int group = getAvailableGroup(color);
    groupByPosition[move] = group;
    stoneFieldByGroup[group].set(move);
    libertyFieldByGroup[group].clear();
    for (int neighbor : neighborPositionsByPosition[move]) {
      if (friendField.get(neighbor)) {
        int friendGroup = groupByPosition[neighbor];
        if (friendGroup != group) {
          stoneFieldByGroup[group].or(stoneFieldByGroup[friendGroup]);
          for (int pos = stoneFieldByGroup[friendGroup].nextSetBit(0); pos != -1;
               pos = stoneFieldByGroup[friendGroup].nextSetBit(pos + 1)) {
            groupByPosition[pos] = group;
          }
          libertyFieldByGroup[group].or(libertyFieldByGroup[friendGroup]);
          stoneFieldByGroup[friendGroup].clear();
        }
      } else if (foeField.get(neighbor)) {
        int foeGroup = groupByPosition[neighbor];
        libertyFieldByGroup[foeGroup].clear(move);
        if (libertyFieldByGroup[foeGroup].isEmpty()) {
          capture(foeGroup);
        }
      } else {
        libertyFieldByGroup[group].set(neighbor);
      }
    }

    libertyFieldByGroup[group].clear(move);
    //noinspection RedundantIfStatement
    if (libertyFieldByGroup[group].isEmpty()) {
      return false;
    }

    return true;
  }

  public static Color getOpponent(Color color) {
    switch (color) {
      case WHITE:
        return Color.BLACK;
      case BLACK:
        return Color.WHITE;
      default:
        throw new RuntimeException("Invalid color");
    }

  }

  private BitSet getField(Color color) {
    return (color == Color.BLACK) ? blackField : whiteField;
  }

  private int getAvailableGroup(Color color) {
    int groupStart = (color == Color.BLACK) ? BLACK_GROUP_START : whiteGroupStart;
    for (int group = groupStart; ; group++) {
      if (stoneFieldByGroup[group].isEmpty()) return group;
    }
  }

  private void capture(int group) {
    Color foeColor = getOpponent(getColorByGroup(group));
    for (int pos = stoneFieldByGroup[group].nextSetBit(0); pos != -1;
         pos = stoneFieldByGroup[group].nextSetBit(pos + 1)) {
      // Remove the stone.
      whiteField.clear(pos);
      blackField.clear(pos);
      groupByPosition[pos] = 0;
      stoneFieldByGroup[group].clear(pos);

      // Create new liberties for neighbors.
      for (int neighbor : neighborPositionsByPosition[pos]) {
        int neighborGroup = groupByPosition[neighbor];
        if (getColorByGroup(neighborGroup) == foeColor) {
          libertyFieldByGroup[neighborGroup].set(pos);
        }
      }
    }
  }

  private Color getColorByGroup(int group) {
    if (group == 0) return null;
    return (group < whiteGroupStart) ? Color.BLACK : Color.WHITE;
  }

  public Color getColor(int x, int y) {
    return getColor(getPos(x, y));
  }

  public Color getColor(int pos) {
    return getColorByGroup(groupByPosition[pos]);
  }

  public int getSize() {
    return size;
  }

  public void copyFrom(GoBoard board) {
    System.arraycopy(board.groupByPosition, 0, groupByPosition, 0, numberOfPositions);
    blackField.or(board.blackField);
    whiteField.or(board.whiteField);
    for (int index = 0; index < numberOfGroups; index++) {
      stoneFieldByGroup[index].or(board.stoneFieldByGroup[index]);
      libertyFieldByGroup[index].or(board.libertyFieldByGroup[index]);
    }
  }

  public int getPos(int x, int y) {
    return y * size + x;
  }

  public int getScore() {
    return blackField.cardinality() - whiteField.cardinality();
  }

  public boolean isEyeFilling(int position, Color color) {
    int[] neighborPositions = neighborPositionsByPosition[position];
    for (int neighborPosition : neighborPositions) {
      if (getColor(neighborPosition) != color) {
        return false;
      }
      if (libertyFieldByGroup[groupByPosition[neighborPosition]].cardinality() == 1) {
        return false;
      }
    }
    return true;
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof GoBoard)) {
      return false;
    }
    GoBoard other = (GoBoard) object;

    return blackField.equals(other.blackField) && whiteField.equals(other.whiteField);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(blackField, whiteField);
  }
}
