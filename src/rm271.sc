;;; Sierra Script 1.0 - (do not remove this comment)
; Score + 1
(script# 271)
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
	
	rm271 0
	
)
(local
	
	dogMode = 0 ; 0 still 2 walking, 1 running, 3 chasing tail
	lastMode
	randogX = 0 ; number used for randomizing where dog go on X
	randogY = 0 ; number used for randomizing where dog go on Y
	
	caught = 0	; if 2, stick has been thrown
	
	inTheClear = 0
	
	chasingStick = 0
	stickVisible = 0	; if left the room after throwing without completion, disallows deadman walking
	;dogReturning = 0
	
	phaseTwo = 0	; behind the second rock with bird watching you
	phaseThree = 0	; Verlorn and Jurgen fighting
	
	; Used for fighting stances
	jurgenRandom = 0
	enemyRandom = 0
	[odds 5] = [1 3 5 7 9]
	[evens 5] = [0 2 4 6 8]
	
	myEvent
	
	dogPet = 0 ; are you trying to pet the dog?
	
	deathMessage = 0	; 1 = throwing stick and getting caught
	
)

(instance rm271 of Rm
	(properties
		picture scriptNumber
		north 0
		east 247
		south 0
		west 269
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript setRegions: 205)
		(switch gPreviousRoomNumber
			(269
				(if (> (gEgo y?) 150)   ; on bridge
					(PlaceEgo 15 170 0)
				;	(gEgo posn: 15 170 loop: 0)
				else
					(PlaceEgo 15 130 0)
				;	(gEgo posn: 15 130 loop: 0)
				)
				;(gEgo posn: 15 170 loop: 0)
			)
			(else 
				(PlaceEgo 15 170 0)
				;(gEgo posn: 15 170 loop: 0)
			)
		)
		(SetUpEgo)
		(gEgo init:)
		
		(verlorn init: ignoreActors: setScript: eatScript)
		(dog init: ignoreActors: setScript: dogScript ignoreControl: ctlWHITE)
		(bone init: hide: yStep: 3 setCycle: Walk ignoreActors: ignoreControl: ctlWHITE)
		(stick init: hide: yStep: 5 xStep: 6 setCycle: Walk ignoreActors: ignoreControl: ctlWHITE setScript: stickScript)
		(actionEgo init: hide: ignoreActors: ignoreControl: ctlWHITE)
		(verlornActor init: hide: ignoreActors: ignoreControl: ctlWHITE setCycle: Walk)
		(bird init: hide: setScript: birdScript ignoreActors: ignoreControl: ctlWHITE xStep: 4 yStep: 3)
		(squawk init: hide: ignoreActors:)
		(wanderer init: hide: ignoreControl: ctlWHITE ignoreActors: setCycle: Walk)
		(dagger init: hide: ignoreControl: ctlWHITE ignoreActors: setCycle: Walk)
		
		(dogScript changeState: 1)
		
		(if (and (== (IsOwnedBy 7 271) TRUE) (not g271Solved))
			(stick show: posn: 40 176)
			(= stickVisible 1)	
		)
	)
)

