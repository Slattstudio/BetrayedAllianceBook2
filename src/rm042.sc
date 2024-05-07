;;; Sierra Script 1.0 - (do not remove this comment)
; Score + 4
(script# 42)
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
	
	rm042 0
	
)
(local
	
	wallBroken = 1
	cabinetOpen = 0
	;paintingDown = 0
	
	[stringName 5] ;  used for the combination of the lock on the chest
	chestOpen = 0  
	firstOpen = 0
	
)

(instance rm042 of Rm
	(properties
		picture scriptNumber
		north 0
		east 44
		south 46
		west 0
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript setRegions: 205)
		(switch gPreviousRoomNumber
			(44 
				(gEgo posn: 210 115 loop: 1)
			)
			(else 
				(gEgo posn: 150 100 loop: 1)
			)
		)
		(SetUpEgo)
		(gEgo init:)
		(RunningCheck)
		
		(bricks init: ignoreActors: setPri: 1)
		(actionEgo init: hide: ignoreActors:)
		(alterEgo init: hide: ignoreActors:)
		(bed init: setScript: bedScript)
		(bookcase init: setPri: 5 ignoreActors: ignoreControl: ctlWHITE)
		(cabinet init: setScript: cabinetScript)
		(chair init: ignoreActors:)
		(hammer init: hide: ignoreActors: ignoreControl: ctlWHITE setScript: hammerScript)
		(magnet init: setScript: magnetScript)
		(magnetShards init: hide: ignoreActors: ignoreControl: ctlWHITE)
		(paintingTop init:)
		(paintingLeft init: setScript: paintingScript ignoreControl: ctlWHITE ignoreActors:)
		(rocks init:)
		(table init: ignoreActors:)
		(trunk init: setScript: chestScript)
		
		(if (== (IsOwnedBy INV_MAGNET 42) TRUE)
			(magnet loop: 1)
		)
		(if g42PaintingDown
			(paintingLeft loop: 5 cel: 3 x: (+ (paintingLeft x?) 20) y: 150 setPri: 0)
		)
		(if (== g44WallBroken 3)
			(magnetShards show: view: 87 posn: (+ (magnet x?) 10) (+ (magnet y?) 30) loop: 3 cel: 4)
			(magnet loop: 0)	
		)	
	)
)

