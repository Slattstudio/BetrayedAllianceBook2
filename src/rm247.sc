;;; Sierra Script 1.0 - (do not remove this comment)
; Score + 5
(script# 247)
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
	
	rm247 0
	
)

(local
	
	moving = 0	; used to prevent game from locking when sending gEgo away from ctl Grey
	upTop = 0	; Leah on the top of the cliff	
	textPlace = 10	; y axis for where the textbox should be in relation to ego
	
	inPosition = 0 	; is Ego in catching position? otherwise death
	fallingToDeath = 0
	
	i = 0
	
	message = 0
	
	conversationMode = 0 ; used when characters are talking in the handleEvent Method
	messagePrint = 0
	
	myEvent
	
	enableSwitching = 0
	
	disableToggleAnimation = 0
	
	[characterXYToggle 3] = [0 0 0]
	; Intermediary variable allowing the change of posn before asigning new gPrevXY variables (and loop)
	itemIteration
	
)


(instance rm247 of Rm
	(properties
		picture scriptNumber
		north 261
		east 248
		south 0
		west 246
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript setRegions: 200 setRegions: 205)
		
		(if gKneeHealed
			(if (not gSeparated)
				(leahProp init: ignoreControl: ctlWHITE) 	
			)
		)
		(switcher init: hide: setScript: leahScript ignoreActors:)
		(switch gPreviousRoomNumber
			(246 
				(PlaceEgo 20 158 0)
				;(gEgo posn: 20 158 loop: 0)
				(leahProp posn: 20 148 view: 343 setCycle: Walk setMotion: MoveTo 93 132 leahScript)
			)
			(248 
				(PlaceEgo 300 158 1)
				;(gEgo posn: 300 158 loop: 1)
				(leahProp posn: 290 152 view: 343 setCycle: Walk setMotion: MoveTo 93 132 leahScript)
			)
			(261 
				(PlaceEgo 267 130 2)
				;(gEgo posn: 267 130 loop: 2)
				(leahProp posn: 93 132)
				(leahScript cue:)
			)
			(271 
				(PlaceEgo 22 31 0)
				;(gEgo posn: 20 40 loop: 0)
				(= upTop 1)
				;(= g271Solved 1)
			)
			(else
				(PlaceEgo 150 100 1)
				(if gSeparated
					(= upTop 1)	
				) 
				;(gEgo posn: 150 100 loop: 1)
			)
		)
		(SetUpEgo)
		(gEgo init: setScript: fallScript)
		(actionEgo init: hide: ignoreActors: ignoreControl: ctlWHITE setScript: liftUpScript)
		(egoProp init: hide: view: 903 loop: 3 ignoreActors: ignoreControl: ctlWHITE setScript: convoScript)
		(characterSelect init: hide: ignoreActors: cycleSpeed: 2)
		
		
		(RunningCheck)
		
		(if gSeparated
			(if (== gSwitchedRoomNumber gRoomNumber)
				(= upTop 1)
				
				(if gAnotherEgo	; if Leah contrlled
					(egoProp show: view: 903 posn: [gPrevXY 0] [gPrevXY 1] loop: 2)
				else
					(egoProp show: view: 1 posn: 69 33 loop: 2 setPri: 2)
				)	
			)
		)
	)
)

