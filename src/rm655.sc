;;; Sierra Script 1.0 - (do not remove this comment)
(script# 655)
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
	rm655 0
)

(local
;	allShow = 1

	noExitNorth = 0
	noExitSouth = 0
	noExitEast = 0
	noExitWest = 0
		
)

(instance rm655 of Rm
	(properties
		picture 601
		north 645
		east 656
		south 665
		west 654
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript)
		(switch gPreviousRoomNumber
			((gRoom north:)	
				(PlaceEgo 135 100 2)	
			)
			((gRoom south:)	
				(PlaceEgo 155 175 3)	
			)
			((gRoom west:)	
				(PlaceEgo 20 135 0)	
			)
			((gRoom east:)	
				(PlaceEgo 310 130 1)	
			)
			(else 
				(PlaceEgo 215 80 2)		
			)
		)
		(SetUpEgo)
		(gEgo init:)
		(RunningCheck)
		
		(backgroundTree init: setPri: 3)
		(backgroundTree2 init: setPri: 3)
				
		(if noExitNorth
			(gEgo observeControl: ctlGREEN)	
			(blockTop init: setPri: 4)
			(blockTopTree init: setPri: 4)
			(blockTopRock init: setPri: 2)
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
		(if (& (gEgo onControl:) ctlSILVER)
			(gRoom newRoom: (gRoom north:))	
		)
	)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if (& (pEvent modifiers?) emRIGHT_BUTTON)
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