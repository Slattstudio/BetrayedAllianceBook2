;;; Sierra Script 1.0 - (do not remove this comment)
(script# 402)
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
	rm402 0
)
(local

	mysteryMusic = 0
	
)

(instance rm402 of Rm
	(properties
		picture scriptNumber
		north 0
		east 401
		south 0
		west 0
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript)
		(switch gPreviousRoomNumber
			(401
				(PlaceEgo 290 145 1)	
			)
			(else 
				(PlaceEgo 290 145 1)
				;(gEgo posn: 150 100 loop: 1)
			)
		)
		(SetUpEgo)
		(gEgo init:)
		(foregroundTree init: setPri: 15)
		(secretBush init:)
	)
)

(instance RoomScript of Script
	(properties)
	
	(method (doit)
		(super doit:)
		; code executed each game cycle
		(if (& (gEgo onControl:) ctlMAROON)
			(secretBush cel: 1)
			(if (not mysteryMusic)
				(gTheSoundFX number: 4 play:)
				(= mysteryMusic 1)
				(gEgo setMotion: NULL)
			)
		else
			(secretBush cel: 0)
			(= mysteryMusic 0)
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
(instance foregroundTree of Prop
	(properties
		y 196
		x 85
		view 999
		loop 0
		cel 1
	)
)
(instance secretBush of Prop
	(properties
		y 160
		x 40
		view 805
		loop 3
		cel 0
	)
)