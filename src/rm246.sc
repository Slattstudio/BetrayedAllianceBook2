;;; Sierra Script 1.0 - (do not remove this comment)
; score + 3
(script# 246)
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
	
	rm246 0
	
)

(local
	
	jurgenRandom = 0
	enemyRandom = 0
	[odds 4] = [1 3 5 7]
	[evens 4] = [0 2 4 6]
	knockDown = 0
	
	message = 0
	
	runningOnMud = 0
	
	conversationMode = 0 ; used when characters are talking in the handleEvent Method
	messagePrint = 0
	
	moveAction = 2
	cutsceneWait = 0
	
)


(instance rm246 of Rm
	(properties
		picture scriptNumber
		north 0
		east 247
		south 244
		west 266
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript setRegions: 200 setRegions: 205)
		
		(if gKneeHealed
			(if (not gSeparated)
				(if (== g246KnockedDown 2)
					(leahProp init: ignoreControl: ctlWHITE ignoreActors:) 
				)
			)
		)
		
		(switch gPreviousRoomNumber
			(100
				;(gEgo posn: 150 150 loop: 3)
				(PlaceEgo 150 150 3)
				(leah posn: 160 170 loop: 3)
				(= conversationMode 1)
				(TheMenuBar state: DISABLED)	
			)
			(244 
				;(gEgo posn: 150 170 loop: 3)
				(PlaceEgo 150 170 3)
				;(if (== g246KnockedDown 2)
					(leahProp posn: 170 165 view: 343 setCycle: Walk setMotion: MoveTo 160 80 leahScript)
				;)
			)
			(247 
				;(gEgo posn: 300 100 loop: 1)
				(PlaceEgo 300 100 1)
				;(if (== g246KnockedDown 2)
					(leahProp posn: 290 90 view: 343 setCycle: Walk setMotion: MoveTo 160 80 leahScript)
				;)
			)
			(266 
				;(gEgo posn: 20 100 loop: 0)
				(PlaceEgo 20 100 0)
				;(if (== g246KnockedDown 2)
					(leahProp posn: 30 90 view: 343 setCycle: Walk setMotion: MoveTo 160 80 leahScript)
				;)
			)
			(else 
				;(gEgo posn: 150 170 loop: 3)
				(= g246KnockedDown 1)
				;(gEgo posn: 150 150 loop: 3)
				(PlaceEgo 150 150 3)
				(leah posn: 160 170 loop: 3)
				(= conversationMode 1)	
				(TheMenuBar state: DISABLED)
			)
		)
		(SetUpEgo)
		(gEgo init: setScript: cutsceneWaitScript)
		(RunningCheck)
		
		(jurgen init: setScript: fightScript )
		(enemy init: ignoreActors: setScript: turnBackScript)
		(brokenStick init: hide: ignoreActors: setScript: leahScript)
		(leah init: setCycle: Walk ignoreActors: ignoreControl: ctlWHITE setScript: leahEnterScript)
		(actionEgo init: hide: ignoreActors: ignoreControl: ctlWHITE setScript: convoScript)
		
		
		(if (not g246KnockedDown)
			(fightScript changeState: 1)
		else
			(if (and (== g246KnockedDown 1) (not g266paper))
				(enemy view: 35 loop: 4 cel: 0 setCycle: End cycleSpeed: 2)
				(RoomScript changeState: 1)
			else
				(jurgen hide:)
				(enemy hide:)
				(leah hide:)
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
			(gEgo posn: (gEgo x?) (+ (gEgo y?) (gEgo yStep?)))
		)
		(if (& (gEgo onControl:) ctlSILVER)
			(if gInDorm
				(turnBackScript changeState: 1)	
			)
		)
		(if (& (gEgo onControl:) ctlGREY)
			(if gInDorm
				(turnBackScript changeState: 4)	
			)
		)
		(if (& (gEgo onControl:) ctlRED)
			(if (not runningOnMud)
				(= runningOnMud 1)
				(turnBackScript changeState: 7)	
			)
		)
	)
	
	(method (handleEvent pEvent button)
		(super handleEvent: pEvent)
		
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if (& (pEvent modifiers?) emRIGHT_BUTTON)
				(if
					(checkEvent pEvent (leahProp nsLeft?) (leahProp nsRight?) (leahProp nsTop?) (leahProp nsBottom?))
					(if gKneeHealed
						(if (not gSeparated)
							(PrintOther 200 29)	
						)
					)
				else
					(if (or (== (OnControl ocPRIORITY (pEvent x?) (pEvent y?)) ctlMAROON) ; rocks
							(== (OnControl ocPRIORITY (pEvent x?) (pEvent y?)) ctlGREEN)
							(== (OnControl ocPRIORITY (pEvent x?) (pEvent y?)) ctlRED))
							(PrintOther 246 4)					
					)
					(if (== (OnControl ocSPECIAL (pEvent x?) (pEvent y?)) ctlTEAL) ; mud
						(PrintOther 246 2)		
					)
				)
				;(if (== (OnControl ocPRIORITY (pEvent x?) (pEvent y?)) ctlTEAL) ; tree
					
				;)					
			)
		)
		
		(if (or (== (pEvent message?) KEY_RETURN) (if (== (pEvent message?) 3) )) ; pressed right arrow
			(if message
				(if (not cutsceneWait)
					(if gPrintDlg
						(gPrintDlg dispose:)
						(= message 0)						
						;(convoScript cycles: 0 cue:)
						(if (< messagePrint moveAction)
							(convoScript cue:)	
						)
							
						(dialogTrack)
						;(FormatPrint "moveAction %u \n\nmessagePrint %u" moveAction messagePrint)
						
						(if (== moveAction messagePrint)
							(= cutsceneWait 1)
							(RoomScript cue:)
							(++ moveAction)	
							
						)
					)
				)
			)
		)
		(if (== (pEvent type?) evKEYBOARD)
			(if (== (pEvent message?) KEY_ESCAPE)
				(if conversationMode
					(= gWndColor 0)
					(= gWndBack 14)
					(= button (Print 997 11 #button { Yes_} 1 #button { No_} 0 #font 4 #at -1 10))
					(= gWndColor 0)
					(= gWndBack 15)
					
					(switch button
						(0
						;(self cue:)
						)
						(1 
							(if gPrintDlg
								(gPrintDlg dispose:)	
							)
							(gEgo posn: 110 120 loop: 0)
							(jurgen hide:)
							(RoomScript cycles: 0 changeState: 25)
							(fightScript changeState: 19)
						)
					)
				)
			)
		)
	;	(if (== (pEvent message?) 7)  ; pressed left arrow
	;		(if (> messagePrint 2)
	;			(if message
	;				(if gPrintDlg
	;					(gPrintDlg dispose:)
	;					(= message 0)
	;					;(buttonInstructions hide:)
	;					(-- messagePrint)						
	;					(RoomScript cycles: 0 changeState: messagePrint)
	;					(dialogTrack)
	;				)
	;			)
	;		)
	;	)
		(if (== (pEvent message?) 7)  ; pressed left arrow
			(if (> messagePrint 1)
				(if message
					(if gPrintDlg
						(gPrintDlg dispose:)
						(= message 0)			
						(-- messagePrint)						
						(convoScript cycles: 0 changeState: messagePrint)
						(dialogTrack)
					)
				)
			)
		)
		; handle Said's, etc...
		(if (Said 'talk/woman,leah')	
			(if (and (not gSeparated) gKneeHealed)
				(PrintLeah 246 5)
			else
				(PrintCantDoThat)
			)
		)
		
		(if (Said 'look>')
			(if (Said '/mud,hill')
				(PrintOther 246 2)
			)
			(if (Said '/monster,creature')
				(PrintOther 246 3)
			)
			(if (Said '/rock')
				(PrintOther 246 4)
			)
			(if (Said '/forest,verlorn')
				(PrintOther 0 105)	
			)
			(if (Said '[/!*]')
				(PrintOther 246 1)
			)
		)
	)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0 ; Handle state changes
			)
			(1 (= cycles 10)
				(ProgramControl)
				(jurgen loop: 11 cel: 0 setCycle: Fwd cycleSpeed: 6)
				(fightScript changeState: 15)
				(leahEnterScript changeState: 5)
				(gTheMusic fade:)
					
			)
			(2
				(jurgen loop: 12 cel: 0 setCycle: End self  cycleSpeed: 4)	
			)
			(3
				(jurgen view: 321 setCycle: Walk setMotion: MoveTo (jurgen x?) (+ 5 (jurgen y?)) self )	
			)
			(4
				(jurgen loop: 1)
				(convoScript cue:)
				;(PrintJurgen 246 15)	; Well done! You're quite skilled with that sword, but it won't save you from all dangers.
				(jurgen view: 250 loop: 5 cel: 0 setCycle: End cycleSpeed: 3)
			)
			(5
				;(PrintLeah 246 16)	; That's an odd way to say thank you.
				(leah view: 373 loop: 0 cel: 0 setCycle: End cycleSpeed: 3)
				(cutsceneWaitScript cue:)
			)
			(6	
				;(PrintJurgen 246 17)	;Yes, well...I don't mean to be rude, only to warn you of the dangers in this place.
				(cutsceneWaitScript cue:)
				(jurgen view: 250 loop: 5 cel: 0 setCycle: End cycleSpeed: 3)
			)
			(7
				;(PrintJurgen 246 18)	; No doubt you saw my brother just south of here, turned to stone by that wretched enchantress.
				(cutsceneWaitScript cue:)
				(jurgen view: 250 loop: 5 cel: 0 setCycle: End cycleSpeed: 3)
			)
			(8
				;(PrintMan 246 19)	; So that wasn't a mere statue after all?
				(actionEgo view: 251 loop: 0 cel: 0 setCycle: End cycleSpeed: 3)
				(cutsceneWaitScript cue:)
			)
			(9
				;(PrintJurgen 246 20)	;No, and you could be next if you're not careful.
				(cutsceneWaitScript cue:)
				(jurgen view: 250 loop: 5 cel: 0 setCycle: End cycleSpeed: 3)
			)
			(10
				;(PrintLeah 246 21)	; Then why are you out here? Aren't you in the same danger?
				(leah view: 373 loop: 0 cel: 0 setCycle: End cycleSpeed: 3)
				(cutsceneWaitScript cue:)
			)
			(11
				(cutsceneWaitScript cue:)
				(jurgen view: 250 loop: 5 cel: 0 setCycle: End cycleSpeed: 3)
				;(PrintJurgen 246 22)	; No, I'm ritually protected by a charm blessed by the winded waters. We were trying to find the waters when she found us first.
			)
			(12
				(cutsceneWaitScript cue:)
				(actionEgo view: 251 loop: 0 cel: 0 setCycle: End cycleSpeed: 3)
				;(PrintMan 246 23)	; And she turned him to stone? Why?
			)
			(13
				(cutsceneWaitScript cue:)
				(jurgen view: 250 loop: 5 cel: 0 setCycle: End cycleSpeed: 3)
				;(PrintJurgen 246 24)	; She's cursed this forest. That's why my brother and I are here. To lift the curse and bring peace to this once-sacred land.
			)
			(14
				(cutsceneWaitScript cue:)
				(jurgen view: 250 loop: 5 cel: 0 setCycle: End cycleSpeed: 3)
				;(PrintJurgen 246 25)	; I hope this will save my brother, also.
			)
			(15
				(cutsceneWaitScript cue:)
				(leah view: 373 loop: 0 cel: 0 setCycle: End cycleSpeed: 3)
				;(PrintLeah 246 26)	; Can you help us find the winded waters, so we can be protected too?
			)
			(16
				(cutsceneWaitScript cue:)
				(jurgen view: 250 loop: 5 cel: 0 setCycle: End cycleSpeed: 3)
				;(PrintJurgen 246 34)	; Yes, justice would...
			)
			(17
				(cutsceneWaitScript cue:)
				(jurgen view: 250 loop: 5 cel: 0 setCycle: End cycleSpeed: 3)
				;(PrintJurgen 246 27)	;There is a ruin east of here that held a trial for those who used to live here. From my study, it was said those who succeeded were granted access.
			)
			(18
				;(PrintJurgen 246 28)	; I don't know how to initiate the trial myself, but here, take this note. It's part of a journal that my brother and I had been pursuing...
				
				(jurgen view: 321 setCycle: Walk cycleSpeed: -1 setMotion: MoveTo (+ (gEgo x?) 24) (jurgen y?) cutsceneWaitScript) ; walk over
			)
			(19
				;(PrintJurgen 246 29)	;But it's of no use to me now.
				
				(jurgen view: 250 posn: (- (jurgen x?) 4) (jurgen y?) loop: 8 cel: 0 setCycle: End cycleSpeed: 3) ; hand note
				(actionEgo view: 251 loop: 5 cel: 0 setCycle: End cutsceneWaitScript cycleSpeed: 3) ; reach for note
			)
			(20
				;(PrintLeah 246 30)	; Thanks, um...I'm Leah von Spier, what's your name?
				(jurgen view: 250 loop: 9 cel: 0 setCycle: End cycleSpeed: 3)	; hand back
				(actionEgo view: 251 loop: 6 cel: 0 setCycle: End cutsceneWaitScript cycleSpeed: 3) ;take paper
			)
			(21
				;(PrintJurgen 246 31)	;Jurgen. Be on your guard. The enchantress has eyes in the trees. A black bird seems to be a confidant of hers.
				(jurgen view: 321 posn: (+ (jurgen x?) 4)(jurgen y?)  setCycle: Walk cycleSpeed: -1  setMotion: MoveTo 300 (jurgen y?) cutsceneWaitScript) ; begin walking away
			)
			(22
				(cutsceneWaitScript cue:)
				;(PrintMan 246 32)	;Must you be on your way? Why not join us.
			)
			(23
				(cutsceneWaitScript cue:)
				;(PrintJurgen 246 33)	; You have my pledge of gratitude for helping me, but it's easier to track a group of three. Now that Rowan is stone, I best become unseen.
				(jurgen loop: 1) ; to stop him from "floating" if you move to this phase while he walks awau.
			)
			(24 
				(gEgo show: loop: 0)
				(actionEgo hide:)
				(fightScript changeState: 17)
								
			
			)
		)
	)
)
(instance turnBackScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0
			)
			(1
				(ProgramControl)
				(gEgo setMotion: MoveTo (- (gEgo x?) 20) (gEgo y?) self)	
			)
			(2 (= cycles 2)
				
				(PlayerControl)		
			)
			(3
				(PrintOther 246 0)	
			)
			(4
				(ProgramControl)
				(gEgo setMotion: MoveTo (gEgo x?) (- (gEgo y?) 10) self)	
			)
			(5	(= cycles 2)
				
				(PlayerControl)	
			)
			(6
				(PrintOther 246 0)		
			)
			(7	(= cycles 20) ;trying and failing to run up the mud
				(ProgramControl)
				(gEgo hide:)
				(if gAnotherEgo
					(actionEgo show: view: 351 loop: 3 posn: (gEgo x?)(gEgo y?) setCycle: Fwd cycleSpeed: 0)		
				else
					(actionEgo show: view: 230 loop: 3 posn: (gEgo x?)(gEgo y?) setCycle: Fwd cycleSpeed: 0)
				)				
			)
			(8
				(= gEgoMovementType 0)
				(RunningCheck)
				(actionEgo hide:)	
				(gEgo show: setMotion: MoveTo (gEgo x?)(+ (gEgo y?) 14) self )
			)
			(9	(= cycles 2)
				(PlayerControl)
				(= runningOnMud 0)
			)
			(10
				(PrintOther 246 6)	
			)
		)
	)
)
(instance leahEnterScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0
				
			)
			(1	(= cycles 1)
				(leah loop: 1)
				
			)
			(2
				(PrintLeah 246 10)
			)
			(3
				(leah setMotion: MoveTo (+ (gEgo x?) 5) (+ (gEgo y?) 20) self)
			)
			(4
				(leah loop: 3)	
			)
			(5
				(leah setMotion: MoveTo 120 137 self)		
			)
			(6
				(leah loop: 0)	
			)
		)		
	)
)