(instance RoomScript of Script
	(properties)
	
	(method (doit)
		(super doit:)
		; code executed each game cycle
		
		(if (& (gEgo onControl:) ctlGREY)
			(gRoom newRoom: 44)
		)
		
		(if (> (gEgo distanceTo: cabinet) 30)
			(if cabinetOpen
				(= cabinetOpen 0)
				(cabinet setCycle: Beg)	
			)	
		)
		(if (or (not (& (gEgo onControl:) ctlSILVER) )(> (gEgo distanceTo: trunk) 30))
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
						(paintingTop nsLeft?)(paintingTop nsRight?)(paintingTop nsTop?)(paintingTop nsBottom?)
					)
					(PrintOther 42 1)	
				)
				(if	
					(checkEvent
						pEvent
						(magnetShards nsLeft?)(magnetShards nsRight?)(magnetShards nsTop?)(magnetShards nsBottom?)
					)
					(if (== g44WallBroken 3)
						(PrintOther 42 40)
						(return)
					)
				)
				(if	
					(checkEvent
						pEvent
						(paintingLeft nsLeft?)(paintingLeft nsRight?)(paintingLeft nsTop?)(paintingLeft nsBottom?)
					)
					(if g42PaintingDown
						(PrintOther 42 2)
					else
						(PrintOther 42 3)
					)	
				)
				(if	
					(checkEvent
						pEvent
						(bookcase nsLeft?)(bookcase nsRight?)(bookcase nsTop?)(bookcase nsBottom?)
					)
					(PrintOther 42 4)	
				)
				(if	
					(checkEvent
						pEvent
						(magnet nsLeft?)(magnet nsRight?)(magnet nsTop?)(magnet nsBottom?)
					)
					(if (== (IsOwnedBy INV_MAGNET 42) TRUE)	; hanging on the wall
						(if (not g44WallBroken)
							(PrintOther 42 28)	
					
						else
							(PrintOther 42 36)
						)
					else	; not on the wall (just a peg)
						(if g42PaintingDown
							(PrintOther 42 22)
						)
					)
				)
				
				(if	
					(checkEvent
						pEvent
						(cabinet nsLeft?)(cabinet nsRight?)(cabinet nsTop?)(cabinet nsBottom?)
					)
					(if cabinetOpen
						(if gRightClickSearch
							(if (and (<= (gEgo distanceTo: cabinet) 35) (> (gEgo y?) (cabinet y?)) )
								(cabinetScript changeState: 1)
							else
								(PrintOther 42 6) ; closed message
							)
						else
							(Print 42 13 #at -1 150)
						)
					else
						(if gRightClickSearch
							(if (and (<= (gEgo distanceTo: cabinet) 35) (> (gEgo y?) (cabinet y?)) )
								(cabinetScript changeState: 1)
							else
								(PrintOther 42 6) ; closed message	
							)
						else
							(PrintOther 42 6) ; closed message
						)
					)	
				)
				(if	
					(checkEvent
						pEvent
						(trunk nsLeft?)(trunk nsRight?)(trunk nsTop?)(trunk nsBottom?)
					)
					(if g42ChestUnlocked
						(PrintOther 42 9)	
					else
						(PrintOther 42 8)
					)	
				else
					(if	
						(checkEvent	pEvent
							(bed nsLeft?)(bed nsRight?)(bed nsTop?)(bed nsBottom?)
						)
						(if gRightClickSearch
							(if (< (gEgo distanceTo: bed) 50)
								(bedScript changeState: 1)	
							else
								(PrintOther 42 10)
							)
						else
							(PrintOther 42 10)
						)	
					else
						(if	
							(and (checkEvent
								pEvent
								(chair nsLeft?)(chair nsRight?)(chair nsTop?)(chair nsBottom?)
							)(> (pEvent x?) 172)
								(PrintOther 42 11)
							)	
						)
					)
				)
				(if	
					(checkEvent
						pEvent
						(table nsLeft?)(table nsRight?)(table nsTop?)(table nsBottom?)
					)
					(PrintOther 42 12)	
				)
			)
		)
		
		(if (Said 'run')
			(Print "There's no reason to do that here.")
		)
		(if (or (Said 'hang,(put<up)/painting')
			(Said 'put/painting/peg'))
			(if g42PaintingDown
				(PrintOther 42 30)
			else
				(PrintCantDoThat)
			)
		)
		(if (Said 'hang/self,myself')
			(PrintOther 42 31)	
		)
		(if(Said 'use,throw/hammer')
			(if (gEgo has: INV_HAMMER)
				(if (or (& (gEgo onControl:) ctlNAVY) (& (gEgo onControl:) ctlSILVER))
					(PrintOK)
					(hammerScript changeState: 1)	
				else
					(PrintNotCloseEnough)
				)	
			)	
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
				(Said 'hit/nail')
				(Said 'use/hammer/nail') 
				(Said 'hammer/nail')
			)
			(PrintOther 42 41)
		)
		(if (or (Said 'hang,(put<up)/magnet,metal')
			(Said 'put,use/magnet,metal,triangle/peg'))
			
			(if (== (IsOwnedBy INV_MAGNET 42) TRUE)
				(PrintCantDoThat)	
			else 
				(if (gEgo has: INV_MAGNET)
					(if (& (gEgo onControl:) ctlNAVY)
						(magnetScript changeState: 1)
					else
						(PrintNotCloseEnough)
					)
				else
					(PrintDontHaveIt)
				)
			)		
		)
		(if (Said 'touch/chair')
			(PrintOther 44 27)			
		)
		(if(Said 'touch,take,move,straighten,(look<behind)/painting')
			(if (not g42PaintingDown)
				(if (& (gEgo onControl:) ctlNAVY)
					(PrintOK)
					(paintingScript changeState: 1)
				else
					(PrintNotCloseEnough)
				)		
			else
				(Print "There's no need to do that now.")
			)	
		)
		(if(Said 'take>')
			(if (Said '/brush, paint')
				(if cabinetOpen
					(PrintOther 42 24)	
				else
					(PrintOther 42 32)	
				)
			)
			(if (Said '/metal,magnet,triangle')
				(if (== g44WallBroken 3)
					(PrintOther 42 42)	
				else
					(if (gEgo has: INV_MAGNET)
						(Print 42 43)
					else
						(if (== (IsOwnedBy INV_MAGNET 42) TRUE)
							(PrintOther 42 45)	
						else
							(PrintOther 42 44)	
						)
					)
				)
			)
			(if (Said '/peg,nail')
				(if g42PaintingDown
					(if (== g44WallBroken 3)
						(Print "You don't need it for anything else.")	
					else
						(PrintOther 42 34)
					)
				else
					(Print 42 32)
				)
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
		(if(Said 'move/bookcase')
			(PrintOther 42 25)
		)
		(if (Said '(look<in),search/chest,box,trunk')
			(if (& (gEgo onControl:) ctlSILVER)
				(chestScript changeState: 1)	
			else
				(PrintNotCloseEnough)
			)	
		)
		(if (Said 'open,unlock/chest,box,trunk')
			(if (& (gEgo onControl:) ctlSILVER)
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
		(if(Said 'open,search>')
			(if (Said '/door')
				(if (< (gEgo distanceTo: cabinet) 50)
					(cabinetScript changeState: 1)
				else
					(PrintOther 42 15)
				)
			)
			(if(Said '/cabinet')
				(if (< (gEgo distanceTo: cabinet) 50)
					(cabinetScript changeState: 1)
				else
					(PrintNotCloseEnough)
				)			
			)
		)
		(if (Said '(look<under),search,examine/bed,mattress')
			(if (< (gEgo distanceTo: bed) 45)
				(bedScript changeState: 1)	
			else
				(PrintOther 42 14)
			)
		)
		(if (Said 'look>')
			(if (Said '/table')
				(PrintOther 42 12)
			)
			(if (Said '/wall')
				(if g42PaintingDown
					(PrintOther 42 22)
				else
					(if (== g44WallBroken 3)
						(PrintOther 42 26)
					else
						(PrintOther 42 23)
					)
				)
			)
			(if (Said '/painting,picture')
				(if (not g42PaintingDown)
					(if (& (gEgo onControl:) ctlNAVY) ; painting on side wall
						(PrintOther 42 3)			
					else
						(if (< (gEgo y?) 107) ; near to the top
							(PrintOther 42 1)	
						else
							(PrintOther 42 0) ; mention both paintings	
						)
					)		
				else
					(if (& (gEgo onControl:) ctlNAVY)
						(PrintOther 42 2)
					else
						(PrintOther 42 1)	
					)	
				)
			)
			(if (Said '/bookshelf,book')
				(PrintOther 42 4)
			)
			(if (Said '/cabinet')
				(if cabinetOpen
					(PrintOther 42 13) 	
				else
					(PrintOther 42 6) ; default
				)
			)
			(if (Said '/chest')
				(if g42ChestUnlocked
					(PrintOther 42 9)	
				else
					(PrintOther 42 8)
				) 
			)
			(if (Said '/bed')
				(PrintOther 42 10)
			)
			(if (Said '/chair')
				(PrintOther 42 11)
			)
			(if (Said '/peg,nail')
				(if g42PaintingDown
					(if (== (IsOwnedBy INV_MAGNET 42) TRUE)
						(if (== g44WallBroken 3)
							(PrintOther 42 36)	
						else
							(PrintOther 42 38)
						)
					else
						(PrintOther 42 37)
					)
				else
					(PrintOther 42 35)
				)	
			)
			(if (Said '/magnet,metal')
				(if g42PaintingDown
					(if (== (IsOwnedBy INV_MAGNET 42) TRUE)
						(if (== g44WallBroken 3)	; if puzzle is complete and magnet is broken
							(PrintOther 42 40)	
						else
							(PrintOther 42 33)	; if magnet is on wall, but hammer not thrown yet
						)
					else ; met is not yet hung up, but painting is down
						(if (gEgo has: INV_MAGNET)
							(Print 0 1 #title "Hammer" #icon 620)	
						else
							(PrintOther 42 39)
						)
					)
				else
					(if (gEgo has: INV_MAGNET)
						(Print 0 1 #title "Hammer" #icon 620)	
					else
						(PrintOther 42 39)
					)
				)	
			)
			(if (Said '[/!*]')
				(PrintOther 42 26)
			)
			
		)
	)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0 ; Handle state changes
			)
		)
	)
)
(instance bedScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0
			)
			(1
				; get closer to bed
				(ProgramControl)
				(gEgo setMotion: MoveTo (+ (bed x?) 10) (- (bed y?) 17) self)
			)
			(2 (= cycles 2)
				; face ego toward screen
				(gEgo loop: 2)	
			)
			(3	
				; kneeling down you see a paper under the bed
				(gEgo hide:)
				(alterEgo show: posn: (gEgo x?)(gEgo y?) view: gEgoPickUpView loop: 1 cel: 0 setCycle: End self cycleSpeed: 3)	
			)
			(4
				(PrintOther 42 5)
				(alterEgo setCycle: Beg self)
			)
			(5
				(PlayerControl)
				(gEgo show:)
				(alterEgo hide:)	
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
				(gEgo setMotion: MoveTo 157 90 self ignoreControl: ctlWHITE)
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
				(PrintUp 42 13)
				(PlayerControl)	
			)
			(5
				(cabinet setCycle: End self cycleSpeed: 2)	
			)
			(6
				(PrintUp 42 13)
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
				(gEgo setMotion: MoveTo (- (trunk x?) 20) (- (trunk y?) 3) self ignoreControl: ctlWHITE)
			)
			(2
				; bend down 
				(gEgo hide:)
				(alterEgo show: view: 450 loop: 0 cel: 0 posn: (gEgo x?) (gEgo y?) setCycle: End self cycleSpeed: 2)	
			)
	
			(3	; if unlocked, open
				(if (not chestOpen)
					(trunk setCycle: End self cycleSpeed: 2)
				else
					(self cue:)
				)
				(= chestOpen 1)
				(if (not firstOpen)
					(PrintUp 42 16)
				)
			)
			(4 (= cycles 7); loot
				; print something
				(PrintUp 42 17)
				(if (== [gNotes 3] 2) ; completed already
					(Print 42 20)	; nothing else
				else
					(if (== [gNotes 3] 44)	; already have the first half note						
						(PrintUp 42 19)
						(= [gNotes 3] 2)
						(readNote)
						(gGame changeScore: 1)
					else
						(if (== [gNotes 3] 42)
							(Print 42 20)	; nothing else		
						else
							(PrintUp 42 18)
							(= [gNotes 3] 42)
							(readNote)	
							(gGame changeScore: 1)	
						)
					)
				)
			)
			(5
				; stand up again
				(alterEgo setCycle: Beg self)
			)
			(6
				(PlayerControl)
				(alterEgo hide:)
				(gEgo show: loop: 0 observeControl: ctlWHITE)
			)
			(8
				; print locked, stand up
				(PrintOther 40 10) ; The lock is fastened tight.
				(alterEgo setCycle: Beg self)
			)
			(9
				(PlayerControl)
				(alterEgo hide:)
				(gEgo show: loop: 0 observeControl: ctlWHITE)
			)
			(10	; opening chest a second time
				; walk to chest
				(ProgramControl)
				(gEgo setMotion: MoveTo (+ (trunk x?) 22) (- (trunk y?) 3) self ignoreControl: ctlWHITE)
			)
			(11
				; bend down 
				(gEgo hide:)
				(alterEgo show: view: 450 loop: 1 cel: 0 posn: (gEgo x?) (gEgo y?) setCycle: End self cycleSpeed: 2)
			)
			(12	; if unlocked, open
				(trunk setCycle: End self cycleSpeed: 2)
				(= chestOpen 1)
			)
			(13
				(PrintOther 40 9)
				(alterEgo setCycle: Beg self)
			)
			(14
				(PlayerControl)
				(alterEgo hide:)
				(gEgo show: loop: 1 observeControl: ctlWHITE)	
			)
			
		)
	)
)

