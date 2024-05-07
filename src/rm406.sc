;;; Sierra Script 1.0 - (do not remove this comment)
(script# 406)
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
	rm406 0
)


(instance rm406 of Rm
	(properties
		picture scriptNumber
		north 0
		east 0
		south 0
		west 668
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript setRegions: 205)
		(switch gPreviousRoomNumber
			(else 
				(gEgo posn: 150 100 loop: 1)
			)
		)
		(SetUpEgo)
		(gEgo init:)
		
		(if (== gSwitchedRoomNumber 241) ; if Leah is in background
			(smallLeah init:)	
		)
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
(instance smallLeah of Prop
	(properties
		y 41
		x 133
		view 105
	)
)