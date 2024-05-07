;;; Sierra Script 1.0 - (do not remove this comment)
(script# 212)
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
	
	rm212 0 
	
)


(instance rm212 of Rm
	(properties
		picture scriptNumber
		north 0
		east 213
		south 0
		west 211
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
		
		(waterFallTop init: setPri: 1 ignoreActors: setCycle: Fwd cycleSpeed: 3)
		(waterFallBottom init: setPri: 1 ignoreActors: setCycle: Fwd cycleSpeed: 3)
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

(instance waterFallTop of Prop
	(properties
		y 66
		x 140
		view 14
	)
)
(instance waterFallBottom of Prop
	(properties
		y 136
		x 145
		view 14
	)
)