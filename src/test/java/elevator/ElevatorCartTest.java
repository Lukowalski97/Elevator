package elevator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import utils.CartDirection;

import static org.junit.Assert.*;

public class ElevatorCartTest {

    private ElevatorCart cart;

    @Before
    public void setUp() {
        cart = new ElevatorCart(0);
        cart.setCurrentFloor(5);

        cart.addRequest(8);
        cart.addRequest(7);
        cart.addRequest(6);

        cart.addRequest(3);
        cart.addRequest(2);
    }

    @Test
    public void getNextDownRequest() {

        assertEquals(3, cart.getNextDownRequest());
    }

    @Test
    public void getNextUpRequest() {

        assertEquals(6, cart.getNextUpRequest());
    }

    @Test
    public void getTargetFloor() {
        assertEquals(cart.getCurrentFloor(), cart.getTargetFloor());

        cart.setDirection(CartDirection.DOWNWARDS);
        assertEquals(3, cart.getTargetFloor());

        cart.setDirection(CartDirection.UPWARDS);
        assertEquals(6, cart.getTargetFloor());
    }

    @Test
    public void removeRequest() {


        cart.setDirection(CartDirection.UPWARDS);
        assertEquals(6, cart.getNextUpRequest());

        cart.removeNearestRequest();
        assertEquals(7, cart.getNextUpRequest());

        cart.setDirection(CartDirection.DOWNWARDS);
        assertEquals(3, cart.getNextDownRequest());

        cart.removeNearestRequest();
        assertEquals(2, cart.getNextDownRequest());

    }

    @Test
    public void multipleRemoveRequest(){
        cart.setDirection(CartDirection.UPWARDS);
        assertEquals(6, cart.getNextUpRequest());
        cart.removeNearestRequest();
        cart.removeNearestRequest();
        assertEquals(8,cart.getNextUpRequest());

        cart.addRequest(7);
        assertEquals(7,cart.getNextUpRequest());
        cart.removeNearestRequest();
        assertEquals(8,cart.getNextUpRequest());
        cart.removeNearestRequest();
        assertFalse(cart.hasMoreUpRequests());
    }


    @Test
    public void toString1() {
        cart.setDirection(CartDirection.DOWNWARDS);
        Assert.assertEquals(String.format("{[C0][\\/]CF: %d, TF: %d}", cart.getCurrentFloor(), cart.getTargetFloor()),
                cart.toString());

    }
}