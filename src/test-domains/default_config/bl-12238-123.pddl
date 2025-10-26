(define 
(domain blocksworld) 
(:requirements 
      :strips :typing :equality 
) 
(:types 
    block 
) 
(:predicates 
    (clear ?x - block)
    (handempty)
    (holding ?x - block)
    (on ?x - block ?y - block)
    (ontable ?x - block)
) 
(:action pick-up
    :parameters (?x - block)
    :precondition 
    (and
        (clear ?x)
        (ontable ?x)
        (handempty)
    )
    :effect 
    (and
        (not (ontable ?x))
        (not (clear ?x))
        (not (handempty))
        (holding ?x)
    )
) 

(:action put-down
    :parameters (?x - block)
    :precondition 
    (holding ?x)
    :effect 
    (and
        (not (holding ?x))
        (clear ?x)
        (handempty)
        (ontable ?x)
    )
) 

(:action stack
    :parameters (?x - block ?y - block)
    :precondition 
    (and
        (holding ?x)
        (clear ?y)
    )
    :effect 
    (and
        (not (holding ?x))
        (not (clear ?y))
        (clear ?x)
        (handempty)
        (on ?x ?y)
    )
) 

(:action unstack
    :parameters (?x - block ?y - block)
    :precondition 
    (and
        (on ?x ?y)
        (clear ?x)
        (handempty)
    )
    :effect 
    (and
        (holding ?x)
        (clear ?y)
        (not (clear ?x))
        (not (handempty))
        (not (on ?x ?y))
    )
) 

(:action macro-3-3-1977642291
    :parameters (?x - block)
    :precondition 
    (and
        (clear ?x)
        (handempty)
    )
    :effect 
    (and
        (clear ?x)
        (not (holding ?x))
        (holding ?x)
    )
) 

(:action macro-3-3--414993528
    :parameters (?x - block ?y - block)
    :precondition 
    (and
        (handempty)
        (not (ontable ?x))
        (clear ?x)
    )
    :effect 
    (and
        (not (on ?x ?y))
        (not (on ?x ?y))
        (not (clear ?x))
    )
) 

(:action macro-pick-up-and-stack-6
    :parameters (?x_0 - block ?y - block)
    :precondition 
    (and
        (clear ?x_0)
        (ontable ?x_0)
        (handempty)
        (not (= ?x_0 ?y))
        (clear ?y)
    )
    :effect 
    (and
        (not (ontable ?x_0))
        (not (holding ?x_0))
        (not (clear ?y))
        (clear ?x_0)
        (handempty)
        (on ?x_0 ?y)
    )
) 




) 
