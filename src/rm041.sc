;;; Sierra Script 1.0 - (do not remove this comment)
; Score +1
(script# 41)
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
	rm041 0	
)

(local
	
	doorOpen = 0
	closetOpen = 0
	doorClosing = 0
	
)

(instance rm041 of Rm
	(properties
		picture scriptNumber
		north 45
		east 47
		south 0
		west 0
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript setRegions: 205)
		(switch gPreviousRoomNumber
			(45
				(PlaceEgo 135 90 2)
				;(gEgo posn: 135 90 loop: 2)
				(door cel: 3 setCycle: Beg cycleSpeed: 2)	
			)
			(266
				(PlaceEgo 116 170 3)
				;(gEgo posn: 116 170 loop: 3)
				(gTheMusic number: 40 loop: -1 priority: -1 play:)			
			)
			(else
				(PlaceEgo 150 100 1) 
				;(gEgo posn: 150 100 loop: 1)
			)
		)
		(SetUpEgo)
		(gEgo init:)
		(= gEgoMovementType 0)
		(RunningCheck)
		
		
		(actionEgo init: hide: ignoreActors:)
		(bed init: setScript: bedScript)
		(bookcase init: setPri: 6 ignoreActors:)
		(bookcaseLeft init: setPri: 6 ignoreActors:)
		
		(chair init: ignoreActors:)
		(closet init:)
		(door init: setPri: 0 ignoreActors: setScript: doorScript)
		(table init: ignoreActors: setScript: cursorScript)		
	)
)

