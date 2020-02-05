package elevator;

import utils.CartDirection;
import utils.RequestDirection;

import java.util.ArrayList;
import java.util.List;


/**
 * Class responsible for controlling every cart in elevator.
 * Contains information about floors and carts amount.
 * Operates on list of {@link ElevatorCart}.
 */
public class ElevatorSystem {

    private final int cartsAmount;
    private final int floorsAmount;

    private final List<ElevatorCart> cartsList;

    public ElevatorSystem(int cartsAmount, int floorsAmount) {

        this.cartsList = new ArrayList<>();
        this.floorsAmount = floorsAmount;
        this.cartsAmount = cartsAmount;

        for (int i = 0; i < cartsAmount; i++) {
            cartsList.add(new ElevatorCart(i));
        }

    }

    /**
     * Responsible for assigning cart with most suitable distance from floor where
     * button was pressed.
     *
     * @param floorNumber      number of floor where passenger pressed the button.
     * @param requestDirection direction choosed by passenger (up or down).
     * @return id of cart assigned to request.
     */
    public int requestCart(int floorNumber, RequestDirection requestDirection) {

        ElevatorCart requestedCart = findMostSuitableCart(floorNumber, requestDirection);

        requestedCart.addRequest(floorNumber);

        return cartsList.indexOf(requestedCart);
    }


    //Calculates metric for chosen cart, based on floor and direction where call was made.
    private int getCartFitness(ElevatorCart elevatorCart, int requestFloorNumber, RequestDirection requestDirection) {

        if (isCartRelativelyBusy(elevatorCart)) {
            return getBusyFitness();
        }

        if (elevatorCart.isIdle()) {

            return getIdleOrSameDirectionFitness(elevatorCart.getCurrentFloor(), requestFloorNumber);

        } else if (isCartTowardsCall(elevatorCart.getCurrentFloor(), elevatorCart.getDirection(), requestFloorNumber)) {

            if (areCartAndRequestDirectionsEqual(elevatorCart.getDirection(), requestDirection)) {

                return getIdleOrSameDirectionFitness(elevatorCart.getCurrentFloor(), requestFloorNumber);

            } else {

                return getOppositeDirectionFitness(elevatorCart.getCurrentFloor(), requestFloorNumber);

            }
        } else {

            return getAwayFromRequestFitness();

        }
    }

    //Returns true if cart has more than 0 requests and its requests amount is higher than average
    // amount of requests in elevator.
    private boolean isCartRelativelyBusy(ElevatorCart cart) {
        return cart.getRequestsAmount() > 0 && cart.getRequestsAmount() > sumRequestsAmount() / cartsAmount;
    }

    // Returns true if cart is heading toward the requested floor (or if it is on the same floor).
    private boolean isCartTowardsCall(int cartFloor, CartDirection cartDirection,
                                      int requestFloor) {

        return cartFloor >= requestFloor && cartDirection == CartDirection.DOWNWARDS ||
                cartFloor <= requestFloor && cartDirection == CartDirection.UPWARDS;
    }

    //Returns true if cart's direction is same as request's direction.
    private boolean areCartAndRequestDirectionsEqual(CartDirection cartDirection, RequestDirection requestDirection) {
        return cartDirection == CartDirection.UPWARDS && requestDirection == RequestDirection.UPWARDS ||
                cartDirection == CartDirection.DOWNWARDS && requestDirection == RequestDirection.DOWNWARDS;
    }

    // Metric method
    private int getIdleOrSameDirectionFitness(int cartFloorNumber, int requestFloorNumber) {
        return floorsAmount + 1 - Math.abs(cartFloorNumber - requestFloorNumber);
    }

    //Metric method
    private int getOppositeDirectionFitness(int cartFloorNumber, int requestFloorNumber) {
        return floorsAmount - Math.abs(cartFloorNumber - requestFloorNumber);
    }

    //Metric method
    private int getAwayFromRequestFitness() {
        return 1;
    }

    //Metric method
    private int getBusyFitness() {
        return 0;
    }

    //Sum currently active requests in every cart.
    private int sumRequestsAmount() {
        return cartsList.stream().mapToInt(ElevatorCart::getRequestsAmount).sum();
    }


