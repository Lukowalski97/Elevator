package app;

import elevator.ElevatorCart;
import elevator.ElevatorSystem;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


/**
 * Application class with main method which simulates behaviour of passengers and elevator.
 */
public class App {


    /**
     * Class used to test elevator API.
     *
     * @param args
     */

    private static PassengerFactory passengerFactory;
    private static ElevatorSystem elevatorSystem;
    private static List<Passenger> waitingPassengers;
    private static final String configFileName = "config.properties";
    private static int cartsAmount = 16;
    private static int floorsAmount = 50;
    private static int minStepsAmount = 20;
    private static int passengersAmount = 70;
    private static int passengersFrequency = 2;

    public static void main(String[] args) throws Exception {

        readParams();

        System.out.println(getParamsString());

        Thread.sleep(2000);

        callElevatorApi();

    }

    private static void readParams() {
        InputStream input = null;
        try {
            input = new FileInputStream(Paths.get(configFileName).toString());
            Properties properties = new Properties();
            properties.load(input);
            cartsAmount = Integer.parseInt(properties.getProperty("cartsAmount"));
            floorsAmount = Integer.parseInt(properties.getProperty("floorsAmount"));
            minStepsAmount = Integer.parseInt(properties.getProperty("minStepsAmount"));
            passengersAmount = Integer.parseInt(properties.getProperty("passengersAmount"));
            passengersFrequency = Integer.parseInt(properties.getProperty("passengersFrequency"));


        } catch (FileNotFoundException ex) {
            System.err.println(String.format("File %s not found!- Using parameters from program!", configFileName));
            ex.printStackTrace();
        } catch (IOException ex) {
            System.err.println(String.format("Problem reading from file: %s!- Using parameters from program!",
                    configFileName));
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException ex) {
                    System.err.println("Problem while closing config file!");
                }
            }
        }
    }


    private static String getParamsString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Current Parameters are:\n");
        sb.append(String.format("Amount of carts: %d%n", cartsAmount));
        sb.append(String.format("Amount of floors: %d%n", floorsAmount));
        sb.append(String.format("Minimum amount of steps in simulation: %d%n", minStepsAmount));
        sb.append(String.format("Amount of passengers: %d%n", passengersAmount));
        sb.append(String.format(
                "Passengers requests frequency: %d(it means each %d simulation steps new request will occur)%n%n"
                , passengersFrequency, passengersFrequency));
        return sb.toString();
    }

    private static void callElevatorApi() {


        elevatorSystem = new ElevatorSystem(cartsAmount, floorsAmount);
        passengerFactory = new PassengerFactory(floorsAmount);
        List<Passenger> passengerList = new ArrayList<>();
        waitingPassengers = new ArrayList<>();

        for (int i = 0; i < passengersAmount; i++) {
            passengerList.add(passengerFactory.getRandomPassenger());
        }

        int i = 0;
        int j = 0;

        while (!waitingPassengers.isEmpty() || minStepsAmount > j || !isEveryCartIdle()) {

            if (((j++ % passengersFrequency) == 0) && i < passengersAmount) {

                makePassengerRequest(passengerList.get(i++));

            }
            checkAndMakeRequestsForActivePassengers();
            elevatorSystem.makeStep();

            System.out.println(String.format("Step %d:", j + 1));
            System.out.println(elevatorSystem.getCartsList());
            System.out.println(waitingPassengers);

        }

    }


    private static void makePassengerRequest(Passenger passenger) {
        passenger.setAssignedCartId(elevatorSystem.requestCart(passenger.getStartingFloor()
                , passenger.getDirection()));

        waitingPassengers.add(passenger);
    }

    private static boolean isEveryCartIdle() {
        for (ElevatorCart cart : elevatorSystem.getCartsList()) {
            if (!cart.isIdle()) {
                return false;
            }
        }
        return true;
    }

    //For every passenger checks if cart assigned to him is now on his floor.
    //If it is true passenger is adding requests for his destination floor and becomes no more active.
    private static void checkAndMakeRequestsForActivePassengers() {
        List<Passenger> passengersToRemove = new ArrayList<>();
        for (Passenger passenger : waitingPassengers) {
            if (elevatorSystem.getCartsList().get(passenger.getAssignedCartId()).getCurrentFloor() == passenger.getStartingFloor()) {
                elevatorSystem.makeDestinationRequest(passenger.getAssignedCartId(), passenger.getDestinationFloor());
                passengersToRemove.add(passenger);
            }
        }
        waitingPassengers.removeAll(passengersToRemove);
    }


}


