;;; Sierra Script 1.0 - (do not remove this comment)
; score + 1
(script# 268)
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
	rm268 0
)

(local
	
	onMud = 0
	onRock = 0
	onMetal = 0
	myEvent
	
	mudMessage = 0	; give the player the message about the thinner mud area only once.
	
)


(instance rm268 of Rm
	(properties
		picture scriptNumber
		north 0
		east 269
		south 0
		west 267
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript setRegions: 205)
		(switch gPreviousRoomNumber
			(267 
				(PlaceEgo 20 145 0)
				;(gEgo posn: 20 145 loop: 0)
			)
			(269 
				(PlaceEgo 310 50 0)
				;(gEgo posn: 310 50 loop: 0)
				(= onMud 2)
				;(downScript changeState: 13)
			)
			(else
				(PlaceEgo 60 145 1) 
				;(gEgo posn: 60 145 loop: 1)
				(if (> (gEgo x?) 245)	; on rack
					(= onMetal 1)
					(= onMud 0)
					(= onRock 0)
				else
					(if (> (gEgo x?) 170)	; on rack
						(= onMetal 0)
						(= onMud 0)
						(= onRock 1)
					else
						(= onMetal 0)
						(= onMud 0)
						(= onRock 0)
					)
				)	
			)
		)
		(SetUpEgo)
		(gEgo init: setScript: downScript)
		(RunningCheck)
		
		(alterEgo init: ignoreActors: ignoreControl: ctlWHITE hide: setScript: mudScript)
		(metalWalkway init: setScript: throwScript ignoreActors: setPri: 1)
		(if (== (IsOwnedBy 4 268) FALSE)
			(metalWalkway hide:)
		)
		(if (== onMud 2)
			(downScript changeState: 13)
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
				(if onRock
					;(mudScript changeState: 6)	
				else
					(mudScript changeState: 1)
				)
			)
		)
		(if (>= (gEgo distanceTo: metalWalkway) 10)
			(if onMetal
				;(mudScript changeState: 11)
			)
		)
		;(if (or (or onMetal onRock) onMud)
		;	(= gNoClick 1)	
		;else
		;	(= gNoClick 0)
		;)
		;(if (or onRock onMetal)
		;	(= gDisableSwitch 1)	
		;)
	)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if (& (pEvent modifiers?) emRIGHT_BUTTON)
				(if (== (OnControl ocPRIORITY (pEvent x?) (pEvent y?)) ctlNAVY)
					(PrintOther 268 0)
				)
				
				;(if	(checkEvent pEvent (metalWalkway nsLeft?) (metalWalkway nsRight?) (metalWalkway nsTop?) (metalWalkway nsBottom?))
				;	(if (IsOwnedBy 4 268)
				;		(PrintOther 268 9)
				;	)	
				;else
					(if (== (OnControl ocSPECIAL (pEvent x?) (pEvent y?)) ctlRED)
						(if (== (IsOwnedBy 4 268) TRUE)
							(PrintOther 268 9)
						else
							(PrintOther 268 12)
						)
					)
				;)
			)
			(if (and (< (pEvent x?) 30) (< (pEvent y?) 30))
				; clicking on switch icon
			else
				(if (> (pEvent x?) (gEgo x?))
					(if onRock
						(if (== (IsOwnedBy 4 268) TRUE)	; if metal is on the mud
							(mudScript changeState: 6)
						else
							(downScript changeState: 6)
						)	
					else
						(if onMetal
							(mudScript changeState: 11)	
						)
					)	
				else
					(if (not onMud)
						(if onRock
							(downScript changeState: 3)	
						else
							(if onMetal
								(downScript changeState: 1)	
							)
						)
					)	
				)
			)
		)
		(if (== (pEvent type?) evJOYSTICK)
			(if (< (pEvent message?) 5) ; if player presses up, right, right-up, or right-down
			;	(if (not handsOff) (moveRight))
				(if (not onMud)
					(if onRock
						(if (== (IsOwnedBy 4 268) TRUE)	; if metal is on the mud
							(mudScript changeState: 6)
						else
							(downScript changeState: 6)
						)
					else
						(if onMetal
							(mudScript changeState: 11)
						)
					)
				)
			)
			(if (and (> (pEvent message?) 4) (< (pEvent message?) 9) )    ; If pressed the LEFT arrow
				(if (not onMud)
					(if onRock
						(downScript changeState: 3)	
					else
						(if onMetal
							(downScript changeState: 1)	
						)
					)
				)
			)
		)
		; handle Said's, etc...
		(if (Said 'look>')
			(if (Said '/mud, hill')
				(if (== (IsOwnedBy 4 268) TRUE)	; if metal is on the mud
					(PrintOther 268 4)
				else
					(PrintOther 268 3)	
					(PrintOther 268 14)	
				)
			)
			(if (Said '/grass, ground')
				(if (== (IsOwnedBy 4 268) TRUE)
					(PrintOther 268 9)
				else
					(PrintOther 268 12)
				)
			)
			(if (Said '/prongs')
				(if (gEgo has: 4)
					(Print 0 4 #title "Rack" #icon 613)
				else
					(if (== (IsOwnedBy 4 268) TRUE)
						(PrintOther 268 11)
					else
						(Print "You do not see anything like that here.")
					)
				)
			)
			(if (Said '/rock')
				(PrintOther 268 0)	
			)
			(if (Said '/metal,rack')
				(if (== (IsOwnedBy 4 268) TRUE)	; if metal is on the mud
					(PrintOther 268 9)	
				else
					(if (gEgo has: 4)
						(Print 0 4 #title "Rack" #icon 613)
					else
						(PrintOther 268 8)
					)
				)
			)
			(if (Said '[/!*]')
				(PrintOther 268 5)
			)
		)
		(if (or (Said 'use/stick/walk,climb,mud')
				(Said 'climb//stick'))
			(if (gEgo has: 7)
				(PrintOther 268 6)
			else
				(PrintDontHaveIt)
			)	
		)
		(if (or (Said 'throw,climb/metal,rack')
				(Said 'use/metal,rack/mud,hill')
				(Said 'use, put/rack,metal/climb,mud')
				(Said 'climb/[hill]/rack,metal')
				(Said 'climb//rack,metal'))
			(if (gEgo has: 4)
				(if onRock
					(throwScript changeState: 1)
				else
					(Print "This isn't a good place to do that.")
				)
			else
				(PrintDontHaveIt)
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

(instance downScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0
			)
			(1 ; Sending EGO down to the rock from metal
				(= onMud 1)
				(ProgramControl)
				(gEgo hide:)
				; running down the hill
				(alterEgo show: view: 351 posn: (gEgo x?) (gEgo y?) xStep: 3 yStep: 1 cycleSpeed: 0 setMotion: MoveTo 209 99 self setCycle: Walk) 
				
			)
			(2
				(gEgo show: posn: (alterEgo x?)(alterEgo y?) loop: 2)
				(alterEgo hide:)
				(PlayerControl)				
				(= onMud 0)
				(= onRock 1)
				(= onMetal 0)
			)
			(3 ; Sending EGO down to grass from rock
				(= onMud 1)
				(ProgramControl)
				(gEgo hide:)
				; running down the hill
				(alterEgo show: view: 351 posn: (gEgo x?) (gEgo y?) xStep: 3 yStep: 1 cycleSpeed: 0 setMotion: MoveTo 71 134 self setCycle: Walk) 
				
			)
			(4
				(gEgo show: posn: (alterEgo x?)(alterEgo y?) loop: 2)
				(alterEgo hide:)
				(PlayerControl)				
				(= onMud 0)
				(= onRock 0)
			)
			; falling down the mud
			(6 
				(= onMud 1)
				(= onRock 0)
				(ProgramControl)
				(gEgo hide:)
				(alterEgo show: view: 351 posn: (gEgo x?) (gEgo y?) xStep: 1 yStep: 1 cycleSpeed: 0 setMotion: MoveTo (metalWalkway x?) (- (metalWalkway y?) 2) self setCycle: Walk) ; running up the hill
			)
			(7
				(alterEgo view: 362 posn: (+ (alterEgo x?) 11) (+ (alterEgo y?) 3) loop: 0 cel: 0 cycleSpeed: 2 setCycle: End self) ; tripping
			)
			(8
				(alterEgo view: 363 xStep: 6 yStep: 3 setMotion: MoveTo 218 99 self) ; falling down
			)
			(9
				(alterEgo view: 362 loop: 2 cel: 0 setCycle: End self) ; standing up
			)
			(10 (= cycles 15)
				(alterEgo posn: (- (alterEgo x?) 10) (alterEgo y?) loop: 3 cel: 0 setCycle: Fwd) ; Blinking
			)
			(11
				(alterEgo loop: 4 cel: 0 setCycle: End self) ; wiping off Mud
				(if (not mudMessage)
					(PrintOther 268 13)
					(= mudMessage 1)
				)
			)
			(12
				(gEgo show: posn: 209 99 loop: 2) ; walking away from hill
				(alterEgo hide:)
				(PlayerControl)				
				(= onMud 0)
				(= onRock 1)
				(= onMetal 0)
				(gEgo loop: 2)
			)
			; Entering from Eastern Room
			(13 ; Sending EGO down to metal 
				(= onMud 1)
				(ProgramControl)
				(gEgo hide:)
				; running down the hill
				(alterEgo show: view: 351 posn: 310 50 xStep: 3 yStep: 1 cycleSpeed: 0 setMotion: MoveTo 278 58 self setCycle: Walk) 
				
			)
			(14
				(gEgo show: posn: (alterEgo x?)(alterEgo y?) loop: 2)
				(alterEgo hide:)
				(PlayerControl)				
				(= onMud 0)
				(= onRock 0)
				(= onMetal 1)
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
			(1 ; Sending EGO up to the rock
				(= onMud 1)
				(ProgramControl)
				(gEgo hide:)
				; running up the hill
				(alterEgo show: view: 351 posn: (gEgo x?) (gEgo y?)
					xStep: 1 yStep: 1 cycleSpeed: 0 setMotion: MoveTo 209 99 mudScript setCycle: Walk) 
			)
			(2 (= cycles 32) ; heavy breathing
				(alterEgo view: 454 loop: 1 cel: 0 setCycle: Fwd cycleSpeed: 6)	
			)
			(3	; wiping brow
				(alterEgo loop: 0 cel: 0 setCycle: End self cycleSpeed: 2)	
			)
			(4 (= cycles 14) ; allowing time to smile
			
			)
			(5	; standing on rock
				(gEgo show: posn: (alterEgo x?)(alterEgo y?) loop: 2)
				(alterEgo hide:)
				(PlayerControl)				
				(= onMud 0)
				(= onRock 1)
			)
			(6 ; Running up to metal walkway
				(= onMud 1)
				(= onRock 0)
				(ProgramControl)
				(gEgo hide:)
				; running up the hill
				(alterEgo show: view: 351 posn: (gEgo x?) (gEgo y?)
					xStep: 1 yStep: 1 cycleSpeed: 0 setMotion: MoveTo (metalWalkway x?) (- (metalWalkway y?) 2) mudScript setCycle: Walk) 	
				
			)
			(7 (= cycles 32) ; heavy breathing
				(alterEgo view: 454 loop: 1 cel: 0 setCycle: Fwd cycleSpeed: 6)	
			)
			(8	; wiping brow
				(alterEgo loop: 0 cel: 0 setCycle: End self cycleSpeed: 2)	
			)
			(9 (= cycles 14) ; allowing time to smile
			
			)
			(10	; standing on metal
				(gEgo show: posn: (alterEgo x?)(alterEgo y?) loop: 2)
				(alterEgo hide:)
				(PlayerControl)				
				(= onMud 0)
				(= onRock 0)
				(= onMetal 1)
			)
			(11
				(= onMud 1)
				(= onMetal 0)
				(ProgramControl)
				(gEgo hide:)
				; running up the hill
				(alterEgo show: view: 351 posn: (gEgo x?) (gEgo y?)
					xStep: 1 yStep: 1 cycleSpeed: 0 setMotion: MoveTo 318 20 mudScript setCycle: Walk) 		
			)
			(12
				(PlayerControl)
				;(= gDisableSwitch 0)
				(gRoom newRoom: 269)	
			)
			
		)
	)
)
(instance throwScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0
			)
			(1
				; lifting metal frame over head
				(PrintOther 268 7)
				(ProgramControl)
				(gEgo hide:)
				(alterEgo show: posn: (gEgo x?)(gEgo y?) view: 453 loop: 5 cel: 0 setCycle: End self cycleSpeed: 2)	
			)
			(2 (= cycles 8)
			)
			(3
				; Prepping a throw
				(alterEgo loop: 4 cel: 0 setCycle: End self)	
			)
			
			(4 (= cycles 10)
			)
			(5
				(alterEgo loop: 3 setCycle: CT)
				(metalWalkway show: posn: (alterEgo x?) (- (alterEgo y?) 50) xStep: 5 setMotion: MoveTo 278 60 self)
			)
			(6
				(gEgo show: posn: (alterEgo x?)(alterEgo y?) loop: 2 put: 4 268)
				(alterEgo hide:)
				(PlayerControl)	
				(gGame changeScore: 1)	
				;(PrintOther 268 7)
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
	(if (> (gEgo y?) 120)
		(Print textRes textResIndex	#width 280 #at -1 10)
	else
		(Print textRes textResIndex	#width 280 #at -1 140)
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
(instance metalWalkway of Act
	(properties
		y 60
		x 278
		view 455
		loop 6
	)
)