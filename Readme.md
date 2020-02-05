# Elevator API

#Application informations
##Aplication details
Application provides API for elevator system controller - elevator and utils packages.
It also provides app package used to simulate elevator and passengers behaviour.
In app package there are 3 classes: 
- App with main method used to run simulation. It Reads parameters of simulation from config file,
create needed objects of passengers and elevator and controls the flow of simulation. 
Simulation is divided into steps, in each step passengers are making requests, check if they can
step into elevator's cart and carts make their move between floors if possible. 
- Passenger is a model of passenger which makes one request from one floor and when
he steps into cart he makes requests for his destination floor. 
- PassengerFactory is used to randomly generate new passengers.
##How to run
#### With IDE
To run application with IDE simply import project as Maven project and run main class.
In main folder there is config.properties file which can be used to change simulation parameters.
####As Jar
In  folder there is .jar file which use same config file. 
Simply type "java -jar elevator-1.0.jar" from main folder.
#API features
ElevatorSystem class is the main controller of whole system. 
It provides public API for requesting cart, adding destination request from the cart, 
making step for each cart, and getting information about floors and carts.

ElevatorCart class provides public methods to check current floor and whether cart is moving or not.

Utils package contains two Enums used to indicate request's and cart's direction.

####Main idea of elevator system:
- Passenger pushes button whether he wants to go up or down.
- Elevator system checks request's floor, direction and calculates metric distance for each cart.
- System choose cart with greatest fitness value and assign it to request, adding floor to cart's requests set.
- When passenger is in the cart he can use control panel to choose his destination floor.
- System adds this floor to cart's requests set.

####Metric:
Metric is calculated basing on request's floor, direction and cart's floor and direction:
- When cart is busy its metric has lowest possible value - 0. Busy means that cart has too many requests
when compared to overall amount of requests in system.
- When cart is idle or is heading towards the requests and has same direction as request calculated
metric is highest possible.
- When cart is heading towards the request but has different direction then metric is second best possible.
- When cart is heading away from the call then metric is second worst possible.
Besides two worst cases metrics considers the distance between floors and building's floors amount.

####Requests sets:
Each cart has two sets of request - those above current floor and those below.
If cart is heading upwards then its checking and removing requests from upper floors set 
when it encounters requested floors. 

Cart is going up/down until it reaches highest/lowest floor or direction's corresponding set is empty.
When cart stops it checks the opposite direction's set and if it is not empty cart starts going in this direction.

This idea is similar to LOOK algorithm used in disks scheduling (where head starts from 
first request on the disk and moves towards the last possible request, servicing requests in between).
It is slightly better than going from maximum to minimum floor (in disks from end to another).
This algorithm is compromise between First Come First to Service (which handles requests in order
as they come to controller - it could give very long time to service all requests ) and Shortest
Seek Time First (which executes the shortest paths first but could lead to having requests 
which will not be serviced even forever). Biggest disadvantage of used algorithm is that 
recently serviced floors could wait a little longer if there are no carts nearby, because 
cart that visited them is now going in the opposite way and probably won't be back quickly.

####Other
The idea of busy cart was introduced in order to avoid using the first carts in list for 
too many requests, while the other carts would be idle for most of the time.

It's still simple system as it doesn't recalculate requests (once cart is assigned to a
particular floor it cannot be changed). In addition capacity of carts is not being used in 
this model. Simulation(not API) only allows to add one request per step. Another improvement could be 
spreading carts' starting floors between building (now every cart starts at floor 1).