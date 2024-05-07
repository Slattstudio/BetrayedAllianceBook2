;;; Sierra Script 1.0 - (do not remove this comment)
; score +3
(script# 243)
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
(use MenuBar)

(public 
	rm243 0
)
(local

	mysteryMusic = 0
	stickyStop = 0
	myEvent 
	;entering = 0
	comeOrGo = 0 ; variable for when Ego is looking from other room to either enter the room or to go back to the vantage point room
	goingForFruit = 0
	
	[fruitStr 30]
)

(instance rm243 of Rm
	(properties
		picture scriptNumber
		north 244
		east 235
		south 33
		west 0
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript setRegions: 200 setRegions: 205) 
		
		(if gKneeHealed
			(if (not gSeparated)
				(leahProp init: setScript: leahScript ignoreControl: ctlWHITE) 
			)
		)
		
		(switch gPreviousRoomNumber
			(235
				(PlaceEgo 300 130 1)
				;(gEgo posn: 300 130 loop: 1)
				(leahProp posn: 302 120 view: 343 setCycle: Walk setMotion: MoveTo 230 90 leahScript)
			)
			(33
				;(gEgo posn: 167 170 loop: 3)
				(PlaceEgo 167 170 3)
				(leahScript cue:)
			)
			(244
				;(gEgo posn: 160 100 loop: 2)
				(PlaceEgo 160 100 2)
				(leahProp posn: 170 90 view: 343 setCycle: Walk setMotion: MoveTo 230 90 leahScript)
			)
			(else 
				;(gEgo posn: 150 100 loop: 1)
				(PlaceEgo 150 100 1)
				(leahScript cue:)
			)
		)
		(SetUpEgo)
		(gEgo init:)
		(RunningCheck)
		
		(bushes init:)
		(gate init:)
		(honeyBush init:)
		(puddle init: ignoreActors: setPri: 0)
		
		(actionEgo init: hide: ignoreActors: setScript: enterScript)
		(actionProp init: hide: ignoreActors: ignoreControl: ctlWHITE)
		
		(characterSelect init: hide: ignoreActors: cycleSpeed: 2)
		
		;(= g243GateOpen 1)
		(if g243GateOpen
			(gate cel: 2 ignoreActors:)	
		)
		
		(if g243Fruit
			(honeyBush loop: 0)	
		)
		
		(if gLookingAhead
			;(= entering 1)
			(enterBackFrame init: ignoreActors: setPri: 15)
			(enterButton init: ignoreActors: setPri: 15)
			(backButton init: ignoreActors: setPri: 15)
			
			(gEgo hide:)
			(= gMap 1)
			(= gArcStl 1)
			
		)
	)
)

