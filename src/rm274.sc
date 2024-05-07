;;; Sierra Script 1.0 - (do not remove this comment)
(script# 274)
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
	rm274 0
)


(instance rm274 of Rm
	(properties
		picture scriptNumber
		north 272
		east 0
		south 273
		west 0
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript)
		(switch gPreviousRoomNumber
			(9 
				(gEgo posn: 160 160 loop: 3)
			)
			(else 
				(gEgo posn: 150 100 loop: 1)
			)
		)
		(SetUpEgo)
		(gEgo init:)
		(RunningCheck)
		
		(statue1 init:)
		(statue2 init:)
		(statue3 init:)
		(statue4 init:)
		(statue5 init:)
		(statue6 init:)
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
(instance statue1 of Prop
	(properties
		y 150
		x 75
		view 93
		loop 0
	)
)
(instance statue2 of Prop
	(properties
		y 130
		x 100
		view 93
		loop 4
	)
)
(instance statue3 of Prop
	(properties
		y 110
		x 105
		view 93
		loop 3
	)
)
(instance statue4 of Prop
	(properties
		y 110
		x 205
		view 93
		loop 1
	)
)
(instance statue5 of Prop
	(properties
		y 130
		x 218
		view 93
		loop 2
	)
)
(instance statue6 of Prop
	(properties
		y 150
		x 236
		view 93
		loop 5
	)
)