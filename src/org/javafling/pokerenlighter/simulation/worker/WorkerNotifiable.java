package org.javafling.pokerenlighter.simulation.worker;

public interface WorkerNotifiable
{
    /**
     * This event will be triggered when the worker completed its job successfully
     * @param event the worker event. It will contain an object of type <code>SimulationWorkerResult</code>
     * which will have within it the results.
     */
    public void onSimulationDone(WorkerEvent event);
    
    /**
     * This event will be triggered when the worker progresses. It won't be called when the worker starts
     * or when it hits 100 %, just intermediary steps.
     * @param event the worker event. It will contain the new job completion percentage.
     */
    public void onSimulationProgress(WorkerEvent event);
    
    /**
     * This event will be triggered when the worker encounteres an error.
     * @param event the worker event. It will contain an Exception that will (hopefully) contain details
     * about what happened.
     */
    public void onSimulationError(WorkerEvent event);
}
