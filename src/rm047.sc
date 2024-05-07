;;; Sierra Script 1.0 - (do not remove this comment)
; Score +4
(script# 47)
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
	
	rm047 0
	
)

(local
	
	doorOpen = 0
	doorClosing = 0
	
	wherePrint = 40
		
)


(instance rm047 of Rm
	(properties
		picture scriptNumber
		north 45
		east 39
		south 0
		west 41
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript 
			setRegions: 205
		)
		
		(switch gPreviousRoomNumber
			(39
				(PlaceEgo 215 110 1)
				;(gEgo posn: 215 110 loop: 1)
			)
			(45
				(PlaceEgo 184 90 2)
				;(gEgo posn: 184 90 loop: 2)
				(door cel: 3 setCycle: Beg cycleSpeed: 2)	
			)
			(else 
				(PlaceEgo 150 100 1)
				;(gEgo posn: 150 100 loop: 1)
			)
		)
		(SetUpEgo)
		(gEgo init: observeControl: ctlYELLOW)
		(RunningCheck)
		
		(bed init:)
		(chair1 init: cel: 2)
		(chair2 init: ignoreActors:)
		(closet init:)
		(closetDoor init: ignoreActors: setPri: 0)
		(door init: ignoreActors: setScript: doorScript)
		(bricks init: setScript: breakScript ignoreActors: setPri: 1)
		(alterEgo init: hide: ignoreActors:)
		
		(swingStream init: hide: ignoreActors: setPri: 15)
		(hammerItem init: ignoreActors: setScript: takeHammerScript)
		(table init:)
		
		(if 
			(or 
				(or (gEgo has: INV_HAMMER) 
					(== (IsOwnedBy INV_HAMMER 210) 1)
				)
				(== (IsOwnedBy INV_HAMMER 42) 1)
			)
			(hammerItem hide:)
		)
		
		(if g47WallBroken
			(bricks cel: 6)
			(gEgo ignoreControl: ctlYELLOW)
		)
	)
)

