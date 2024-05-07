;;; Sierra Script 1.0 - (do not remove this comment)
(script# 6)
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
	rm006 0
)


(instance rm006 of Rm
	(properties
		picture scriptNumber
		north 5
		east 470
		south 0
		west 0
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript)
		(switch gPreviousRoomNumber
			
			(5
				(PlaceEgo 250 40 2)
					
			)
			(470
				(PlaceEgo 300 125 1)	
			)
			(else 
				(PlaceEgo 300 125 1)
			)
		)
		(SetUpEgo)
		(gEgo init:)
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