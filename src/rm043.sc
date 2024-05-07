;;; Sierra Script 1.0 - (do not remove this comment)
; +2 score
(script# 43)
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

	rm043 0
	
)
(local
	
	vaseFalling = 0	; used to prevent infinite regress in doit
	whichVase = 0 ; indicates which vase ego is near correct one will fall
	chairFalling = 0
	looseBoard = 0
	
	doorOpen = 0
	doorClosing = 0
	cabinetUnlocked = 0
	cabinetOpen = 0
	cabinetClosing = 0
	
	[canString 40] 
	
	tooClose = 0	; got to close to the bed
)


(instance rm043 of Rm
	(properties
		picture scriptNumber
		north 46
		east 0
		south 0
		west 40
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript setRegions: 205)
		(switch gPreviousRoomNumber
			(40
				(PlaceEgo 100 117 0)
				;(gEgo posn: 100 110 loop: 0)		
			)
			(46
				(PlaceEgo 180 90 2)
				;(gEgo posn: 180 90 loop: 2)
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
		
		(actionEgo init: hide: ignoreActors: ignoreControl: ctlWHITE)
		(vase1 init: ignoreActors: setScript: vaseScript)
		(vase2 init: ignoreActors:)
		(vaseThin1 init: ignoreActors:)
		(bed init: setScript: wakeUpScript)
		(zzz init: ignoreActors: setCycle: Fwd cycleSpeed: 5 setPri: 15)
		(bookcase init: setPri: 7 ignoreActors:)
		(cabinet init: setScript: cabinetScript cycleSpeed: 2)
		(table init:)
		(chair1 init: cel: 2)
		(chair2 init: ignoreActors:)
		(dagger init: hide: ignoreControl: ctlWHITE ignoreActors: setCycle: Walk)
		(door init: setScript: doorScript ignoreActors:)
		(screech init: hide: ignoreActors: setPri: 15)
		(verlorn init: ignoreActors: setPri: 14)
		
		(if g43DoorOiled
			(gEgo ignoreControl: ctlYELLOW)
			(cabinet loop: 1)	
		else
			(if (gEgo has: 9)
				(cabinet loop: 1)
			)
		)
		
		
	)
)

(instance RoomScript of Script
	(properties)
	
	(method (doit)
		(super doit:)
		; code executed each game cycle
		
		(if (> (gEgo distanceTo: door) 25)
			(if doorOpen
				(if (not doorClosing)
					(doorScript changeState: 11)
					(= doorClosing 1)
				)
			)
		)
		(if (>= (gEgo distanceTo: cabinet) 20)
		(if cabinetOpen
			(cabinet setCycle: Beg)
			(= cabinetOpen 0)
		)
	)
	
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
		(VaseFall vase1)
		(VaseFall vase2)
		(VaseFall vaseThin1)
			
		
		(if (gEgo inRect: (- (chair2 x?) 29) (- (chair2 y?) 8) (+ (chair2 x?) 1) (+ 5 (chair2 y?) ))
			(if (not chairFalling)
				(RoomScript changeState: 4)
				(= chairFalling 1)	
			)	
		)
		(if (& (gEgo onControl:) ctlRED)
			(if (not tooClose)
				(ProgramControl)
				(= tooClose 1)
				(wakeUpScript changeState: 1)
			)			
		)
		
		
		(if (& (gEgo onControl:) ctlMAROON)
			(gRoom newRoom: 40)
		)
		(if (& (gEgo onControl:) ctlGREY)
			(gRoom newRoom: 46)
		)
		(if doorOpen
			(gEgo ignoreControl: ctlYELLOW)
		else
			(gEgo observeControl: ctlYELLOW)
		)
		
		(if (& (gEgo onControl:) ctlBLUE)
			(if (not looseBoard)
				(RoomScript changeState: 1)
				(= looseBoard 1)
			)
		)
		(if (& (gEgo onControl:) ctlCYAN)
			(if (< gEgoMovementType 2)	; if not sneaking
				(if (not looseBoard)
					(RoomScript changeState: 1)
					(= looseBoard 1)
				)
			)
		)
	)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if (& (pEvent modifiers?) emRIGHT_BUTTON)
				
				(if (== (OnControl ocSPECIAL (pEvent x?) (pEvent y?)) ctlBLUE) ; floor boards
					(PrintOther 43 23)
				)
				(if	
					(checkEvent
						pEvent
						(door nsLeft?)(door nsRight?)(door nsTop?)(door nsBottom?)
					)
					(PrintOther 43 24)	
				)
				(if	
					(checkEvent
						pEvent
						(bed nsLeft?)(bed nsRight?)(+ (bed nsTop?) 10)(bed nsBottom?)
					)
					(PrintOther 43 4)	
				)
				(if	
					(checkEvent
						pEvent
						(cabinet nsLeft?)(cabinet nsRight?)(cabinet nsTop?)(cabinet nsBottom?)
					)
					(if cabinetOpen
						(if (or g43DoorOiled
							(gEgo has: 9))
							(PrintOther 43 20)
						else
							(PrintOther 43 21)
						)
								
					else
						(if (== (IsOwnedBy 10 43) TRUE)
							(PrintOther 43 22)	 ; cabinet key in toom
						else
							(PrintOther 43 19)
						)
					)	
				)
				(if	
					(or (or (checkEvent pEvent
							(vase1 nsLeft?)(vase1 nsRight?)(vase1 nsTop?)(vase1 nsBottom?)
						)
						(checkEvent
							pEvent
							(vaseThin1 nsLeft?)(vaseThin1 nsRight?)(vaseThin1 nsTop?)(vaseThin1 nsBottom?)
						))
						(checkEvent
							pEvent
							(vase2 nsLeft?)(vase2 nsRight?)(vase2 nsTop?)(vase2 nsBottom?)
							
						)
					)
					(Print 43 18 #at -1 150)	
				else
					(if	
						(checkEvent
							pEvent
							(table nsLeft?)(table nsRight?)(table nsTop?)(table nsBottom?)
						)
						(PrintOther 43 3)	
					else
						(if	
							(or (checkEvent
									pEvent
									(chair1 nsLeft?)(chair1 nsRight?)(chair1 nsTop?)(chair1 nsBottom?)
								)
								(checkEvent
									pEvent
									(chair2 nsLeft?)(- (chair2 nsRight?) 24)(chair2 nsTop?)(chair2 nsBottom?)
								)
							)
							(PrintOther 43 5)	
						else
							(if	
								(checkEvent
									pEvent
									(bookcase nsLeft?)(bookcase nsRight?)(bookcase nsTop?)(bookcase nsBottom?)
								)
								(PrintOther 43 17)	
							)
						)
					)
				)
			)
		)
		; handle Said's, etc..
		(if (Said 'kill,attack,hit,smash/creature')
			(PrintOther 43 28)	
		)
		(if (Said 'talk/creature')
			(PrintOther 43 29)	
		)
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
		(if (Said 'look<in/vase')
			(PrintOther 43 30)	
		)
		(if (Said 'look,read,take/note,letter,paper')
			(if (< (gEgo distanceTo: table) 26)
				(Print 43 1 #font 4 #at 160 -1 #width 130)
			else
				(PrintNotCloseEnough)
			)	
		)
		(if (Said 'move,(pick<up)/vase')
			(PrintOther 43 31)	
		)
		(if (Said 'move,(pick<up)/chair')
			(if (& (gEgo onControl:) ctlNAVY)
				(ProgramControl)
				(PrintOK)
				(gEgo setMotion: MoveTo (chair2 x?)(chair2 y?))
			else
				(PrintNotCloseEnough)
			)
		)
		(if (Said 'look>')
			(if (Said '/table')
				(if (< (gEgo distanceTo: table) 20)
					(PrintOther 43 3)
					(Print 43 1 #font 4)
				else
					(PrintOther 43 3)		
				)
			)
			(if (Said '/bed,creature')
				(PrintOther 43 4)
			)
			(if (Said '/chair')
				(PrintOther 43 5)
			)
			(if (Said '/bookshelf,book')
				(PrintOther 43 17)
			)
			(if (Said '/vase,pot')
				(PrintOther 43 18)
			)
			(if (Said '/cabinet')
				(if cabinetOpen
					(if (or g43DoorOiled
						(gEgo has: 9))
						(PrintOther 43 20)
					else
						(PrintOther 43 21)
					)			
				else
					(if (== (IsOwnedBy 10 43) TRUE)
						(PrintOther 43 22)	 ; cabinet key in room
					else
						(PrintOther 43 19)
					)
				)
			)
			(if (Said '/floor,board')
				(PrintOther 43 23)
			)
			(if (Said '/door')
				(PrintOther 43 24)
			)
			(if (Said '/oil,can,flask')
				(if (gEgo has: 9)
					(Format @canString "An olive oil canister. You expect %u more use(s) before it's empty." gOil)
					(Print @canString #title "Oil Can" #icon 618)
				else
					(if g43DoorOiled
						(PrintOther 43 25)	
					else
						(if cabinetOpen
							(PrintOther 43 8)	
						else
							(PrintOther 43 26)
						)
					) 
				)
			)
			(if (Said '[/!*]')
				(PrintOther 43 0)
			)	
		)
		(if (Said 'unlock,search/cabinet')
			(if (< (gEgo distanceTo: cabinet) 20)
					(if (not cabinetOpen)
						(cabinetScript changeState: 1)
					else
						(PrintItIs)
					)
				else
					(PrintNotCloseEnough)
				)	
		)
		(if (Said 'open>')
			(if (Said '/door')
				(if (and (and 
					(<= (gEgo distanceTo: cabinet) 20)
					(> (gEgo y?) (cabinet y?))) 
					(< (gEgo x?) 140) )
					(cabinetScript changeState: 1)
				else
					(if (not doorOpen)
						(if (< (gEgo distanceTo: door) 20)
							(if g43DoorOiled 
								(doorScript changeState: 8)
							else
								(doorScript changeState: 1)
							)
						else
							(PrintNotCloseEnough)
						)
					else
						(Print "It's already open.")
					)
				)
			)
			(if (Said '/cabinet,dresser,closet')
				(if (< (gEgo distanceTo: cabinet) 20)
					(if (not cabinetOpen)
						(cabinetScript changeState: 1)
					else
						(PrintItIs)
					)
				else
					(PrintNotCloseEnough)
				)	
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
		(if (or (Said 'use,put/oil/door,hinges')
			(Said 'oil/hinges, door'))
			(if (gEgo has: 9) ; oil can
				(if (< (gEgo distanceTo: door) 20)
					(if (not g43DoorOiled)					
						(= g43DoorOiled 1)
						(-- gOil)
						((gInv at: 9) count: gOil)
						(doorScript changeState: 1)
					else
						(Print 43 12)
					)
				else
					(PrintNotCloseEnough)
				)	
			else
				(PrintOther 43 7)
			)	
		)
		(if (Said 'touch/chair')
			(PrintOther 44 27)			
		)
		(if(Said 'move/bookcase')
			(PrintOther 42 25)
		)
		(if (Said 'use/oil')
			(if (gEgo has: 9) ; oil can
				(PrintOther 43 27)	
			else
				(PrintDontHaveIt)
			)		
		)
		(if (Said 'take>')
			(if (Said '/oil,can,container')
				(if (< (gEgo distanceTo: cabinet) 20)
					(if cabinetOpen
						(if (not (gEgo has: 9))
							(PrintOther 43 14)
							(gEgo get: 9)
							;(++ gOil)
							((gInv at: 9) count: gOil)
							(cabinet loop: 1)
							
							(gGame changeScore: 1)
						else
							(PrintOther 43 11) ; Besides the residual smell of olive oil, there is nothing left of note in this cabinet.
						)
					else
						(PrintOther 43 15)	; You can't do that while the cabinet is closed.
					)
				else
					(PrintNotCloseEnough)
				)
			)
		)
		(if (or (Said 'use/key')(Said 'unlock/cabinet,lock'))
			(if (< (gEgo distanceTo: cabinet) 20)
				(if (not cabinetOpen)
					(cabinetScript changeState: 1)
				else
					(PrintItIs)
				)
			else
				(PrintNotCloseEnough)
			)	
		)				
	)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0 ; Handle state changes
			)
			(1	; stepping on a loose board
				(ProgramControl)
				(screech show: posn: (gEgo x?)(- (gEgo y?) 40) cel: 0 setCycle: End self)		
			)
			(2 (= cycles 10) ; short pause for effect
				
			)
			(3
				(screech hide:)
				(PrintOther 43 16)
				(wakeUpScript changeState: 1)	
			)
			(4	; chair falling
				(chair2 setCycle: End self cycleSpeed: 2)
				(ProgramControl)
			)
			(5 (= cycles 5)
				(ShakeScreen 1)
				;(vase1 loop: 10)
					
			)
			(6
				(wakeUpScript changeState: 1)
				(gEgo loop: 2)	
			)
		)
	)
)

(instance cabinetScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0
			)
			(1	; walk to cabinet
				(ProgramControl)
				(gEgo setMotion: MoveTo (cabinet x?) (+ (cabinet y?) 2) self)	
			)
			(2	(= cycles 2) ; fix direction
				(gEgo loop: 3)
			)
			(3	; opening the cabinet
				(if (or (gEgo has: 10) (== (IsOwnedBy 10 43) TRUE))	; key for cabinet
					(if (or (gEgo has: 9) (== (IsOwnedBy 9 205) TRUE))	; oil
						(cabinet loop: 1 setCycle: End self)
					else
						(if (gEgo has: 10)
							(PrintOther 43 2)
							(gEgo put: 10 43)
						)
						(cabinet loop: 2 setCycle: End self)	
					)
				else
					(PrintOther 43 9) ;The lock on the cabinet refuses to budge. Looks like you'll need a key.
					(PlayerControl)	
				)
			)
			(4	(= cycles 2)
				(if (or (gEgo has: 9) (== (IsOwnedBy 9 205) TRUE))
					(PrintOther 43 11) ; Besides the residual smell of olive oil, there is nothing left of note in this cabinet.
				else
					(PrintOther 43 8) ; Inside the cabinet you notice a small stain on the wood around a canister. Giving it a quick smell, you think the can contains a bit of olive oil.		
				)
			)
			(5
				(= cabinetOpen 1)
				(PlayerControl)	
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
			(1	; walk to the door
				(ProgramControl)
				(gEgo setMotion: MoveTo 181 85 self ignoreControl: ctlWHITE)	
			)
			(2	(= cycles 3) ; turn to face proper direction
				(gEgo loop: 3)	
			)
			(3	; check to see if door is oiled (goto 6 if true)
				(gEgo loop: 3)
				(if g43DoorOiled
					(self changeState: 6)
				else	
					(door cel: 1)
					(screech show: posn: 169 40 cel: 0 setCycle: End self)	
				)
			)
			(4	(= cycles 2)
				(PrintOther 43 10) ; "The door hinges are too squeaky. You don't want to risk waking that creature.")	
			)
			(5
				(door cel: 0)
				(screech hide:)
				
				(PlayerControl)	
			)
			(6	; open the door
				(PrintOther 43 6) ; You pour a generous stream of pungeant olive oil on the old hinges. They are now whisper quiet.
				(door setCycle: End self cycleSpeed: 2)
				(= doorOpen 1)
				(gEgo ignoreControl: ctlYELLOW)
				(gGame changeScore: 1)	
			)
			(7
				(PrintOther 43 13)
				(PlayerControl)	
			)
			(8 ; walk to the door
				(ProgramControl)
				(gEgo setMotion: MoveTo 181 82 self ignoreControl: ctlWHITE)				
			)
			(9
				(door setCycle: End self cycleSpeed: 2)
				(= doorOpen 1)
			)
			(10
				(PlayerControl)		
			)
			(11
				(door setCycle: Beg self)
				(= doorClosing 1)	
			)
			(12
				(= doorOpen 0)
				(= doorClosing 0)	
			)
		)
	)
)
(instance wakeUpScript of Script
	(properties)
	
	(method (changeState newState dyingScript)
		(= state newState)
		(switch state
			(0
			)
			(1	
				(zzz loop: 3 cel: 0 setCycle: End self cycleSpeed: 2)	
			)
			(2	(= cycles 5)
				;(bed cel: 1)
				(verlorn cel: 1)
			)
			(3	; jumps out of bed
				(verlorn loop: 6 cel: 0 setCycle: End self cycleSpeed: 2)
				(gEgo hide:)
				(actionEgo show: posn: (gEgo x?)(gEgo y?) view: 382 loop: 2 cel: 0 setCycle: End cycleSpeed: 2)
			)
			(4	; prepares to throw dagger
				(verlorn loop: 7 cel: 0 setCycle: End self)	
			)
			(5	; THROWS dagger
				(verlorn loop: 8)
				(dagger show: posn: (- (verlorn x?) 10)(- (verlorn y?) 10) yStep: 8 xStep: 8 setMotion: MoveTo (+ (gEgo x?) 1) (- (gEgo y?) 12) self)
			)
			(6
				(dagger hide:)
				(actionEgo posn: (gEgo x?)(gEgo y?) view: 382 loop: 0 cel: 0 setCycle: End self cycleSpeed: 2)
				
			)
			(7
				(ShakeScreen 1)
				(if (not [gDeaths 8])
					(++ gUniqueDeaths)
				)
				(++ [gDeaths 8])
				
				(= gDeathIconTop 1)
				
				(if tooClose
					(= dyingScript (ScriptID DYING_SCRIPT))
					(dyingScript
						caller: 700
						register:
							{\nYou got too close to that creature and woke him up! Best keep a distance, armed or not.}
					)	
				else
					(= dyingScript (ScriptID DYING_SCRIPT))
					(dyingScript
						caller: 700
						register:
							{\nYou woke the poor creature from its much needed beauty sleep. The good news is it'll be easier to be quiet now that you're dead!}
					)
				(gGame setScript: dyingScript)
				)
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
				;(ProgramControl)	
			)
			(4
				(wakeUpScript changeState: 1)
				;(Print "uh oh! That was loud.")	
			)
			(5
				(ProgramControl)
				(vaseThin1 setCycle: End self cycleSpeed: 2)
			)
			(6 (= cycles 5)
				(ShakeScreen 1)
				(vaseThin1 loop: 11)
				;(ProgramControl)	
			)
			(7
				(wakeUpScript changeState: 1)
				;(Print "uh oh! That was loud.")	
			)
			(8
				(ProgramControl)
				(vase2 setCycle: End self cycleSpeed: 2)
			)
			(9 (= cycles 5)
				(ShakeScreen 1)
				(vase2 loop: 10)
				;(ProgramControl)	
			)
			(10
				(wakeUpScript changeState: 1)
				;(Print "uh oh! That was loud.")	
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
(procedure (VaseFall view)
	(if (gEgo inRect: (- (view x?) 7) (- (view y?) 5) (+ (view x?) 12) (+ 3 (view y?) ))
		(if (== gEgoMovementType 2) ; sneaking
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

(instance actionEgo of Act
	(properties
		y 160
		x 40
		view 372
	)
)
(instance bed of Prop
	(properties
		y 174
		x 212
		view 17
		loop 1
	)
)
(instance bookcase of Act
	(properties
		y 127
		x 221
		view 46
	)
)
(instance cabinet of Prop
	(properties
		y 87
		x 127
		view 18
	)
)
(instance chair1 of Prop
	(properties
		y 170
		x 85
		view 18
		loop 3
	)
)
(instance chair2 of Prop
	(properties
		y 140
		x 210
		view 18
		loop 4
	)
)
(instance dagger of Act
	(properties
		y 160
		x 40
		view 79
	)
)
(instance door of Prop
	(properties
		y 80
		x 179
		view 52
		loop 2
	)
)
(instance screech of Prop
	(properties
		y 40
		x 169
		view 978
		loop 0
	)
)

(instance table of Prop
	(properties
		y 170
		x 115
		view 16
		loop 5
		cel 1
	)
)
(instance vase1 of Prop
	(properties
		y 110
		x 186
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
(instance verlorn of Prop
	(properties
		y 160
		x 207
		view 17
		loop 5
	)
)
(instance zzz of Prop
	(properties
		y 140
		x 214
		view 17
		loop 4
	)
)
