;;; Sierra Script 1.0 - (do not remove this comment)
(script# 261)
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
	
	rm261 0
	
)

(local


	innerCel =  0
	outerCel =  0
	innerVar
	outerVar
	smallCel =  0
	middleCel =  0
	largeCel =  0
	onBlue =  0
	onSilver =  0
	onGrey =  0
	onNothing =  0
	myEvent
	
	gameOn =  0
	
	clickOff = 0
	whichCircle = 0 ; used for up,down,left,right controls
	
	timerVar = 0
)


(instance rm261 of Rm
	(properties
		picture scriptNumber
		north 0
		east 0
		south 247
		west 0
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript)
		(switch gPreviousRoomNumber
			(247 
				(gEgo posn: 262 170 loop: 3)
			)
			(else 
				(gEgo posn: 262 170 loop: 3)
			)
		)
		(SetUpEgo)
		(gEgo init:)
		
		(circleSmall init: hide:)
		(circleMiddle init: hide:)
		(circleLarge init: hide:)
		(lightSmall init: hide: setPri: 14)
		(lightMiddle init: hide: setPri: 14)
		(lightLarge init: hide: setPri: 14)
		(energySmall init: hide: setPri: 14)
		(energyBig init: hide: setPri: 14)
		(trigger init: hide: ignoreActors:)
		(exit init: hide: setPri: 15)
		
		(= gEgoMovementType 0)
		(RunningCheck)
		
		(if (and
			(== [g261Points 0] 1)
			(== [g261Points 1] 5)
			(== [g261Points 2] 6))
		
			(= timerVar 35)	; don't give message that puzzle solved if used again
		)
	)
)

