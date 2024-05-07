;;; Sierra Script 1.0 - (do not remove this comment)
(script# 25)
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
	
	rm025 0
	
)


(instance rm025 of Rm
	(properties
		picture scriptNumber
		north 0
		east 24
		south 254
		west 28
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
			(24 
				;(gEgo posn: 300 140 loop: 1)
				(PlaceEgo 300 140 1)
				(leahScript cue:)
				(leahProp posn: 198 112)
			)
			(28 
				;(gEgo posn: 20 140 loop: 0)
				(PlaceEgo 20 150 0)
				(leahProp posn: 30 125 view: 343 setCycle: Walk ignoreControl: ctlWHITE cycleSpeed: 0 setMotion: MoveTo 198 112 leahScript)
			)
			(254 
				;(gEgo posn: 160 170 loop: 3)
				(PlaceEgo 160 170 3)
				(leahProp posn: 180 165 view: 343 setCycle: Walk ignoreControl: ctlWHITE cycleSpeed: 0 setMotion: MoveTo 198 112 leahScript)
			)
			(else 
				;(gEgo posn: 150 100 loop: 1)
				(PlaceEgo 150 100 1) 
			)
		)
		(SetUpEgo)
		(gEgo init:)
		(RunningCheck)
		
		(rock init:)
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
				(if (== (OnControl ocPRIORITY (pEvent x?) (pEvent y?)) ctlNAVY)	; tree
					(PrintOther 25 1)	
				)
			
			)
		)
		
		; handle Said's, etc...
		(if (Said 'talk/woman,leah')	
			(if (and (not gSeparated) gKneeHealed)
				(PrintLeah 25 10)					
			else
				(PrintCantDoThat)
			)
		)
		
		(if (Said 'look>')
			(if (Said 'tree')
				(PrintOther 25 1)
			)
			(if (Said 'rock')
				(PrintOther 25 2)
			)
			(if (Said '/forest,verlorn')
				(PrintOther 0 105)	
			)
			(if (Said '[/!*]')
				(PrintOther 25 0)
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
(procedure (PrintOther textRes textResIndex)
	(if (> (gEgo y?) 120)
		(Print textRes textResIndex	#width 280 #at -1 10)
	else
		(Print textRes textResIndex	#width 280 #at -1 140)
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
(instance leahProp of Act
	(properties
		y 90
		x 90
		view 1
	)
)
(instance rock of Prop
	(properties
		y 107
		x 113
		view 805
		loop 2
	)
)
