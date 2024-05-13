;;; Sierra Script 1.0 - (do not remove this comment)
(script# 900)
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
	
	rm900 0
	
)

(local
	
	upDown = 2	
	riding = 1
	;inForest = 0
	movingRight = 1
	
	visible = 1
	badRiding = 1
	
	hText
	
	canMoveToNextLine = 0
	
	skipTime = 0
	
	arrowShot = 0
	shooting = 0
	
	deathCounter = 0
	
	leahRight = 0
	
	secondTime = 0	; used to diable the "enter" button if this is the chase scene (second time here)
)

(instance rm900 of Rm
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
			(4
				(= secondTime 1)	
			)
			(else 
				(gEgo posn: 150 100 loop: 1)
			)
		)
		(SetUpEgo)
		(gEgo init: hide: setScript: escapeScript)
		
		(arrow init: hide: xStep: 10 ignoreControl: ctlWHITE ignoreActors: setPri: 12)
		(cliff init: setPri: 2 hide: ignoreActors:)
		
		(frameTop init: setPri: 15 ignoreActors: setScript: deathScript)
		(frameBottom init: setPri: 15 ignoreActors:)
		(frameLeft init: setPri: 15 ignoreActors:)
		(frameRight init: setPri: 15 ignoreActors:)
		
		(badRider init: hide:)
		(badHorse init: ignoreControl: ctlWHITE ignoreActors: setPri: 5 xStep: 1 loop: 1 setCycle: Fwd cycleSpeed: 2)
		
		(leah init: hide: setScript: skipScript)
		(egoRiding init: hide:)
		(horse init: hide: ignoreControl: ctlWHITE ignoreActors: setPri: 5 xStep: 1 loop: 1 setCycle: Fwd cycleSpeed: 2)
		
		(enter init: hide: setPri: 15)
		;(= gMap 1)
	)
)

