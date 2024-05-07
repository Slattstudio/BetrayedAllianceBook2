;;; Sierra Script 1.0 - (do not remove this comment)
(script# 685)
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
	rm685 0
)


(instance rm685 of Rm
	(properties
		picture 605
		north 675
		east 686
		south 0
		west 684
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript)
		(switch gPreviousRoomNumber
			((gRoom north:)	
				(PlaceEgo 115 80 2)	
			)
			((gRoom south:)	
				(PlaceEgo 115 175 3)	
			)
			((gRoom west:)	
				(PlaceEgo 20 125 0)	
			)
			((gRoom east:)	
				(PlaceEgo 310 120 1)	
			)
			(else 
				(PlaceEgo 215 80 2)		
			)
		)
		(SetUpEgo)
		(gEgo init:)
		(RunningCheck)
		
		(backgroundTree2 init: setPri: 0)
		(bottomBlock init:)
		
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