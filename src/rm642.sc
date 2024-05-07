;;; Sierra Script 1.0 - (do not remove this comment)
(script# 642)
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
	rm642 0
)
(local
	
)

(instance rm642 of Rm
	(properties
		picture 606
		north 632
		east 0
		south 652
		west 641
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
		
		(blockRightBush init:)
		(blockRightBush2 init:)
		(blockRightRock init:)
		(blockRightRock2 init:)
		
		(gEgo observeControl: ctlTEAL)	
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
(instance blockRightBush of Prop
	(properties
		y 125
		x 320
		view 805
		loop 5
		cel 3
	)
)
(instance blockRightBush2 of Prop
	(properties
		y 145
		x 325
		view 805
		loop 4
		cel 0
	)
)
(instance blockRightRock of Prop
	(properties
		y 160
		x 310
		view 805
		loop 2
		cel 5
	)
)
(instance blockRightRock2 of Prop
	(properties
		y 113
		x 310
		view 805
		loop 2
		cel 2
	)
)