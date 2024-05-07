;;; Sierra Script 1.0 - (do not remove this comment)
; Score + 1
(script# 46)
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
	
	rm046 0
	
)
(local
	
	doorOpen = 0
	doorClosing = 0
	
	cabinetOpen = 0
	stepAside = 0
	
)


(instance rm046 of Rm
	(properties
		picture scriptNumber
		north 44
		east 0
		south 43
		west 38
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript setRegions: 205)
		(switch gPreviousRoomNumber
			(38 
				(PlaceEgo 52 121 0)
				;(gEgo posn: 52 121 loop: 0)
			)
			(43 
				(PlaceEgo 234 126 3)
				;(gEgo posn: 234 126 loop: 3)
			)
			(44 
				(PlaceEgo 212 116 2)
				(rightDoor cel: 3 setCycle: Beg cycleSpeed: 2)
				;(gEgo posn: 212 116 loop: 2)
			)
			(else 
				(PlaceEgo 150 100 1)
				;(gEgo posn: 150 100 loop: 1)
			)
		)
		(SetUpEgo)
		(gEgo init:)
		(RunningCheck)
		
		(cabinet init: setScript: cabinetScript ignoreActors:)
		(leftDoor init: ignoreActors: setPri: 1)
		(rightDoor init: ignoreActors: setScript: doorScript)
		
	)
)

