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
		(blob init: setScript: blobScript cycleSpeed: 2)
		
		(blobScript changeState: 1)
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
(instance blobScript of Script
	(properties)
	
	(method (changeState newState &tmp dyingScript)
		(= state newState)
		(switch state
			(0)
			(1	(= cycles 20) ; blob talk
				(blob loop: 5 cel: 0 setCycle: Fwd)
			)
			(2	; blob blink
				(blob loop: 2 cel: 0 setCycle: End self)
			)
			(3	(= cycles (Random 10 30))
				; wait a moment
			)
			(4
				(self changeState: 1)	
			)
		)
	)
)

(instance blob of Prop
	(properties
		y 100
		x 160
		view 40
		loop 5
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