(instance RoomScript of Script
	(properties)
	
	(method (doit)
		(super doit:)
		; code executed each game cycle
		(if (& (gEgo onControl:) ctlGREEN)
			(= gDisableSwitch 0)
			(gRoom newRoom: 247)
		)
		
		(if (& (gEgo onControl:) ctlTEAL)
			(if chasingStick
				;(if (not dogReturning)
					(dogScript changeState: 15)	
				;)
			)
			(if phaseTwo
				(birdScript changeState: 4)
				(= phaseTwo 0)	
			)
			(if (== phaseThree 1)
				(self changeState: 10)
				(= phaseThree 2)	
			)
		)
		(if (& (gEgo onControl:) ctlSILVER)
			(if phaseTwo
				(birdScript changeState: 4)
				(= phaseTwo 0)	
			)		
		)
		(if (& (gEgo onControl:) ctlMAROON)
			(if (not caught)
				(if (not inTheClear)
					(eatScript cycles: 0)
					(eatScript changeState: 13)
					(= caught 1)
					(ProgramControl)
				)	
			)		
		)
	)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if (& (pEvent modifiers?) emRIGHT_BUTTON)
				(if	; stick
					(checkEvent pEvent
						(stick nsLeft?)
						(stick nsRight?)
						(stick nsTop?)
						(stick nsBottom?)
					)
					(if stickVisible
						(PrintOther 271 16)
					)
				)
				(if	; bird
					(checkEvent pEvent
						(bird nsLeft?)
						(bird nsRight?)
						(bird nsTop?)
						(bird nsBottom?)
					)
					(PrintOther 271 11)
				else
					(if (== (OnControl ocPRIORITY (pEvent x?) (pEvent y?)) ctlCYAN)
						(PrintOther 271 0)
					)
				)
				(if	; dog
					(checkEvent pEvent
						(dog nsLeft?)
						(dog nsRight?)
						(dog nsTop?)
						(dog nsBottom?)
					)
					(PrintOther 271 1)
				)
				
				(if	; verlorn
					(checkEvent pEvent
						(verlorn nsLeft?)
						(verlorn nsRight?)
						(verlorn nsTop?)
						(verlorn nsBottom?)
					)
					(PrintOther 271 2)
					(PrintOther 271 3)
				)					
			)
		)
		
		
		
		; handle Said's, etc...
		(if (Said 'look>')
			(if (Said '/creature,monster,goblin')
				(PrintOther 271 2)
				(PrintOther 271 3)
			)
			(if (Said '/dog')
				(PrintOther 271 1)
			)
			(if (Said '/bone')
				(PrintOther 271 1)
			)
			(if (Said '/stick')
				(if stickVisible
					(PrintOther 271 16)
				else
					(PrintOther 271 17)
				)
			)
			(if (Said '[/!*]')
				(PrintOther 271 21)
			)
		)
		
		(if (Said 'take,(pick<up)/stick')
			(if stickVisible
				(RoomScript changeState: 1)
			else
				(PrintOther 271 5)
			)	
		)
		(if (or (Said 'throw/stick') (Said 'play/fetch'))
			(if (gEgo has: 7) ; stick
				(if (& (gEgo onControl:) ctlFUCHSIA)
					(PrintOK)
					(stickScript changeState: 1)
				else
					(if (< (gEgo y?) 150)
						(ProgramControl)
						(PrintOK)	
						(gEgo setMotion: MoveTo 73 137)
						(= deathMessage 1)
					else
						(PrintOther 271 4)
					)
				)	
			else
				(PrintDontHaveIt)
			)	
		)
		(if (Said 'play/dog')
			(PrintOther 271 22)
		)
		(if (or (Said 'talk/man,jurgen')
				(Said 'ask<about/*'))
			(if phaseThree
				(PrintWanderer 271 24)
			else
				(PrintOther 271 25)
			)
		)
		(if (Said 'pet/dog')
			(if chasingStick
				(PrintCantDoThat)	
			else
				(if (or phaseTwo phaseThree)
					(PrintOther 271 19)	
				else
					(PrintOK)
					(ProgramControl)
					(gEgo setMotion: MoveTo 120 (gEgo y?))
					(= dogPet 1)
				)
			)			
		)
	)
	
	(method (changeState newState dyingScript)
		(= state newState)
		(switch state
			(0 ; Handle state changes
			)
			(1	; Getting the Stick
				(ProgramControl)
				(gEgo setMotion: MoveTo (- (stick x?) 10)(stick y?) self)	
			)
			(2
				(gEgo hide:)
				(actionEgo show: posn: (gEgo x?) (gEgo y?) view: 450 cel: 0 setCycle: End self cycleSpeed: 3)	
			)
			(3
				(actionEgo setCycle: Beg self)
				(stick hide:)
				(PrintOther 271 6)	
			)
			(4
				(actionEgo hide:)
				(gEgo show: get: 7)
				(= stickVisible 0)
				(PlayerControl)	
			)
				; death!
			(5	; prepare to throw dagger
				(verlornActor show: view: 37 loop: 4 posn: (verlorn x?)(- (verlorn y?) 4) setCycle: End self cycleSpeed: 3) ; moving to the ego	
				(gEgo loop: 3)	
			)
			(6	(= cycles 6)	; pause of effect
			)
			(7
				(verlornActor loop: 5)
				(dagger show: posn: (- (verlornActor x?) 10)(verlornActor y?) yStep: 8 xStep: 8 setMotion: MoveTo (+ (gEgo x?) 5) (- (gEgo y?) 22) self)	
			)
			(8
				(dagger hide:)
				(actionEgo show: posn: (gEgo x?)(gEgo y?) view: 382 loop: 1 cel: 0 setCycle: End self cycleSpeed: 2)
				(gEgo hide:)	
			)
			(9
				(ShakeScreen 1)
				(if (not [gDeaths 8])
					(++ gUniqueDeaths)
				)
				(++ [gDeaths 8])
				(= gDeathIconTop 1)
				(= gDefaultFont 4)
				
				(if dogPet
					(= dyingScript (ScriptID DYING_SCRIPT))
					(dyingScript
						caller: 700
						register:
						{What a doggone shame! Your attraction to cute things led you right into the line of fire.}
					)
				else
					(if (== deathMessage 1)	; THrowing a stick
						(= dyingScript (ScriptID DYING_SCRIPT))
						(dyingScript
							caller: 700
							register:
							{Great idea! Too bad the execution was, well, an execution. Perhaps you could throw your stick from a more hidden spot.}
						)
					else
						(= dyingScript (ScriptID DYING_SCRIPT))
						(dyingScript
							caller: 700
							register:
							{Turns out your vital organs shutting down is a good distraction from the loss of blood. To avoid the same fate again maybe a good distraction could do the trick.}
						)
					)
					
				)
				(gGame setScript: dyingScript)	
			)
			(10
				(ProgramControl)
				(gEgo setMotion: MoveTo (+ (gEgo x?) 10) (gEgo y?) self)
			)
			(11
				(PlayerControl)
				(= gDisableSwitch 1)
				(Print 271 20)
				(= phaseThree 1)	
			)
		)
	)
)
(instance birdScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0
			)
			(1
				;(PrintOK)
				(bird show: setCycle: Walk cycleSpeed: 1 setMotion: MoveTo 220 80 self)
			)
			(2
				(bird view: 302 setCycle: Walk setMotion: MoveTo 204 118 self)	
			)
			(3
				(bird view: 303 setPri: 12)
				(= phaseTwo 1)
				(gGame changeScore: 1)
				(PlayerControl)
				(= gDisableSwitch 1)
				(PrintOther 271 8)		
			)
			(4 ;(= cycles 16)
				(ProgramControl)
				(bird loop: 1 setCycle: End) ;SQUACK
				(squawk show: setCycle: End self cycleSpeed: 2)
				(gEgo setMotion: MoveTo 208 168)	
			)
			(5
				(squawk hide:)
				(PrintOther 271 12) ; Uh no! The bird's alerting the creature.
				(eatScript cycles: 0)	
				(verlorn posn: (verlorn x?) (+ (verlorn y?) 30) loop: 9 cel: 0 setCycle: End self cycleSpeed: 4) ; jumping down
				(gEgo loop: 1)
				(bird view: 301 setCycle: Walk xStep: 4 yStep: 3 setMotion: MoveTo 40 -20)
				
			)
			(6
				(verlorn view: 35 loop: 3 cel: 0 setCycle: End self cycleSpeed: 2)	; pulling knife
			)
			(7
				(PrintOther 271 10)	
				(verlorn hide:)
				(verlornActor show: view: 37 posn: (verlorn x?)(- (verlorn y?) 4) setMotion: MoveTo 138 165 self setPri: -1)
				(dogScript cycles: changeState: 20)
			)
			(8	(= cycles 5)
				(verlornActor loop: 0)
				(PrintLeah 271 13)	
			)
			(9
				(verlornActor setMotion: MoveTo 150 165 self)	
			)
			(10
				(PrintWanderer	271 14)
				(wanderer show: setMotion: MoveTo 222 113 self)
				(verlornActor setMotion: MoveTo 130 154)
			)
			(11
				(wanderer setMotion: MoveTo 152 132 self)
				(verlornActor loop: 3)
				
			)
			(12	(= cycles 10)
				(wanderer setMotion: MoveTo 150 145)
				(verlornActor setMotion: MoveTo 85 146 self)
				(PrintWanderer 271 15)	
			)
			(13 (= cycles 1)
				(verlornActor hide:)
				(verlorn show: view: 307 posn: (verlornActor x?)(verlornActor y?) cycleSpeed: 3)
				(wanderer view: 324 cycleSpeed: 3)
				(PlayerControl)
				(= gDisableSwitch 1)
				(= phaseThree 1)
					
			)
			(14
				(= enemyRandom (Random 0 3))

				(verlorn loop: [evens enemyRandom] cel: 0 setCycle: End)
				(= jurgenRandom (Random 0 3))
				(wanderer
					loop: [odds jurgenRandom]
					cel: 0
					setCycle: End self
				)
			)
			(15
				(= enemyRandom (Random 0 3))
				
				(verlorn loop: [evens enemyRandom] cel: 0 setCycle: End)
				(= jurgenRandom (Random 0 3))
				(wanderer
					loop: [odds jurgenRandom]
					cel: 0
					setCycle: End self
				)
			)
			(16
				(self changeState: 14)
			)
		)
	)
)