(instance RoomScript of Script
	(properties)
	
	(method (doit)
		(super doit:)
		; code executed each game cycle
		(if (== doorOpen 1)
			(gEgo ignoreControl: ctlYELLOW)	
		else
			(gEgo observeControl: ctlYELLOW)	
		)
		
		
		(if (& (gEgo onControl:) ctlMAROON)
			(gRoom newRoom: 44)
		)
		(if (& (gEgo onControl:) ctlBLUE)
			(gRoom newRoom: 43)
		)
		(if (& (gEgo onControl:) ctlGREEN)
			(gRoom newRoom: 38)
		)
		(if (& (gEgo onControl:) ctlGREY)
			(if (not stepAside)
				(RoomScript changeState: 1)
			)
		)
		
		(if (> (gEgo distanceTo: cabinet) 30)
			(if cabinetOpen
				(= cabinetOpen 0)
				(cabinet setCycle: Beg)	
			)	
		)
		(if (> (gEgo distanceTo: rightDoor) 25)
			(if doorOpen
				(if (not doorClosing)
					(doorScript changeState: 7)
					(= doorClosing 1)
				)
			)
		)
	)
	
	
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if (& (pEvent modifiers?) emRIGHT_BUTTON)
				(if (== (OnControl ocPRIORITY (pEvent x?) (pEvent y?)) ctlGREEN )
					(PrintOther 46 9)
					(return)
				)
				(if (== (OnControl ocPRIORITY (pEvent x?) (pEvent y?)) ctlNAVY )
					(PrintOther 46 8)
					;(return)
				)
				(if	
					(checkEvent
						pEvent
						(rightDoor nsLeft?)(rightDoor nsRight?)(rightDoor nsTop?)(rightDoor nsBottom?)
					)
					(if doorOpen
						
					else
						(PrintOther 46 14)
					)
				)
				(if	
					(checkEvent
						pEvent
						(leftDoor nsLeft?)(leftDoor nsRight?)(leftDoor nsTop?)(leftDoor nsBottom?)
					)
					(PrintOther 46 7)
				)
				(if	
					(checkEvent
						pEvent
						(cabinet nsLeft?)(cabinet nsRight?)(cabinet nsTop?)(cabinet nsBottom?)
					)
					(if cabinetOpen
						(PrintOther 46 1)
						(Print 46 4 #font 4 #at 160 10 #width 130)
					else
						(if gRightClickSearch
							(if (and (<= (gEgo distanceTo: cabinet) 35) (> (gEgo y?) (cabinet y?)) )
								(cabinetScript changeState: 1)
							else
								(PrintOther 46 11) ; closed message	
							)
						else
							(PrintOther 46 11) ; closed message
						)
					)
				)
			)
		)
		
		; handle Said's, etc...
		
		(if (Said 'run')
			(Print "There's no reason to do that here.")
		)
		(if 
			(or 
				(Said 'break/wall')
				(Said 'use/hammer/wall') 
				(Said 'hit/wall/hammer')
			)
			(PrintOther 49 40)
		)
		(if 
			(or 
				(Said 'break/rock,debris')
				(Said 'use/hammer/rock,debris') 
				(Said 'hit/rock,debris/hammer')
			)
			(PrintOther 49 45)
		)
		(if 
			(or 
				(Said 'break/door')
				(Said 'use/hammer/door') 
				(Said 'hit/door/hammer')
			)
			(if (gEgo has: 3 )
				(PrintOther 43 33)
			else
				(PrintOther 43 32)
			)
		)
		(if (Said 'move,(look<behind)/painting')
			(PrintOther 46 16)	
		)
		(if (Said 'look>')
			(if (Said '/cabinet,desk,table')
				(if cabinetOpen
					(PrintOther 46 12)
				else
					(PrintOther 46 11)
				)
			)
			(if (Said '/door')
				(PrintOther 46 15)
			)
			(if (Said '/painting,picture,drawing')
				(PrintOther 46 8)
			)
			(if (Said '/note')
				(if cabinetOpen
					(PrintOther 46 1)
					(Print 46 4 #font 4 #at 160 10 #width 130)	
				else
					(PrintOther 46 3)
				)
			)
			(if (Said '/rock,rubble,debris')
				(PrintOther 46 9)
			)
			(if (Said '[/!*]')
				(PrintOther 46 10)
			)
		)
		(if (Said 'read,take,(pick<up)/note,letter')
			(if cabinetOpen
				(PrintOther 46 1)
				(Print 46 4 #font 4 #at 160 10 #width 130)	
			else
				(PrintOther 46 3)
			)	
		)
		
		(if(Said 'open/door')
			(if (< (gEgo x?) 160)
				(if (< (gEgo distanceTo: cabinet) 50)
					(cabinetScript changeState: 1)	
				else
					(if (& (gEgo onControl:) ctlSILVER)
						(Print "This door is blocked off. Perhaps there's a different way in.")
					else
						(Print "Move closer to the door you'd like to open.")
					)
				)	
			else
				(if (not doorOpen)			
					(doorScript changeState: 1)
				else
					(PrintItIs)
				)
			 )		
		)
		(if (or (Said 'unlock/door')
			(Said 'use/key'))
			(if (gEgo has: 8)
				(if (> (gEgo x?) 160)
					(if (not doorOpen)			
						(doorScript changeState: 1)
					else
						(PrintOther 46 3)
					)	
				else
					(PrintNotCloseEnough)
				)
			else
				(if (== (IsOwnedBy 8 46) TRUE)
					(PrintItIs)	
				else
					(PrintOther 46 12)
				)
			)
		)
		(if(Said 'open/cabinet')
			(cabinetScript changeState: 1)			
		)
		(if (Said 'close/cabinet')
			(if cabinetOpen
				(if (< (gEgo distanceTo: cabinet) 30)
					(= cabinetOpen 0)
					(cabinet setCycle: Beg)
				else
					(PrintNotCloseEnough)
				)
			else
				(PrintItIs)
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
				(gEgo setMotion: MoveTo (gEgo x?) (- (gEgo y?) 5) self)	
			)
			(2 (= cycles 2)
				(gEgo loop: 2)
			)
			(3
				(Print "This doorway is blocked and barricaded.")
				(PlayerControl)	
			)
		)
	)
)
(instance cabinetScript of Script
	(properties)
	
	(method (changeState newState button)
		(= state newState)
		(switch state
			(0
			)
			(1
				(ProgramControl)
				(gEgo setMotion: MoveTo 130 114 self ignoreControl: ctlWHITE)
			)
			(2 (= cycles 2)
				(gEgo loop: 3)
			)
			(3
				(gEgo observeControl: ctlWHITE)
				(if cabinetOpen
					(self cue:)
				else
					(self changeState: 5)
				)	
			)
			(4
				(PrintOther 46 1)
				(Print 46 4 #font 4 #at 160 10 #width 130)
				(PlayerControl)	
			)
			(5
				(cabinet setCycle: End self cycleSpeed: 2)	
			)
			(6
				(PrintOther 46 1)
				(Print 46 4 #font 4 #at 160 10 #width 130)
				(= cabinetOpen 1)
				(PlayerControl)	
			)
		)
	)
)

(instance doorScript of Script
	(properties)
	
	(method (changeState newState button)
		(= state newState)
		(switch state
			(0
			)
			(1
				(ProgramControl)
				(gEgo setMotion: MoveTo 212 (+ (rightDoor y?) 4) self)
			)
			(2	(= cycles 2)
				(gEgo loop: 3)	
			)
			(3	(= cycles 2)
				(if (or (== (gEgo has: 8) TRUE)	(== (IsOwnedBy 8 46) TRUE))
					(= doorOpen 1)
					(rightDoor setCycle: End cycleSpeed: 2)
					(if (== (gEgo has: 8) TRUE)
						(gEgo put: 8 46)
						(PrintOther 46 0)
						(gGame changeScore: 1)
					)
				else
					(Print "The door's locked.")
				)
			)
			(4
				(PlayerControl)	
			)
			(7
				(rightDoor setCycle: Beg self)
				(= doorClosing 1)	
			)
			(8
				(= doorOpen 0)
				(= doorClosing 0)	
			)
		)
	)
)

(procedure (PrintOther textRes textResIndex)
	(Print textRes textResIndex
		#width 280
		#at -1 140
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

(instance cabinet of Prop
	(properties
		y 110
		x 130
		view 18
		loop 9
	)
)
(instance leftDoor of Prop
	(properties
		y 107
		x 42
		view 52
	)
)
(instance rightDoor of Prop
	(properties
		y 107
		x 211
		view 52
		loop 1
	)
)