(instance RoomScript of Script
	(properties)
	
	(method (doit)
		(super doit:)
		; code executed each game cycle
		
		(if (& (gEgo onControl:) ctlMAROON)
			(gRoom newRoom: 45)
		)
		(if (& (gEgo onControl:) ctlGREY)
			(gRoom newRoom: 39)
		)
		
		(if (== doorOpen 1)
			(gEgo ignoreControl: ctlFUCHSIA)	
		else
			(gEgo observeControl: ctlFUCHSIA)	
		)
		(if (> (gEgo distanceTo: door) 25)
			(if doorOpen
				(if (not doorClosing)
					(doorScript changeState: 4)
					(= doorClosing 1)
				)
			)
		)
	)
	
	(method (handleEvent pEvent &tmp i)
		(super handleEvent: pEvent)
		
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if (& (pEvent modifiers?) emRIGHT_BUTTON)
				(if
					(checkEvent
						pEvent
						(bed nsLeft?)(bed nsRight?)(bed nsTop?)(bed nsBottom?)
					)
					(if gRightClickSearch
						(if (<= (gEgo distanceTo: bed) 45)
							(self changeState: 5)	
						else
							(PrintOther 47 10)
						)
					else
						(PrintOther 47 10)
					)
				)
				(if
					(checkEvent
						pEvent
						(closet nsLeft?)(closet nsRight?)(closet nsTop?)(closet nsBottom?)
					)
					(if gRightClickSearch
						(if (& (gEgo onControl:) ctlSILVER)
							(self changeState: 9)
						else
						(PrintOther 47 11)
						)
					else
						(PrintOther 47 11)
					)
				)
				(if
					(checkEvent
						pEvent
						(hammerItem nsLeft?) (hammerItem nsRight?) (hammerItem nsTop?) (hammerItem nsBottom?)
					)
					(if (not (or (== (IsOwnedBy INV_HAMMER 205) 1) (gEgo has: 3) ))

					(PrintOther 47 12)
					)
				else
					(if
						(checkEvent
							pEvent
							(bricks nsLeft?)(bricks nsRight?)(bricks nsTop?)(bricks nsBottom?)
						)
						(if (not g47WallBroken)
							(PrintOther 47 17)
						)
					)
				)
				(if
					(checkEvent
						pEvent
						(table nsLeft?)(table nsRight?)(table nsTop?)(table nsBottom?)
					)
					(PrintOther 47 13)
				else
					(if
						(or (checkEvent
							pEvent
							(chair1 nsLeft?)(chair1 nsRight?)(chair1 nsTop?)(chair1 nsBottom?)
							)
							(checkEvent
							pEvent
							(chair2 nsLeft?)(chair2 nsRight?)(chair2 nsTop?)(chair2 nsBottom?)
							)
						)
						(PrintOther 47 14)
					)
				)
				(if
					(checkEvent
						pEvent
						(closetDoor nsLeft?)(closetDoor nsRight?)(closetDoor nsTop?)(closetDoor nsBottom?)
					)
					(if gRightClickSearch
						(if (<= (gEgo distanceTo: closetDoor) 25)
							(self changeState: 12)
						else
							(PrintOther 47 15)
						)
					else
						(PrintOther 47 15)
					)
				)
				(if
					(checkEvent
						pEvent
						(door nsLeft?)(door nsRight?)(door nsTop?)(door nsBottom?)
					)
					(PrintOther 47 16)
				)
				
				(if
					(checkEvent
						pEvent 108 121 91 96	; shirt on ground
					)
					(self changeState: 1)
				)
			)
		)
		; handle Said's, etc...
		;(if(Said 'drop/hammer')
			;(gEgo put: INV_HAMMER 205)	
		;)
		(if (Said 'run')
			(Print "There's no reason to do that here.")
		)
		(if(Said 'open>')
			(if (Said '/door')
				(if (<= (gEgo distanceTo: door) 45)
					(if (not doorOpen)
						(if (not doorClosing)
							(doorScript changeState: 1)
						else
							(PrintOther 47 19)
						)
					else
						(PrintOther 47 19)
					)
				else
					(PrintNotCloseEnough)
				)
			)
			(if (Said '/cabinet')
				(PrintOther 47 36)	
			)
		)
		(if (Said 'close/door')
			(if doorOpen
				(if (<= (gEgo distanceTo: door) 45)				
					(= doorOpen 0)
					(door setCycle: Beg)
				else
					(PrintNotCloseEnough)
				)
			else
				(PrintItIs)
			)			
		)
		(if (Said 'sit>')
			(if (Said '/chair')
				(PrintOther 47 6)
			)
			(if (Said '/bed')
				(PrintOther 47 7)
			)
			(if (Said '[/!*]')
				(PrintOther 47 8)
			)
			(if (Said '/*')
				(PrintOther 47 9)
			)
				
		)
		(if (Said 'take,examine, lift,move,(pick<up),(look<under)/board,(door[<cabinet])')
				;(Said 'look<under/door'))
			(self changeState: 12)
		)
		(if (Said 'move,push,pull>')
			(if (Said '/*')
				(PrintShort 47 37)
			)	
		)
		(if (Said 'take,(pick<up)>')
			(if (Said '/hammer')
				(if (gEgo has: 3)
					(Print "You already have it.")
				else
					(if (or (== (IsOwnedBy INV_HAMMER 210) 1)	; leah has it
							(== (IsOwnedBy INV_HAMMER 42) 1))
						(Print "There's no hammer here.")
					else
						(takeHammerScript changeState: 1)
					)
				)
			)
			(if (Said '/clothes,shirt,pants')
				(self changeState: 9)
				;(PrintOther 47 32)
			)
			(if (Said '/*')
				(PrintShort 47 33)
			)
		)
		(if (Said 'touch/chair')
			(PrintOther 47 35)
		)
		(if (Said 'wear/clothes,shirt,pants')
			(PrintOther 47 32)
		)
		(if (Said 'clean,(tidy<up)/room,floor')
			(PrintOther 47 34)
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
		(if 
			(or 
				(Said 'break/wall')
				(Said 'use/hammer/wall') 
				(Said 'hit/wall/hammer')
			)
			(if g47WallBroken
				(PrintOther 47 25)	; The wall is already broken.
			else
				(if (gEgo has: 3)
					(breakScript changeState: 1)
				else
					(PrintOther 47 24) ; A great idea, but you don't have anything to break it with.
				)
			)
		)
		(if 
			(or 
				(Said 'break/*')
				(Said 'use/hammer/*') 
			)
			(if (gEgo has: 3)
				(Print 47 27 #at -1 140)
			else
				(PrintOther 47 26)
			)
		)
		(if (Said 'hit/wall/*')
			(PrintOther 47 31)		
		)
		(if (Said 'hit/wall')
			(PrintOther 47 29)		
		)
		(if (Said 'use/hammer')
			(if (gEgo has: 3)
				(PrintOther 47 30)
			else
				(PrintDontHaveIt)
			)		
		)
		(if (Said 'search,(look<through,under)/cabinet,closet,clothes')
			(self changeState: 9)
		)
		(if (Said 'sleep')
			(PrintOther 47 10)	
		)
		(if (Said '(jump<on)/bed')
			(Print 47 28 #at -1 140)	
		)
		(if (Said 'look/(shirt,clothes)/floor')
			(self changeState: 1)
		)
		(if (Said '(look<under),search/bed,mattress')
			(self changeState: 5)
		)
		(if (Said 'look>')
			(if (Said '/board,(door<cabinet)')
				(PrintOther 47 15)
			)
			(if (Said '/floor')
				(PrintOther 47 41)
			)
			(if (Said '/shirt,(floor<clothes)')
				(self changeState: 1)
			)
			(if (Said '/cabinet,closet,clothes')
				(if (<= (gEgo distanceTo: closet) 30)	
					(self changeState: 9)
				else
					(PrintOther 47 11)
				)	
			)
			(if (Said '/hammer')
				(if (not (or (== (IsOwnedBy INV_HAMMER 205) 1) (gEgo has: 3) ))
					(PrintOther 47 12)
				else
					(Print 0 2 #title "Hammer" #icon 612)
				)
			)
			(if (Said '/bed')
				(PrintOther 47 10)
			)
			(if (Said '/table')
				(PrintOther 47 13)
			)
			(if (Said '/chair')
				(PrintOther 47 14)
			)
			(if (Said '/door')
				(PrintOther 47 16)
			)
			(if (Said '/wall')
				(if (not g47WallBroken)
					(PrintOther 47 17)
				else
					(PrintOther 47 38)
				)
			)			
			(if (Said '[/!*]')
				(PrintOther 47 40)
				(if (or 
					(gEgo has: INV_HAMMER) 
					(== (IsOwnedBy INV_HAMMER 210) 1)
					(== (IsOwnedBy INV_HAMMER 42) 1))
				else
					(PrintOther 47 12)
				)
			)
		)
		
	)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0 ; Handle state changes
			)
			(1	; walking to clothes on the floor
				(ProgramControl)
				(gEgo setMotion: MoveTo 135 95 self ignoreControl: ctlWHITE)
			)
			(2	; stoop down
				(gEgo hide:)
				(alterEgo show: posn: (gEgo x?)(gEgo y?) view: gEgoPickUpView cel: 0 loop: 1 setCycle: End self cycleSpeed: 3)
			)
			(3	; stand up
				(PrintOther 47 1)
				(alterEgo setCycle: Beg self)
			)
			(4	; return control
				(PlayerControl)
				(alterEgo hide:)
				(gEgo show: observeControl: ctlWHITE)
			)
			; checking underneath the bed
			
			(5	; walking to bed
				(ProgramControl)
				(gEgo setMotion: MoveTo 122 153 self ignoreControl: ctlWHITE)
			)
			(6	; stoop down
				(gEgo hide:)
				(alterEgo show: posn: (gEgo x?)(gEgo y?) view: gEgoPickUpView cel: 0 loop: 1 setCycle: End self cycleSpeed: 3)
			)
			(7	; stand up
				(PrintUp 47 2)
				(alterEgo setCycle: Beg self)
			)
			(8	; return control
				(PlayerControl)
				(alterEgo hide:)
				(gEgo show: observeControl: ctlWHITE)
			)
			; checking the closet
			
			(9	; walking to closet
				(ProgramControl)
				(gEgo setMotion: MoveTo 129 90 self ignoreControl: ctlWHITE)
			)
			(10	(= cycles 2)
				(gEgo loop: 3)
			)
			(11	; return control
				(PlayerControl)
				(switch [gNotes 2]
					(0	; nothing found yet
						(PrintOther 47 3)
						(Print 47 4 #at -1 140)
						(PrintOther 47 5)
						(= [gNotes 2] 3)	; note found in closet but not under board
						(readNote)
						(gGame changeScore: 1)		
					)
					(1	;if note found underboard
						(PrintOther 47 3)
						(Print 47 4 #at -1 140)
						(PrintOther 47 5)
						(= [gNotes 2] 2)
						(readNote)
						(gGame changeScore: 1)
					)
					(else	; 2 or 3 = already got it
						(PrintOther 47 22)
					)
				) 
				(gEgo show: observeControl: ctlWHITE)
			)
			
			; Checking under the cabinet door
			(12	; walking to board
				(ProgramControl)
				(gEgo setMotion: MoveTo 173 102 self ignoreControl: ctlWHITE)
			)
			(13	; stoop down
				(gEgo hide:)
				(alterEgo show: posn: (gEgo x?)(gEgo y?) view: gEgoPickUpView cel: 0 loop: 1 setCycle: End self cycleSpeed: 3)
			)
			(14	(= cycles 5)
				(closetDoor cel: 1)
			)
			(15	; stand up
				(switch [gNotes 2]
					(0	; nothing found yet
						(PrintOther 47 20)
						(= [gNotes 2] 1)	; note found in closet but not under board
						(readNote)
						(gGame changeScore: 1)		
					)
					(3	;if note found underboard
						(PrintOther 47 21)
						(= [gNotes 2] 2)
						(readNote)
						(gGame changeScore: 1)
					)
					(else	; 2 or 1 = already got it
						(PrintOther 47 39)
					)
				)
				(alterEgo setCycle: Beg self)
				(closetDoor cel: 0)
			)
			(16	; return control
				(PlayerControl)
				(alterEgo hide:)
				(gEgo show: loop: 1 observeControl: ctlWHITE)
			)
		)
	)
)

(instance doorScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0
			)
			(1
				(ProgramControl)
				(gEgo setMotion: MoveTo 188 83 self)
			)
			(2
				(door setCycle: End self cycleSpeed: 2)
				(gEgo loop: 3)
				(= doorOpen 1)
			)
			(3
				(PlayerControl)	
			)
			; closing the door
			(4
				(door setCycle: Beg self)
				(= doorClosing 1)	
			)
			(5
				(= doorOpen 0)
				(= doorClosing 0)	
			)
		)
	)
)
(instance takeHammerScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0
			)
			(1
				(ProgramControl)
				(gEgo setMotion: MoveTo 215 130 self)	
			)
			(2
				(gEgo hide:)
				(hammerItem hide:)
				(alterEgo show: posn: (gEgo x?)(gEgo y?) view: 51 loop: 5 cel: 0 setCycle: End self cycleSpeed: 3)	
			)
			(3
				(gEgo show:)
				(alterEgo hide:)
				(gEgo get: 3)
				(PlayerControl)
				
				(gGame changeScore: 1)
			)
		)
	)
)
(instance breakScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0
			)
			(1
				(ProgramControl)
				(gEgo setMotion: MoveTo 215 115 self)
			)
			(2
				(gEgo hide:)
				(alterEgo show: posn: (gEgo x?)(gEgo y?) view: 51 loop: 0 cel: 0 setCycle: End self cycleSpeed: 2)	
			)
			(3 (= cycles 8)
			)
			(4
				(alterEgo view: 51 loop: 1 cel: 0 setCycle: End self)
			)
			(5 (= cycles 10)
			)
			(6
				(alterEgo view: 51 loop: 2 cel: 0 setCycle: End self)
				(bricks setCycle: End cycleSpeed: 2)
				(swingStream show: setCycle: End cycleSpeed: 1)
				
			)
			(7
				(alterEgo view: 51 loop: 4 cel: 0 setCycle: End self)
			)
			(8
				(alterEgo hide:)
				(gEgo show: loop: 0)
				(PlayerControl)
				(= g47WallBroken 1)
				(gEgo ignoreControl: ctlYELLOW)
				
				(gGame changeScore: 1)
			)
			
		)
	)
)
; Right-click-look Procedure
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
		(Print textRes textResIndex	#width 280 #at -1 10)
	else
		(Print textRes textResIndex	#width 280 #at -1 140)
	)
)
(procedure (PrintShort textRes textResIndex)
	(Print textRes textResIndex		
		#width -1
		#at -1 140
	)
)
(procedure (PrintUp textRes textResIndex)
	(Print textRes textResIndex		
		#width 290
		#at -1 10
	)
)
(procedure (readNote)
	(= gDefaultFont 4)
	(if (> (gEgo x?) 140)
		(= wherePrint 40)	
	else
		(= wherePrint 160)	
	)
	(if (or (< [gNotes 2] 2) (== [gNotes 2] 3))
		(Print 0 68 #width 100 #at wherePrint -1)
		(Print 0 69)
	else
		(Print 0 65 #width 100 #at wherePrint -1)
	)
	(= gDefaultFont 6)
)

(instance alterEgo of Prop
	(properties
		y 125
		x 232
		view 51
	)
)
(instance bed of Prop
	(properties
		y 175
		x 100
		view 16
		loop 0
		cel 2
	)
)
(instance bricks of Prop
	(properties
		y 125
		x 232
		view 50
		loop 0
	)
)
(instance closet of Prop
	(properties
		y 87
		x 130
		view 18
		loop 6
		cel 1
	)
)
(instance closetDoor of Prop
	(properties
		y 107
		x 140
		view 18
		loop 7
	)
)
(instance chair1 of Prop
	(properties
		y 170
		x 170
		view 18
		loop 3
	)
)
(instance chair2 of Prop
	(properties
		y 170
		x 250
		view 18
		loop 4
	)
)
(instance door of Prop
	(properties
		y 80
		x 186
		view 50
		loop 2
	)
)
(instance hammerItem of Prop
	(properties
		y 130
		x 230
		view 53
	)
)
(instance swingStream of Prop
	(properties
		y 105
		x 212
		view 51
		loop 3
	)
)
(instance table of Prop
	(properties
		y 170
		x 205
		view 16
		loop 5
	)
)