(instance dogScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0
			)
			(1
				
				(= randogX (Random 67 152))
				(= randogY (Random 129 170))
				
				(dogRunRandomizer)	
			)
			(2
				(dogScript changeState: 1)	
			)
			(3	(= cycles (Random 40 70)) 
				;(dog setMotion: NULL view: 58 loop: (dog loop?) cel: 0 setCycle: Fwd cycleSpeed: 4)	
			)
			(4
				(dogScript changeState: 1)	
			)
			(5 (= cycles 0)
				(dog setMotion: NULL view: 58 cel: 0 setCycle: Fwd cycleSpeed: 4)
			)
			(6
				; a nothing state to catch 5 going to 6 from potential movement	
			)
			(7	; dog goes to bone
				(dog view: 57 setCycle: Walk cycleSpeed: 1 xStep: 4 setMotion: MoveTo (- (bone x?) 10) 137 self)	
			)
			(8	; pick up bone 
				(dog view: 58 loop: 3 cel: 0 setCycle: End self cycleSpeed: 3) ; dog animation getting bone
				(bone hide:)	
			)
			(9
				(dog view: 56 setCycle: Walk cycleSpeed: 1 setMotion: MoveTo 50 (dog y?) self)	; dropping off bone
			)
			(10
				(dog view: 57 setMotion: MoveTo 130 (dog y?) self)	; coming back
			)
			(11
				(dogScript changeState: 1)	
			)
			(12
				(dog view: 57 xStep: 4 setMotion: MoveTo 223 119 self) ; chasing stick
			)
			(13
				(dog posn: 237 119 view: 55 loop: 0 cel: 0 setCycle: End self cycleSpeed: 2 setPri: 7) ; jumping into bushes
				(eatScript cycles: 0)
				(verlorn loop: 8)	
			)
			(14
				(dog hide:)
				(eatScript cycles: 0 changeState: 13)	
			)
			(15
				(ProgramControl)
				(dog show: view: 55 loop: 1 cel: 0 setCycle: End self) ;dog jumping back
				(gEgo setMotion: MoveTo 188 161 ignoreControl: ctlWHITE)
				(= chasingStick 0)	
					
			)
			(16
				(dog posn: 223 119 view: 54 setCycle: Walk cycleSpeed: 1 setMotion: MoveTo 50 137 self)	
				(PrintOther 271 9)
				
			)
			(17
				(dog view: 57 setMotion: MoveTo 130 (dog y?) self)	; coming back
				(eatScript changeState: 17)
				(gEgo loop: 2)	
			)
			(18
				(dogScript changeState: 1)	
			)
			(20 ; dog runs away
				(dog view: 57 xStep: 4 setMotion: MoveTo 100 130 self)	
			)
			(21
				(dog view: 57 xStep: 4 setMotion: MoveTo 1 124 self)	
			)
			(22
				;(dog hide:)	
			)
		)
	)
)

