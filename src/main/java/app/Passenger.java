package app;

import utils.RequestDirection;

/**
 * Class used to test elevator API.
 */
public class Passenger {

    private final int startingFloor;
    private final int destinationFloor;
    private final String name;
    private  int assignedCartId;
    private final RequestDirection direction;

    /**
     * Creates passenger with given floors and name parameters.
     * @param startingFloor from which elevator will be requestes.
     * @param destinationFloor to which passenger wants to ride with elevator.
     * @param name passenger's name used to distinguish him.
     */
    public Passenger(int startingFloor, int destinationFloor,String name){
        this.startingFloor=startingFloor;
        this.destinationFloor=destinationFloor;
        this.name=name;
        this.assignedCartId=-1;
        this.direction= calculateDirection();
    }

    /**
     *
     * @return ID of cart which passenger will ride.
     */
    public int getAssignedCartId(){
        return assignedCartId;
    }

    /**
     *
     * @param assignedCartId ID of cart which passenger will ride.
     */
    public void setAssignedCartId(int assignedCartId){
        this.assignedCartId=assignedCartId;
    }

    /**
     *
     * @return passenger's name
     */
    public String getName(){
        return name;
    }

    /**
     *
     * @return floor number where passenger requests elevator.
     */
    public int getStartingFloor() {
        return startingFloor;
    }

    /**
     *
     * @return floor number where passenger wants to go using elevator.
     */
    public int getDestinationFloor() {
        return destinationFloor;
    }

    /**
     *
     * @return human-readable data about passenger's name,floors and assigned cart's ID.
     */
    public String toString(){
        return String.format("{%s(%d => %d)[%d]}",name,startingFloor,destinationFloor,assignedCartId);
    }

    /**
     *
     * @return the direction of passenger's request based on starting and destination floors compared.
     */
    public RequestDirection getDirection(){
        return direction;
    }

    private  RequestDirection calculateDirection() {
        if (startingFloor > destinationFloor) {
            return RequestDirection.DOWNWARDS;
        } else {
            return RequestDirection.UPWARDS;
        }
    }

}