(instance RoomScript of Script
	(properties)
	
	(method (doit)
		(super doit:)
		
		(if (& (gEgo onControl:) ctlRED)
			(if (not fallingToDeath)
				(= fallingToDeath 1)
				(fallScript changeState: 1)
			)
		)
		
		(if (& (gEgo onControl:) ctlMAROON)
			(gRoom newRoom: 261)
		)
		(if (& (gEgo onControl:) ctlGREY)
			(if g271Solved
				(gRoom newRoom: 271)
			else
				(if (not moving)
					(RoomScript changeState: 1)
					(= moving 1)	
				)
			)
		)
		(if upTop
			(gEgo setPri: 2)
		else
			(gEgo setPri: -1)
		)
		; code executed each game cycle
		
		(= myEvent (Event new: evNULL))
		(if 
				(checkEvent
					myEvent
					(- (switcher nsLeft?) 5)
					(switcher nsRight?)
					(+ (switcher nsTop?) 10)
					(+ (switcher nsBottom?) 10)
				)
				(= enableSwitching 1)
				
		else
			(= enableSwitching 0)	
		)
		(myEvent dispose:)
		
		(if gSameRoomSwitch
			(localSwitcheroo)
			(= gSameRoomSwitch 0)	
		)
	)
	
	(method (handleEvent pEvent button)
		(super handleEvent: pEvent)
		

		(if (== (pEvent type?) evMOUSEBUTTON)
			(if enableSwitching
				;(localSwitcheroo)
			)		
	
			(if (& (pEvent modifiers?) emRIGHT_BUTTON)
				(if (checkEvent pEvent 247 288 89 98) ; table
					(PrintOther 247 1)
				)
				(if (checkEvent pEvent 294 319 96 129) ; symbol
					(PrintOther 247 2)
				)
				(if (== (OnControl ocSPECIAL (pEvent x?) (pEvent y?)) ctlSILVER) ; platform
					(if (and upTop gAnotherEgo)
						(PrintOther 247 8)			
					else
						(PrintOther 247 3)
					)					
				)
				(if
					(checkEvent pEvent (leahProp nsLeft?) (leahProp nsRight?) (leahProp nsTop?) (leahProp nsBottom?))
					(if gKneeHealed
						(if (not gSeparated)
							(PrintOther 200 29)	
						else
							(if (== gRoomNumber gSwitchedRoomNumber)
								(PrintOther 200 29)
							)
						)
					)
				)
			)
		)
		(if (or (== (pEvent message?) KEY_RETURN) (if (== (pEvent message?) 3) )) ; pressed right arrow
			(if message
				(if gPrintDlg
					(gPrintDlg dispose:)
					(= message 0)
					;(buttonInstructions hide:)						
					(convoScript cycles: 0 cue:)
					(dialogTrack)
				)
			)
		)

		(if (== (pEvent message?) 7)  ; pressed left arrow
			(if (> messagePrint 1)
				(if message
					(if gPrintDlg
						(gPrintDlg dispose:)
						(= message 0)
						;(buttonInstructions hide:)
						(-- messagePrint)						
						(convoScript cycles: 0 changeState: messagePrint)
						(dialogTrack)
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
							(leahProp init: show: posn: (actionEgo x?)(actionEgo y?) view: 343 loop: 2)														
							(actionEgo hide:)
							
							(if gPrintDlg
								(gPrintDlg dispose:)	
							)
							(= upTop 0)
							(= conversationMode 0)
							(= gAnotherEgo 0)
							(= gEgoMovementType 0)
							
							(RunningCheck)
							
							(egoProp hide:)
							(gEgo show: posn: (egoProp x?)(egoProp y?) setMotion: MoveTo (egoProp x?)(+ (egoProp y?) 14) setPri: -1)
							
							(leahScript changeState: 1)
							(convoScript changeState: 10)
						)
					)
				)
			)
		)		
		
		; handle Said's, etc...
		(if (Said 'talk/woman,leah')	
			(if gSeparated
				(if (not gAnotherEgo)	; if male in control
					(if upTop	; leah on the top
						(PrintLeah 247 21)	
					else
						(PrintCantDoThat)
					)
				else
					(PrintOther 247 24)
				)				
			else
				(PrintLeah 247 22)
			)
		)
		(if (Said 'talk/man')
			(if gAnotherEgo
				(if (== gSwitchedRoomNumber gRoomNumber)	
					(if upTop	; leah on the top
						(PrintMan 247 23)	
					else
						(PrintMan 247 24)	
					)
				else
					(PrintOther 247 25)		
				)
			else
				(PrintOther 247 24)						
			)
		)
		(if (Said 'talk/friend')	
			(if (and (not gSeparated) gKneeHealed)
				(if upTop	; leah on the top
					(if gAnotherEgo
						(PrintMan 247 23)
					else
						(PrintLeah 247 21)
					)	
				else
					(PrintLeah 247 22)
				)
				
			else
				(PrintCantDoThat)
			)
		)
		
		(if (Said 'touch,push/block,symbol')
			(PrintOther 247 19)
		)
		
		(if (or (Said '(climb,jump)<down') 
			(Said 'catch/woman,leah')
			(Said 'climb/rock,cliff')	
			(Said 'jump'))
			(if upTop
				(if (== gSwitchedRoomNumber gRoomNumber)
					(if (not gAnotherEgo)
						(= disableToggleAnimation 1)
						(localSwitcheroo)
					)
					(RoomScript changeState: 3)
					(liftUpScript changeState: 1)
				else
					(ProgramControl)
					(gEgo setMotion: MoveTo 90 40 fallScript ignoreControl: ctlWHITE)
				)	
			else
				(PrintOther 247 4) ; There's no need to do that from where you are now.
			)	
		)
		(if (Said 'look>')
			(if (Said '/masonry, symbol')
				(PrintOther 247 2)
			)
			(if (Said '/cliff,wall')
				(if (and upTop gAnotherEgo)
					(PrintOther 247 6)
				else
					(PrintOther 247 7)
				)
			)
			(if (Said '/ground,stone,rock, platform, circle')
				(if (and upTop gAnotherEgo)
					(PrintOther 247 8)			
				else
					(PrintOther 247 3)
				)
			)
			(if (Said '/path')
				(PrintOther 246 4)
			)
			(if (Said '/forest,verlorn')
				(PrintOther 0 105)	
			)
			(if (Said '[/!*]')
				(PrintOther 247 5)
			)
		)
	)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0 ; Handle state changes
			)
			(1	; if heading back to the room with danger
				(ProgramControl)
				(gEgo setMotion: MoveTo 22 31 self ignoreControl: ctlWHITE)	
			)
			(2	
				(= moving 0)
				(gEgo observeControl: ctlWHITE)
				(PlayerControl)
				(PrintOther 247 0)	
			)
			(3	; jumping down the cliff starts here
				(ProgramControl)
				(gEgo setMotion: MoveTo 90 40 self ignoreControl: ctlWHITE)
					
			)
			(4	;kneeling down	
				(gEgo hide:)
				(actionEgo show: posn: (gEgo x?)(gEgo y?) loop: 0 setCycle: End self cycleSpeed: 2 setPri: 15)
			)
			(5
				; falling
				(actionEgo posn: (actionEgo x?) (+ (actionEgo y?) 5) loop: 2 yStep: 5 setMotion: MoveTo (actionEgo x?) 95 self setPri: -1)	
			)
			(6
				; Ego catch
				(if inPosition
					(actionEgo hide:)
					(egoProp show: posn: 83 130 view: 375 loop: 0 cel: 0 setCycle: End self cycleSpeed: 3)
				else
					(fallScript changeState: 1)
				)
			)
			(7 (= cycles 20) ; add time
				(= gSwitchingAllowed 0)
				(= gSeparated 0)
					
			)
			(8	; kiss
				(egoProp loop: 1 cel: 0 posn: (- (egoProp x?) 3) (+ (egoProp y?) 1) setCycle: End self)
				; Ego catch
				;(egoProp show: loop: 0 cel: 0 setCycle: End self cycleSpeed: 2)
			)
			(9	(= cycles 12) ; break for effect
					
			)	
			(10	; drop Leah
				(egoProp loop: 2 cel: 0 setCycle: End self)
			)
			(11 (= cycles 3)
				; make falling animation more fluid
			)
			(12 (= cycles 15) ; hits ground and breathes hard
				(egoProp view: 903 loop: 0 posn: (- (egoProp x?) 15) (+ (egoProp y?) 0) )
				(actionEgo show: view: 376 loop: 0 posn: (+ (egoProp x?) 22) (+ (egoProp y?) 1) setCycle: Fwd cycleSpeed: 5)
				(ShakeScreen 1)
			)
			(13	 
				(egoProp view: 0 setCycle: Walk cycleSpeed: -1 setMotion: MoveTo (- (actionEgo x?) 25) (actionEgo y?) self)	
			)
			(14	; Leah reaches up to Ego
				(egoProp view: 903 loop: 0)
				(actionEgo loop: 2 cel: 0 setCycle: End self)
			)
			(15	; lifting up
				(egoProp view: 376 loop: 3 cel: 0 posn: (+ (egoProp x?) 12) (+ (egoProp y?) 1) setCycle: End self cycleSpeed: 3)
				(actionEgo hide:)
			)
			(16
				(egoProp view: 903 loop: 0 x: (- (egoProp x?) 15) )
				(gEgo loop: 1 posn: (+ (egoProp x?) 30) (egoProp y?) observeControl: ctlWHITE setPri: -1)
				(actionEgo show: posn: (gEgo x?)(gEgo y?) view: 373 loop: 1)
				(itemsMerge)
				(SetMenu MENU_SWITCH 112 0) ; disable switching
				(= gNoClick 0)
				
				;(PlayerControl)
				
				(= conversationMode 1)
				(convoScript changeState: 1)
				(TheMenuBar state: DISABLED)					
			)
				; death fall animation	
				; deathscript time	
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
				(PrintLeah 247 10)		
			)
			(2
				(PrintMan 247 11)	
			)
			(3
				(PrintLeah 247 12)
			)
			(4
				(PrintLeah 247 13)	
			)
			(5
				(PrintMan 247 14)
			)
			(6
				(PrintMan 247 15)	
			)
			(7
				(PrintLeah 247 16)
			)
			(8
				(PrintLeah 247 17)	
			)
			(9
				(PrintMan 247 18)	
			)
			(10 
				
				(leahProp init: show: posn: (actionEgo x?)(actionEgo y?) view: 343 loop: 2)
				(leahScript changeState: 1)
				
				(actionEgo hide:)
				(= upTop 0)
				(= conversationMode 0)
				(= gAnotherEgo 0)
				(= gEgoMovementType 0)
				(RunningCheck)
				
				(egoProp hide:)
				(gEgo show: posn: (egoProp x?)(egoProp y?) setPri: -1 setMotion: MoveTo (egoProp x?)(+ (egoProp y?) 14) self)
				
			)
			(11
				(PlayerControl)		
				(TheMenuBar state: ENABLED)			
				;(characterSelect show: posn: (gEgo x?) (+ (gEgo y?) 2) loop: 4 cel: 0 setCycle: End setPri: -1)	; male ego
				(gGame changeScore: 5)
				
			)
		)
	)
)

