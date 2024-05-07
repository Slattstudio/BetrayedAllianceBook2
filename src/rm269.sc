;;; Sierra Script 1.0 - (do not remove this comment)
(script# 269)
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
	
	rm269 0
	
)
(local
	
	onRock = 0
	onMud = 0
	
)

(instance rm269 of Rm
	(properties
		picture scriptNumber
		north 0
		east 271
		south 0
		west 268
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript setRegions: 205)
		(switch gPreviousRoomNumber
			(268
				(PlaceEgo 29 175 2)
				(= onRock 1)	
			)
			(271 
				(if (> (gEgo y?) 150)   ; on bridge
					(PlaceEgo 305 170 1)
					;(gEgo posn: 305 170 loop: 1)
				else
					(PlaceEgo 305 130 1)
					;(gEgo posn: 305 130 loop: 1)
				)
				;(gEgo posn: 305 170 loop: 1)
			)
			(else
				(PlaceEgo 200 150 2)		
			)
		)
		(SetUpEgo)
		(gEgo init:)
		
		(alterEgo init: ignoreActors: ignoreControl: ctlWHITE hide:)
	)
)

(instance RoomScript of Script
	(properties)
	
	(method (doit)
		(super doit:)
		; code executed each game cycle
		(if (& (gEgo onControl:) ctlYELLOW)
			(if (not onRock)
				(if (not onMud)
					(= onMud 1)
					(RoomScript changeState: 8)
				)
			)
		)
	)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if (& (pEvent modifiers?) emRIGHT_BUTTON)
				
			)
			(if (> (pEvent x?) (gEgo x?))
				(if onRock
					(RoomScript changeState: 1)
				)
			
			else
				(if onRock
					(RoomScript changeState: 6)
				)
			)
		)
					
		(if (== (pEvent type?) evJOYSTICK)
			(if (< (pEvent message?) 5) ; if player presses up, right, right-up, or right-down
				(if onRock
					(RoomScript changeState: 1)
				)
			)
			(if (and (> (pEvent message?) 4) (< (pEvent message?) 9) )   ; if player presses down, left, left-up, or left-down
				(if onRock
					(RoomScript changeState: 6)
				)
			)
		)
		; handle Said's, etc...
		
		(if (Said 'look>')
			(if (Said '/hill')
				(PrintOther 269 1)
			)
			(if (Said '[/!*]')
				(PrintOther 269 0)
			)
		)
		(if (Said 'smell')
			(PrintOther 269 2)	
		)
		
	)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0 ; Handle state changes
			)
			(1 ; running up the hill
				(ProgramControl)
				(gEgo hide:)
				(= onRock 0)
				(= onMud 1)
				(alterEgo show: view: 351 posn: (gEgo x?) (gEgo y?)
					xStep: 1 yStep: 1 cycleSpeed: 0 setMotion: MoveTo 121 153 self setCycle: Walk) 
			)
			(2 (= cycles 32) ; heavy breathing
				(alterEgo view: 454 loop: 1 cel: 0 setCycle: Fwd cycleSpeed: 6)	
			)
			(3	; wiping brow
				(alterEgo loop: 0 cel: 0 setCycle: End self cycleSpeed: 2)	
			)
			(4 (= cycles 14) ; allowing time to smile
			
			)
			(5	; standing on rock
				(gEgo show: posn: (alterEgo x?)(alterEgo y?) loop: 2)
				(alterEgo hide:)
				(= onMud 0)
				(PlayerControl)				
			)
			; Going down the hill
			(6
				(ProgramControl)
				(gEgo hide:)
				(= onRock 0)
				(= onMud 1)
				(alterEgo show: view: 351 posn: (gEgo x?) (gEgo y?)
					xStep: 1 yStep: 1 cycleSpeed: 0 setMotion: MoveTo 15 180 self setCycle: Walk) 
			)
			(7
				(PlayerControl)
				(gRoom newRoom: 268)	
			)
			(8 ; running down to the rock
				(ProgramControl)
				(gEgo hide:)
				(alterEgo show: view: 351 posn: (gEgo x?) (gEgo y?)
					xStep: 3 yStep: 1 cycleSpeed: 0 setMotion: MoveTo 29 175 self setCycle: Walk) 
			)
			(9	; standing on rock
				(gEgo show: posn: (alterEgo x?)(alterEgo y?) loop: 2)
				(alterEgo hide:)
				(= onRock 1)
				(= onMud 0)
				(PlayerControl)	
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

(instance alterEgo of Act
	(properties
		y 180
		x 27
		view 0
		loop 1
	)
)
