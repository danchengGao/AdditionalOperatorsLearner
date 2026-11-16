(define 
(domain matching-bw-typed) 
(:requirements 
    :typing :equality 
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

(:action macro-3-3--1915910974
    :parameters (?b - block ?h - hand)
    :precondition 
    (and
        (not (solid ?b))
        (clear ?b)
    )
    :effect 
    (and
        (not (hand-positive ?h))
        (clear ?b)
        (holding ?h ?b)
    )
) 

(:action macro-2-3--1338183311
    :parameters (?b - block ?h - hand)
    :precondition 
    (and
        (block-negative ?b)
        (not (block-positive ?b))
    )
    :effect 
    (and
        (block-positive ?b)
        (not (solid ?b))
        (not (hand-negative ?h))
    )
) 


) 
