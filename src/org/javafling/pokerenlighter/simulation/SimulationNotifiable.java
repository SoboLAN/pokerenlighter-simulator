package org.javafling.pokerenlighter.simulation;

public interface SimulationNotifiable
{
    public void onSimulationStart(SimulationEvent event);
    public void onSimulationDone(SimulationEvent event);
    public void onSimulationCancel(SimulationEvent event);
    public void onSimulationProgress(SimulationEvent event);
    public void onSimulationError(SimulationEvent event);
}
