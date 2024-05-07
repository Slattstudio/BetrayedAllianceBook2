;;; Sierra Script 1.0 - (do not remove this comment)
; Score +1
(script# 45)
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
	
	rm045 0
	
)

(local
	
	doorOpen = 0
	doorClosing = 0
	
	cabinetOpen = 0
	killedByBugs = 0
	
)


(instance rm045 of Rm
	(properties
		picture scriptNumber
		north 48
		east 47
		south 41
		west 0
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript setRegions: 205)
		(switch gPreviousRoomNumber
			
			(41 
				(PlaceEgo 80 118 3)
				;(gEgo posn: 80 118 loop: 3)
			)
			(47
				(PlaceEgo 272 118 3)
				;(gEgo posn: 272 118 loop: 3)	
			)
			(48 
				(PlaceEgo 105 106 2)
				(leftDoor cel: 3 setCycle: Beg cycleSpeed: 2)
				;(gEgo posn: 105 106 loop: 2)
			)
			(else 
				(PlaceEgo 80 118 3)
				;(gEgo posn: 80 118 loop: 3)
			)
		)
		(SetUpEgo)
		(gEgo init:)
		(RunningCheck)
		
		(bugs init: hide: ignoreActors: ignoreControl: ctlWHITE setScript: fleshEatingScript)
		(cabinet init: setScript: cabinetScript ignoreActors: setPri: 1)
		(leftDoor init: ignoreActors: setScript: doorScript)
		(rightDoor init: setPri: 1)
	)
)

