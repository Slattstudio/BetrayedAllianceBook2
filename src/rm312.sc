;;; Sierra Script 1.0 - (do not remove this comment)
(script# 312)
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
	rm312 0
)

(local

	mysteryMusic = 0
	blockUp = 1
	doorOpen = 0
	doorOpeningMotion = 0
	
)

(instance rm312 of Rm
	(properties
		picture scriptNumber
		north 0
		east 404
		south 0
		west 0
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript)
		(switch gPreviousRoomNumber
			(else 
				(gEgo posn: 150 100 loop: 1)
			)
		)
		(SetUpEgo)
		(gEgo init: observeControl: ctlYELLOW)
		(bushes init:)
		(door init: ignoreActors: ctlWHITE setPri: 4 setScript: doorScript)
		(stone init: ignoreActors: setScript: triangleScript ignoreControl: ctlWHITE setPri: 6)
	)
)

(instance RoomScript of Script
	(properties)
	
	(method (doit)
		(super doit:)
		; code executed each game cycle
		(if (& (gEgo onControl:) ctlMAROON)
			(bushes cel: 1)
			(if (not mysteryMusic)
				(gTheSoundFX number: 4 play:)
				(= mysteryMusic 1)
				(gEgo setMotion: NULL)
			)
		else
			(bushes cel: 0)
			(= mysteryMusic 0)
		)
		
		(if (& (gEgo onControl:) ctlGREY)
			(if (not doorOpen)
				(if (not doorOpeningMotion)
					(doorScript changeState: 1)
				)
			)
		)
		(if (& (gEgo onControl:) ctlFUCHSIA)
			(if doorOpen
				(if (not doorOpeningMotion)
					(doorScript changeState: 5)
				)
			)
		)
		
		(if doorOpen
			(gEgo ignoreControl: ctlYELLOW)
		else
			(gEgo observeControl: ctlYELLOW)	
		)
	)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		; handle Said's, etc...
		(if(Said 'hi')
			(if blockUp
				(triangleScript changeState: 1)
			)	
		)
		(if(Said 'open/door')
			(if doorOpen
				(Print "It already is.")
			else
				(doorScript changeState: 1)
			)	
		)
		(if(Said 'close/door')
			(if doorOpen
				(doorScript changeState: 5)
			else
				(Print "It already is.")
			)	
		)
	)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0 (= cycles 1) ; Handle state changes
				
			)
			(1 (= cycles (Random 40 140))
			)
			(2 
				(if blockUp
					(stone setCycle: End RoomScript cycleSpeed: 3)
				)
			)
			(3
				(RoomScript changeState: 1)
			)
		)
	)
)

(instance triangleScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0
			)
			(1
				(stone loop: 1 cel: 0 setCycle: End triangleScript cycleSpeed: 2)
				(= blockUp 0)
				(ShakeScreen 1)
			)
			(2 (= cycles 3)
			)
			(3
				(stone posn: 95 43 loop: 2 cel: 0 setCycle: End cycleSpeed: 2 yStep: 5 setMotion: MoveTo 95 100 triangleScript )	
			)
			(4
				(ShakeScreen 2)
			)
		)
	)
)

(instance doorScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0
			)
			(1 ;Open the door
				(= doorOpeningMotion 1)
				(door loop: 1 cel: 0 setCycle: End doorScript cycleSpeed: 2)
				
			)
			(2
				(= doorOpeningMotion 0)
				(= doorOpen 1)
			)
			(5 ;Close the door
				(= doorOpeningMotion 1)
				(door loop: 2 cel: 12 setCycle: Beg doorScript cycleSpeed: 2)
				(= doorOpen 0)
			)
			(6
				(= doorOpeningMotion 0)
				
			)
		)
	)
)
			

(instance bushes of Prop
	(properties
		y 215
		x 10
		view 803
		loop 1
	)
)
(instance door of Prop
	(properties
		y 88
		x 87
		view 11
	)
)
(instance stone of Act
	(properties
		y 33
		x 84
		view 10
	)
)