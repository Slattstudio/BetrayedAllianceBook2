;;; Sierra Script 1.0 - (do not remove this comment)
(script# 229)
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
	rm229 0
)

(instance rm229 of Rm
	(properties
		picture scriptNumber
		north 0
		east 227
		south 248
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
			(104 ; tree stump
				;(gEgo posn: 209 124 loop: 3)
				(PlaceEgo 209 124 3)
				(leahProp posn: 242 115)
				(leahScript cue:)	
			)
			(227 
				;(gEgo posn: 300 130 loop: 1)
				(PlaceEgo 300 130 1)
				(leahProp posn: 242 115)
				(leahScript cue:)	
			)
			(248 
				;(gEgo posn: 200 165 loop: 3)
				(PlaceEgo 200 165 3)
				(leahProp posn: 230 160 view: 343 setCycle: Walk ignoreControl: ctlWHITE cycleSpeed: 0 setMotion: MoveTo 242 117 leahScript)
			)
			(else 
				;(gEgo posn: 150 100 loop: 1)
				(PlaceEgo 150 100 1)
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
				
				(if (checkEvent pEvent 190 231 87 109)	; stump
					(if (& (gEgo onControl:) ctlMAROON)  
						(self changeState: 1)
						;(gRoom newRoom: 104)
					else
						(PrintOther 229 1)
					)		
				)
			)
		)
		; handle Said's, etc...
		(if (Said 'talk/woman,leah')	
			(if (and (not gSeparated) gKneeHealed)
				(PrintLeah 229 10)					
			else
				(PrintCantDoThat)
			)
		)
		(if (Said 'search,read/stump,tree,trunk')
			(if (& (gEgo onControl:) ctlMAROON)  
				(self changeState: 1)
				;(gRoom newRoom: 104)
			else
				(PrintOther 229 1)
			)	
		)
			
			
		(if (Said 'look>')
			(if (Said '/tree,trunk,stump')
				(if (& (gEgo onControl:) ctlMAROON)  
					(self changeState: 1)
					;(gRoom newRoom: 104)
				else
					(PrintOther 229 1)
				)
			)
			(if (Said '[/!*]')
				(PrintOther 229 0)
			)
		)
		
	)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0 ; Handle state changes
				
			)
			(1	; move toward the stump
				(ProgramControl)
				(gEgo setMotion: MoveTo 201 119 self)
			)
			(2	(= cycles 2)
				(gEgo loop: 3)	
			)
			(3
				(PlayerControl)
				(PrintOther 229 2)
				(gRoom newRoom: 104)	
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