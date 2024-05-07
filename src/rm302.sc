;;; Sierra Script 1.0 - (do not remove this comment)
; score + 3
(script# 302)
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
	
	rm302 0
	
)

(local
	
	leahVisible = 1
	message = 0 ; used to remove "dispose" windows and avoid crashing
	
	testing = 0
	
	messagePrint = 0 ; used to all changes to dialog to go backwards and forwards
	visionChange = 0 ; used to disable changes if player goes backwards during text
	
	buttonInstructionsOn = 0
	
)


(instance rm302 of Rm
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
		(leah init: setScript: swapScript setPri: 14)
		(leahEyes init: setPri: 15 setScript: blinkScript)
		(leahMouth init: setPri: 15 cycleSpeed: 1 setScript: convoScript)
		(egoMouth init: setPri: 15 cycleSpeed: 1 setScript: egoMouthScript)
		(buttonInstructions init: hide: ignoreActors: setPri: 15)
		
		
		(colors1 init: setCycle: Fwd cycleSpeed: 1 setPri: 8 hide: setScript: mouthScript)
		(colors2 init: setCycle: Fwd cycleSpeed: 1 setPri: 8 hide:)
		(colors3 init: setCycle: Fwd cycleSpeed: 1 setPri: 8 hide:)
		(colors4 init: setCycle: Fwd cycleSpeed: 1 setPri: 8 hide:)
		(colors5 init: setCycle: Fwd cycleSpeed: 1 setPri: 8 hide:)
		(colors6 init: setCycle: Fwd cycleSpeed: 1 setPri: 8 hide:)
		
		(TheMenuBar state: DISABLED)
		
		;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;for Testing purposes
		;(= g303egoHealed 3)
		
	)
)

(instance RoomScript of Script
	(properties)
	
	(method (doit)
		(super doit:)
		; code executed each game cycle
	)
	
	(method (handleEvent pEvent button)
		(super handleEvent: pEvent)
		
		(if (or (== (pEvent message?) KEY_RETURN) (if (== (pEvent message?) 3) )) ; pressed right arrow
			(if message
				(if gPrintDlg
					(gPrintDlg dispose:)
					(= message 0)
					(buttonInstructions hide:)

					(if (== g303egoHealed 3)						
						(mouthScript changeState: 2)
						(convoScript cycles: 0 cue:)
						(dialogTrack)
					else
						(TheMenuBar state: ENABLED)
						(gRoom newRoom: 236)
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
						(buttonInstructions hide:)

						(if (== g303egoHealed 3)
						
							(-- messagePrint)						
							(mouthScript changeState: 2)
							(convoScript cycles: 0 changeState: messagePrint)
							(dialogTrack)
						else
							(TheMenuBar state: ENABLED)
							(gRoom newRoom: 236)
						)
					)
				)
			)
		)
		(if (== (pEvent type?) evKEYBOARD)
			(if (== (pEvent message?) KEY_ESCAPE)
				(if (== g303egoHealed 3)
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
							(convoScript changeState: 22)
						)
					)
				)
			)
		)
		; handle Said's, etc...
		(if (== (pEvent type?) evMOUSEBUTTON) 
			;(if (& pEvent emRIGHT_BUTTON)
			;(if (== leahVisible 0)
				;(leah cel: 0)
				;(= leahVisible 1)
			;else
				;(leah cel: 1)
				;(= leahVisible 0)
			;)	
			;)
		)
		(if(Said 'hi')
			;(swapScript changeState: 1)
			;(PrintMan 302 6)
			;(PrintLeah 302 7)
			;(PrintJulyn 302 10)
			(convoScript changeState: 1)	
			(= testing 1)
		)
		(if(Said 'test')
			(leahEyes hide:)
			(leahMouth setCycle: Fwd)	
		)
		(if(Said 'exit,leave')
			(TheMenuBar state: ENABLED)
			(gRoom newRoom: 236)	
		)
	)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0 (= cycles 10); Handle state changes
				(ProgramControl)
			)
			(1	
				(PlayerControl)
				(if (< g303egoHealed 3)
					(PrintLeah 302 0)		
				else
					(PrintLeah 302 1)
					(ProgramControl)					
				)
				;(self cue:)	
			)
			(2 (= cycles 10)
						
			)
			(3	(= cycles 14)
				(if (< g303egoHealed 3)
					(convoScript changeState: 2)
				else
					(TheMenuBar state: ENABLED)
					(gRoom newRoom: 236)
				)
				;(PrintLeah 302 2)	
			)
			(4
				;(convoScript changeState: 2)	
			)
		)
	)
)(instance mouthScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0
			)
			(1	(= cycles 25)
				(leahMouth setCycle: Fwd)
			)
			(2
				(leahMouth setCycle: CT)	
			)
		)
	)
)
(instance egoMouthScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0
			)
			(1	(= cycles 25)
				(egoMouth setCycle: Fwd)
			)
			(2
				(egoMouth setCycle: CT)	
			)
		)
	)
)



