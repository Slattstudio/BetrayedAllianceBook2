;;; Sierra Script 1.0 - (do not remove this comment)
; Score + 1
(script# 235)
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
	rm235 0
)

(local
	
	treeTop = 0
	myEvent
	lookWhere = 4 ; 0 for up, 1 for right, 2 for down, 3 for left
	animation = 0 ; switch to allow me to control control lines in the doitMethod
	
	[mushStr 100]
	
)

(instance rm235 of Rm
	(properties
		picture scriptNumber
		north 257
		east 236
		south 233
		west 243
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript setRegions: 200 setRegions: 205)
		
		(if gKneeHealed
			(if (not gSeparated)
				(leahProp init: setScript: leahScript ignoreControl: ctlWHITE) 
			)
		)
		(switch gPreviousRoomNumber
			(233
				;(gEgo posn: 150 180 loop: 3)
				(PlaceEgo 150 180 3)
				(leahScript cue:)
			)
			(236
				;(gEgo posn: 300 110 loop: 1)
				(PlaceEgo 300 110 1)
				(leahProp posn: 302 140 view: 343 setCycle: Walk setMotion: MoveTo 160 150 leahScript)
			)
			(243
				;(gEgo posn: 10 130 loop: 0)
				(PlaceEgo 10 130 0)
				(leahProp posn: 8 140 view: 343 setCycle: Walk setMotion: MoveTo 160 150 leahScript)
			)
			(257
				;(gEgo posn: 60 100 loop: 2)
				(PlaceEgo 60 100 2)
				(leahScript cue:)
			)
			(else 
				;(gEgo posn: 150 100 loop: 1)
				(PlaceEgo 150 100 1)
				(leahScript cue:)
			)
		)
		(SetUpEgo)
		(gEgo init:)
		;(= gMovementLocked 0)
		(RunningCheck)
		
		
		(actionEgo init: hide: ignoreActors: ignoreControl: ctlWHITE setPri: 15)
		(lookUp init: hide: ignoreActors: setPri: 15)
		(lookRight init: hide: ignoreActors: setPri: 15)
		(lookDown init: hide: ignoreActors: setPri: 15)
		(lookLeft init: hide: ignoreActors: setPri: 15)
		
		(escapeButton init: hide: ignoreActors: setPri: 15)
		
		(treeOne init: ignoreActors: setPri: 1 setScript: climbDownScript)
		(treeTwo init: ignoreActors: setPri: 1)
		 
		(if (not g235Mushroom)
			(mushroom init: setPri: 15)
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
		
		(if (& (gEgo onControl:) ctlMAROON)
			(gRoom newRoom: 257)
		)
		
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
			(if (not animation)
				(gEgo observeControl: ctlRED)
			else
				(gEgo ignoreControl: ctlRED)	
			)	
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
			(if (& (pEvent modifiers?) emRIGHT_BUTTON)
				(if	(checkEvent pEvent (mushroom nsLeft?) (mushroom nsRight?) (mushroom nsTop?) (mushroom nsBottom?))
					(if g235Mushroom
						
					else
						(PrintOther 235 0)
					)
				else
					(if (== (OnControl ocSPECIAL (pEvent x?) (pEvent y?)) ctlNAVY) ; Branch
						(PrintOther 235 1)					
					else
						(if
							(checkEvent pEvent (leahProp nsLeft?) (leahProp nsRight?) (leahProp nsTop?) (leahProp nsBottom?))
							(if gKneeHealed
								(if (not gSeparated)
									(PrintOther 200 29)	
								)
							)
						else
							(if (== (OnControl ocPRIORITY (pEvent x?) (pEvent y?)) ctlBLUE) ; Tree
								(PrintOther 235 2)
							)
						)	
					)
				)
			)
		)
		(if (Said 'talk/woman,leah')	
			(if (and (not gSeparated) gKneeHealed)
				(PrintLeah 235 16)	
			else
				(PrintCantDoThat)
			)
		)
		(if (Said 'run')
			(if treeTop
				(PrintOther 235 9)	
			else
				(RunSneakWalk 1 230 351 (pEvent x?) (pEvent y?) 5 4)
			)	
		)
		(if (Said 'walk')
			(if treeTop
				(Print 235 11)	
			else
				(RunSneakWalk 0 0 343 (pEvent x?) (pEvent y?) 3 2)
			)	
		)
		(if (Said 'sneak')
			(if treeTop
				(PrintOther 235 9)	
			else
				(RunSneakWalk 2 0 352 (pEvent x?) (pEvent y?) 2 1)
			; ego can't sneak, so this extra code:
			(if (and (== gEgoMovementType 2) (not gAnotherEgo))
				(Print	0 45)
				(= gEgoMovementType 0)
				(gEgo xStep: 3 yStep: 2)
				(runProc)
			)
			)	
		)
		(if (or (Said '(climb<up)/tree')
			(Said '(climb<up)[/!*]') ;[/noword]
			(Said 'climb,grab/branch'))
			(if treeTop
				(PrintOther 235 6)
			else
				(if (& (gEgo onControl:) ctlSILVER)
					(if gAnotherEgo
						(RoomScript changeState: 1)
					else
						(PrintOther 235 15)
					)
				else
					(PrintNotCloseEnough)
				)	
			)
		)
		(if (or (Said '(climb<down)/tree')(Said '(climb<down)[/!*]'))
			(if treeTop
				(climbDownScript changeState: 2)
			else
				(PrintOther 235 4 )	; You can't climb down where you are now.
			)
		)
		(if (Said 'climb>')
			(if (Said '/tree')
				(if treeTop
					(climbDownScript changeState: 2)	
				else
					(if (& (gEgo onControl:) ctlSILVER)  
						(if gAnotherEgo 
							(RoomScript changeState: 1)
						else
							(PrintOther 235 15)
						)
					else
						(PrintNotCloseEnough)
					)
				)
			)
			(if (Said '[/!*]')
				(PrintOther 235 7)
			)
		)
		(if (Said '(jump<down,off)/tree')
			(if treeTop
				(PrintOther 235 8)
			else
				(PrintOther 235 9)
			)
		)
		(if (Said 'take,(pick<up)/mushroom')
			(if treeTop
				(if (not g235Mushroom)  
					(PrintOK)
					(= animation 1)
					(RoomScript changeState: 12)
				else
					(PrintAlreadyTookIt)
				)
			else
				(PrintOther 235 3)	; You can't reach it from there.
			)
		)
		(if (Said 'look,view>')
			(if (Said '/mushroom')	
				(if g235Mushroom
					(if (gEgo has: 5)
						(Format @mushStr "This rare mushroom is said to have curative powers. You have %d." gMushrooms)
						(Print @mushStr #icon 614 )
						;(Print 0 2 #icon 612)
					else
						(Print "There's no mushroom here to see.")
					)		
				else
					(PrintOther 235 0)
				)	
			)
			(if (Said '/tree')
				(PrintOther 235 2)
			)
			(if (Said '/branch')
				(PrintOther 235 1)
			)
			
			(if (Said '/forest,verlorn')
				(PrintOther 0 105)	
			)
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
		; handle Said's, etc...
	)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0 ; Handle state changes
			)
			(1
				(ProgramControl)
				(= gEgoMovementType 0) (RunningCheck)
				(gEgo ignoreControl: ctlWHITE setMotion: MoveTo 178 160 RoomScript) ; Walk to tree
			)
			(2
				(gEgo hide:)
				(actionEgo show: view: 361 posn: (gEgo x?)(gEgo y?) setCycle: End RoomScript cycleSpeed: 2) ; prepare for jump animation
			)
			(3
				(actionEgo yStep: 5 setMotion: MoveTo 178 130 RoomScript) ; jump
			)
			(4
				(actionEgo loop: 4 cel: 0 setCycle: End RoomScript ) ; pulling self up
			)
			(5 (= cycles 4)
				(actionEgo loop: 1 cel: 0 posn: (actionEgo x?) (- (actionEgo y?) 33) )  ; standing
			)
			(6
				(actionEgo setCycle: End RoomScript) ; preparing to jump
			)
			(7
				(actionEgo yStep: 5 setMotion: MoveTo 175 90 RoomScript) ; jump
			)
			(8
				(actionEgo loop: 4 cel: 0 setCycle: End RoomScript ) ; pulling self up
			)
			(9 
				(actionEgo hide: )
				(gEgo show: posn: (- (actionEgo x?) 5) (- (actionEgo y?) 37) setMotion: MoveTo 155 44 RoomScript setPri: 15)
			)
			(10	(= cycles 2)
				(gEgo loop: 2)	
			)
			(11
				(gEgo loop: 2)
				(= treeTop 1)
				
				(PlayerControl)
				(if (not g235Mushroom)
					(if gYellowTips
						(= gWndColor 0)
						(= gWndBack 14)
						(Print 235 10 #font 4 #button "Ok")
						(= gWndColor 0)
						(= gWndBack 15)
					)
				)
			)
			(12
				(= animation 1)
				(ProgramControl)
				;(gEgo setMotion: MoveTo 150 44 self ignoreControl: ctlRED) ; walk to the left	
				(gEgo hide:)
				(actionEgo show: posn: (gEgo x?)(gEgo y?) view: 343 setCycle: Walk cycleSpeed: -1 setMotion: MoveTo 150 44 self)
			)
			(13		; pick up animation
				(gEgo hide:)
				(actionEgo show: posn: 150 44 view: 450 loop: 0 cel: 0 setCycle: End self cycleSpeed: 3)	
			)
			(14		; hide mushroom and get back up
				(mushroom hide:)
				(actionEgo view: 450 setCycle: Beg self)
				(PrintOther 235 12)		
			)
			(15		; move back to position and put item in inventory
				(actionEgo hide:)
				(gEgo get: 5 show: posn: (actionEgo x?)(actionEgo y?) setMotion: MoveTo 155 44 self)
				
				(= gMushrooms (+ gMushrooms 1))
				((gInv at: 5) count: gMushrooms)
				(= g235Mushroom 1)	
			)
			(16
				(gEgo observeControl: ctlRED)
				(PlayerControl)
				(= animation 0)
				
				(gGame changeScore: 1)
			)
			(17	(= cycles 1)
				
			)
			(18
				(gEgo setMotion: NULL loop: 2)	
			)	 	
		)
	)
)

