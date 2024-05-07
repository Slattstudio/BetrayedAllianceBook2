;;; Sierra Script 1.0 - (do not remove this comment)
(script# 379)
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
	
	rm379 0
	
)


(instance rm379 of Rm
	(properties
		picture scriptNumber
		north 0
		east 378
		south 0
		west 380
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript)
		
		(SetUpEgo)
		(gEgo init:)
		(RunningCheck)
		
		(switch gPreviousRoomNumber
			(378
				(PlaceEgo 302 178 1)
				(RoomScript changeState: 1)	
			)
			(380
				(PlaceEgo 24 133 0)
			)
				
			(else 
				(gEgo posn: 150 100 loop: 1)
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
			(1
				(ProgramControl)
				(gEgo setMotion: MoveTo 213 164 self)	
			)
			(2
				(PlayerControl)	
			)
		)
	)
)
