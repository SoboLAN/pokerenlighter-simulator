package org.javafling.pokerenlighter.simulation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.javafling.pokerenlighter.combination.Card;
import org.javafling.pokerenlighter.simulation.SimulationFinalResult.ResultBuilder;
import org.javafling.pokerenlighter.simulation.worker.OmahaHiLoWorker;
import org.javafling.pokerenlighter.simulation.worker.OmahaHiLoWorker.OmahaHiLoBuilder;
import org.javafling.pokerenlighter.simulation.worker.OmahaWorker;
import org.javafling.pokerenlighter.simulation.worker.OmahaWorker.OmahaBuilder;
import org.javafling.pokerenlighter.simulation.worker.SimulationWorker;
import org.javafling.pokerenlighter.simulation.worker.SimulationWorker.WorkerBuilder;
import org.javafling.pokerenlighter.simulation.worker.SimulationWorkerResult;
import org.javafling.pokerenlighter.simulation.worker.TexasHoldemWorker;
import org.javafling.pokerenlighter.simulation.worker.TexasHoldemWorker.TexasHoldemBuilder;
import org.javafling.pokerenlighter.simulation.worker.WorkerEvent;
import org.javafling.pokerenlighter.simulation.worker.WorkerNotifiable;

/**
 * The control center of the simulations. This class manages the progress of all the simulations and
 * centralizes them. It will split the simulations on multiple threads, so you can safely create a
 * <code>Simulator</code> and/or start it on any thread you want, even the GUI thread.
 * <br />
 * The progress and events of the simulation must be monitored by passing an instance of
 * <code>SimulationNotifiable</code> to the Simulator's constructor.
 * <br />
 * Once a simulation is completed, it can not be started again.
 * 
 * @author Radu Murzea
 */
public final class Simulator implements WorkerNotifiable
{
    //simulation data
    private PokerType gameType;
    private ArrayList<PlayerProfile> profiles;
    private int nrRounds;
    private Card[] communityCards;
    
    //workers
    private ArrayList<SimulationWorker> workers;
    
    //additional stuff needed for correct implementation
    private ExecutorService executor;
    private CountDownLatch latch;
    private long startTime, endTime;
    private int updateInterval, overallProgress, lastUpdatePercentage;
    private int nrOfWorkers;
    private boolean isRunning;
    
    private SimulationNotifiable notifiable;
    
    private SimulationFinalResult simulationResult;
    
    public static class SimulatorBuilder
    {
        private PokerType gameType;
        private ArrayList<PlayerProfile> profiles;
        private int nrRounds;
        private Card[] communityCards = new Card[5];
        private int updateInterval;
        private SimulationNotifiable notifiable;

        public PokerType getGameType()
        {
            return this.gameType;
        }

        public SimulationNotifiable getNotifiable()
        {
            return this.notifiable;
        }

        public ArrayList<PlayerProfile> getProfiles()
        {
            return this.profiles;
        }

        public int getUpdateInterval()
        {
            return this.updateInterval;
        }

        public int getNrRounds()
        {
            return this.nrRounds;
        }

        public Card[] getCommunityCards()
        {
            return this.communityCards;
        }
        
        public SimulatorBuilder setGameType(PokerType gameType)
        {
            this.gameType = gameType;
            return this;
        }
        
        public SimulatorBuilder addPlayer(PlayerProfile profile)
        {
            if (this.profiles == null) {
                this.profiles = new ArrayList<>();
            }
            
            this.profiles.add(profile);
            
            return this;
        }

        public SimulatorBuilder setFlop(Card[] flopCards)
        {            
            this.communityCards[0] = flopCards[0];
            this.communityCards[1] = flopCards[1];
            this.communityCards[2] = flopCards[2];
            
            return this;
        }
        
        public SimulatorBuilder setTurn(Card turn)
        {
            this.communityCards[3] = turn;
            return this;
        }
        
        public SimulatorBuilder setRiver(Card river)
        {
            this.communityCards[4] = river;
            return this;
        }
        
        public SimulatorBuilder setNrRounds(int nrRounds)
        {
            this.nrRounds = nrRounds;
            return this;
        }

        public SimulatorBuilder setUpdateInterval(int updateInterval)
        {
            this.updateInterval = updateInterval;
            return this;
        }

        public SimulatorBuilder setNotifiable(SimulationNotifiable notifiable)
        {
            this.notifiable = notifiable;
            return this;
        }
        
        private boolean isPredictableResult()
        {
            boolean commSet = true;
            for (int i = 0; i < 5; i++) {
                if (this.communityCards[i] == null) {
                    commSet = false;
                    break;
                }
            }

            if (commSet) {
                boolean correctPlayerTypes = false;
                for (PlayerProfile profile : this.profiles) {
                    if (profile.getHandType() != HandType.EXACTCARDS) {
                        correctPlayerTypes = true;
                        break;
                    }
                }

                if (! correctPlayerTypes) {
                    return true;
                }
            }

            return false;
        }
        