(instance hammerScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0
			)
			(1	; walking area to throw hammer
				(ProgramControl)
				(gEgo setMotion: MoveTo 105 142 self ignoreControl: ctlWHITE)	
			)
			(2	; pull out hammer
				(gEgo hide:)
				(actionEgo show: posn: (gEgo x?) (gEgo y?) view: 315 loop: 2 cel: 0 setCycle: End self cycleSpeed: 2)	
			)
			(3	; swing hammer
				(actionEgo x: (+ (actionEgo x?) 10)loop: 4 cel: 0 setCycle: End self)	
			)
			(4	; throw hammer
				(actionEgo loop: 5 cel: 0)
				(hammer show: posn: (actionEgo x?)(actionEgo y?) setCycle: Walk yStep: 5 xStep: 12 setMotion: MoveTo 106 92 self)	
			)
			(5	; hammer goes horizontal
				(ShakeScreen 1)
				(actionEgo view: 1 loop: 0)
				(hammer setPri: 8 setMotion: MoveTo 300 92 self)
				(magnetScript changeState: 6)	
			)
			(6	(= cycles 10)
				;()	
			)
			(7	(= cycles 10)
				(ShakeScreen 2)
			)
			(8	(= cycles 1)
				(actionEgo hide:)
				(gEgo show: posn: (actionEgo x?)(actionEgo y?) loop: 0 observeControl: ctlWHITE put: 3 42)
				(= g44WallBroken 3)
				(PlayerControl)
				
				(gGame changeScore: 3)	
			)
			(9
				(PrintOther 42 29)	
			)
		)
	)
)

