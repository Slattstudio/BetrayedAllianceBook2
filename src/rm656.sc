;;; Sierra Script 1.0 - (do not remove this comment)
(script# 656)
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
	rm656 0
)
(local
	
	noExitNorth = 0
	noExitSouth = 0
	noExitEast = 0
	noExitWest = 0
	
)


(instance rm656 of Rm
	(properties
		picture 604
		north 646
		east 657
		south 666
		west 655
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript)
		(switch gPreviousRoomNumber
			((gRoom north:)	
				(PlaceEgo 123 100 2)	
			)
			((gRoom south:)	
				(PlaceEgo 209 175 3)	
			)
			((gRoom west:)	
				(PlaceEgo 10 115 0)	
			)
			((gRoom east:)	
				(PlaceEgo 300 125 1)	
			)
			(else 
				(PlaceEgo 215 80 2)		
			)
		)
		(SetUpEgo)
		(gEgo init:)
		(RunningCheck)
		
		(backgroundTree2 init: setPri: 0)
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
		x 210
		view 698
		cel 1
	)
)