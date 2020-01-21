package me.hugmanrique.cellarium.tests;

import me.hugmanrique.cellarium.Key;
import me.hugmanrique.cellarium.Repository;
import me.hugmanrique.cellarium.simple.SimpleKey;
import me.hugmanrique.cellarium.simple.SimpleRepository;
import me.hugmanrique.cellarium.util.EnumValues;
import me.hugmanrique.cellarium.util.IntegerValues;

public class ChessExample {

    enum Rank {
        BEGINNER, CHAMPION, MASTER
    }

    static class Statistics {

        static final Key<Integer> ELO = new SimpleKey.Builder<>(Integer.class)
                .defaultValue(1200)
                .build();

        static final Key<Integer> WIN_COUNT = new SimpleKey.Builder<>(Integer.class)
                .defaultValue(0)
                .build();

        static final Key<Rank> RANK = new SimpleKey.Builder<>(Rank.class)
                .defaultValue(Rank.BEGINNER)
                .build();
    }

    static void broadcast(String message) {
        // TODO
    }

    static class Player {
        private final Repository statistics;

        public Player() {
            this.statistics = SimpleRepository.newInstance();
        }

        public void onWin(int eloDelta) {
            // Update ELO
            int newElo = statistics.compute(Statistics.ELO, previous -> previous + eloDelta);

            broadcast("New player ELO is " + newElo);

            // Increase win count
            statistics.compute(Statistics.WIN_COUNT, IntegerValues::increment);
        }

        public void levelUp() {
            // e.g. BEGINNER -> CHAMPION
            statistics.compute(Statistics.RANK, EnumValues::nextValue);
        }
    }
}
