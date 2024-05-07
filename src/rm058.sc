;;; Sierra Script 1.0 - (do not remove this comment)
(script# 58)
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
	rm058 0
)

(local
	comeOrGo = 0
	myEvent	
)

(instance rm058 of Rm
	(properties
		picture scriptNumber
		north 59
		east 0
		south 0
		west 57
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript)
		(switch gPreviousRoomNumber
			(57	; west
				(PlaceEgo 15 120 0)			
			)
			(59	; north
				(PlaceEgo 160 40 2)		
			)
			(else 
				(gEgo posn: 150 100 loop: 1)
			)
		)
		(SetUpEgo)
		(gEgo init:)
		(RunningCheck)
		
		(actionEgo init: hide: ignoreActors: setScript: enterScript)
		(log init: ignoreControl: ctlWHITE)
		
		(if (< g58LogState 2)
			(log init:)
		else
			(log hide: ignoreActors:)
		)
		
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
				(gRoom newRoom: (gRoom north:))		
			)
		)
		(if (== (pEvent type?) evKEYBOARD)
			(if gLookingAhead
				(if (== (pEvent message?) KEY_ESCAPE)
					(gRoom newRoom: (gRoom north:))	
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
					(gRoom newRoom: (gRoom north:))		
				)
			)
		)
		
		(if gLookingAhead
			(if (== (pEvent type?) evJOYSTICK)
				(if (== (pEvent message?) 1) 
					(gRoom newRoom: (gRoom north:))	
				)
			)
		)
		
		; handle Said's, etc...
		(if (Said 'move,push/log,wood')
			(if (and (> (gEgo y?) (log y?)) (<= (gEgo distanceTo: log) 40))
				(switch g58LogState
					(0
						(self changeState: 1)		
					)
					(1
						(self changeState: 1)	
					)
					(else
						(Print "There is no log here to push.")	
					)	
				)
			else
				(if (> g58LogState 1)
					(Print "There is no log here to push.")		
				else
					(PrintNotCloseEnough)
				)
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
				(gEgo setMotion: MoveTo (log x?) (+ (log y?) 3) self )	
			)
			(2
				(switch g58LogState
					(0
						(self cue:)
					)
					(1
						(self changeState: 5)
					)	
				)	
			)
			(3	; pushing log
				(gEgo yStep: 1 setMotion: MoveTo (gEgo x?) (- (gEgo y?) 20) self)
				(log yStep: 1 setMotion: MoveTo (log x?) (- (log y?) 20))	
			)
			(4
				(RunningCheck)
				(gEgo loop: 3)
				(= g58LogState 1)
				(PlayerControl)	
			)
			(5
				; pushing log to a new screen
				(gEgo yStep: 1 setMotion: MoveTo (gEgo x?) 10 self)
				(log yStep: 1 setMotion: MoveTo (log x?) 6)		
			)
			(6
				(= g58LogState 4)	; 4 used to indicate the log is being pushed
				(gRoom newRoom: 59)	
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
				(actionEgo show: view: 343 posn: 160 0 loop: 1 cel: 0 setCycle: Walk setMotion: MoveTo 160 40 self)
			)
			(2
				(= gMap 0)
				(= gArcStl 0)
				(actionEgo hide:)
				(gEgo show: posn: 160 40 loop: 2)
				(PlayerControl)
				(TheMenuBar state: ENABLED)
			)
			(3
				
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
	(if (> (gEgo y?) 120)
		(Print textRes textResIndex #width 280 #at -1 10)
	else
		(Print textRes textResIndex #width 280 #at -1 140)
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

(instance log of Act
	(properties
		y 92
		x 175
		view 99
	)
)