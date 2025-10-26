(define 
(domain satellite) 
(:requirements 
      :strips :equality :typing 
) 
(:types 
    direction 
    instrument 
    mode 
    satellite 
) 
(:predicates 
    (calibrated ?i - instrument)
    (calibration_target ?i - instrument ?d - direction)
    (have_image ?d - direction ?m - mode)
    (on_board ?i - instrument ?s - satellite)
    (pointing ?s - satellite ?d - direction)
    (power_avail ?s - satellite)
    (power_on ?i - instrument)
    (supports ?i - instrument ?m - mode)
) 
(:action turn_to
    :parameters (?s - satellite ?d_new - direction ?d_prev - direction)
    :precondition 
    (and
        (pointing ?s ?d_prev)
        (not (= ?d_new ?d_prev))
    )
    :effect 
    (and
        (pointing ?s ?d_new)
        (not (pointing ?s ?d_prev))
    )
) 

(:action switch_on
    :parameters (?i - instrument ?s - satellite)
    :precondition 
    (and
        (on_board ?i ?s)
        (power_avail ?s)
    )
    :effect 
    (and
        (power_on ?i)
        (not (calibrated ?i))
        (not (power_avail ?s))
    )
) 

(:action switch_off
    :parameters (?i - instrument ?s - satellite)
    :precondition 
    (and
        (on_board ?i ?s)
        (power_on ?i)
    )
    :effect 
    (and
        (not (power_on ?i))
        (power_avail ?s)
    )
) 

(:action calibrate
    :parameters (?s - satellite ?i - instrument ?d - direction)
    :precondition 
    (and
        (on_board ?i ?s)
        (calibration_target ?i ?d)
        (pointing ?s ?d)
        (power_on ?i)
    )
    :effect 
    (calibrated ?i)
) 

(:action take_image
    :parameters (?s - satellite ?d - direction ?i - instrument ?m - mode)
    :precondition 
    (and
        (calibrated ?i)
        (on_board ?i ?s)
        (supports ?i ?m)
        (power_on ?i)
        (pointing ?s ?d)
        (power_on ?i)
    )
    :effect 
    (have_image ?d ?m)
) 

(:action macro-relaxed-switch_off-and-calibrate-0
    :parameters (?i_0 - instrument ?s_0 - satellite ?d - direction)
    :precondition 
    (and
        (on_board ?i_0 ?s_0)
        (power_on ?i_0)
        (calibration_target ?i_0 ?d)
        (pointing ?s_0 ?d)
    )
    :effect 
    (and
        (power_avail ?s_0)
        (calibrated ?i_0)
    )
) 


) 
