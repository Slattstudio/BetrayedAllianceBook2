;;; Sierra Script 1.0 - (do not remove this comment)
(script# 231)
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
	
	rm231 0
	
)


(instance rm231 of Rm
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
		(hollow init: setCycle: Walk xStep: 3 cycleSpeed: 2 ignoreControl: ctlWHITE hide:)
		(hollow2 init: setCycle: Walk xStep: 3 cycleSpeed: 2 ignoreControl: ctlWHITE)
		(hollow3 init: setCycle: Walk xStep: 3 cycleSpeed: 2 ignoreControl: ctlWHITE hide:)
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
		(if(Said 'hi')
			(hollow setMotion: MoveTo (gEgo x?) 100)
			(hollow2 setMotion: MoveTo (gEgo x?) 130)
			(hollow3 setMotion: MoveTo (gEgo x?) 160)
		)
	)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0 ; Handle state changes
			)
		)
	)
)
(instance hollow of Act
	(properties
		y 100
		x 265
		view 21
	)
)
(instance hollow2 of Act
	(properties
		y 130
		x 285
		view 32
	)
)
(instance hollow3 of Act
	(properties
		y 160
		x 305
		view 25
	)
)