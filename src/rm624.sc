;;; Sierra Script 1.0 - (do not remove this comment)
(script# 624)
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
	rm624 0
)
(local
	allShow = 1
	
	noExitNorth = 0
	noExitSouth = 0
	noExitEast = 0
	noExitWest = 0
	
)

(instance rm624 of Rm
	(properties
		picture 606
		north 614
		east 625
		south 634
		west 623
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript)
		(switch gPreviousRoomNumber
			((gRoom north:)	
				(PlaceEgo 215 80 2)	
			)
			((gRoom south:)	
				(PlaceEgo 200 175 3)	
			)
			((gRoom west:)	
				(PlaceEgo 10 125 0)	
			)
			((gRoom east:)	
				(PlaceEgo 300 120 1)	
			)
			(else 
				(PlaceEgo 215 80 2)		
			)
		)
		(SetUpEgo)
		(gEgo init:)
		(RunningCheck)
		
		;(bottomBlock init:)
		;(blockTop init: setPri: 1 ignoreActors:)
		;(blockTopTree init: setPri: 4)
		;(blockTopRock init: setPri: 1)
		;(blockLeftBush init:)
		;(blockLeftBush2 init:)
		;(blockLeftRock init:)
		;(blockLeftRock2 init:)
		;(blockRightBush init:)
		;(blockRightBush2 init:)
		;(blockRightRock init:)
		;(blockRightRock2 init:)
		
		(if noExitNorth
			(gEgo observeControl: ctlGREEN)	
		)
		;(if noExitSouth
		;	(gEgo observeControl: ctlNAVY)	
		;)
		(if noExitEast
			(gEgo observeControl: ctlMAROON)	
		)
		(if noExitWest
			(gEgo observeControl: ctlTEAL)	
		)
	)
)

(instance RoomScript of Script
	(properties)
	
	(method (doit)
		(super doit:)
		; code executed each game cycle
		(if (& (gEgo onControl:) ctlSILVER)
			(gRoom newRoom: (gRoom north:))	
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
(instance backgroundTree of Prop
	(properties
		y 86
		x 20
		view 698
	)
)
(instance backgroundTree2 of Prop
	(properties
		y 76
		x 230
		view 698
		cel 1
	)
)
(instance bottomBlock of Prop
	(properties
		y 213
		x 120
		view 803
		loop 3
	)
)
(instance blockTop of Prop
	(properties
		y 63
		x 110
		view 805
		loop 6
	)
)
(instance blockTopRock of Prop
	(properties
		y 59
		x 157
		view 805
		loop 2
		cel 3
	)
)
(instance blockTopTree of Prop
	(properties
		y 94
		x 110
		view 698
		cel 0
	)
)
(instance blockLeftBush of Prop
	(properties
		y 140
		x -5
		view 805
		loop 3
	)
)
(instance blockLeftBush2 of Prop
	(properties
		y 100
		x -15
		view 805
		loop 5
		cel 2
	)
)
(instance blockLeftRock of Prop
	(properties
		y 156
		x 14
		view 805
		loop 2
	)
)
(instance blockLeftRock2 of Prop
	(properties
		y 111
		x 10
		view 805
		loop 2
		cel 4
	)
)
(instance blockRightBush of Prop
	(properties
		y 125
		x 320
		view 805
		loop 5
		cel 3
	)
)
(instance blockRightBush2 of Prop
	(properties
		y 145
		x 325
		view 805
		loop 4
		cel 0
	)
)
(instance blockRightRock of Prop
	(properties
		y 160
		x 310
		view 805
		loop 2
		cel 5
	)
)
(instance blockRightRock2 of Prop
	(properties
		y 113
		x 310
		view 805
		loop 2
		cel 2
	)
)