;;; Sierra Script 1.0 - (do not remove this comment)
(script# 401)
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
	rm401 0	
)
(local

	entering = 0
	
)

(instance rm401 of Rm
	(properties
		picture scriptNumber
		north 202
		east 0
		south 403
		west 402
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript)
		(switch gPreviousRoomNumber
			(210
				(= entering 1)
				(PlaceEgo 162 77 2)
				(self changeState: 1)	
			)
			(402
				(PlaceEgo 20 140 0)	
			)
			(else 
				(gEgo posn: 150 100 loop: 1)
			)
		)
		(SetUpEgo)
		(gEgo init:)
		(bigRock init: ignoreActors:)
	)
)

(instance RoomScript of Script
	(properties)
	
	(method (doit)
		(super doit:)
		; code executed each game cycle
		(if (& (gEgo onControl:) ctlMAROON)
			(if (not entering)
				(self changeState: 3)
				(= entering 1)
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
			(1
				(ProgramControl)
				(gEgo setMotion: MoveTo 124 113 self)	
			)
			(2
				(gEgo loop: 2)
				(PlayerControl)	
			)
			(3
				(ProgramControl)
				(gEgo setMotion: MoveTo 172 68 self)	
			)
			(4
				(gRoom newRoom: 202)	
			)
		)
	)
)
(instance bigRock of Prop
	(properties
		y 104
		x 50
		view 805
		loop 2
		cel 2
	)
)