;;; Sierra Script 1.0 - (do not remove this comment)
(script# 244)
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
	rm244 0
)
(local
	cutOffHead = 0	
)

(instance rm244 of Rm
	(properties
		picture scriptNumber
		north 246
		east 0
		south 243
		west 0
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
			(243 
				;(gEgo posn: 167 165 loop: 3)
				(PlaceEgo 167 165 3)
				(leahProp posn: 180 160 view: 343 setCycle: Walk setMotion: MoveTo (+ (statue x?) 25) (+ (statue y?) 8) leahScript)
			)
			(246 
				;(gEgo posn: 177 75 loop: 2)
				(PlaceEgo 177 75 2)
				(leahProp posn: 190 80 view: 343 setCycle: Walk setMotion: MoveTo (+ (statue x?) 16) (- (statue y?) 12) leahScript)
			)
			(else 
				(PlaceEgo 150 100 1)
				;(gEgo posn: 150 100 loop: 1)
			)
		)
		(SetUpEgo)
		(gEgo init:)
		(RunningCheck)
		
		(statue init: ignoreActors:)
		(otherEgo init: hide:)
		(actionEgo init: hide: ignoreActors:)
	)
)

(instance RoomScript of Script
	(properties)
	
	(method (doit)
		(super doit:)
		; code executed each game cycle
		(if (& (gEgo onControl:) ctlMAROON)
			(gRoom newRoom: 246)
		)
		
		(if (and
			(or (== (gEgo loop?) 2)(== (gEgo loop?) 3))
			(>= (gEgo y?) (- (statue y?) 2))
			(< (gEgo y?) (+ (statue y?) 1)) 
			(> (gEgo x?) (statue nsLeft?))
			(< (gEgo x?) (- (statue nsRight?) 35)))
			
			(if (not cutOffHead)
				;(Print "Dead!" #at -1 140)
				(RoomScript changeState: 1)	; death
				(= cutOffHead 1)
			)
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
						)
					)
				else
					(if
						(checkEvent pEvent
							(statue nsLeft?)
							(statue nsRight?)
							(statue nsTop?)
							(statue nsBottom?)
						)
						(if (== g246KnockedDown 2)
							(PrintOther 244 10)
						else
							(PrintOther 244 0)
						)
					)
				)
			)
		)
		
		; handle Said's, etc...
		(if (Said 'talk/woman,leah')	
			(if (and (not gSeparated) gKneeHealed)
				(if g246KnockedDown
					(PrintLeah 244 4)
				else
					(PrintLeah 244 5)
				)	
			else
				(PrintCantDoThat)
			)
		)
		(if (Said 'look>')
			(if (Said '/statue,man')
				(PrintOther 244 0)	
			)
			(if (Said '/sword')
				(PrintOther 244 7)	
			)
			(if (Said '/face,expression')
				(PrintOther 244 1)	
			)
			(if (Said '/forest,verlorn')
				(PrintOther 0 105)	
			)
			(if (Said '[/!*]') ; just "look"
				(PrintOther 244 3)	
			)
			;(if (Said '/*')	; "look whatever"
			;	(PrintOther 244 2)		
			;)
		)
		(if (Said 'take,steal/sword')
			(PrintOther 244 6)
		)
	)
	
	(method (changeState newState dyingScript)
		(= state newState)
		(switch state
			(0 ; Handle state changes
			)
			(1	;Head cut off
				(ProgramControl)
				(if (== (gEgo loop?) 3)
					(gEgo setMotion: MoveTo (gEgo x?)(- (gEgo y?) 2) self)
				else
					(gEgo setMotion: MoveTo (gEgo x?)(+ (gEgo y?) 5) self)
				)
			)
			(2
				(gEgo hide:)
				(actionEgo show: view: 430 posn: (gEgo x?) (gEgo y?) loop: (gEgo loop?) cel: 0 setCycle: End self cycleSpeed: 3)	
			)
			(3
				;(= gDeathIconTop 1)
				(if (not [gDeaths 5])
					(++ gUniqueDeaths)
				)
				(++ [gDeaths 5])
				(= gDeathIconEnd 1)
				(= dyingScript (ScriptID DYING_SCRIPT))
				(dyingScript
					caller: 701
					register:
						{\nHeaded out so early? Hats off to you for playing Betrayed Alliance! Have fun and stay sharp!}
				)
				(gGame setScript: dyingScript)	
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
		y 150
		x 160
		view 1
	)
)
(instance statue of Prop
	(properties
		y 130
		x 190
		view 30
	)
)
(instance otherEgo of Act
	(properties
		y 160
		x 170
		view 1
		loop 3
	)
)