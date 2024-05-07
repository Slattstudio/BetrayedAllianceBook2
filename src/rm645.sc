;;; Sierra Script 1.0 - (do not remove this comment)
(script# 645)
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
	rm645 0
)
(local
	
	noExitNorth = 1
	noExitSouth = 1
	noExitEast = 1
	noExitWest = 1
	
)

(instance rm645 of Rm
	(properties
		picture 606
		north 635
		east 646
		south 655
		west 644
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript)
		(switch gPreviousRoomNumber
			((gRoom north:)	
				(PlaceEgo 215 80 2)	
			)
			((gRoom south:)	
				(PlaceEgo 200 175 3)	
			)
			((gRoom west:)	
				(PlaceEgo 10 125 0)	
			)
			((gRoom east:)	
				(PlaceEgo 300 120 1)	
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
		;(if noExitSouth
		;	(gEgo observeControl: ctlNAVY)	
		;)
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
