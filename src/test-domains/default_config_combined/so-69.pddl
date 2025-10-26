(define 
(domain sokoban-typed) 
(:requirements 
    :equality 
) 
(:types 
    BOX 
    DIR 
    LOC 
) 
(:predicates 
    (adjacent ?l1 - LOC ?l2 - LOC ?d - DIR)
    (at ?o - BOX ?l - LOC)
    (at-robot ?l - LOC)
    (clear ?l - LOC)
) 
(:action move
    :parameters (?from - LOC ?to - LOC ?dir - DIR)
    :precondition 
    (and
        (clear ?to)
        (at-robot ?from)
        (adjacent ?from ?to ?dir)
    )
    :effect 
    (and
        (at-robot ?to)
        (not (at-robot ?from))
    )
) 

(:action push
    :parameters (?rloc - LOC ?bloc - LOC ?floc - LOC ?dir - DIR ?b - BOX)
    :precondition 
    (and
        (at-robot ?rloc)
        (at ?b ?bloc)
        (clear ?floc)
        (adjacent ?rloc ?bloc ?dir)
        (adjacent ?bloc ?floc ?dir)
    )
    :effect 
    (and
        (at-robot ?bloc)
        (at ?b ?floc)
        (clear ?bloc)
        (not (at-robot ?rloc))
        (not (at ?b ?bloc))
        (not (clear ?floc))
    )
) 

(:action macro-push-and-push-129876
    :parameters (?rloc_0 - LOC ?dir_0 - DIR ?b_0 - BOX ?rloc - LOC ?floc - LOC ?dir - DIR ?b - BOX)
    :precondition 
    (and
        (at-robot ?rloc_0)
        (at ?b_0 ?rloc_0)
        (clear ?rloc_0)
        (adjacent ?rloc_0 ?rloc_0 ?dir_0)
        (not (= ?rloc_0 ?rloc))
        (not (= ?rloc_0 ?floc))
        (not (= ?dir_0 ?dir))
        (not (= ?b_0 ?b))
        (not (= ?rloc ?floc))
        (at-robot ?rloc)
        (at ?b ?rloc_0)
        (clear ?floc)
        (adjacent ?rloc ?rloc_0 ?dir)
        (adjacent ?rloc_0 ?floc ?dir)
    )
    :effect 
    (and
        (at-robot ?rloc_0)
        (at ?b_0 ?rloc_0)
        (clear ?rloc_0)
        (not (at ?b_0 ?rloc_0))
        (at-robot ?rloc_0)
        (at ?b ?floc)
        (clear ?rloc_0)
        (not (at-robot ?rloc))
        (not (at ?b ?rloc_0))
        (not (clear ?floc))
    )
) 

(:action nonr-move-and-push-1855
    :parameters (?from_0 - LOC ?to_0 - LOC ?dir_0 - DIR ?rloc - LOC ?bloc - LOC ?b - BOX)
    :precondition 
    (and
        (clear ?to_0)
        (at-robot ?from_0)
        (adjacent ?from_0 ?to_0 ?dir_0)
        (not (= ?from_0 ?to_0))
        (not (= ?from_0 ?rloc))
        (not (= ?from_0 ?bloc))
        (not (= ?to_0 ?rloc))
        (not (= ?to_0 ?bloc))
        (not (= ?rloc ?bloc))
        (not (= ?rloc ?to_0))
        (not (= ?bloc ?to_0))
        (at-robot ?rloc)
        (at ?b ?bloc)
        (adjacent ?rloc ?bloc ?dir_0)
        (adjacent ?bloc ?to_0 ?dir_0)
    )
    :effect 
    (and
        (at-robot ?to_0)
        (not (at-robot ?from_0))
        (at-robot ?bloc)
        (at ?b ?to_0)
        (clear ?bloc)
        (not (at-robot ?rloc))
        (not (at ?b ?bloc))
        (not (clear ?to_0))
    )
) 

) 
