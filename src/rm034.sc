;;; Sierra Script 1.0 - (do not remove this comment)
; +1 score
(script# 34)
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
	
	rm034 0
	
)

(local
	
	awake = 0
	moving = 0
	hiding = 0  ; Bushes
				; 1, 2, 
			 	; 3, 4
			 	
	falling = 0 ; ego being knocked over by eyepod
	placingSticky = 0
	rando
	stickyDown = 0
	eyePodStuck = 0
	
	stickyStop = 0 ; used for when player is on the honey
	goingForEye = 0
	
	triedToGrabCounter = 0
	
	
)


(instance rm034 of Rm
	(properties
		picture scriptNumber
		north 248
		east 0
		south 0
		west 0
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript setRegions: 205)
		(switch gPreviousRoomNumber
			(248 
				;(gEgo posn: 150 60 loop: 2)
				(PlaceEgo 150 60 2)
			)
			(else 
				;(gEgo posn: 150 60 loop: 2)
				(PlaceEgo 150 60 2)
			)
		)
		(SetUpEgo)
		(RunningCheck)
		
		(gEgo init:)
		(eyePod init: xStep: 6 yStep: 4 ignoreActors: ignoreControl: ctlWHITE setScript: eyePodScript)
		
		(bushTopLeft init: ignoreActors:)
		(bushBottomLeft init: ignoreActors:)
		(bushTopRight init: ignoreActors:)
		(bushBottomRight init: ignoreActors:)
		
		(actionEgo init: hide: ignoreActors:)
		(sticky init: hide: ignoreActors: setScript: stickyScript setPri: 0)
		
		(switch g34EyePod
			(1
				(eyePod show:)
				(sticky show:)
				(= stickyDown 1)
				(= awake 1)	
				(= hiding 0)
				(eyePod posn: (sticky x?) (- (sticky y?) 3) setMotion: NULL view: 41 loop: 3 cel: 0 setCycle: Fwd cycleSpeed: 3)
				(= eyePodStuck 1)
				(= moving 0)		
			)
			(2 
				(eyePod hide:)
				(sticky show:)
				(= stickyDown 1)
				(= awake 1)	
				(= hiding 0)
			)
		)
	)
)

