;;; Sierra Script 1.0 - (do not remove this comment)
; Score + 1
(script# 233)
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
	rm233 0
)

(local
	
	fallingToDeath = 0
	eaten = 0
	rockState = 0
	wurmState = 1 ; True if Wurm is around, 0 if Wurm has been sent away
	
	entering = 0
	countdown = -50
	comeOrGo = 0 ; variable for when Ego is looking from other room to either enter the room or to go back to the vantage point room
	myEvent
	
	textPlace = 10
	[mushStr 100]
	
)


(instance rm233 of Rm
	(properties
		picture scriptNumber
		north 235
		east 0
		south 0
		west 0
	)
	
	(method (init)
		(super init:)
		
		;(= [gWhereBuddy 0] 50)
		;(= [gWhereBuddy 1] 50)
		
		(self setScript: RoomScript setRegions: 205)
		(switch gPreviousRoomNumber
			(235
				(gEgo posn: 150 40 loop: 2)	
			)
			(else 
				(gEgo posn: 150 40 loop: 1)
			)
		)
		(SetUpEgo)
		(gEgo init: setPri: 2)
		(RunningCheck)
		
		(mushroom init: ignoreActors: setScript: enterScript)
		(if g233Mushroom
			(mushroom hide:)	
		)
		(actionEgo init: ignoreControl: ctlWHITE ignoreActors: hide: setPri: 2)
		
		(wurm init: ignoreActors: setCycle: Fwd cycleSpeed: 2 setPri: 2 setScript: wurmScript)
		(if (or (> g233Wurm 0) (== gAnotherEgo 0)) 
			(wurm hide:)
			(= eaten 1)	
		)
		
		(rock init: ignoreControl: ctlWHITE yStep: 5 setScript: rockScript setPri: 2)
		(fruit init: hide: ignoreControl: ctlWHITE yStep: 5 setScript: fruitScript setPri: 15)
		
		(if gLookingAhead
			(= entering 1)
			(enterBackFrame init: ignoreActors: setPri: 15)
			(enterButton init: ignoreActors: setPri: 15)
			(backButton init: ignoreActors: setPri: 15)
			
			(actionEgo init: show: view: 343 posn: 155 20 loop: 2 setCycle: Fwd cycleSpeed: 1 z: -50 setPri: 0)
			(gEgo hide:)
			(= gMap 1)
			(= gArcStl 1)
		)
	)
)

