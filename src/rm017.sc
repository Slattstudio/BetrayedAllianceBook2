;;; Sierra Script 1.0 - (do not remove this comment)
(script# 17)
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
	
	rm017 0
	
)
(local
	
	foundOut = 0	
	
	message = 0
	
	conversationMode = 0 ; used when characters are talking in the handleEvent Method
	messagePrint = 0
	
)

(instance rm017 of Rm
	(properties
		picture scriptNumber
		north 12
		east 22
		south 237
		west 0
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript RoomScript setRegions: 205 setRegions: 200)
		
		(if gKneeHealed
			(if (not gSeparated)
				(leahProp init: setScript: leahScript) 
			)
		)
		
		(switch gPreviousRoomNumber
			(12 
				(PlaceEgo 240 40 2)
				;(gEgo posn: 240 40 loop: 2)
			)
			(22 
				(PlaceEgo 300 120 1)
				;(gEgo posn: 300 120 loop: 1)
				(leahProp posn: 310 105 view: 343 setCycle: Walk ignoreControl: ctlWHITE cycleSpeed: 0 setMotion: MoveTo 305 105 leahScript)
			)
			(237
				(PlaceEgo 240 168 3)
				;(gEgo posn: 240 168 loop: 3)
			)
			(else 
				(PlaceEgo 150 100 1)
				;(gEgo posn: 150 100 loop: 1)
			)
		)
		(SetUpEgo)
		(gEgo init:)
		(RunningCheck)

		(actionEgo init: hide: ignoreActors:)
		
		(blastOne init: hide: ignoreActors: ignoreControl: ctlWHITE xStep: 7 yStep: 7 setCycle: Walk)
		(blastTwo init: hide: ignoreActors: ignoreControl: ctlWHITE xStep: 7 yStep: 7 setCycle: Walk)
		(enchantress init: setScript: enchantressScript)	
		(leah init: ignoreActors: hide:)			
		(brokenWheel init: ignoreActors:)
		(rock init: ignoreActors:)
		(wagon init: ignoreActors:)
	)
)