    //Returns best fitted cart for given direction and floor number.
    private ElevatorCart findMostSuitableCart(int floorNumber, RequestDirection requestDirection) {
        int bestCartFitness = 0;
        ElevatorCart outp = null;
        for (ElevatorCart cart : cartsList) {
            int cartFitness = getCartFitness(cart, floorNumber, requestDirection);
            if (cartFitness > bestCartFitness) {
                bestCartFitness = cartFitness;
                outp = cart;
            }
        }
        if (outp == null) {
            throw new RuntimeException(String.format("No suitable cart found to handle %s request from %d floor",
                    requestDirection.toSring(), floorNumber));
        }
        return outp;
    }

    /**
     * Responsible for choosing floor when passenger is inside cart.
     *
     * @param cartId      id of cart whose requests list is being changed
     * @param floorNumber number of passenger's destination floor.
     */
    public void makeDestinationRequest(int cartId, int floorNumber) {
        this.cartsList.get(cartId).addRequest(floorNumber);
    }


    /**
     * Responsible for simulating one step for each cart.
     * Each cart is moving zero or one floors up/down and changes direction if has to.
     */
    public void makeStep() {
        for (ElevatorCart cart : cartsList) {
            switch (cart.getDirection()) {
                case UPWARDS:
                    makeStepUpDirection(cart);
                    break;

                case DOWNWARDS:
                    makeStepDownDirection(cart);
                    break;

                case NONE:
                    makeStepNoneDirection(cart);
                    break;

            }
        }
    }

    //Method that simulates one step downwards for one cart.
    private void makeStepDownDirection(ElevatorCart cart) {

        if (cart.getCurrentFloor() > 0) {
            cart.moveDown();
        }
        if (cart.hasMoreDownRequests()) {
            int nextRequestFloor = cart.getNextDownRequest();
            if (nextRequestFloor == cart.getCurrentFloor()) {
                cart.removeNearestRequest();
            }
        }

        //no more requests on lower floors
        if (!cart.hasMoreDownRequests()) {
            //If possible start riding up
            if (cart.hasMoreUpRequests()) {
                cart.setDirection(CartDirection.UPWARDS);
            } else {
                cart.setDirection(CartDirection.NONE);
            }
        }
    }

    //Method that simulates one step upwards for one cart.
    private void makeStepUpDirection(ElevatorCart cart) {

        if (cart.getCurrentFloor() < floorsAmount) {
            cart.moveUp();
        }

        if (cart.hasMoreUpRequests()) {
            int nextRequestFloor = cart.getNextUpRequest();
            if (nextRequestFloor == cart.getCurrentFloor()) {
                cart.removeNearestRequest();
            }
        }

        //no more requests on upper floors
        if (!cart.hasMoreUpRequests()) {
            //If possible start riding down
            if (cart.hasMoreDownRequests()) {
                cart.setDirection(CartDirection.DOWNWARDS);
            } else {
                cart.setDirection(CartDirection.NONE);
            }
        }

    }

    //Method that simulates one step for idle cart.
    private void makeStepNoneDirection(ElevatorCart cart) {

        if (cart.hasMoreUpRequests() && cart.getCurrentFloor() < floorsAmount) {

            cart.setDirection(CartDirection.UPWARDS);

        } else if (cart.hasMoreDownRequests() && cart.getCurrentFloor() > 0) {
            cart.setDirection(CartDirection.DOWNWARDS);
        }

    }

    /**
     * @return list of elevator's carts - {@link ElevatorCart}.
     */
    public List<ElevatorCart> getCartsList() {
        return this.cartsList;
    }

    /**
     * @return amount of carts in elevator.
     */
    public int getCartsAmount() {
        return this.cartsAmount;
    }

    /**
     * @return amount of floors in building.
     */
    public int getFloorsAmount() {
        return this.floorsAmount;
    }

    /**
     * @return human-readable data about elevator's carts and floors amount.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Elevator shaft has %d carts, and handles %d floors. Carts are:%n",
                this.cartsAmount, this.floorsAmount));
        cartsList.forEach(cart -> {
            sb.append(cart.toString());
            sb.append(String.format("%n"));
        });
        return sb.toString();
    }

}
