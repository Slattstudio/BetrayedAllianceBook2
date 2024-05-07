;;; Sierra Script 1.0 - (do not remove this comment)
; Score +1
(script# 257)
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
	
	rm257 0
	
)

(local
	
	mysteryMusic = 0
	bearAwake = 0
	birdGone = 0
	birdAnimation = 0
	entering = 0
	countdown = -60
	comeOrGo = 0 ; variable for when Ego is looking from other room to either enter the room or to go back to the vantage point room
	myEvent
	hidingBehindRockCounter = 0
	
	noTriggerLeaves = 0
	
	honeyDown = 0	; used to save ego while > 1, counts up then returns to zero to give ego a few extra moments
	
	placingHoney = 0	; used to make sure ego doesn't die while putting down the honey
		
	[mushStr 30]
)


(instance rm257 of Rm
	(properties
		picture scriptNumber
		north 0
		east 259
		south 235
		west 81
	)
	
	(method (init)
		(super init:)
		
		;(= [gWhereBuddy 0] 100)
		;(= [gWhereBuddy 1] 150)
		
		(self setScript: RoomScript setRegions: 205)
		
		(SetUpEgo)
		(gEgo init:)
		
		(switch gPreviousRoomNumber
			(81 
				(gEgo posn: 25 139 loop: 0)
			)
			(235 
				(gEgo posn: 160 184 loop: 3)
			)
			(259 
				(PlaceEgo 300 110 1)
				;(gEgo posn: 160 184 loop: 3)
			)
			(else 
				(gEgo posn: 160 184 loop: 3)
			)
		)
		
		(if (== gEgoMovementType 1)
			(= gEgoMovementType 0)
		)
		(RunningCheck)
		
		(bush init: setScript: bearScript)
		(squawk init: hide: setPri: 15 ignoreActors:)
		(sticky init: hide: ignoreActors: setPri: 0)
		(actionEgo init: hide: ignoreActors:)
		
		(if (not g257Mushroom)
			(mushroom init:)
		)
		
		(if (== g303egoHealed 3)
			(bear init: hide:)
			(bird init: hide:)
			(= birdGone 1)
		else
			(bear init: ignoreActors:)
			(bird init: cycleSpeed: 2 setScript: birdScript ignoreActors: ignoreControl: ctlWHITE xStep: 4 yStep: 4)
		)
		
		(birdScript changeState: 1)
		
		
		(if gLookingAhead
			(= entering 1)
			(enterBackFrame init: ignoreActors: setPri: 15)
			(enterButton init: ignoreActors: setPri: 15)
			(backButton init: ignoreActors: setPri: 15)
			
			(actionEgo init: show: loop: 3 setCycle: Fwd cycleSpeed: 1 z: -60)
			(gEgo hide:)
			(= gMap 1)
			(= gArcStl 1)
			
			(birdScript changeState: 6)
		else		
			(if (not g257Entering)
				(actionEgo init: show: loop: 3 setCycle: Fwd cycleSpeed: 1 z: -60)
				(gEgo hide:)
				(RoomScript changeState: 1)	
			)
		)
		
		(if (not gKneeHealed)	; secret area
			(gEgo observeControl: ctlRED)
		)
		
		;(if gKneeHealed
		;	(if (not gSeparated)
		;		(= gSwitchedRoomNumber gPreviousRoomNumber)
		;	)
		;)
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
		
		(if gAnotherEgo
			(if (& (gEgo onControl:) ctlMAROON) ; alert bird
				(if (not birdGone)
					(if (not placingHoney)
						(if (not honeyDown)
							(birdScript changeState: 11) ; cue death!
							(= birdGone 1)
						)
					)
				)	
			)
			(if (& (gEgo onControl:) ctlFUCHSIA) ; awaken bear
				(if (not bearAwake)
					;(PrintOK)
					;(= bearAwake 1)	
				)	
			)
			(if (& (gEgo onControl:) ctlCYAN) ; death
				(if (not noTriggerLeaves)
					(if (not bearAwake)
						(if (< gEgoMovementType 2) ; if not sneaking
							;(PrintOK)
							(= bearAwake 1)	
							(bearScript changeState: 1)
							; death 
							;(= bearAwake 1)
						)	
					)
				)	
			)
			(if (& (gEgo onControl:) ctlTEAL) ; death
				(if (not noTriggerLeaves)
					(if (not bearAwake)
					;	(if (not gEgoSneak)
							(= bearAwake 1)
							(bearScript changeState: 1)	
							; death 
					)
				)		
			)
			
			(if (& (gEgo onControl:) ctlGREY)
				(if (not birdGone)
					(if (== hidingBehindRockCounter 180)
						(birdScript cycles: 0 changeState: 8)
						(++ hidingBehindRockCounter)
					else
						(++ hidingBehindRockCounter)
					)	
				)
					
			else
				(= hidingBehindRockCounter 0)
			)
			(if (== (gEgo onControl:) ctlGREY)
				(if bearAwake
					(if (not honeyDown)
						(RoomScript changeState: 16)
						(= bearAwake 0)
						(= noTriggerLeaves 1)
						(bearScript cycles: 0 changeState: 0)
					)
				)
			)
		)
		
		
		; Hidden Bush
		(if (< (gEgo x?) 10)
		;(if (& (gEgo onControl:) ctlSILVER)
			(bush cel: 1)
			(if (not mysteryMusic)
				(gTheSoundFX number: 4 play:)
				(= mysteryMusic 1)
				(gEgo setMotion: NULL)
			)
		else
			(bush cel: 0)
			(= mysteryMusic 0)
		)
		
		(if (> honeyDown 0)
			(++ honeyDown)
			(switch honeyDown
				(50
					(sticky cel: 1)
				)
				(80
					(sticky cel: 2)
				)
				(110
					(sticky cel: 3)		
				)
				(135
					(sticky cel: 4)
				)
				(136
					
					(bear loop: 1 cel: 0 setCycle: CT)					
				)
				(155
					(bearScript changeState: 3)
					(= honeyDown 0)	
				)
			)
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
					(if (not g257Entering)
						(RoomScript changeState: 1)
					else	
						(actionEgo posn: (actionEgo x?) 185)
						(RoomScript changeState: 8)	
					)
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
		)
		(if gLookingAhead
			(if (== (pEvent type?) evJOYSTICK)
				(if (== (pEvent message?) 5) 
					(gRoom newRoom: 235)	
				)
			)
		)
	
		
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if (& (pEvent modifiers?) emRIGHT_BUTTON)
				(if (or (or (== (OnControl ocSPECIAL (pEvent x?) (pEvent y?)) ctlFUCHSIA)
					(== (OnControl ocSPECIAL (pEvent x?) (pEvent y?)) ctlCYAN))
					(== (OnControl ocSPECIAL (pEvent x?) (pEvent y?)) ctlTEAL))
					(PrintOther 257 22)	
				)
				(if (== (OnControl ocPRIORITY (pEvent x?) (pEvent y?)) ctlBLUE ) ; TREE
					(PrintOther 257 18)
				else
					(if
						(checkEvent pEvent
							(bear nsLeft?)
							(bear nsRight?)
							(bear nsTop?)
							(bear nsBottom?)
						)
						(if g303egoHealed
						else
							(if bearAwake
								(PrintOther 257 2)
							else
								(PrintOther 257 3)
							)
						)
					)
				)
				(if (== (OnControl ocPRIORITY (pEvent x?) (pEvent y?)) ctlRED) ; ROCK
					(PrintOther 257 19)
				)
				(if
					(checkEvent pEvent
						(bird nsLeft?)
						(bird nsRight?)
						(bird nsTop?)
						(bird nsBottom?)
					)
					(if birdGone
					else
						(if entering
							(PrintOther 257 0)
						else
							(PrintOther 257 1)
						)
					)
				else
				(if	; stump
						(checkEvent pEvent 165 200  39 59 
						)
						(PrintOther 257 13)
					)
				)
				
				(if
					(checkEvent pEvent
						(mushroom nsLeft?)
						(mushroom nsRight?)
						(mushroom nsTop?)
						(mushroom nsBottom?)
					)
					(if g257Mushroom
						;
					else
						(PrintOther 257 4)
					)
				else
					(if	; log
						(checkEvent pEvent 218 268  50 65 
						)
						(if g257Mushroom
							(PrintOther 257 11)	
						else
							(PrintOther 257 12)
						)
					)
				)							
			)
			(if (== comeOrGo 1)
				(if (not g257Entering)
					(RoomScript changeState: 1)
				else	
					(actionEgo posn: (actionEgo x?) 185 )
					(RoomScript changeState: 8)	
				)
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
		
		(if (Said 'talk/bear')
			(if (not (== g303egoHealed 3))
				(if bearAwake
					(PrintOther 257 31)	
				else
					(PrintOther 257 32)	
				)	
			else
				(PrintOther 257 33); no bear
			)	
		)
		
		(if (Said 'look>')
			(if (Said '/bear')
				(if g303egoHealed
					(PrintOther 257 17)
				else
					(if bearAwake
						(PrintOther 257 2)
					else
						(PrintOther 257 3)
					)
				)
			)
			(if (Said '/bird')
				(if g303egoHealed
					(PrintOther 257 7)	
				else
					(if entering
						(PrintOther 257 0)
					else
						(if birdGone
							(PrintOther 257 7)
						else
							(PrintOther 257 1)
						)
					)
				)
			)
			;(if (Said '/mushroom')
			;	(if g257Mushroom
			;		(PrintOther 257 6)	
			;	else
			;		(PrintOther 257 4)
			;	)
			;)
			(if (Said '/mushroom')
				(if g257Mushroom
					(if (gEgo has: 5)
						(Format @mushStr "This rare mushroom is said to have curative powers. You have %d." gMushrooms)
						(Print @mushStr #icon 614 )
					else
						(Print "There's no mushroom here to see.")
					)		
				else
					(PrintOther 257 4)
				)	
			)
			(if (Said '/leaves,ground')
				(PrintOther 257 8)
			)
			(if (Said '/log')
				(if g257Mushroom
					(PrintOther 257 11)	
				else
					(PrintOther 257 12)
				)
			)
			(if (Said '/stump')
				(PrintOther 257 13)
			)
			(if (Said '/tree')
				(PrintOther 257 18)
			)
			(if (Said '/rock')
				(PrintOther 257 19)
			)
			(if (Said '/forest,verlorn')
				(PrintOther 0 105)	
			)
			(if (Said '[/!*]')	; this will handle just "look" by itself
				(PrintOther 257 14)
				(if (not g303egoHealed)
					(PrintOther 257 15)		
				)
			)
			
			;(if (Said '/*')	; handles look "something"
			;	(PrintOther 457 15)
			;)
		)
		
		(if(Said 'take,(pick<up)/mushroom')
			(if (<= (gEgo distanceTo: mushroom) 30)
				(if g257Mushroom
					(PrintOther 257 6)
				else				
					(PrintOther 257 5)
					(gEgo get: 5)	
				
					(= gMushrooms (+ gMushrooms 1))
					((gInv at: 5) count: gMushrooms)
					(= g257Mushroom 1)
					(mushroom hide:)
					
					(gGame changeScore: 1)
				)
			else
				(PrintNotCloseEnough)
			)
		)
		(if(Said 'run')
			(if (or g303egoHealed bearAwake)	 
				(if (not (== gEgoMovementType 1)) ; if not running
					(if gAnotherEgo
						(= gEgoView 351)	
					else
						(= gEgoView 230)
					)
					(gEgo xStep: 5 yStep: 4)
					(= gEgoMovementType 1)
					;(= gEgoSneak 0)
					(= [gArray 0] (pEvent x?))
					(= [gArray 1] (pEvent y?))
					(runProc)
				else
					(Print 0 39)
				)
			else
				(Print "That is not advisable at this time.")	
			)	
		)
		
		(if (or (Said 'use, pour, break, give, drop, throw/honey, fruit[/bear]') 
			(Said 'feed/bear')
			(Said 'put/honey,fruit/ground'))
			(if gHoneyNum ; if has any number of Honey Fruit
				(if bearAwake
					(if (> (gEgo x?) 128)
						(PrintOK)
						(RoomScript changeState: 11)
						(= placingHoney 1)
						;(gEgo ignoreControl: ctlMAROON)
						;(if (not birdGone)
						(birdScript changeState: 9)
						;)
					else
						(PrintNotCloseEnough)
					)
				else
					(PrintOther 257 21)
					(PrintOther 257 28)
				)
			else
				(Print "You don't have any honey fruit.")
			)	
		)
		(if (Said 'use, release,throw, drop/bug,insect')
			(if gBugs
				(PrintOther 257 23)
			else
				(PrintDontHaveIt)
			)
		)
		(if (Said 'jump/leaves')
			(PrintOther 257 27)	
		)
		(if (Said 'step<over/leaves')
			(Print "Just do it!")	
		)  

		(if (or (Said 'hide')
				(Said 'take/cover'))
			(PrintOther 257 20)		
		)
		;(if (Said 'hi')
		;	(FormatPrint "%u" honeyDown)
		;	(++ gHoneyNum)	
		;)
	)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0 ; Handle state changes
			)
				; entering the room for the first time
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
			(3	(= cycles 6); stepping on leaves bear awakens
				(Print 257 10 #font 9) ; CRUNCH!
				(actionEgo posn: (- (actionEgo x?) 15)(actionEgo y?) view: 339 loop: 0 setCycle: CT)
				(bear setCycle: End cycleSpeed: 3)
					
			)
			(4	; Ego dashed behind rock
				(PrintOther 257 9) ; The leaves crackle beheath your clumsy step alerting the local wildlife to your presence.
				(actionEgo setCycle: End self cycleSpeed: 3)		
			)	
			(5	(= cycles 26)	; breathing in relief
				(actionEgo loop: 1 setCycle: Fwd cycleSpeed: 5)
				(bear setCycle: Beg)
			)
			(6	; wiping brow
				(actionEgo view: 454 posn: (- (actionEgo x?) 15) (actionEgo y?) loop: 0 cel: 0 setCycle: End self cycleSpeed: 2)	
			)
			(7	; return to "normal"
				(actionEgo hide:)
				(gEgo show: posn: (actionEgo x?) (actionEgo y?) loop: 2)
				(= g257Entering 1)
				(PlayerControl)
				(= gMap 0)
				(= gArcStl 0)
				(TheMenuBar state: ENABLED)
				
				(if gYellowTips
					(= gWndColor 0)
					(= gWndBack 14)
					(Print 257 25 #font 4 #button "Ok")
					(= gWndColor 0)
					(= gWndBack 15)
				)
				
			)
			(8 (= cycles 1)
				(ProgramControl)
				(enteringProc)
						
			)
			(9
				(if	(not (== countdown 0))
					(self changeState: 8)	
				else
					(self changeState: 10)
				)
			)
			(10	;(= cycles 6); stepping on leaves bear awakens
				;(Print 257 1 #font 9) ; CRUNCH!
				(actionEgo hide:)
				(gEgo show: posn: (actionEgo x?)(actionEgo y?))
				(PlayerControl)	
				
				(= gMap 0)
				(= gArcStl 0)
				(TheMenuBar state: ENABLED)				
			)
			; appease bear
			(11
				(ProgramControl)
				(= gMap 0)
				(birdScript cycles: 0)
				(bearScript cycles: 0)
				(= gEgoMovementType 0)
				(RunningCheck)
				(gEgo setMotion: MoveTo 185 95 self)
			)
			(12	; walk infront of bear
				(gEgo hide:)
				(actionEgo show: view: 450 loop: 1 posn: (gEgo x?)(gEgo y?) show: cel: 0 setCycle: End self cycleSpeed: 2)		
			)
			(13 (= cycles 3)
				(PrintOther 257 24)
			)
			(14
				(sticky show: cel: 0)
				(actionEgo setCycle: Beg self)
				(-- gHoneyNum)
				((gInv at: 2) count: gHoneyNum)
				(if (== gHoneyNum 0)
					(gEgo put: 2)
				)
			)
			(15
				(= honeyDown 1)
				(= placingHoney 0)
				(= honeyDown 1)
				(actionEgo hide:)
				(gEgo show: observeControl: ctlWHITE)
				(bear loop: 2 setCycle: Fwd cycleSpeed: 3)
				(PlayerControl)
			)
			(16	; hiding from bear
				(ProgramControl)
				(if (> (gEgo y?) 160)
					(gEgo setMotion: MoveTo 106 172 self)	; behind the rock	
				else
					(gEgo setMotion: MoveTo 92 138 self)	; behind the tree
				)
			)
			(17	(= cycles 20)
				(gEgo hide:)
				(actionEgo show: view: 339 loop: 1 cel: 0 posn: (+ (gEgo x?) 15) (gEgo y?) setCycle: Fwd self cycleSpeed: 3)
				(bear setCycle: Beg)	
			)
			(18
				(actionEgo view: 454 posn: (- (actionEgo x?) 14) (+ (actionEgo y?) 1)  loop: 0 cel: 0 setCycle: End self cycleSpeed: 2)	
			)
			(19	(= cycles 2)
				(PlayerControl)
				(gEgo show: loop: 2)
				(actionEgo hide:)
			)
			(20
				(PrintOther 257 29)	
				(= noTriggerLeaves 0)
			)
		)
	)
)

(instance bearScript of Script
	(properties)
	
	(method (changeState newState dyingScript)
		(= state newState)
		(switch state
			(0
			)
			(1
				(ProgramControl)
				(Print 257 10 #font 9) ; CRUNCH!
				(PrintOther 257 30)
				(bear setCycle: End self cycleSpeed: 3)	
			)
			(2	(= cycles 30)
				(PlayerControl)
				;(= gMap 1)
			)	
			(3 
				(ProgramControl)
				(bear loop: 1 cel: 0 setCycle: End self)
			)	
			(4
				(++ [gDeaths 2])
				(= gDeathIconTop 1)
				(= dyingScript (ScriptID DYING_SCRIPT))
				(dyingScript
					caller: 606
					register:
						{\nYou woke up the sleeping bear with your careless steps. You're certainly in no shape to tussle with an awake bear, but perhaps there's another way.}
				)
				(gGame setScript: dyingScript)		
			)
		)
	)
)

(instance birdScript of Script
	(properties)
	
	(method (changeState newState dyingScript)
		(= state newState)
		(switch state
			(0
			)
			(1
				(bird loop: 4 cel: 0 setCycle: End self)
			)
			(2 (= cycles (Random 30 60))
			)
			(3
				(= birdAnimation (Random 0 10))
				(if (> birdAnimation 4)
					(birdScript changeState: 4)		
				else
					(birdScript changeState: 1)	
				)
			)
			(4 (= cycles 10)
				(bird loop: 2 cel: 0 setCycle: Fwd)	
			)
			(5
				(bird setCycle: CT)
				(birdScript changeState: 2)	
			)
			(6
				(bird loop: 4 cel: 0 setCycle: End self)
			)
			(7	(= cycles (Random 40 70))
			)
			(8
				(if (or gLookingAhead (== (gEgo onControl:) ctlGREY))
					(self cue:)
				else
					(self changeState: 1)
				)	
			)
			(9
				(bird view: 301 setCycle: Walk setMotion: MoveTo 1 20 self)	
			)
			(10
				(bird hide:)
				(= birdGone 1)	
			)
			(11 ;(= cycles 16)
				(ProgramControl)
				(self cycles: 0)
				(bird loop: 1 cel: 0 setCycle: End) ;SQWUACK
				(squawk show: setCycle: End self cycleSpeed: 2)	
				(= bearAwake 1)
			)
			(12	(= cycles 3)
				
			)
			(13
				(squawk hide:)
				(Print "Uh oh! The bird's sure to wake the beast.")
				(bear setCycle: End self cycleSpeed: 3)	
			)
			(14	(= cycles 15)
				(PlayerControl)
				(= gMap 1)
			)	
			(15 
				(ProgramControl)
				(bear loop: 1 cel: 0 setCycle: End self)
			)	
			(16
				(++ [gDeaths 2])
				(= gDeathIconTop 1)
				(= dyingScript (ScriptID DYING_SCRIPT))
				(dyingScript
					caller: 606
					register:
						{\nThat foul fowl! You've heard of bird watching, but this one's watching you! Perhaps if you stay hidden it would get bored and leave.}
				)
				(gGame setScript: dyingScript)		
			)
				
		)
	)
)

(procedure (enteringProc)
    (= countdown (+ countdown 2))
    (actionEgo z: countdown)
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

(instance actionEgo of Act
	(properties
		y 180
		x 153
		view 343
	)
)
(instance bird of Act
	(properties
		y 41
		x 182
		view 303
	)
)
(instance bush of Prop
	(properties
		y 155
		x -16
		view 805
	)
)
(instance mushroom of Prop
	(properties
		y 52
		x 243
		view 002
		loop 1
	)
)
(instance bear of Prop
	(properties
		y 90
		x 125
		view 349
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
(instance squawk of Prop
	(properties
		y 35
		x 170
		view 978
		loop 1
	)
)
(instance sticky of Prop
	(properties
		y 92
		x 165
		view 63 ; honey on ground
		loop 3
	)
)