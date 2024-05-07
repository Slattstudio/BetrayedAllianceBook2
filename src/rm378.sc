;;; Sierra Script 1.0 - (do not remove this comment)
(script# 378)
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
	rm378 0
)


(instance rm378 of Rm
	(properties
		picture scriptNumber
		north 277
		east 277
		south 379
		west 379
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript)
		(switch gPreviousRoomNumber
			(277
				(PlaceEgo 272 51 1)		
			)
			(379
				(PlaceEgo 30 158 0)	
			)
			(else 
				(PlaceEgo 272 51 1)	
				;(gEgo posn: 150 100 loop: 1)
			)
		)
		(SetUpEgo)
		(gEgo init:)
		(RunningCheck)
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
