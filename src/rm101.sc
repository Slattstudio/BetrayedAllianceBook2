;;; Sierra Script 1.0 - (do not remove this comment)
; Score + 5
(script# 101)
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
	
	rm101 0
	
)

(local
	
	canWalk = 0
	
	walking = 0	
	steps = 0
	questions = 0 ; Which question is player currently on? set to 11 when complete
	[whichQuestion 7] = [0 0 0 0 0 0 0]
	
	correctAnswers = 0
	
	reachedTheEnd = 0	; finished the Demo
	float = 0	; used in the doit method to allow the goddess to shift up and down at a pace more than 1 cycle
	
	goddessVisible = 0
	goddessUp = 2	; used to determine if goddess should hover up or down
	
	message = 0
	
	conversationMode = 0 ; used when characters are talking in the handleEvent Method
	messagePrint = 0
	
	cutsceneWait = 0	; if true, cannot go forward or back in the text
	noBack = 0	; cannot go backward in the text
	
)
(instance rm101 of Rm
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
		(gEgo init: hide:)
		(bigEgo init:)
		(pathWay init: setPri: 0 cycleSpeed: 2)
		(backHair init: hide: ignoreActors: setPri: 2 setCycle: Fwd cycleSpeed: 3)
		(frontHair init: hide: ignoreActors: setPri: 7 setCycle: Fwd cycleSpeed: 3 setScript: randoScript)
		(goddess init: loop: 1 cel: 0 ignoreActors: setScript: questionScript setPri: 6)
		
		
		(PlayerControl)
		(gTheMusic number: 76 loop: -1 priority: -1 play:)	
		;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
		;(RoomScript changeState: 7)
		;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
	)
)

