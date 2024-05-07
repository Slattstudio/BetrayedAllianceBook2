;;; Sierra Script 1.0 - (do not remove this comment)
(script# 675)
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
	rm675 0
)

(instance rm675 of Rm
	(properties
		picture 601
		north 665
		east 676
		south 685
		west 0
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
		
		(gEgo observeControl: ctlTEAL)
		(blockLeftBush init:)
		(blockLeftRock init:)
		(blockLeftRock2 init:)	
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
		y 86
		x 260
		view 698
		cel 1
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