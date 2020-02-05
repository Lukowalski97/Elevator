package app;

import java.util.Random;

/**
 * Class used to create {@link Passenger} objects - used to test elevator API.
 */
public class PassengerFactory {

    private final int maxFloors;

    private final Random random;

    private int passengerId;

    /**
     * creates Factory to create {@link Passenger }.
     * @param maxFloors sets maximum number of floors from which passenger can call elevator
     *                or go to using the called one.
     */
    public PassengerFactory(int maxFloors) {
        this.maxFloors = maxFloors;
        this.random = new Random();
        this.passengerId = 0;

    }

    /**
     *
     * @param startingFloor from which passenger will request an elevator
     * @param destinationFloor to which passenger will go
     * @return passenger with given floors parameters and id increased by one with each passenger created.
     */
    public Passenger getPassenger(int startingFloor, int destinationFloor) {
        return new Passenger(startingFloor, destinationFloor, generateNextPassengerName());
    }

    private String generateNextPassengerName() {
        return String.format("P%d", passengerId++);
    }

    /**
     *
     * @return randomized variation of {@link PassengerFactory#getPassenger(int, int)} where both floors are random.
     */
    public Passenger getRandomPassenger() {
        int startingFloor = random.nextInt(maxFloors) + 1;
        int destinationFloor;
        while ((destinationFloor = random.nextInt(maxFloors) + 1) == startingFloor) {

        }

        return getPassenger(startingFloor, destinationFloor);
    }

}


