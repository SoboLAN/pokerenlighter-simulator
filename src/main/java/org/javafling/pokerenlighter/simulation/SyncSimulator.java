package org.javafling.pokerenlighter.simulation;

import org.javafling.pokerenlighter.combination.Card;
import org.javafling.pokerenlighter.simulation.worker.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Same as {@link Simulator} but runs synchronous. Setting workers or notification have no effect. The result is
 * directly returned by the {@link #start()} method.
 *
 * @author Radu Murzea
 * @author Matthias Eichner
 */
public class SyncSimulator {

    //simulation data
    private PokerType gameType;
    private ArrayList<PlayerProfile> profiles;
    private int nrRounds;
    private Card[] communityCards;
    private long startTime, endTime;

    public SyncSimulator(SimulatorBuilder builder) {
        this.gameType = builder.getGameType();
        this.nrRounds = builder.getNrRounds();
        this.profiles = builder.getProfiles();
        this.communityCards = builder.getCommunityCards();
        this.startTime = this.endTime = 0;
    }

    public SimulationFinalResult start() {
        SimulationWorker worker;
        SimulationWorker.WorkerBuilder builder;
        if (this.gameType == PokerType.TEXAS_HOLDEM) {
            builder = TexasHoldemWorker.builder();
        } else if (this.gameType == PokerType.OMAHA) {
            builder = OmahaWorker.builder();
        } else if (this.gameType == PokerType.OMAHA_HILO) {
            builder = OmahaHiLoWorker.builder();
        } else if (this.gameType == PokerType.FOMAHA) {
            builder = FiveCardOmahaWorker.builder();
        } else if (this.gameType == PokerType.SHORT_DECK) {
            builder = ShortDeckWorker.builder();
        } else {
            builder = FiveCardOmahaHiLoWorker.builder();
        }

        builder.setCommunityCards(this.communityCards)
                .setRounds(nrRounds);
        for (PlayerProfile profile : this.profiles) {
            builder.addPlayer(profile);
        }
        builder.setAsync(false);
        worker = builder.build();
        this.startTime = System.currentTimeMillis();
        worker.run();
        this.endTime = System.currentTimeMillis();
        return buildFinalResult(Collections.singletonList(worker));
    }

    private SimulationFinalResult buildFinalResult(List<SimulationWorker> workers)
    {
        int nrPlayers = profiles.size();

        double[] wins = new double[nrPlayers];
        double[] loses = new double[nrPlayers];
        double[] ties = new double[nrPlayers];

        for (int j = 0; j < nrPlayers; j++) {
            wins[j] = loses[j] = ties[j] = 0;
        }

        //sum up all the percentages
        int nrOfWorkers = workers.size();
        for (int i = 0; i < nrOfWorkers; i++) {
            SimulationWorkerResult result = workers.get(i).getResult();

            for (int j = 0; j < nrPlayers; j++) {
                wins[j] += result.getWinPercentage(j);
                loses[j] += result.getLosePercentage(j);
                ties[j] += result.getTiePercentage(j);
            }
        }

        //average is needed, so... divide
        for (int j = 0; j < nrPlayers; j++) {
            wins[j] /= nrOfWorkers;
            loses[j] /= nrOfWorkers;
            ties[j] /= nrOfWorkers;
        }

        long duration = endTime - startTime;

        SimulationFinalResult.ResultBuilder resultBuilder = new SimulationFinalResult.ResultBuilder().setGameType(gameType)
                .setPlayers(profiles);

        //flop is set
        if (communityCards[0] != null) {
            resultBuilder.setFlop(Arrays.copyOfRange(communityCards, 0, 3));
        }

        return resultBuilder.setTurn(communityCards[3])
                .setRiver(communityCards[4])
                .setWins(wins)
                .setTies(ties)
                .setLoses(loses)
                .setRounds(nrRounds)
                .setThreads(nrOfWorkers)
                .setDuration(duration)
                .build();
    }

}
