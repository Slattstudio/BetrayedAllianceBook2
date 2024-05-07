;;; Sierra Script 1.0 - (do not remove this comment)
(script# 35)
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
	
	rm035 0
	
)
(local
	
	onLeft = 0
	onRight = 0
	onStairs = 0
	
	cutScene = 0
	
)

(instance rm035 of Rm
	(properties
		picture scriptNumber
		north 0
		east 0
		south 254
		west 0
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript setRegions: 200 setRegions: 205)
		
		(if gKneeHealed
			(if (not gSeparated)
				(leahProp init: setScript: leahScript) 
			)
		)
		(switch gPreviousRoomNumber
			(254 
				;(gEgo posn: 70 170 loop: 3)
				(PlaceEgo 70 170 3)
				(leahProp posn: 90 165 view: 343 setCycle: Walk ignoreControl: ctlWHITE cycleSpeed: 0 setMotion: MoveTo 152 142 leahScript)
			)
			(else 
				;(gEgo posn: 150 100 loop: 1)
				(PlaceEgo 150 100 1)
			)
		)
		(SetUpEgo)
		(gEgo init:)
		(= gEgoMovementType 0)
		(RunningCheck)
		
		
		(leftRing init: setPri: 2 setScript: turnBackScript)
		(rightRing init: setPri: 2)
		(actionEgo init: hide: ignoreActors:)
		
		(gTheMusic fade:)
		
		(if (and
			(== [g261Points 0] 1)
			(== [g261Points 1] 5)
			(== [g261Points 2] 6))
			
			(leftRing cel: 1)
			(rightRing cel: 1)
		else
			(leftRing cel: 0)
			(rightRing cel: 0)
		)

	)
)