(instance climbDownScript of Script
	(properties)
	
	(method (changeState newState dyingScript)
		(= state newState)
		(switch state
			(0
			)
			(1
				; animation of Leah getting into climb down position	
			)
			(2	; climbing down
				(ProgramControl)
				(gEgo hide:)
				(actionEgo show: view: 314 loop: 3 posn: 120 70 setCycle: Fwd yStep: 2 setMotion: MoveTo 120 150 self cycleSpeed: 3)
				(= treeTop 0)
				(= gMap 0)
				(= gArcStl 0)
			)
			(3
				(actionEgo view: 343 setCycle: Walk setMotion: MoveTo 110 170 self cycleSpeed: 1)	
			)
			(4
				(actionEgo hide:)
				(gEgo show: posn: (actionEgo x?)(actionEgo y?) loop: 2 observeControl: ctlWHITE setPri: -1)
				(PlayerControl)			
				
			)
		)
	)
)
(instance leahScript of Script
	(properties)
	
	(method (changeState newState rando)
		(= state newState)
		(switch state
			(1	(= cycles (Random 20 150))
				(leahProp view: 1 loop: 5 setCycle: Fwd cycleSpeed: 5)	
			)
			(2
				(leahProp loop: 6 cel: 0 setCycle: End self)	
			)
			(3	(= cycles (Random 20 150))
				(leahProp loop: 2)	
			)
			(4
				(= rando (Random 7 8))
				(leahProp loop: rando setCycle: End self)	
			)
			(5
				(leahProp setCycle: Beg self)
			)
			(6
				(self cycles: 0 changeState: (Random 1 4))	
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
(procedure (PrintLeah textRes textResIndex)
	(= gWndColor 14)
	(= gWndBack 5)
	(Print scriptNumber textResIndex		
		#width 280
		#at -1 10
		#title "She says:"
	)
	(= gWndColor 0)
	(= gWndBack 15)
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

(instance actionEgo of Act
	(properties
		y 170
		x 160
		view 361
	)
)
(instance mushroom of Prop
	(properties
		y 45
		x 155
		view 002
		loop 1
	)
)
(instance leahProp of Act
	(properties
		y 150
		x 160
		view 1
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
(instance treeOne of Prop
	(properties
		y 70
		x 260
		view 999
		loop 0
	)
)
(instance treeTwo of Prop
	(properties
		y 73
		x 30
		view 999
		loop 2
	)
)