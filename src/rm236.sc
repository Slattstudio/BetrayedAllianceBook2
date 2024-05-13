;;; Sierra Script 1.0 - (do not remove this comment)
; SCORE + 1
(script# 236)
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
(use menubar)

(public
	rm236 0
)

(local

	myEvent 
	comeOrGo = 0 ; variable for when Ego is looking from other room to either enter the room or to go back to the vantage point room
	opening = 0 ; used to make sure ego doesn't look straight during the intro scene
	
	usingMushrooms = 0
	
	messagePrint = 0 ; used for all changes to dialog to go backwards and forwards
	message = 0 ; used to remove "dispose" windows and avoid crashing
	conversationMode = 0
	cutsceneWait = 0	; when true, can't advance cutscene
	
	moveAction = 2 ; used in cutscene to move the action only once
	
	
)


(instance rm236 of Rm
	(properties
		picture scriptNumber
		north 0
		east 234
		south 0
		west 235
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript setRegions: 200 setRegions: 205)
		
		(if (not gLookingAhead)
			(TheMenuBar draw:)
			(SL enable:)
			(TheMenuBar state: ENABLED)
			;(= gMenuBarBlock 0) ; disable menubar
		)
		
		(SetUpEgo)
		(gEgo init: setScript: cutsceneWaitScript)
		(RunningCheck)
		
		(= gMap 0)
		(= gArcStl 0)
		
		(injuredEgo init: setScript: pickUpScript)
		
		(if gKneeHealed
			(if (not gSeparated)
				(injuredEgo hide: ignoreActors: setScript: convoScript)
				(leahProp init: setScript: leahScript) 
			)
		)
		(actionEgo init: hide: ignoreActors: setScript: enterScript)
		
		(if gLookingAhead
			(enterBackFrame init: ignoreActors: setPri: 15)
			(enterButton init: ignoreActors: setPri: 15)
			(backButton init: ignoreActors: setPri: 15)
			(RoomScript changeState: 19)
			
			(gEgo hide:)
			(= gMap 1)
			(= gArcStl 1)			
		)
		
		(switch gPreviousRoomNumber
			(234
				(PlaceEgo 300 120 1)
				;(gEgo posn: 300 120 loop: 1)
				(leahScript cue:)	
			)
			(235
				(PlaceEgo 25 120 0)
				;(gEgo posn: 25 120 loop: 0)
				(leahProp posn: 30 100 view: 343 setCycle: Walk cycleSpeed: 0 setMotion: MoveTo 80 90 leahScript)
				
			)
			(302
				(PlaceEgo (injuredEgo x?)(+ (injuredEgo y?) 5) 3)
				;(gEgo posn: 80 120 loop: 3)
				(if gKneeHealed
					(enterScript changeState: 3)
					(leahScript cue:)
				else
					(PlaceEgo 85 92 1)
					(gEgo hide:)
					(actionEgo show: posn: (gEgo x?)(gEgo y?) view: 450 loop: 1 cel: 3)
					(pickUpScript changeState: 5)
				)
				
			)
			(336
				(PlaceEgo 96 82 1)
				
				(RoomScript changeState: 3)
				(gEgo hide:)
				(actionEgo show: posn: 96 92 view: 311 loop: 8 cel: 0)
				(= conversationMode 1)
				(= gMap 1)
				(TheMenuBar state: DISABLED)
				
			)
			(else 
				;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
				;(PlaceEgo 96 82 1)
				
				;(RoomScript changeState: 3)
				;(gEgo hide:)
				;(actionEgo show: posn: 96 92 view: 311 loop: 8 cel: 0)
				;(= conversationMode 1)
				;(= gMap 1)
				;(TheMenuBar state: DISABLED)
				;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
				
				; tutorial box and toggle
				(PlaceEgo 100 120 2)
				(RoomScript changeState: 14)
			)
		)
	)
)