(instance RoomScript of Script
	(properties)
	
	(method (doit)
		(super doit:)
		; code executed each game cycle
		
		(if (& (gEgo onControl:) ctlYELLOW)
			(if (not onStairs)
				(if (> (gEgo y?) 115) ; on the bottom of the stairs
					(self changeState: 1)
				else
					(self changeState: 3)
				)
				(= onStairs 1)
			)
		)

		(if (<= (gEgo distanceTo: leftRing) 20)
			(if (< (gEgo y?) 88)
				(if (and
					(== [g261Points 0] 1)
					(== [g261Points 1] 5)
					(== [g261Points 2] 6))
					
					;(leftRing cel: 1)
					(= onLeft TRUE)
					
					(if (not gSeparated)
						(if (not cutScene)
							(if g44WallBroken
								(self changeState: 5)
								(= cutScene 1)
							else
								(turnBackScript changeState: 1)
								(= cutScene 1)
							)	
						)
					)
				)
			)
		else
			(if (not cutScene)	; allows the ring to light up during the cutscene
				;(leftRing cel: 0)
				(= onLeft FALSE)
			)	
		)
		(if (<= (gEgo distanceTo: rightRing) 20)
			(if (and
				(== [g261Points 0] 1)
				(== [g261Points 1] 5)
				(== [g261Points 2] 6))
				
				;(rightRing cel: 1)
				(= onRight TRUE)
				
				(if (not gSeparated)
					(if (not cutScene)
						(if g44WallBroken
							(self changeState: 5)
							(= cutScene 1)	
						else
							(turnBackScript changeState: 1)
							(= cutScene 1)
						)
					)
				)
			)
		else
			(if (not cutScene)	; allows the ring to light up during the cutscene
				;(rightRing cel: 0)
				(= onRight FALSE)
			)	
		)
		
	)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if (& (pEvent modifiers?) emRIGHT_BUTTON)
				(if (== (OnControl ocSPECIAL (pEvent x?) (pEvent y?)) ctlSILVER)	; red carpet
					(PrintOther 35 4)	
				)
				(if (== (OnControl ocSPECIAL (pEvent x?) (pEvent y?)) ctlGREY)	; candle holder
					(PrintOther 35 5)	
				)
				(if
					(checkEvent pEvent (leftRing nsLeft?)(leftRing nsRight?)(leftRing nsTop?)(leftRing nsBottom?))
						(PrintOther 35 2)
				else
					(if
						(checkEvent pEvent (rightRing nsLeft?)(rightRing nsRight?)(rightRing nsTop?)(rightRing nsBottom?))
							(PrintOther 35 2)
					else
						(if (checkEvent pEvent (leahProp nsLeft?)(leahProp nsRight?)(leahProp nsTop?)(leahProp nsBottom?))
							(if gKneeHealed
								(if (not gSeparated)
									(PrintOther 200 29)
								)
							)
						else
							(if (checkEvent pEvent 170 245 0 61)	; banner
								(PrintOther 35 3)
							)
						)
					)
				)
				
			)
		)
		
		; handle Said's, etc...
		
		(if (Said 'talk/woman,leah')	
			(if (and (not gSeparated) gKneeHealed)
				(PrintLeah 35 10)					
			else
				(PrintCantDoThat)
			)
		)
		
		(if (Said 'look>')
			(if (Said '/carpet,stage,symbol')
				(PrintOther 35 6)		
			)
			(if (Said '/sconce,candle,torch')
				(PrintOther 35 5)		
			)
			(if (Said '/banner,cloth')
				(PrintOther 35 3)		
			)
			(if (Said '/forest,verlorn')
				(PrintOther 0 105)	
			)
			(if (Said '[/!*]')
				(PrintOther 35 0)
			)
		)
		
		(if (Said 'pull, move, touch/sconce,candle')
			(if (& (gEgo onControl:) ctlSILVER)
				(PrintOther 35 7)	
			else
				(PrintNotCloseEnough)
			)	
		)
		(if (Said 'pull, move, touch/tapestry,banner')
			(if (& (gEgo onControl:) ctlSILVER)
				(PrintOther 35 8)	
			else
				(PrintNotCloseEnough)
			)	
		)
	)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0 ; Handle state changes
			)
			(1
				(ProgramControl)
				(gEgo setMotion: MoveTo 166 82 self)	
			)
			(2
				(PlayerControl)
				(= onStairs 0)	
			)
			(3
				(ProgramControl)
				(gEgo setMotion: MoveTo 120 142 self)	
			)
			(4
				(PlayerControl)
				(= onStairs 0)	
			)
			(5	; getting on both stairs
				(ProgramControl)
				
				
				(if onLeft
					(gEgo setMotion: MoveTo (leftRing x?)(- (leftRing y?) 5) self)	
				else
					(gEgo setMotion: MoveTo (rightRing x?)(- (rightRing y?) 5) self)		
				)			
			)
			(6	; face the front ; leah walks to bottom of stairs
				(gEgo loop: 2)
				(leahScript cycles: 0)
				(leahProp view: 343 setMotion: MoveTo 120 139 self setCycle: Walk cycleSpeed: 0)
			)
			(7	; leah walks to top of stairs
				(leahProp setMotion: MoveTo 174 82 self)	
			)
			(8
				(if onLeft
					(leahProp setMotion: MoveTo (rightRing x?)(- (rightRing y?) 5) self)
				else
					(leahProp setMotion: MoveTo (leftRing x?)(- (leftRing y?) 5) self)			
				)		
			)
			(9	(= cycles 20)
				(leahProp view: 1 loop: 2)
				;(if onLeft
				;	(rightRing cel: 1)	
				;else
				;	(leftRing cel: 1)
				;)	
			)
			(10 (= cycles 10)
				(PrintLeah 35 11)	
			)
			(11	; characters disappear
				(gEgo hide:)
				(actionEgo show: posn: (gEgo x?)(gEgo y?) view: 722 loop: 2 cel: 0 setCycle: End cycleSpeed: 2)
				(leahProp view: 1 loop: 10 cel: 0 setCycle: End self cycleSpeed: 2)	
				
				(gTheSoundFX number: 4 play:)
			)
			(12	(= cycles 10)
			)
			(13
				(gRoom newRoom: 101)	
			)
			
		)
	)
)
(instance turnBackScript of Script
	(properties)
	
	(method (changeState newState rando)
		(= state newState)
		(switch state
			(1	(= cycles 5)
				(ProgramControl)
				(if onLeft
					(gEgo setMotion: MoveTo (leftRing x?)(- (leftRing y?) 5) self)	
				else
					(gEgo setMotion: MoveTo (rightRing x?)(- (rightRing y?) 5) self)		
				)					
				(leahScript cycles: 0)
				(leahProp view: 1 loop: 3 setCycle: NULL) 	
			)
			(2	(= cycles 5)
				(gEgo loop: 2)
				;(PrintOK)
			)
			(3
				(PrintMan 35 13)	
			)
			(4	(= cycles 5)
				(PrintLeah 35 14)
				(PrintLeah 35 16)	
			)
			(5	(= cycles 5)
				(PrintMan 35 15)	
			)
			(6
				(gEgo setMotion: MoveTo 169 (gEgo y?) self)	
			)
			(7	
				(PlayerControl)	
				(gEgo loop: 2)
				(= cutScene 0)
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
	(if onRight
		(Print scriptNumber textResIndex		
				#width 180
				#at 25 20
				#title "You say:"
		)
	else
		(Print scriptNumber textResIndex		
				#width 180
				#at 160 20
				#title "You say:"
		)
	)	
	(= gWndColor 0)
	(= gWndBack 15)
)
(procedure (PrintLeah textRes textResIndex)
	(= gWndColor 14)
	(= gWndBack 5)
	(if (> (leahProp y?) 130)
		(Print scriptNumber textResIndex		
			#width 180
			#at -1 140
			#title "Leah says:"
		)
	else
		(Print scriptNumber textResIndex		
			#width 180
			#at -1 140
			#title "Leah says:"
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
(instance actionEgo of Act
	(properties
		y 180
		x 27
		view 0
		loop 1
	)
)
(instance leahProp of Act
	(properties
		y 90
		x 90
		view 1
	)
)
(instance leftRing of Prop
	(properties
		y 88
		x 118
		view 61
	)
)
(instance rightRing of Prop
	(properties
		y 88
		x 241
		view 61
	)
)