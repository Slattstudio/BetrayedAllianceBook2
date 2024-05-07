;;; Sierra Script 1.0 - (do not remove this comment)
(script# 237)
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
	
	rm237 0 
	
)
(local
	
	movingNorth = 0
	
)


(instance rm237 of Rm
	(properties
		picture scriptNumber
		north 0
		east 26
		south 245
		west 9
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript)
		(switch gPreviousRoomNumber
			(9 
				(gEgo posn: 20 140 loop: 0)
			)
			(17 
				(gEgo posn: 160 105 loop: 2)
			)
			(26 
				(gEgo posn: 300 140 loop: 1)
			)
			(245
				(gEgo posn: 160 170 loop: 3)
			)
			(else 
				(gEgo posn: 150 100 loop: 1)
			)
		)
		(SetUpEgo)
		(gEgo init:)
		(RunningCheck)
	)
)

(instance RoomScript of Script
	(properties)
	
	(method (doit)
		(super doit:)
		; code executed each game cycle
		(if (& (gEgo onControl:) ctlMAROON)
			(PlayerControl)
			(gRoom newRoom: 17)
		)
		(if (& (gEgo onControl:) ctlSILVER)
			(if (== movingNorth 0)
				(RoomScript changeState: 1)
				(= movingNorth 1)	
			)
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
			(1
				(ProgramControl)
				(gEgo setMotion: MoveTo (gEgo x?) 1)
			)
		)
	)
)
