;;; Sierra Script 1.0 - (do not remove this comment)
(script# 405)
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
	
	rm405 0 
	
)


(instance rm405 of Rm
	(properties
		picture scriptNumber
		north 0
		east 0
		south 0
		west 404
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript)
		(switch gPreviousRoomNumber
			(else 
				(gEgo posn: 150 100 loop: 1)
			)
		)
		(SetUpEgo)
		(gEgo init:)
		(vine init:)
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
(instance vine of Prop
	(properties
		y 41
		x 163
		view 100
		loop 0
	)
)
(instance pod of Act
	(properties
		y 97
		x 143
		view 101
	)
)