(instance RoomScript of Script
	(properties)
	
	(method (doit)
		(super doit:)
		; code executed each game cycle
		(if (& (gEgo onControl:) ctlMAROON)
			(gRoom newRoom: 48)
		)
		(if (& (gEgo onControl:) ctlSILVER)
			(gRoom newRoom: 49)
		)
		(if (& (gEgo onControl:) ctlBLUE)
			(gRoom newRoom: 41)
		)
		(if (& (gEgo onControl:) ctlGREY)
			(gRoom newRoom: 47)
		)
		
		(if (== doorOpen 1)
			(gEgo ignoreControl: ctlYELLOW)	
		else
			(gEgo observeControl: ctlYELLOW)	
		)
		
		(if (> (gEgo distanceTo: cabinet) 30)
			(if cabinetOpen
				(= cabinetOpen 0)
				(cabinet setCycle: Beg)	
			)	
		)
		
		(if (> (gEgo distanceTo: leftDoor) 25)
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
					(PrintOther 45 9)
					(return)
				)
				(if	
					(checkEvent
						pEvent
						(leftDoor nsLeft?)(leftDoor nsRight?)(leftDoor nsTop?)(leftDoor nsBottom?)
					)
					(if doorOpen
						
					else
						(PrintOther 45 14)
					)
				)
				(if	
					(checkEvent
						pEvent
						(rightDoor nsLeft?)(rightDoor nsRight?)(rightDoor nsTop?)(rightDoor nsBottom?)
					)
					(PrintOther 45 7)
				)
				(if	
					(checkEvent
						pEvent
						(cabinet nsLeft?)(cabinet nsRight?)(cabinet nsTop?)(cabinet nsBottom?)
					)
					(if cabinetOpen
						(PrintOther 45 12)
					else
						(if gRightClickSearch
							(if (and (<= (gEgo distanceTo: cabinet) 35) (> (gEgo y?) (cabinet y?)) )
								(cabinetScript changeState: 1)
							else
								(PrintOther 45 11) ; closed message	
							)
						else
							(PrintOther 45 11) ; closed message
						)
					)
				)	
			)
		)
		
		(if (Said 'look>')
			(if (Said '/cabinet,desk,table')
				(if cabinetOpen
					(PrintOther 45 12)
				else
					(PrintOther 45 11)
				)
			)
			(if (Said '/door')
				(PrintOther 45 15)
			)
			(if (Said '/web')
				(PrintOther 45 16)
			)
			(if (Said '/rock,rubble,debris')
				(PrintOther 45 9)
			)
			(if (Said '[/!*]')
				(PrintOther 45 10)
			)
		)
		(if(Said 'open/door')
			(if (and (< (gEgo distanceTo: cabinet) 30) (> (gEgo y?) (cabinet y?)))
				(cabinetScript changeState: 1)
			else	
				(if (< (gEgo x?) 160)
					(if (not doorOpen)			
						(doorScript changeState: 1)
					else
						(Print 45 5) ; It's already open
					)
				else
					(if (& (gEgo onControl:) ctlNAVY)
						(PrintOther 45 7) ; The rubble barricades the door too efficiently for you to use this door. Hopefully you can find a different way in to that room.
					else
						(Print 45 6) ; Move closer to the door you'd like to open.
					)
				)
			)		
		)
		(if(Said 'open,search/cabinet')
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
		(if (or (Said 'pour/oil')
			(Said 'put,rub,use,pour/oil/body,self')
			(Said 'cover/body,self/oil'))
			(if (& (gEgo has: 9) (> gOil 0))
				(if (not gEgoOiled)
					(PrintOther 45 3)
					(= gEgoOiled 1)
					(-- gOil)
					((gInv at: 9) count: gOil)
				else
					(Print "You already did that.")
				)
			else
				(Print "You can't do that right now.")
			)
		)
		
		; REMOVE AFTER TESTING;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
		(if (Said 'get/oil')
			(if (not (gEgo has: 9))
				(gEgo get: 9)
				(++ gOil)
				((gInv at: 9) count: gOil)
			)
		)
		;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
		
	)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0 ; Handle state changes
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
				(gEgo setMotion: MoveTo 180 107 self ignoreControl: ctlWHITE)
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
				(Print 45 8)
				(PlayerControl)	
			)
			(5
				(cabinet setCycle: End self cycleSpeed: 2)	
			)
			(6
				(PrintOther 45 13)
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
			(1 ; walk to door
				(ProgramControl)
				(gEgo setMotion: MoveTo 105 101 self ignoreControl: ctlWHITE)
			)
			(2	(= cycles 2)
				(gEgo loop: 3)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)	
			)
			(3
				(if (not g48Bookshelf)	; despite name, it just tracks whether ego has been in rom 48 or not. (equalling "2" determines bookshelf location )
					(self cue:)
				else
					(self changeState: 5)
				)	
			)
			(4
				(PrintOther 45 0)
				(= button 
					(Print
						{Do you still want to enter?}
						#button { Yes_} 1
						#button { No_} 0
					)
				)
				(if button
					; enter room
					(leftDoor setCycle: End cycleSpeed: 2)
					(= doorOpen 1)
					
					; is Ego oiled?
					(if gEgoOiled
						(fleshEatingScript changeState: 7) ; alive
					else
						(fleshEatingScript changeState: 1) ; dead
					)	
				else
					; don't enter
					(PrintOther 45 1)
					(PlayerControl)
				)		
			)
			(5
				(leftDoor setCycle: End self cycleSpeed: 2)
				(= doorOpen 1)		
			)
			(6
				(PlayerControl)	
			)
			(7
				(leftDoor setCycle: Beg self)
				(= doorClosing 1)	
			)
			(8
				(= doorOpen 0)
				(= doorClosing 0)	
			)
		)
	)
)
(instance fleshEatingScript of Script
	(properties)
	
	(method (changeState newState &tmp dyingScript)
		(= state newState)
		(switch state
			(0)
			(1
				(= cycles 10)
				(= killedByBugs 1)
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(bugs setPri: 15)
				(Print
					{You open the door fearlessly to confront the unknown.}
					#width
					300
					#at
					-1
					8
				)
			)
			(2
				(= cycles 20)
				(gEgo hide:)
				; swiping at bugs
				(alterEgo init: show: posn: (gEgo x?) (gEgo y?) view: 906 loop: 1 cel: 0 setCycle: Fwd cycleSpeed: 3)
				(bugs show: loop: 1 posn: (gEgo x?)(gEgo y?)  setCycle: Fwd)
			)
			(3
				(= cycles 10)
				; getting eaten
				(PrintOther 45 2)
				(alterEgo loop: 2 cel: 0 setCycle: End cycleSpeed: 5)
				;(alterEgo loop: 0 cel: 0 setCycle: CT)
			)
			(4
				(= cycles 10)
				; blinking
				(alterEgo loop: 3 cel: 0 setCycle: Fwd)
				(bugs loop: 0 setCycle: Fwd)
			)
			(5
				; bones falling
				(alterEgo loop: 0 cel: 0 setCycle: End self)
			)
			(6
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				
				(if (not [gDeaths 9])	; eaten by insects
					(++ gUniqueDeaths)
				)
				(++ [gDeaths 9])
				
				(= gCurrentCursor 999)
				(= gDeathIconEnd 1)
				
				(= dyingScript (ScriptID DYING_SCRIPT))
				(dyingScript
					caller: 720
					register:
						{Repelling as it is to your sensibilities, your skin made a nice meal for the flesh eating bugs. Perhaps try repelling them first next time!}
				)
				(gGame setScript: dyingScript)
			)
			(7	(= cycles 20); NOT killed by bugs
				(ProgramControl)
				(bugs show: loop: 1 posn: (gEgo x?)(gEgo y?)  setCycle: Fwd)
				(gEgo loop: 3)
			)
			(8	
				(PrintOther 45 4)
				(bugs loop: 2 cel: 0 setCycle: End self)	
			)
			(9
				(PlayerControl)
				(gGame changeScore: 1)
				;(gEgo loop: 2)	
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

(instance alterEgo of Prop
	(properties
		y 130
		x 150
		view 311
	)
)
(instance bugs of Act
	(properties
		y 105
		x 168
		view 78
	)
)

(instance cabinet of Prop
	(properties
		y 102
		x 180
		view 18
		loop 8
	)
)
(instance leftDoor of Prop
	(properties
		y 99
		x 108
		view 52
	)
)
(instance rightDoor of Prop
	(properties
		y 99
		x 277
		view 52
		loop 1
	)
)
