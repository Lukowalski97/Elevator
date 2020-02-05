package elevator;

import utils.CartDirection;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

/**
 * Model class of one cart used in system.
 * Its properties are current occupied floor, {@link CartDirection} where cart is heading
 * and separate sets of requests for floors below and above cart.
 */
public class ElevatorCart {

    private final int id;
    private int currentFloor;
    private CartDirection direction;
    private final TreeSet<Integer> upwardRequests;
    private final TreeSet<Integer> downwardRequests;

    ElevatorCart(int id) {
        this.currentFloor = 0;
        this.direction = CartDirection.NONE;
        this.upwardRequests = new TreeSet<>();
        this.downwardRequests = new TreeSet<>(Comparator.reverseOrder());
        this.id = id;
    }

    CartDirection getDirection() {
        return this.direction;
    }

    void setDirection(CartDirection direction) {
        this.direction = direction;
    }

    //Adds request to correct set basing on current floor number.
    // Doesn't add request if current floor is same as request's
    void addRequest(int floorNumber) {

        if (currentFloor > floorNumber) {
            this.downwardRequests.add(floorNumber);
        } else if (currentFloor < floorNumber) {
            this.upwardRequests.add(floorNumber);
        }

    }

    int getId() {
        return id;
    }

    int getRequestsAmount() {
        return upwardRequests.size() + downwardRequests.size();
    }

    int getNextUpRequest() {
        return getNextRequest(this.upwardRequests);
    }

    int getNextDownRequest() {
        return getNextRequest(this.downwardRequests);
    }

    private int getNextRequest(Set<Integer> requests) {
        return requests.iterator().next();
    }

    boolean hasMoreUpRequests() {
        return hasMoreRequests(upwardRequests);
    }

    boolean hasMoreDownRequests() {
        return hasMoreRequests(downwardRequests);
    }

    private boolean hasMoreRequests(Set<Integer> requests) {
        return !requests.isEmpty();
    }

    //Removes next element in one of requests set (set is chosen according to cart's direction)
    void removeNearestRequest() {
        if (direction == CartDirection.UPWARDS) {
            if (hasMoreUpRequests()) {
                upwardRequests.remove(upwardRequests.first());

            }
        } else if (direction == CartDirection.DOWNWARDS) {
            if (hasMoreDownRequests()) {
                this.downwardRequests.remove(downwardRequests.first());

            }
        }
    }

    /**
     * @return current floor number.
     */
    public int getCurrentFloor() {
        return currentFloor;
    }


    void moveUp() {
        this.currentFloor++;
    }

    void moveDown() {
        this.currentFloor--;
    }

    /**
     * @param currentFloor new current floor number
     */
     void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }

    //Returns number of last floor in set corresponding to current direction.
    int getTargetFloor() {
        if (this.direction == CartDirection.UPWARDS) {
            return upwardRequests.first();
        } else if (this.direction == CartDirection.DOWNWARDS) {
            return downwardRequests.first();
        } else {
            return getCurrentFloor();
        }
    }

    /**
     * @return true if current direction is none
     */
    public boolean isIdle() {
        return this.getDirection() == CartDirection.NONE;
    }


    /**
     * @return human-readable data about cart's id, direction, current and target floors.
     */
    public String toString() {
        String directionChar = "-";
        if (this.getDirection() == CartDirection.DOWNWARDS) {
            directionChar = "\\/";
        } else if (this.getDirection() == CartDirection.UPWARDS) {
            directionChar = "/\\";
        }
        return String.format("{[C%d][%s]CF: %d, TF: %d}"
                , getId(), directionChar, this.currentFloor, this.getTargetFloor());
    }

}
