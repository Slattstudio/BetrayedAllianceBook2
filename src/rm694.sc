;;; Sierra Script 1.0 - (do not remove this comment)
(script# 694)
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
	rm694 0
)

(instance rm694 of Rm
	(properties
		picture 605
		north 0
		east 695
		south 699
		west 693
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript)
		(switch gPreviousRoomNumber
			((gRoom north:)	
				(PlaceEgo 115 80 2)	
			)
			((gRoom south:)	
				(PlaceEgo 115 175 3)	
			)
			((gRoom west:)	
				(PlaceEgo 20 125 0)	
			)
			((gRoom east:)	
				(PlaceEgo 310 120 1)	
			)
			(else 
				(PlaceEgo 215 80 2)		
			)
		)
		(SetUpEgo)
		(gEgo init:)
		(RunningCheck)
		
		(backgroundTree2 init: setPri: 0)
		
		(gEgo observeControl: ctlGREEN)
		(blockTop init: setPri: 1 ignoreActors:)
		(blockTopRock init: setPri: 1)	
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
(instance backgroundTree2 of Prop
	(properties
		y 76
		x 230
		view 698
		cel 1
	)
)
(instance blockTop of Prop
	(properties
		y 63
		x 110
		view 805
		loop 6
	)
)
(instance blockTopRock of Prop
	(properties
		y 59
		x 157
		view 805
		loop 2
		cel 3
	)
)
(instance blockTopTree of Prop
	(properties
		y 94
		x 110
		view 698
		cel 0
	)
)