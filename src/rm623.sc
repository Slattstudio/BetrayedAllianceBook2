;;; Sierra Script 1.0 - (do not remove this comment)
(script# 623)
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
	rm623 0
)
(local
	allShow = 1

	noExitNorth = 1
	noExitSouth = 1
	noExitEast = 0
	noExitWest = 0
		
)

(instance rm623 of Rm
	(properties
		picture 601
		north 0
		east 624
		south 0
		west 622
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript)
		(switch gPreviousRoomNumber
			(622
				(PlaceEgo 20 135 0)
			)
			(624
				(PlaceEgo 310 130 1)
			)
			(else 
				(PlaceEgo 150 100 1)
				;(gEgo posn: 150 100 loop: 1)
			)
		)
		(SetUpEgo)
		(gEgo init:)
		(RunningCheck)
		
		(backgroundTree init: setPri: 3)
		(backgroundTree2 init: setPri: 3)
		(bottomBlock init:)
		(blockTop init: setPri: 4)
		(blockTopTree init: setPri: 4)
		(blockTopRock init: setPri: 2)
		;(blockLeftBush init:)
		;(blockLeftRock init:)
		;(blockLeftRock2 init:)
		;(blockRightBush init:)
		;(blockRightRock init:)
		;(blockRightRock2 init:)
		
		(if noExitNorth
			(gEgo observeControl: ctlGREEN)	
		)
		(if noExitSouth
			(gEgo observeControl: ctlNAVY)	
		)
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
		y 86
		x 260
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
		x 145
		view 805
		loop 6
	)
)
(instance blockTopRock of Prop
	(properties
		y 83
		x 177
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
		x 298
		view 805
		loop 5
		cel 1
	)
)
(instance blockRightRock of Prop
	(properties
		y 153
		x 304
		view 805
		loop 2
		cel 3
	)
)
(instance blockRightRock2 of Prop
	(properties
		y 123
		x 269
		view 805
		loop 2
		cel 3
	)
)