;;; Sierra Script 1.0 - (do not remove this comment)
(script# 336)
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
	
	rm336 0
	
)
(local
	pullTalkNum = 10
	beingPulled = 1	
	message = 0
	messagePrint = 0
	
	moveAction = 2
	cutsceneWait = 1
)

(instance rm336 of Rm
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
		
		(injuredEgo init: hide: setCycle: Fwd cycleSpeed: 6 setScript: convoScript) 
		(dress init: hide: ignoreActors: setPri: 15 setScript: cutsceneWaitScript)
		(actionEgo init: ignoreActors: ignoreControl: ctlWHITE)
		(mushroom init: ignoreActors:)
		(pulledEgo init: ignoreActors: ignoreControl: ctlWHITE setScript: pullScript)
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
							(if beingPulled
								(pullScript cue:)	
							else
								(RoomScript cue:)
							)
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
						(-- messagePrint)
						(= message 0)															
						(convoScript cycles: 0 changeState: messagePrint)
						(dialogTrack)
					)
				)
			)
		)
		
		(if (== (pEvent message?) KEY_ESCAPE)	; Pressed Escape
			(escapeScript changeState: 1)
		)
		; handle Said's, etc...
	)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0 ;(= cycles 5) ; Handle state changes
			)
			(1
				;(PrintLeah 0)
				(actionEgo view: 318 setCycle: Walk cycleSpeed: 0 xStep: 3 setMotion: MoveTo (+ (actionEgo x?) 50) (- (actionEgo y?) 10) self)
			)
			(2
				(actionEgo view: 318 setCycle: Walk cycleSpeed: 0 setMotion: MoveTo (mushroom x?) (actionEgo y?) self)
			)
			(3	
				(actionEgo view: 334 loop: 4 cel: 0 setCycle: End cutsceneWaitScript cycleSpeed: 3)
				
			)
			(4	
				;(PrintLeah 1)
				(mushroom hide:)
				(actionEgo setCycle: Beg self)	
			)
			(5	; removing dress
				;(PrintLeah 2)
				(actionEgo view: 316 loop: 8 setCycle: End self cycleSpeed: 3)	
			)
			(6
				(actionEgo loop: 9 cel: 0 setCycle: End self )	
			)
			(7	;(= cycles 10)
				(actionEgo loop: 10)
				(dress show:)
				(cutsceneWaitScript cue:)	
			)
			(8
				(actionEgo view: 334 setCycle: Walk cycleSpeed: -1 setMotion: MoveTo (+ (injuredEgo x?) 70) (+ (actionEgo y?) 1) cutsceneWaitScript)	
			)
			(9
				;(PrintLeah 3)
				(actionEgo view: 344 loop: 0 cel: 0 setCycle: End self cycleSpeed: 2)	
			)
			(10	(= cycles 10)
				(actionEgo loop: 1 setCycle: Fwd)
				;(cutsceneWaitScript cue:)	
			)
			(11
				;(PrintLeah 4)
				(actionEgo view: 343 setCycle: Walk cycleSpeed: -1 setMotion: MoveTo (+ (injuredEgo x?) 15) (+ (actionEgo y?) 5) cutsceneWaitScript)	
			)
			(12
				(actionEgo view: 450 setCycle: End cutsceneWaitScript cycleSpeed: 2)
			)
			(13	(= cycles 1)
				;(PrintLeah 5)					
			)
			(14				
				;(PrintLeah 7)
				(cutsceneWaitScript cue:)			
			)
			(15	
				(actionEgo setCycle: Beg cutsceneWaitScript)	
			)
			(16	(= cycles 5)
				(DrawPic 900 0 1 0)
				(gTheMusic fade:)
					
				(injuredEgo hide:)
				(dress hide:)
				(actionEgo hide:)
			)
			(17	
				(= gWndColor 14)
				(= gWndBack 1)
				(Print "Before long, blades of sunlight cut through the canopy above." #width 200)
				(Print "A new adventure is about to begin." #width 206)
				(= gWndColor 15)
				(= gWndBack 0)
				(gRoom newRoom: 236)
			)
		)
	)
)

(instance pullScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0	(= cycles 10)
			)
			(1
				(actionEgo posn: (- (pulledEgo x?) 20) (+ (pulledEgo y?) 5) view: 380 setCycle: Walk cycleSpeed: 2 xStep: 2 setMotion: MoveTo (- (actionEgo x?) 33) (pulledEgo y?))
				(pulledEgo xStep: 2  setMotion: MoveTo (- (pulledEgo x?) 33) (- (pulledEgo y?) 5) self setCycle: Fwd cycleSpeed: 3)	
				(if (== messagePrint 0)
					(convoScript cue:)
					;(++ pullTalkNum)
					;(dialogTrack)
				)
			)
			(2	
				(actionEgo setMotion: NULL loop: 2 cel: 0 setCycle: Fwd cycleSpeed: 2)
				(cutsceneWaitScript cue:)
			)
			(3	(= cycles 1)
				(if (> (actionEgo x?) (injuredEgo x?))
					;(convoScript cue:)
					;(PrintLeahDispose pullTalkNum)
					;(++ pullTalkNum)
					
				else
					(RoomScript changeState: 1)
					(pulledEgo hide:)
					(injuredEgo show:)
					(= beingPulled 0)
					(self cycles: 0)
				)	
			)
			(4
				(self changeState: 1)
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
				(PrintLeahDispose 10)	
			)
			(2
				(PrintLeahDispose 11)	
			)
			(3
				(PrintLeahDispose 12)
			)
			(4
				(PrintLeahDispose 13)	
			)
			(5
				(PrintLeahDispose 14)	
			)
			(6
				(PrintLeahDispose 15)
			)
			(7
				(PrintLeahDispose 0)	
			)
			(8
				(PrintLeahDispose 1)	
			)
			(9
				(PrintLeahDispose 2)
			)
			(10
				(PrintLeahDispose 3)	
			)
			(11
				(PrintLeahDispose 4)	
			)
			(12
				(PrintLeahDispose 5)
			)
			(13
				(if gPrintDlg
					(gPrintDlg dispose:)
				)
				(Print scriptNumber 6 #at -1 130 #dispose)
				(= message 1)	
			)
			(14
				(PrintLeahDispose 7)
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
	(Print scriptNumber textResIndex		
		#width 200
		#at -1 140
		#title "Leah says:"
	)
	(= gWndColor 0)
	(= gWndBack 15)
)
(procedure (PrintLeahDispose textResIndex)
	(= gWndColor 14)
	(= gWndBack 5)
	(Print scriptNumber textResIndex		
		#width 200
		#at -1 140
		#title "Leah says:"
		#dispose
	)
	(= gWndColor 0)
	(= gWndBack 15)
	(= message 1)
)

(instance actionEgo of Act
	(properties
		y 130
		x 290
		view 318
	)
)
(instance pulledEgo of Act
	(properties
		y 125
		x 310
		view 379
	)
)
(instance dress of Prop
	(properties
		y 68
		x 255
		view 316
		loop 7
	)
)
(instance injuredEgo of Prop
	(properties
		y 90
		x 70
		view 412
		loop 6
	)
)
(instance mushroom of Prop
	(properties
		y 45
		x 240
		view 002
		loop 1
	)
)