(instance fightScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0
				
			)
			(1
				(ProgramControl)
				(gTheMusic fade:)
				
				(gEgo setMotion: MoveTo 150 150)
				(leah setMotion: MoveTo 200 160)
				(jurgen setCycle: End self cycleSpeed: 4)
				(enemy setCycle: End cycleSpeed: 4)
			)
			(2
				; (if(switchSides)
				(= enemyRandom (Random 0 3))

				(enemy loop: [evens enemyRandom] cel: 0 setCycle: End)
				(= jurgenRandom (Random 0 3))
				(jurgen
					loop: [odds jurgenRandom]
					cel: 0
					setCycle: End self
				)
			)
			(3
				(= enemyRandom (Random 0 3))
				
				(enemy loop: [evens enemyRandom] cel: 0 setCycle: End)
				(= jurgenRandom (Random 0 3))
				(jurgen loop: [odds jurgenRandom] cel: 0 setCycle: End self )
			)
			(4
				(if (< knockDown 1)
					(self changeState: 2)
					(++ knockDown)
				else
					(self changeState: 5)	
				)
			)
			(5
				(enemy loop: 8 setCycle: End self)
				(jurgen loop: [odds jurgenRandom] cel: 0 setCycle: End)
			)
			(6
				(enemy loop: 9 setCycle: End )
				(jurgen loop: 10 setCycle: End self)
			)
			(7	(= cycles 10) ; Break staff on Jurgen
				(enemy view: 35 loop: 0 cel: 0 )
				(jurgen loop: 11 cel: 0 setCycle: Fwd cycleSpeed: 6)
				(brokenStick show:)	
			)
			(8
				(enemy view: 35 loop: 0 cel: 0 setCycle: End self cycleSpeed: 9)	; pull out knife	
			)
			(9
				(enemy view: 37 setCycle: Walk setMotion: MoveTo (+ 10 (enemy x?)) (enemy y?) self)	 ; walked toward Jurgen
			)
			(10 (= cycles 10)
				(leahEnterScript changeState: 1)	; leah responds to danger to Jurgen
				
			)
			(11 (= cycles 10)
				;(enemy view: 35 loop: 3 cel: 0 setCycle: End self)
				(PrintMan 246 11)
				(leahEnterScript changeState: 3)	
				(gEgo setMotion: MoveTo (gEgo x?) (- (gEgo y?) 10))
			)
			(12
				(enemy setMotion: MoveTo 157 130 self)
				
			)
			(13 (= cycles 10)
				(PrintJurgen 246 12)
				(PrintJurgen 246 13)
				; instructions
				(if gYellowTips
					(= gWndColor 0)
					(= gWndBack 14)
					(Print 246 14 #font 4 #at -1 30 #width 120 #button "Ok")	
					(= gWndColor 0)
					(= gWndBack 15)
				)
				
			)
			(14
				(= g246KnockedDown 1)
				(gRoom newRoom: 100)	
			)
			(15
				(gEgo setMotion: MoveTo 110 120 self) ; move Ego to talking position
			)
			(16
				(gEgo hide:)
				(actionEgo show: view: 251 posn: (gEgo x?)(gEgo y?))
			)
			(17
				(jurgen view: 321 posn: (+ (jurgen x?) 4)(jurgen y?)  setCycle: Walk cycleSpeed: -1  setMotion: MoveTo 320 (jurgen y?) self)	
			)
			(18	(= cycles 20)
				(jurgen hide:)
			)
			(19
				(Print 0 63 #font 4 #width 120 #at 160 -1)
				(= [gNotes 0] 1)
				(PlayerControl)	
				(TheMenuBar state: ENABLED)
				
				(= g246KnockedDown 2)
				(= conversationMode 0)
				;(gEgo get: 13)
				
				(gEgo show: loop: 0 get: 13)
				(actionEgo hide:)
				
				(leah hide:)
				(leahProp init: show: posn: (leah x?)(leah y?) view: 343 setCycle: Walk setMotion: MoveTo 160 80 leahScript ignoreControl: ctlWHITE ignoreActors:)
				
				(gGame changeScore: 3)
				(gTheMusic number: 10 loop: -1 priority: -1 play:)
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

(instance convoScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0
			)
			(1
				(PrintJurgen 246 15)	; Well done! You're quite skilled with that sword, but it won't save you from all dangers.
			)
			(2
				(PrintLeah 246 16)	; That's an odd way to say thank you.
			)
			(3
				(PrintJurgen 246 17)	;Yes, well...I don't mean to be rude, only to warn you of the dangers in this place.
			)
			(4
				(PrintJurgen 246 18)	; No doubt you saw my brother just south of here, turned to stone by that wretched enchantress.
			)
			(5
				(PrintMan 246 19)	; So that wasn't a mere statue after all?
			)
			(6
				(PrintJurgen 246 20)	;No, and you could be next if you're not careful.
			)
			(7
				(PrintLeah 246 21)	; Then why are you out here? Aren't you in the same danger?
			)
			(8
				(PrintJurgen 246 22)	; No, I'm ritually protected by a charm blessed by the winded waters. We were trying to find the waters when she found us first.
			)
			(9
				(PrintMan 246 23)	; And she turned him to stone? Why?
			)
			(10
				(PrintJurgen 246 24)	; She's cursed this forest. That's why my brother and I are here. To lift the curse and bring peace to this once-sacred land.
			)
			(11
				(PrintJurgen 246 25)	; I hope this will save my brother, also.
			)
			(12
				(PrintLeah 246 26)	; Can you help us find the winded waters, so we can be protected too?
			)
			(13
				(PrintJurgen 246 34)	; Yes, justice would...
			)
			(14
				(PrintJurgen 246 27)	;There is a ruin east of here that held a trial for those who used to live here. From my study, it was said those who succeeded were granted access.
			)
			(15
				(PrintJurgen 246 28)
			)
			(16
				(PrintJurgen 246 29)	;But it's of no use to me now.
			)
			(17
				(PrintLeah 246 30)	; Thanks, um...I'm Leah von Spier, what's your name?
			)
			(18
				(PrintJurgen 246 31)	;Jurgen. Be on your guard. The enchantress has eyes in the trees. A black bird seems to be a confidant of hers.
			)
			(19
				(PrintMan 246 32)	;Must you be on your way? Why not join us.
			)
			(20
				(PrintJurgen 246 33)
			)
		)
	)
)

(procedure (dialogTrack)
	(= messagePrint (convoScript state?))
)

(instance cutsceneWaitScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0
			)
			(1 
				(= cutsceneWait 0)
			)
			(2
				(self changeState: 1)	
			)
		)
	)
)

