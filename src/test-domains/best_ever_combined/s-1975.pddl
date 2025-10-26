(define 
(domain satellite) 
(:requirements 
    :equality 
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

(:action macro-calibrate-and-switch_on-0
    :parameters (?s_0 - satellite ?i_0 - instrument ?d_0 - direction)
    :precondition 
    (and
        (on_board ?i_0 ?s_0)
        (calibration_target ?i_0 ?d_0)
        (pointing ?s_0 ?d_0)
        (power_on ?i_0)
        (power_avail ?s_0)
    )
    :effect 
    (and
        (power_on ?i_0)
        (not (calibrated ?i_0))
        (not (power_avail ?s_0))
    )
) 

(:action nonr-switch_off-and-calibrate-1
    :parameters (?i_0 - instrument ?s_0 - satellite ?i - instrument ?d - direction)
    :precondition 
    (and
        (on_board ?i_0 ?s_0)
        (power_on ?i_0)
        (not (= ?i_0 ?i))
        (on_board ?i ?s_0)
        (calibration_target ?i ?d)
        (pointing ?s_0 ?d)
        (power_on ?i)
    )
    :effect 
    (and
        (not (power_on ?i_0))
        (power_avail ?s_0)
        (calibrated ?i)
    )
) 

(:action nonr-take_image-and-calibrate-3
    :parameters (?s_0 - satellite ?d_0 - direction ?i_0 - instrument ?m_0 - mode ?s - satellite ?d - direction)
    :precondition 
    (and
        (calibrated ?i_0)
        (on_board ?i_0 ?s_0)
        (supports ?i_0 ?m_0)
        (power_on ?i_0)
        (pointing ?s_0 ?d_0)
        (not (= ?s_0 ?s))
        (not (= ?d_0 ?d))
        (on_board ?i_0 ?s)
        (calibration_target ?i_0 ?d)
        (pointing ?s ?d)
    )
    :effect 
    (and
        (have_image ?d_0 ?m_0)
        (calibrated ?i_0)
    )
) 

(:action nonr-switch_on-and-calibrate-2
    :parameters (?i_0 - instrument ?s_0 - satellite ?s - satellite ?d - direction)
    :precondition 
    (and
        (on_board ?i_0 ?s_0)
        (power_avail ?s_0)
        (not (= ?s_0 ?s))
        (on_board ?i_0 ?s)
        (calibration_target ?i_0 ?d)
        (pointing ?s ?d)
    )
    :effect 
    (and
        (power_on ?i_0)
        (not (power_avail ?s_0))
        (calibrated ?i_0)
    )
) 

) 
