;;; Sierra Script 1.0 - (do not remove this comment)
(script# 24)
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
	
	rm024 0
	
)

(local
	onMud = 0
)

(instance rm024 of Rm
	(properties
		picture scriptNumber
		north 0
		east 0
		south 0
		west 25
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript setRegions: 205)
		(switch gPreviousRoomNumber
			(25 
				(gEgo posn: 20 170 loop: 0)
			)
			(else 
				(gEgo posn: 150 100 loop: 1)
			)
		)
		(SetUpEgo)
		(gEgo init:)
		
		(alterEgo init: ignoreActors: ignoreControl: ctlWHITE hide: setScript: mudScript)
		
		(RunningCheck)
		
		
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
					(mudScript changeState: 1)
				else
					(mudScript changeState: 6)	
				)
			)
		)
	)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if (& (pEvent modifiers?) emRIGHT_BUTTON)
				(if (== (OnControl ocPRIORITY (pEvent x?) (pEvent y?)) ctlNAVY)	; mud slide
					(PrintOther 24 1)	
				)
			
			)
		)
		; handle Said's, etc...
		(if (Said 'look>')
			(if (Said 'mud,hill')
				(PrintOther 24 1)
			)
			(if (Said 'rock')
				(PrintOther 24 2)
			)
			(if (Said '/forest,verlorn')
				(PrintOther 0 105)	
			)
			(if (Said '[/!*]')
				(PrintOther 24 0)
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
			(0)
			(1 ; Sending EGO up the Mud Hill
				(= onMud 1)
				(ProgramControl)
				(gEgo hide:)
				(alterEgo show: view: 230 posn: (gEgo x?) (gEgo y?) xStep: 1 yStep: 1 cycleSpeed: 0 setMotion: MoveTo 300 95 mudScript setCycle: Walk) ; running up the hill
			)
			(2
				(alterEgo view: 428 loop: 1 cel: 0 setCycle: End cycleSpeed: 2 xStep: 6 yStep: 3 setMotion: MoveTo 240 143 mudScript) ; falling down
			)
			(3
				(alterEgo view: 409 loop: 1 cel: 0 setCycle: End mudScript) ; standing up
			)
			(4
				(gEgo show: posn: 240 143 loop: 2 setMotion: MoveTo 225 143 mudScript) ; walking away from hill
				(alterEgo hide:)
			)
			(5
				(PlayerControl)				
				(= onMud 0)
				(gEgo loop: 2)
			)
			(6 ; Sending Leah up the Mud Hill
				(= onMud 1)
				(ProgramControl)
				(gEgo hide:)
				(alterEgo show: view: 351 posn: (gEgo x?) (gEgo y?) xStep: 1 yStep: 1 cycleSpeed: 0 setMotion: MoveTo 300 95 mudScript setCycle: Walk) ; running up the hill
			)
			(7
				(alterEgo view: 362 posn: (+ (alterEgo x?) 11) (+ (alterEgo y?) 3) loop: 0 cel: 0 cycleSpeed: 2 setCycle: End mudScript) ; tripping
			)
			(8
				(alterEgo view: 363 xStep: 6 yStep: 3 setMotion: MoveTo 240 143 mudScript) ; falling down
			)
			(9
				(alterEgo view: 362 loop: 2 cel: 0 setCycle: End mudScript) ; standing up
			)
			(10 (= cycles 15)
				(alterEgo posn: (- (alterEgo x?) 15)(alterEgo y?) loop: 3 cel: 0 setCycle: Fwd) ; Blinking
			)
			(11
				(alterEgo loop: 4 cel: 0 setCycle: End mudScript) ; wiping off Mud
			)
			(12
				(gEgo show: posn: 230 143 loop: 2 setMotion: MoveTo 215 143 mudScript) ; walking away from hill
				(alterEgo hide:)
			)
			(13
				(PlayerControl)				
				(= onMud 0)
				(gEgo loop: 2)
			)
			
		)
	)
)
(procedure (PrintOther textRes textResIndex)
	(Print textRes textResIndex
		#width 280
		#at -1 10
	)
)
(procedure (checkEvent pEvent x1 x2 y1 y2)
	(if
		(and
			(> (pEvent x?) x1)
			(< (pEvent x?) x2)
			(> (pEvent y?) y1)
			(< (pEvent y?) y2)
		)
		(return TRUE)
	else
		(return FALSE)
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