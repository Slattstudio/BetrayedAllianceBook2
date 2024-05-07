;;; Sierra Script 1.0 - (do not remove this comment)
(script# 13)
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
	rm013 0
)

(local
	bump = 0	
)

(instance rm013 of Rm
	(properties
		picture scriptNumber
		north 0
		east 12
		south 0
		west 217
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
		(gEgo init: hide:)
		(mudBoard init: setScript: mudScript)
	)
)

(instance RoomScript of Script
	(properties)
	
	(method (doit)
		(super doit:)
		; code executed each game cycle
		(++ bump)
		(if (> bump 5)
			(mudBoard loop: 2)
			(= bump 0)
			;(PrintOK)
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

(instance mudScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0 (= cycles 1)
			)
			(1
				(mudBoard setMotion: MoveTo 190 180 self)
			)
			(2
				(gRoom newRoom: 12)	
			)
		)
	)
)
(instance mudBoard of Act
	(properties
		y 50
		x 10
		view 370
		loop 0
		xStep 8
		yStep 4
	)
)