(instance RoomScript of Script
	(properties)
	
	(method (doit)
		(super doit:)
		; code executed each game cycle
		
		(if gLookingAhead
			(= myEvent (Event new: evNULL))
			(if
					(checkEvent
						myEvent
						(backButton nsLeft?)
						(backButton nsRight?)
						(+ (backButton nsTop?) 7)
						(+ (backButton nsBottom?) 7)
					)
					(= comeOrGo 2)
					(backButton cel: 1)
					(enterButton cel: 0)
					
					
			else
				(if
					(checkEvent
						myEvent
						(enterButton nsLeft?)
						(enterButton nsRight?)
						(+ (enterButton nsTop?) 7)
						(+ (enterButton nsBottom?) 7)
					)
					(= comeOrGo 1)
					(backButton cel: 0)
					(enterButton cel: 1)
				else
					(= comeOrGo 0)
					(backButton cel: 0)
					(enterButton cel: 0)
				)
			)
			(myEvent dispose:)
		)
		
		(if (not eaten)
			(if (<= (gEgo distanceTo: wurm) 33) ; Ego close to wurm - visual cue that you're too close
				(wurm view: 68)
				(if (<= (gEgo distanceTo: wurm) 14)	; Cue Death			
					(wurmScript changeState: 9); eatscript
				)		
				
			else
				(wurm loop: 2)
			)
			(if (== rockState 2)
				(if (<= (rock distanceTo: wurm) 15)
					(rockScript changeState: 7)
					(= eaten 1)
				)	
			)
		)
		
		(if (& (gEgo onControl:) ctlBROWN)
			(gRoom newRoom: 235)
		)
		
		(if (& (gEgo onControl:) ctlYELLOW) ; At the bottom of the Screen
			(if (== fallingToDeath 0)
				(RoomScript changeState: 1)
				(= fallingToDeath 1)
			)
		)
		
		(if (& (gEgo onControl:) ctlBLUE) ; On either edge of the mud - pushing ego inward to the mud
			(if (> (gEgo x?) 150) ; ego on the right side of screen
				(gEgo posn: (- (gEgo x?) 1) (gEgo y?)) ; push Ego Left
			else
				(gEgo posn: (+ (gEgo x?) 1) (gEgo y?)) ; push Ego Right
			)
		)
		(if (& (gEgo onControl:) ctlFUCHSIA) ;On right side of platform
			(gEgo posn: (- (gEgo x?) 3) (gEgo y?)) ; push Ego Left
		)
		
		(if (& (gEgo onControl:) ctlRED)
			; death
			(if (== fallingToDeath 0)
				(RoomScript changeState: 1)
				(= fallingToDeath 1)
			)
			;(gEgo posn: (gEgo x?) (+ (gEgo y?) 3))
		else
			(if (& (gEgo onControl:) ctlMAROON)
				(gEgo posn: (gEgo x?) (+ (gEgo y?) 3))
				(if (and (> g233Mushroom 0)(< g233Mushroom 10))
					(if (not (== gEgoMovementType 1))
						(++ g233Mushroom)
						(if (== g233Mushroom 10)
							(if gYellowTips
								(= gWndColor 0)
								(= gWndBack 14)
								(Print 233 10 #font 4 #at -1 40 #button "Ok")
								(= gWndColor 0)
								(= gWndBack 15)
								;(= g233Mushroom 2)	
							)
						)
					)	
				)
			else
				(if (& (gEgo onControl:) ctlGREY)
					(gEgo posn: (gEgo x?) (+ (gEgo y?) 2))
				else
					(if (& (gEgo onControl:) ctlSILVER)
						(gEgo posn: (gEgo x?) (+ (gEgo y?) 1))
					)
				)
			)
		)
		
		; Dropping the Rock if on the mud
		(if (& (gEgo onControl:) ctlSILVER)
			(if (== rockState 1)	; if holding rock
				(= rockState 2)
				(rock show: posn: (gEgo x?)(gEgo y?) setMotion: MoveTo (gEgo x?) 220 ignoreActors:)
				(= gEgoView 343)
				(= gEgoStoppedView 1)
				(= gMovementLocked 0)
					
			)
		)
		
		(if (> (rock y?) 176)
			(rock hide:)	
		)
	)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		(if (== (pEvent type?) evKEYBOARD)
			(if gLookingAhead
				(if (== (pEvent message?) KEY_ESCAPE)
					(gRoom newRoom: 235)
				)
			)
		)
		
		(if gLookingAhead
			(if (== (pEvent type?) evKEYBOARD) 
				(if (or (== (pEvent message?) KEY_e) (== (pEvent message?) KEY_RETURN)) ; enter
					(enterScript changeState: 1)
					(= gLookingAhead 0)
					(= comeOrGo 0)
					(backButton hide:)
					(enterButton hide:)
					(enterBackFrame hide:)
				)
				(if (== (pEvent message?) KEY_b)	; back
					(gRoom newRoom: 235)	
				)
			)
			(if (== (pEvent type?) evJOYSTICK)
				(if (== (pEvent message?) 1) 
					(gRoom newRoom: 235)	
				)
			)
		)
		
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if (& (pEvent modifiers?) emRIGHT_BUTTON)
				
				; Looking
				(if
					(checkEvent
						pEvent
						(rock nsLeft?)
						(rock nsRight?)
						(rock nsTop?)
						(rock nsBottom?)
					)
					(if (not rockState)
						(PrintOther 233 2)
					)
				)
				(if
					(checkEvent pEvent
						(mushroom nsLeft?)
						(mushroom nsRight?)
						(mushroom nsTop?)
						(mushroom nsBottom?)
					)
					(if (not g233Mushroom)
						(PrintOther 233 1)
					)
				)
				(if
					(checkEvent pEvent
						(- (wurm nsLeft?) 3)
						(+ (wurm nsRight?) 3)
						(- (wurm nsTop?) 3)
						(+ (wurm nsBottom?) 3)
					)
					(if (not g233Wurm)
						(PrintOther 233 0)
					)
				)							
			)
			(if (== comeOrGo 1)
				(enterScript changeState: 1)
				(= gLookingAhead 0)
				(= comeOrGo 0)
				(backButton hide:)
				(enterButton hide:)
				(enterBackFrame hide:)
			)
			(if (== comeOrGo 2)
				(gRoom newRoom: 235)	
			)
			
			
		)
		; handle Said's, etc...
		(if (Said 'take,(pick<up)/mushroom')
			(if (not g233Mushroom)
				(if (<= (gEgo distanceTo: mushroom) 7)
					(PrintOK)
					(pickUpScript changeState: 11)
				else
					(PrintOther 233 3) ; You can't reach it from there.
				)
			else
				(Print 233 4) ; There are no mushrooms left to take.
			)
		)
		(if(Said 'take,move,(pick<up)/rock')
			(if gAnotherEgo
				(if(== rockState 0)
					(if (& (gEgo onControl:) ctlBLACK)
						(rockScript changeState: 1)					
					else
						(PrintNotCloseEnough)
					)
				else
					(if(== rockState 1)
						(PrintOther 233 8)
					)
					(if(== rockState 2)
						(PrintOther 233 7)
					)
					
				)
			else
				(Print "There's no need to do that now.")
			)
		)
		(if(Said 'throw,toss,drop/rock')
			(if(== rockState 1)
				(if (& (gEgo onControl:) ctlBLACK)
					(if g233Wurm
						(= rockState 2)
						(rock show: posn: (gEgo x?)(gEgo y?) setMotion: MoveTo (gEgo x?) 220 ignoreActors:)
						(= gEgoView 343)
						(= gEgoStoppedView 1)
					else	
						(= rockState 2)
						(PrintOther 233 11)
						(rockScript changeState: 5)
					)
				else
					(Print "You're not in a good position to do that.")					
				)
			else
				(Print "You don't have a rock.")		
			)	
		)
		;(if(Said 'get,(pick<up)/fruit')
		;	(++ gHoneyNum)	
		;)
		(if(Said 'throw,toss,drop/fruit,honey')
			(if(> gHoneyNum 0)
				(if g233Wurm
					(Print "There's no reason to do that now.")
				else	
					(if (& (gEgo onControl:) ctlBLACK)
						(-- gHoneyNum)
						((gInv at: 2) count: gHoneyNum)
						(if (== gHoneyNum 0)
							(gEgo put: 2)
						)
						(PrintOther 233 12)
						(fruitScript changeState: 1)
					else
						(Print "You're not in a good position to do that.")						
					)
				)
			else
				(Print "You don't have any.")	
			)	
		)
		
		(if (Said 'look>')
			(if (Said '/mud,hill')
				(PrintOther 233 5)
				(if (not g233Wurm)
					(PrintOther 233 0)		
				)	
			)
			(if (Said '/monster,creature')
				(if g233Wurm ; if creature not there
					(PrintOther 233 13)	
				else
					(PrintOther 233 0)
				)
			)
			(if (Said '/rock')
				(switch rockState
					(0
						(PrintOther 233 2)
					)
					(1
						(PrintOther 233 8)
					)
					(2
						(PrintOther 233 7)		
					)
				)
			)
			(if (Said '/mushroom')
				(if g233Mushroom
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
			(if (Said '/forest,verlorn')
				(PrintOther 0 105)	
			)
			(if (Said '[/!*]')	; this will handle just "look" by itself
				(PrintOther 233 5)
			)
			
			;(if (Said '/*')	; handles look "something"
			;	(PrintOther 233 6)
			;)
		)
	)
	
	(method (changeState newState dyingScript whoDied)
		(= state newState)
		(switch state
			(0 ; Handle state changes
			)
			(1 ;(= cycles 1)
				(ProgramControl)
				;(gEgo z: -10)
				(gEgo hide:)
				(if gAnotherEgo	; leah
					(actionEgo show: posn: (gEgo x?)(gEgo y?) view: 374 yStep: 7 setMotion: MoveTo (gEgo x?) 190 self)
				else
					(actionEgo show: posn: (gEgo x?)(gEgo y?) view: 229 yStep: 7 setMotion: MoveTo (gEgo x?) 190 self)	
				)	
			)
			(2 (= cycles 1)
				(actionEgo hide:)
				;(gEgo z: -20)	
			)
			(3 (= cycles 1)
				;(gEgo z: -30)	
			)
			(4 (= cycles 10)
				;(gEgo z: -45)	
			)
			(5 (= cycles 20)
				
			)
			(6
				(ShakeScreen 1)
				(gEgo hide:)
				(if (not [gDeaths 3])
					(++ gUniqueDeaths)
				)
				(++ [gDeaths 3])
				(= gDeathIconEnd 1)
				(if gAnotherEgo 
				
					(= dyingScript (ScriptID DYING_SCRIPT))
					(dyingScript
						caller: 749
						register:
							{\nThe slippery mud pulls you down the steep cliff. Maybe next time you'll get off on a better foot.}
					)
				else
					(= dyingScript (ScriptID DYING_SCRIPT))
					(dyingScript
						caller: 709
						register:
							{\nThe slippery mud pulls you down the steep cliff. Maybe next time you'll get off on a better foot.}
					)
				)
				(gGame setScript: dyingScript)
			)
		)
	)
)
(instance fruitScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0
			)
			(1	; walking to position
				(ProgramControl)
				(= eaten 1)
				(= g233Wurm 1)
				(gEgo setMotion: MoveTo 150 30 self ignoreControl: ctlWHITE )	
			)
			(2	; dropping the fruit
				(gEgo loop: 2)
				(fruit show: posn: 150 30 setMotion: MoveTo 150 (- (wurm y?) 15) self)
				;(wurmScript changeState: 12)	
			)
			(3	; eating the fruit
				(fruit hide:)
				(wurmScript changeState: 12 cycles: 0)
				(wurm view: 66 loop: 2 cel: 0 setCycle: End self cycleSpeed: 3)	
			)	; chewing the fruit
			(4 (= cycles 20)
				(wurm loop: 3 cel: 0 setCycle: Fwd cycleSpeed: 4)	
			)
			(5
				(wurm loop: 0 cel: 0 setCycle: End self)		
			)
			(6
				(PlayerControl)
				(= wurmState 0)
				(gEgo observeControl: ctlWHITE)	
			)
		)
	)
)
			
