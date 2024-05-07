;;; Sierra Script 1.0 - (do not remove this comment)
(script# 641)
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
	rm641 0
)


(instance rm641 of Rm
	(properties
		picture 15
		north 0
		east 642
		south 651
		west 0
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript)
		(switch gPreviousRoomNumber
			((gRoom south:)	
				(PlaceEgo 180 180 3)	
			)
			((gRoom east:)	
				(PlaceEgo 305 130 1)	
			)
			(else 
				(PlaceEgo 215 80 2)		
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