(instance RoomScript of Script
	(properties)
	
	(method (doit)
		(super doit:)
		; code executed each game cycle
		(if (& (gEgo onControl:) ctlSILVER)
			(if (not foundOut)
				(= foundOut 1)
				(= conversationMode 1)
				(self changeState: 1)
					
			)
		)
	)
	
	(method (handleEvent pEvent)
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
					(if (checkEvent pEvent (enchantress nsLeft?)(enchantress nsRight?)(enchantress nsTop?)(enchantress nsBottom?))
						(PrintOther 17 2)
					else
						(if (checkEvent pEvent (wagon nsLeft?)(wagon nsRight?)(wagon nsTop?)(wagon nsBottom?))
							(PrintOther 17 3)
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
					(RoomScript cycles: 0 cue:)
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
						(RoomScript cycles: 0 changeState: messagePrint)
						(dialogTrack)
					)
				)
			)
		)
		
		; handle Said's, etc...
		(if (Said 'talk/woman')
			(if gSeparated
				(self changeState: 13)	; set up for death		
			else
				(PrintLeahWhisper 17 10)
			)	
		)
		(if (Said 'talk/woman,leah')	
			(if (and (not gSeparated) gKneeHealed)
				(PrintLeahWhisper 17 10)					
			else
				(PrintCantDoThat)
			)
		)
		(if (or (Said 'ask<about>') (Said 'ask/woman,leah>'))
			(if (or (Said '/*') (Said '//*'))
				(if gSeparated
					(PrintLeahWhisper 17 4)	
				else
					(PrintOther 17 5)
				)
			)
		)
		
		(if (Said 'talk/enchantress')	
			(self changeState: 13)	; set up for death					
		)
		
		(if (Said 'look>')
			(if (Said '/enchantress,woman')
				(PrintOther 17 2)	
			)
			(if (Said '/wagon')
				(PrintOther 17 3)	
			)
			(if (Said '/forest,verlorn')
				(PrintOther 0 105)	
			)
			(if (Said '[/!*]')
				(PrintOther 17 0)
				(PrintOther 17 2)
			)
		)
	)
	
	(method (changeState newState dyingScript)
		(= state newState)
		(switch state
			(0 ; Handle state changes
			)
			(1
				(ProgramControl)
				(enchantressScript cycles: 0 changeState: 5)
				(enchantress loop: 8 cel: 0 setCycle: Fwd cycleSpeed: 2)				
				(gEgo hide:)
				(actionEgo show: view: 251 loop: 1 cel: 0 posn: (gEgo x?)(gEgo y?))
				(PrintEnchantress 17 11)
				
				(if gSeparated
					
				else
					(leahProp hide:)
					(leah view: show: 373 loop: 1 show: posn: (leahProp x?)(leahProp y?))
				)		
			)
			(2
				(enchantress loop: 5 cel: 0 setCycle: CT)
				;(leah view: 373 loop: 0 cel: 0 setCycle: Fwd cycleSpeed: 2)				
				(if gSeparated
					(PrintMan 17 16)	
				else
					(PrintLeah 17 12)
				)
			)
			(3
				(enchantress loop: 5 cel: 0 setCycle: CT)
				;(leah view: 373 loop: 0 cel: 0 setCycle: Fwd cycleSpeed: 2)
				(if gSeparated
					(PrintMan 17 17)	
				else	
					(PrintMan 17 13)	
				)	
			)
			(4
				(enchantress loop: 8 cel: 0 setCycle: Fwd cycleSpeed: 2)
				(PrintEnchantress 17 14)		
			)
			(5
				(enchantress loop: 5 cel: 0 setCycle: End self cycleSpeed: 2)	
			)
			(6	(= cycles 20)
				(enchantress loop: 6 cel: 0 setCycle: Fwd cycleSpeed: 2)		
			)
			(7	(= cycles 3)
				(enchantress loop: 9 cel: 0)	
			)
			(8
				(enchantress loop: 7 cel: 0 setCycle: End cycleSpeed: 2)
				(blastOne show: posn: (enchantress x?)(- (enchantress y?) 20) setMotion: MoveTo (gEgo x?)(- (gEgo y?) 20) self)
				(if (not gSeparated)
					(blastTwo show: posn: (enchantress x?)(- (enchantress y?) 20) setMotion: MoveTo (leah x?)(- (leah y?) 20))	
				)
			)
			(9
				(gEgo hide:)
				(blastOne hide:)
				(blastTwo hide:)
				(actionEgo show: view: 722 loop: 1 cel: 0 posn: (gEgo x?)(gEgo y?) setCycle: End self cycleSpeed: 3)
				(if (not gSeparated)
					(leah view: 1 loop: 9 cel: 0 setCycle: End)
				)
			)	
			(10
				(self cycles: 0)
				(if (not [gDeaths 11])	; turned to stone
					(++ gUniqueDeaths)
				)
				(++ [gDeaths 11])
				
				(= gCurrentCursor 999)
				(= gDeathIconEnd 1)
				
				(= dyingScript (ScriptID DYING_SCRIPT))
				(dyingScript
					caller: 722
					register:
						{\nIt's going to be awfully hard to pass that kidney stone now that your kidney is the stone. Well, that and the whole rest of you.}
				)
				(gGame setScript: dyingScript)	
			)
			(13	
				(ProgramControl)
				(enchantressScript cycles: 0 changeState: 5)
				(= foundOut 1)
				(gEgo setMotion: MoveTo (- (gEgo x?) 10) (gEgo y?) self)				
				(enchantress loop: 0 setCycle: CT)	
				
				(if gSeparated
					
				else
					(leahProp hide:)
					(leah view: show: 373 loop: 1 show: posn: (leahProp x?)(leahProp y?))	
				)
			)
			(14	(= cycles 2)
			)
			(15	
				(= conversationMode 1)
				(gEgo hide:)
				(actionEgo show: view: 251 loop: 1 cel: 0 posn: (gEgo x?)(gEgo y?))	
				(PrintMan 17 15)	
			)
			(16
				
				(self changeState: 1)	
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

(instance enchantressScript of Script
	(properties)
	
	(method (changeState newState rando)
		(= state newState)
		(switch state
			(0
				(enchantress setCycle: End self cycleSpeed: 2)
			)	
			(1	(= cycles (Random 50 150))
					
			)
			(2
				(enchantress setCycle: Beg self)	
			)
			(3 (= cycles (Random 50 150))	
			)
			(4
				(self changeState: 0)	
			)
			
		)
	)
)
				
(procedure (dialogTrack)
	(= messagePrint (RoomScript state?))
)
(procedure (PrintOther textRes textResIndex)
	(if (> (gEgo y?) 120)
		(Print textRes textResIndex	#width 280 #at -1 10)
	else
		(Print textRes textResIndex	#width 280 #at -1 140)
	)
)

(procedure (PrintMan textRes textResIndex)
	(= gWndColor 7)
	(= gWndBack 1)
	(if conversationMode
		(Print textRes textResIndex		
			#width 160
			#at 140 130	
			#dispose
			#title "You say:"
		)
		(= message 1)
		(actionEgo view: 251 loop: 1 cel: 0 setCycle: End cycleSpeed: 3)
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
(procedure (PrintEnchantress textRes textResIndex)
	(= gWndColor 14)
	(= gWndBack 1)
	(if conversationMode
		(Print textRes textResIndex		
			#width 160
			#at 10 120	
			#title "Woman:"
			#dispose
		)
		(= message 1)
		;(enchantress view: 250 loop: 5 cel: 0 setCycle: End cycleSpeed: 3)
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
			#at 140 20	
			#title "Leah"
			#dispose
		)
		(= message 1)
		(leah view: 373 loop: 1 cel: 0 setCycle: End cycleSpeed: 3)
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
(procedure (PrintLeahWhisper textRes textResIndex)
	(= gWndColor 14)
	(= gWndBack 5)
	(if conversationMode
	else
		(Print textRes textResIndex
		#width 160
		#at 160 10
		#title "Leah whispers:"
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
(instance blastOne of Act
	(properties
		y 180
		x 200
		view 96		
	)
)
(instance blastTwo of Act
	(properties
		y 180
		x 200
		view 96		
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
		y 90
		x 90
		view 1
	)
)
(instance actionEgo of Prop
	(properties
		y 100
		x 190
		view 374
		loop 1
	)
)
(instance enchantress of Act
	(properties
		y 120
		x 200
		view 345
		loop 4
	)
)
(instance rock of Prop
	(properties
		y 111
		x 90
		view 805
		loop 2
	)
)
(instance wagon of Prop
	(properties
		y 118
		x 200
		view 95
		loop 1
	)
)
(instance brokenWheel of Prop
	(properties
		y 95
		x 210
		view 95
		loop 2
		cel 1
	)
)