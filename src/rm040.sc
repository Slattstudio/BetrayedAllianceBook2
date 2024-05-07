;;; Sierra Script 1.0 - (do not remove this comment)
; +2 score
(script# 40)
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
	
	rm040 0
	
)

(local
	
	vaseFalling = 0
	whichVase = 0
	vase1Broken = 0
	
	[stringName 5] ;  used for the combination of the lock on the chest
	chestOpen = 0 
	;doorOpen = 0
	cabinetOpen = 0
	
)


(instance rm040 of Rm
	(properties
		picture scriptNumber
		north 46
		east 43
		south 266
		west 0
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript
			setRegions: 205
		)
		
		;(= gAnotherEgo 1)
		(SetUpEgo)
		(RunningCheck)
		
		(gEgo init:)
		(alterEgo init: hide: ignoreActors:)
		(bricks init: hide: setPri: 1 ignoreActors:)
		(chair init: cel: 2 ignoreActors:)
		(chair2 init: ignoreActors:)
		(paper init: hide: ignoreActors: setPri: 15)
		
		(rock init:)
		(door init: setPri: 0 ignoreActors:)
		(bed init: setScript: bedScript)
		(closet init:)
		(shards init: ignoreActors:)
		(table init: ignoreActors: setPri: 11)
		(trunk init: setScript: chestScript)
		(vase1 init: ignoreActors: setScript: vaseScript)
		(vaseTrash init: ignoreActors:)
		;(vaseThin1 init: ignoreActors:)
		
		(switch gPreviousRoomNumber
			(266
				(if (== g266paper 1)
					(gEgo hide:)
					(alterEgo show: posn: 150 130)
					(RoomScript changeState: 1)
					(= g266paper 2)
					(= gInDorm 1)
				else
					(PlaceEgo 220 110 1)
				)
			)
			
			(else
				(PlaceEgo 220 110 1)
				(= vase1Broken 1)
				(vase1 loop: 10)
				;(gEgo posn: 220 110 loop: 1)
			)
		)
		
	)
)

