;;; Sierra Script 1.0 - (do not remove this comment)
(script# 56)
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
	
	rm056 0

)


(instance rm056 of Rm
	(properties
		picture scriptNumber
		north 0
		east 0
		south 0
		west 255
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript)
		(switch gPreviousRoomNumber
			(255 				
				(gEgo posn: 20 50 loop: 0)
			)
			(else 				
				(gEgo posn: 20 50 loop: 0)
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