(instance rockScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)
			; Send Player to Rock
			(1
				(ProgramControl)
				(rock 
					ignoreActors:
				)
				(gEgo 
					setMotion: MoveTo (- (rock x?) 14) (rock y?) rockScript ignoreControl: ctlWHITE 
				)
			)
			; Bending over to up the Rock
			(2
				(gEgo hide:)
				(actionEgo 
					show: 
					posn: (gEgo x?)(gEgo y?) 
					view: 450
					loop: 0
					setCycle: End rockScript
					cycleSpeed: 4
				)	
			)
			; Picking up the Rock
			(3
				(rock
					hide:
				)
				(actionEgo
					posn: (+ (actionEgo x?) 2) (actionEgo y?) 
					loop: 2
					cel: 0
					setCycle: End rockScript
				)		
			)
			(4
				(PlayerControl)
				(= rockState 1)
				(= gEgoMovementType 0)
				(RunningCheck)
				(= gEgoView 451)
				(= gEgoStoppedView 452)
				(= gMovementLocked 1)
				(actionEgo hide:)
				(gEgo show: loop: 0 posn: (- (actionEgo x?) 5)(actionEgo y?) observeControl: ctlWHITE)
				(PrintOther 233 14) 
			)
			(5	; walking to position
				(ProgramControl)
				(= eaten 1)
				(gEgo setMotion: MoveTo 150 30 self ignoreControl: ctlWHITE )	
			)
			(6	; dropping the rock
				(= gEgoView 343)
				(= gEgoStoppedView 1)
				(RunningCheck)
				(gEgo loop: 2)
				(rock show: posn: 150 30 setMotion: MoveTo 150 (- (wurm y?) 15) self)
				;(wurmScript changeState: 12)	
			)
			(7	; eating rock
				(rock hide:)
				(wurmScript changeState: 12 cycles: 0)
				(wurm view: 66 loop: 5 cel: 0 setCycle: End self cycleSpeed: 3)
				(= g233Wurm 1)	
			)	; chewing rock
			(8 (= cycles 20)
				(wurm loop: 6 cel: 0 setCycle: Fwd cycleSpeed: 4)	
			)
			(9 ; teeth breaking
				(wurm loop: 7 cel: 0 setCycle: End self)	
			)
			(10 ; burrowing
				(wurm loop: 0 cel: 0 setCycle: End self)		
			)
			(11
				(PlayerControl)
				(= wurmState 0)
				;(= rockState 0)
				(= gMovementLocked 0)
				(gEgo observeControl: ctlWHITE)	
			)
		)
	)
)