(instance RoomScript of Script
	(properties)
	
	(method (doit)
		(super doit:)
		; code executed each game cycle
		(if (< (gEgo distanceTo: vase1) 30)
			(= whichVase 1)
		else
			(if (< (gEgo distanceTo: vaseThin1) 30)
				(= whichVase 2)	
			else
				(if (< (gEgo distanceTo: vase2) 30)
					(= whichVase 3)
				else
					(= whichVase 0)
				)
			)
		)
		
		(if (not vase1Broken)
			(VaseFall vase1)
		)
		
		(if (& (gEgo onControl:) ctlFUCHSIA)
			(if (not vase1Broken)
				(= whichVase 1)	
				(if (not vaseFalling)
					(vaseScript changeState: 1)
					(= vaseFalling 1)	
				)		
			)
		)
		
		(if (& (gEgo onControl:) ctlMAROON)
			(gRoom newRoom: 43)
		)
		
		(if (> (gEgo distanceTo: closet) 20)
			(if cabinetOpen
				(closet setCycle: Beg)
				(= cabinetOpen 0)
			)	
		)
		(if (not (& (gEgo onControl:) ctlSILVER) )
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
						pEvent (vaseTrash nsLeft?)(vaseTrash nsRight?)(vaseTrash nsTop?)(vaseTrash nsBottom?)
					)
					(if gRightClickSearch
						(if (<= (gEgo distanceTo: vaseTrash) 35)
							(self changeState: 15)
						else
							(PrintOther 40 14)	
						)
					)
				else
					(if	
						(checkEvent
							pEvent 117 151 64 85 ; rubble neqr door
						)
						(PrintOther 40 3)	
					else
						(if	
							(checkEvent
								pEvent
								(door nsLeft?)(door nsRight?)(door nsTop?)(door nsBottom?)
							)
							(PrintOther 40 3)
						)
					)
				)
				(if	
					(checkEvent
						pEvent
						(table nsLeft?)(table nsRight?)(table nsTop?)(table nsBottom?)
					)
					(PrintOther 40 2)	
				)
				(if	
					(checkEvent
						pEvent
						(chair nsLeft?)(- (chair nsRight?) 13)(chair nsTop?)(chair nsBottom?)
					)
					(PrintOther 40 1)	
				)
				(if	
					(checkEvent
						pEvent
						(vase1 nsLeft?)(vase1 nsRight?)(vase1 nsTop?)(vase1 nsBottom?)
					)
					(if vase1Broken
						(PrintOther 40 13)
					else
						(PrintOther 40 12)	
					)
				)
				(if	
					(checkEvent
						pEvent
						(shards nsLeft?)(shards nsRight?)(shards nsTop?)(shards nsBottom?)
					)
					(PrintOther 40 13)	
				)
				(if	
					(checkEvent	pEvent
						(bed nsLeft?)(bed nsRight?)(bed nsTop?)(bed nsBottom?)
					)
					(if gRightClickSearch
						(if (< (gEgo distanceTo: bed) 50)
							(bedScript changeState: 1)	
						else
							(PrintOther 40 4)
						)
					else
						(PrintOther 40 4)
					)
				)
				(if	
					(checkEvent
						pEvent
						(trunk nsLeft?)(trunk nsRight?)(trunk nsTop?)(trunk nsBottom?)
					)
					(if g40ChestUnlocked
						(PrintOther 40 16)
					else
						(PrintOther 40 15)	
					)
				)
				(if	
					(checkEvent
						pEvent
						(closet nsLeft?)(closet nsRight?)(closet nsTop?)(closet nsBottom?)
					)
					(if cabinetOpen
						(if gRightClickSearch
							(if (and (<= (gEgo distanceTo: closet) 35) (> (gEgo y?) (closet y?)) )
								(self changeState: 8)
							else
								(PrintOther 47 15)
							)
						else
							(PrintOther 49 15)
						)
					else
						(if gRightClickSearch
							(if (and (<= (gEgo distanceTo: closet) 35) (> (gEgo y?) (closet y?)) )
								(self changeState: 8)
							else
								(PrintOther 49 14) ; closed message	
							)
						else
							(PrintOther 49 14) ; closed message
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
		(if (Said '(look<in),search/chest,box,trunk')
			(if (& (gEgo onControl:) ctlSILVER)
				(chestScript changeState: 1)	
			else
				(PrintNotCloseEnough)
			)	
		)
		(if (Said 'open,unlock/chest,box,trunk')
			(if (& (gEgo onControl:) ctlSILVER)
				(if (not g40ChestUnlocked)
					(if (not chestOpen)
						(chestScript changeState: 1)
					;else
						;(chestScript changeState: 10)	
					)
				else
					(if chestOpen
						(PrintItIs)	
					else
						(chestScript changeState: 10)
					)
				)
			else
				(PrintNotCloseEnough)
			)	
		)
		(if (Said 'close/cabinet,door,trunk,chest')
			(if chestOpen
				(= chestOpen 0)
				(trunk setCycle: Beg)
			else
				(if cabinetOpen
					(= cabinetOpen 0)
					(closet setCycle: Beg)
				else
					(PrintCantDoThat)
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
			(if (Said '/vase, paper')
				(if (< (gEgo distanceTo: vaseTrash) 50)
					(self changeState: 15)
				else
					(Print 51)
				)	
			)
			(if (Said '/key')
				(if (or (gEgo has: 10) (== (IsOwnedBy 10 43) 1))
					(PrintAlreadyTookIt)
				else
					(PrintOther 40 33)
				)
			)
		)
		(if (Said 'search/cabinet')
			(if (and (<= (gEgo distanceTo: closet) 20) (> (gEgo y?) (closet y?)) )
				(self changeState: 8)
			else
				(PrintNotCloseEnough)
			)	
		)
		(if(Said 'open>')
			(if (Said '/door')
				(if (and (and 
					(<= (gEgo distanceTo: closet) 45)
					(> (gEgo y?) (closet y?))) 
					(> (gEgo x?) 162) )
					(self changeState: 12)
				else
					(PrintOther 40 6)
				)
			)
			(if (Said '/chest,box,trunk')
				(if (& (gEgo onControl:) ctlSILVER)
					(if (not chestOpen)
						; changestate	
					else
						(PrintItIs)
					)
				else
					(PrintNotCloseEnough)
				)	
			)
			(if (Said '/cabinet,dresser,closet')
				(if (< (gEgo distanceTo: closet) 20)
					(if (not cabinetOpen)
						(self changeState: 8)
					else
						(Print 40 8) ; It's already open
					)	
				else
					(PrintNotCloseEnough)
				)
			)
			(if (Said '/*')
				(Print 40 7)	
			)	
		)
		(if (Said 'touch/chair')
			(PrintOther 44 27)			
		)
		(if (Said 'search, (look<in)/vase')
			(if (< (gEgo distanceTo: vaseTrash) 50)
				(self changeState: 15)
			else
				(PrintOther 40 31)
			)			
		)
		(if (Said '(look<under),search,examine/bed,mattress')
			(if (< (gEgo distanceTo: bed) 50)
				(bedScript changeState: 1)	
			else
				(PrintOther 40 31)
			)
		)
		(if (Said 'read/paper,note,letter')
			(if (< (gEgo distanceTo: vaseTrash) 50)
				(self changeState: 15)
			else
				(PrintNotCloseEnough)
			)
		)
		(if (Said 'look<under/rock')
			(PrintOther 40 29)	
		)
		(if (Said 'look>')
			(if (Said '/bed')
				(PrintOther 40 4)		
			)
			(if (Said '/table')
				(PrintOther 40 2)		
			)
			(if (Said '/paper')
				(if (< (gEgo distanceTo: vaseTrash) 50)
					(self changeState: 15)
				else
					(PrintOther 40 14)	
				)		
			)
			(if (Said '/door,rock,rubble')
				(PrintOther 40 3)		
			)
			(if (Said '/clothes')
				(if chestOpen
					(PrintOther 40 23)
				else
					(PrintOther 40 26)
				)		
			)
			(if (Said '/chair')
				(PrintOther 40 1)		
			)
			(if (Said '/cabinet')
				(if cabinetOpen
					(PrintShort 49 15)	
				else
					(PrintOther 49 14)	
				)							
			)
			(if (Said '/chest,trunk')
				(if g40ChestUnlocked
					(PrintOther 40 16)
				else
					(PrintOther 40 15)	
				)		
			)
			(if (Said '/vase,pot')
				(if (< (gEgo distanceTo: vaseTrash) 50)
					(PrintOther 40 14)	
				else
					(if vase1Broken
						(PrintOther 40 13)
					else
						(PrintOther 40 25)
					)
				)	
			)
			(if (Said '[/!*]')	; this will handle just "look" by itself
				(PrintOther 40 0)
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
				(gTheMusic number: 41 loop: -1 priority: -1 play:)	
				
				(paper show: setCycle: End self cycleSpeed: 3)
				(alterEgo setCycle: Fwd cycleSpeed: 2)
					
			)
			(2
				(alterEgo view: 311 loop: 3 cel: 3 setCycle: End self)	
			)
			(3
				(alterEgo loop: 4 cel: 0 setCycle: End self)	
			)
			(4 (= cycles 21)
				(alterEgo loop: 5 cel: 0 setCycle: Fwd)	
			)
			(5
				(alterEgo loop: 6 cel: 0 setCycle: End self)
				(Print 0 67 #font 4 #at 180 -1 #width 110)
				(= [gNotes 4] 1)	
			)
			(6
				(alterEgo loop: 2 cel: 0 setCycle: End self)	
			)
			(7
				(PlayerControl)
				(alterEgo hide:)
				(gEgo show: posn: (alterEgo x?)(alterEgo y?) loop: 0)	
				(= gSwitchingAllowed 1)
				(SetMenu MENU_SWITCH 112 1)	
				(gGame changeScore: 1)
				(if gYellowTips
					(= gWndColor 0)
					(= gWndBack 14)
					(Print 40 30 #font 4 #at -1 10 #button "Ok")
					(= gWndColor 0)
					(= gWndBack 15)
				)
			)
			; open closet
			(8
				(ProgramControl)
				(gEgo setMotion: MoveTo (closet x?)(+ (closet y?) 3) self)	
			)
			(9	(= cycles 2)
				(gEgo loop: 3)
			)
			(10
				(if (not cabinetOpen)
					(closet setCycle: End self cycleSpeed: 3)
				else
					(self cue:)
				)	
			)
			(11
				(PrintShort 49 15)
				(= cabinetOpen 1)
				(PlayerControl)	
			)
			(12	; walking to closet
				(ProgramControl)
				(gEgo setMotion: MoveTo (closet x?) (+ (closet y?) 3) self ignoreControl: ctlWHITE)
			)
			(13	(= cycles 2)
				(gEgo loop: 3)
				(if (not cabinetOpen)
					(= cycles 0)
					(closet setCycle: End self cycleSpeed: 2)
				)
			)
			(14	; return control
				(PlayerControl)
				(if (not cabinetOpen)
					(= cabinetOpen 1)	
				)
				(Print "Rats. Nothing's inside.")
			)
			(15	; walk to vase with paper in it
				(ProgramControl)
				(gEgo setMotion: MoveTo (vaseTrash x?)(+ (vaseTrash y?) 2) self)
			)	
			(16 (= cycles 3)
				(gEgo loop: 3)
			)
			(17
				(PrintOther 40 17)
				(Print 40 20 #width 120 #font 4 #at 160 -1 #title "It reads:")
				(PlayerControl)
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
				(PrintOther 40 28)
				(Print 40 27 #font 4 #at 160 -1 #width 120 #title "It reads:")
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
				(alterEgo show: view: 450 loop: 1 cel: 0 posn: (gEgo x?) (gEgo y?) setCycle: End self cycleSpeed: 2)	
			)
			(3
				;	check if unlocked
				(if (not g40ChestUnlocked)
					(= stringName 0)
					(EditPrint
						@stringName
						8
						{The lock has an alphabetical, 5-letter code. Enter here:}
						#at
						-1
						20
					)
					(if (or (== (StrCmp @stringName {moski}) 0) (== (StrCmp @stringName {Moski}) 0))
						(= g40ChestUnlocked 1)	
					)
					(if g40ChestUnlocked
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
					(= chestOpen 1)
				else
					(self cue:)
				)
			)
			(5 (= cycles 7); loot
				(if (or (gEgo has: 10) (== (IsOwnedBy 10 43) 1))
					(PrintUp 40 21)	
				else
					(PrintUp 40 22)
					(gEgo get: 10) ; cabinet key
					(gGame changeScore: 1)
				)
			)
			(6
				; stand up again
				(alterEgo setCycle: Beg self)
			)
			(7
				(PlayerControl)
				(alterEgo hide:)
				(gEgo show: loop: 1 observeControl: ctlWHITE)
			)
			(8
				; print locked, stand up
				(PrintUp 40 10) ; The lock is fastened tight.
				(alterEgo setCycle: Beg self)
			)
			(9
				(PlayerControl)
				(alterEgo hide:)
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
				(alterEgo show: view: 450 loop: 1 cel: 0 posn: (gEgo x?) (gEgo y?) setCycle: End self cycleSpeed: 2)
			)
			(12	; if unlocked, open
				(trunk setCycle: End self cycleSpeed: 2)
				(= chestOpen 1)
			)
			(13
				(PrintUp 40 9)
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
(instance vaseScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0
			)
			(1
				(switch whichVase
					(1
						(vaseScript changeState: 2)
					)
					(2
						(vaseScript changeState: 5)
					)
					(3
						(vaseScript changeState: 8)
					)
				)	
			)
			(2
				(ProgramControl)
				(vase1 setCycle: End self cycleSpeed: 2)
			)
			(3 (= cycles 5)
				(ShakeScreen 1)
				(vase1 loop: 10)
					
			)
			(4
				(PlayerControl)
				(= vase1Broken 1)
				(PrintOther 40 32)	
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
		#width 280
		#at -1 10
	)
)

(procedure (VaseFall view)
	(if (gEgo inRect: (- (view x?) 7) (- (view y?) 5) (+ (view x?) 12) (+ 3 (view y?) ))
		(if (== gEgoMovementType 2)
			(if (gEgo inRect: (- (view x?) 6) (- (view y?) 4) (+ (view x?) 11) (+ 3 (view y?) ))
				(if (not vaseFalling)
					(vaseScript changeState: 1)
					(= vaseFalling 1)	
				)
			)
		else	
			(if (not vaseFalling)
				(vaseScript changeState: 1)
				(= vaseFalling 1)	
			)
		)	
	)
)

(instance alterEgo of Prop
	(properties
		y 130
		x 150
		view 311
	)
)
(instance bricks of Prop
	(properties
		y 125
		x 232
		view 50
		loop 4
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
(instance chair of Prop
	(properties
		y 130
		x 120
		view 18
		loop 3
		
	)
)
(instance chair2 of Prop
	(properties
		y 156
		x 200
		view 16
		loop 4
		
	)
)
(instance closet of Prop
	(properties
		y 87
		x 187
		view 16
		loop 2
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
(instance paper of Prop
	(properties
		y 110
		x 160
		view 62
		loop 4
	)
)
(instance rock of Prop
	(properties
		y 94
		x 160
		view 16
		loop 12
	)
)
(instance shards of Prop
	(properties
		y 133
		x 180
		view 16
		loop 11
	)
)
(instance table of Prop
	(properties
		y 170
		x 220
		view 16
		loop 13
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
(instance vase1 of Prop
	(properties
		y 113
		x 222
		view 16
		loop 6
	)
)
(instance vase2 of Prop
	(properties
		y 165
		x 167
		view 16
		loop 6
	)
)
(instance vaseThin1 of Prop
	(properties
		y 132
		x 116
		view 16
		loop 7
	)
)
(instance vaseTrash of Prop
	(properties
		y 87
		x 111
		view 16
		loop 15
	)
)
