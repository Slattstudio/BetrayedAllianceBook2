;;; Sierra Script 1.0 - (do not remove this comment)
(script# 695)
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
	rm695 0
)


(instance rm695 of Rm
	(properties
		picture 602
		north 0
		east 696
		south 0
		west 694
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
		
		(gEgo observeControl: ctlGREEN)
		(blockTop init: setPri: 4)
		(blockTopTree init: setPri: 4)
		(blockTopRock init: setPri: 3)
		
		(gEgo observeControl: ctlNAVY)
		(bottomBlock init:)	
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