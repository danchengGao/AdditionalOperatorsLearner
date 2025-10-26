(define 
(domain parking) 
(:requirements 
     :strips :typing :equality 
) 
(:types 
    car 
    curb 
) 
(:predicates 
    (at-curb ?car - car)
    (at-curb-num ?car - car ?curb - curb)
    (behind-car ?car - car ?front-car - car)
    (car-clear ?car - car)
    (curb-clear ?curb - curb)
) 
(:action move-curb-to-curb
    :parameters (?car - car ?curbsrc - curb ?curbdest - curb)
    :precondition 
    (and
        (car-clear ?car)
        (curb-clear ?curbdest)
        (at-curb-num ?car ?curbsrc)
    )
    :effect 
    (and
        (not (curb-clear ?curbdest))
        (curb-clear ?curbsrc)
        (at-curb-num ?car ?curbdest)
        (not (at-curb-num ?car ?curbsrc))
    )
) 

(:action move-curb-to-car
    :parameters (?car - car ?curbsrc - curb ?cardest - car)
    :precondition 
    (and
        (car-clear ?car)
        (car-clear ?cardest)
        (at-curb-num ?car ?curbsrc)
        (at-curb ?cardest)
    )
    :effect 
    (and
        (not (car-clear ?cardest))
        (curb-clear ?curbsrc)
        (behind-car ?car ?cardest)
        (not (at-curb-num ?car ?curbsrc))
        (not (at-curb ?car))
    )
) 

(:action move-car-to-curb
    :parameters (?car - car ?carsrc - car ?curbdest - curb)
    :precondition 
    (and
        (car-clear ?car)
        (curb-clear ?curbdest)
        (behind-car ?car ?carsrc)
    )
    :effect 
    (and
        (not (curb-clear ?curbdest))
        (car-clear ?carsrc)
        (at-curb-num ?car ?curbdest)
        (not (behind-car ?car ?carsrc))
        (at-curb ?car)
    )
) 

(:action move-car-to-car
    :parameters (?car - car ?carsrc - car ?cardest - car)
    :precondition 
    (and
        (car-clear ?car)
        (car-clear ?cardest)
        (behind-car ?car ?carsrc)
        (at-curb ?cardest)
    )
    :effect 
    (and
        (not (car-clear ?cardest))
        (car-clear ?carsrc)
        (behind-car ?car ?cardest)
        (not (behind-car ?car ?carsrc))
    )
) 

(:action macro-3-2--762575659
    :parameters (?curb - curb ?car - car ?front-car - car)
    :precondition 
    (and
        (not (curb-clear ?curb))
        (at-curb ?car)
        (behind-car ?car ?front-car)
    )
    :effect 
    (and
        (not (curb-clear ?curb))
        (curb-clear ?curb)
    )
) 

(:action macro-relaxed-move-curb-to-curb-and-move-car-to-car-8
    :parameters (?car_0 - car ?curbsrc_0 - curb ?curbdest_0 - curb)
    :precondition 
    (and
        (car-clear ?car_0)
        (curb-clear ?curbdest_0)
        (at-curb-num ?car_0 ?curbsrc_0)
        (not (= ?curbsrc_0 ?curbdest_0))
        (behind-car ?car_0 ?car_0)
        (at-curb ?car_0)
    )
    :effect 
    (and
        (curb-clear ?curbsrc_0)
        (at-curb-num ?car_0 ?curbdest_0)
        (car-clear ?car_0)
        (behind-car ?car_0 ?car_0)
    )
) 


) 
