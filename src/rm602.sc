;;; Sierra Script 1.0 - (do not remove this comment)
(script# 602)
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
	rm602 0
)
(local

	noExitNorth = 1
	noExitSouth = 1
	noExitEast = 1
	noExitWest = 1
		
)

(instance rm602 of Rm
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
			((gRoom north:)	
				(PlaceEgo 177 100 2)	
			)
			((gRoom south:)	
				(PlaceEgo 170 175 3)	
			)
			((gRoom west:)	
				(PlaceEgo 10 130 0)	
			)
			((gRoom east:)	
				(PlaceEgo 300 135 1)	
			)
			(else 
				(PlaceEgo 215 80 2)		
			)
		)
		(SetUpEgo)
		(gEgo init:)
		(RunningCheck)
		
		(backgroundTree init: setPri: 0)
		(backgroundTree2 init: setPri: 0)
		
		
		
		
		
		(if noExitNorth
			(gEgo observeControl: ctlGREEN)
			(blockTop init: setPri: 4)
			(blockTopTree init: setPri: 4)
			(blockTopRock init: setPri: 3)	
		)
		(if noExitSouth
			(gEgo observeControl: ctlNAVY)
			(bottomBlock init:)	
		)
		(if noExitEast
			(gEgo observeControl: ctlMAROON)
			(blockRightBush init:)
			(blockRightRock init:)
			(blockRightRock2 init:)	
		)
		(if noExitWest
			(gEgo observeControl: ctlTEAL)
			(blockLeftBush init:)
			(blockLeftRock init:)
			(blockLeftRock2 init:)	
		)
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
(instance backgroundTree of Prop
	(properties
		y 90
		x 8
		view 698
	)
)
(instance backgroundTree2 of Prop
	(properties
		y 90
		x 297
		view 698
		cel 1
	)
)
(instance bottomBlock of Prop
	(properties
		y 210
		x 150
		view 803
		loop 3
	)
)
(instance blockTop of Prop
	(properties
		y 97
		x 195
		view 805
		loop 6
	)
)
(instance blockTopRock of Prop
	(properties
		y 88
		x 155
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
		y 133
		x 0
		view 805
		loop 5
	)
)
(instance blockLeftRock of Prop
	(properties
		y 150
		x 16
		view 805
		loop 2
	)
)
(instance blockLeftRock2 of Prop
	(properties
		y 123
		x 23
		view 805
		loop 2
	)
)
(instance blockRightBush of Prop
	(properties
		y 134
		x 335
		view 805
		loop 5
		cel 1
	)
)
(instance blockRightRock of Prop
	(properties
		y 153
		x 314
		view 805
		loop 2
		cel 3
	)
)
(instance blockRightRock2 of Prop
	(properties
		y 123
		x 310
		view 805
		loop 2
		cel 3
	)
)