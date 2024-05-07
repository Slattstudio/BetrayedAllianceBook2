;;; Sierra Script 1.0 - (do not remove this comment)
(script# 228)
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
	rm228 0
)

(local
	dark = 0
)


(instance rm228 of Rm
	(properties
		picture scriptNumber
		north 0
		east 0
		south 0
		west 0
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
	)
)

(instance RoomScript of Script
	(properties)
	
	(method (doit)
		(super doit:)
		; code executed each game cycle
		(if (& (gEgo onControl:) ctlGREY) 
			(if dark
				(= dark 0)
				(= gEgoDark 0)
				(= gEgoStoppedView 1)
				(RunningCheck)	
			)	
		else
			(if (not dark)
				(= dark 1)
				(= gEgoDark 1)
				(= gEgoStoppedView 342)
				(RunningCheck)	
			)
		) 
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
