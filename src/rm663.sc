;;; Sierra Script 1.0 - (do not remove this comment)
(script# 663)
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
	rm663 0
)

(instance rm663 of Rm
	(properties
		picture 604
		north 653
		east 664
		south 673
		west 662
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript)
		(switch gPreviousRoomNumber
			((gRoom north:)	
				(PlaceEgo 123 100 2)	
			)
			((gRoom south:)	
				(PlaceEgo 209 175 3)	
			)
			((gRoom west:)	
				(PlaceEgo 10 115 0)	
			)
			((gRoom east:)	
				(PlaceEgo 300 125 1)	
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