(instance magnetScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0
			)
			(1	; walking to pin on wall
				(ProgramControl)
				(gEgo setMotion: MoveTo 105 113 self ignoreControl: ctlWHITE)	
			)
			(2	; lifting hand to touch
				(gEgo hide:)
				(actionEgo show: posn: (gEgo x?)(gEgo y?) view: 377 loop: 1 cel: 0 setCycle: End self cycleSpeed: 2)	
			)
			(3	(= cycles 7)	; put the magnet on the hanger
				(magnet loop: 1)	
			)
			(4
				(actionEgo setCycle: Beg self)

			)
			(5
				(gEgo show: loop: 1 observeControl: ctlWHITE put: INV_MAGNET 42)
				(actionEgo hide:)
				(PlayerControl)
				(PrintOther 42 33)
			)
			(6
				(magnet loop: 0)
				(magnetShards show: view: 87 loop: 0 setCycle: End self cycleSpeed: 2)	
			)
			(7
				(magnetShards setCycle: Walk yStep: 4 setMotion: MoveTo (+ (magnet x?) 10)(+ (magnet y?) 30) self)
			)	
			(8
				(magnetShards loop: 3 cel: 0 setCycle: End self)		
			)
		)
	)
)

(instance paintingScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0
			)
			(1	; walking to painting
				(ProgramControl)
				(gEgo setMotion: MoveTo 105 113 self ignoreControl: ctlWHITE)	
			)
			(2	; lifting hand to touch
				(gEgo hide:)
				(actionEgo show: posn: (gEgo x?)(gEgo y?) view: 377 loop: 1 cel: 0 setCycle: End self cycleSpeed: 2)	
			)
			(3	;painting wiggling
				(= cycles 10)
				(paintingLeft loop: 4 setCycle: Fwd cycleSpeed: 2)
			)
			(4	; painting falls to ground
				(paintingLeft loop: 2 yStep: 5 setMotion: MoveTo (paintingLeft x?) 150 self )
				(actionEgo setCycle: Beg)
			)
			(5	; painting falls flat
				(paintingLeft loop: 5 x: (+ (paintingLeft x?) 20)setCycle: End self setPri: 0)	
			)
			(6
				(gEgo show: observeControl: ctlWHITE loop: 2)
				(actionEgo hide:)
				(= g42PaintingDown 1)
				(PlayerControl)
				(PrintOther 42 21)
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
(procedure (PrintBook textRes textResIndex)
	(Print textRes textResIndex		
		#width 100
		#font 4
		#title "It reads:"
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
	(if (== [gNotes 3] 42)
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
		view 404
	)
)
(instance bed of Prop
	(properties
		y 174
		x 218
		view 19
		loop 0
		cel 1
	)
)
(instance bookcase of Act
	(properties
		y 107
		x 210
		view 46
	)
)
(instance bricks of Prop
	(properties
		y 125
		x 232
		view 50
		cel 6
	)
)
(instance cabinet of Prop
	(properties
		y 86
		x 161
		view 18
		loop 11
	)
)
(instance chair of Prop
	(properties
		y 142
		x 170
		view 18
		loop 3
	)
)
(instance hammer of Act
	(properties
		y 142
		x 170
		view 85
	)
)
(instance rocks of Prop
	(properties
		y 180
		x 90
		view 18
		loop 12
	)
)
(instance alterEgo of Prop
	(properties
		y 125
		x 232
		view 51
	)
)
(instance magnet of Prop
	(properties
		y 91
		x 83
		view 80
		loop 0
	)
)
(instance magnetShards of Act
	(properties
		y 91
		x 83
		view 80
		loop 0
	)
)
(instance paintingLeft of Act
	(properties
		y 110
		x 82
		view 19
		loop 2
	)
)
(instance paintingTop of Prop
	(properties
		y 58
		x 161
		view 19
		loop 3
	)
)
(instance table of Prop
	(properties
		y 144
		x 150
		view 19
		loop 1
	)
)
(instance trunk of Prop
	(properties
		y 172
		x 174
		view 18
		loop 13
	)
)