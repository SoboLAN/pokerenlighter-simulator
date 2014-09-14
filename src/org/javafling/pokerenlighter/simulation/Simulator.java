package org.javafling.pokerenlighter.simulation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.javafling.pokerenlighter.combination.Card;
import org.javafling.pokerenlighter.simulation.SimulationFinalResult.ResultBuilder;
import org.javafling.pokerenlighter.simulation.worker.SimulationWorker;
import org.javafling.pokerenlighter.simulation.worker.SimulationWorkerResult;
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

    public Simulator(PokerType type, int rounds, SimulationNotifiable notifiable)
    {
        if (type == null || rounds <= 0) {
            throw new IllegalArgumentException("invalid arguments");
        }
        
        this.gameType = type;
        this.nrRounds = rounds;
        this.profiles = new ArrayList<>();
        this.communityCards = new Card[5];
        this.workers = new ArrayList<>();
        this.updateInterval = 100;
        this.startTime = this.endTime = this.overallProgress = this.lastUpdatePercentage = 0;
        this.nrOfWorkers = SystemUtils.getNrOfLogicalCPUs();
        this.notifiable = notifiable;
    }
        
    /**
     * Tells the Simulator how often to report progress. The progress will be reported by
     * firing a property change event on all listening PropertyChangeListeners.
     * <br />
     * Note: Calling this method AFTER the Simulator started execution will have no effect. If
     * this method is not called at all, then the progress will be reported only once, when the
     * SimulationWorker is finished (equivalent with calling <code>setUpdateInterval(100)</code>).
     * 
     * @param percentage the update interval. Value must be a strictly positive integer divisible by 100.
     * 
     * @throws IllegalArgumentException if the parameter is an invalid value.
     */
    public void setUpdateInterval(int percentage)
    {
        if (percentage <= 0 || 100 % percentage != 0) {
            throw new IllegalArgumentException("invalid update interval value");
        }
        
        if (startTime == 0) {
            updateInterval = percentage;
        }
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
        
    }

    /**
     * Adds a player to the simulation.
     * <br />
     * Note: if the profile has a set range of 100 %, it will be substituted with a new profile
     * that has random as the hand type.
     * 
     * @param player the player.
     * 
     * @throws NullPointerException if player is null
     * 
     * @throws IllegalArgumentException if at least one of the cards contained within the player's
     * profile are already contained within the previously added profiles or withing the community cards.
     * This exception will be thrown only if the profile has a set hand type of PokerType.EXACTCARDS.
     */
    public void addPlayer(PlayerProfile player)
    {
        if (player == null) {
            throw new NullPointerException("invalid player added");
        }
        
        if (player.getHandType() == HandType.EXACTCARDS) {
            Card[] newCards = player.getCards();
            
            for (Card newCard : newCards) {
                if (isCardInProfiles(newCard) || isCardInCommunity(newCard)) {
                    throw new IllegalArgumentException("card already exists");
                }
            }
        }
        
        //if the player has a range of 100 % set, then it's a random hand
        if (player.getHandType() == HandType.RANGE && player.getRange().getPercentage() == 100) {
            this.profiles.add(new PlayerProfile(HandType.RANDOM, null, null));
        } else {
            this.profiles.add(player);
        }
    }
    
    /**
     * Removes the player given as parameter.
     * 
     * @param player the PlayerProfile to be removed
     * 
     * @return true if the profile has been removed, false otherwise
     * 
     * @since 1.1
     */
    public boolean removePlayer(PlayerProfile player)
    {
        if (player == null) {
            return false;
        }
        
        return this.profiles.remove(player);
    }
    
    public void setFlop(Card[] flopCards)
    {
        if (flopCards == null) {
            throw new NullPointerException("null cards not allowed");
        } else if (flopCards.length != 3) {
            throw new IllegalArgumentException("flop must have 3 cards");
        } else if (flopCards[0] == null || flopCards[1] == null || flopCards[2] == null) {
            throw new NullPointerException("null card not allowed");
        }
        
        if (isCardInProfiles(flopCards[0]) || isCardInProfiles(flopCards[1]) || isCardInProfiles(flopCards[2])) {
            throw new IllegalArgumentException("cards already exist");
        }

        //check if one of the new flop cards is not set as turn or river cards
        if (flopCards[0].equals(communityCards[3])
            || flopCards[0].equals(communityCards[4])
            || flopCards[1].equals(communityCards[3])
            || flopCards[1].equals(communityCards[4])
            || flopCards[2].equals(communityCards[3])
            || flopCards[2].equals(communityCards[4]))
        {
            throw new IllegalArgumentException("cards already exist");
        }
        
        communityCards[0] = flopCards[0];
        communityCards[1] = flopCards[1];
        communityCards[2] = flopCards[2];
    }
    
    /**
     * Removes the flop cards.
     * @since 1.1
     */
    public void removeFlop()
    {
        this.communityCards[0] = null;
        this.communityCards[1] = null;
        this.communityCards[2] = null;
    }
    
    public void setTurn(Card turnCard)
    {
        if (turnCard == null) {
            this.communityCards[3] = null;
            return;
        }
        
        if (isCardInProfiles(turnCard) || isCardInCommunity(turnCard)) {
            throw new IllegalArgumentException("card already exists");
        }
        
        this.communityCards[3] = turnCard;
    }
    
    public void setRiver(Card riverCard)
    {
        if (riverCard == null) {
            this.communityCards[4] = null;
            return;
        }
        
        if (isCardInProfiles(riverCard) || isCardInCommunity(riverCard)) {
            throw new IllegalArgumentException("card already exists");
        }
        
        this.communityCards[4] = riverCard;
    }
    
    public void start()
    {
        if (this.isRunning || this.simulationResult != null) {
            return;
        }
        
        if (isPredictableResult()) {
            throw new IllegalStateException("the simulation result is predictable");
        }
        
        if (! isCorrectCommunitySequence()) {
            throw new IllegalStateException("the community cards sequence is incorrect");
        }

        this.latch = new CountDownLatch(this.nrOfWorkers);
        int roundsPerWorker = getNrOfRoundsPerWorker(this.nrOfWorkers);
        
        this.executor = Executors.newFixedThreadPool(this.nrOfWorkers);
    
        for (int i = 0; i < this.nrOfWorkers; i++) {
            SimulationWorker worker = new SimulationWorker(
                i,
                this.gameType,
                this.profiles,
                this.communityCards,
                roundsPerWorker,
                this
            );
            
            worker.setUpdateInterval(getUpdateInterval(this.nrOfWorkers));
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
    
    private boolean isPredictableResult()
    {
        boolean commSet = true;
        for (int i = 0; i < 5; i++) {
            if (communityCards[i] == null) {
                commSet = false;
                break;
            }
        }
        
        if (commSet) {
            boolean correctPlayerTypes = false;
            for (PlayerProfile profile : profiles) {
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
    
    private boolean isCardInProfiles(Card card)
    {
        for (PlayerProfile profile : this.profiles) {
            if (profile.getHandType() == HandType.EXACTCARDS) {
                if (isCardInArray(card, profile.getCards())) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    private boolean isCardInCommunity(Card card)
    {
        for (Card communityCard : this.communityCards) {
            if (communityCard != null) {
                if (card.equals(communityCard))
                {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    private boolean isCardInArray(Card c, Card[] arr)
    {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].equals(c)) {
                return true;
            }
        }
        
        return false;
    }
    
    private boolean isCorrectCommunitySequence()
    {
        //if turn is set
        if (communityCards[3] != null) {
            //if flop is not set
            if (communityCards[0] == null || communityCards[1] == null || communityCards[2] == null) {
                return false;
            }
        }
        
        //if river is set
        if (communityCards[4] != null) {
            //if turn is not set
            if (communityCards[3] == null) {
                return false;
            }
        }
        
        return true;
    }
    
    private int getNrOfRoundsPerWorker(int workers)
    {
        while (this.nrRounds % workers != 0) {
            this.nrRounds++;
        }
        
        return this.nrRounds / workers;
    }
    
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
    
    private class Supervisor implements Runnable
    {
        @Override
        public void run()
        {
            try {
                latch.await();
            } catch (InterruptedException ex) {
                executor.shutdownNow();
                SimulationEvent event = new SimulationEvent(SimulationEvent.EVENT_SIM_ERROR, ex);
                notifiable.onSimulationError(event);
                return;
            }
            
            this.finalizeSimulation();
        }
        
        //will be called when all workers are done
        private void finalizeSimulation()
        {
            isRunning = false;
            
            if (! isSimulationDone()) {
                SimulationEvent event = new SimulationEvent(SimulationEvent.EVENT_SIM_CANCELLED, overallProgress);
                notifiable.onSimulationCancel(event);
            }

            endTime = System.currentTimeMillis();

            int nrPlayers = profiles.size ();
            
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
            
            ResultBuilder resultBuilder = new SimulationFinalResult.ResultBuilder().setGameType(gameType).
                                                                                    setPlayers(profiles);
            
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
            
            SimulationEvent event = new SimulationEvent(SimulationEvent.EVENT_SIM_DONE, simulationResult);
            notifiable.onSimulationDone(event);
            
            if (! executor.isShutdown()) {
                executor.shutdownNow();
            }
        }
    }
}
