;;; Sierra Script 1.0 - (do not remove this comment)
(script# 255)
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
	
	rm255 0 
	
)


(instance rm255 of Rm
	(properties
		picture scriptNumber
		north 0
		east 56
		south 254
		west 24
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript)
		(switch gPreviousRoomNumber
			(56 
				(gEgo posn: 300 140 loop: 1)
			)
			(101 
				(gEgo posn: 177 171 loop: 3)
			)
			(else 
				(gEgo posn: 150 100 loop: 1)
			)
		)
		(SetUpEgo)
		(= gAnotherEgo 0)
		(gEgo init:)
		
		(actionEgo init:)
		
		(waterfallLeft init: setCycle: Fwd cycleSpeed: 3 ignoreActors: setPri: 5)
		(waterfallMidLeft init: setCycle: Fwd cycleSpeed: 3 ignoreActors: setPri: 5)
		(waterfallRight init: setCycle: Fwd cycleSpeed: 3 ignoreActors: setPri: 5)
		(waterfallMidRight init: setCycle: Fwd cycleSpeed: 3 ignoreActors: setPri: 5)
		
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
(instance actionEgo of Act
	(properties
		y 170
		x 160
		view 343
	)
)

(instance waterfallLeft of Prop
	(properties
		y 99
		x 67
		view 9
	)
)
(instance waterfallMidLeft of Prop
	(properties
		y 99
		x 127
		view 9
		loop 2
		cel 1
	)
)
(instance waterfallMidRight of Prop
	(properties
		y 99
		x 207
		view 9
		loop 2
		cel 1
	)
)
(instance waterfallRight of Prop
	(properties
		y 99
		x 271
		view 9
	)
)
