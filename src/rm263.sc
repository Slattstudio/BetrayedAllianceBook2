;;; Sierra Script 1.0 - (do not remove this comment)
(script# 263)
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
	rm263 0	
)

(local
	onMud = 0
	onRock = 0
	rockState = 0 ; 0 = rock on ground, 1 = rock being carried, 2 = rock in mud
)

(instance rm263 of Rm
	(properties
		picture scriptNumber
		north 264
		east 0
		south 28
		west 0
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript setRegions: 200 setRegions: 205)
		
		(if gKneeHealed
			(if (not gSeparated)
				(leahProp init: setScript: leahScript) 
			)
		)
		(switch gPreviousRoomNumber
			(28 
				;(gEgo posn: 230 184 loop: 3)
				(PlaceEgo 230 184 3)
				(leahProp posn: 260 180 view: 343 setCycle: Walk ignoreControl: ctlWHITE cycleSpeed: 0 setMotion: MoveTo 257 154 leahScript)
			)
			(else 
				;(gEgo posn: 230 184 loop: 3)
				(PlaceEgo 230 184 3)
			)
		)
		(SetUpEgo)
		(gEgo init:)
		(RunningCheck)
		
		(alterEgo init: ignoreActors: ignoreControl: ctlWHITE hide: setScript: mudScript)
		(rock init: setScript: rockScript ignoreControl: ctlWHITE xStep: 7 yStep: 3)
		(spear init: hide: setScript: jumpScript ignoreActors: setPri: 15)
		
		(if (not g210BridgeCollapse)
			(rock posn: 140 56)
			(= rockState 3)
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
				(if (== rockState 2)
					(mudScript changeState: 14)		
				else
					(if(not gAnotherEgo) 
						(mudScript changeState: 1)
					else
						(mudScript changeState: 6)	
					)
				)
			)
		)
		(if(== rockState 1)
			(gEgo observeControl: ctlMAROON)	
		else
			(gEgo ignoreControl: ctlMAROON)
		)
	)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if (& (pEvent modifiers?) emRIGHT_BUTTON)
				(if (== (OnControl ocSPECIAL (pEvent x?) (pEvent y?)) ctlGREY) ; HILL
					(PrintOther 263 8)	
				)
				(if
					(checkEvent pEvent (leahProp nsLeft?) (leahProp nsRight?) (leahProp nsTop?) (leahProp nsBottom?))
					(if gKneeHealed
						(if (not gSeparated)
							(PrintOther 200 29)
							(return)	
						)
					)
				)
				(if
					(checkEvent pEvent (rock nsLeft?) (rock nsRight?) (rock nsTop?) (rock nsBottom?))
					(switch rockState
						(0	; on ground
							(PrintOther 263 0) ;The rock has fallen from its perch. Turns out it couldn't fly.
						)
						(1	; in hand
							(PrintOther 263 1) ; "it currently looks like a rock you don't want to drop on your head."
						)
						(2	; in mud
							(PrintOther 263 2) ; "The rock is embedded firmly into the mud slope. Great throw!"
						)
						(3	; on cliff
							(PrintOther 263 3) ; "The rock sits just at the edge of the rock cliff."
						)
					)
					(return)
				)
				(if	(checkEvent pEvent 50 139 47 69)	; platform
					(PrintOther 263 6)
					(if (not g263RockState)
						;(PrintOther 263 3)
					)
					
				)
			)
		)
		
		(if(Said 'take,(pick<up)/rock')
			(switch g263RockState
				(0
					(if (< (gEgo y?) 69) ; on top
						; push rock off cliff		
					else
						(PrintOther 263 7)
					)
				)
				(1
					(if (< (gEgo y?) 69) ; on top
						(PrintOther 263 7)	
					else
						(rockScript changeState: 1) ; pick up rock
					)
				)
				(2
				
				)
			)
			;(if (== rockState 0)
			;	(if (<= (gEgo distanceTo: rock) 60)
			;		(rockScript changeState: 1)
			;	else
			;		(if (& (gEgo onControl:) ctlSILVER) ; Rock on Grass
			;			(Print "This one is stuck in the ground. You can't move it.")
			;		else
			;			(if (& (gEgo onControl:) ctlBROWN)
			;				(Print "This rock is stuck in the mud. It's good for standing on, but isn't so good for moving.")
			;			else
			;				(Print "You're not close enough to any rocks.") 
			;			)
			;		) 
			;	)
			;else
			;	(if(== rockState 1)
			;		(Print "You already have it.")
			;	)
			;	(if(== rockState 2)
			;		(Print "You can't get it from there.")
			;	)
			;	(if (== rockState 3)
			;		(if (> (gEgo y?) 90)
			;			(Print "Push it!")
			;		else
			;			(Print "You can't get it from there.")	
			;		)	
			;	)
				
		)
		; handle Said's, etc...
		
		(if (Said 'talk/woman,leah')	
			(if (and (not gSeparated) gKneeHealed)
				(PrintLeah 263 10)					
			else
				(PrintCantDoThat)
			)
		)
		
		(if (Said 'look>')
			(if (Said '/rock')
				(switch rockState
					(0	; on ground
						(PrintOther 263 0) ;The rock has fallen from its perch. Turns out it couldn't fly.
					)
					(1	; in hand
						(PrintOther 263 1) ; "it currently looks like a rock you don't want to drop on your head."
					)
					(2	; in mud
						(PrintOther 263 2) ; "The rock is embedded firmly into the mud slope. Great throw!"
					)
					(3	; on cliff
						(PrintOther 263 3) ; "The rock sits just at the edge of the rock cliff."
					)
				)
			)
			(if (Said '/cliff,platform')
				(PrintOther 263 6)	
			)
			(if (Said '/hill, mud')
				(PrintOther 263 8)	
			)
			(if (Said '/pond, water')
				(PrintOther 263 9)	
			)
			(if (Said '/forest,verlorn')
				(PrintOther 0 105)	
			)
			(if (Said '[/!*]')
				(PrintOther 263 8)
			)	
		)
		
		(if(Said 'throw,toss/rock')
			(if(== rockState 1)
				(rockScript changeState: 5)
			else
				(PrintOther 263 4) ; "You don't have a rock to throw."
			)	
		)
		(if(Said 'jump,climb')
			(if (& (gEgo onControl:) ctlBROWN)
				(PrintOther 263 5) ; "From here it's just out of reach. But perhaps if you could get just a little higher."
			else
				(if onRock
					(jumpScript changeState: 1)
				else
					(if (< (gEgo y?) 64)
						(Print "Yeah man!")
					else
						(Print "That won't work here.")
					)
				)
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

(instance rockScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)
			; Send Player to Rock
			(1
				(ProgramControl)
				(rock 
					ignoreActors:
				)
				(gEgo 
					setMotion: MoveTo (- (rock x?) 14) (rock y?) rockScript ignoreControl: ctlWHITE 
				)
			)
			; Bending over to up the Rock
			(2
				(gEgo hide:)
				(alterEgo show: posn: (gEgo x?)(gEgo y?)  view: 450 loop: 0 setCycle: End rockScript cycleSpeed: 4)	
			)
			; Picking up the Rock
			(3
				(rock
					hide:
				)
				(alterEgo posn: (+ (alterEgo x?) 2) (alterEgo y?)  loop: 2 cel: 0 setCycle: End rockScript)		
			)
			(4
				(PlayerControl)
				(= rockState 1)
				(alterEgo hide:)
				(= gEgoView 451)
				(= gEgoStoppedView 452)
				(= gMovementLocked 1)
				(= gEgoMovementType 0)
				;(RunningCheck)
				(gEgo show: loop: 0 xStep: 3 yStep: 2 posn: (- (alterEgo x?) 5)(alterEgo y?) observeControl: ctlWHITE) 
			)
			(5 ; Throwing the Rock
				(ProgramControl)
				(gEgo 
					setMotion: MoveTo 120 150 self ignoreControl: ctlWHITE 
				)
			)
			(6
				(gEgo hide:)
				(alterEgo 
					show:
					view: 453
					loop: 0
					cel: 0 
					posn: (gEgo x?)(gEgo y?) 					
					setCycle: End self
					cycleSpeed: 3
				)
			)
			(7 (= cycles 7)

			)
			(8
				(alterEgo loop: 1 cel: 0 )
				(rock show: posn: 120 110 setMotion: MoveTo 30 120 rockScript setCycle: Walk)
			)
			(9
				
				(rock loop: 2 setPri: 0)
				(= rockState 2)
				(= gEgoView 343)
				(= gEgoStoppedView 1)
				
				(alterEgo hide:)
				(gEgo
					show:
					observeControl: ctlWHITE 
				)
				(PlayerControl)
				(= gMovementLocked 0)
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
				(alterEgo show: view: 230 posn: (gEgo x?) (gEgo y?) xStep: 1 yStep: 1 cycleSpeed: 0 setMotion: MoveTo 30 100 mudScript setCycle: Walk) ; running up the hill
			)
			(2
				(alterEgo view: 428 loop: 0 cel: 0 setCycle: End cycleSpeed: 2 xStep: 6 yStep: 3 setMotion: MoveTo 91 148 mudScript) ; falling down
			)
			(3
				(alterEgo view: 409 loop: 1 cel: 0 setCycle: End mudScript) ; standing up
			)
			(4
				(gEgo show: posn: 91 148 loop: 2 setMotion: MoveTo 120 156 mudScript) ; walking away from hill
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
				(alterEgo show: view: 351 posn: (gEgo x?) (gEgo y?) xStep: 1 yStep: 1 cycleSpeed: 0 setMotion: MoveTo 30 100 mudScript setCycle: Walk) ; running up the hill
			)
			(7
				(alterEgo view: 362 posn: (- (alterEgo x?) 6) (+ (alterEgo y?) 3) loop: 1 cel: 0 cycleSpeed: 2 setCycle: End mudScript) ; tripping
			)
			(8
				(alterEgo view: 369 xStep: 6 yStep: 3 setMotion: MoveTo 91 148 mudScript) ; falling down
			)
			(9
				(alterEgo view: 362 loop: 5 cel: 0 setCycle: End mudScript) ; standing up
			)
			(10 (= cycles 15)
				(alterEgo posn: (+ (alterEgo x?) 15)(alterEgo y?) loop: 3 cel: 0 setCycle: Fwd) ; Blinking
			)
			(11
				(alterEgo loop: 4 cel: 0 setCycle: End mudScript) ; wiping off Mud
			)
			(12
				(gEgo show: posn: 94 148 loop: 2 setMotion: MoveTo 120 156 mudScript) ; walking away from hill
				(alterEgo hide:)
			)
			(13
				(PlayerControl)				
				(= onMud 0)
				(gEgo loop: 2)
			)
			(14	; Leah up to rock
				(= onMud 1)
				(ProgramControl)
				(gEgo hide:)
				(alterEgo show: view: 351 posn: (gEgo x?) (gEgo y?) xStep: 1 yStep: 1 cycleSpeed: 0 setMotion: MoveTo (rock x?) (- (rock y?) 6) mudScript setCycle: Walk) ; running up mud to rock
			)
			(15
				(alterEgo hide:)
				(gEgo show: posn: (alterEgo x?)(alterEgo y?) loop: 2)
				(= onRock 1)
				(= gMap 1)
				(PlayerControl)	
			)
			
		)
	)
)
(instance jumpScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)
			(1	; Prepping Jumping to the overHang
				(ProgramControl)
				(gEgo hide: ignoreControl: ctlWHITE)
				(alterEgo show: view: 361 loop: 0 cel: 0 setCycle: End self cycleSpeed: 2)	
			)
			(2	; jumping
				(alterEgo view: 371 yStep: 5 xStep: 5 setMotion: MoveTo 55 110 self)	
			)
			(3
				; pulling self up
				(alterEgo view: 361 loop: 4 cel: 0 setCycle: End self)	
			)
			(4
				(alterEgo hide:)
				(gEgo show: posn: (alterEgo x?)(- (alterEgo y?) 40) setMotion: MoveTo 90 56 self)
			)
			(5
				(gEgo loop: 2 observeControl: ctlWHITE)
				(PlayerControl)
				(= gMap 0)	
				(= onRock 0)
			)
		)
	)
)
(instance leahScript of Script
	(properties)
	
	(method (changeState newState rando)
		(= state newState)
		(switch state
			(1	(= cycles (Random 20 150))
				(leahProp view: 1 loop: 5 setCycle: Fwd cycleSpeed: 5)	
			)
			(2
				(leahProp loop: 6 cel: 0 setCycle: End self)	
			)
			(3	(= cycles (Random 20 150))
				(leahProp loop: 2)	
			)
			(4
				(= rando (Random 7 8))
				(leahProp loop: rando setCycle: End self)	
			)
			(5
				(leahProp setCycle: Beg self)
			)
			(6
				(self cycles: 0 changeState: (Random 1 4))	
			)
		)
	)
)

(procedure (PrintOther textRes textResIndex)
	(if (> (gEgo y?) 120)
		(Print textRes textResIndex	#width 280 #at -1 10)
	else
		(Print textRes textResIndex	#width 280 #at -1 140)
	)
)
(procedure (PrintLeah textRes textResIndex)
	(= gWndColor 14)
	(= gWndBack 5)
	(Print scriptNumber textResIndex		
		#width 280
		#at -1 10
		#title "She says:"
	)
	(= gWndColor 0)
	(= gWndBack 15)
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

(instance leahProp of Act
	(properties
		y 90
		x 90
		view 1
	)
)
(instance spear of Prop
	(properties
		y 45
		x 50
		view 3
		loop 1
	)
)
(instance rock of Act
	(properties
		y 160
		x 260
		view 4
		loop 0
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