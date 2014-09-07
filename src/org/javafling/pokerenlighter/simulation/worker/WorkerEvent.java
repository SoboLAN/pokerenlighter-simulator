package org.javafling.pokerenlighter.simulation.worker;

public class WorkerEvent
{
    public static int EVENT_SIMWORKER_DONE = 1;
    public static int EVENT_SIMWORKER_PROGRESS = 2;
    public static int EVENT_SIMWORKER_ERROR = 3;
    
    private int eventType;
    private Object eventData;
    
    public WorkerEvent(int eventType, Object eventData)
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