(instance fallScript of Script
	(properties)
	
	(method (changeState newState dyingScript)
		(= state newState)
		(switch state
			(0
			)
			(1
				(ProgramControl)
				(gEgo hide:)
				(if fallingToDeath	
					(actionEgo show: posn: (gEgo x?)(gEgo y?) view: 374 loop: 2 yStep: 8 setMotion: MoveTo (gEgo x?) 145 self)
					(if (< (gEgo y?) 36)
						(actionEgo setPri: 0)	
					)
				else
					(actionEgo show: posn: (gEgo x?)(gEgo y?) view: 374 loop: 2 yStep: 8 setMotion: MoveTo (gEgo x?) 145 self)
				)	
			)
			(2
				(actionEgo posn: (gEgo x?) 145 loop: 4 cel: 0 setCycle: End self cycleSpeed: 2)		
			)
			(3
				(ShakeScreen 1)
				(actionEgo loop: 5 cel: 0 setCycle: End self cycleSpeed: 2)		
			)
			(4
				(if (not [gDeaths 3])
					(++ gUniqueDeaths)
				)
				(++ [gDeaths 3])
				(= gDeathIconEnd 1)
				
				(= dyingScript (ScriptID DYING_SCRIPT))
				(dyingScript
					caller: 749
					register:
						{\nWhat an exhilarating little adventure! Too bad you couldn't quite stick the landing.}
				)
				(gGame setScript: dyingScript)	
			)
		)
	)
)


(instance liftUpScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0
			)
			(1	; sent Ego to catch
				(egoProp show: view: 0 setCycle: Walk setMotion: MoveTo 70 130 self)	
			)
			(2
				(egoProp view: 903 loop: 0)
				(= inPosition 1)	
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
(procedure (localSwitcheroo)
	(if (not gProgramControl)
		(if gSeparated
			(if gSwitchingAllowed
				(if (== gRoomNumber gSwitchedRoomNumber)
					;(PrintOK)
					(= [characterXYToggle 0] (gEgo x?))
					(= [characterXYToggle 1] (gEgo y?))
					(= [characterXYToggle 2] (gEgo loop?))
					
					(gEgo posn: (egoProp x?) (egoProp y?) loop: 2)
					(egoProp posn: [characterXYToggle 0] [characterXYToggle 1] loop: 2)
					
					(= itemIteration 0)
									
					(for ( (= itemIteration 0)) (< itemIteration 14)  ( (++ itemIteration))
						(if gAnotherEgo
						; Switching from Female Ego
							(if (== (gEgo has: itemIteration) 1)
								(gEgo put: itemIteration 210)
							)                                          ; Items stored in Room 210
							(if (== (IsOwnedBy itemIteration 205) 1)
						; Items stored in Rm 205 given to Male Ego
								(gEgo get: itemIteration)
							)
						else
						; Switching from Male Ego
							(if (== (gEgo has: itemIteration) 1)
								(gEgo put: itemIteration 205)
							)                                          ; Items stored in Room 205
							(if (== (IsOwnedBy itemIteration 210) 1)
						; Items stored in Rm 210 given to Female Ego
								(gEgo get: itemIteration)
							)
						)
					)
					
					(if gAnotherEgo
						(= gAnotherEgo 0)
						(egoProp view: 1)
						(if upTop
							 (egoProp setPri: 2)	
						else
							(egoProp setPri: -1)
						)
						(if (not disableToggleAnimation)
							(characterSelect show: posn: (gEgo x?) (+ (gEgo y?) 2) loop: 4 cel: 0 setCycle: End setPri: -1)	; male ego
						)
					else
						(= gAnotherEgo 1)
						(egoProp view: 903 setPri: -1)
						(if (not disableToggleAnimation)
							(characterSelect show: posn: (gEgo x?) (+ (gEgo y?) 1) loop: 3 cel: 0 setCycle: End setPri: 2)	; female ego
						)
					)
					(RunningCheck)
					
					(if gAnotherEgo ; leah
						(= [gAnotherEgoXYL 0] (gEgo x?))
						(= [gAnotherEgoXYL 1] (gEgo y?))
						(= [gAnotherEgoXYL 2] (gEgo loop?))
					else
						(= [gPrevXY 0] (gEgo x?))
						(= [gPrevXY 1] (gEgo y?))
						(= [gPrevXY 2] (gEgo loop?))	
					)
				)
			)
		)
	)
)
(procedure (itemsMerge)
	(for ( (= i 0)) (< i 14)  ( (++ i))                                   
		(if (== (IsOwnedBy i 210) 1)
			(gEgo get: i)
		)
	)
)
(procedure (dialogTrack)
	(= messagePrint (convoScript state?))
)
(procedure (PrintOther textRes textResIndex)
	(if (> (gEgo y?) 70)
		(= textPlace 10)
	else
		(= textPlace 140)
	)
	(Print textRes textResIndex
		#width 280
		#at -1 textPlace
	)
)
(procedure (PrintMan textRes textResIndex)
	(= gWndColor 7)
	(= gWndBack 1)
	(if conversationMode
		(Print textRes textResIndex		
			#width 160
			#at 20 10	
			#dispose
			#title "You say:"
		)
		(= message 1)
		(egoProp view: 251 loop: 0 cel: 0 setCycle: End cycleSpeed: 3)
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
(procedure (PrintLeah textRes textResIndex)
	(= gWndColor 14)
	(= gWndBack 5)
	(if conversationMode
		(Print textRes textResIndex		
			#width 160
			#at 110 90	
			#title "Leah"
			#dispose
		)
		(= message 1)
		(actionEgo view: 373 loop: 1 cel: 0 setCycle: End cycleSpeed: 3)
	else
		(Print textRes textResIndex
		#width 160
		#at 160 10
		#title "Leah:"
		)
	)
	(= gWndColor 0)
	(= gWndBack 15)
	;(= message 1)
)
(instance actionEgo of Act
	(properties
		y 1
		x 1
		view 374
	)
)
(instance egoProp of Act
	(properties
		y 160
		x 100
		view 0
	)
)
(instance leahProp of Act
	(properties
		y 150
		x 160
		view 1
	)
)
(instance switcher of Prop
	(properties
		y 29
		x 15
		view 997
		loop 1
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