        private Card[] getAllCards()
        {
            ArrayList<Card> cards = new ArrayList<>();
            
            for (Card card : this.communityCards) {
                if (card != null) {
                    cards.add(card);
                }
            }
            
            for (PlayerProfile profile : this.profiles) {
                if (profile.getHandType() == HandType.EXACTCARDS) {
                    Card[] playerCards = profile.getCards();
                    for (Card card : playerCards) {
                        if (card != null) {
                            cards.add(card);
                        }
                    }
                }
            }
            
            Card[] result = new Card[cards.size()];
            
            return cards.toArray(result);
        }
        
        public Simulator build()
        {
            if (this.updateInterval <= 0 || 100 % this.updateInterval != 0) {
                throw new IllegalStateException("Invalid update interval value");
            } else if (this.gameType == null) {
                throw new IllegalStateException("Invalid game type value");
            } else if (this.nrRounds <= 0) {
                throw new IllegalStateException("Invalid rounds value");
            } else if (this.notifiable == null) {
                throw new IllegalStateException("No notifiable was set");
            } else if (this.profiles == null || this.profiles.size() < 2) {
                throw new IllegalStateException("Invalid or insufficient player profiles");
            }
            
            for (PlayerProfile profile : this.profiles) {
                if (profile == null) {
                    throw new NullPointerException("NULL PlayerProfile was used");
                }
            }
            
            //flop cards must either be all equal to NULL or all different from NULL
            //if this is not the case (the "!" operator marks that), an exception is thrown
            if (! (
                (this.communityCards[0] == null && this.communityCards[1] == null && this.communityCards[2] == null)
                ||
                (this.communityCards[0] != null && this.communityCards[1] != null && this.communityCards[2] != null)
                )) {
                throw new IllegalStateException("Invalid flop cards");
            }
            
            //if the turn is set, but not the flop, that's not OK
            if (this.communityCards[3] != null && this.communityCards[0] == null) {
                throw new IllegalStateException("Turn card can not exist without flop");
            }
            
            //if the river is set, but not the turn, that's not OK
            if (this.communityCards[4] != null && this.communityCards[3] == null) {
                throw new IllegalStateException("River card can not exist without the turn card");
            }
            
            if (this.isPredictableResult()) {
                throw new IllegalStateException("The simulation result is predictable");
            }
            
            Card[] allCards = this.getAllCards();
            for (int i = 0; i < allCards.length - 1; i++) {
                for (int j = i + 1; j < allCards.length; j++) {
                    if (allCards[i].equals(allCards[j])) {
                        throw new IllegalStateException("No duplicate cards allowed");
                    }
                }
            }
            
            return new Simulator(this);
        }
    }

    private Simulator(SimulatorBuilder builder)
    {        
        this.gameType = builder.getGameType();
        this.nrRounds = builder.getNrRounds();
        this.profiles = builder.getProfiles();
        this.communityCards = builder.getCommunityCards();
        this.updateInterval = builder.getUpdateInterval();
        this.notifiable = builder.getNotifiable();
        
        this.workers = new ArrayList<>();
        this.startTime = this.endTime = this.overallProgress = this.lastUpdatePercentage = 0;
        this.nrOfWorkers = SystemUtils.getNrOfLogicalCPUs();
    }
    
    public boolean isRunning()
    {
        return isRunning;
    }
    
    public int getProgress()
    {
        return overallProgress;
    }
    
    public SimulationFinalResult getResult()
    {
        return this.simulationResult;
    }
    
    /**
     * {@inheritDoc} 
     */
    @Override
    public void onSimulationDone(WorkerEvent event)
    {
        this.latch.countDown();
    }
    
    /**
     * {@inheritDoc} 
     */
    @Override
    public synchronized void onSimulationProgress(WorkerEvent event)
    {
        int totalProgress = 0;
        for (int i = 0; i < this.nrOfWorkers; i++) {
            totalProgress += this.workers.get(i).getProgress();
        }
        
        this.overallProgress = (int) (totalProgress / this.nrOfWorkers);
        
        if (this.overallProgress - this.lastUpdatePercentage >= this.updateInterval) {
            SimulationEvent mainEvent = new SimulationEvent(SimulationEvent.EVENT_SIM_PROGRESS, this.overallProgress);
            this.notifiable.onSimulationProgress(mainEvent);
            this.lastUpdatePercentage = this.overallProgress;
        }
    }
    
    /**
     * {@inheritDoc} 
     */
    @Override
    public void onSimulationError(WorkerEvent event)
    {
        SimulationEvent errorEvent = new SimulationEvent(SimulationEvent.EVENT_SIM_ERROR, event.getEventData());
        this.notifiable.onSimulationError(errorEvent);
    }

