(define 
(domain driverlog) 
(:requirements 
      :typing :equality 
) 
(:types 
    locatable - object 
    driver - locatable 
    object 
    location - object 
    obj - locatable 
    truck - locatable 
) 
(:predicates 
    (at ?obj - locatable ?loc - location)
    (driving ?d - driver ?v - truck)
    (empty ?v - truck)
    (in ?obj1 - obj ?obj - truck)
    (link ?x - location ?y - location)
    (path ?x - location ?y - location)
) 
(:action LOAD-TRUCK
    :parameters (?obj - obj ?truck - truck ?loc - location)
    :precondition 
    (and
        (at ?truck ?loc)
        (at ?obj ?loc)
    )
    :effect 
    (and
        (not (at ?obj ?loc))
        (in ?obj ?truck)
    )
) 

(:action UNLOAD-TRUCK
    :parameters (?obj - obj ?truck - truck ?loc - location)
    :precondition 
    (and
        (at ?truck ?loc)
        (in ?obj ?truck)
    )
    :effect 
    (and
        (not (in ?obj ?truck))
        (at ?obj ?loc)
    )
) 

(:action BOARD-TRUCK
    :parameters (?driver - driver ?truck - truck ?loc - location)
    :precondition 
    (and
        (at ?truck ?loc)
        (at ?driver ?loc)
        (empty ?truck)
    )
    :effect 
    (and
        (not (at ?driver ?loc))
        (driving ?driver ?truck)
        (not (empty ?truck))
    )
) 

(:action DISEMBARK-TRUCK
    :parameters (?driver - driver ?truck - truck ?loc - location)
    :precondition 
    (and
        (at ?truck ?loc)
        (driving ?driver ?truck)
    )
    :effect 
    (and
        (not (driving ?driver ?truck))
        (at ?driver ?loc)
        (empty ?truck)
    )
) 

(:action DRIVE-TRUCK
    :parameters (?truck - truck ?loc-from - location ?loc-to - location ?driver - driver)
    :precondition 
    (and
        (at ?truck ?loc-from)
        (driving ?driver ?truck)
        (link ?loc-from ?loc-to)
    )
    :effect 
    (and
        (not (at ?truck ?loc-from))
        (at ?truck ?loc-to)
    )
) 

(:action WALK
    :parameters (?driver - driver ?loc-from - location ?loc-to - location)
    :precondition 
    (and
        (at ?driver ?loc-from)
        (path ?loc-from ?loc-to)
    )
    :effect 
    (and
        (not (at ?driver ?loc-from))
        (at ?driver ?loc-to)
    )
) 


(:action macro-relaxed-DRIVE-TRUCK-and-DRIVE-TRUCK-174
    :parameters (?truck_0 - truck ?loc-from_0 - location ?loc-to_0 - location ?driver_0 - driver ?loc-to - location)
    :precondition 
    (and
        (at ?truck_0 ?loc-from_0)
        (driving ?driver_0 ?truck_0)
        (link ?loc-from_0 ?loc-to_0)
        (not (= ?loc-from_0 ?loc-to_0))
        (not (= ?loc-from_0 ?loc-to))
        (not (= ?loc-to_0 ?loc-to))
        (link ?loc-to_0 ?loc-to)
    )
    :effect 
    (and
        (at ?truck_0 ?loc-to_0)
        (at ?truck_0 ?loc-to)
    )
) 


) 
