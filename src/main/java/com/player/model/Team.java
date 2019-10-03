package com.player.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.Scanner;

public class Team {
    private int score;

    private Collection<Entity> robots;

    public void readScore(Scanner in) {
        score = in.nextInt();
        robots = new ArrayList<>();
    }

    public int getNumberOfRobotAlive() {
        return (int) this.robots.stream().filter(Entity::isAlive).count();
    }

    public int getScore() {
        return score;
    }

    public Collection<Entity> getRobots() {
        return robots;
    }

    public Optional<Entity> getRobot(final int id) {
        return this.getRobots().stream()
                .filter(entity -> entity.getId() == id)
                .findFirst();
    }
}