    public void start()
    {
        if (this.isRunning || this.simulationResult != null) {
            return;
        }
        
        this.latch = new CountDownLatch(this.nrOfWorkers);
        int roundsPerWorker = getNrOfRoundsPerWorker(this.nrOfWorkers);
        
        this.executor = Executors.newFixedThreadPool(this.nrOfWorkers);
    
        int workerUpdateInterval = getUpdateInterval(this.nrOfWorkers);
        
        for (int i = 0; i < this.nrOfWorkers; i++) {
            SimulationWorker worker;
            WorkerBuilder builder;
            if (this.gameType == PokerType.TEXAS_HOLDEM) {
                builder = TexasHoldemWorker.builder();
            } else if (this.gameType == PokerType.OMAHA) {
                builder = OmahaWorker.builder();
            } else {
                builder = OmahaHiLoWorker.builder();
            }
            
            builder.setCommunityCards(this.communityCards)
                .setNotifier(this)
                .setRounds(roundsPerWorker)
                .setUpdateInterval(workerUpdateInterval);
            
            for (PlayerProfile profile : this.profiles) {
                builder.addPlayer(profile);
            }
            
            worker = builder.build();
            
            this.executor.execute(worker);
            this.workers.add(worker);
        }
        
        this.startTime = System.currentTimeMillis();
        
        this.isRunning = true;
        
        Thread masterThread = new Thread(new Supervisor());
        masterThread.setDaemon(true);
        masterThread.start();
        
        SimulationEvent event = new SimulationEvent(SimulationEvent.EVENT_SIM_STARTED, this.nrOfWorkers);
        this.notifiable.onSimulationStart(event);
    }
    
    public void stop()
    {
        this.executor.shutdownNow();
        
        this.isRunning = false;
        
        if (this.endTime == 0) {
            SimulationEvent event = new SimulationEvent(SimulationEvent.EVENT_SIM_DONE, this.overallProgress);
            this.notifiable.onSimulationCancel(event);
        }
    }
        
    public boolean isSimulationDone()
    {
        for (SimulationWorker worker : workers) {
            if (worker.getProgress() != 100) {
                return false;
            }
        }
        
        return true;
    }
        
    private int getNrOfRoundsPerWorker(int workers)
    {
        return (int) Math.ceil(1.0 * this.nrRounds / workers);
    }
    
    //calculates how often the workers should notify the simulator of progress,
    //based on how many workers there are
    private int getUpdateInterval(int workers)
    {
        switch (workers) {
            case 1:
            case 2: return 10;
            case 3:
            case 4: return 20;
            default: return 25;
        }
    }
    
    private void buildFinalResult()
    {
        int nrPlayers = profiles.size();
            
        double[] wins = new double[nrPlayers];
        double[] loses = new double[nrPlayers];
        double[] ties = new double[nrPlayers];
            
        for (int j = 0; j < nrPlayers; j++) {
            wins[j] = loses[j] = ties[j] = 0;
        }
            
        //sum up all the percentages
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
            
        ResultBuilder resultBuilder = new SimulationFinalResult.ResultBuilder().setGameType(gameType)
                                                                                .setPlayers(profiles);
            
        //flop is set
        if (communityCards[0] != null) {
            resultBuilder.setFlop(Arrays.copyOfRange(communityCards, 0, 3));
        }
            
        simulationResult = resultBuilder.setTurn(communityCards[3])
                                        .setRiver(communityCards[4])
                                        .setWins(wins)
                                        .setTies(ties)
                                        .setLoses(loses)
                                        .setRounds(nrRounds)
                                        .setThreads(nrOfWorkers)
                                        .setDuration(duration)
                                        .build();
    }
    
    private class Supervisor implements Runnable
    {
        @Override
        public void run()
        {
            try {
                latch.await();
            } catch (InterruptedException ex) {
                SimulationEvent event = new SimulationEvent(SimulationEvent.EVENT_SIM_ERROR, ex);
                notifiable.onSimulationError(event);
                executor.shutdownNow();
                return;
            }
            
            try {
                this.finalizeSimulation();
            } finally {
                isRunning = false;
                if (! executor.isShutdown()) {
                    executor.shutdownNow();
                }
            }
        }
        
        //will be called when all workers are done
        private void finalizeSimulation()
        {
            if (! isSimulationDone()) {
                SimulationEvent event = new SimulationEvent(SimulationEvent.EVENT_SIM_CANCELLED, overallProgress);
                notifiable.onSimulationCancel(event);
                
                return;
            }

            endTime = System.currentTimeMillis();

            buildFinalResult();
            
            SimulationEvent event = new SimulationEvent(SimulationEvent.EVENT_SIM_DONE, simulationResult);
            notifiable.onSimulationDone(event);
        }
    }
}
