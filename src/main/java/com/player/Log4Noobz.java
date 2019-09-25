package com.player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class Log4Noobz {

  private int textNumber;

  private int windowSize;

  private int framesBeforeUpdate;

  private int iterations = 0;

  private static final String[] NOOB_ROLLING_MESSAGES = new String[] {
      "Before software can be reusable it first has to be usable. ",
      "Walking on water and developing software from a specification are easy if both are frozen. ",
      "You would never believe how this team has already won 10 codingames ",
      "Free croissants in main site 3rd floor coffee room!!! ",
      "Kind reminder, fill your IPPM by tonight! ",
      "This bot went through no CCB! :(",
  };

  Log4Noobz() {
    textNumber = (int)(Math.random() * NOOB_ROLLING_MESSAGES.length);
    windowSize = 10;
    framesBeforeUpdate = 1 + 80 / NOOB_ROLLING_MESSAGES[textNumber].length();
  }

  Log4Noobz(int windowSize) {
    textNumber = (int)(Math.random() * NOOB_ROLLING_MESSAGES.length);
    this.windowSize = windowSize;
    framesBeforeUpdate = 1 + 80 / NOOB_ROLLING_MESSAGES[textNumber].length();
  }

  Log4Noobz(int windowSize, int framesBeforeUpdate) {
    textNumber = (int)(Math.random() * NOOB_ROLLING_MESSAGES.length);
    this.windowSize = windowSize;
    this.framesBeforeUpdate = Math.max(1, framesBeforeUpdate);
  }

  Log4Noobz(int textNumber, int windowSize, int framesBeforeUpdate) {
    this.textNumber = textNumber;
    this.windowSize = windowSize;
    this.framesBeforeUpdate = Math.max(1, framesBeforeUpdate);
  }

  String getRollingText() {
    return getRollingText(iterations);
  }

  String getRollingText(int iteration) {
    if (windowSize >= NOOB_ROLLING_MESSAGES[textNumber].length()) {
      return NOOB_ROLLING_MESSAGES[textNumber];
    }
    List<Character> chars = stringToList(NOOB_ROLLING_MESSAGES[textNumber]);
    int currentOffset = iteration / framesBeforeUpdate;
    Collections.rotate(chars, chars.size() - currentOffset);

    return listToString(chars.subList(0, Math.min(windowSize, chars.size())));
  }

  private static List<Character> stringToList(String str) {
    List<Character> chars = new ArrayList<>();
    for (char c : str.toCharArray()) {
      chars.add(c);
    }
    return chars;
  }

  private static String listToString(List<Character> chars) {
    StringBuilder sb = new StringBuilder();
    for (Character ch : chars) {
      sb.append(ch);
    }

    return sb.toString();
  }
}
