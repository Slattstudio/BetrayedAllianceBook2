;;; Sierra Script 1.0 - (do not remove this comment)
(script# 277)
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
	rm277 0
)


(instance rm277 of Rm
	(properties
		picture scriptNumber
		north 0
		east 276
		south 0
		west 278
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript)
		(switch gPreviousRoomNumber
			(276
				(PlaceEgo 300 147 1)	
			)
			(278
				(PlaceEgo 20 147 0)	
			)
			(else 
				(PlaceEgo 300 147 1)
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
