(define 
(domain matching-bw-typed) 
(:requirements 
    :equality 
) 
(:types 
    block 
    hand 
) 
(:predicates 
    (block-negative ?b - block)
    (block-positive ?b - block)
    (clear ?b - block)
    (empty ?h - hand)
    (hand-negative ?h - hand)
    (hand-positive ?h - hand)
    (holding ?h - hand ?b - block)
    (on ?b1 - block ?b2 - block)
    (on-table ?b - block)
    (solid ?b - block)
) 
(:action pickup
    :parameters (?h - hand ?b - block)
    :precondition 
    (and
        (clear ?b)
        (on-table ?b)
        (empty ?h)
    )
    :effect 
    (and
        (holding ?h ?b)
        (not (on-table ?b))
        (not (clear ?b))
        (not (empty ?h))
    )
) 

(:action putdown-pos-pos
    :parameters (?h - hand ?b - block)
    :precondition 
    (and
        (holding ?h ?b)
        (hand-positive ?h)
        (block-positive ?b)
    )
    :effect 
    (and
        (empty ?h)
        (on-table ?b)
        (clear ?b)
        (not (holding ?h ?b))
    )
) 

(:action putdown-neg-neg
    :parameters (?h - hand ?b - block)
    :precondition 
    (and
        (holding ?h ?b)
        (hand-negative ?h)
        (block-negative ?b)
    )
    :effect 
    (and
        (empty ?h)
        (on-table ?b)
        (clear ?b)
        (not (holding ?h ?b))
    )
) 

(:action putdown-pos-neg
    :parameters (?h - hand ?b - block)
    :precondition 
    (and
        (holding ?h ?b)
        (hand-positive ?h)
        (block-negative ?b)
    )
    :effect 
    (and
        (empty ?h)
        (on-table ?b)
        (clear ?b)
        (not (solid ?b))
        (not (holding ?h ?b))
    )
) 

(:action putdown-neg-pos
    :parameters (?h - hand ?b - block)
    :precondition 
    (and
        (holding ?h ?b)
        (hand-negative ?h)
        (block-positive ?b)
    )
    :effect 
    (and
        (empty ?h)
        (on-table ?b)
        (not (solid ?b))
        (clear ?b)
        (not (holding ?h ?b))
    )
) 

(:action stack-pos-pos
    :parameters (?h - hand ?b - block ?underb - block)
    :precondition 
    (and
        (clear ?underb)
        (solid ?underb)
        (holding ?h ?b)
        (hand-positive ?h)
        (block-positive ?b)
    )
    :effect 
    (and
        (empty ?h)
        (on ?b ?underb)
        (clear ?b)
        (not (clear ?underb))
        (not (holding ?h ?b))
    )
) 

(:action stack-neg-neg
    :parameters (?h - hand ?b - block ?underb - block)
    :precondition 
    (and
        (clear ?underb)
        (solid ?underb)
        (holding ?h ?b)
        (hand-negative ?h)
        (block-negative ?b)
    )
    :effect 
    (and
        (empty ?h)
        (on ?b ?underb)
        (clear ?b)
        (not (clear ?underb))
        (not (holding ?h ?b))
    )
) 

(:action stack-pos-neg
    :parameters (?h - hand ?b - block ?underb - block)
    :precondition 
    (and
        (clear ?underb)
        (solid ?underb)
        (holding ?h ?b)
        (hand-positive ?h)
        (block-negative ?b)
    )
    :effect 
    (and
        (empty ?h)
        (on ?b ?underb)
        (not (solid ?b))
        (clear ?b)
        (not (clear ?underb))
        (not (holding ?h ?b))
    )
) 

(:action stack-neg-pos
    :parameters (?h - hand ?b - block ?underb - block)
    :precondition 
    (and
        (clear ?underb)
        (solid ?underb)
        (holding ?h ?b)
        (hand-negative ?h)
        (block-positive ?b)
    )
    :effect 
    (and
        (empty ?h)
        (on ?b ?underb)
        (not (solid ?b))
        (clear ?b)
        (not (clear ?underb))
        (not (holding ?h ?b))
    )
) 

(:action unstack
    :parameters (?h - hand ?b - block ?underb - block)
    :precondition 
    (and
        (on ?b ?underb)
        (clear ?b)
        (empty ?h)
    )
    :effect 
    (and
        (holding ?h ?b)
        (clear ?underb)
        (not (clear ?b))
        (not (on ?b ?underb))
        (not (empty ?h))
    )
) 

(:action macro-break-mutex-group-1339433973
    :parameters (?b - block)
    :precondition 
    (not (on-table ?b))
    :effect 
    (on-table ?b)
) 

(:action macro-break-mutex-group--850707572
    :parameters (?h - hand)
    :precondition 
    (not (empty ?h))
    :effect 
    (empty ?h)
) 

(:action macro-break-mutex-group--438864681
    :parameters (?h - hand)
    :precondition 
    (hand-negative ?h)
    :effect 
    (not (hand-negative ?h))
) 

(:action macro-break-mutex-group-1608792779
    :parameters (?h - hand)
    :precondition 
    (hand-positive ?h)
    :effect 
    (not (hand-positive ?h))
) 

(:action nonr-unstack-and-putdown-neg-pos-10
    :parameters (?h_0 - hand ?b_0 - block ?underb_0 - block)
    :precondition 
    (and
        (on ?b_0 ?underb_0)
        (clear ?b_0)
        (empty ?h_0)
        (not (= ?b_0 ?underb_0))
        (hand-negative ?h_0)
        (block-positive ?b_0)
    )
    :effect 
    (and
        (clear ?underb_0)
        (not (on ?b_0 ?underb_0))
        (empty ?h_0)
        (on-table ?b_0)
        (not (solid ?b_0))
        (clear ?b_0)
        (not (holding ?h_0 ?b_0))
    )
) 

(:action nonr-stack-pos-neg-and-stack-pos-neg-61
    :parameters (?h_0 - hand ?b_0 - block ?h - hand ?b - block)
    :precondition 
    (and
        (clear ?b_0)
        (solid ?b_0)
        (holding ?h_0 ?b_0)
        (hand-positive ?h_0)
        (block-negative ?b_0)
        (not (= ?h_0 ?h))
        (not (= ?b_0 ?b))
        (clear ?b)
        (solid ?b)
        (holding ?h ?b)
        (hand-positive ?h)
        (block-negative ?b)
    )
    :effect 
    (and
        (empty ?h_0)
        (on ?b_0 ?b_0)
        (not (solid ?b_0))
        (clear ?b_0)
        (not (clear ?b_0))
        (not (holding ?h_0 ?b_0))
        (empty ?h)
        (on ?b ?b)
        (not (solid ?b))
        (clear ?b)
        (not (clear ?b))
        (not (holding ?h ?b))
    )
) 

(:action nonr-putdown-pos-pos-and-stack-pos-neg-10
    :parameters (?h_0 - hand ?b_0 - block ?b - block)
    :precondition 
    (and
        (holding ?h_0 ?b_0)
        (hand-positive ?h_0)
        (block-positive ?b_0)
        (not (= ?b_0 ?b))
        (solid ?b_0)
        (holding ?h_0 ?b)
        (block-negative ?b)
    )
    :effect 
    (and
        (empty ?h_0)
        (on-table ?b_0)
        (not (holding ?h_0 ?b_0))
        (empty ?h_0)
        (on ?b ?b_0)
        (not (solid ?b))
        (clear ?b)
        (not (clear ?b_0))
        (not (holding ?h_0 ?b))
    )
) 

) 
