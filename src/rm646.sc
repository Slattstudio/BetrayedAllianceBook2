;;; Sierra Script 1.0 - (do not remove this comment)
(script# 646)
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
	rm646 0
)
(local

	noExitNorth = 0
	noExitSouth = 0
	noExitEast = 0
	noExitWest = 0
		
)

(instance rm646 of Rm
	(properties
		picture 601
		north 636
		east 647
		south 656
		west 645
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