(instance RoomScript of Script
	(properties)
	
	(method (doit)
		(super doit:)
		; code executed each game cycle
		(if (& (gEgo onControl:) ctlMAROON)
			(gRoom newRoom: 247)
		)
		
		(lightSmall cel: smallCel)
		(lightMiddle cel: middleCel)
		(lightLarge cel: largeCel)
		(findInnerCurve)
		(energySmall loop: smallCel cel: innerVar)
		(findOuterCurve)
		(energyBig loop: middleCel cel: outerVar)
; (lightMiddle:cel(innerCel))
;        (lightLarge:cel(outerCel))
		(if gameOn
			(if (not clickOff)
				(= myEvent (Event new: evNULL))
				(trigger posn: (myEvent x?) (- (myEvent y?) 7))
				(if
					(and
						(> (myEvent x?) (- (exit nsLeft?) 6))
						(< (myEvent x?) (exit nsRight?))
						(> (myEvent y?) (exit nsTop?))
						(< (myEvent y?) (+ (exit nsBottom?) 12))
					)
						(exit cel: 1)
				else
					(exit cel: 0)
				)
				(cond 
					((& (trigger onControl:) ctlBLUE)
						(= onNothing 0)
						(= onBlue 1)
						(= onSilver 0)
						(= onGrey 0)
						(circleSmall loop: 1)
						(circleMiddle loop: 0)
						(circleLarge loop: 0)
						(SetCursor 999 (HaveMouse))
						(= gCurrentCursor 999)
						(= whichCircle 1)
					)
					((& (trigger onControl:) ctlSILVER)           ; Pointer Cursor
						(= onNothing 0)
						(= onBlue 0)
						(= onSilver 1)
						(= onGrey 0)
						(circleMiddle loop: 1)
						(circleSmall loop: 0)
						(circleLarge loop: 0)
						(SetCursor 999 (HaveMouse))
						(= gCurrentCursor 999)
						(= whichCircle 2)
					)
					((& (trigger onControl:) ctlGREY)                 ; Pointer Cursor
						(= onNothing 0)
						(= onBlue 0)
						(= onSilver 0)
						(= onGrey 1)
						(circleLarge loop: 1)
						(circleSmall loop: 0)
						(circleMiddle loop: 0)
						(SetCursor 999 (HaveMouse))
						(= gCurrentCursor 999)
						(= whichCircle 3)
					)
					(else                                                 ; Pointer Cursor
						(= onNothing 1)
						(= onBlue 0)
						(= onSilver 0)
						(= onGrey 0)
						(circleLarge loop: 0)
						(circleSmall loop: 0)
						(circleMiddle loop: 0)
						(if (> (trigger x?) 160)
							(SetCursor 985 (HaveMouse))
							(= gCurrentCursor 985)
						else                                                  ; Close Cursor
							(SetCursor 999 (HaveMouse))
							(= gCurrentCursor 999)
						)
					)
				)                                                             ; Pointer Cursor
				(myEvent dispose:)
				(= [g261Points 0] smallCel)
				(= [g261Points 1] middleCel)
				(= [g261Points 2] largeCel)
			else
				(SetCursor 998 (HaveMouse))
				(= gCurrentCursor 998)
			)
		)
		(if (and
			(== [g261Points 0] 1)
			(== [g261Points 1] 5)
			(== [g261Points 2] 6))
			
			(if (< timerVar 35)
				(++ timerVar)
				(if (== timerVar 35)
					(if gameOn
						(PrintOther 261 2) ; "You hear a strange sound off in the distance."
					)	
				)
			)
		else
			(= timerVar 0)
		)
	)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		
		(if (== (pEvent type?) evKEYBOARD)
			(if (or  (== (pEvent message?) KEY_x) (== (pEvent message?) KEY_RETURN))
				(if gameOn
					(self changeState: 3)	
				)
			)
		)
		(if gameOn
			(switch (pEvent message?)
				(1	; up
					(++ whichCircle)
					(if (> whichCircle 3)
						(= whichCircle 1)
					)
					(= clickOff 1)
					(switch whichCircle
						(1
							(circleSmall loop: 1)
							(circleMiddle loop: 0)
							(circleLarge loop: 0)
						)
						(2
							(circleSmall loop: 0)
							(circleMiddle loop: 1)
							(circleLarge loop: 0)
						)
						(3
							(circleSmall loop: 0)
							(circleMiddle loop: 0)
							(circleLarge loop: 1)
						)
					)	
				)
				
				(3	; right
					(switch whichCircle
						(1
							(++ smallCel)
							(if (> smallCel 3)
								(= smallCel 0)
							)
						)
						(2
							(++ middleCel)
							(if (> middleCel 5)
								(= middleCel 0)
							)
						)
						(3
							(++ largeCel)
							(if (> largeCel 7)
								(= largeCel 0)
							)
						)
					)
					(= clickOff 1)
				)
				(5	; down
					(-- whichCircle)
					(if (< whichCircle 1)
						(= whichCircle 3)
					)
					(switch whichCircle
						(1
							(circleSmall loop: 1)
							(circleMiddle loop: 0)
							(circleLarge loop: 0)
						)
						(2
							(circleSmall loop: 0)
							(circleMiddle loop: 1)
							(circleLarge loop: 0)
						)
						(3
							(circleSmall loop: 0)
							(circleMiddle loop: 0)
							(circleLarge loop: 1)
						)
					)
					(= clickOff 1)
				)
				(7	; left
					(switch whichCircle
						(1
							(-- smallCel)
							(if (< smallCel 0)
								(= smallCel 3)
							)
						)
						(2
							(-- middleCel)
							(if (< middleCel 0)
								(= middleCel 5)
							)
						)
						(3
							(-- largeCel)
							(if (< largeCel 0)
								(= largeCel 7)
							)
						)
					)
					(= clickOff 1)
				)	
			)
		)
				
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if (& (pEvent modifiers?) emRIGHT_BUTTON)
				(if
					(and
						(> (pEvent x?) 249) (< (pEvent x?) 272) (> (pEvent y?) 105) (< (pEvent y?) 121)) ; Puzzle board
					(= smallCel [g261Points 0] )
					(= middleCel [g261Points 1])
					(= largeCel [g261Points 2])
					(gameSwitch)
				)
				
			)
			(if gameOn
				(if (not clickOff)
					(if onBlue
						(++ smallCel)
						(if (== smallCel 4) (= smallCel 0))
					)
					(if onSilver
						(++ middleCel)
						(if (== middleCel 6) (= middleCel 0))
					)
					(if onGrey
						(++ largeCel)
						(if (== largeCel 8) (= largeCel 0))
					)
					(if onNothing (RoomScript changeState: 3))
				else
					(= clickOff 0)
				)
			)
			
		)
		; handle Said's, etc...
		
		(if (Said 'look,use,touch,move,turn/puzzle,stone,inlay,pedestal')
			(gameSwitch)
		)
		(if (Said 'look>')
			(if (Said '/pedestal')
				(PrintOther 261 1)	
			)
			(if (Said '/forest,verlorn')
				(PrintOther 0 105)	
			)
			(if (Said '[/!*]')
				(PrintOther 261 0)
			)
		)
	)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0 (= cycles 1))
			(1
				(ProgramControl)
				(gEgo setMotion: MoveTo 262 155 RoomScript)
			)
			(2 (PlayerControl))
			(3 (= cycles 1))
			(4
				(gameSwitch)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
			)
		)
	)
)
(procedure (PrintOther textRes textResIndex)

	(Print textRes textResIndex
		#width 290
		#at -1 10
	)
)
(procedure (gameSwitch)
	(if (not gameOn)
		(= gameOn 1)
		(= gMap 1)
		(= gArcStl 1)
		(circleSmall show:)
		(circleMiddle show:)
		(circleLarge show:)
		(lightSmall show:)
		(lightMiddle show:)
		(lightLarge show:)
		(energySmall show:)
		(energyBig show:)
		(exit show:)
		
		(if (and
			(== [g261Points 0] 1)
			(== [g261Points 1] 5)
			(== [g261Points 2] 6))
			(= timerVar 36)
		)
	else
		(= gameOn 0)
		(= gMap 0)
		(= onNothing 0)
		(= gArcStl 0)
		
		(= [g261Points 0] smallCel)
		(= [g261Points 1] middleCel)
		(= [g261Points 2] largeCel)
				
		(circleSmall hide:)
		(circleMiddle hide:)
		(circleLarge hide:)
		(lightSmall hide:)
		(lightMiddle hide:)
		(lightLarge hide:)
		(energySmall hide:)
		(energyBig hide:)
		(exit hide:)
		(SetCursor 999 (HaveMouse))
		(= gCurrentCursor 999)
	)
)

