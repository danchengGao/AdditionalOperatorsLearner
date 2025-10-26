(define 
(domain barman) 
(:requirements 
    :equality 
) 
(:types 
    object 
    beverage - object 
    cocktail - beverage 
    container - object 
    dispenser - object 
    hand - object 
    ingredient - beverage 
    level - object 
    shaker - container 
    shot - container 
) 
(:predicates 
    (clean ?c - container)
    (cocktail-part1 ?c - cocktail ?i - ingredient)
    (cocktail-part2 ?c - cocktail ?i - ingredient)
    (contains ?c - container ?b - beverage)
    (dispenses ?d - dispenser ?i - ingredient)
    (empty ?c - container)
    (handempty ?h - hand)
    (holding ?h - hand ?c - container)
    (next ?l1 - level ?l2 - level)
    (ontable ?c - container)
    (shaked ?s - shaker)
    (shaker-empty-level ?s - shaker ?l - level)
    (shaker-level ?s - shaker ?l - level)
    (unshaked ?s - shaker)
    (used ?c - container ?b - beverage)
) 
(:action grasp-shot
    :parameters (?h - hand ?c - shot)
    :precondition 
    (and
        (ontable ?c)
        (handempty ?h)
    )
    :effect 
    (and
        (not (ontable ?c))
        (not (handempty ?h))
        (holding ?h ?c)
    )
) 

(:action grasp-shaker
    :parameters (?h - hand ?c - shaker)
    :precondition 
    (and
        (ontable ?c)
        (handempty ?h)
    )
    :effect 
    (and
        (not (ontable ?c))
        (not (handempty ?h))
        (holding ?h ?c)
    )
) 

(:action leave-shot
    :parameters (?h - hand ?c - shot)
    :precondition 
    (holding ?h ?c)
    :effect 
    (and
        (not (holding ?h ?c))
        (handempty ?h)
        (ontable ?c)
    )
) 

(:action leave-container
    :parameters (?h - hand ?c - shaker)
    :precondition 
    (holding ?h ?c)
    :effect 
    (and
        (not (holding ?h ?c))
        (handempty ?h)
        (ontable ?c)
    )
) 

(:action fill-shot
    :parameters (?s - shot ?i - ingredient ?h1 - hand ?h2 - hand ?d - dispenser)
    :precondition 
    (and
        (holding ?h1 ?s)
        (handempty ?h2)
        (dispenses ?d ?i)
        (empty ?s)
        (clean ?s)
    )
    :effect 
    (and
        (not (empty ?s))
        (contains ?s ?i)
        (not (clean ?s))
        (used ?s ?i)
    )
) 

(:action refill-shot
    :parameters (?s - shot ?i - ingredient ?h1 - hand ?h2 - hand ?d - dispenser)
    :precondition 
    (and
        (holding ?h1 ?s)
        (handempty ?h2)
        (dispenses ?d ?i)
        (empty ?s)
        (used ?s ?i)
    )
    :effect 
    (and
        (not (empty ?s))
        (contains ?s ?i)
    )
) 

(:action empty-shot-ingredient
    :parameters (?h - hand ?p - shot ?b - ingredient)
    :precondition 
    (and
        (holding ?h ?p)
        (contains ?p ?b)
    )
    :effect 
    (and
        (not (contains ?p ?b))
        (empty ?p)
    )
) 

(:action empty-shot-cocktail
    :parameters (?h - hand ?p - shot ?b - cocktail)
    :precondition 
    (and
        (holding ?h ?p)
        (contains ?p ?b)
    )
    :effect 
    (and
        (not (contains ?p ?b))
        (empty ?p)
    )
) 

(:action clean-shot-ingredient
    :parameters (?s - shot ?b - ingredient ?h1 - hand ?h2 - hand)
    :precondition 
    (and
        (holding ?h1 ?s)
        (handempty ?h2)
        (empty ?s)
        (used ?s ?b)
    )
    :effect 
    (and
        (not (used ?s ?b))
        (clean ?s)
    )
) 

(:action clean-shot-cocktail
    :parameters (?s - shot ?b - cocktail ?h1 - hand ?h2 - hand)
    :precondition 
    (and
        (holding ?h1 ?s)
        (handempty ?h2)
        (empty ?s)
        (used ?s ?b)
    )
    :effect 
    (and
        (not (used ?s ?b))
        (clean ?s)
    )
) 

(:action pour-shot-to-clean-shaker
    :parameters (?s - shot ?i - ingredient ?d - shaker ?h1 - hand ?l - level ?l1 - level)
    :precondition 
    (and
        (holding ?h1 ?s)
        (contains ?s ?i)
        (empty ?d)
        (clean ?d)
        (shaker-level ?d ?l)
        (next ?l ?l1)
    )
    :effect 
    (and
        (not (contains ?s ?i))
        (empty ?s)
        (contains ?d ?i)
        (not (empty ?d))
        (not (clean ?d))
        (unshaked ?d)
        (not (shaker-level ?d ?l))
        (shaker-level ?d ?l1)
    )
) 

(:action pour-shot-to-used-shaker
    :parameters (?s - shot ?i - ingredient ?d - shaker ?h1 - hand ?l - level ?l1 - level)
    :precondition 
    (and
        (holding ?h1 ?s)
        (contains ?s ?i)
        (unshaked ?d)
        (shaker-level ?d ?l)
        (next ?l ?l1)
    )
    :effect 
    (and
        (not (contains ?s ?i))
        (contains ?d ?i)
        (empty ?s)
        (not (shaker-level ?d ?l))
        (shaker-level ?d ?l1)
    )
) 

(:action empty-shaker
    :parameters (?h - hand ?s - shaker ?b - cocktail ?l - level ?l1 - level)
    :precondition 
    (and
        (holding ?h ?s)
        (contains ?s ?b)
        (shaked ?s)
        (shaker-level ?s ?l)
        (shaker-empty-level ?s ?l1)
    )
    :effect 
    (and
        (not (shaked ?s))
        (not (shaker-level ?s ?l))
        (shaker-level ?s ?l1)
        (not (contains ?s ?b))
        (empty ?s)
    )
) 

(:action clean-shaker
    :parameters (?h1 - hand ?h2 - hand ?s - shaker)
    :precondition 
    (and
        (holding ?h1 ?s)
        (handempty ?h2)
        (empty ?s)
    )
    :effect 
    (and
        (clean ?s)
    )
) 

(:action shake
    :parameters (?b - cocktail ?d1 - ingredient ?d2 - ingredient ?s - shaker ?h1 - hand ?h2 - hand)
    :precondition 
    (and
        (holding ?h1 ?s)
        (handempty ?h2)
        (contains ?s ?d1)
        (contains ?s ?d2)
        (cocktail-part1 ?b ?d1)
        (cocktail-part2 ?b ?d2)
        (unshaked ?s)
    )
    :effect 
    (and
        (not (unshaked ?s))
        (not (contains ?s ?d1))
        (not (contains ?s ?d2))
        (shaked ?s)
        (contains ?s ?b)
    )
) 

(:action pour-shaker-to-shot-ingredient
    :parameters (?b - ingredient ?d - shot ?h - hand ?s - shaker ?l - level ?l1 - level)
    :precondition 
    (and
        (holding ?h ?s)
        (shaked ?s)
        (empty ?d)
        (clean ?d)
        (contains ?s ?b)
        (shaker-level ?s ?l)
        (next ?l1 ?l)
    )
    :effect 
    (and
        (not (clean ?d))
        (not (empty ?d))
        (contains ?d ?b)
        (shaker-level ?s ?l1)
        (not (shaker-level ?s ?l))
    )
) 

(:action pour-shaker-to-shot-cocktail
    :parameters (?b - cocktail ?d - shot ?h - hand ?s - shaker ?l - level ?l1 - level)
    :precondition 
    (and
        (holding ?h ?s)
        (shaked ?s)
        (empty ?d)
        (clean ?d)
        (contains ?s ?b)
        (shaker-level ?s ?l)
        (next ?l1 ?l)
    )
    :effect 
    (and
        (not (clean ?d))
        (not (empty ?d))
        (contains ?d ?b)
        (shaker-level ?s ?l1)
        (not (shaker-level ?s ?l))
    )
) 

(:action macro-3-3-1504934090
    :parameters (?s - shaker ?l - level)
    :precondition 
    (and
        (shaker-level ?s ?l)
        (not (shaked ?s))
        (shaker-empty-level ?s ?l)
    )
    :effect 
    (and
        (not (shaker-empty-level ?s ?l))
        (shaked ?s)
    )
) 

(:action macro-2-2-1122333793
    :parameters (?s - shaker ?h - hand ?c - container ?l - level)
    :precondition 
    (and
        (unshaked ?s)
        (holding ?h ?c)
    )
    :effect 
    (and
        (shaker-empty-level ?s ?l)
        (shaker-level ?s ?l)
    )
) 

(:action nonr-leave-container-and-grasp-shot-0
    :parameters (?h_0 - hand ?c_0 - shaker ?c - shot)
    :precondition 
    (and
        (holding ?h_0 ?c_0)
        (ontable ?c)
    )
    :effect 
    (and
        (not (holding ?h_0 ?c_0))
        (ontable ?c_0)
        (not (ontable ?c))
        (not (handempty ?h_0))
        (holding ?h_0 ?c)
    )
) 

(:action nonr-fill-shot-and-clean-shot-ingredient-170
    :parameters (?s_0 - shot ?i_0 - ingredient ?h1_0 - hand ?d_0 - dispenser ?b - ingredient ?h1 - hand)
    :precondition 
    (and
        (holding ?h1_0 ?s_0)
        (handempty ?h1_0)
        (dispenses ?d_0 ?i_0)
        (empty ?s_0)
        (clean ?s_0)
        (not (= ?i_0 ?b))
        (not (= ?h1_0 ?h1))
        (not (= ?h1 ?h1_0))
        (holding ?h1 ?s_0)
        (used ?s_0 ?b)
    )
    :effect 
    (and
        (not (empty ?s_0))
        (contains ?s_0 ?i_0)
        (used ?s_0 ?i_0)
        (not (used ?s_0 ?b))
        (clean ?s_0)
    )
) 

(:action nonr-pour-shaker-to-shot-ingredient-and-pour-shaker-to-shot-ingredient-1018
    :parameters (?b_0 - ingredient ?d_0 - shot ?h_0 - hand ?s_0 - shaker ?l_0 - level ?l1_0 - level ?d - shot ?s - shaker ?l - level ?l1 - level)
    :precondition 
    (and
        (holding ?h_0 ?s_0)
        (shaked ?s_0)
        (empty ?d_0)
        (clean ?d_0)
        (contains ?s_0 ?b_0)
        (shaker-level ?s_0 ?l_0)
        (next ?l1_0 ?l_0)
        (not (= ?d_0 ?d))
        (not (= ?s_0 ?s))
        (not (= ?l_0 ?l1_0))
        (not (= ?l_0 ?l))
        (not (= ?l_0 ?l1))
        (not (= ?l1_0 ?l))
        (not (= ?l1_0 ?l1))
        (not (= ?l ?l1))
        (holding ?h_0 ?s)
        (shaked ?s)
        (empty ?d)
        (clean ?d)
        (contains ?s ?b_0)
        (shaker-level ?s ?l)
        (next ?l1 ?l)
    )
    :effect 
    (and
        (not (clean ?d_0))
        (not (empty ?d_0))
        (contains ?d_0 ?b_0)
        (shaker-level ?s_0 ?l1_0)
        (not (shaker-level ?s_0 ?l_0))
        (not (clean ?d))
        (not (empty ?d))
        (contains ?d ?b_0)
        (shaker-level ?s ?l1)
        (not (shaker-level ?s ?l))
    )
) 

(:action nonr-fill-shot-and-clean-shot-ingredient-252
    :parameters (?s_0 - shot ?i_0 - ingredient ?h1_0 - hand ?h2_0 - hand ?d_0 - dispenser ?h1 - hand ?h2 - hand)
    :precondition 
    (and
        (holding ?h1_0 ?s_0)
        (handempty ?h2_0)
        (dispenses ?d_0 ?i_0)
        (empty ?s_0)
        (clean ?s_0)
        (not (= ?h1_0 ?h2_0))
        (not (= ?h1_0 ?h1))
        (not (= ?h1_0 ?h2))
        (not (= ?h2_0 ?h1))
        (not (= ?h2_0 ?h2))
        (not (= ?h1 ?h2))
        (holding ?h1 ?s_0)
        (handempty ?h2)
        (used ?s_0 ?i_0)
    )
    :effect 
    (and
        (not (empty ?s_0))
        (contains ?s_0 ?i_0)
        (used ?s_0 ?i_0)
        (not (used ?s_0 ?i_0))
        (clean ?s_0)
    )
) 

) 
