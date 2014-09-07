package org.javafling.pokerenlighter.simulation.worker;

public interface WorkerNotifiable
{
    public void onSimulationDone(WorkerEvent event);
    public void onSimulationProgress(WorkerEvent event);
    public void onSimulationError(WorkerEvent event);
}
