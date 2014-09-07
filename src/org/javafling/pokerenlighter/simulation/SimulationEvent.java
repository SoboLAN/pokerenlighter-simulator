package org.javafling.pokerenlighter.simulation;

public class SimulationEvent
{
    public static int EVENT_SIM_STARTED = 1;
    public static int EVENT_SIM_DONE = 2;
    public static int EVENT_SIM_CANCELLED = 3;
    public static int EVENT_SIM_PROGRESS = 4;
    public static int EVENT_SIM_ERROR = 5;
    
    private int eventType;
    private Object eventData;
    
    public SimulationEvent(int eventType, Object eventData)
    {
        this.eventType = eventType;
        this.eventData = eventData;
    }
    
    public int getType()
    {
        return eventType;
    }

    public Object getEventData()
    {
        return eventData;
    }
}
