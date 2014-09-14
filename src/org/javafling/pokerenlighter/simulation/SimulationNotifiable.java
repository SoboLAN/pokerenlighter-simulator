package org.javafling.pokerenlighter.simulation;

/**
 * Any class that wants to be notified by the Simulator of its events (and results) must implement this interface
 * and send an instance of that particular class to the Simulator.
 * @author Radu Murzea
 */
public interface SimulationNotifiable
{
    /**
     * This event will be triggered when the simulation starts.
     * @param event the simulation event. It will contain the number of threads that will be used for the simulation
     */
    public void onSimulationStart(SimulationEvent event);
    
    /**
     * This event will be triggered when the simulation is completed successfully
     * @param event the simulation event. It will contain an object of type SimulationFinalResult
     * which will have within it the results and how long the simulation took.
     */
    public void onSimulationDone(SimulationEvent event);
    
    /**
     * This event will be triggered when the simulation is stopped before it could be completed.
     * @param event the simulation event. It will contain the simulation's completion percentage
     * at the time of the simulation stop.
     */
    public void onSimulationCancel(SimulationEvent event);
    
    /**
     * This event will be triggered when the simulation progresses. It won't be called when the simulation starts
     * or when it hits 100 %, just intermediary steps.
     * @param event the simulation event. It will contain the new simulation completion percentage.
     */
    public void onSimulationProgress(SimulationEvent event);
    
    /**
     * This event will be triggered when the simulation encounteres an error.
     * @param event the simulation event. It will contain an Exception that will (hopefully) contain details
     * about what happened.
     */
    public void onSimulationError(SimulationEvent event);
}
