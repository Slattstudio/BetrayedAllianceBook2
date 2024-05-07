;;; Sierra Script 1.0 - (do not remove this comment)
(script# 470)
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
	rm470 0
)


(instance rm470 of Rm
	(properties
		picture scriptNumber
		north 0
		east 23
		south 471
		west 6
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript)
		
		(SetUpEgo)
		(gEgo init:)
		(RunningCheck)
		
		(switch gPreviousRoomNumber
			(6 
				(PlaceEgo 10 80 0)
				(RoomScript changeState: 1)	
			)
			(else 
				(PlaceEgo 150 100 1)
			)
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
		; handle Said's, etc...
	)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0 ; Handle state changes
			)
			(1	; entering from the log bridge
				(ProgramControl)
				(gEgo setMotion: MoveTo 100 80 self)
			)
			(2
				(PlayerControl)	
			)
		)
	)
)
