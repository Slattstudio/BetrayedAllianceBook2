;;; Sierra Script 1.0 - (do not remove this comment)
(script# 59)
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
	rm059 0
)
(local
	canClimb = 0
	treeTop = 0	
	
	myEvent
	lookWhere = 4 ; 0 for up, 1 for right, 2 for down, 3 for left
)


(instance rm059 of Rm
	(properties
		picture scriptNumber
		north 61
		east 62
		south 58
		west 60
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript)
		(switch gPreviousRoomNumber
			(58	; south
				(PlaceEgo 205 160 3)
			)
			(60	; west
				(PlaceEgo 15 120 0)		
			)
			(61	; north
				(PlaceEgo 205 80 2)		
			)
			(62	; east
				(PlaceEgo 300 120 1)		
			)
			(else 
				(gEgo posn: 150 100 loop: 1)
			)
		)
		(SetUpEgo)
		(gEgo init:)
		
		(actionEgo init: hide: ignoreActors: ignoreControl: ctlWHITE)

		(lookUp init: hide: ignoreActors: setPri: 15)
		(lookRight init: hide: ignoreActors: setPri: 15)
		(lookDown init: hide: ignoreActors: setPri: 15)
		(lookLeft init: hide: ignoreActors: setPri: 15)		
		(escapeButton init: hide: ignoreActors: setPri: 15)
				
		(log init: yStep: 1 ignoreControl: ctlWHITE)
		
		(switch g58LogState
			(2	; stationary under tree
				(log posn: 175 163)
				(= canClimb 1)	
			)
			(4	; being moved from room 58
				(RoomScript changeState: 1)	
			)
			(else
				(log hide: ignoreActors:)
				(= canClimb 0)	
			)	
		)
		
		(if gLookingAhead
			(gEgo posn: 155 44 loop: 2 setPri: 15)
			(= treeTop 1)
			(= gMap 1)
			(= gArcStl 0)	
		)
		
	)
)