(procedure (PrintOther textRes textResIndex)
	
	(if conversationMode
		(Print textRes textResIndex		
			#width 280
			#at -1 10	
			#dispose
		)
		(= message 1)
	else
		(if (> (gEgo y?) 120)
			(Print textRes textResIndex	#width 280 #at -1 10)
		else
			(Print textRes textResIndex	#width 280 #at -1 140)
		)
	)
)
(procedure (PrintMan textRes textResIndex)
	(= gWndColor 7)
	(= gWndBack 1)
	(if conversationMode
		(Print textRes textResIndex		
			#width 160
			#at 40 20	
			#dispose
			#title "You say:"
		)
		(= message 1)
	;	(actionEgo view: 251 loop: 0 cel: 0 setCycle: End cycleSpeed: 3)
	else
		(Print textRes textResIndex
			#width 160
			#at 60 10
			#title "You say:"		
		)
	)
	
	(= gWndColor 0)
	(= gWndBack 15)
	;(= message 1)
)
(procedure (PrintJurgen textRes textResIndex)
	(= gWndColor 0)
	(= gWndBack 9)
	(if conversationMode
		(Print textRes textResIndex		
			#width 160
			#at 160 140	
			#title "Unknown man:"
			#dispose
		)
		(= message 1)
		;(jurgen view: 250 loop: 5 cel: 0 setCycle: End cycleSpeed: 3)
	else
		(Print textRes textResIndex
		#width 160
		#at 160 10
		#title "Unknown man:"
		
		)
	)
	(= gWndColor 0)
	(= gWndBack 15)
	;(= message 1)
)
(procedure (PrintLeah textRes textResIndex)
	(= gWndColor 14)
	(= gWndBack 5)
	(if conversationMode
		(Print textRes textResIndex		
			#width 160
			#at 40 140	
			#title "Leah"
			#dispose
		)
		(= message 1)
		;(leah view: 373 loop: 0 cel: 0 setCycle: End cycleSpeed: 3)
	else
		(Print textRes textResIndex
		#width 160
		#at 160 10
		#title "Leah:"
		)
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

(instance actionEgo of Act
	(properties
		y 1
		x 1
		view 351		
	)
)
(instance leah of Act
	(properties
		y 180
		x 200
		view 343		
	)
)
(instance leahProp of Act
	(properties
		y 150
		x 160
		view 1
	)
)
(instance jurgen of Act
	(properties
		y 110
		x 180
		view 324
		loop 1
		
	)
)
(instance brokenStick of Prop
	(properties
		y 111
		x 158
		view 306
		loop 10
		
	)
)
(instance enemy of Act
	(properties
		y 110
		x 130
		view 306
	)
)