(instance RoomScript of Script
	(properties)
	
	(method (doit)
		(super doit:)
		; code executed each game cycle
		(if walking
			(++ steps)	
		)
		(if (> steps 100)
			(if (not questions)				
				(questionScript changeState: 1)
				(= questions 1)
			)
		)
		; Goddess floating
		(if goddessVisible 
			(if (< float 3)
				(++ float)
			else			
				(switch goddessUp
					(0
						(goddess y: (+ (goddess y?) 1))
						(frontHair y: (+ (frontHair y?) 1))
						(backHair y: (+ (backHair y?) 1))
						(= goddessUp 2)
					)
					(1
						(goddess y: (+ (goddess y?) 1))
						(frontHair y: (+ (frontHair y?) 1))
						(backHair y: (+ (backHair y?) 1))
						(-- goddessUp)
					)
					(2
						(goddess y: (- (goddess y?) 1))
						(frontHair y: (- (frontHair y?) 1))
						(backHair y: (- (backHair y?) 1))
						(++ goddessUp)
					)
					(3
						(goddess y: (- (goddess y?) 1))
						(frontHair y: (- (frontHair y?) 1))
						(backHair y: (- (backHair y?) 1))
						(= goddessUp 1)
					)
				)
				(= float 0)
			)
		)
		
		;(PlayerControl)
	)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		(if (== (pEvent type?) evJOYSTICK)
			(if (== (pEvent message?) 1)	; up
				(if canWalk
					(if (not reachedTheEnd)
						(if walking
							(bigEgo loop: 1 cel: 0 setCycle: NULL)
							(= walking 0)
							(pathWay setCycle: NULL)
						else
							(bigEgo loop: 0 setCycle: Fwd)
							(= walking 1)
							(pathWay setCycle: Fwd)
						)
					)
				)
			)	
		)
		(if (or (== (pEvent message?) KEY_RETURN) (if (== (pEvent message?) 3) )) ; pressed right arrow
			(if message
				(if (not cutsceneWait)
					(if gPrintDlg
						(gPrintDlg dispose:)
						(= message 0)						
						;(convoScript cycles: 0 cue:)
						;(if (< messagePrint moveAction)
							(questionScript cue:)	
						;)
							
						(dialogTrack)
						
						;(if (== moveAction messagePrint)
						;	(= cutsceneWait 1)
						;	(RoomScript cue:)
						;	(++ moveAction)	
							
						;)
					)
				)
			)
		)
		(if (== (pEvent message?) 7)  ; pressed left arrow
			(if (> messagePrint 2)
				(if (not noBack)
					(if message
						(if gPrintDlg
							(gPrintDlg dispose:)
							(= message 0)			
							(-- messagePrint)						
							(questionScript cycles: 0 changeState: messagePrint)
							(dialogTrack)
						)
					)
				)
			)
		)
		; handle Said's, etc...
		(if (and (> questions 0) (< questions 10))
			(if (Said '/senna')
				(answerChecker 1)
			)
			(if (Said '/black')
				(answerChecker 2)	
			)
			(if (Said '/bird,swan')
				(answerChecker 3)	
			)
			(if (Said '/blue')
				(answerChecker 4)	
			)
			(if (Said '/art,painting')
				(answerChecker 5)	
			)
			(if (Said 'oil')
				(answerChecker 6)	
			)
			(if (or (Said '/*') (Said '*'))
				(questionScript changeState: 23)
			)
			
			

		else
			(if (Said 'hi')
				(randoScript changeState: 1)	
			)
			(if (Said 'look<down')
				(PrintOther 101 5)	
			)
			(if (Said 'look>')
				(if (Said '/star, sky')
					(PrintOther 101 3)
				)
				(if (Said '/path, floor, walkway')
					(PrintOther 101 4)
				)
				(if (Said '[/!*]')
					(PrintOther 101 6)
				)	
			)
		)
	)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0
				(bigEgo loop: 3 cel: 0 setCycle: End self cycleSpeed: 2)	
			)
			(1	(= cycles 10) 
				(bigEgo loop: 1 cel: 0 setCycle: End cycleSpeed: 2)
			)
			(2	;(= cycles 10)
				(PrintMan 101 0)
				(bigEgo setCycle: Beg self)
			)
			(3	(= cycles 10)
				(bigEgo loop: 2 cel: 0 setCycle: End cycleSpeed: 2)
			)
			(4
				(PrintMan 101 1)
				(bigEgo setCycle: Beg self)	
			)
			(5	(= cycles 10)
				
			)
			(6
				(PrintMan 101 2)
				(gGame changeScore: 5)
				(= canWalk 1)	
			)
		)
	)
)
(instance questionScript of Script
	(properties)
	
	(method (changeState newState &tmp dyingScript)
		(= state newState)
		(switch state
			(0)
			(1 ;(= cycles 20)
				; she appears
				(goddess loop: 1 setCycle: End self cycleSpeed: 2)
					
				(= canWalk 0)
				(bigEgo loop: 1 setCycle: CT)
				(pathWay setCycle: NULL)
				
			)	
			(2
				(frontHair show:)
				(backHair show:)
				(goddess loop: 2)
				(= goddessVisible 1)
				
				(= conversationMode 1)
				(PrintGoddess 101 16)
			)
			(3
				(PrintGoddess 101 17)
			)
			(4
				(PrintGoddess 101 18)
			)
			(5
				(PrintGoddess 101 19)
				(= cutsceneWait 1)	; cannot continue text until you answer
				(= noBack 1)
				(= questions 1)	; question number 1
			)
			(6
				(PrintGoddess 101 20)
				(= questions 2)	; eye color						
			)
			(7
				(PrintGoddess 101 21)
				(= questions 3)	; spirit animal	
			)
			(8
				(PrintGoddess 101 22)
				(= questions 4)	; fav color
			)
			(9
				(PrintGoddess 101 23)
				(= questions 5)	; skill/hobby
			)
			(10
				(PrintGoddess 101 24)
				(= questions 6)	; self-conscious
			)
			; answered question correctly
			(15	(= cycles 15)
				(if gPrintDlg
					(gPrintDlg dispose:)
				)
			)
			(16
				(PrintGoddessCorrect 101 25)	; answered correctly
				(= cutsceneWait 0)
				(= noBack 1)		
			)
			(17
				(randoScript changeState: 1)
				(= cutsceneWait 1)
				(= noBack 0)		
			)
			; You win and the goddess departs
			(18 (= cycles 15)
				(if gPrintDlg
					(gPrintDlg dispose:)
				)	
			)
			(19
				(= cutsceneWait 0)
				(= noBack 1)	
				(PrintGoddessCorrect 101 26)	
			)
			(20
				(PrintGoddess 101 27)		
			)
			(21 (= cycles 10)
				(if gPrintDlg
					(gPrintDlg dispose:)
				)
			)
			(22
				; animation of goddess away
				(goddess loop: 1 cel: 5 setCycle: Beg)
				(frontHair hide:)
				(backHair hide:)
				(= goddessVisible 0)
				
				(= cutsceneWait 0)
				(= canWalk 1)
				(= questions 11)
				(= noBack 1)		
			)
			(23	; answer incorrectly
				(ProgramControl)
				(PrintGoddess 101 28)
				(= cutsceneWait 0)	
				(= noBack 0)
			)
			(24
				(PrintGoddess 101 29)	
			)
			(25	(= cycles 20)
				(= cutsceneWait 1)
				(if gPrintDlg
					(gPrintDlg dispose:)
				)
			)
			; animation
			(26
				(++ [gDeaths 12])
				
				(= dyingScript (ScriptID DYING_SCRIPT))
				(dyingScript
					caller: 699
					register:
						{\nTellyn never mentioned being smitten by a celestial entity being on the cards in his notes! Wouldn't hurt to refresh yourself on those next time.}
				)
				(gGame setScript: dyingScript)		
			)
		)
	)
)

; Script used to randomly select the next question to be answered
(instance randoScript of Script
	(properties)
	
	(method (changeState newState num)
		(= state newState)
		(switch state
			(0
			)
			(1	(= cycles 15)
								
			)
			(2
				(= num (Random 3 7))
				(self changeState: num)		
			)
			(3
				(questionChecker 1 6)	; eye color
			)
			(4
				(questionChecker 2 7)	; spirit animal
			)
			(5
				(questionChecker 3 8)	; fav color	
			)
			(6
				(questionChecker 4 9)	; skill/hobby	
			)
			(7
				(questionChecker 5 10)	; self-conscious
			)
		)
	)
)
(procedure (questionChecker num1 num2)
	(if (not [whichQuestion num1])
		(questionScript changeState: num2)
	else
		(randoScript changeState: 2)
	)	
)
(procedure (answerChecker number)
	(if (== questions number)
		(++ correctAnswers)
		(= [whichQuestion (- questions 1) ] 1)
		(if (>= correctAnswers 4)
			(questionScript changeState: 18)	
		else
			(questionScript changeState: 15)	
		)
	else
		; incorrect answer script
		(questionScript changeState: 23)
	)
)
(procedure (dialogTrack)
	(= messagePrint (questionScript state?))
)

(procedure (PrintOther textRes textResIndex)
	(Print textRes textResIndex
		#width 280
		#at -1 10
	)
)
(procedure (PrintMan  textRes textResIndex)
	(= gWndColor 7)
	(= gWndBack 1)
	(Print textRes textResIndex
		#width 180
		#at -1 30
		#title "You say:"
		;#dispose		
	)
	(= gWndColor 0)
	(= gWndBack 15)
;	(= message 1)
)
(procedure (PrintGoddess  textRes textResIndex)
	(if conversationMode
		(= gWndColor 11)
		(= gWndBack 9)
		(Print textRes textResIndex
			#width 80
			#at 200 -1
			#title "She says:"
			#dispose		
		)
		(= gWndColor 0)
		(= gWndBack 15)
		(= message 1)
	)
)
(procedure (PrintGoddessCorrect  textRes textResIndex)
	(if conversationMode
		(= gWndColor 11)
		(= gWndBack 9)
		(Print textRes textResIndex
			#width 80
			#at 40 120
			#title "She says:"
			#dispose		
		)
		(= gWndColor 0)
		(= gWndBack 15)
		(= message 1)
	)
)

(instance bigEgo of Act
	(properties
		y 160
		x 160
		view 220
		loop 1
	)
)
(instance pathWay of Prop
	(properties
		y 180
		x 160
		view 222
	)
)
(instance left of Prop
	(properties
		y 100
		x 220
		view 222
		loop 1
	)
)
(instance right of Prop
	(properties
		y 100
		x 220
		view 222
		loop 1
	)
)
(instance backHair of Prop
	(properties
		y 105
		x 88
		view 222
		loop 3
	)
)
(instance frontHair of Prop
	(properties
		y 99
		x 88
		view 222
		loop 4
	)
)
(instance goddess of Prop
	(properties
		y 130
		x 90
		view 222
		loop 2
	)
)

