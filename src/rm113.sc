;;; Sierra Script 1.0 - (do not remove this comment)
(script# 113)
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
	rm113 0
)

(local
	
	myEvent ; used to track the cursor's position outside of handleEvent
	
	holdingCube = 0		; On/Off for when the player can manipulate the block
	startingXposn = 0	; tracks the x position of the cursor when starting to manipulate the block
	startingYposn = 0
	
	xStretch = 3		; Used to make it so the cube will turn only if you move the cursor further and further
	xStretchLeft = 3	; Same as above, but for moving the opposite direction
	noMoreX = 0			; Used to make the block "stop" when you get back to start
	
	yStretchUp = 3
	yStretchDown = 9
	noMoreY = 0
	
	CubeCel = 0			; used to designate which Cel should be displayed based on X axis
	CubeLoop = 0
		
)

(instance rm113 of Rm
	(properties
		picture scriptNumber
		north 0
		east 0
		south 0
		west 0
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript)
		(switch gPreviousRoomNumber
			(else 
				(gEgo posn: 150 100 loop: 1)
			)
		)
		(SetUpEgo)
		(gEgo init: hide:)
		(block init:)
		(onOffSwitch init: hide:)
	)
)

(instance RoomScript of Script
	(properties)
	
	(method (doit)
		(super doit:)
		
		(= myEvent (Event new: evNULL)) ; Track the cursor's posn
		
		(if (and 
				(and 
					(and
						(> (myEvent x?) (block nsLeft:)) 
				 		(< (myEvent x?) (block nsRight:))
					)
                 	(> (myEvent y?) (block nsTop:))
				)
                (< (myEvent y?) (block nsBottom:))
			)
                
                (SetCursor 991 (HaveMouse)) (= gCurrentCursor 996)
                ;= target 1
		else
			(SetCursor 999 (HaveMouse)) (= gCurrentCursor 996)
		)
		
		; Cube Manipulation
		(if holdingCube
			; Moving Right
			(if (< noMoreX 12)
				(if (>  (myEvent x?) (+ startingXposn xStretch) ) 
					(++ CubeCel)
					(++ noMoreX)
					
					; Make sure that going Right will "forget" the stretching effect that may have taken place previous to the reverse move
					(if (> xStretchLeft 3) 
						(= xStretchLeft (- xStretchLeft 3))	
					)
				
					(= xStretch (+ xStretch 3))
					(if (>= CubeCel 12)
						(= CubeCel 0)
					)
					(block cel: CubeCel)
				)
			)
			; Moving Left
			(if (> noMoreX -12)
				(if (<  (myEvent x?) (- startingXposn xStretchLeft) ) 
					(-- CubeCel)
					(-- noMoreX)					
					
					; Make sure that going left will "forget" the stretching effect that may have taken place previous to the reverse move
					(if (> xStretch 3) 
						(= xStretch (- xStretch 3))	
					)
					
					(= xStretchLeft (+ xStretchLeft 3))
					(if (<= CubeCel -1)
						(= CubeCel 11)
					)
					(block cel: CubeCel)
				)
			)
			; Moving Up
			(if (> noMoreY -3)
				(if (<  (myEvent y?) (- startingYposn yStretchUp) ) 
					(++ CubeLoop)
					(-- noMoreY)					
					
					; Make sure that going left will "forget" the stretching effect that may have taken place previous to the reverse move
					(if (> yStretchDown 3) 
						(= yStretchDown (- yStretchDown 3))	
					)
					
					(= yStretchUp (+ yStretchUp 3))
					(if (>= CubeLoop 3)
						(= CubeLoop 2)
					)
					(block loop: CubeLoop)
				)
			)
			; Moving Down
			(if (< noMoreY 3)
				(if (>  (myEvent y?) (+ startingYposn yStretchDown) ) 
					(-- CubeLoop)
					(++ noMoreY)					
					
					; Make sure that going left will "forget" the stretching effect that may have taken place previous to the reverse move
					(if (> yStretchUp 3) 
						(= yStretchUp (- yStretchUp 3))	
					)
					
					(= yStretchDown (+ yStretchDown 3))
					(if (<= CubeLoop -1)
						(= CubeLoop 0)
					)
					(block loop: CubeLoop)
				)
			)
		)
		
		
		
		(myEvent dispose:) ; dispose of the info to nullify heap loss
				
	)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		(if (== (pEvent type:) evMOUSEBUTTON)
			(if holdingCube
				;(PrintOK)
				(= holdingCube 0)
				(= noMoreX 0)
				(= xStretch 3)
				(= xStretchLeft 3)
				(= yStretchUp 3)
				(= yStretchDown 12)
				(onOffSwitch hide:)
			else
				; Grabbing hold of the cube
				
				(= holdingCube 1)
				(= startingXposn (pEvent x?))
				(= startingYposn (pEvent y?))
				(onOffSwitch show:)				;Visual to show you have it.
				
			)
		)
		; handle Said's, etc...
	)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0 ; Handle state changes
			)
		)
	)
)
(instance block of Prop
	(properties
		y 120
		x 160
		view 520
	)
)

(instance onOffSwitch of Prop
	(properties
		y 30
		x 10
		view 800
	)
)