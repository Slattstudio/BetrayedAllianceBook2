;;; Sierra Script 1.0 - (do not remove this comment)
(script# 334)
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
	rm334 0	
)

(local
	message = 0	
	messagePrint = 0
	
	moveAction = 2
	cutsceneWait = 0
)

(instance rm334 of Rm
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
		
		(actionEgo init: ignoreActors: ignoreControl: ctlWHITE setScript: cutsceneWaitScript)
		(injuredEgo init: setCycle: Fwd cycleSpeed: 3 setScript: convoScript)
		
		(treeOne init: )
		(treeTwo init:)
		
		(gTheMusic number: 81 loop: -1 priority: -1 play:)	
	)
)

(instance RoomScript of Script
	(properties)
	
	(method (doit)
		(super doit:)
		; code executed each game cycle
	)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		; handle Said's, etc...
		(if (or (== (pEvent message?) KEY_RETURN) (if (== (pEvent message?) 3) )) ; pressed right arrow
			(if message
				(if (not cutsceneWait)
					(if gPrintDlg
						(gPrintDlg dispose:)
						(= message 0)
						;(FormatPrint "moveAction %u \n\nmessagePrint %u" moveAction messagePrint)						
						;(convoScript cycles: 0 cue:)
						(if (< messagePrint moveAction)
							(convoScript cue:)	
						)
							
						(dialogTrack)
						;(FormatPrint "moveAction %u \n\nmessagePrint %u" moveAction messagePrint)
						
						(if (== moveAction messagePrint)
							(RoomScript cue:)
							(++ moveAction)	
							(= cutsceneWait 1)
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
	
		(if (== (pEvent message?) KEY_ESCAPE)	; Pressed Escape
			(escapeScript changeState: 1)
		)
	)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0	(= cycles 10) ; Handle state changes
			)
			(1
				(actionEgo setCycle: End self cycleSpeed: 3)	
			)
			(2
				(actionEgo view: 318 loop: 6 cel: 0 setCycle: End self cycleSpeed: 2)		
			)
			(3	
				(convoScript changeState: 1)	
			)
			(4
				(actionEgo view: 318 setCycle: Walk cycleSpeed: 0 setMotion: MoveTo (+ (injuredEgo x?) 15) (injuredEgo y?) self)	
			)
			(5	
				(cutsceneWaitScript cue:)
			)
			(6
				(actionEgo setMotion: MoveTo (- (injuredEgo x?) 30) (injuredEgo y?) self)		
			)
			(7	(= cycles 10)
				(actionEgo loop: 7 setCycle: Fwd cycleSpeed: 3)
					
			)
			(8	
				(cutsceneWaitScript cue:)
			)
			(9	(= cycles 10)
			)
			(10
				(cutsceneWaitScript cue:)
			)
			(11
				(gRoom newRoom: 336)
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
				(PrintLeah 0)	
			)
			(2
				(PrintLeah 2)	
			)
			(3
				(PrintLeah 3)	
			)
			(4
				(PrintLeah 4)	
			)
			(5
				;(PrintLeah 0)
			)
		)
	)
)

(procedure (dialogTrack)
	(= messagePrint (convoScript state?))
)

(procedure (PrintLeah textResIndex)
	(= gWndColor 14)
	(= gWndBack 5)
	(if gPrintDlg
		(gPrintDlg dispose:)
	)
	(Print scriptNumber textResIndex		
		#width 200
		#at 50 50
		#title "Leah says:"
		;#font 4
		#dispose
	)
	(= message 1)
	(= gWndColor 0)
	(= gWndBack 15)
)

(instance actionEgo of Act
	(properties
		y 135
		x 130
		view 310
		loop 4
	)
)
(instance injuredEgo of Prop
	(properties
		y 140
		x 70
		view 379
	)
)
(instance treeOne of Prop
	(properties
		y 80
		x 60
		view 999
		loop 4
	)
)
(instance treeTwo of Prop
	(properties
		y 70
		x 200
		view 999
		loop 5
	)
)