;;; Sierra Script 1.0 - (do not remove this comment)
(script# 311)
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
	
	rm311 0
	
)


(instance rm311 of Rm
	(properties
		picture scriptNumber
		north 0
		east 0
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
		(gEgo init:)
		
		(frog init: ignoreControl: ctlWHITE setScript: eatenScript)
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
		(if(Said 'pet')
			(eatenScript changeState: 1)	
		)
	)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0 ; Handle state changes
				(frog setCycle: Walk setMotion: MoveTo 280 100 RoomScript ignoreActors: cycleSpeed: 2)
			)
			(1
				(frog setMotion: MoveTo 280 150 RoomScript ignoreActors:)	
			)
			
			(2
				(frog setMotion: MoveTo 220 150 RoomScript ignoreActors:)	
			)
			(3
				(frog setMotion: MoveTo 220 100 RoomScript ignoreActors:)	
			)
			(4
				(self changeState: 0)
			)
		)
	)
)

(instance eatenScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)
			(1 
				(gEgo setMotion: MoveTo (- (frog x?) 30)(frog y?) self)
				(frog setMotion: NULL loop: 1)
			)
			(2
				(gEgo hide:)
				(frog view: 13 loop: 0 cel: 0 posn: (- (frog x?) 9)(frog y?)setCycle: End self cycleSpeed: 4)	
			)
			(3 (= cycles 16)
				(frog loop: 1 cel: 0 setCycle: Fwd cycleSpeed: 2)	
			)
			(4
				(frog loop: 2 cel: 0 setCycle: End self cycleSpeed: 4)	
			)
			(5 (= seconds 2)
				; time passes for comedy
			)
			(6 
				(frog loop: 3 cel: 0 posn: (- (frog x?) 10)(frog y?) setCycle: End self cycleSpeed: 4)	
			)
			(7 (= cycles 5) 
				(frog loop: 4 setCel: 0)	
			)
			(8
				(Print "Dead")	
			)
			
			
		)
	)
)

(instance frog of Act
	(properties
		y 100
		x 280
		view 305
		loop 1
	)
)