(instance RoomScript of Script
	(properties)
	
	(method (doit)
		(super doit:)
		; code executed each game cycle
		(if (& (gEgo onControl:) ctlMAROON)
			(gRoom newRoom: 248)
		)
		
		
		(if (<= (gEgo distanceTo: eyePod) 40)
			(if (not awake)
				(= awake 1)
				(eyePodScript changeState: 1); wake up
			)
		)
		(if (<= (gEgo distanceTo: eyePod) 20)
			(if (not awake)
				;(eyePodScript changeState: 1); wake up
			else
				(if hiding
					(if (not moving)
						(if (not eyePodStuck)
							(eyePodScript changeState: 7)
						)
					)
				)
			)
		)
		(if (<= (gEgo distanceTo: eyePod) 11)
			(if moving
				(if (not falling)
					(RoomScript changeState: 1)	
				)	
			)
		)
		; slowing down the eyepod
		(if (<= (eyePod distanceTo: sticky) 3)
			(if stickyDown
				(if (not eyePodStuck)
					(eyePod setMotion: NULL view: 41 loop: 3 cel: 0 setCycle: Fwd cycleSpeed: 3 ignoreActors: FALSE)
					(= eyePodStuck 1)
					(= moving 0)	
				)			
			)
		)
		; slowing down player if stepping on honey
		(if (and stickyDown (not goingForEye))
			(if (& (gEgo onControl:) ctlSILVER)
				(if (not stickyStop)
					;(gEgo setMotion: NULL)
					(= stickyStop 1)
				else
					;(if goingForEye
					;	(gEgo xStep: 1 yStep: 1)					
					;	(gEgo setMotion: MoveTo 83 100 RoomScript)
					;else
						(gEgo xStep: 1 yStep: 1)
						(runProc)
					;)
				)	
			else
				(if stickyStop
					(RunningCheck)
					(runProc)
					(= stickyStop 0)
				)
			)
		)
	)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if (& (pEvent modifiers?) emRIGHT_BUTTON)
				(if (or (or (or (checkEvent pEvent (bushBottomLeft nsLeft?)(bushBottomLeft nsRight?)(bushBottomLeft nsTop?)(bushBottomLeft nsBottom?))
					(checkEvent pEvent (bushBottomRight nsLeft?)(bushBottomRight nsRight?)(bushBottomRight nsTop?)(bushBottomRight nsBottom?)))
					(checkEvent pEvent (bushTopLeft nsLeft?)(bushTopLeft nsRight?)(bushTopLeft nsTop?)(bushTopLeft nsBottom?)))
					(checkEvent pEvent (bushTopRight nsLeft?)(bushTopRight nsRight?)(bushTopRight nsTop?)(bushTopRight nsBottom?)))
					(PrintOther 34 2)	
				else
					(if (checkEvent pEvent (eyePod nsLeft?)(eyePod nsRight?)(eyePod nsTop?)(eyePod nsBottom?))
						(if awake
							(if (== g34EyePod 2) ;  did you get the eye?
								
							else
								(if (== g34EyePod 1)
									(if eyePodStuck
										(PrintOther 34 5)
									else
										(PrintOther 34 4)
									)
								else
									(PrintOther 34 3)	
								)	
							)
						else
							(PrintOther 34 3)
						)						
					else
						
					)
				)
					
			)
			(if (checkEvent pEvent (sticky nsLeft?)(sticky nsRight?)(sticky nsTop?)(sticky nsBottom?))
					(if stickyDown
						(if eyePodStuck
							(if (== g34EyePod 2)
								(PrintOther 34 7)	
							else
								(PrintOther 34 13)	
							)							
						else
							(PrintOther 34 7)	
						)	
					else
					
					)
				)
		)
		
		; handle Said's, etc...
		(if (Said 'grab, get/animal, creature,tail')
			(if (<= (gEgo distanceTo: eyePod) 30)
				(if eyePodStuck
					(if (== g34EyePod 1)
						(= goingForEye 1)
						(eyePodScript changeState: 9)
					else
						(Print "There is nothing like that here.")
					)	
				else
					(++ triedToGrabCounter)
					(if (> triedToGrabCounter 3)
						(PrintOther 34 20)	
					else
						(PrintPod 34 12)
					)
					
				)
			else
				(++ triedToGrabCounter)
				(if (> triedToGrabCounter 3)
					(PrintOther 34 20)		
				else
					(PrintPod 34 1)
				)
				
			)	
		)
		(if (or (Said 'use, pour, break, tear, open, squeeze/honey, fruit')
			(Said 'toss/fruit[/ground]'))
			(if (not g34EyePod)
				(if gHoneyNum ; if has any number of Honey Fruit
					(PrintOK)
					(-- gHoneyNum)
					((gInv at: 2) count: gHoneyNum)
					(if (== gHoneyNum 0)
						(gEgo put: 2)
					)
					(= g34EyePod 1)	
					(stickyScript changeState: 1)	
				else
					(Print "You don't have any honey fruit.")
				)
			else
				(Print "There's no need to do that again.")
			)	
		)
		(if (Said 'talk/creature,animal')
			(if awake
				(if g34EyePod	; did you get the eye?
					(Print 34 17)
				else
					(PrintPod 34 10)
					(PrintPod 34 11)	
				)
			else
				(PrintOther 34 16)
				(eyePodScript changeState: 1)	
			)
		)
		(if (Said 'look>')
			(if (Said '/creature,animal')
				(if awake
					(if g34EyePod	; did you get the eye?
						(PrintOther 34 6)	
					else
						(if eyePodStuck
							(PrintOther 34 5)
						else
							(PrintOther 34 4)
						)
					)
				else
					(PrintOther 34 3)	
				)
			)
			(if (Said '/bush')
				(PrintOther 34 2)		
			)
			(if (Said '/pool, honey')
				(if stickyDown
					(PrintOther 34 7)
				else
					(PrintOther 34 9)	
				)		
			)
			(if (Said '/ground')
				(if stickyDown
					(PrintOther 34 7)
				else
					(PrintOther 34 8)	
				)		
			)
			(if (Said '/forest,verlorn')
				(PrintOther 0 105)	
			)
			(if (Said '[/!*]')
				(PrintOther 34 19)
				(if (not g34EyePod)
					(PrintOther 34 3)	
				)
			)
		)
		(if (Said 'hi')
			(++ gHoneyNum)	
		)
	)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0 ; Handle state changes
			)
			(1 ; Ego Falling
				(= falling 1)
				(ProgramControl)
				(gEgo hide:)
				(egoCheck)
				(actionEgo cel: 0 posn: (gEgo x?)(gEgo y?) show: setCycle: End self cycleSpeed: 2)	
			)
			(2
				(= falling 0)
				(egoCheck)
				(actionEgo cel: 0 setCycle: End self)		
			)
			(3
				(PlayerControl)
				(actionEgo hide:)
				(gEgo show: loop: 2)	
			)
		)
	)
)

