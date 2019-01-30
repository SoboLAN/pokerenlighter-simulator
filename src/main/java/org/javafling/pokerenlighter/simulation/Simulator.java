package org.javafling.pokerenlighter.simulation;

import org.javafling.pokerenlighter.combination.Card;
import org.javafling.pokerenlighter.simulation.SimulationFinalResult.ResultBuilder;
import org.javafling.pokerenlighter.simulation.worker.*;
import org.javafling.pokerenlighter.simulation.worker.SimulationWorker.WorkerBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    public Simulator(SimulatorBuilder builder)
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