(instance RoomScript of Script
	(properties)
	
	(method (doit)
		(super doit:)
		; code executed each game cycle
		(= myEvent (Event new: evNULL)) ; check location of cursor
		(if
			(and
				(> (myEvent x?) (- (lookUp nsLeft?) 6))
				(< (myEvent x?) (lookUp nsRight?))
				(> (myEvent y?) (lookUp nsTop?))
				(< (myEvent y?) (+ (lookUp nsBottom?) 12))
			)
			(lookUp cel: 1)
			(lookDown cel: 0)
			(lookRight cel: 0)
			(lookLeft cel: 0)
			(escapeButton cel: 0)
			(= lookWhere 0) ; Look NORTH
		else
			(if
				(and
					(> (myEvent x?) (- (lookRight nsLeft?) 6))
					(< (myEvent x?) (lookRight nsRight?))
					(> (myEvent y?) (lookRight nsTop?))
					(< (myEvent y?) (+ (lookRight nsBottom?) 12))
				)
				(lookUp cel: 0)
				(lookDown cel: 0)
				(lookRight cel: 1)
				(lookLeft cel: 0)
				(escapeButton cel: 0)
				(= lookWhere 1) ; Look RIGHT
			else
				(if
					(and
						(> (myEvent x?) (- (lookDown nsLeft?) 6))
						(< (myEvent x?) (lookDown nsRight?))
						(> (myEvent y?) (lookDown nsTop?))
						(< (myEvent y?) (+ (lookDown nsBottom?) 12))
					)
					(lookUp cel: 0)
					(lookDown cel: 1)
					(lookRight cel: 0)
					(lookLeft cel: 0)
					(escapeButton cel: 0)
					(= lookWhere 2) ; Look SOUTH
				else
					(if
						(and
							(> (myEvent x?) (- (lookLeft nsLeft?) 6))
							(< (myEvent x?) (lookLeft nsRight?))
							(> (myEvent y?) (lookLeft nsTop?))
							(< (myEvent y?) (+ (lookLeft nsBottom?) 12))
						)
						(lookUp cel: 0)
						(lookDown cel: 0)
						(lookRight cel: 0)
						(lookLeft cel: 1)
						(escapeButton cel: 0)
						(= lookWhere 3) ; Look LEFT
					else
						(if
							(and
								(> (myEvent x?) (- (escapeButton nsLeft?) 6))
								(< (myEvent x?) (escapeButton nsRight?))
								(> (myEvent y?) (escapeButton nsTop?))
								(< (myEvent y?) (+ (escapeButton nsBottom?) 12))
							)
							(lookUp cel: 0)
							(lookDown cel: 0)
							(lookRight cel: 0)
							(lookLeft cel: 0)
							(escapeButton cel: 1)
							(= lookWhere 5) ; Look ESCAPE
						else
							(lookUp cel: 0)
							(lookDown cel: 0)
							(lookRight cel: 0)
							(lookLeft cel: 0)
							(escapeButton cel: 0)
							(= lookWhere 4) ; Look NOWHERE
						)
					
					)
				)
			)	
		)
		
		(myEvent dispose:)	; end the checking of location of cursor
		
		(if (== treeTop 1)
			;(if (not animation)
				(gEgo observeControl: ctlRED)
			;else
			;	(gEgo ignoreControl: ctlRED)	
			;)	
		else
			(= gLookingAhead 0)
			(lookUp hide:)
			(lookRight hide:)
			(lookDown hide:)
			(lookLeft hide:)
			(escapeButton hide:)
			(gEgo ignoreControl: ctlRED)
		)
		(if gLookingAhead
			;(= gLookingAhead 1)
			(lookUp show:)
			(lookRight show:)
			(lookDown show:)
			(lookLeft show:)
			(escapeButton show:)
		else
			(lookUp hide:)
			(lookRight hide:)
			(lookDown hide:)
			(lookLeft hide:)
			(escapeButton hide:)
		)
	)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		
		(if (and gLookingAhead (not gProgramControl))
			(switch (pEvent message?)
				(1	; up
					(gRoom newRoom: (gRoom north:)) ; NORTH	
				)
				(3	; right
					(gRoom newRoom: (gRoom east:)) ; RIGHT
				)
				(5	; down
					(gRoom newRoom: (gRoom south:)) ; SOUTH
				)
				(7	; left
					(gRoom newRoom: (gRoom west:)) ; LEFT
				)	
			)
		)
		(if (== (pEvent type?) evKEYBOARD)
			(if treeTop
				(if gLookingAhead
					(if (== (pEvent message?) KEY_ESCAPE)
						(= gLookingAhead 0)
						(TheMenuBar state: ENABLED)	
						(= gMap 0)
						(= gArcStl 0)
					)
				)
			)
		)
		(if (== (pEvent type?) evMOUSEBUTTON)
			; Clicking on the arrows to show the rooms in any direction		
			(if treeTop
				(if gLookingAhead
					(if (== (pEvent message?) KEY_ESCAPE)
						(= gLookingAhead 0)
						(TheMenuBar state: ENABLED)	
						(= gMap 0)
						(= gArcStl 0)
					)
					(if (not gProgramControl)
						(switch lookWhere			
							(0
								(gRoom newRoom: (gRoom north:)) ; NORTH
							)
							(1
								(gRoom newRoom: (gRoom east:)) ; RIGHT
							)
							(2
								(gRoom newRoom: (gRoom south:)) ; SOUTH
							)
							(3
								(gRoom newRoom: (gRoom west:)) ; LEFT
							)
							(5	; ESCAPE
								(= gLookingAhead 0)
								(TheMenuBar state: ENABLED)	
								(= gMap 0)
								(= gArcStl 0)	
								(self changeState: 17)
							)
						)
					)
				)
			)
		)
		; handle Said's, etc...
		(if (or (Said '(climb<up)/tree')
			(Said '(climb<up)[/!*]') ;[/noword]
			(Said 'climb,grab/branch'))
			(if treeTop
				(PrintOther 235 6)
				(Print "You're already at the top.")
			else
				(if canClimb
					(if (<= (gEgo distanceTo: log) 30)
						(RoomScript changeState: 3)	; climb up the tree
						;(gEgo ignoreActors: setMotion: MoveTo (- (log x?) 10) (log y?) self 4 )
					else
						(PrintNotCloseEnough)
					)
				else
					(PrintOther 59 1) ; You can't climb this tree yet. The limb is just too high to grab.
				)	
			)
		)
		(if (or (Said '(climb<down)/tree')(Said '(climb<down)[/!*]'))
			(if canClimb
				(if treeTop
					(self changeState: 14)	
				else
					(PrintOther 235 4 )	; You can't climb down where you are now.
				)
			else
				(PrintOther 59 1) ; You can't climb this tree yet. The limb is just too high to grab.
			)
		)
		(if (Said 'climb>')
			(if (Said '/tree')
				(if treeTop
					(self changeState: 14)	
				else
					(if canClimb
						(if (<= (gEgo distanceTo: log) 30)
							(RoomScript changeState: 3)	; climb up the tree
							;(gEgo ignoreActors: setMotion: MoveTo (- (log x?) 10) (log y?) self 4 )
						else
							(PrintNotCloseEnough)
						)
					else
						(Print "You can't yet climb the tree. The limb is too high to grab.")
					)
					
				)
			)
			(if (Said '[/!*]')
				(PrintOther 235 7)
			)
		)
		(if (Said 'look,view>')
			(if (Said '[/!*]')
				(if treeTop
					(= gLookingAhead 1)
					(TheMenuBar state: DISABLED)
				else
					(PrintOther 235 5)
				)
			)	
		)
		(if (Said 'close,exit,stop')
			(if gLookingAhead
				(= gLookingAhead 0)
				(= gMap 0)
				(= gArcStl 0)
				(TheMenuBar state: ENABLED)
			else
				(PrintCantDoThat)	
			)
		)
	)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0 ; Handle state changes
			)
			(1	; moving the log into place from below
				(ProgramControl)	
				(log posn: 175 183 setMotion: MoveTo 175 163)
				(gEgo posn: (log x?) 186 yStep: 1 setMotion: MoveTo (log x?) 166 self)	
			)
			(2
				(= g58LogState 2)
				(= canClimb 1)
				(RunningCheck)
				(PlayerControl)	
			)
			(3	; moving to log
				(ProgramControl)
				(gEgo setMotion: MoveTo (- (log x?) 10) (log y?) self)	
			)
			(4
				(gEgo hide:)
				(actionEgo show: view: 361 posn: (gEgo x?)(gEgo y?) setCycle: End RoomScript cycleSpeed: 2) ; prepare for jump animation
			)
			(5
				(actionEgo yStep: 5 setMotion: MoveTo (- (log x?) 20) 130 RoomScript) ; jump
			)
			(6
				(actionEgo loop: 4 cel: 0 setCycle: End RoomScript ) ; pulling self up
			)
			(7 (= cycles 4)
				(actionEgo loop: 1 cel: 0 posn: (actionEgo x?) (- (actionEgo y?) 33) )  ; standing
			)
			(8
				(actionEgo setCycle: End RoomScript) ; preparing to jump
			)
			(9
				(actionEgo yStep: 5 setMotion: MoveTo (- (log x?) 30) 90 RoomScript) ; jump
			)
			(10
				(actionEgo loop: 4 cel: 0 setCycle: End RoomScript ) ; pulling self up
			)
			(11 
				(actionEgo hide: )
				(gEgo show: posn: (- (actionEgo x?) 5) (- (actionEgo y?) 37) setMotion: MoveTo 125 55 RoomScript setPri: 15)
			)
			(12	(= cycles 2)
				(gEgo loop: 2)	
			)
			(13
				(PlayerControl)
				(= treeTop 1)	
			)
			; CLIMBING DOWN TREE
			(14	; climbing down
				(ProgramControl)
				(gEgo hide:)
				(actionEgo show: view: 314 loop: 3 posn: 100 70 setCycle: Fwd yStep: 2 setMotion: MoveTo 100 150 self cycleSpeed: 3)
				(= treeTop 0)
				(= gMap 0)
				(= gArcStl 0)
			)
			(15
				(actionEgo view: 343 setCycle: Walk setMotion: MoveTo 110 170 self cycleSpeed: 1)	
			)
			(16
				(actionEgo hide:)
				(gEgo show: posn: (actionEgo x?)(actionEgo y?) loop: 2 observeControl: ctlWHITE setPri: -1)
				(PlayerControl)			
				
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
(instance actionEgo of Act
	(properties
		y 170
		x 160
		view 361
	)
)
(instance log of Act
	(properties
		y 150
		x 175
		view 99
	)
)
(instance escapeButton of Prop
	(properties
		y 9
		x 300
		view 998
		loop 4
	)
)
(instance lookUp of Prop
	(properties
		y 30
		x 90
		view 958
		loop 0
	)
)
(instance lookRight of Prop
	(properties
		y 100
		x 290
		view 958
		loop 1
	)
)
(instance lookDown of Prop
	(properties
		y 180
		x 160
		view 958
		loop 2
	)
)
(instance lookLeft of Prop
	(properties
		y 100
		x 20
		view 958
		loop 3
	)
)