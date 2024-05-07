;;; Sierra Script 1.0 - (do not remove this comment)
; Score + 1
(script# 44)
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
	
	rm044 0
	
)

(local
	
	[stringName 4] ;  used for the combination of the lock on the chest
	chestOpen = 0 
	cabinetOpen = 0	
)

(instance rm044 of Rm
	(properties
		picture scriptNumber
		north 0
		east 267
		south 46
		west 42
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript setRegions: 205)
		(switch gPreviousRoomNumber			
			(42 
				(PlaceEgo 100 110 0)
				;(gEgo posn: 100 110 loop: 0)
			)
			(46 
				(PlaceEgo 206 151 3)
				;(gEgo posn: 206 151 loop: 3)
			)			
			(267 
				(PlaceEgo 205 110 1)
				(= gEgoMovementType 0)
				;(gEgo posn: 205 110 loop: 1)
			)
			
			(else 
				(PlaceEgo 150 100 1)
				;(gEgo posn: 150 100 loop: 1)
			)
		)
		(SetUpEgo)
		(gEgo init:)
		(RunningCheck)
		
		(bed init:)
		(bricks init: ignoreActors: setPri: 1 setScript: breakScript)
		(chair init: ignoreActors:)
		(cabinetSmall init: setScript: cabinetScript)
		(rubble init: ignoreActors: setPri: 1)
		(actionEgo init: hide: ignoreActors:)
		(bookcase init: ignoreActors:)
		(piano init: ignoreActors:)
		(rack init: setPri: 13)
		(table init: ignoreActors:)
		(trunk init: setScript: chestScript ignoreActors:)
		(viola init: setPri: 0 ignoreActors:)
		
		(gEgo observeControl: ctlFUCHSIA)
		
		(if (== g44WallBroken 3) ; broken
			(bricks cel: 1)
			(rack hide:)
			(gEgo ignoreControl: ctlFUCHSIA)	
		)
	)
)