(instance wurmScript of Script
	(properties)
	
	(method (changeState newState dyingScript)
		(= state newState)
		(switch state
			(0
				(wurm setMotion: MoveTo 155 105 self)
			)
			(1
				(wurm setMotion: MoveTo 160 110 self)
			)
			(2
				(wurm setMotion: MoveTo 155 115 self)
			)
			(3
				(wurm setMotion: MoveTo 150 120 self)
			)
			
			(4
				(wurm setMotion: MoveTo 145 115 self)
			)
			(5
				(wurm setMotion: MoveTo 140 110 self)
			)
			(6
				(wurm setMotion: MoveTo 145 105 self)
			)
			(7
				(wurm setMotion: MoveTo 150 100 self)
			)
			(8
				(self changeState: 0)	
			)
			
			(9
				(ProgramControl)
				(gEgo hide:)
				(wurm view: 66 loop: 1 cel: 0 setCycle: End self)	
				(= eaten 1)
				
			)
			(10 (= cycles 9)
				; a small time for effect	
			)
			(11
				;(if (not [gDeaths 6])
				;	(++ gUniqueDeaths)
				;)
				(++ [gDeaths 6])
				(= dyingScript (ScriptID DYING_SCRIPT))
				(dyingScript
					caller: 711
					register:
						{\nHungry things have to eat. But maybe next time try not letting the food be you!}
				)
				(gGame setScript: dyingScript)	
			)
			(12
				
			)
		)
	)
)

