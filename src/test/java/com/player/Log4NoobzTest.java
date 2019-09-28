package com.player;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.player.log.Log4Noobz;

class Log4NoobzTest {

  @Test
  void getRollingText() {
    Log4Noobz log4Noobz = new Log4Noobz();
    Assertions.assertNotNull(log4Noobz.getRollingText());
  }

  @Test
  void testGetRollingText() {
    Log4Noobz log4Noobz = new Log4Noobz(10);
    Assertions.assertEquals(10, log4Noobz.getRollingText(4).length());
  }
}