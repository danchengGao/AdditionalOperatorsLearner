(define 
(domain TPP-Propositional) 
(:requirements 
    :strips :typing :equality 
) 
(:types 
    place - object 
    depot - place 
    locatable - object 
    goods - locatable 
    object 
    level - object 
    market - place 
    truck - locatable 
) 
(:predicates 
    (at ?t - truck ?p - place)
    (connected ?p1 - place ?p2 - place)
    (loaded ?g - goods ?t - truck ?l - level)
    (next ?l1 - level ?l2 - level)
    (on-sale ?g - goods ?m - market ?l - level)
    (ready-to-load ?g - goods ?m - market ?l - level)
    (stored ?g - goods ?l - level)
) 
(:action drive-de-de
    :parameters (?t - truck ?from - depot ?to - depot)
    :precondition 
    (and
        (at ?t ?from)
        (connected ?from ?to)
    )
    :effect 
    (and
        (not (at ?t ?from))
        (at ?t ?to)
    )
) 

(:action drive-ma-ma
    :parameters (?t - truck ?from - market ?to - market)
    :precondition 
    (and
        (at ?t ?from)
        (connected ?from ?to)
    )
    :effect 
    (and
        (not (at ?t ?from))
        (at ?t ?to)
    )
) 

(:action drive-de-ma
    :parameters (?t - truck ?from - depot ?to - market)
    :precondition 
    (and
        (at ?t ?from)
        (connected ?from ?to)
    )
    :effect 
    (and
        (not (at ?t ?from))
        (at ?t ?to)
    )
) 

(:action drive-ma-de
    :parameters (?t - truck ?from - market ?to - depot)
    :precondition 
    (and
        (at ?t ?from)
        (connected ?from ?to)
    )
    :effect 
    (and
        (not (at ?t ?from))
        (at ?t ?to)
    )
) 

(:action load
    :parameters (?g - goods ?t - truck ?m - market ?l1 - level ?l2 - level ?l3 - level ?l4 - level)
    :precondition 
    (and
        (at ?t ?m)
        (loaded ?g ?t ?l3)
        (ready-to-load ?g ?m ?l2)
        (next ?l2 ?l1)
        (next ?l4 ?l3)
    )
    :effect 
    (and
        (loaded ?g ?t ?l4)
        (not (loaded ?g ?t ?l3))
        (ready-to-load ?g ?m ?l1)
        (not (ready-to-load ?g ?m ?l2))
    )
) 

(:action unload
    :parameters (?g - goods ?t - truck ?d - depot ?l1 - level ?l2 - level ?l3 - level ?l4 - level)
    :precondition 
    (and
        (at ?t ?d)
        (loaded ?g ?t ?l2)
        (stored ?g ?l3)
        (next ?l2 ?l1)
        (next ?l4 ?l3)
    )
    :effect 
    (and
        (loaded ?g ?t ?l1)
        (not (loaded ?g ?t ?l2))
        (stored ?g ?l4)
        (not (stored ?g ?l3))
    )
) 

(:action buy
    :parameters (?t - truck ?g - goods ?m - market ?l1 - level ?l2 - level ?l3 - level ?l4 - level)
    :precondition 
    (and
        (at ?t ?m)
        (on-sale ?g ?m ?l2)
        (ready-to-load ?g ?m ?l3)
        (next ?l2 ?l1)
        (next ?l4 ?l3)
    )
    :effect 
    (and
        (on-sale ?g ?m ?l1)
        (not (on-sale ?g ?m ?l2))
        (ready-to-load ?g ?m ?l4)
        (not (ready-to-load ?g ?m ?l3))
    )
) 

(:action nonr-drive-ma-ma-and-drive-ma-de-6
    :parameters (?t_0 - truck ?from_0 - market ?to_0 - market ?to - depot)
    :precondition 
    (and
        (at ?t_0 ?from_0)
        (connected ?from_0 ?to_0)
        (not (= ?from_0 ?to_0))
        (connected ?to_0 ?to)
    )
    :effect 
    (and
        (not (at ?t_0 ?from_0))
        (not (at ?t_0 ?to_0))
        (at ?t_0 ?to)
    )
) 

) 