(instance stickyScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0
			)
			(1
				(ProgramControl)
				(= placingSticky 1)
				(gEgo setMotion: MoveTo 150 70 self ignoreControl: ctlWHITE)	
			)
			(2
				(gEgo hide:)
				(egoCheck)
				(actionEgo posn: (gEgo x?)(gEgo y?) show: cel: 0 setCycle: End self cycleSpeed: 2)	
			)
			(3 (= cycles 3)
				(PrintOther 34 15)
			)
			(4
				(sticky show:)
				(actionEgo setCycle: Beg self)
			)
			(5
				(= stickyDown 1)
				(= placingSticky 0)
				(actionEgo hide:)
				(gEgo show: observeControl: ctlWHITE)
				(PlayerControl)
			)	
		)
	)
)

(instance eyePodScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0
			)
			(1 ; Wake up
				(ProgramControl)
				(= awake 1)
				(eyePod setCycle: End self cycleSpeed: 3)
			)
			(2 (= cycles 16)
				(eyePod loop: 2 setCycle: Fwd)
			)
			(3
				(eyePod loop: 0 setCycle: End self)	
			)
			(4 (= cycles 6)
				
			)
			(5
				(eyePod view: 44 setCycle: Walk setMotion: MoveTo 86 112 self)	
			)
			(6				
				(PrintPod 34 10)
				(PrintPod 34 11)
				(= hiding 3)
				(PlayerControl)	
			)
			(7
				(= moving 1) ; while moving is true, disables trigger in doit. Perhaps use moving variable to float a value for hiding?
				(= rando (Random 1 100))
				(switch hiding
					(1
						(if stickyDown
							(eyePod setMotion: MoveTo 220 78 self)
							(= moving 2)
						else
							(if (> rando 50)
								(eyePod setMotion: MoveTo 86 112 self)
								(= moving 3)
							else
								(eyePod setMotion: MoveTo 220 78 self)
								(= moving 2)
							)
						)
					)
					(2
						(if stickyDown
							(eyePod setMotion: MoveTo 86 78 self)
							(= moving 1)
						else
							(if (> rando 50)
								(eyePod setMotion: MoveTo 86 78 self)
								(= moving 1)
							else
								(eyePod setMotion: MoveTo 220 112 self)
								(= moving 4)
							)
						)
					)
					(3						
						(if stickyDown
							(eyePod setMotion: MoveTo 86 78 self)
							(= moving 1)
						else
							(if (> rando 50)
								(eyePod setMotion: MoveTo 86 78 self)
								(= moving 1)
							else
								(eyePod setMotion: MoveTo 220 112 self)
								(= moving 4)
							)
						)
					)
					(4
						(if stickyDown
							(eyePod setMotion: MoveTo 220 78 self)
							(= moving 2)
						else
							(if (> rando 50)
								(eyePod setMotion: MoveTo 86 112 self)
								(= moving 3)
							else
								(eyePod setMotion: MoveTo 220 78 self)
								(= moving 2)
							)
						)
					)
					
				)	
			)
			(8
				(= hiding moving)
				(= moving 0)	
			)
			(9 ; Eyepod giving up its eye
				(ProgramControl)
				(eyePod ignoreActors:)
				(if (< (gEgo y?) (eyePod y?))
					(gEgo setMotion: MoveTo (- (eyePod x?) 20) (gEgo y?) self)
				else
					(self cue:)
					;(PrintOK)	
				)
			)
			(10
				(gEgo setMotion: MoveTo 155 (+ (eyePod y?) 30) self ignoreControl: ctlWHITE)
			)
			(11 (= cycles 2)
				(gEgo loop: 3)
			)
			(12 (= cycles 7)
				(PrintPod 34 14)
			)
			(13
				(= gEgoMovementType 0)
				(RunningCheck)
				(gEgo xStep: 3 setMotion: MoveTo 135 (+ (eyePod y?) 20) self ignoreControl: ctlWHITE)		
			)
			(14 (= cycles 2)
				(gEgo loop: 3)	
			)
			(15
				(eyePod loop: 4 cel: 0 setCycle: End self)	
			)
			(16	; player goes to retrieve eye
				(gEgo setMotion: MoveTo 150 (eyePod y?) self)
			)
			(17
				(gEgo hide:)
				(actionEgo show: posn: (gEgo x?)(gEgo y?) view: gEgoPickUpView loop: 0 cel: 0 setCycle: End self)	
			)
			(18	
				(actionEgo setCycle: Beg self)
				(eyePod hide:)
				;(gEgo get: number)
				(PrintOther 34 18)
			)
			(19 (= cycles 2)
				(gEgo show: get: 11)
				(actionEgo hide:)
				(= goingForEye 0)
				(PlayerControl)
				
				(++ gEyes)
				((gInv at: 11) count: gEyes)
				(= g34EyePod 2)
				(gGame changeScore: 1)	
				
			)
			(20
				(PrintPod 34 21)
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


(procedure (egoCheck)
	(if falling
		(if gAnotherEgo
			(actionEgo view: 457)	; leah
		else
			(actionEgo view: 410)	; ego
		)
	else
		(if gAnotherEgo
			(actionEgo view: 310 loop: 1)	; leah
		else
			(actionEgo view: 310 loop: 2)	; ego
		)	
	)
	(if placingSticky
		(if gAnotherEgo
			(actionEgo view: 450 loop: 0)	; leah
		else
			(actionEgo view: 232 loop: 0)	; ego
		)	
	)	
)

(procedure (PrintPod textRes textResIndex)
		(= gWndColor 13)
		(= gWndBack 5)
		(Print textRes textResIndex
			#width 220
			#at -1 140
			#title "A voice inside your head:"
			#font 4
		)
		(= gWndColor 0)
		(= gWndBack 15)
)
(procedure (PrintOther textRes textResIndex)
	(Print textRes textResIndex		
		#width 280
		#at -1 140
	)
)


(instance sticky of Prop
	(properties
		y 80
		x 160
		view 63 ; honey on ground
		loop 3
	)
)
(instance actionEgo of Prop
	(properties
		y 118
		x 170
		view 457 ; Leah falling to ground
	)
)

(instance eyePod of Act
	(properties
		y 118
		x 170
		view 41
		loop 1 ; sleeping
	)
)
(instance bushTopLeft of Prop
	(properties
		y 80
		x 86
		view 60
	)
)
(instance bushBottomLeft of Prop
	(properties
		y 116
		x 86
		view 60
		loop 1
	)
)
(instance bushTopRight of Prop
	(properties
		y 80
		x 220
		view 60
		loop 2
	)
)
(instance bushBottomRight of Prop
	(properties
		y 116
		x 220
		view 60
		loop 3
	)
)