(instance RoomScript of Script
	(properties)
	
	(method (doit)
		(super doit:)
		; code executed each game cycle
		(if(== doorOpen 1)
			(gEgo ignoreControl: ctlYELLOW)
		else
			(gEgo observeControl: ctlYELLOW)
		)
		
		(if (& (gEgo onControl:) ctlMAROON)
			(if (and (and (and (and (and [gNotes 0]
					[gNotes 1])
					(== [gNotes 2] 2))
					(== [gNotes 3] 2))
					[gNotes 4])
					[gNotes 5])
				(gRoom newRoom: 266)
			else
				(self changeState: 12)
			)
		)
		(if (& (gEgo onControl:) ctlGREY)
				(gRoom newRoom: 45)
		)
		(if (> (gEgo distanceTo: door) 25)
			(if doorOpen
				(if (not doorClosing)
					(doorScript changeState: 4)
					(= doorClosing 1)
				)
			)
		)
		(if (>= (gEgo distanceTo: closet) 30)
			(if closetOpen
				(closet setCycle: Beg)
				(= closetOpen 0)
			)
		)
	)
	

	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if (& (pEvent modifiers?) emRIGHT_BUTTON)
				(if	
					(checkEvent
						pEvent (door nsLeft?)(door nsRight?)(door nsTop?)(door nsBottom?)
					)
					(PrintOther 41 8)	
				)
				(if	
					(checkEvent
						pEvent (closet nsLeft?)(closet nsRight?)(closet nsTop?)(closet nsBottom?)
					)
					(if closetOpen
						(if gRightClickSearch
							(if (and (<= (gEgo distanceTo: closet) 45) (> (gEgo y?) (closet y?)) )
								(self changeState: 9)
							else
								(Print 41 9)
							)
						else
							(Print 41 9)
						)
					else
						(if gRightClickSearch
							(if (and (<= (gEgo distanceTo: closet) 45) (> (gEgo y?) (closet y?)) )
								(self changeState: 9)
							else
								(PrintOther 41 10) ; closed message
							)
						else
							(PrintOther 41 10) ; closed message
						)
					)
				)
				(if	
					(checkEvent
						pEvent (bookcase nsLeft?)(bookcase nsRight?)(bookcase nsTop?)(bookcase nsBottom?)
					)
					(if (<= (gEgo distanceTo: bookcase) 45)
						(PrintOther 41 2)					
					else
						(PrintOther 41 1)
					)	
				)
				(if	
					(checkEvent
						pEvent (bookcaseLeft nsLeft?)(bookcaseLeft nsRight?)(bookcaseLeft nsTop?)(bookcaseLeft nsBottom?)
					)
					(if (and (and gRightClickSearch (<= (gEgo distanceTo: bookcaseLeft) 45)) (== [gNotes 5] 0))
						(self changeState: 1)
					else
						(if (<= (gEgo distanceTo: bookcaseLeft) 45)
							(PrintOther 41 2)					
						else
							(PrintOther 41 1)
						)
					)						
				)
				
				(if	
					(checkEvent
						pEvent (bed nsLeft?)(bed nsRight?)(bed nsTop?)(bed nsBottom?)
					)
					(if gRightClickSearch
						(if (< (gEgo distanceTo: bed) 50)
							(bedScript changeState: 1)	
						else
							(PrintOther 41 3)
						)
					else
						(PrintOther 41 3)
					)	
				else
					(if	
						(checkEvent
							pEvent (chair nsLeft?)(chair nsRight?)(chair nsTop?)(chair nsBottom?)
						)
						(PrintOther 41 14)	
					else
						(if	
							(checkEvent
								pEvent (table nsLeft?)(table nsRight?)(table nsTop?)(table nsBottom?)
							)
							(PrintOther 41 13)	
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
		(if (Said 'touch/chair')
			(PrintOther 41 15)
		)
		(if (or (Said '(pick<up), fix/chair') (Said 'clean/room'))
			(PrintOther 41 11)
		)
		(if (Said 'read/note,letter,paper')
			(if [gNotes 1]
				(Print 0 64 #font 4)
			else
				(Print "You don't see any.")
			)
		)
		(if(Said 'break/wall')
			(if (gEgo has: 3) ; hammer
				(PrintOther 41 17)		
			else
				(PrintOther 41 16)
			)
		)
		
		(if(Said 'open/door')
			(if (and (and 
				(<= (gEgo distanceTo: closet) 45)
				(> (gEgo y?) (closet y?))) 
				(> (gEgo x?) 170) )
				(self changeState: 9)
			else
				(if (not doorOpen)
					(if (not doorClosing)
						(if (<= (gEgo distanceTo: door) 30)
							(doorScript changeState: 1)
						else
							(PrintNotCloseEnough)
						)
					else
						(PrintOther 47 19)
					)
				else
					(PrintItIs)
				)
			)
		)
		(if (Said 'close/door')
			(if doorOpen
				(if (>= (gEgo distanceTo: door) 30)
					(door setCycle: Beg cycleSpeed: 2)
					(= doorOpen 0)
				else
					(PrintNotCloseEnough)
				)
			else
				(if (<= (gEgo distanceTo: closet) 45)
					(if closetOpen
						(closet setCycle: Beg)
						(= closetOpen 0)	
					else
						(PrintItIs)
					)	
				else	
					(PrintItIs)
				)
			)	
		)
		(if (Said 'search/cabinet')
			(if (and (<= (gEgo distanceTo: closet) 45) (> (gEgo y?) (closet y?)) )
				(self changeState: 9)
			else
				(PrintNotCloseEnough)
			)	
		)
		(if (Said 'open/cabinet,door')
			(if closetOpen
				(PrintItIs)	
			else
				(if (and (<= (gEgo distanceTo: closet) 45) (> (gEgo y?) (closet y?)) )
					(self changeState: 9)
				else
					(PrintNotCloseEnough)
				)	
			)	
		)
		(if (Said 'close/cabinet')
			(if closetOpen
				(if (< (gEgo distanceTo: closet) 30)
					(= closetOpen 0)
					(closet setCycle: Beg)
				else
					(PrintNotCloseEnough)
				)
			else
				(PrintItIs)
			) 	
		)
		(if (Said '(look<under),search,examine/bed')
			(if (< (gEgo distanceTo: bed) 50)
				(bedScript changeState: 1)	
			else
				(PrintOther 41 4)
			)
		)
		(if(Said 'move/bookcase')
			(PrintOther 42 25)
		)
		(if(Said 'take/book')
			(if (<= (gEgo distanceTo: bookcaseLeft) 45)
				(if (not [gNotes 1])
					(self changeState: 1)
				)
			else
				(if (<= (gEgo distanceTo: bookcase) 45)
					(PrintOther 41 2)
				else
					(PrintNotCloseEnough)
				)
			)	
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
		(if (Said 'search/bookshelf,book')
			(if (<= (gEgo distanceTo: bookcase) 45)
				(PrintOther 41 2)
			else
				(if (<= (gEgo distanceTo: bookcaseLeft) 45)
					(if (not [gNotes 1])
						(self changeState: 1)
					else
						(PrintOther 41 2)	
					)
				else
					(PrintOther 41 1) ; "There's a sturdy bookshelf here with plenty of books, but you can't get a good look at them from this distance."
				)
			)	
		)
		(if (Said 'look>')
			(if (Said '/bookshelf, book')
				(if (<= (gEgo distanceTo: bookcase) 45)
					(PrintOther 41 2)
				else
					(if (<= (gEgo distanceTo: bookcaseLeft) 45)
						(if (not [gNotes 1])
							(self changeState: 1)
						else
							(PrintOther 41 2)	
						)
					else
						(PrintOther 41 1) ; "There's a sturdy bookshelf here with plenty of books, but you can't get a good look at them from this distance."
					)
				)	
			)
			(if (Said '/closet, cabinet')
				(if closetOpen
					(Print 41 9)
				else
					(PrintOther 41 10)
				)	
			)
			(if (Said '/bed')
				(PrintOther 41 3)
			)
			(if (Said '/paper, note')
				(PrintOther 41 7)
			)
			(if (Said '/chair')
				(PrintOther 41 14)
			)
			(if (Said '/table')
				(PrintOther 41 13)
			)
			(if (Said '[/!*]')
				(PrintOther 41 6)
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
				(gEgo setMotion: MoveTo (+ (bookcaseLeft x?) 17) (- (bookcaseLeft y?) 8) self)	
			)
			(2	; reaching for the bookshelf
				(if (not [gNotes 1])
					(gEgo hide:)
					(actionEgo show: posn: (gEgo x?)(gEgo y?) view: 251 loop: 7 cel: 0 setCycle: End self cycleSpeed: 3)
				else
					
					(self cue:)
				)	
			)
			(3	(= cycles 15)
				(gEgo loop: 1)
				; waiting just a moment	
			)
			(4	; pulling a paper to his face
				(if (not [gNotes 1])
					(PrintOther 41 0) ; You find a loose paper between a couple of books.
					(actionEgo loop: 9 cel: 0 setCycle: End self)
				else
					(PrintOther 41 18)
					(PlayerControl)
				)	
			)
			(5	(= cycles 25)
				; waiting for effect	
			)
			(6	(= cycles 5) ; reading page 
				(Print 0 64 #font 4 #at 160 -1 #width 120)
				(= [gNotes 1] 1)
				(gGame changeScore: 1)	
			)
			(7	; putting paper away
				(actionEgo loop: 11 cel: 0 setCycle: End self)
			)
			(8
				(PlayerControl)
				(actionEgo hide:)
				(gEgo show: x: (+ (gEgo x?) 4) loop: 1)	
			)
			(9	; walking to closet
				(ProgramControl)
				(gEgo setMotion: MoveTo (closet x?) (+ (closet y?) 3) self ignoreControl: ctlWHITE)
			)
			(10	(= cycles 2)
				(gEgo loop: 3)
				(if (not closetOpen)
					(= cycles 0)
					(closet setCycle: End self cycleSpeed: 2)
				)
			)
			(11	; return control
				(PlayerControl)
				(if (not closetOpen)
					(= closetOpen 1)	
				)
				(Print 41 9)
			)
			(12	; Ego can't leave until he has all letters collected
				(ProgramControl)
				(gEgo setMotion: MoveTo (gEgo x?)(- (gEgo y?) 10) self)	
			)
			(13	(= cycles 4)
				(gEgo loop: 2)
			)
			(14
				(Print 41 20 #width 280 #title "You think" #at -1 20)
				(PlayerControl)	
			)
		)
	)
)
(instance bedScript of Script
	(properties)
	
	(method (changeState newState button)
		(= state newState)
		(switch state
			(0
			)
			(1
				; get closer to bed
				(ProgramControl)
				(gEgo setMotion: MoveTo (bed x?) (- (bed y?) 20) self)
			)
			(2 (= cycles 2)
				; face ego toward screen
				(gEgo loop: 2)	
			)
			(3	
				; kneeling down you see a paper under the bed
				(gEgo hide:)
				(actionEgo show: posn: (gEgo x?)(gEgo y?) view: 405 loop: 0 cel: 0 setCycle: End self cycleSpeed: 3)	
			)
			(4
				(PrintUp 41 5)
				(actionEgo setCycle: Beg self)
			)
			(5
				(PlayerControl)
				(gEgo show:)
				(actionEgo hide:)	
			)
				
		)
	)
)
(instance cursorScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0 (= cycles 1)
			)
			(1
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
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
				(gEgo setMotion: MoveTo (door x?) (+ (door y?) 2) self)
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
(procedure (PrintUp textRes textResIndex)
	(Print textRes textResIndex		
		#width 290
		#at -1 10
	)
)
(instance actionEgo of Prop
	(properties
		y 125
		x 232
		view 51
	)
)
(instance bed of Prop
	(properties
		y 174
		x 218
		view 19
	)
)
(instance bookcase of Prop
	(properties
		y 127
		x 221
		view 46
	)
)
(instance bookcaseLeft of Prop
	(properties
		y 127
		x 95
		view 16
		loop 8
	)
)
(instance closet of Prop
	(properties
		y 85
		x 189
		view 16
		loop 2
	)
)
(instance chair of Prop
	(properties
		y 142
		x 170
		view 16
		loop 3
		cel 2
	)
)
(instance door of Prop
	(properties
		y 80
		x 131
		view 52
		loop 2
	)
)
(instance table of Prop
	(properties
		y 124
		x 150
		view 19
		loop 1
	)
)