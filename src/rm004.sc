;;; Sierra Script 1.0 - (do not remove this comment)
(script# 4)
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
	rm004 0
)

(local
	
	upDown = 0	
	riding = 0
	badRiding = 0
	inForest = 0
	movingRight = 1
	
	visible = 1
	
	message = 0
	
	conversationMode = 0 ; used when characters are talking in the handleEvent Method
	messagePrint = 0
)

(instance rm004 of Rm
	(properties
		picture scriptNumber
		north 0
		east 0
		south 0
		west 0
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript)
		(switch gPreviousRoomNumber
			(else 
				(gEgo posn: 150 100 loop: 1)
			)
		)
		(SetUpEgo)
		(gEgo init: hide: setScript: escapeScript)
		(leah init: hide: setScript: convoScript)
		(egoRiding init: hide:)
		(horse init: ignoreControl: ctlWHITE ignoreActors: setPri: 5 hide:)
		
		(badRider init: hide:)
		(badHorse init: ignoreControl: ctlWHITE ignoreActors: setPri: 5 hide:)
		
		(soldier init:)
		(soldierSitting init: setCycle: Fwd cycleSpeed: 6)
	)
)

(instance RoomScript of Script
	(properties)
	
	(method (doit)
		(super doit:)
		; code executed each game cycle
		(cond 
			(riding
				(if movingRight
					(if (> upDown 2)
						(leah init: show: loop: 4	posn: (+ (horse x?) 3) (- (horse y?) 17) setPri: 6)
						(egoRiding show: loop: 0 posn: (- (horse x?) 6) (- (horse y?) 25) setPri: 6)
					else
						(leah init: show: loop: 4 posn: (+ (horse x?) 3) (- (horse y?) 16) setPri: 6)
						(egoRiding 	show: loop: 0 posn: (- (horse x?) 6) (- (horse y?) 24) setPri: 6)
					)
				else
					(leah init: show: loop: 4 posn: (horse x?) 2 (- (horse y?) 17) setPri: 6)
				)
			)
		)
		(if badRiding
			(if (> upDown 2)
				(badRider show: posn: (- (badHorse x?) 6) (- (badHorse y?) 24) setPri: 6)
			else
				(badRider show: posn: (- (badHorse x?) 6) (- (badHorse y?) 23) setPri: 6)
			)
			(if (and (> (badHorse x?) 56) (< (badHorse x?) 143))
				(badRider loop: 3)	
			else
				(badRider loop: 1)
			)	
		)
		(++ upDown)
		(if (not visible)
			(egoRiding hide:)
			(leah hide:)	
		)
		(if (< (horse x?)(soldier x?))
			(if (< (convoScript state?) 4)
				(convoScript cue:)
			)
		)
	)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		; handle Said's, etc...
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
			(if (> messagePrint 7)
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
		(if (== (pEvent message?) KEY_ESCAPE)	; Pressed Escape
			(escapeScript changeState: 1)
		)
	)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0
			)
			(1
					
			)
			(10 ; Handle state changes
				(= riding 1)
				(horse show: setCycle: Walk xStep: 8 setMotion: MoveTo 1 85 self)
			)
			(11
				(horse hide:)
				(= visible 0)
				(convoScript cue:)
			)
		)
	)
)
(instance convoScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0	(= cycles 10)
				;(ProgramControl)
			)
			(1	(= cycles 50)
				(PrintSoldier 0)
				(soldier loop: 3 cel: 0 setCycle: End cycleSpeed: 3)			
			)
			(2	(= cycles 50)
				(if gPrintDlg
						(gPrintDlg dispose:)
				)
				(PrintSoldier 1)
				(soldier loop: 4 cel: 0 setCycle: Fwd)	
				(soldierSitting loop: 5 cel: 0 setCycle: End cycleSpeed: 3)		
			)
			(3
				(soldier loop: 3 cel: 2 setCycle: Beg)
				(RoomScript changeState: 10)
				(if gPrintDlg
						(gPrintDlg dispose:)
				)	
			)
			(4
				(soldier loop: 6 cel: 0 setCycle: CT)	
			)
			(5	(= cycles 30)
				(PrintSoldierRiding 2)	
			)
			(6
				(if gPrintDlg
					(gPrintDlg dispose:)
				)
				(badHorse show: setCycle: Walk xStep: 8 setMotion: MoveTo 1 85 self)
				(= badRiding 1)	
			)
			(7	
				(= conversationMode 1)
				(= badRiding 0)
				(badHorse hide:)
				(badRider hide:)
				
				(if gPrintDlg
					(gPrintDlg dispose:)
				)
				
				(soldier setCycle: Fwd)	
				(PrintSoldier 3)
			)
			(8	
				(PrintSoldierSitting 4)
				(soldierSitting loop: 7 setCycle: Fwd)
				(soldier setCycle: CT)		
			)
			(9 
				(soldier setCycle: Fwd)	
				(soldierSitting setCycle: CT)
				(PrintSoldier 5)	
			)
			(10	(= cycles 20)
				(soldier setCycle: CT)		
			)
			(11	
				(PrintSoldierSitting 6)
				(soldierSitting loop: 7 setCycle: Fwd)
				(soldier setCycle: CT)		
			)
			(12 (= cycles 10)
				(= conversationMode 0)	
				(soldierSitting setCycle: CT)
			)
			(13
				(gRoom newRoom: 900)	
			)			
		)
	)
)
(instance escapeScript of Script
	(properties)
	
	(method (changeState newState button)
		(= state newState)
		(switch state
			(0
			)
			(1	(= cycles 1)
				(PlayerControl)	; this should make the cursor visible
			)
			(2
				(= gWndColor 0)
				(= gWndBack 14)
				(= button (Print 997 10 #button { Yes_} 1 #button { No_} 0 #font 4 #at -1 10))
				(= gWndColor 0)
				(= gWndBack 15)
				
				(switch button
					(0
					(self cue:)
					)
					(1 
						(gRoom newRoom: 236)
					)
				)	
			)
			(3	; set the cursor invisible again
				(SetCursor 998 (HaveMouse))
				(= gCurrentCursor 998)	
			)
		)
	)
)
(procedure (dialogTrack)
	(= messagePrint (convoScript state?))
)
(procedure (PrintSoldier  textResIndex)
	(= gWndColor 7)
	(= gWndBack 1)
	;(if conversationMode
		(Print 4 textResIndex		
			#width 160
			#at 122 110	
			#dispose
			#title "Soldier 1:"
		)
		(= message 1)
		;(soldier loop: 1 cel: 0 setCycle: End cycleSpeed: 3)
	;)
	(= gWndColor 0)
	(= gWndBack 15)
)
(procedure (PrintSoldierRiding  textResIndex)
	(= gWndColor 12)
	(= gWndBack 8)
	;(if conversationMode
		(Print 4 textResIndex		
			#width 140
			#at 160 60	
			#dispose
			#title "Commander:"
		)
		(= message 1)
		;(soldierSitting loop: 0 cel: 0 setCycle: End cycleSpeed: 3)
	;)
	(= gWndColor 0)
	(= gWndBack 15)
)
(procedure (PrintSoldierSitting  textResIndex)
	(= gWndColor 1)
	(= gWndBack 11)
	;(if conversationMode
		(Print 4 textResIndex		
			#width 140
			#at 160 60	
			#dispose
			#title "Fletch:"
		)
		(= message 1)
		(soldierSitting loop: 0 cel: 0 setCycle: End cycleSpeed: 3)
	;)
	(= gWndColor 0)
	(= gWndBack 15)
)

(instance badHorse of Act
	(properties
		y 125
		x 300
		view 327
	)
)
(instance badRider of Prop
	(properties
		y 150
		x 38
		view 332
		loop 1
	)
)
(instance horse of Act
	(properties
		y 125
		x 300
		view 325
	)
)

(instance leah of Prop
	(properties
		y 150
		x 38
		view 318
		loop 4
	)
)

(instance egoRiding of Prop
	(properties
		y 150
		x 38
		view 427
	)
)
(instance soldier of Prop
	(properties
		y 120
		x 105
		view 335
		loop 1
	)
)
(instance soldierSitting of Prop
	(properties
		y 90
		x 140
		view 335
		loop 2
	)
)