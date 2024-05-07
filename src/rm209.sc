;;; Sierra Script 1.0 - (do not remove this comment)
(script# 209)
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
	rm209 0 
	
)


(instance rm209 of Rm
	(properties
		picture scriptNumber
		north 0
		east 211
		south 0
		west 252
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
		
		(vine init: setPri: 8)
		(vine2 init: setPri: 1)
		(pod init: setPri: 6 xStep: 2 yStep: 1 setMotion: MoveTo 330 119 ignoreControl: ctlWHITE ignoreActors:)
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
		y 59
		x 76
		view 100
		loop 0
	)
)
(instance vine2 of Prop
	(properties
		y 37
		x 244
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
