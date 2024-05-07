;;; Sierra Script 1.0 - (do not remove this comment)
(script# 12)
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
	
	rm012 0
	
)
(local
	onMud = 0
)

(instance rm012 of Rm
	(properties
		picture scriptNumber
		north 0
		east 0
		south 17
		west 13
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript)
		
		(SetUpEgo)
		(gEgo init:)
		(RunningCheck)
		
		(mudBoard init: hide: setScript: mudScript ignoreControl: ctlWHITE ignoreActors:)
		(alterEgo init: ignoreActors: ignoreControl: ctlWHITE hide:)
		(sign init: setScript: signScript)
		
		(switch gPreviousRoomNumber
			(13				
				(mudScript changeState: 1)	
			)
			(17			
				(gEgo posn: 160 175 loop: 3)
				(mudBoard hide:)	
			)
			(else 
				(gEgo posn: 160 160 loop: 3)
				(mudBoard hide:)
			)
		)
		
	)
)

(instance RoomScript of Script
	(properties)
	
	(method (doit)
		(super doit:)
		; code executed each game cycle
		(if (& (gEgo onControl:) ctlYELLOW)
			(if (not onMud)
				(if(not gAnotherEgo) 
					(mudScript changeState: 3)
				else
					(mudScript changeState: 8)	
				)
			)
		)
	)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		; handle Said's, etc...
		
		(if (Said 'look,read/sign,post')
			(if (<= (gEgo distanceTo: sign) 50)
				(signScript changeState: 1)	
			else
				(PrintNotCloseEnough)
			)
		)
	)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0 ; Handle state changes
			)
		)
	)
)

(instance mudScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0
			
			)
			(1
				(ProgramControl)
				(gEgo hide:)
				(mudBoard show: setMotion: MoveTo 73 140 self)
			)
			(2
				(mudBoard hide:)
				(gEgo show: posn: (mudBoard x?)(mudBoard y?) loop: 0)
				(PlayerControl)	
			)
			(3 ; Sending EGO up the Mud Hill
				(= onMud 1)
				(ProgramControl)
				(gEgo hide:)
				(alterEgo show: view: 230 posn: (gEgo x?) (gEgo y?) xStep: 1 yStep: 1 cycleSpeed: 0 setMotion: MoveTo 30 100 mudScript setCycle: Walk) ; running up the hill
			)
			(4
				(alterEgo view: 428 loop: 0 cel: 0 setCycle: End cycleSpeed: 2 xStep: 6 yStep: 3 setMotion: MoveTo 91 148 mudScript) ; falling down
			)
			(5
				(alterEgo view: 409 loop: 1 cel: 0 setCycle: End mudScript) ; standing up
			)
			(6
				(gEgo show: posn: 91 148 loop: 2 setMotion: MoveTo 120 156 mudScript) ; walking away from hill
				(alterEgo hide:)
			)
			(7
				(PlayerControl)				
				(= onMud 0)
				(gEgo loop: 2)
			)
			(8 ; Sending Leah up the Mud Hill
				(= onMud 1)
				(ProgramControl)
				(gEgo hide:)
				(alterEgo show: view: 351 posn: (gEgo x?) (gEgo y?) xStep: 1 yStep: 1 cycleSpeed: 0 setMotion: MoveTo 30 100 mudScript setCycle: Walk) ; running up the hill
			)
			(9
				(alterEgo view: 362 posn: (- (alterEgo x?) 6) (+ (alterEgo y?) 3) loop: 1 cel: 0 cycleSpeed: 2 setCycle: End mudScript) ; tripping
			)
			(10
				(alterEgo view: 369 xStep: 6 yStep: 3 setMotion: MoveTo 91 148 mudScript) ; falling down
			)
			(11
				(alterEgo view: 362 loop: 5 cel: 0 setCycle: End mudScript) ; standing up
			)
			(12 (= cycles 15)
				(alterEgo posn: (+ (alterEgo x?) 15)(alterEgo y?) loop: 3 cel: 0 setCycle: Fwd) ; Blinking
			)
			(13
				(alterEgo loop: 4 cel: 0 setCycle: End mudScript) ; wiping off Mud
			)
			(14
				(gEgo show: posn: 94 148 loop: 2 setMotion: MoveTo 120 156 mudScript) ; walking away from hill
				(alterEgo hide:)
			)
			(15
				(PlayerControl)				
				(= onMud 0)
				(gEgo loop: 2)
			)
		)
	)
)
(instance signScript of Script
	(properties)
	
	(method (changeState newState button)
		(= state newState)
		(switch state
			(0)
			(1
				(ProgramControl)
				(gEgo setMotion: MoveTo (sign x?) (+ (sign y?) 3) self)
			)
			(2 (= cycles 2)
				(gEgo loop: 2)	
			)
			(3
				(PlayerControl)
				(if [gSign 0] ; came down
					(= button (Print "Up the mountain?" #button { Yes_} 1 #button { No_} 0))
					(if button
						(gRoom newRoom: 217)	
					)		
				else
					(Print "A blank sign. It doesn't help you yet.")
				)	
			)
		)
	)
)
(instance mudBoard of Act
	(properties
		y 90
		x 10
		view 370
		loop 0
		xStep 8
		yStep 4
	)
)
(instance alterEgo of Act
	(properties
		y 180
		x 27
		view 0
		loop 1
	)
)
(instance sign of Prop
	(properties
		y 125
		x 110
		view 999
		loop 6
	)
)