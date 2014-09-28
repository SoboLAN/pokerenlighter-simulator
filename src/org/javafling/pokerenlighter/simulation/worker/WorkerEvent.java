package org.javafling.pokerenlighter.simulation.worker;

/**
 * Instances of this class will be sent by the Worker as part of its event notification system.
 * @author Radu Murzea
 */
public class WorkerEvent
{
    public static int EVENT_SIMWORKER_DONE = 1;
    public static int EVENT_SIMWORKER_PROGRESS = 2;
    public static int EVENT_SIMWORKER_ERROR = 3;
    
    private int eventType;
    private Object eventData;
    
    /**
     * Creates an event of the specified type which contains the specified data.
     * @param eventType the event type. See the constants in this class for the acceptable values.
     * @param eventData extra information about the event. Can be anything you want.
     * @throws IllegalArgumentException if the event type is not one of the pre-defined constants.
     */
    public WorkerEvent(int eventType, Object eventData)
    {
        if (eventType != EVENT_SIMWORKER_DONE
            && eventType != EVENT_SIMWORKER_ERROR
            && eventType != EVENT_SIMWORKER_PROGRESS
        ) {
            throw new IllegalArgumentException("Invalid event type");
        }
        
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
