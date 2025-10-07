(define 
(domain Rover) 
(:requirements 
    :typing :equality 
) 
(:types 
    camera 
    lander 
    mode 
    objective 
    rover 
    store 
    waypoint 
) 
(:predicates 
    (at ?x - rover ?y - waypoint)
    (at_lander ?x - lander ?y - waypoint)
    (at_rock_sample ?w - waypoint)
    (at_soil_sample ?w - waypoint)
    (available ?r - rover)
    (calibrated ?c - camera ?r - rover)
    (calibration_target ?i - camera ?o - objective)
    (can_traverse ?r - rover ?x - waypoint ?y - waypoint)
    (channel_free ?l - lander)
    (communicated_image_data ?o - objective ?m - mode)
    (communicated_rock_data ?w - waypoint)
    (communicated_soil_data ?w - waypoint)
    (empty ?s - store)
    (equipped_for_imaging ?r - rover)
    (equipped_for_rock_analysis ?r - rover)
    (equipped_for_soil_analysis ?r - rover)
    (full ?s - store)
    (have_image ?r - rover ?o - objective ?m - mode)
    (have_rock_analysis ?r - rover ?w - waypoint)
    (have_soil_analysis ?r - rover ?w - waypoint)
    (on_board ?i - camera ?r - rover)
    (store_of ?s - store ?r - rover)
    (supports ?c - camera ?m - mode)
    (visible ?w - waypoint ?p - waypoint)
    (visible_from ?o - objective ?w - waypoint)
) 
(:action navigate
    :parameters (?x - rover ?y - waypoint ?z - waypoint)
    :precondition 
    (and
        (can_traverse ?x ?y ?z)
        (available ?x)
        (at ?x ?y)
        (visible ?y ?z)
    )
    :effect 
    (and
        (not (at ?x ?y))
        (at ?x ?z)
    )
) 

(:action sample_soil
    :parameters (?x - rover ?s - store ?p - waypoint)
    :precondition 
    (and
        (at ?x ?p)
        (at_soil_sample ?p)
        (equipped_for_soil_analysis ?x)
        (store_of ?s ?x)
        (empty ?s)
    )
    :effect 
    (and
        (not (empty ?s))
        (full ?s)
        (have_soil_analysis ?x ?p)
        (not (at_soil_sample ?p))
    )
) 

(:action sample_rock
    :parameters (?x - rover ?s - store ?p - waypoint)
    :precondition 
    (and
        (at ?x ?p)
        (at_rock_sample ?p)
        (equipped_for_rock_analysis ?x)
        (store_of ?s ?x)
        (empty ?s)
    )
    :effect 
    (and
        (not (empty ?s))
        (full ?s)
        (have_rock_analysis ?x ?p)
        (not (at_rock_sample ?p))
    )
) 

(:action drop
    :parameters (?x - rover ?y - store)
    :precondition 
    (and
        (store_of ?y ?x)
        (full ?y)
    )
    :effect 
    (and
        (not (full ?y))
        (empty ?y)
    )
) 

(:action calibrate
    :parameters (?r - rover ?i - camera ?t - objective ?w - waypoint)
    :precondition 
    (and
        (equipped_for_imaging ?r)
        (calibration_target ?i ?t)
        (at ?r ?w)
        (visible_from ?t ?w)
        (on_board ?i ?r)
    )
    :effect 
    (calibrated ?i ?r)
) 

(:action take_image
    :parameters (?r - rover ?p - waypoint ?o - objective ?i - camera ?m - mode)
    :precondition 
    (and
        (calibrated ?i ?r)
        (on_board ?i ?r)
        (equipped_for_imaging ?r)
        (supports ?i ?m)
        (visible_from ?o ?p)
        (at ?r ?p)
    )
    :effect 
    (and
        (have_image ?r ?o ?m)
        (not (calibrated ?i ?r))
    )
) 

(:action communicate_soil_data
    :parameters (?r - rover ?l - lander ?p - waypoint ?x - waypoint ?y - waypoint)
    :precondition 
    (and
        (at ?r ?x)
        (at_lander ?l ?y)
        (have_soil_analysis ?r ?p)
        (visible ?x ?y)
        (available ?r)
        (channel_free ?l)
    )
    :effect 
    (and
        (not (available ?r))
        (not (channel_free ?l))
        (channel_free ?l)
        (communicated_soil_data ?p)
        (available ?r)
    )
) 

(:action communicate_rock_data
    :parameters (?r - rover ?l - lander ?p - waypoint ?x - waypoint ?y - waypoint)
    :precondition 
    (and
        (at ?r ?x)
        (at_lander ?l ?y)
        (have_rock_analysis ?r ?p)
        (visible ?x ?y)
        (available ?r)
        (channel_free ?l)
    )
    :effect 
    (and
        (not (available ?r))
        (not (channel_free ?l))
        (channel_free ?l)
        (communicated_rock_data ?p)
        (available ?r)
    )
) 

(:action communicate_image_data
    :parameters (?r - rover ?l - lander ?o - objective ?m - mode ?x - waypoint ?y - waypoint)
    :precondition 
    (and
        (at ?r ?x)
        (at_lander ?l ?y)
        (have_image ?r ?o ?m)
        (visible ?x ?y)
        (available ?r)
        (channel_free ?l)
    )
    :effect 
    (and
        (not (available ?r))
        (not (channel_free ?l))
        (channel_free ?l)
        (communicated_image_data ?o ?m)
        (available ?r)
    )
) 

(:action macro-2-2-2125278790
    :parameters (?s - store ?r - rover ?w - waypoint)
    :precondition 
    (and
        (store_of ?s ?r)
        (have_rock_analysis ?r ?w)
    )
    :effect 
    (and
        (communicated_rock_data ?w)
        (not (equipped_for_soil_analysis ?r))
    )
) 

(:action macro-2-3-1029023178
    :parameters (?l - lander ?w - waypoint ?p - waypoint ?o - objective ?s - store ?r - rover)
    :precondition 
    (and
        (not (channel_free ?l))
        (at_soil_sample ?w)
    )
    :effect 
    (and
        (not (visible ?w ?p))
        (visible_from ?o ?w)
        (store_of ?s ?r)
    )
) 

) 
