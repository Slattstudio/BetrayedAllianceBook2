;;; Sierra Script 1.0 - (do not remove this comment)
(script# 254)
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
	
	rm254 0 
	
)


(instance rm254 of Rm
	(properties
		picture scriptNumber
		north 25
		east 35
		south 0
		west 248
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
			
			(25 
				;(gEgo posn: 100 110 loop: 2)
				(PlaceEgo 100 110 2) 
				(leahProp posn: 120 110 view: 343 setCycle: Walk ignoreControl: ctlWHITE cycleSpeed: 0 setMotion: MoveTo 154 112 leahScript)
			)
			(35 
				;(gEgo posn: 230 135 loop: 1)
				(PlaceEgo 230 135 1)
				(leahProp posn: 180 125 view: 343 setCycle: Walk ignoreControl: ctlWHITE cycleSpeed: 0 setMotion: MoveTo 154 112 leahScript)
				
				(gTheMusic number: 10 loop: -1 priority: -1 play:)	
			)
			(248 
				;(gEgo posn: 20 140 loop: 0)
				(PlaceEgo 20 140 0)
				(leahProp posn: 25 125 view: 343 setCycle: Walk ignoreControl: ctlWHITE cycleSpeed: 0 setMotion: MoveTo 154 112 leahScript)
			)
			(else 
				;(gEgo posn: 150 100 loop: 1)
				(PlaceEgo 120 150 1)
			)
		)
		(SetUpEgo)
		(gEgo init:)
		(RunningCheck)
		
		(stoneWork init: setPri: 0 ignoreActors:)
		
		(if (and
			(== [g261Points 0] 1)
			(== [g261Points 1] 5)
			(== [g261Points 2] 6))
			
			(stoneWork cel: 1)
		)
	)
)

(instance RoomScript of Script
	(properties)
	
	(method (doit)
		(super doit:)
		; code executed each game cycle
		(if (& (gEgo onControl:) ctlMAROON)
			(gRoom newRoom: 25)
		)
		(if (& (gEgo onControl:) ctlGREY)
			(gRoom newRoom: 35)
		)
	)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if (& (pEvent modifiers?) emRIGHT_BUTTON)
				(if
					(checkEvent pEvent (leahProp nsLeft?) (leahProp nsRight?) (leahProp nsTop?) (leahProp nsBottom?))
					(if gKneeHealed
						(if (not gSeparated)
							(PrintOther 200 29)
							(return)	
						)
					)
				)
				(if (or (== (OnControl ocPRIORITY (pEvent x?) (pEvent y?)) ctlCYAN) (== (OnControl ocPRIORITY (pEvent x?) (pEvent y?)) ctlNAVY))	; building
					(PrintOther 254 1)
				)
				(if (== (OnControl ocSPECIAL (pEvent x?) (pEvent y?)) ctlSILVER)	;pavement and symbol
					(if (== (stoneWork cel?) 1)	; on
						(if g255Baptized
							(PrintOther 254 3)	
						else
							(PrintOther 254 2)
						)
					else
						(PrintOther 254 2)
					)
				)
				(if (== (OnControl ocSPECIAL (pEvent x?) (pEvent y?)) ctlRED)	; background
					(PrintOther 254 4)	
				)
				(if (== (OnControl ocPRIORITY (pEvent x?) (pEvent y?)) ctlGREEN)	; wall
					(PrintOther 254 5)					
				)	
			)
		)
		; handle Said's, etc...
		(if (Said 'talk/woman,leah')	
			(if (and (not gSeparated) gKneeHealed)
				(PrintLeah 254 10)					
			else
				(PrintCantDoThat)
			)
		)
		
		(if (Said 'look>')
			(if (Said '/building')
				(PrintOther 254 1)
			)
			(if (Said '/pavement,symbol')
				(if g255Baptized
					(PrintOther 254 3)	
				else
					(PrintOther 254 2)
				)
			)
			(if (Said '/stone, wall')
				(PrintOther 254 5)	
			)
			(if (Said '/forest,verlorn')
				(PrintOther 0 105)	
			)
			(if (Said '[/!*]')
				(PrintOther 254 0)
			)
		)
	)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0 ; Handle state changes
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

(procedure (PrintOther textRes textResIndex)
	(Print textRes textResIndex		
		#width 280
		#at -1 10
	)
)
(procedure (PrintLeah textRes textResIndex)
	(= gWndColor 14)
	(= gWndBack 5)
	(Print scriptNumber textResIndex		
		#width 280
		#at -1 10
		#title "She says:"
	)
	(= gWndColor 0)
	(= gWndBack 15)
)
(instance leahProp of Act
	(properties
		y 90
		x 90
		view 1
	)
)
(instance stoneWork of Prop
	(properties
		y 148
		x 146
		view 61
		loop 2
	)
)