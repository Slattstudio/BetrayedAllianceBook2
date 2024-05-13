;;; Sierra Script 1.0 - (do not remove this comment)
(script# 76)
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
	rm076 0	
)
(local
	
	message = 0
	
	conversationMode = 0 ; used when characters are talking in the handleEvent Method
	messagePrint = 0
	
	tutorialMessage = 0
	
	noBack = 0	; prevent player from pressing back for dialog
	

)

(instance rm076 of Rm
	(properties
		picture 14
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
		
		(soldier init: hide:)
		(soldierSitting init: hide: )
		
		;(Animate (DrawPic 14 dpCLOSEREOPEN_CENTEREDGE))
		(DrawPic 14 dpCLOSEREOPEN_CENTEREDGE)
		
		(SetUpEgo)
		(gEgo init: hide: setScript: escapeScript)
		
			
	)
)

(instance RoomScript of Script
	(properties)
	
	(method (doit)
		(super doit:)
		; code executed each game cycle
		
		;(SetCursor 998 (HaveMouse))
		;(= gCurrentCursor 998)
		
		(if (< tutorialMessage 161)	
			(++ tutorialMessage)
		)
		(if (and (== tutorialMessage 160)(== (convoScript state?) 1))
			
			;(Print 76 10)
			(= gWndColor 0)
			(= gWndBack 14)
			(Print 76 10 #font 4 #at -1 10)
			(= gWndColor 0)
			(= gWndBack 15)	
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
					(= tutorialMessage 161)
				)
			)
		)
		(if (== (pEvent message?) 7)  ; pressed left arrow
			(if (not noBack)
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
		)
		(if (== (pEvent message?) KEY_ESCAPE)	; Pressed Escape
			(escapeScript changeState: 1)
		)
	)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0 (= cycles 1); Handle state changes
			)
			(1
				(= cycles 30)
				
				
				(SetCursor 998 (HaveMouse))
				(= gCurrentCursor 998)
				
			)
			(2
				(gTheMusic stop:)
				;(Animate (DrawPic scriptNumber dpOPEN_CENTEREDGE))
				(DrawPic scriptNumber dpOPEN_CENTEREDGE)
				(gTheMusic number: 76 loop: -1 priority: -1 play:)
				(soldier show: setScript: convoScript)
				(soldierSitting show:)
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
			(1
				(= conversationMode 1)	
				(PrintSoldier 0)
			)
			(2
				(PrintSoldierSitting 1)		
			)
			(3
				(PrintSoldier 2)		
			)
			(4
				(PrintSoldierSitting 3)		
			)
			(5
				(PrintSoldier 4)		
			)
			(6
				(PrintSoldierSitting 5)		
			)
			(7
				(PrintSoldierSitting 6)		
			)
			(8
				(PrintSoldier 7)	
			)
			(9
				(PrintSoldierSitting 8)		
			)
			(10
				(PrintSoldier 9)	
			)
			(11	(= cycles 20)
				(= conversationMode 0)	
				(= noBack 1)
			)
			(12	(= cycles 50)	
				(= gWndColor 11)
				(= gWndBack 1)
				(Print 76 20 #width 260 #at -1 140 #dispose)
				(= gWndColor 0)
				(= gWndBack 15)
			)
			(13 (= cycles 10)
				(DrawPic 3 8 1 0)	
			)
			(14	(= cycles 10)
				(DrawPic 4 8 1 0)	
			)
			(15
				(= message 1)
			)
			(16
				(gTheMusic stop:)
				(gTheMusic number: 77 loop: -1 priority: -1 play:)
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
						(gTheMusic fade:)
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
(procedure (PrintOther  textResIndex)
	(Print 76 textResIndex
		#width 280
		#at -1 130
	)
)
(procedure (PrintSoldier  textResIndex)
	(= gWndColor 7)
	(= gWndBack 1)
	(if conversationMode
		(Print 76 textResIndex		
			#width 160
			#at 122 110	
			#dispose
			#title "Soldier 1:"
		)
		(= message 1)
		(soldier loop: 1 cel: 0 setCycle: End cycleSpeed: 3)
	)
	(= gWndColor 0)
	(= gWndBack 15)
)
(procedure (PrintSoldierSitting  textResIndex)
	(= gWndColor 1)
	(= gWndBack 11)
	(if conversationMode
		(Print 76 textResIndex		
			#width 140
			#at 160 60	
			#dispose
			#title "Soldier 2:"
		)
		(= message 1)
		(soldierSitting loop: 0 cel: 0 setCycle: End cycleSpeed: 3)
	)
	(= gWndColor 0)
	(= gWndBack 15)
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
	)
)