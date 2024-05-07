;;; Sierra Script 1.0 - (do not remove this comment)
(script# 26)
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
	
	rm026 0
	
)


(instance rm026 of Rm
	(properties
		picture scriptNumber
		north 0
		east 0
		south 0
		west 237
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript)
		(switch gPreviousRoomNumber
			(237 
				(gEgo posn: 20 110 loop: 0)
			)
			(else 
				(gEgo posn: 150 100 loop: 1)
			)
		)
		(SetUpEgo)
		(gEgo init:)
		(RunningCheck)
		
		(log init:)
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

(instance log of Prop
	(properties
		y 80
		x 100
		view 88
		loop 0
	)
)