(instance eatScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0 (= cycles 24)
				(verlorn loop: 0 cel: 0 setCycle: CT)
			)
			(1 
				(verlorn loop: 0 cel: 0 setCycle: End self cycleSpeed: 3)	
			)
			(2
				(verlorn loop: 1 cel: 0 setCycle: End self cycleSpeed: 3)	
			)
			(3 (= cycles 15)
				(verlorn loop: 2 cel: 0 setCycle: Fwd cycleSpeed: 3)	
			)
			(4
				(verlorn loop: 3 cel: 0 setCycle: End self cycleSpeed: 3)	
			)
			(5 (= cycles 24)
				
			)
			(6
				(verlorn loop: 4 cel: 0 setCycle: End self cycleSpeed: 3)			
			)
			(7
				(= cycles 3)	
			)
			(8  (= cycles 15); Throw Bone
				(verlorn loop: 5 cel: 0 setCycle: CT)	
				(bone show: posn: 150 100 setMotion: MoveTo 152 140)
				(if (not chasingStick)
					(dogScript changeState: 5)
				)
			)
			(9
				(verlorn loop: 6 cel: 0 setCycle: End self cycleSpeed: 3)
				(bone cel: 3)
				(if (not chasingStick)
					(dogScript changeState: 7)
				)	
			)
			(10
				(verlorn loop: 7 cel: 0 setCycle: End self cycleSpeed: 3)	
			)
			(11 (= cycles 24)
				
			)
			(12
				(self changeState: 0)	
			)
			(13
				(verlorn posn: (verlorn x?) (+ (verlorn y?) 30) loop: 9 cel: 0 setCycle: End self cycleSpeed: 3) ; jumping down
			)
			(14
				(verlorn hide:)
				(if caught
					(RoomScript changeState: 5)
				else
					(verlornActor show: posn: (verlorn x?)(- (verlorn y?) 4) setMotion: MoveTo 227 116 self setPri: 7) ; moving to the bushes
				)
					
			)
			(15
				(verlornActor setMotion: MoveTo 319 116 self )	; moving out of sight in the bushes
			)
			(16
				(PlayerControl)
				(= gDisableSwitch 1)
				(PrintOther 271 7)
			)
			(17
				(verlornActor show: setMotion: MoveTo 227 116 self) ; returning from bushes
				
			)
			(18
				(verlornActor setMotion: MoveTo 150 129 self) ; returning to position
			)
			(19
				;(Print "Look around?")
				(verlornActor hide:)
				(verlorn show: posn: (verlorn x?) (verlorn y?) loop: 9 cel: 6 setCycle: Beg self cycleSpeed: 2) ; jumping up
				(birdScript changeState: 1)	
				
			)
			(20
				(verlorn posn: (verlorn x?) (- (verlorn y?) 30))
				(self changeState: 9)
				(gEgo observeControl: ctlWHITE)
			)
		)
	)
)

