package org.javafling.pokerenlighter.simulation;

/**
 * Instances of this class will be sent by the Simulator as part of its event notification system.
 * @author Radu Murzea
 */
public class SimulationEvent
{
    public static final int EVENT_SIM_STARTED = 1;
    public static final int EVENT_SIM_DONE = 2;
    public static final int EVENT_SIM_CANCELLED = 3;
    public static final int EVENT_SIM_PROGRESS = 4;
    public static final int EVENT_SIM_ERROR = 5;
    
    private int eventType;
    private Object eventData;
    
    /**
     * Creates an event of the specified type which contains the specified data.
     * @param eventType the event type. See the constants in this class for the acceptable values.
     * @param eventData extra information about the event. Can be anything you want.
     */
    public SimulationEvent(int eventType, Object eventData)
    {
        this.eventType = eventType;
        this.eventData = eventData;
    }
    
    /**
     * Returns the type of the event.
     * @return the type of the event.
     */
    public int getType()
    {
        return eventType;
    }

    /**
     * Returns extra information about the event.
     * @return extra information about the event.
     */
    public Object getEventData()
    {
        return eventData;
    }
}
