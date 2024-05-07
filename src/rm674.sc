;;; Sierra Script 1.0 - (do not remove this comment)
(script# 674)
(include sci.sh)
(include game.sh)
(use Controls)
(use Cycle)
(use Door)
(use Feature)
(use Game)
(use Inv)
(use Main)
(use Obj)

(public
	rm674 0
)


(instance rm674 of Rm
	(properties
		picture scriptNumber
		north 0
		east 0
		south 684
		west 0
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript)
		(switch gPreviousRoomNumber
			(else 
				(PlaceEgo 158 175 3)
			)
		)
		(SetUpEgo)
		(gEgo init:)
		
		(blockLeftRock init: ignoreActors:)
	)
)

(instance RoomScript of Script
	(properties)
	
	(method (doit)
		(super doit:)
		; code executed each game cycle
	)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		; handle Said's, etc...
	)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0 ; Handle state changes
			)
		)
	)
)

(instance blockLeftRock of Prop
	(properties
		y 140
		x -4
		view 805
		loop 2
		cel 4
	)
)