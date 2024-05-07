;;; Sierra Script 1.0 - (do not remove this comment)
(script# 9)
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
	
	rm009 0	

)

(instance rm009 of Rm
	(properties
		picture scriptNumber
		north 0
		east 237
		south 0
		west 27
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript)
		(switch gPreviousRoomNumber
			(27 
				(gEgo posn: 20 140 loop: 0  )
			)
			(237 
				(gEgo posn: 300 140 loop: 1  )
			)
			(else 
				(gEgo posn: 60 140 loop: 0  )
			)
		)
		(SetUpEgo)
		(gEgo init:)
		(RunningCheck)
		
		
		(alterEgo init: setCycle: Walk xStep: 5 setMotion: MoveTo 300 140 ignoreActors:)
		(barry init: setCycle: Walk cycleSpeed: 1 xStep: 5 setMotion: MoveTo 300 140 ignoreActors:)
		(bushBottom init:)
	)
)

(instance RoomScript of Script
	(properties)
	
	(method (doit)
		(super doit:)
		; code executed each game cycle
		(if (& (gEgo onControl:) ctlMAROON)
			(PlayerControl)
			(gRoom newRoom: 274)
		)
	)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		(if(Said 'hi') (Print 9 3 #width 300 #title "Deivore:" #at -1 10)
			(Print 9 4 #width 300 #title "Deivore:" #at -1 10)
			(Print 9 5 #width 300 #title "Deivore:" #at -1 10)
			)
			
		
					
		; handle Said's, etc...
	)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0; Handle state changes
				(= cycles 1)
			)(1
				(alterEgo setMotion: MoveTo 300 140)
			)
		)
	)
)
(instance barry of Act
	(properties
		y 135
		x 10
		view 356
	)
)
(instance alterEgo of Act
	(properties
		y 140
		x 70
		view 230
	)
)
(instance bushBottom of Prop
	(properties
		y 210
		x 160
		view 803
	)
)