(instance blinkScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0 (= cycles 20)
			)
			(1
				(leahEyes setCycle: End self)
			)
			(2 (= cycles (Random 30 50))
				
			)
			(3
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
				;(PrintLeah 302 0)
			)
			(1	;(= cycles 10)	
				(PrintLeah 302 2)
			)
			(2  ;(= cycles 40)
				(PrintMan 302 3)				
			)
			(3
				(PrintOther 302 19)
				(if (not visionChange)
					(swapScript changeState: 1)
					(++ visionChange)	
				)	
			)
			(4	;(= cycles 10)
				(PrintMan 302 4)	
			)
			(5 ;(= cycles 20)
				(PrintLeah 302 5)	
			)
			(6 ;(= cycles 40)
				(PrintMan 302 6)		
			)
			(7
				(PrintOther 302 20)
				(if (== visionChange 1)
					(swapScript cue:)
					(++ visionChange)
				)	
			)
			(8 ;(= cycles 20)
				(PrintMan 302 18)		
			)
			(9 ;(= cycles 20)
				(PrintLeah 302 7)
			)
			(10
				(PrintLeah 302 8)
			)		
			(11	;(= cycles 30)
				(if (== visionChange 2)
					(swapScript changeState: 5)
				)
				(PrintOther 302 21)	
			)
			(12	;(= cycles 20)
				(PrintMan 302 9)
			)
			(13
				(PrintMan 302 10)		
			)
			(14 ;(= cycles 20)
				(PrintJulyn 302 11)	
			)
			(15	;(= cycles 20)
				(PrintMan 302 12)		
			)
			(16 ;(= cycles 20)
				(PrintJulyn 302 13)	
			)
			(17	;(= cycles 20)
				(PrintMan 302 14)		
			)
			(18 ;(= cycles 20)
				(PrintJulyn 302 15)	
			)
			(19	;(= cycles 20)
				(PrintMan 302 16)		
			)
			(20 ;(= cycles 20)
				(PrintJulyn 302 17)	
			)
			(21	(= cycles 20)
			
			)	
			(22
				(TheMenuBar state: ENABLED)
				(PlayerControl)
				(gGame changeScore: 3)
				(= gKneeHealed 1)
				(= gAnotherEgo 0)
				(gRoom newRoom: 236)	
			)
		)
	)
)

(instance swapScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0
			)
			(1 (= cycles 2)
				; cycles the colors
				(DrawPic 0 0 1 0)
				(gTheSoundFX number: 5 play:)
				;(gTheMusic stop:)
			)
			(2 ;(= cycles 40)
				(DrawPic 302 0 1 0)
				(colors1 show:)
				(colors2 show:)
				(colors3 show:)
				(colors4 show:)
				(colors5 show:)
				(colors6 show:)
			)
			(3 (= cycles 1)
				; flash screen
				(DrawPic 0 0 1 0)
				(gTheSoundFX number: 5 play:)
			)
			(4 ;(= cycles 60)
				(DrawPic 303 0 1 0)
				; change the image from leah to Julyn
			)
			(5 (= cycles 1)
				(DrawPic 0 0 1 0)
				(gTheSoundFX number: 5 play:)	
			)
			(6 ;(= cycles 200)				
				(DrawPic 303 0 1 0)
				(leah cel: 1 y: 137)
				(leahEyes loop: 6 x: (- (leahEyes x?) 1))
			)
			(7
				;(leah cel: 0)	
			)
		)
	)
)

(procedure (dialogTrack)
	(= messagePrint (convoScript state?))
)

(procedure (PrintOther textRes textResIndex)
	(Print textRes textResIndex
		
		#width 290
		#at -1 140
		#dispose
		
	)
	(= message 1)
	(if buttonInstructionsOn
		(buttonInstructions show: posn: 50 137)
	)
)
(procedure (PrintMan textRes textResIndex)
	(= gWndColor 7)
	(= gWndBack 1)
	(Print textRes textResIndex
		#width 180
		#at 110 130
		#dispose		
	)
	(= gWndColor 0)
	(= gWndBack 15)
	(= message 1)
	(egoMouthScript changeState: 1)
	(if buttonInstructionsOn
		(buttonInstructions show: posn: 150 127)
	)
)
(procedure (PrintLeah textRes textResIndex)
	(= gWndColor 14)
	(= gWndBack 5)
	(Print textRes textResIndex
		#width 160
		#at 20 110
		#dispose
	)
	(= gWndColor 0)
	(= gWndBack 15)
	(= message 1)
	(mouthScript changeState: 1)
	(if buttonInstructionsOn
		(buttonInstructions show: posn: 60 107)
	)
)
(procedure (PrintJulyn textRes textResIndex)
	(= gWndColor 11)
	(= gWndBack 3)
	(Print textRes textResIndex
		#width 160
		#at 20 110
		#dispose
	)
	(= gWndColor 0)
	(= gWndBack 15)
	(= message 1)
	(mouthScript changeState: 1)
	
	(if buttonInstructionsOn
		(buttonInstructions show: posn: 60 107)
	)
)

(instance buttonInstructions of Prop
	(properties
		y 180
		x 70
		view 994
	)
)
(instance leah of Prop
	(properties
		y 130
		x 110
		view 957
	)
)
(instance leahEyes of Prop
	(properties
		y 23
		x 141
		view 957
		loop 3
	)
)
(instance leahMouth of Prop
	(properties
		y 41
		x 140
		view 957
		loop 4
	)
)
(instance egoMouth of Prop
	(properties
		y 56
		x 213
		view 957
		loop 5
	)
)

(instance colors1 of Prop
	(properties
		y 49
		x 75
		view 957
		loop 1
	)
)
(instance colors2 of Prop
	(properties
		y 49
		x 123
		view 957
		loop 1
	)
)
(instance colors3 of Prop
	(properties
		y 49
		x 171
		view 957
		loop 1
	)
)
(instance colors4 of Prop
	(properties
		y 49
		x 219
		view 957
		loop 1
	)
)
(instance colors5 of Prop
	(properties
		y 49
		x 267
		view 957
		loop 1
	)
)
(instance colors6 of Prop
	(properties
		y 49
		x 315
		view 957
		loop 1
	)
)