(instance stickScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0
			)
			(1
				(ProgramControl)
				(= inTheClear 1)
				(gEgo setMotion: MoveTo 30 166 self ignoreControl: ctlWHITE)	
			)
			(2
				(gEgo hide:)
				(actionEgo show: view: 372 posn: (gEgo x?) (gEgo y?) loop: 0 cel: 0 setCycle: End self cycleSpeed: 4)	
			)
			(3
				(= chasingStick 1)
				(stick show: posn: 60 150 setMotion: MoveTo 113 113 self)	
			)
			(4
				(stick setMotion: MoveTo 162 95 self)	
			)
			(5
				(stick setMotion: MoveTo 281 113 self)
				(dogScript cycles: 0 changeState: 12)
				(actionEgo hide:)
				(gEgo show: observeControl: ctlWHITE put: 7 271)	
			)
			(6
				
			)
		)
	)
)

(procedure (dogRunRandomizer)
	(= lastMode dogMode)
	(= dogMode (Random 0 3))
	(if (== lastMode dogMode)
		(= dogMode (Random 0 3))	
	)
	(switch dogMode
		(0
			(if (> randogX (dog x?)) 
				(dog loop: 0)	
			else
				(dog loop: 1)
			)
			(dog setMotion: NULL view: 58 cel: 0 setCycle: Fwd cycleSpeed: 4)	; looking around
			(dogScript changeState: 3)
		)
		(1
			(if (> randogX (dog x?)) 
				(dog loop: 0)	
			else
				(dog loop: 1)
			)
			(dog view: 57 xStep: 4 cycleSpeed: 1)	; running
			(dog setCycle: Walk setMotion: MoveTo randogX randogY self)
		)
		(2
			(dog view: 59 xStep: 3 cycleSpeed: 1)	; walking
			(dog setCycle: Walk setMotion: MoveTo randogX randogY self)
		)
		(3
			(dog setMotion: NULL view: 58 loop: 2 cel: 0 setCycle: Fwd cycleSpeed: 2)	; circles
			(dogScript changeState: 3)	
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
(procedure (PrintLeah textRes textResIndex)
	(= gWndColor 14)
	(= gWndBack 5)
	(Print textRes textResIndex
		#width 160
		#at 120 20
		#title "Leah:"
	)
	(= gWndColor 0)
	(= gWndBack 15)
)
(procedure (PrintWanderer textRes textResIndex)
	(= gWndColor 0)
	(= gWndBack 9)
	(Print textRes textResIndex
		#width 160
		#at 120 20
		#title "Jurgen:"
	)
	(= gWndColor 0)
	(= gWndBack 15)
)

(instance verlorn of Prop
	(properties
		y 100
		x 150
		view 49
	)
)
(instance verlornActor of Act
	(properties
		y 100
		x 150
		view 38
	)
)
(instance dog of Act
	(properties
		y 140
		x 150
		view 57
	)
)
(instance bird of Act
	(properties
		y 30
		x 310
		view 301
	)
)
(instance wanderer of Act
	(properties
		y 114
		x 314
		view 321
	)
)
(instance bone of Act
	(properties
		y 100
		x 150
		view 48
	)
)
(instance stick of Act
	(properties
		y 150
		x 60
		view 47
	)
)
(instance actionEgo of Act
	(properties
		y 160
		x 40
		view 372
	)
)
(instance dagger of Act
	(properties
		y 160
		x 40
		view 79
	)
)
(instance squawk of Prop
	(properties
		y 110
		x 200
		view 978
		loop 1
	)
)