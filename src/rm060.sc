;;; Sierra Script 1.0 - (do not remove this comment)
(script# 60)
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
	
	rm060 0
	
)

(local
	comeOrGo = 0
	myEvent	
)

(instance rm060 of Rm
	(properties
		picture scriptNumber
		north 0
		east 59
		south 0
		west 0
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript)
		(switch gPreviousRoomNumber
			(59
				(PlaceEgo 300 110 1)	
			)
			(else 
				(gEgo posn: 150 100 loop: 1)
			)
		)
		(SetUpEgo)
		(gEgo init:)
		
		(actionEgo init: hide: ignoreActors: setScript: enterScript)
		
		(if gLookingAhead
			;(= entering 1)
			(enterBackFrame init: ignoreActors: setPri: 15)
			(enterButton init: ignoreActors: setPri: 15)
			(backButton init: ignoreActors: setPri: 15)

			(= gMap 1)
			(= gArcStl 1)
			(gEgo hide:)
		)
	)
)

(instance RoomScript of Script
	(properties)
	
	(method (doit)
		(super doit:)
		; code executed each game cycle
		(if gLookingAhead
			(= myEvent (Event new: evNULL))
			(if
					(checkEvent
						myEvent
						(backButton nsLeft?)
						(backButton nsRight?)
						(+ (backButton nsTop?) 7)
						(+ (backButton nsBottom?) 7)
					)
					(= comeOrGo 2)
					(backButton cel: 1)
					(enterButton cel: 0)
					
					
			else
				(if
					(checkEvent
						myEvent
						(enterButton nsLeft?)
						(enterButton nsRight?)
						(+ (enterButton nsTop?) 7)
						(+ (enterButton nsBottom?) 7)
					)
					(= comeOrGo 1)
					(backButton cel: 0)
					(enterButton cel: 1)
				else
					(= comeOrGo 0)
					(backButton cel: 0)
					(enterButton cel: 0)
				)
			)
			(myEvent dispose:)
		)
	)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if (& (pEvent modifiers?) emRIGHT_BUTTON)
				; right click look
			)
			(if (== comeOrGo 1)
				(enterScript changeState: 1)
				
				(= gLookingAhead 0)
				(= comeOrGo 0)
				(backButton hide:)
				(enterButton hide:)
				(enterBackFrame hide:)
			)
			(if (== comeOrGo 2)
				(gRoom newRoom: (gRoom east:))		
			)
		)
		(if (== (pEvent type?) evKEYBOARD)
			(if gLookingAhead
				(if (== (pEvent message?) KEY_ESCAPE)
					(gRoom newRoom: (gRoom east:))	
				)
			)
		)
		
		(if gLookingAhead
			(if (== (pEvent type?) evKEYBOARD)
				(if (or (== (pEvent message?) KEY_e) (== (pEvent message?) KEY_RETURN)) ; enter
					(enterScript changeState: 1)				
					(= gLookingAhead 0)
					(= comeOrGo 0)
					(backButton hide:)
					(enterButton hide:)
					(enterBackFrame hide:)
				)
				(if (== (pEvent message?) KEY_b)	; back
					(gRoom newRoom: (gRoom east:))		
				)
			)
		)
		
		(if gLookingAhead
			(if (== (pEvent type?) evJOYSTICK)
				(if (== (pEvent message?) 3) 
					(gRoom newRoom: (gRoom east:))	
				)
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

(instance enterScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)
			(1 ; 
				(ProgramControl)
				(gEgo hide:)
				(actionEgo show: view: 343 posn: 320 130 loop: 1 cel: 0 setCycle: Walk setMotion: MoveTo 300 130 self)
			)
			(2
				(= gMap 0)
				(= gArcStl 0)
				(actionEgo hide:)
				(gEgo show: posn: 300 130 loop: 0)
				(PlayerControl)
				(TheMenuBar state: ENABLED)
			)
			(3
				
			)
		)
	)
)

(procedure (PrintOther textRes textResIndex)
	(if (> (gEgo y?) 120)
		(Print textRes textResIndex #width 280 #at -1 10)
	else
		(Print textRes textResIndex #width 280 #at -1 140)
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
(instance actionEgo of Act
	(properties
		y 230
		x 170
		view 310
		loop 1
	)
)
(instance enterBackFrame of Prop
	(properties
		y 180
		x 280
		view 983
	)
)
(instance enterButton of Prop
	(properties
		y 158
		x 280
		view 983
		loop 1
	)
)
(instance backButton of Prop
	(properties
		y 178
		x 280
		view 983
		loop 2
	)
)