(instance RoomScript of Script
	(properties)
	
	(method (doit)
		(super doit:)
		; code executed each game cycle
		
		(if (not gLookingAhead)
			(if (> (- (gEgo x?) 20) (injuredEgo x?))
				(injuredEgo loop: 2 cel: 2)	; look right	
			else
				(if (not opening)
					(if (> (+ (gEgo x?) 20) (injuredEgo x?))
						(injuredEgo loop: 2 cel: 0) ; look straight
					else
						(injuredEgo loop: 2 cel: 1) ; look left
					)
				)
			)
		)
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
		
		
	)
	
	(method (handleEvent pEvent button)
		(super handleEvent: pEvent)
		; CUTSCENE stuff
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
		
		(if (== (pEvent type?) evKEYBOARD)
			(if gLookingAhead
				(if (== (pEvent message?) KEY_ESCAPE)
					(gRoom newRoom: 235)
				)
			else
				(if (== (pEvent message?) KEY_ESCAPE)
					(if conversationMode
						(= gWndColor 0)
						(= gWndBack 14)
						(= button (Print 997 10 #button { Yes_} 1 #button { No_} 0 #font 4 #at -1 10))
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
								(actionEgo hide:)
								(PlaceEgo 100 120 2)
								(gEgo show:)
								(PlayerControl)
								(RoomScript changeState: 14)
								(TheMenuBar state: ENABLED)
								(= gArcStl 0)
								(= gMap 0)
								(= conversationMode 0)
							)
						)
					)
				)
			)
		)
		
		(if gLookingAhead
			(if (== (pEvent type?) evKEYBOARD)
				(if (or (== (pEvent message?) KEY_e) (== (pEvent message?) KEY_RETURN)) ; enter
					(enterScript changeState: 1)
					(RoomScript cycles: 0 changeState: 0)
					(injuredEgo setCycle: NULL)
					
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
				(if (== (pEvent message?) 7) 
					(gRoom newRoom: 235)	
				)
			)
		)
		
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if (& (pEvent modifiers?) emRIGHT_BUTTON)
				
				(if
					(checkEvent pEvent (leahProp nsLeft?) (leahProp nsRight?) (leahProp nsTop?) (leahProp nsBottom?))
					(if gKneeHealed
						(if (not gSeparated)
							(PrintOther 200 29)	
							(return)
						)
					)
				else
				
					(if (checkEvent pEvent 262 287 52 65) ; bug cave
						(if g236Bug
							(Print 236 9)	; The hole is now empty.	
						else
							(if gRightClickSearch
								
								(if (& (gEgo onControl:) ctlSILVER)
									(self changeState: 16)
								else
									(PrintOther 236 3)
								)
							else
								(PrintOther 236 3)
							)
						)
						;(PrintOther 236 3)
					)
				)
				(if (== (OnControl ocPRIORITY (pEvent x?) (pEvent y?)) ctlGREEN) ; Rock overhang
					(PrintOther 236 4)					
				)
				
				(if
					(checkEvent pEvent
						(injuredEgo nsLeft?)
						(injuredEgo nsRight?)
						(injuredEgo nsTop?)
						(injuredEgo nsBottom?)
					)
					(if gKneeHealed
					else
						(PrintOther 236 31)
					)
				else
					(if (== (OnControl ocPRIORITY (pEvent x?) (pEvent y?)) ctlNAVY) ; cave inside
						(PrintOther 236 5)					
					)
				)
			)
			(if (== comeOrGo 1)
				(enterScript changeState: 1)
				(= gLookingAhead 0)
				(= comeOrGo 0)
				(backButton hide:)
				(enterButton hide:)
				(enterBackFrame hide:)
				;(self changeState: 19)
			)
			(if (== comeOrGo 2)
				(gRoom newRoom: 235)	
			)
		)
		; handle Said's, etc...
		
		(if gAnotherEgo
			(if (or (Said 'ask<about>') (Said 'ask/man>'))
			    (if (or (Said '/nose') (Said '//nose'))
			        (PrintOther 236 21)   
				)
			   	(if (or (Said '/mushroom') (Said '//mushroom'))
			       	(PrintMan  28 )
			 	)
			 	(if (or (Said '/princess,julyn') (Said '//princess,julyn'))
			       	(PrintMan  34 )
			 	)
			 	(if (or (Said '/wizard') (Said '//wizard'))
			       	(PrintMan  35 )
			 	)
			 	(if (or (Said '/gyre') (Said '//gyre'))
			       	(PrintMan  36 )
			 	)
			 	(if (or (Said '/*') (Said '//*'))
			       	(PrintMan 29)
			 	)
			)
		
			;(if (Said 'ask/man/nose')
			;	(PrintMan 21)
			;)
			;(if (Said 'ask/man>')
			;	(if (Said '/nose')
			;		(PrintMan 21)
			;	)
			;)
			;(if (Said 'ask<about>')
			;   (if (Said '[/man]/nose')
			;           (PrintMan 21) 
			;	)  
			;)
			;(if (Said '(ask<about)>')
			;	(if (Said '/tree')
			;		(PrintMan 25) ; default statement	
			;	)
			;	(if (Said '/(nose, booger)')
			;		(PrintMan 21)	
			;	)
			;	(if (Said '/*')
			;		(PrintMan 25)	
			;	)
			;)
		)
		(if (Said 'give,put,show/insect,bug>') ;[man,friend,companion]')
			(if (Said '//man,friend,companion')
				(if gAnotherEgo
					(if gKneeHealed
						(PrintOther 0 89)		
					else	
						(if gBugs
							(if (< (gEgo distanceTo: injuredEgo) 45)
								(PrintOther 236 32)	
							else
								(PrintNotCloseEnough)
							)	
						else
							(PrintDontHaveIt)
						)
					)
				else
					(PrintOther 0 89)
				)
			)
			(if (Said '//*')
				(if gBugs
					(Print 0 90)	
				else
					(PrintDontHaveIt)
				)	
			)
			(if (Said 'give,put,show/insect,bug')
				(if gBugs
					(if (< (gEgo distanceTo: injuredEgo) 45)
						(PrintOther 236 32)	
					else
						(Print 0 90)
					)	
				else
					(PrintDontHaveIt)
				)	
			)
		)
	
			
	
		(if (Said 'take>')		
			(if (Said '/bug,insect')
				(if (not g236Bug)
					(if (& (gEgo onControl:) ctlSILVER)
						(self changeState: 16)
					else
						(PrintOther 236 8)
					)
				else
					(PrintAlreadyTookIt)
				)	
			)
		)
		(if (Said 'talk/woman,leah')	
			(if (and (not gSeparated) gKneeHealed)
				(PrintLeah 26)
				(if gYellowTips
					(= gWndColor 0)
					(= gWndBack 14)
					(Print 236 27 #font 4 #at -1 10 #button "Ok")
					(= gWndColor 0)
					(= gWndBack 15)
				)
				
			else
				(PrintCantDoThat)
			)
		)
		(if (or (Said 'talk/man,companion')
			(Said 'look/leg, knee'))
			(if (not gKneeHealed)
				(if (< (gEgo distanceTo: injuredEgo) 45)
					(pickUpScript changeState: 1)	
				else
					(PrintNotCloseEnough)
				)
			else
				(PrintOther 236 18)
			)	
		)
		(if (Said 'look>')		
			(if (Said '/man,companion,friend')
				(if (not gKneeHealed)
					(if (< (gEgo distanceTo: injuredEgo) 45)
						(pickUpScript changeState: 1)	
					else
						(PrintNotCloseEnough)
					)
				else
					(Print "There's nothing interesting about it.")
				)
			)
			(if (Said '/shelter,cave,cavern')
				(if (& (gEgo onControl:) ctlSILVER)
					(if g236Bug
						(Print 236 9)	; The hole is now empty.	
					else
						(PrintOther 236 6)
					)
				else	
					(PrintOther 236 5)
				)
			)
			(if (Said '/hole,pit')
				(if (& (gEgo onControl:) ctlSILVER)
					(if g236Bug
						(Print 236 9)	; The hole is now empty.	
					else
						(PrintOther 236 6)
					)
				else
					(PrintOther 236 3)
				)
			)
			(if (Said '/forest,verlorn')
				(PrintOther 0 105)	
			)
			(if (Said '[/!*]')
				;(if (& (gEgo onControl:) ctlSILVER)
				;	(Print "There's something in there")
				;else
					(PrintOther 236 17) ;"The small recess in the wall provides a slightly private area.")
				;)
			)
		)
		
		(if (Said 'give,use,put/mushroom')
			(if gMushrooms
				(= usingMushrooms 1)
				(pickUpScript changeState: 1)
			else
				(PrintDontHaveIt)
			)	
		)
		(if (Said 'sit')
			(PrintOther 236 20)
		)
		(if (Said 'smell')
			(if (& (gEgo onControl:) ctlSILVER)
				(if g236Bug ; got the bug
					(PrintOther 236 24)
				else
					(PrintOther 236 23)
				)
			else
				(if g236Bug ; got the bug
					(PrintOther 236 24)
				else
					(PrintOther 236 22)
				)
			)
		)
		; REMOVE :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
		(if (Said 'hi')
			(= gKneeHealed 1)
			(= g303egoHealed 1)
		)
		;(if (Said 'get/mushroom')
		;	(= gMushrooms 3)	
		;)
		;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
		
	)
	
	(method (changeState newState button)
		(= state newState)
		(switch state
			(1 ; Handle state changes
				(ProgramControl)
				(gEgo posn: 1 120 setMotion: MoveTo 30 120 self)
			)
			(2
				(PlayerControl)	
			)
			(3	(= cycles 30)				
				(ProgramControl)
				(= opening 1)
				(actionEgo loop: 8 cel: 0 setCycle: Fwd cycleSpeed: 5)	
			)
			(4	; sitting up
				(actionEgo loop: 7 cel: 0 setCycle: End self cycleSpeed: 3)
					
			)
			(5	
				;(PrintLeah 10)	; Good morning! It was nice to sleep under the stars again, even if just for a little while.
				;(PrintLeah 11)	; How're you feeling now? Let's check that leg.
				(convoScript changeState: 1)
			)
			(6	; lifting up to stand
				(actionEgo loop: 1 cel: 0 x: (+ (actionEgo x?) 10) setCycle: End cutsceneWaitScript)
				
				;(convoScript changeState: 2)
			)
			(7	; move to check leg
				(gEgo show: posn: (actionEgo x?)(actionEgo y?) setMotion: MoveTo (+ (injuredEgo x?) 15) (injuredEgo y?) self)
				(actionEgo hide:)
			)
			(8	; bend down
				(gEgo hide:)
				(actionEgo show: posn: (gEgo x?)(gEgo y?) view: 450 loop: 1 cel: 0 setCycle: End self cycleSpeed: 2)
			)
			(9
				(cutsceneWaitScript changeState: 1)
				;(convoScript changeState: 3)		
			)
			(10
				(cutsceneWaitScript changeState: 1)
				;(convoScript changeState: 4)		
			)
			(11	; straighten up
				(actionEgo setCycle: Beg cutsceneWaitScript)
				;(convoScript changeState: 5)	
			)
			(12	;(= cycles 5)
				;(PrintLeah 14)	; It'll help with more than that, but I will need more. Probably about three, I'd say.
				;(PrintLeah 15)	; Rest here. I'll find them, then you'll be back to your usual self in no time.
				;(PrintMan 16)	; Thank you. I wish I could help. I owe you one...well two now, I suppose.
				(cutsceneWaitScript changeState: 1)
				;(convoScript changeState: 6)
			)
			(13
				(actionEgo hide:)
				(gEgo show: setMotion: MoveTo (+ (gEgo x?) 4) (+ (gEgo y?) 15) cutsceneWaitScript )
				;(convoScript changeState: 7)
			)
			(14	(= cycles 2)
				
				(= opening 0)
				(= conversationMode 0)
				(TheMenuBar state: ENABLED)
				
				(PlayerControl)	
					
			)
			(15
				; if not character named
				(if (== gName 0)
					(EditPrint @gName 14
					{Welcome to Betrayed Alliance: Book 2.\n\nHonor us again with knowledge of your name:}
					#title {Player Name:}
					#at 160 -1
					#width 120
					)
					(if (== gName 0)
						(Print {Come on you can do better!})
						(self changeState: 14)
						(return)
					)
				)
				; tutorial box and toggle
				(= gWndColor 0)
				(= gWndBack 14)
				;(= gYellowTips
					(Print 236 30 #font 4 #at 160 -1 #width 120 #title {How to play:} #button { OK_} )
				;)
				(= gWndColor 0)     ; clBLACK
				(= gWndBack 15)	
				
				(= gMap 0)
				(= gArcStl 0)
				
				(gTheMusic fade:) 
				;(Load rsSOUND 10)
				(gTheMusic number: 10 loop: -1 priority: -1 play:)	
			)
			(16	; getting the bugs
				(ProgramControl)
				(gEgo setMotion: MoveTo 265 83 self)
			)
			(17	(= cycles 5)
				(gEgo loop: 3)	
			)
			(18
				(PlayerControl)
				(PrintOther 236 7)	
				(= g236Bug 1)
				(gEgo get: 6)
				(= gBugs (+ gBugs 1))
				((gInv at: 6) count: gBugs)	
				
				(gGame changeScore: 1)
			)
			; nose picking
			(19	(= cycles 25)
				(injuredEgo loop: 3 setCycle: Fwd cycleSpeed: 5)	
			)
			(20
				(injuredEgo loop: 4 cel: 0 setCycle: End self cycleSpeed: 4)
			)
			(21
				(injuredEgo loop: 5 cel: 0 setCycle: End cycleSpeed: 3)		
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
			(1 
				(ProgramControl)
				(gEgo hide:)
				(actionEgo show: view: 343 posn: 0 120 loop: 1 cel: 0 setCycle: Walk setMotion: MoveTo 40 120 self)
			)
			(2
				(actionEgo hide:)
				(gEgo show: posn: 40 120)
				(PlayerControl)
				(= gMap 0)
				(= gArcStl 0)
				(TheMenuBar state: ENABLED)	
			)
			(3	; ego Standing up
				(ProgramControl)
				(gEgo hide:)
				(actionEgo show: posn: (gEgo x?)(gEgo y?) view: 405 loop: 0 cel: 2 setCycle: Beg self cycleSpeed: 3)
				(PrintOther 236 19)	
			)
			(4		
				(= gEgoMovementType 0)
				(RunningCheck)
				(gEgo show: setMotion: MoveTo (gEgo x?) (+ (gEgo y?) 20) self)
				(actionEgo hide:)
				;(PrintOther 236 19)		
			)
			(5
				(PlayerControl)
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
(instance pickUpScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)
			(1	; move close to Ego
				(ProgramControl)
				(gEgo setMotion: MoveTo 85 92 self)
			)
			(2	; bend over to examing
				(gEgo hide:)
				(actionEgo show: posn: (gEgo x?)(gEgo y?) view: 450 loop: 1 cel: 0 setCycle: End self cycleSpeed: 3)		
			)
			(3	(= cycles 7)	; apply mushrooms to just get close to look/talk
				(if usingMushrooms
					(FormatPrint "You open your bag looking for mushrooms and find %u." gMushrooms)
					(PrintOther 236 0)
					(if (> gMushrooms 1)
						(PrintOther 236 1)		
					)
					(= g303egoHealed (+ g303egoHealed gMushrooms))
					(= gMushrooms 0)
					((gInv at: 5) count: gMushrooms)
					(gEgo put: 5 236)
					(if (== g303egoHealed 3)
						(PrintOther 236 2)	
						;(gRoom newRoom: 302)
					else
						(FormatPrint "It looks like there is room for %u more to cover the wound." (- 3 g303egoHealed))	
					)	
				else
						
				)		
			)
			(4
				(gRoom newRoom: 302)	
			)
			(5	; back from close up - kneeling to standing
				(ProgramControl)
				(actionEgo setCycle: Beg self cycleSpeed: 3)
				
			)
			(6
				(actionEgo hide:)
				(gEgo show: setMotion: MoveTo (gEgo x?)(+ (gEgo y?) 10) self)	
			)
			(7				
				(PlayerControl)	
			)
		)
	)
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
(instance convoScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0
			)
			(1	
				(PrintLeah 10)	; Good morning! It was nice to sleep under the stars again, even if just for a little while.
			)
			(2
				(PrintLeah 11)	; How're you feeling now? Let's check that leg.
			)
			(3
				(PrintLeah 12)	; Still in pretty rough shape, isn't it?
			)
			(4
				(PrintMan 13)	; Yes, I'm afraid so. The mushroom did seem to help at least with the pain though. I was able to get some sleep.
			)
			(5
				(PrintLeah 14)	; It'll help with more than that, but I will need more. Probably about three, I'd say.
			)
			(6
				(PrintLeah 15)	; Rest here. I'll find them, then you'll be back to your usual self in no time.
			)
			(7
				(PrintMan 16)	; Thank you. I wish I could help. I owe you one...well two now, I suppose.
			)
		)
	)
)

