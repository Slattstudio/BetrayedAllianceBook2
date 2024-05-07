;;; Sierra Script 1.0 - (do not remove this comment)

(script# 250)
(include sci.sh)
(include game.sh)
(use controls)
(use cycle)
(use feature)
(use game)
(use inv)
(use main)
(use obj)
(use window)

(public
	rm250 0
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
)

(instance rm250 of Rm
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
				(gEgo posn: 56 172 loop: 3)
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
		
		(= gEgoMovementType 0)
		(RunningCheck)
	)
)

(instance RoomScript of Script
	(properties)
	
	(method (changeState mainState)
		(= state mainState)
		(switch state
			(0 (= cycles 1))
			(1
				(ProgramControl)
				(gEgo setMotion: MoveTo 56 163 RoomScript)
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
	                                                              ; Pointer Cursor
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if (& (pEvent modifiers?) emRIGHT_BUTTON)
				(if
					(and
						(> (pEvent x?) 47)    ; Puzzle board
						(< (pEvent x?) 72)
						(> (pEvent y?) 105)
						(< (pEvent y?) 121)
					)
					(gameSwitch)
				)
			)
			(if gameOn
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
			)
		)
	)
	
; ++innerCel
;                ++outerCel
;                (if (== innerCel 6)
;                    = innerCel 0
;                )
;                (if (== outerCel 8)
;                    = outerCel 0
;                )
	(method (doit)
		(super doit:)
		(if (& (gEgo onControl:) ctlMAROON)
			(gRoom newRoom: 249)
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
			(= myEvent (Event new: evNULL))
			(trigger posn: (myEvent x?) (- (myEvent y?) 7))
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
				)
				(else                                                 ; Pointer Cursor
					(= onNothing 1)
					(= onBlue 0)
					(= onSilver 0)
					(= onGrey 0)
					(circleLarge loop: 0)
					(circleSmall loop: 0)
					(circleMiddle loop: 0)
					(if (> (trigger x?) 110)
						(SetCursor 985 (HaveMouse))
						(= gCurrentCursor 985)
					else                                                  ; Close Cursor
						(SetCursor 999 (HaveMouse))
						(= gCurrentCursor 999)
					)
				)
			)                                                             ; Pointer Cursor
			(myEvent dispose:)
		)
	)
)


(procedure (gameSwitch)
	(if (not gameOn)
		(= gameOn 1)
		(= gMap 1)
		(circleSmall show:)
		(circleMiddle show:)
		(circleLarge show:)
		(lightSmall show:)
		(lightMiddle show:)
		(lightLarge show:)
		(energySmall show:)
		(energyBig show:)
	else
		(= gameOn 0)
		(= gMap 0)
		(= onNothing 0)
		(circleSmall hide:)
		(circleMiddle hide:)
		(circleLarge hide:)
		(lightSmall hide:)
		(lightMiddle hide:)
		(lightLarge hide:)
		(energySmall hide:)
		(energyBig hide:)
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
		x 200
		view 589
		loop 0
	)
)

(instance circleMiddle of Prop
	(properties
		y 130
		x 200
		view 590
		loop 0
	)
)

(instance circleLarge of Prop
	(properties
		y 145
		x 200
		view 591
		loop 0
	)
)

(instance lightSmall of Prop
	(properties
		y 116
		x 200
		view 589
		loop 2
	)
)

(instance lightMiddle of Prop
	(properties
		y 130
		x 200
		view 590
		loop 2
	)
)

(instance lightLarge of Prop
	(properties
		y 145
		x 200
		view 591
		loop 2
	)
)

(instance energySmall of Prop
	(properties
		y 117
		x 200
		view 592
		loop 0
	)
)

(instance energyBig of Prop
	(properties
		y 131
		x 200
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