(procedure (findInnerCurve)
	(switch smallCel
		(0
			(switch middleCel
				(0 (= innerVar 0))
				(1 (= innerVar 1))
				(2 (= innerVar 2))
				(3 (= innerVar 3))
				(4 (= innerVar 4))
				(5 (= innerVar 5))
			)
		)
		(1
			(switch middleCel
				(0 (= innerVar 4))
				(1 (= innerVar 5))
				(2 (= innerVar 0))
				(3 (= innerVar 1))
				(4 (= innerVar 2))
				(5 (= innerVar 3))
			)
		)
		(2
			(switch middleCel
				(0 (= innerVar 3))
				(1 (= innerVar 4))
				(2 (= innerVar 5))
				(3 (= innerVar 0))
				(4 (= innerVar 1))
				(5 (= innerVar 2))
			)
		)
		(3
			(switch middleCel
				(0 (= innerVar 1))
				(1 (= innerVar 2))
				(2 (= innerVar 3))
				(3 (= innerVar 4))
				(4 (= innerVar 5))
				(5 (= innerVar 0))
			)
		)
	)
)

(procedure (findOuterCurve)
	; (var x)
	(switch middleCel
		(0
			(switch largeCel
				(0 (= outerVar 0))
				(1 (= outerVar 1))
				(2 (= outerVar 2))
				(3 (= outerVar 3))
				(4 (= outerVar 4))
				(5 (= outerVar 5))
				(6 (= outerVar 6))
				(7 (= outerVar 7))
			)
		)
		(1
			(switch largeCel
				(0 (= outerVar 7))
				(1 (= outerVar 0))
				(2 (= outerVar 1))
				(3 (= outerVar 2))
				(4 (= outerVar 3))
				(5 (= outerVar 4))
				(6 (= outerVar 5))
				(7 (= outerVar 6))
			)
		)
		(2
			(switch largeCel
				(0 (= outerVar 5))
				(1 (= outerVar 6))
				(2 (= outerVar 7))
				(3 (= outerVar 0))
				(4 (= outerVar 1))
				(5 (= outerVar 2))
				(6 (= outerVar 3))
				(7 (= outerVar 4))
			)
		)
		(3
			(switch largeCel
				(0 (= outerVar 4))
				(1 (= outerVar 5))
				(2 (= outerVar 6))
				(3 (= outerVar 7))
				(4 (= outerVar 0))
				(5 (= outerVar 1))
				(6 (= outerVar 2))
				(7 (= outerVar 3))
			)
		)
		(4
			(switch largeCel
				(0 (= outerVar 3))
				(1 (= outerVar 4))
				(2 (= outerVar 5))
				(3 (= outerVar 6))
				(4 (= outerVar 7))
				(5 (= outerVar 0))
				(6 (= outerVar 1))
				(7 (= outerVar 2))
			)
		)
		(5
			(switch largeCel
				(0 (= outerVar 1))
				(1 (= outerVar 2))
				(2 (= outerVar 3))
				(3 (= outerVar 4))
				(4 (= outerVar 5))
				(5 (= outerVar 6))
				(6 (= outerVar 7))
				(7 (= outerVar 0))
			)
		)
	)
)

(instance circleSmall of Prop
	(properties
		y 116
		x 100
		view 589
		loop 0
	)
)

(instance circleMiddle of Prop
	(properties
		y 130
		x 100
		view 590
		loop 0
	)
)

(instance circleLarge of Prop
	(properties
		y 145
		x 100
		view 591
		loop 0
	)
)

(instance lightSmall of Prop
	(properties
		y 116
		x 100
		view 589
		loop 2
	)
)

(instance lightMiddle of Prop
	(properties
		y 130
		x 100
		view 590
		loop 2
	)
)

(instance lightLarge of Prop
	(properties
		y 145
		x 100
		view 591
		loop 2
	)
)

(instance energySmall of Prop
	(properties
		y 117
		x 100
		view 592
		loop 0
	)
)

(instance energyBig of Prop
	(properties
		y 131
		x 100
		view 593
		loop 0
	)
)

(instance trigger of Act
	(properties
		y 1
		x 1
		view 981
	)
)

(instance exit of Prop
	(properties
		y 30
		x 100
		view 998
		loop 2
	)
)