(instance pickUpScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0
				
			)
			(11
				;(= animation 1)
				(ProgramControl)
				(gEgo setMotion: MoveTo 150 153 self) ; walk to the left	
			)
			(12		; pick up animation
				(gEgo hide:)
				(actionEgo show: posn: 150 151 view: 450 loop: 0 cel: 0 setCycle: End self cycleSpeed: 3)	
			)
			(13		; hide mushroom and get back up
				(mushroom hide:)
				(actionEgo view: 450 setCycle: Beg self)
				(PrintOther 233 9)		
			)
			(14		; move back to position and put item in inventory
				(actionEgo hide:)
				(gEgo get: 5 show: setMotion: MoveTo 151 153 self)	
			)
			(15
				(= gMushrooms (+ gMushrooms 1))
				((gInv at: 5) count: gMushrooms)
				(= g233Mushroom 1)
				(PlayerControl)
				;(= animation 0)
				
				(gGame changeScore: 1)
			)
		)
	)
)
(instance enterScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0
			)
			(1 (= cycles 1)
				(ProgramControl)
				(enteringProc)	
			)
			(2
				(if	(not (== countdown 0))
					(self changeState: 1)	
				else
					(self changeState: 3)
				)
			)
			(3	
				(actionEgo loop: 3 setMotion: MoveTo (actionEgo x?)(+ (actionEgo y?)21) self setPri: 2)
					
			)
			(4
				(PlayerControl)
				(= gMap 0)
				(= gArcStl 0)
				(TheMenuBar state: ENABLED)
				
				(actionEgo hide:)
				(gEgo show: posn: (actionEgo x?)(actionEgo y?))	
			)
		)
	)
)

(procedure (PrintOther textRes textResIndex)
	(if (> (gEgo y?) 70)
		(= textPlace 10)
	else
		(= textPlace 140)
	)
	(Print textRes textResIndex
		#width 280
		#at -1 textPlace
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

(procedure (enteringProc)
    (= countdown (+ countdown 2))
    (actionEgo z: countdown)
)

(instance rock of Act
	(properties
		y 30
		x 248
		view 4
		loop 0
	)
)
(instance mushroom of Prop
	(properties
		y 155
		x 151
		view 002
		loop 1
	)
)
(instance wurm of Act
	(properties
		y 100
		x 150
		view 67
	)
)

(instance fruit of Act
	(properties
		y 30
		x 240
		view 64
	)
)
(instance actionEgo of Act
	(properties
		y 170
		x 160
		view 361
	)
)
(instance enterBackFrame of Prop
	(properties
		y 180
		x 280
		view 983
	)
)
(instance enterButton of Prop
	(properties
		y 158
		x 280
		view 983
		loop 1
	)
)
(instance backButton of Prop
	(properties
		y 178
		x 280
		view 983
		loop 2
	)
)