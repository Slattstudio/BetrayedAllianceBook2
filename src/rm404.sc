;;; Sierra Script 1.0 - (do not remove this comment)
(script# 404)
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
	
	rm404 0
	
)


(instance rm404 of Rm
	(properties
		picture scriptNumber
		north 403
		east 405
		south 614
		west 312
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
		(foregroundTree init:)
		(foregroundTree2 init:)
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

(instance foregroundTree of Prop
	(properties
		y 196
		x 0
		view 999
		loop 0
		cel 1
	)
)
(instance foregroundTree2 of Prop
	(properties
		y 236
		x 235
		view 999
		loop 0
		cel 1
	)
)