(instance RoomScript of Script
	(properties)
	
	(method (doit)
		(super doit:)
		; code executed each game cycle
		(if (& (gEgo onControl:) ctlMAROON)
			(gRoom newRoom: 46)
		)
		(if (& (gEgo onControl:) ctlGREY)
			(gRoom newRoom: 42)
		)
		(if (& (gEgo onControl:) ctlSILVER)
			(if (and (and (and (and (and [gNotes 0]
				[gNotes 1])
				(== [gNotes 2] 2))
				(== [gNotes 3] 2))
				[gNotes 4])
				[gNotes 5])
				(gRoom newRoom: 267)
			else
				(self changeState: 1)
			)
		)
		(if (> (gEgo distanceTo: cabinetSmall) 30)
			(if cabinetOpen
				(= cabinetOpen 0)
				(cabinetSmall setCycle: Beg)	
			)	
		)
		
		(if (not (& (gEgo onControl:) ctlBLUE) )
			(if chestOpen
				(trunk setCycle: Beg)
				(= chestOpen 0)
			)	
		)
	
	)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if (& (pEvent modifiers?) emRIGHT_BUTTON)
				
				(if	
					(checkEvent
						pEvent
						(- (rack nsLeft?) 5)(+ (rack nsRight?) 5)(- (rack nsTop?) 5)(+ (rack nsBottom?) 10)
						;(return)
					)
					(if (not (== g44WallBroken 3))
						(PrintOther 44 7)
						(if (== g44WallBroken 0)
							(= g44WallBroken 1)	
						)
						;(return)
					)	
				)
				(if	
					(checkEvent
						pEvent
						(cabinetSmall nsLeft?)(cabinetSmall nsRight?)(cabinetSmall nsTop?)(cabinetSmall nsBottom?)
					)
					(if cabinetOpen
						(if gRightClickSearch
							(if (and (<= (gEgo distanceTo: cabinetSmall) 35) (> (gEgo y?) (cabinetSmall y?)) )
								(cabinetScript changeState: 1)
							else
								(PrintOther 44 21) ; closed message
							)
						else
							(PrintOther 44 22)	; music sheet sticks out
						)
					else
						(if gRightClickSearch
							(if (and (<= (gEgo distanceTo: cabinetSmall) 35) (> (gEgo y?) (cabinetSmall y?)) )
								(cabinetScript changeState: 1)
							else
								(PrintOther 44 21) ; closed message	
							)
						else
							(PrintOther 44 21) ; closed message
						)
					)	
				)
				(if	
					(checkEvent
						pEvent
						(viola nsLeft?)(viola nsRight?)(viola nsTop?)(viola nsBottom?)
					)
					(PrintOther 44 1)	
				)
				(if	
					(checkEvent
						pEvent
						(trunk nsLeft?)(trunk nsRight?)(trunk nsTop?)(trunk nsBottom?)
					)
					(PrintOther 44 9)	
				else
					(if	
						(and (checkEvent
							pEvent
							(bed nsLeft?)(bed nsRight?)(bed nsTop?)(bed nsBottom?)
						) (> (pEvent y?) 145)
							(PrintOther 44 6)
						)	
					else
						(if	
							(checkEvent
								pEvent
								(bookcase nsLeft?)(bookcase nsRight?)(bookcase nsTop?)(bookcase nsBottom?)
							)
							(PrintOther 44 5)	
						)
					)
				)
				(if	
					(checkEvent
						pEvent
						(chair nsLeft?)(- (chair nsRight?) 25)(chair nsTop?)(chair nsBottom?)
					)
					(PrintOther 44 4)	
				else
					(if	
						(checkEvent
							pEvent
							(table nsLeft?)(table nsRight?)(table nsTop?)(table nsBottom?)
						)
						(PrintOther 44 3)
					else
						(if	
							(checkEvent
								pEvent
								(piano nsLeft?)(piano nsRight?)(piano nsTop?)(piano nsBottom?)
							)
							(if gRightClickSearch
								(if (< (gEgo distanceTo: piano) 40)
									(PrintOther 44 28)
									(Print "" #title "It reads:" #icon 94 1)
								else
									(PrintOther 44 2)
								)
							else
								(PrintOther 44 2)
								(if (< (gEgo distanceTo: piano) 40)
									(PrintOther 44 28)
								)	
							)	
						)
					)
				)					
			)
		)
		
		; handle Said's, etc...
		(if (Said 'close/cabinet')
			(if cabinetOpen
				(if (< (gEgo distanceTo: cabinetSmall) 30)
					(= cabinetOpen 0)
					(cabinetSmall setCycle: Beg)
				else
					(PrintNotCloseEnough)
				)
			else
				(PrintItIs)
			) 	
		)
		(if (Said 'run')
			(Print "There's no reason to do that here.")
		)
		(if (Said '(look<under),search,examine/bed,mattress')
			(PrintOther 44 11)
		)
		(if (Said 'look,read/book')
			(if (< (gEgo distanceTo: piano) 40)
				(PrintOther 44 28)
				(Print "" #title "It reads:" #icon 94 1)
			else
				(PrintOther 44 2)
			)	
		)
		(if (Said 'take,wear,(pick<up)>')
			(if (Said '/clothes')
				(if chestOpen
					(PrintUp 40 23)
				else
					(PrintUp 40 24)
				)
			)
		)
		(if (Said 'look>')
			(if (Said '/rack, metal, mount')
				(PrintOther 44 7)	
			)
			(if (Said 'instrument')
				(PrintOther 44 35)	
			)
			(if (Said '/bed')
				(PrintOther 44 6)	
			)
			(if (Said '/bookcase')
				(PrintOther 44 5)			
			)
			(if (Said '/piano')
				(PrintOther 44 2)
				(if (< (gEgo distanceTo: piano) 40)
					(PrintOther 44 28)
				)
			)
			(if (Said '/paper,sheet,music')
				(if (< (gEgo distanceTo: piano) 40)
					(PrintOther 44 28)
					(Print "" #title "It reads:" #icon 94 1)
				else
					(PrintOther 0 100)
				)
			)
			(if (Said '/violin')
				(PrintOther 44 1)	
			)
			(if (Said '/table')
				(PrintOther 44 3)
			)
			(if (Said '/chair')
				(PrintOther 44 4)		
			)
			(if (Said '/wall')
				(if (== g44WallBroken 3)
					(PrintOther 44 13)	
				else
					(PrintOther 44 12)
					(if (== g44WallBroken 0)
						(= g44WallBroken 1)	
					)
				)
			)
			(if (Said '/hole,pathway')
				(if (== g44WallBroken 3)
					(PrintOther 44 13)	
				else
					(PrintOther 44 14)
				)		
			)
			(if (Said '[/!*]')
				(PrintOther 44 30)
			)
		)
		(if (Said 'take>')
			(if (Said '/violin')
				(PrintOther 44 24)
			)
			(if (Said '/rack, metal, mount')
				(PrintOther 44 25)
			)
			(if (Said '/seat,chair')
				(PrintOther 44 26)
			)
		)
		(if (Said 'touch/chair')
			(PrintOther 44 27)			
		)
		(if (Said 'sit')
			(PrintOther 44 26)			
		)
		(if (Said '(look<in),search/chest,box,trunk')
			(if (& (gEgo onControl:) ctlBLUE)
				(chestScript changeState: 1)	
			else
				(PrintNotCloseEnough)
			)	
		)
		(if (Said 'open,unlock/chest,box,trunk')
			(if (& (gEgo onControl:) ctlBLUE)
				(if (not chestOpen)
					; changestate
					(chestScript changeState: 1)	
				else
					(PrintItIs)
				)
			else
				(PrintNotCloseEnough)
			)	
		)
		(if(Said 'open,search>')
			(if(Said '/cabinet')
				(if (< (gEgo distanceTo: cabinetSmall) 40)
					(cabinetScript changeState: 1)
				else
					(PrintNotCloseEnough)
				)			
			)
			(if (Said '/door')
				(if (< (gEgo distanceTo: cabinetSmall) 40)
					(cabinetScript changeState: 1)
				else
					(PrintShort 44 10)
				)				
			)			
		)
		(if (Said '(look<under),search,examine/bed')
			(PrintOther 44 1)
		)
		(if 
			(or 
				(Said 'break/wall')
				(Said 'use/hammer/wall') 
				(Said 'hit/wall/hammer')
			)
			(if (== g44WallBroken 3)
				(Print "The wall is already broken.")
			else
				(if (< (gEgo distanceTo: bricks) 50)
					(if (gEgo has: 3)
						(breakScript changeState: 1)
					else
						(Print "A great idea, but you don't have anything to break it with.")
					)
				else
					(PrintNotCloseEnough)
				)
			)
		)
		(if(Said 'move/bookcase')
			(PrintOther 42 25)
		)
		(if(Said 'play/piano')
			(PrintOther 42 27)
		)
		(if(Said 'use/magnet,metal,triangle/rack,metal,mount')
			(if (gEgo has: INV_MAGNET)
				(PrintOther 44 16)
			else
				(PrintDontHaveIt)
			)
		)
		(if(Said 'throw/hammer')
			(if (gEgo has: INV_HAMMER)
				(if (== (IsOwnedBy INV_MAGNET 42) TRUE)	
					(PrintOther 44 34)	
				else
					(PrintOther 44 33)
				)
			else
				(PrintDontHaveIt)
			)
		)
		(if(Said 'use/magnet,metal,triangle/hammer')
			(if (and (gEgo has: INV_HAMMER) (gEgo has: INV_MAGNET))
				(PrintOther 44 23)
			else
				(PrintOther 44 29)
			)
		)
		(if (Said 'hang/magnet,metal,triangle')
			(if (gEgo has: INV_MAGNET)
				(PrintOther 44 31)
			else
				(PrintDontHaveIt)
			)
		)
	)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0 ; Handle state changes
			)
			(1	; can't leave without letters
				(ProgramControl)
				(gEgo setMotion: MoveTo (- (gEgo x?) 20) (gEgo y?) self)
			)
			(2
				(PlayerControl)
				(= gWndColor 14)
				(= gWndBack 5)
				(Print 44 15 #title "Leah thinks" #width 280 #at -1 20)	
				(= gWndColor 0)
				(= gWndBack 15)
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
				(gEgo setMotion: MoveTo 204 115 self)
			)
			(2	; pull out hammer
				(gEgo hide:)
				(actionEgo show: view: 315 posn: (+ (gEgo x?) 5) (gEgo y?) loop: 2 cel: 0 setCycle: End self cycleSpeed: 2 setPri: 14)	
			)
			(3 (= cycles 8)
			)
			(4	; hit wall
				(actionEgo posn: (+ (gEgo x?) 14) (gEgo y?) loop: 0 cel: 0 setCycle: End self)
			)
			(5 (= cycles 16) ;blinking
				(actionEgo loop: 3 setCycle: Fwd)	
			)
			(6	(= cycles 1)
				(if (== (IsOwnedBy INV_MAGNET 42) TRUE)
					(PrintOther 44 32)
				else
					(PrintOther 44 0)
				)	
			)
			(7	; rewind hammer swing
				(actionEgo loop: 0 cel: 4 setCycle: Beg self)
			)
			(8	; put hammer away
				(actionEgo posn: (+ (gEgo x?) 5) (gEgo y?) loop: 2 cel: 3 setCycle: Beg self)
			)
			(9	; return to player
				(= g44WallBroken 2) ; player hit it, but didn't break it - used for hint in room 39
				(actionEgo hide: setPri: -1)
				(gEgo show: loop: 0 posn: (+ (gEgo x?) 3) (gEgo y?) )
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
				(gEgo setMotion: MoveTo (cabinetSmall x?)(+ (cabinetSmall y?) 5)  self ignoreControl: ctlWHITE)
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
				(PrintOther 44 22)
				(Print "" #icon 94 0 #title "The Sheet Music")
				(PlayerControl)	
			)
			(5
				(cabinetSmall setCycle: End self cycleSpeed: 2)	
			)
			(6
				(PrintOther 44 22)
				(Print "" #title "The Sheet Music" #icon 94 0)
				(= cabinetOpen 1)
				(PlayerControl)	
			)
		)
	)
)
(instance chestScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0
			)
			(1
				; walk to chest
				(ProgramControl)
				(gEgo setMotion: MoveTo (+ (trunk x?) 22) (- (trunk y?) 3) self ignoreControl: ctlWHITE)
			)
			(2
				; bend down 
				(gEgo hide:)
				(actionEgo show: view: 450 loop: 1 cel: 0 posn: (gEgo x?) (gEgo y?) setCycle: End self cycleSpeed: 2)	
			)
			(3
				;	check if unlocked
				(if (not g44ChestUnlocked)
					(= stringName 0)
					(EditPrint
						@stringName
						6
						{The lock has an alphabetical, 4-letter code. Enter here:}
						#at
						-1
						20
					)
					(if (or (== (StrCmp @stringName {bede}) 0)
							(== (StrCmp @stringName {BEDE}) 0))
						(= g44ChestUnlocked 1)	
					)
					(if g44ChestUnlocked
						(self changeState: 4)
					else
						(self changeState: 8)
					)
				else
					(self changeState: 4)	
				)
			)
			(4	; if unlocked, open
				(if (not chestOpen)
					(trunk setCycle: End self cycleSpeed: 2)
				else
					(self cue:)
				)
				(= chestOpen 1)
			)
			(5 (= cycles 7); loot
				(PrintUp 44 17)
				(if (== [gNotes 3] 2) ; completed already
					(Print 44 20)	; nothing else
				else
					(if (== [gNotes 3] 42)	; already have the first half note						
						(PrintUp 44 19)
						(= [gNotes 3] 2)
						(readNote)
						(gGame changeScore: 1)
					else
						(if (== [gNotes 3] 44)
							(Print 44 20)	; nothing else	
						else
							(PrintUp 44 18)
							(= [gNotes 3] 44)
							(readNote)
							(gGame changeScore: 1)	
						)	
					)
				)
			)
			(6
				; stand up again
				(actionEgo setCycle: Beg self)
			)
			(7
				(PlayerControl)
				(actionEgo hide:)
				(gEgo show: loop: 1 observeControl: ctlWHITE)
			)
			(8
				; print locked, stand up
				(PrintOther 40 10) ; The lock is fastened tight.
				(actionEgo setCycle: Beg self)
			)
			(9
				(PlayerControl)
				(actionEgo hide:)
				(gEgo show: loop: 1 observeControl: ctlWHITE)
			)
			(10	; opening chest a second time
				; walk to chest
				(ProgramControl)
				(gEgo setMotion: MoveTo (+ (trunk x?) 22) (- (trunk y?) 3) self ignoreControl: ctlWHITE)
			)
			(11
				; bend down 
				(gEgo hide:)
				(actionEgo show: view: 450 loop: 1 cel: 0 posn: (gEgo x?) (gEgo y?) setCycle: End self cycleSpeed: 2)
			)
			(12	; if unlocked, open
				(trunk setCycle: End self cycleSpeed: 2)
				(= chestOpen 1)
			)
			(13
				(PrintOther 40 9)
				(actionEgo setCycle: Beg self)
			)
			(14
				(PlayerControl)
				(actionEgo hide:)
				(gEgo show: loop: 1 observeControl: ctlWHITE)	
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
(procedure (PrintBook textRes textResIndex)
	(Print textRes textResIndex		
		#width 100
		#font 4
		#title "It reads:"
	)
)
(procedure (readNote)
	(= gDefaultFont 4)
	(if (== [gNotes 3] 44)
		(Print 0 72 #at 40 -1 #width 100)
		(Print 0 69)
	else
		(if (== [gNotes 3] 2)
			(Print 0 66 #at 30 -1 #width 120)
		)
	)
	(= gDefaultFont 6)
)

(instance actionEgo of Prop
	(properties
		y 125
		x 232
		view 315
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
(instance bookcase of Prop
	(properties
		y 155
		x 115
		view 18
		loop 10
	)
)
(instance bricks of Prop
	(properties
		y 127
		x 229
		view 50
		loop 5
	)
)

(instance cabinetSmall of Prop
	(properties
		y 84
		x 122
		view 18
		loop 8
	)
)
(instance chair of Prop
	(properties
		y 118
		x 190
		view 18
		loop 4
		
	)
)
(instance piano of Prop
	(properties
		y 87
		x 167
		view 82
	)
)
(instance rack of Prop
	(properties
		y 95
		x 230
		view 82
		loop 1
		cel 1
	)
)
(instance rubble of Prop
	(properties
		y 130
		x 89
		view 50
		loop 5
		cel 1
	)
)
(instance table of Prop
	(properties
		y 120
		x 155
		view 16
		loop 5
	)
)
(instance trunk of Prop
	(properties
		y 172
		x 148
		view 18
		loop 5
	)
)
(instance viola of Prop
	(properties
		y 100
		x 210
		view 82
		loop 2
	)
)