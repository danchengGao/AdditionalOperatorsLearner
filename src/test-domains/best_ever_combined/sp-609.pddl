(define 
(domain spanner) 
(:requirements 
    :equality 
) 
(:types 
    object - object 
    locatable - object 
    location - object 
    man - locatable 
    nut - locatable 
    spanner - locatable 
) 
(:predicates 
    (at ?m - locatable ?l - location)
    (carrying ?m - man ?s - spanner)
    (link ?l1 - location ?l2 - location)
    (loose ?n - nut)
    (tightened ?n - nut)
    (useable ?s - spanner)
) 
(:action walk
    :parameters (?start - location ?end - location ?m - man)
    :precondition 
    (and
        (at ?m ?start)
        (link ?start ?end)
    )
    :effect 
    (and
        (not (at ?m ?start))
        (at ?m ?end)
    )
) 

(:action pickup_spanner
    :parameters (?l - location ?s - spanner ?m - man)
    :precondition 
    (and
        (at ?m ?l)
        (at ?s ?l)
    )
    :effect 
    (and
        (not (at ?s ?l))
        (carrying ?m ?s)
    )
) 

(:action tighten_nut
    :parameters (?l - location ?s - spanner ?m - man ?n - nut)
    :precondition 
    (and
        (at ?m ?l)
        (at ?n ?l)
        (carrying ?m ?s)
        (useable ?s)
        (loose ?n)
    )
    :effect 
    (and
        (not (loose ?n))
        (not (useable ?s))
        (tightened ?n)
    )
) 

(:action macro-2-2-1566244177
    :parameters (?m - man ?s - spanner ?m - locatable ?l - location)
    :precondition 
    (and
        (carrying ?m ?s)
        (at ?m ?l)
    )
    :effect 
    (and
        (not (useable ?s))
        (not (at ?m ?l))
    )
) 

(:action nonr-walk-and-pickup_spanner-7
    :parameters (?start_0 - location ?end_0 - location ?m_0 - man ?l - location ?s - spanner)
    :precondition 
    (and
        (at ?m_0 ?start_0)
        (link ?start_0 ?end_0)
        (not (= ?start_0 ?end_0))
        (not (= ?start_0 ?l))
        (not (= ?end_0 ?l))
        (at ?m_0 ?l)
        (at ?s ?l)
    )
    :effect 
    (and
        (not (at ?m_0 ?start_0))
        (at ?m_0 ?end_0)
        (not (at ?s ?l))
        (carrying ?m_0 ?s)
    )
) 

(:action nonr-walk-and-walk-7
    :parameters (?start_0 - location ?end_0 - location ?m_0 - man)
    :precondition 
    (and
        (at ?m_0 ?start_0)
        (link ?start_0 ?end_0)
        (not (= ?start_0 ?end_0))
        (link ?end_0 ?end_0)
    )
    :effect 
    (and
        (not (at ?m_0 ?start_0))
        (not (at ?m_0 ?end_0))
        (at ?m_0 ?end_0)
    )
) 

(:action nonr-walk-and-walk-91
    :parameters (?start_0 - location ?end_0 - location ?m_0 - man ?start - location)
    :precondition 
    (and
        (at ?m_0 ?start_0)
        (link ?start_0 ?end_0)
        (not (= ?start_0 ?end_0))
        (not (= ?start_0 ?start))
        (not (= ?end_0 ?start))
        (at ?m_0 ?start)
        (link ?start ?start_0)
    )
    :effect 
    (and
        (at ?m_0 ?end_0)
        (not (at ?m_0 ?start))
        (at ?m_0 ?start_0)
    )
) 

(:action nonr-pickup_spanner-and-pickup_spanner-2
    :parameters (?l_0 - location ?s_0 - spanner ?m_0 - man ?s - spanner)
    :precondition 
    (and
        (at ?m_0 ?l_0)
        (at ?s_0 ?l_0)
        (not (= ?s_0 ?s))
        (at ?s ?l_0)
    )
    :effect 
    (and
        (not (at ?s_0 ?l_0))
        (carrying ?m_0 ?s_0)
        (not (at ?s ?l_0))
        (carrying ?m_0 ?s)
    )
) 

(:action nonr-walk-and-pickup_spanner-3
    :parameters (?start_0 - location ?end_0 - location ?m_0 - man ?s - spanner)
    :precondition 
    (and
        (at ?m_0 ?start_0)
        (link ?start_0 ?end_0)
        (not (= ?start_0 ?end_0))
        (at ?s ?end_0)
    )
    :effect 
    (and
        (not (at ?m_0 ?start_0))
        (at ?m_0 ?end_0)
        (not (at ?s ?end_0))
        (carrying ?m_0 ?s)
    )
) 

) 
