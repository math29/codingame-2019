package com.player.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EntityTypeTest {

  @Test
  void valueOf() {
    Assertions.assertEquals(EntityType.NOTHING, EntityType.valueOf(-1));
    Assertions.assertEquals(EntityType.ALLY_ROBOT, EntityType.valueOf(0));
    Assertions.assertEquals(EntityType.ENEMY_ROBOT, EntityType.valueOf(1));
    Assertions.assertEquals(EntityType.RADAR, EntityType.valueOf(2));
    Assertions.assertEquals(EntityType.TRAP, EntityType.valueOf(3));
    Assertions.assertEquals(EntityType.AMADEUSIUM, EntityType.valueOf(4));
  }
}