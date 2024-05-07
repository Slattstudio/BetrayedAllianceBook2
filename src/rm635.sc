;;; Sierra Script 1.0 - (do not remove this comment)
(script# 635)
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
	rm635 0
)

(local

	noExitNorth = 0
	noExitSouth = 0
	noExitEast = 0
	noExitWest = 0
	
)


(instance rm635 of Rm
	(properties
		picture 603
		north 625
		east 636
		south 645
		west 634
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
