;;; Sierra Script 1.0 - (do not remove this comment)
(script# 234)
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
	rm234 0
)

(local
	onMud = 0
	timesTried = 0
)

(instance rm234 of Rm
	(properties
		picture scriptNumber
		north 0
		east 0
		south 0
		west 236
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript setRegions: 205)
		(switch gPreviousRoomNumber
			(236
				(gEgo posn: 30 140 loop: 0)	
			)
			(else 
				(gEgo posn: 100 140 loop: 1)
			)
		)
		(SetUpEgo)
		(gEgo init:)
		(RunningCheck)
		
		(alterEgo init: ignoreActors: ignoreControl: ctlWHITE hide: setScript: mudScript)
		(treeOne init: ignoreActors:)
		(treeTwo init: ignoreActors:)
		
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
				(if (== (OnControl ocSPECIAL (pEvent x?) (pEvent y?)) ctlMAROON) ; mudslope
					(PrintOther 234 0)
					
				)
			)
		)
		(if (Said 'climb>')
			(if (Said '/mud,hill')
				(ProgramControl)
				(gEgo setMotion: MoveTo 167 133)
			)
			(if (Said '/tree')
				(RoomScript changeState: 1)
			)	
		)
		(if (Said 'look>')
			(if (Said '/tree')
				(PrintOther 234 4)
			)
			(if (Said '/mud,hill')
				(PrintOther 234 0)
			)
			(if (Said '/forest,verlorn')
				(PrintOther 0 105)	
			)
			(if (Said '[/!*]')
				(PrintOther 234 10)
			)
		)
		
	)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0 ; Handle state changes
			)
			(1	; move to climb tree
				(ProgramControl)
				(gEgo setMotion: MoveTo 107 117 self ignoreControl: ctlWHITE)
			)
			(2	; face the tree
				(= cycles 5)
				(gEgo loop: 3)
			)
			(3
				(PlayerControl)
				(gEgo observeControl: ctlWHITE)
				(PrintOther 234 3)	
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
				(alterEgo show: view: 230 posn: (gEgo x?) (gEgo y?) xStep: 1 yStep: 1 cycleSpeed: 0 setMotion: MoveTo 232 87 mudScript setCycle: Walk) ; running up the hill
			)
			(2
				(alterEgo view: 428 loop: 1 cel: 0 setCycle: End cycleSpeed: 2 xStep: 6 yStep: 3 setMotion: MoveTo 157 138 mudScript) ; falling down
			)
			(3
				(alterEgo view: 409 loop: 1 cel: 0 setCycle: End mudScript) ; standing up
			)
			(4
				(gEgo show: posn: 157 138 loop: 2 setMotion: MoveTo 140 140 mudScript) ; walking away from hill
				(alterEgo hide:)
			)
			(5
				(PlayerControl)				
				(= onMud 0)
				(gEgo loop: 2)
				
				(++ timesTried)
				(if (< timesTried 5)
					(PrintOther 234 1)
				else
					(PrintOther 234 2)
				)
			)
			(6 ; Sending Leah up the Mud Hill
				(= onMud 1)
				(ProgramControl)
				(gEgo hide:)
				(alterEgo show: view: 351 posn: (gEgo x?) (gEgo y?) xStep: 1 yStep: 1 cycleSpeed: 0 setMotion: MoveTo 232 87 mudScript setCycle: Walk) ; running up the hill
			)
			(7
				(alterEgo view: 362 posn: (+ (alterEgo x?) 11) (+ (alterEgo y?) 3) loop: 0 cel: 0 cycleSpeed: 2 setCycle: End mudScript) ; tripping
			)
			(8
				(alterEgo view: 363 xStep: 6 yStep: 3 setMotion: MoveTo 157 138 mudScript) ; falling down
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
				(gEgo show: posn: 149 138 loop: 2 setMotion: MoveTo 140 140 mudScript) ; walking away from hill
				(alterEgo hide:)
			)
			(13 (= cycles 2)
				(PlayerControl)	
				(gEgo loop: 2)
			)
			(14			
				(= onMud 0)
				;(gEgo loop: 2)
				
				(++ timesTried)
				(if (< timesTried 3)
					(PrintOther 234 1)
				else
					(PrintOther 234 2)
				)
				
			)
			
		)
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
(procedure (PrintOther textRes textResIndex)
	(Print textRes textResIndex		
		#width 280
		#at -1 10
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
(instance treeOne of Prop
	(properties
		y 80
		x 60
		view 999
		loop 0
	)
)
(instance treeTwo of Prop
	(properties
		y 70
		x 200
		view 999
		loop 2
	)
)