(instance RoomScript of Script
	(properties)
	
	(method (doit)
		(super doit:)
		
		(if gLookingAhead
			(= myEvent (Event new: evNULL))
			(if
					(checkEvent
						myEvent
						(backButton nsLeft?)
						(backButton nsRight?)
						(+ (backButton nsTop?) 7)
						(+ (backButton nsBottom?) 7)
					)
					(= comeOrGo 2)
					(backButton cel: 1)
					(enterButton cel: 0)
					
					
			else
				(if
					(checkEvent
						myEvent
						(enterButton nsLeft?)
						(enterButton nsRight?)
						(+ (enterButton nsTop?) 7)
						(+ (enterButton nsBottom?) 7)
					)
					(= comeOrGo 1)
					(backButton cel: 0)
					(enterButton cel: 1)
				else
					(= comeOrGo 0)
					(backButton cel: 0)
					(enterButton cel: 0)
				)
			)
			(myEvent dispose:)
		)
		
		(if (& (gEgo onControl:) ctlFUCHSIA)
			(if g243GateOpen
				(gRoom newRoom: 244)	
			)
		)
		
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
		(if (& (gEgo onControl:) ctlSILVER)
			(if (not stickyStop)
				;(gEgo setMotion: NULL)
				(= stickyStop 1)
			else
				(if goingForFruit
					(gEgo xStep: 1 yStep: 1)					
					(gEgo setMotion: MoveTo 83 100 RoomScript)
				else
					(gEgo xStep: 1 yStep: 1)
					(runProc)
				)
			)	
		else
			(if stickyStop
				(RunningCheck)
				(runProc)
				(= stickyStop 0)
			)
		)
	)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		(if (== (pEvent type?) evKEYBOARD)
			(if gLookingAhead
				(if (== (pEvent message?) KEY_ESCAPE)
					(gRoom newRoom: 235)
				)
			)
		)
		
		(if gLookingAhead
			(if (== (pEvent type?) evKEYBOARD)
				(if (or (== (pEvent message?) KEY_e) (== (pEvent message?) KEY_RETURN)) ; enter
					(enterScript changeState: 1)				
					(= gLookingAhead 0)
					(= comeOrGo 0)
					(backButton hide:)
					(enterButton hide:)
					(enterBackFrame hide:)
				)
				(if (== (pEvent message?) KEY_b)	; back
					(gRoom newRoom: 235)	
				)
			)
		)
		
		(if gLookingAhead
			(if (== (pEvent type?) evJOYSTICK)
				(if (== (pEvent message?) 3) 
					(gRoom newRoom: 235)	
				)
			)
		)
		
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if (& (pEvent modifiers?) emRIGHT_BUTTON)
				(if
					(checkEvent pEvent
						(gate nsLeft?)
						(gate nsRight?)
						(gate nsTop?)
						(gate nsBottom?)
					)
					(if g243GateOpen
						;(PrintOther 243 12)
					else
						(if (< (gEgo y?) 90) ; above the gate
							(PrintOther 243 21)
						else
							(PrintOther 243 2)
						)
					)
				)
				(if
					(checkEvent pEvent
						(honeyBush nsLeft?)
						(honeyBush nsRight?)
						(honeyBush nsTop?)
						(honeyBush nsBottom?)
					)
					(PrintOther 243 0)
				else
					(if
						(checkEvent pEvent (leahProp nsLeft?) (leahProp nsRight?) (leahProp nsTop?) (leahProp nsBottom?))
						(if gKneeHealed
							(if (not gSeparated)
								(PrintOther 200 29)	
							)
						)
					else
						(if (== (OnControl ocPRIORITY (pEvent x?) (pEvent y?)) ctlPURPLE) ; wall
							(if g243GateOpen
								(PrintOther 243 22)	
							else
								(PrintOther 243 4)
								(PrintOther 243 7)		
							)
						)
					)
				)
				(if
					(checkEvent pEvent
						(puddle nsLeft?)
						(puddle nsRight?)
						(puddle nsTop?)
						(puddle nsBottom?)
					)
					(PrintOther 243 1)
				)
				
			)
			(if (== comeOrGo 1)
				(enterScript changeState: 1)
				
				(= gLookingAhead 0)
				(= comeOrGo 0)
				(backButton hide:)
				(enterButton hide:)
				(enterBackFrame hide:)
			)
			(if (== comeOrGo 2)
				(gRoom newRoom: 235)	
			)
		)
		; handle Said's, etc...
		
		;(if (Said 'up')
		;	(gRoom newRoom: 244)	
		;)
		(if (Said 'talk/woman,leah')	
			(if (and (and (not gSeparated) gKneeHealed) (not gAnotherEgo))
				(if g243GateOpen
					(PrintLeah 243 25)	
				else
					(PrintLeah 243 24)			
				)
			else
				(PrintCantDoThat)
			)
		)
		(if (Said 'talk/man')
			(if gAnotherEgo
				(PrintMan 26)
			else
				(PrintCantDoThat)
			)	
		)
		(if gAnotherEgo
			(if (Said 'ask<about/*')
				(PrintMan 31)	
			)
		)
		(if (Said 'smell')
			(PrintOther 243 23)		
		)
		(if (Said 'look>')
			(if (Said '/bush')
				(PrintOther 243 0)	
			)
			(if (Said '/wall')
				(if g243GateOpen
					(PrintOther 243 22)	
				else
					(PrintOther 243 4)
					(PrintOther 243 7)		
				)
			)
			(if (Said '/fruit')
				(if g243Fruit
					(if (gEgo has: INV_HONEY)
						(Format @fruitStr "A plump fruit filled with a sticky honey-like goo. You have %d." gHoneyNum)
						(Print @fruitStr #icon 610)
						;(Print 0 2 #icon 612)
					else
						(PrintOther 243 16)
					)		
				else
					(PrintOther 243 14)
					(PrintOther 243 15)
				)	
			)
			(if (Said '/honey,puddle')
				(PrintOther 243 1)	
			)
			(if (Said '/ground')
				(PrintOther 243 27)	
			)
			(if (Said '/gate,door')
				(if g243GateOpen
					(PrintOther 243 12)	
				else
					(if (< (gEgo y?) 90) ; above the gate
						(PrintOther 243 21)
					else
						(PrintOther 243 2)
					)
				)	
			)
			(if (Said '/latch,handle')
				(if (< (gEgo y?) 95)
					(if g243GateOpen
						(PrintOther 243 10)	
					else
						(PrintOther 243 13)
					)	
				else	
					(PrintOther 243 11)	
				)
			)
			(if (Said '/forest,verlorn')
				(PrintOther 0 105)	
			)
			(if (Said '[/!*]') ; just "look"
				(PrintOther 243 9)			
			)
		)
		(if (or (Said 'unlock,open/gate,latch') (Said 'pull,lift,raise,move/lever,latch,bar'))
			(if (< (gEgo y?) 90)
				(if (not g243GateOpen)
					(RoomScript changeState: 11)
				else
					(PrintOther 243 12)
				)
			else
				(PrintOther 243 5)
			)	
			;(PrintOK)
			;(gRoom newRoom: 244)		
		)
		(if (or (or (or (Said 'climb,(jump<over)/wall,fence') 
				(Said 'lift,help/leah, woman'))
				(Said 'give/hand, boost/leah, woman'))
				(Said 'give/leah, woman/hand, boost'))
			(if g243GateOpen
				(PrintOther 243 18)		
			else
				(if g303egoHealed
					(if (& (gEgo onControl:) ctlGREY)				
						(PrintOK)
						(RoomScript changeState: 1)	
					else	
						(PrintNotCloseEnough)	
					)
				else
				(PrintOther 243 7)					
				)
			)		
		)
		(if (Said 'climb>')
			(if (Said '[/!*]')
				(PrintOther 235 7)
			)
		)
		(if (Said 'jump/bush,wall')
			(PrintOther 243 28)	
		)
		(if(Said 'take,(pick<up),scoop/goo')
			(PrintOther 243 30)	
		)
		(if(Said 'take,(pick<up)/fruit')
			(if g243Fruit
				(Print "There's no fruit left to take.")	
			else
				(= goingForFruit 1)
				(RoomScript changeState: 16)
			)		
		)
	)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0 ; Handle state changes
			)
			(1 ; Ego Moving to support Leah's Jump
				(ProgramControl); (SetCursor 997 (HaveMouse))
				(gEgo setMotion: MoveTo 210 95 self ignoreControl: ctlWHITE)
			)
			(2	(= cycles 2)
				(gEgo loop: 2)	
			)
			(3
				; Ego positions and Leah prepares to jump
				(PrintMan 29)
				(gEgo hide:)
				; EGO
				(actionEgo show: posn: 210 95 view: 413 loop: 0 cel: 0 setCycle: End cycleSpeed: 2)
				; Leah
				(leahProp hide:)
				(actionProp show: view: 343 posn: (leahProp x?)(leahProp y?) setCycle: Walk setMotion: MoveTo 205 130 self cycleSpeed: 1)
			)
				; get into position to jump
			(4 (= cycles 12) 
				(actionProp loop: 3)	
			)
			(5
				; running to leap
				(actionProp view: 351 yStep: 3 setMotion: MoveTo 210 100 self)	
			)
			(6
				; prepping jump
				(actionProp view: 361 loop: 0 cel: 0 setCycle: End self cycleSpeed: 2)	
			)
			(7
				; jumping 
				(actionProp yStep: 4 setMotion: MoveTo 210 90 self setPri: 6)
				(actionEgo loop: 1 cel: 0 setCycle: CT setPri: 5)
					
			)
			(8
				; pulling self up
				(actionProp loop: 4 cel: 0 setCycle: End self)
				(actionEgo hide:)
				(gEgo show: loop: 2 observeControl: ctlWHITE setMotion: MoveTo (- (gEgo x?) 20) (+ (gEgo y?) 2))
			)
			(9
				(actionProp view: 361 posn: 210 45 yStep: 4 setMotion: MoveTo 210 82 self setPri: -1)
				(gEgo loop: 3)
			)
			(10
				
				(actionProp view: 343 posn: (- (actionProp x?) 5) (- (actionProp y?) 10) view: 1 loop: 2) ; setPri: 15)
				(PlayerControl)
				(= gAnotherEgo 1)
				(RunningCheck)
				(gEgo posn: (+ (actionProp x?) 4) (actionProp y?) loop: 2)
				(characterSelect show: posn: (gEgo x?) (+ (gEgo y?) 1) loop: 3 cel: 0 setCycle: End setPri: 14)	; female ego
				(actionProp view: 903 posn: 190 97 loop: 3)	; ego character
				
			)
			(11
				(ProgramControl)
				(gEgo setMotion: MoveTo 150 79 self ignoreControl: ctlWHITE)
				(enterScript changeState: 3)
				(actionProp view: 0 setCycle: Walk cycleSpeed: -1 setMotion: MoveTo 165 110 enterScript)		
			)
			(12	(= cycles 2)
				(gEgo loop: 2)
					
			)
			(13 (= cycles 10)				
				(PrintOther 243 6)
				(gate setCycle: End cycleSpeed: 2 ignoreActors:)
			)
			(14
				(gEgo setMotion: MoveTo 150 110 self)
				(actionProp view: 903 loop: 3)	
			)
			(15
				(gEgo loop: 3 observeControl: ctlWHITE)
				(= g243GateOpen 1)
				(= gAnotherEgo 0)
				(RunningCheck)
				(gEgo posn: (actionProp x?)(actionProp y?))
				(characterSelect show: posn: (gEgo x?) (+ (gEgo y?) 2) loop: 4 cel: 0 setCycle: End setPri: -1)	; male ego
				
				(actionProp view: 1 posn: 150 110)
				(PlayerControl)	
				
				(gGame changeScore: 2)
			)
			(16
				(ProgramControl)
				(gEgo setMotion: MoveTo 83 100 self ignoreControl: ctlWHITE)	; 84	
			)
			(17 (= cycles 9)
				(gEgo loop: 2)
			)
			(18
				(PrintOther 243 19)
				(PrintOther 243 20)
				(honeyBush loop: 0)
				(gEgo get: 2 observeControl: ctlWHITE)
				(= gHoneyNum (+ gHoneyNum 1))
				((gInv at: 2) count: gHoneyNum)
				
				(= g243Fruit 1)
				(= goingForFruit 0)
				(gGame changeScore: 1)
				
				(PlayerControl)
					
			)
		)
	)
)

