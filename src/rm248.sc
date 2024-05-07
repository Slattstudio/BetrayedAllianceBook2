;;; Sierra Script 1.0 - (do not remove this comment)
(script# 248)
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
	
	rm248 0
	
)


(instance rm248 of Rm
	(properties
		picture scriptNumber
		north 229
		east 254
		south 34
		west 247
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript setRegions: 200 setRegions: 205)
		
		(if gKneeHealed
			(if (not gSeparated)
				(leahProp init: setScript: leahScript ignoreControl: ctlWHITE) 
			)
		)
		(switch gPreviousRoomNumber
			(34 ; from south  
				(PlaceEgo 166 180 3)
				;(gEgo posn: 166 180 loop: 3)
				(leahProp posn: 115 109)
				(leahScript cue:)
			)
			(229 ; from north  
				(PlaceEgo 166 110 2)
				;(gEgo posn: 166 110 loop: 2)
				(leahProp posn: 151 108 view: 343 setCycle: Walk setMotion: MoveTo 115 109 leahScript)
			)
			(247 
				(PlaceEgo 20 135 0)
				;(gEgo posn: 20 135 loop: 0)
				(leahProp posn: 20 148 view: 343 setCycle: Walk setMotion: MoveTo 115 109 leahScript)
			)
			(254 
				(PlaceEgo 300 135 1)
				;(gEgo posn: 300 135 loop: 1)
				(leahProp posn: 290 124 view: 343 setCycle: Walk setMotion: MoveTo 115 109 leahScript)
			)
			(else 
				(PlaceEgo 300 135 1)
				;(gEgo posn: 150 100 loop: 1)
			)
		)
		(SetUpEgo)
		(gEgo init:)
		(RunningCheck)
	)
)

(instance RoomScript of Script
	(properties)
	
	(method (doit)
		(super doit:)
		; code executed each game cycle
		(if (& (gEgo onControl:) ctlMAROON)
			(gRoom newRoom: 229)
		)
	)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if (& (pEvent modifiers?) emRIGHT_BUTTON)
				(if (== (OnControl ocPRIORITY (pEvent x?) (pEvent y?)) ctlNAVY)	; tree stump
					(PrintOther 248 1)					
				)
				(if
					(checkEvent pEvent (leahProp nsLeft?) (leahProp nsRight?) (leahProp nsTop?) (leahProp nsBottom?))
					(if gKneeHealed
						(if (not gSeparated)
							(PrintOther 200 29)
							(return)	
						)
					)
				)
				(if (== (OnControl ocPRIORITY (pEvent x?) (pEvent y?)) ctlGREEN)	; masonry
					(PrintOther 248 3)					
				)
				(if (== (OnControl ocPRIORITY (pEvent x?) (pEvent y?)) ctlTEAL)	; masonry
					(PrintOther 248 2)					
				)	
			)
		)
		
		(if (Said 'talk/woman,leah')	
			(if (and (not gSeparated) gKneeHealed)
				(PrintLeah 248 5)					
			else
				(PrintCantDoThat)
			)
		)
		
		(if (Said 'look>')
			(if (Said '/stump')
				(PrintOther 248 1)
			)
			(if (Said '/stone,masonry,symbol')
				(PrintOther 248 3)
			)
			(if (Said '/carving, wall')
				(PrintOther 248 2)
			)
			(if (Said '/forest,verlorn')
				(PrintOther 0 105)	
			)
			(if (Said '[/!*]')
				(PrintOther 248 0)
			)
		)
		; handle Said's, etc...
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
		y 150
		x 160
		view 1
	)
)