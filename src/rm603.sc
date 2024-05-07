;;; Sierra Script 1.0 - (do not remove this comment)
(script# 603)
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
	rm603 0
)
(local
	allShow = 1
	noExitNorth = 1
	noExitSouth = 1
	noExitEast = 1
	noExitWest = 1
	
)

(instance rm603 of Rm
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
				(PlaceEgo 190 100 2)	
			)
			((gRoom south:)	
				(PlaceEgo 115 175 3)	
			)
			((gRoom west:)	
				(PlaceEgo 20  125 0)	
			)
			((gRoom east:)	
				(PlaceEgo 310 115 1)	
			)
			(else 
				(PlaceEgo 215 80 2)		
			)
		)
		(SetUpEgo)
		(gEgo init:)
		(RunningCheck)
		
		(foregroundTree init: setPri: 14)
		
		;(backgroundTree init: setPri: 3)
		(backgroundTree2 init: setPri: 2)
		
		
		
		
		
		(if noExitNorth
			(gEgo observeControl: ctlGREEN)
			(blockTop init: setPri: 4 ignoreActors:)
			;(blockTopTree init: setPri: 4)
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
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if (& (pEvent modifiers?) emRIGHT_BUTTON)
				(if allShow
					(bottomBlock hide:)
					(blockTop hide:)
					;(blockTopTree hide:)
					(blockTopRock hide:)
					(blockLeftBush hide:)
					(blockLeftRock hide:)
					(blockLeftRock2 hide:)
					(blockRightBush hide:)
					(blockRightRock hide:)
					(blockRightRock2 hide:)	
					(= allShow 0)
				else
					(bottomBlock show:)
					(blockTop show:)
					;(blockTopTree show:)
					(blockTopRock show:)
					(blockLeftBush show:)
					(blockLeftRock show:)
					(blockLeftRock2 show:)
					(blockRightBush show:)
					(blockRightRock show:)
					(blockRightRock2 show:)	
					(= allShow 1)
				)	
			)	
		)
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
		x 0
		view 999
		loop 0
		cel 1
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
		y 88
		x 200
		view 805
		loop 6
	)
)
(instance blockTopRock of Prop
	(properties
		y 79
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
		y 133
		x -5
		view 805
		loop 5
	)
)
(instance blockLeftRock of Prop
	(properties
		y 150
		x 14
		view 805
		loop 2
	)
)
(instance blockLeftRock2 of Prop
	(properties
		y 110
		x 10
		view 805
		loop 2
	)
)
(instance blockRightBush of Prop
	(properties
		y 134
		x 303
		view 805
		loop 5
		cel 1
	)
)
(instance blockRightRock of Prop
	(properties
		y 149
		x 310
		view 805
		loop 2
		cel 3
	)
)
(instance blockRightRock2 of Prop
	(properties
		y 107
		x 310
		view 805
		loop 2
		cel 3
	)
)