(instance enterScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)
			(1 ; 
				(ProgramControl)
				(gEgo hide:)
				(actionEgo show: view: 343 posn: 320 130 loop: 1 cel: 0 setCycle: Walk setMotion: MoveTo 260 130 self)
			)
			(2
				(= gMap 0)
				(= gArcStl 0)
				(actionEgo hide:)
				(gEgo show: posn: 260 130 loop: 1)
				(PlayerControl)
				(TheMenuBar state: ENABLED)
			)
			(3
				
			)
			(4
				(actionProp view: 903 loop: 3)
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
		(Print textRes textResIndex
			#width 280
			#at -1 10
		)
	else
		(Print textRes textResIndex
			#width 280
			#at -1 140
		)
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
(procedure (PrintMan  textResIndex)
	(= gWndColor 7)
	(= gWndBack 1)
	(Print scriptNumber textResIndex
		#width 180
		#at -1 240
		#title "He says:"
		;#dispose		
	)
	(= gWndColor 0)
	(= gWndBack 15)
;	(= message 1)
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

(instance gate of Prop
	(properties
		y 89
		x 151
		view 12
	)
)
(instance bushes of Prop
	(properties
		y 230
		x 170
		view 803
		loop 1
	)
)
(instance honeyBush of Prop
	(properties
		y 96
		x 67
		view 63
		loop 1
	)
)
(instance leahProp of Act
	(properties
		y 90
		x 230
		view 1
	)
)
(instance puddle of Prop
	(properties
		y 105
		x 84
		view 63
		loop 2
	)
)

(instance actionEgo of Act
	(properties
		y 230
		x 170
		view 310
		loop 1
	)
)
(instance actionProp of Act
	(properties
		y 170
		x 202
		view 343
	)
)

(instance enterBackFrame of Prop
	(properties
		y 180
		x 280
		view 983
	)
)
(instance enterButton of Prop
	(properties
		y 158
		x 280
		view 983
		loop 1
	)
)
(instance backButton of Prop
	(properties
		y 178
		x 280
		view 983
		loop 2
	)
)
(instance characterSelect of Prop
	(properties
		y 29
		x 15
		view 997
		loop 3
	)
)