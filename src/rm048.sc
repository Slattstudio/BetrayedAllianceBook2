;;; Sierra Script 1.0 - (do not remove this comment)
; Score + 1
(script# 48)
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
	
	rm048 0
	
)
(local
	
	;wallBroken = 0
	;bookcaseMoved = 0
	deskOpen = 0
	closetOpen = 0
	bug1Moving = 0
	bug2Moving = 0
	
)

(instance rm048 of Rm
	(properties
		picture scriptNumber
		north 0
		east 49
		south 45
		west 0
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript setRegions: 205)
		(switch gPreviousRoomNumber
			(45
				(PlaceEgo 85 160 3)
				;(gEgo posn: 85 160 loop: 3)	
			)
			(49
				(PlaceEgo 220 110 1)
				;(gEgo posn: 220 110 loop: 1)	
			)
			(else 
				(PlaceEgo 150 100 1)
				;(gEgo posn: 150 100 loop: 1)
			)
		)
		(SetUpEgo)
		(gEgo init: observeControl: ctlYELLOW observeControl: ctlNAVY)
		(RunningCheck)
		
		(actionEgo init: hide: ignoreActors: setScript: bedScript)
		(bricks init: setPri: 1 ignoreActors: setScript: breakScript)
		(bookcase init: setPri: 7 ignoreActors: ignoreControl: ctlWHITE setScript: moveScript)
		(alterEgo init: hide: ignoreActors:)
		
		(bug1 init: ignoreActors: ignoreControl: ctlWHITE xStep: 2 yStep: 2 setScript: bugScript)
		(bug2 init: ignoreActors: ignoreControl: ctlWHITE xStep: 2 yStep: 2 setScript: bug2Script)
		
		(cabinet init:)
		(cabinetSmall init:)
		(chair init: ignoreActors:)
		(swingStream init: hide: ignoreActors: setPri: 15)
		(bed init:)
		(table init: ignoreActors:)
		
		(if (not g48Bookshelf)
			(= g48Bookshelf 1)	; despite the name, this actually tracks whether the room has been entered yet (bookshelf moved equals 2)
		)
		(if (== g48Bookshelf 2)
			(bookcase posn: 213 107)
			(gEgo observeControl: ctlBLUE ignoreControl: ctlFUCHSIA)	
		else
			(gEgo observeControl: ctlFUCHSIA ignoreControl: ctlBLUE)	
		)
		(if g48WallBroken
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
			(gRoom newRoom: 49)
		)
		
		(if (== g48WallBroken 1)
			(gEgo ignoreControl: ctlYELLOW)
		else
			(gEgo observeControl: ctlYELLOW)	
		)
		
		(if (>= (gEgo distanceTo: cabinet) 35)
			(if closetOpen
				(cabinet setCycle: Beg)
				(= closetOpen 0)
			)
		)
		(if (>= (gEgo distanceTo: cabinetSmall) 35)
			(if deskOpen
				(cabinetSmall setCycle: Beg)
				(= deskOpen 0)
			)
		)
		
		(if (<= (gEgo distanceTo: bug1) 35)
			(if (not bug1Moving)
				(bugScript changeState: 1)
				(= bug1Moving 1)
			)	
		)
		(if (<= (gEgo distanceTo: bug2) 35)
			(if (not bug2Moving)
				(bug2Script changeState: 1)
				(= bug2Moving 1)
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
						(cabinet nsLeft?)(cabinet nsRight?)(cabinet nsTop?)(cabinet nsBottom?)
					)
					(if closetOpen
						(if gRightClickSearch
							(if (and (<= (gEgo distanceTo: cabinet) 45) (> (gEgo y?) (cabinet y?)) )
								(self changeState: 9)
							else
								(PrintOther 47 11)
							)
						else
							(PrintOther 47 11)
						)
					else
						(if gRightClickSearch
							(if (and (<= (gEgo distanceTo: cabinet) 45) (> (gEgo y?) (cabinet y?)) )
								(self changeState: 9)
							else
								(Print 48 0) ; closed message
							)
						else
							(Print 48 0) ; closed message
						)
					)
				)
				(if
					(checkEvent
						pEvent
						(table nsLeft?)(table nsRight?)(table nsTop?)(table nsBottom?)
					)
					(PrintOther 48 13)
				)
				(if
					(checkEvent
						pEvent
						(+ (chair nsLeft?) 25)(chair nsRight?)(chair nsTop?)(chair nsBottom?)
					)
					(PrintOther 48 14)
				)
				
				(if
					(checkEvent
						pEvent
						(bookcase nsLeft?)(bookcase nsRight?)(bookcase nsTop?)(bookcase nsBottom?)
					)
					(PrintOther 48 1)
					(if (not g48Bookshelf)
						(PrintOther 48 9) ; closed message	
					)
				else
					(if
						(checkEvent
							pEvent
							(bricks nsLeft?)(bricks nsRight?)(bricks nsTop?)(bricks nsBottom?)
						)
						(if (not g48WallBroken)
							(PrintOther 48 17)
						)
					else
						(if (checkEvent
								pEvent
								(cabinetSmall nsLeft?)(cabinetSmall nsRight?)(cabinetSmall nsTop?)(cabinetSmall nsBottom?)
							)
							(PrintOther 48 12)
						)
					)
				)
			)
		)
		(if (Said 'run')
			(Print "There's no reason to do that here.")
		)
		(if (Said '(look<under),search,examine/bed')
			(if (< (gEgo distanceTo: bed) 50)
				(bedScript changeState: 1)	
			else
				(PrintOther 41 4)
			)
		)
		(if (Said 'search/cabinet')
			(if (and (<= (gEgo distanceTo: cabinet) 45) (> (gEgo y?) (cabinet y?)) )
				(self changeState: 9)
			else
				(PrintNotCloseEnough)
			)	
		)
		(if (Said 'look>')
			(if (Said '/floor')
				(PrintOther 48 25)
			)
			(if (Said '/bug')
				(if (<= (gEgo distanceTo: cabinetSmall) 35)	; look at bugs in case
					(PrintOther 48 24)
				else	; look at bugs on the floor
					(PrintOther 48 23)				
				)
			)
			(if (Said '/bookshelf, book')
				(PrintOther 48 1)
			)
			(if (Said '/desk, case, board')
				(PrintOther 48 12)		
			)
			(if (Said '/drawer')
				(if deskOpen
					(PrintOther 48 18)
					(Print 48 19 #font 4 #width 120 #at 20 -1)
				else
					(Print "It's closed.")
				)	
			)
			(if (Said '/cabinet,closet')
				(if closetOpen
					(PrintOther 48 2)
					(PrintOther 48 3)	
				else
					(PrintOther 48 10)
				)
			)
			(if (Said '/bone')
				(if closetOpen
					(PrintOther 48 2)
					(PrintOther 48 3)
				else
					(PrintOther 48 6)
				)
			)
			(if (Said '/bed')
				(PrintOther 47 0)
			)
			(if (Said '/chair')
				(PrintOther 47 14)
			)
			(if (Said '/wall')
				(if g48Bookshelf ; if bookshelf moved
					(if g48WallBroken
						(PrintOther 48 11)	
					else
						(PrintOther 48 9)
					)
				else
					(PrintOther 48 9)
				) 
			)
			(if (Said '/table')
				(PrintOther 48 13)	
			)			
			(if (Said '[/!*]')
				(if g48WallBroken
					(PrintOther 48 26)
				else
					(PrintOther 48 20)
				)
			)
		)
		(if (Said 'take>')
			(if (Said '/finger,hand,bone')
				(if closetOpen
					(PrintOther 48 5)
				else
					(PrintOther 48 6)
				)
			)
			(if (Said '/bug')
				(PrintOther 48 16)	
			)
		)
		(if (Said 'open/cabinet,door')
			(if closetOpen
				(PrintItIs)	
			else
				(if (and (<= (gEgo distanceTo: cabinet) 45) (> (gEgo y?) (cabinet y?)) )
					(self changeState: 9)
				else
					;(PrintOther 48 10)
					(PrintNotCloseEnough)
				)	
			)	
		)
		(if (Said 'close/cabinet')
			(if closetOpen
				(if (< (gEgo distanceTo: cabinet) 30)
					(= closetOpen 0)
					(cabinet setCycle: Beg)
				else
					(PrintNotCloseEnough)
				)
			else
				(PrintItIs)
			) 	
		)
		(if (Said 'open/desk,drawer')
			(if deskOpen
				(PrintItIs)	
			else
				(if (and (<= (gEgo distanceTo: cabinetSmall) 45) (> (gEgo y?) (cabinetSmall y?)) )
					(self changeState: 12)
				else
					(PrintNotCloseEnough)
				)	
			)	
		)
		(if (Said 'open/case')
			(PrintOther 48 16)
		)
		(if (Said 'read/note,letter')
			(if deskOpen
				(self changeState: 12)
			else
				(PrintCantDoThat)	
			)	
		)
		(if 
			(or 
				(Said 'break/wall')
				(Said 'use/hammer/wall') 
				(Said 'hit/wall/hammer')
			)
			(if g48WallBroken
				(Print "The wall is already broken.")
			else
				(if (gEgo has: 3)
					(if (== g48Bookshelf 2)
						(breakScript changeState: 1)
					else
						(PrintOther 48 4) ; While it does look like there are some cracks in the wall, you can't get to them with the bookshelf in the way.
					)
				else
					(PrintOther 48 7)	; A great idea, but you don't have anything to break it with.
				)	
			)				
		)
		(if(Said 'move/bookcase')
			(if (== g48Bookshelf 2)
				(PrintOther 48 8) ; There's no need to move it anywhere else.
			else
				(if (< (gEgo distanceTo: bookcase) 40)
					(moveScript changeState: 1)
				else
					(PrintNotCloseEnough)
				)	
			)
		)
		
		(if (Said '(step<on), squish, kill, smash/bug, spider')
			(= bug1Moving 1)
			(bugScript changeState: 3)
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
				(gEgo setMotion: MoveTo 196 152 self ignoreControl: ctlWHITE)
			)
			(6	; stoop down
				(gEgo hide:)
				(alterEgo show: posn: (gEgo x?)(gEgo y?) view: gEgoPickUpView cel: 0 loop: 0 setCycle: End self cycleSpeed: 3)
			)
			(7	; stand up
				(PrintUp 47 2)
				(alterEgo setCycle: Beg self)
			)
			(8	; return control
				(PlayerControl)
				(alterEgo hide: loop: 0)
				(gEgo show: observeControl: ctlWHITE)
			)
			(9	; walking to closet
				(ProgramControl)
				(gEgo setMotion: MoveTo (cabinet x?) (+ (cabinet y?) 3) self ignoreControl: ctlWHITE)
			)
			(10	(= cycles 2)
				(gEgo loop: 3)
				(if (not closetOpen)
					(= cycles 0)
					(cabinet setCycle: End self cycleSpeed: 2)
				)
			)
			(11	; return control
				(PlayerControl)
				(if (not closetOpen)
					(= closetOpen 1)	
				)
				(PrintOther 48 2)
				(PrintOther 48 3)
			)
			(12	; walking to desk
				(ProgramControl)
				(gEgo setMotion: MoveTo (cabinetSmall x?) (+ (cabinetSmall y?) 3) self ignoreControl: ctlWHITE)
			)
			(13	(= cycles 2)
				(gEgo loop: 3)
				(if (not deskOpen)
					(= cycles 0)
					(cabinetSmall setCycle: End self cycleSpeed: 2)
				)
			)
			(14	; return control
				(PlayerControl)
				(if (not deskOpen)
					(= deskOpen 1)	
				)
				(PrintOther 48 18)
				(Print 48 19 #font 4 #width 120 #at 20 -1)
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
(instance moveScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0
			)
			(1
				(ProgramControl)
				(gEgo setMotion: MoveTo 215 137 self)
				(gEgo ignoreControl: ctlBLUE ignoreControl: ctlFUCHSIA ignoreControl: ctlNAVY)
			)
			(2
				(gEgo setMotion: MoveTo 210 117 self)
				(bookcase setMotion: MoveTo 213 107)	
			)
			(3
				(= g48Bookshelf 2)
				(gEgo observeControl: ctlBLUE ignoreControl: ctlFUCHSIA observeControl: ctlNAVY)
				(bookcase setPri: 5)
				(PlayerControl)	
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
				(gEgo show: loop: 0 ignoreControl: ctlYELLOW)
				(PlayerControl)
				(= g48WallBroken 1)
				(gGame changeScore: 1)
			)
			
		)
	)
)
(instance bugScript of Script
	(properties)
	
	(method (changeState newState dyingScript)
		(= state newState)
		(switch state
			(0
			)
			(1
				(bugMove bug1 bugScript)
			)
			(2
				(= bug1Moving 0)	
			)
			(3	(= cycles 10)
				(ProgramControl)
			)
			(4 	(= cycles 5)
				(PrintOther 48 21)
				(gEgo loop: 2)	
			)
			(5	(= cycles 1)
				(PrintOther 48 22)	
			)
			(6
				(gEgo hide:)
				(alterEgo show: posn: (+ (gEgo x?) 13) (gEgo y?) view: 723 loop: 1 cel: 0 setCycle: End self cycleSpeed: 2)		
			)
			(7
				(ShakeScreen 1)
				(if (not [gDeaths 10])
					(++ gUniqueDeaths)
				)
				(++ [gDeaths 10])
				(= gDeathIconEnd 1)
				
				(= dyingScript (ScriptID DYING_SCRIPT))
				(dyingScript
					caller: 723
					register:
						{\nWas it really bugging you so badly you had try to kill it. Well, now you know gnat to do that again!}
				)
				(gGame setScript: dyingScript)	
			)
		)
	)
)
(instance bug2Script of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0
			)
			(1
				(bugMove bug2 bug2Script)
			)
			(2
				(= bug2Moving 0)	
			)
		)
	)
)
(procedure (bugMove view whichScript)
	(view setMotion: MoveTo (+ (- (view x?) 10) (Random 0 20)) (+ (- (view y?) 10) (Random 0 20)) whichScript)		
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
(procedure (PrintUp textRes textResIndex)
	(Print textRes textResIndex		
		#width 280
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
		loop 0
		cel 1
	)
)
(instance bookcase of Act
	(properties
		y 127
		x 221
		view 46
	)
)
(instance bug1 of Act
	(properties
		y 120
		x 180
		view 90
	)
)
(instance bug2 of Act
	(properties
		y 140
		x 145
		view 90
	)
)
(instance bricks of Prop
	(properties
		y 125
		x 232
		view 50
	)
)
(instance cabinet of Prop
	(properties
		y 87
		x 127
		view 19
		loop 9
	)
)
(instance cabinetSmall of Prop
	(properties
		y 84
		x 182
		view 19
		loop 8
	)
)
(instance chair of Prop
	(properties
		y 122
		x 100
		view 18
		loop 3
	)
)

(instance alterEgo of Prop
	(properties
		y 125
		x 232
		view 51
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
		y 124
		x 150
		view 19
		loop 1
	)
)