;;; Sierra Script 1.0 - (do not remove this comment)
(script# 219)
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
	rm219 0
)

(local
	birdPerched = 0	
)


(instance rm219 of Rm
	(properties
		picture scriptNumber
		north 0
		east 472
		south 0
		west 221
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
		(bird 
			init:
			ignoreControl: ctlWHITE
			ignoreActors: 
			setCycle: Walk 
			xStep: 4
			yStep: 3
			cycleSpeed: 2
			setScript: birdScript)
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
(instance birdScript of Script
	(properties)
	(method (changeState newState)
		(= state newState)
		(switch state
			(0
				(bird 
					setMotion: MoveTo 130 60 birdScript
				)
			)
			(1
				(bird
					view: 359 ; descending
					setMotion: MoveTo 98 103 birdScript
				)
			)
			(2
				(bird
					view: 360 ; perched
					loop: 0
				)
				(= birdPerched 1)
			)
		)
	)
)

(instance bird of Act
	(properties
		y 60
		x 300
		view 358
	)
)
