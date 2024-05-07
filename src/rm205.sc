;;; Sierra Script 1.0 - (do not remove this comment)

(script# 205)
(include sci.sh)
(include game.sh)
(use Controls)
(use Cycle)
(use Feature)
(use Game)
(use Main)
(use Obj)
(use follow)

(public
	egoSwitchRegion 0
)

(local
; Ego-Switching Region
; by Ryan Slattery
; Originally Designed For Betrayed Alliance Book 2



	myEvent
; Used in the Doit method to track the mouse over the switch button
	buttonOpen =  0
; Used so that the Doit method doesn't send constant signals cancelling the button animation
	roomToggleNumber
; Intermediary variable allowing the change of gSwitchedRoomNumber before calling gRoom:newRoom command
	[characterXYToggle 3] = [0 0 0]
; Intermediary variable allowing the change of posn before asigning new gPrevXY variables (and loop)
	itemIteration
; Used to switch all items from one character to a room or from a room to the other character
; Items stored for Male Ego in Rm 205 and for Female Ego in Rm 210
; IMPORTANT - remember to change the item Iteration # as you add items
;	otherEgoIdle =  1
; 1 when Ego stands still, 0 when Ego is following
;	onEgo =  0
	buttonLocation = 1
	
	movementToggle = 0
)
; 1 = gEgo, 2 = otherEgo

(instance egoSwitchRegion of Rgn
	(properties)
	
	(method (init)
		(super init:)
		
		(switcher init: ignoreActors: setPri: 15 setScript: timerScript)
		
		(if (not gSwitchingAllowed)
			(switcher hide:)	
		)
		(if gAnotherEgo
			(switcher loop: 1)
		else
			
			(switcher loop: 0)
		)
		
		;(otherEgo init: hide: ignoreActors: posn: [gPrevXY 0] [gPrevXY 1] loop: [gPrevXY 2] ignoreActors: )
		
		(if gSeparated
			(if (== gRoomNumber gSwitchedRoomNumber) ; Are the Ego and Other Ego in the Same room?
				
			)
		else
			;(otherEgo posn: [gWhereBuddy 0] [gWhereBuddy 1] )
		)
		
		(if (not gSeparated)
			(= gSwitchedRoomNumber gRoomNumber)
			(if gAnotherEgo
			;	(otherEgo view: 0 show:)
			else
			;	(otherEgo view: 343 show:)
			)	
		)
	)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		(if (== (pEvent type?) evMOUSEBUTTON)
		;	(if (& (pEvent modifiers?) emRIGHT_BUTTON)
		;		(if (== onEgo 2)
		;			(if otherEgoIdle
		;				(= otherEgoIdle 0)
						;(otherEgo setCycle: Walk setMotion: Follow gEgo)
		;			else
		;				(= otherEgoIdle 1)
						;(otherEgo setMotion: NULL)
		;			)
		;		)
		;	)
		)
		(if gSwitchingAllowed
			(if (or (Said 'switch')
					;(or
					;	(if (& (pEvent modifiers?) emRIGHT_BUTTON)
					;		(if (not gSeparated)
					;			(checkEvent
					;				pEvent
					;				(otherEgo nsLeft?)
					;				(otherEgo nsRight?)
					;				(otherEgo nsTop?)
					;				(otherEgo nsBottom?)
					;			)
					;		)
					;	)
					(if (== (pEvent type?) evMOUSEBUTTON)
						(if
							(checkEvent
								pEvent
								(switcher nsLeft?)
								(switcher nsRight?)
								(switcher nsTop?)
								(switcher nsBottom?)
							)
						)
					)
				)
				(= gSwitchingAllowed 2)
				;(switcheroo)
				                      ; Haven't drawn it yet
				
			)
		)
	)
	
	(method (doit)
		(super doit:)
		(= myEvent (Event new: evNULL))
		(cond 
			(
				(checkEvent
					myEvent
					(- (switcher nsLeft?) 5)
					(switcher nsRight?)
					(+ (switcher nsTop?) 10)
					(+ (switcher nsBottom?) 10)
				)
				; (SetCursor(996 HaveMouse()) = gCurrentCursor 996) //Set Cursor to "right-click cursor"
				(if (not buttonOpen)
					(switcher setCycle: End)
					(= buttonOpen 1)
					(= gNoClick 1)
				)
			)
			(buttonOpen (switcher setCycle: Beg) (= buttonOpen 0) (= gNoClick 0))
		)
		(if (not buttonOpen)
			(= gNoClick 0)	
		)
		; Is the cursor over the EGO
		;(if (not buttonOpen)
		;	(cond 
				;(
				;	(checkEvent
				;		myEvent
				;		(gEgo nsLeft?)
				;		(gEgo nsRight?)
				;		(gEgo nsTop?)
				;		(gEgo nsBottom?)
				;	)
				;	(SetCursor 988 (HaveMouse))
				;	(= gCurrentCursor 988)                        ; Switch Cursor
				;	(= onEgo 1)
				;)
		;	(
			;		(checkEvent
			;			myEvent
			;			(otherEgo nsLeft?)
			;			(otherEgo nsRight?)
			;			(otherEgo nsTop?)
			;			(otherEgo nsBottom?)
			;		)
					;(if otherEgoIdle
					;	(SetCursor 987 (HaveMouse))
					;	(= gCurrentCursor 987)
					;else 
			;		(if (not gSeparated)                                                   ; Follow Cursor
					;	(SetCursor 986 (HaveMouse))
						;(= gCurrentCursor 986)
					;)
					                                                   ; Stay Cursor
			;			(= onEgo 1)
			;		)
			;	)
			;	(else
					;(SetCursor 999 (HaveMouse))
					;(= gCurrentCursor 999)                            ; Normal Cursor
			;		(= onEgo 0)
			;	)
			;)
		;)
		; Is the cursor over the Other Ego
; (if ( checkEvent(myEvent (otherEgo:nsLeft) (otherEgo:nsRight) (otherEgo:nsTop) (otherEgo:nsBottom)) )
;            (if(otherEgoIdle)
;                (SetCursor(987 HaveMouse()) = gCurrentCursor 987) // Follow Cursor
;            )(else
;                (SetCursor(986 HaveMouse()) = gCurrentCursor 986) // Stay Cursor
;            )
;        )(else
;            (SetCursor(999 HaveMouse()) = gCurrentCursor 999)
;        )
		(myEvent dispose:)
		
		(if (not gSwitchingAllowed)
			(switcher hide:)
		else
			(switcher show:)	
		)
		(if (== gSwitchingAllowed 2)
			(if (not gDisableSwitch)
				(switcheroo)
			)
			(= gSwitchingAllowed 1)
		)
		(if gAnotherEgo
			(if gDisableSwitch
				(switcher loop: 6)	; grayed out
			else	
				(switcher loop: 1)
			)
		else
			(if gDisableSwitch
				(switcher loop: 5)	; grayed out
			else	
				(switcher loop: 0)
			)
		)
	)
)

(instance timerScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0 (= cycles 1))
			(1 (= gTeleporting 0))
		)
	)
)
(procedure (switcheroo)
	(if (not gProgramControl)
		(if (not (== gRoomNumber gSwitchedRoomNumber))
						;(= [gPrevXY 0] (otherEgo x?))
						;(= [gPrevXY 1] (otherEgo y?))
						;(= [gPrevXY 2] (otherEgo loop?))
		;		(if gAnotherEgo
							;(otherEgo view: 0 posn: (gEgo x?) (gEgo y?))
							
		;		else
							;(otherEgo view: 343 posn: (gEgo x?) (gEgo y?))
		;		)
		;		(gEgo posn: [gPrevXY 0]  [gPrevXY 1] loop: [gPrevXY 2])
		;	)
		; Remove and Give Items based on who is being used
			(if gSeparated
				(for ( (= itemIteration 0)) (< itemIteration 14)  ( (++ itemIteration))
					(if gAnotherEgo
		; Switching from Female Ego
						(if (== (gEgo has: itemIteration) 1)
							(gEgo put: itemIteration 210)
						)                                          ; Items stored in Room 210
						(if (== (IsOwnedBy itemIteration 205) 1)
		; Items stored in Rm 205 given to Male Ego
							(gEgo get: itemIteration)
						)
					else
		; Switching from Male Ego
						(if (== (gEgo has: itemIteration) 1)
							(gEgo put: itemIteration 205)
						)                                          ; Items stored in Room 205
						(if (== (IsOwnedBy itemIteration 210) 1)
		; Items stored in Rm 210 given to Female Ego
							(gEgo get: itemIteration)
						)
					)
				)
			)
			(if gSeparated
		; Get Room Number Variables saved and set up for room switch
				(= roomToggleNumber gSwitchedRoomNumber) ; saves variable for newRoom call
				(= gSwitchedRoomNumber gRoomNumber)    ; saves current room as the new switched room
		; Get Ego's X/Y position saved and set up for room switch
						;(= [characterXYToggle 0] (gEgo x?))
						;(= [characterXYToggle 1] (gEgo y?))
						;(= [characterXYToggle 2] (gEgo loop?))
						;(gEgo posn: [gPrevXY 0] [gPrevXY 1] loop: [gPrevXY 2])        ; wondering if this works without setting Up Ego in this script
						;(= [gPrevXY 0] [characterXYToggle 0])
						;(= [gPrevXY 1] [characterXYToggle 1])
						;(= [gPrevXY 2] [characterXYToggle 2])
						
				(if gAnotherEgo ; leah
					(= [gAnotherEgoXYL 0] (gEgo x?))
					(= [gAnotherEgoXYL 1] (gEgo y?))
					(= [gAnotherEgoXYL 2] (gEgo loop?))
							;(FormatPrint "Saving Leah's x: %u, y: %u" [gAnotherEgoXYL 0] [gAnotherEgoXYL 1])
				else
					(= [gPrevXY 0] (gEgo x?))
					(= [gPrevXY 1] (gEgo y?))
					(= [gPrevXY 2] (gEgo loop?))
							;(FormatPrint "Saving Ego's x: %u, y: %u" [gPrevXY 0] [gPrevXY 1])	
				)
				(if gAnotherEgo	; If you're playing as Leah
					(= gAnotherEgo 0) ; Male character
					(= gEgoView 0)
					(= gEgoStoppedView 903)
					
				else
					(= gAnotherEgo 1) ; Female character
					(= gEgoView 343)
					(= gEgoStoppedView 1)
				)
				; Save and implement movement types when switching between egos
				(= movementToggle gEgoMovementType)
				(= gEgoMovementType gSwitchedMovement)
				(= gSwitchedMovement movementToggle)
				
				
				(= gTeleporting 1) ; This makes it so the New Room's init method won't determind Ego's Posn
				
				; DEMO MUSIC CODE for EASY MUSIC SWITCHING ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
				
				(if (and (> roomToggleNumber 37)	; if you're inside
						(< roomToggleNumber 50))
					(if gAnotherEgo	; if you're leah						
						(gTheMusic number: 41 loop: -1 priority: -1 play:)
					else
						(gTheMusic number: 40 loop: -1 priority: -1 play:)	
					)	
					
				else
					; OUTSIDE
					(if (not (== (gTheMusic number?) 10))
						(gTheMusic number: 10 loop: -1 priority: -1 play:)	
					)
				)
				
				
				
				
				;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
				
				
				(gRoom newRoom: roomToggleNumber)
			else
					; Set up a "waiting" animation for the character not being used (IF IN SAME ROOM ONLY)
					;	(if otherEgoIdle
					;		(if gAnotherEgo
					;			(otherEgo view: 903 loop: 4 setCycle: Fwd cycleSpeed: 4)
					;		else
					;			(otherEgo view: 1 loop: 5 setCycle: Fwd cycleSpeed: 4)
					;		)
					;	)
			)
		else
			(= gSameRoomSwitch 1)	
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

(instance switcher of Prop
	(properties
		y 29
		x 15
		view 997
		loop 1
	)
)

;(instance otherEgo of Act
;	(properties
;		y 1
;		x 1
;		view 0
;	)
;)