(instance RoomScript of Script
	(properties)
	
	(method (doit)
		(super doit:)
		; code executed each game cycle
		(if (== arrowShot 3)
			(if (== (gTheMusic prevSignal?) 10)
				(self changeState: 23)
			)
		)
		;(if (> (leah loop?) 8)
			;(= leahRight 1)	
		;)
		(if badRiding
			(if (> upDown 2)
				(badRider show: posn: (- (badHorse x?) 3) (- (badHorse y?) 24) setPri: 6)
			else
				(badRider show: posn: (- (badHorse x?) 3) (- (badHorse y?) 23) setPri: 6)
			)	
		)
		(cond 
			(riding
				(if movingRight
					(if (> upDown 2)
						(if leahRight
							(leah init: show: posn: (+ (horse x?) 6) (- (horse y?) 17) setPri: 6 )	
						else
							(leah init: show: posn: (+ (horse x?) 3) (- (horse y?) 17) setPri: 6 )
						)
						
						(egoRiding show: posn: (- (horse x?) 6) (- (horse y?) 21) setPri: 6)
						(++ upDown)
						(if (> upDown 4)
							(= upDown 0)
						)
					else
						(if leahRight
							(leah init: show: posn: (+ (horse x?) 6) (- (horse y?) 16) setPri: 6 )
						else
							(leah init: show: posn: (+ (horse x?) 3) (- (horse y?) 16) setPri: 6 )
						)
						
						(egoRiding
							show:
							;loop: 0
							posn: (- (horse x?) 6) (- (horse y?) 20)
							setPri: 6
						)
						(++ upDown)
					)
				else
					(leah
						init:
						show:
						;loop: 4
						posn: (horse x?) 2 (- (horse y?) 17)
						setPri: 6
					)
				)
			)
		)
		(if (and deathCounter (< deathCounter 500))
			(++ deathCounter)
			(if (== deathCounter 500)	
				(deathScript changeState: 1)	
			)
		)
	)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		; handle Said's, etc...
		(if (or (== (pEvent message?) KEY_RETURN) (if (== (pEvent message?) 3) ))
			(if (not shooting)
				(if skipTime
					(if canMoveToNextLine
						(self cue:)
						(= canMoveToNextLine 0)
						(= skipTime 0)
						(enter setCycle: Beg)			
					)
				)
			)		
		)
		(if (== (pEvent message?) KEY_SPACE)
			(if skipTime
				(if canMoveToNextLine
					(if shooting
						(if (not (== deathCounter 500))	; if not already dead
							(self cue:)
							(= canMoveToNextLine 0)
							(= skipTime 0)
						)	
					)	
				)
			)		
		)
		(if (== (pEvent message?) 7)	; Pressed Left
		)
		(if (== (pEvent message?) KEY_ESCAPE)	; Pressed Escape
			(if (not (== deathCounter 500))
				(escapeScript changeState: 1)
			)
		)	
	)
	
	(method (changeState newState &tmp [buffer 300])
		(= state newState)
		(switch state
			(0 (= cycles 1)
			)
			(1 (= cycles 10)
				(if (== gPreviousRoomNumber 4)
					(self cycles: 0)
					(self changeState: 10)	
				else
					(horse show: xStep: 1 setMotion: MoveTo 190 (horse y?) skipScript)
					(leah setCycle: Fwd cycleSpeed: 3)
				)	
			)
			(2
				(= hText 
					(Display 900 0
	                    dsCOORD 10 150 dsFONT 4 dsALIGN alCENTER dsCOLOUR clCYAN dsBACKGROUND clBLACK dsWIDTH 300 dsSAVEPIXELS
	                )
				)
				(= canMoveToNextLine 1)	
			)
			(3	(= seconds 2)
				(Display
                    ""
                    dsRESTOREPIXELS hText
                )
                (horse xStep: 1 setMotion: MoveTo 150 (horse y?) skipScript)	
			)
			(4
				(= hText 
					(Display 900 1
	                    dsCOORD 10 150 dsFONT 4 dsALIGN alCENTER dsCOLOUR clCYAN dsBACKGROUND clBLACK dsWIDTH 300 dsSAVEPIXELS
	                )
				)
				(= canMoveToNextLine 1)		
			)
			(5	(= seconds 2)
				(Display
                    ""
                    dsRESTOREPIXELS hText
                )
                (horse setMotion: MoveTo 88 (horse y?) skipScript)
                (leah loop: 8 setCycle: End cycleSpeed: 3)
                (badHorse setMotion: MoveTo 220 125)	
			)
			(6
				(= hText 
					(Display 900 2
	                    dsCOORD 10 150 dsFONT 4 dsALIGN alCENTER dsCOLOUR clCYAN dsBACKGROUND clBLACK dsWIDTH 300 dsSAVEPIXELS
	                )
				)
				(= canMoveToNextLine 1)		
			)
			(7	(= seconds 2)
				(Display
                    ""
                    dsRESTOREPIXELS hText
                )	
			)
			(8
				(gRoom newRoom: 4)
							)
			(10	(= cycles 10)
				;(Print "Riding with soldier behind")
				(horse show: posn: 150 (horse y?))
				(badHorse setMotion: MoveTo 220 125)
				(leah loop: 8 setCycle: End cycleSpeed: 3)	
			)
			(11	;(= cycles 10)
				;(Print "shoot 3 arrows")
				(horse setMotion: MoveTo 98 (horse y?) self)				
			)
			(12	;(= cycles 10)
				(= leahRight 1)
				(leah loop: 9 cel: 0 setCycle: End self)			
			)
			(13
				(= hText 
					(Display 900 3
	                    dsCOORD 10 150 dsALIGN alCENTER dsCOLOUR clRED dsBACKGROUND clBLACK dsWIDTH 300 dsSAVEPIXELS
	                )
				)
				(= canMoveToNextLine 1)
				(skipScript cue:)
				(= shooting 1)
				(badHorse setMotion: MoveTo 200 125)
				(++ deathCounter)			
			)
			(14
				(= deathCounter 0)
				(++ arrowShot)
				(if (== arrowShot 3)
					(self changeState: 17)	
				)
				
				(switch arrowShot
					(1				
						(gTheMusic stop:)
						(gTheMusic number: 78 loop: -1 priority: -1 play:)
						(leah loop: 10)
						(badRider loop: 2)
						(arrow show: posn: (+ (leah x?) 6)(- (leah y?) 26) setMotion: MoveTo 320 (- (leah y?) 30) self)
						(badHorse setMotion: MoveTo 220 125)
					)
					(2
						(gTheMusic stop:)
						(gTheMusic number: 79 loop: -1 priority: -1 play:)
						(leah loop: 10)
						(badRider loop: 3)
						(arrow show: posn: (+ (leah x?) 6)(- (leah y?) 26) setMotion: MoveTo 320 (- (leah y?) 30) self setPri: 1)
						(badHorse setMotion: MoveTo 220 125)	
					)
				)
				(Display
                    ""
                    dsRESTOREPIXELS hText
                )	
			)
			(15
				(badRider loop: 1)
				(leah loop: 11 cel: 0 setCycle: End self)
				
			)
			(16
				(self changeState: 13)	
			)
			(17
				(= riding 0)
				(= shooting 0)
				(= leahRight 0)
				
				(gTheMusic stop:)
				(gTheMusic number: 80 loop: 1 priority: -1 play:)
				(horse view: 328 xStep: 15 setMotion: MoveTo (+ (horse x?) 114) (horse y?) self cycleSpeed: 5)
				(egoRiding view: 247 xStep: 15 y: (- (egoRiding y?) 10) setMotion: MoveTo (+ (egoRiding x?) 114) (egoRiding y?))
				(leah view: 248 xStep: 15 y: (- (leah y?) 10) setMotion: MoveTo (+ (leah x?) 114) (leah y?) )
				(badHorse xStep: 10 setMotion: MoveTo 300 (badHorse y?))
				(badRider xStep: 10 setMotion: MoveTo 300 (badRider y?))	
			)
			(18
				;(ShakeScreen 1)
				(cliff show: setCycle: End)	
				(horse view: 325 loop: 2 setCycle: End cycleSpeed: 5)
				(egoRiding view: 247 y: (- (egoRiding y?) 10) xStep: 7 setMotion: MoveTo (- (egoRiding x?) 70) (- (egoRiding y?) 20) self)
				(leah view: 248 y: (- (leah y?) 10) xStep: 7 setMotion: MoveTo (- (leah x?) 50) (- (leah y?) 10))					
			)
			(19
				(horse loop: 3 cel: 0 setCycle: End self)
				(egoRiding view: 229 y: (- (egoRiding y?) 10) yStep: 6 setMotion: MoveTo (- (egoRiding x?) 40) (+ (egoRiding y?) 40) self)
				(leah view: 248 y: (- (leah y?) 10) yStep: 6 setMotion: MoveTo (- (leah x?) 30) (+ (leah y?) 40))	
			)
			(20
				(egoRiding setMotion: MoveTo (- (egoRiding x?) 10) (+ (egoRiding y?) 60) self)
				(leah view: 248 setMotion: MoveTo (- (leah x?) 10) (+ (leah y?) 40))	
			)
			(21	
				;wait for crash sound to shake screen in next state
			)
			(23	(= cycles 40)
				(ShakeScreen 3)	
			)
			(24	(= cycles 20)
				(arrow hide:)
				(cliff hide:)
		
				(frameTop hide:)
				(frameBottom hide:)
				(frameLeft hide:)
				(frameRight hide:)
		
				(badRider hide:)
				(badHorse hide:)
		
				(leah hide:)
				(egoRiding hide:)
				(horse hide:)
			)
			
			(25	
				(= cycles 1)
			)
			(26	
				(EditPrint @gName 14
					{Welcome to Betrayed Alliance: Book 2.\n\nHonor us again with knowledge of your name:}
					#at -1 40
					#width 230
				)
				(if (== gName 0)
					(Print {Come on you can do better!})
					(self changeState: 26)
				else
					(self cue:)
				)
			)
			(27 (= cycles 10)
				(Format @buffer {Thus Leah von Spier and %s were thrown deep into the cursed forest bereft of saddle and resources, left to survive the wilderness together.} @gName) 
				(= gWndColor 11)
				(= gWndBack 1)
				(Print @buffer #font 4)
				(= gWndColor 0)
				(= gWndBack 15)	
			)
			(28
				(gRoom newRoom: 334)	
			)
		)
	)
)
(instance skipScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0
			)
			(1	(= cycles 1)
				(= skipTime 1)
				(if (not secondTime)
					(enter show: setCycle: End cycleSpeed: 1)
				)	
			)
			(2
				(self changeState: 0)	
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
(instance deathScript of Script
	(properties)
	
	(method (changeState newState dyingScript)
		(= state newState)
		(switch state
			(0
			)
			(1	
				(ProgramControl)
				(badHorse setMotion: MoveTo (- (badHorse x?) 40) (badHorse y?) self)	
			)
			(2	
				; secret deaths do not count to death counter
				;(if (not [gDeaths 31])
				;	(++ gUniqueDeaths)
				;)
				
				(++ [gDeaths 31])
				
				(= dyingScript (ScriptID DYING_SCRIPT))
				(dyingScript
					caller: 699
					register:
						{\nCaptured by Ishvi soldiers before the game even properly began?\nImpressive! Inept sure, but impressive nonetheless!}
				)
				(gGame setScript: dyingScript)	
			)
		)
	)
)

(instance frameTop of Act
	(properties
		y 52 ; 41 for full
		x 160
		view 993
		loop 2
		cel 2
	)
)
; Frame Props
(instance frameBottom of Act
	(properties
		y 144 ; 155 for full
		x 160
		view 993
		loop 2
		cel 2
	)
)
(instance frameLeft of Prop
	(properties
		y 155
		x 70
		view 993
		loop 1
		cel 4
	)
)
(instance frameRight of Prop
	(properties
		y 155
		x 249
		view 993
		loop 0
		cel 4
	)
)
; Horse Riding Props
(instance badHorse of Act
	(properties
		y 125
		x 282
		view 327
	)
)
(instance badRider of Act
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
		x 230
		view 325
	)
)
(instance leah of Act
	(properties
		y 150
		x 38
		view 318
		loop 4
	)
)

(instance egoRiding of Act
	(properties
		y 150
		x 38
		view 427
		loop 0
	)
)
(instance arrow of Act
	(properties
		y 70
		x 100
		view 331
	)
)
(instance cliff of Prop
	(properties
		y 140
		x 210
		view 325
		loop 4
	)
)
(instance enter of Prop
	(properties
		y 147
		x 273
		view 998
		loop 3
	)
)