(procedure (dialogTrack)
	(= messagePrint (convoScript state?))
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
(procedure (PrintLeah textResIndex)
	(= gWndColor 14)
	(= gWndBack 5)
	(if conversationMode
		(Print scriptNumber textResIndex		
			#width 180
			#at 100 110
			#title "Leah says:"
			#dispose
		)
		(= message 1)
		;(leah view: 373 loop: 0 cel: 0 setCycle: End cycleSpeed: 3)
	else
			(Print scriptNumber textResIndex		
			#width 180
			#at 100 110
			#title "Leah says:"
		)
	)
	(= gWndColor 0)
	(= gWndBack 15)

)
(procedure (PrintMan  textResIndex)
	(= gWndColor 7)
	(= gWndBack 1)
	(if conversationMode
		(Print scriptNumber textResIndex
			#width 180
			#at 20 110
			#title "You say:"
			#dispose		
		)
		(= message 1)
	else
		(Print scriptNumber textResIndex
			#width 180
			#at 20 110
			#title "You say:"

		)
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

(instance actionEgo of Act
	(properties
		y 170
		x 160
		view 343
	)
)
(instance injuredEgo of Prop
	(properties
		y 90
		x 70
		view 412
		loop 2
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
(instance leahProp of Act
	(properties
		y 90
		x 90
		view 1
	)
)
