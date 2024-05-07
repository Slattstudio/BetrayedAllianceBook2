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
	[characterXYToggle 2] = [0 0]
; Intermediary variable allowing the change of posn before asigning new gPrevXY variables
	itemIteration
; Used to switch all items from one character to a room or from a room to the other character
; Items stored for Male Ego in Rm 205 and for Female Ego in Rm 210
; IMPORTANT - remember to change the item Iteration # as you add items
	otherEgoIdle =  1
; 1 when Ego stands still, 0 when Ego is following
	onEgo =  0
)
; 1 = gEgo, 2 = otherEgo

(instance egoSwitchRegion of Rgn
	(properties)
	
	(method (init)
		(super init:)
		(switcher
			init:
			ignoreActors:
			setPri: 14
			setScript: timerScript
		)
		(otherEgo
			init:
			hide:
			ignoreActors:
			posn: [gPrevXY 0] [gPrevXY 1]
			ignoreActors:
		)
		(if (== gRoomNumber gSwitchedRoomNumber) ; Are the Ego and Other Ego in the Same room?
			(if gAnotherEgo
				(otherEgo view: 0 show:)
			else
				(otherEgo view: 343 show:)
			)
		)
	)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if (& (pEvent modifiers?) emRIGHT_BUTTON)
				(if (== onEgo 2)
					(if otherEgoIdle
						(= otherEgoIdle 0)
						(otherEgo setCycle: Walk setMotion: Follow gEgo)
					else
						(= otherEgoIdle 1)
						(otherEgo setMotion: NULL)
					)
				)
			)
		)
		(if
			(or
				(Said 'switch')
				(if (== (pEvent type?) evMOUSEBUTTON)
					(if
						(and
							(> (pEvent x?) (switcher nsLeft?))
							(< (pEvent x?) (switcher nsRight?))
							(> (pEvent y?) (switcher nsTop?))
							(< (pEvent y?) (switcher nsBottom?))
						)
					)
				)
			)
			; )
			(if gAnotherEgo
				(= gAnotherEgo 0) ; Male character
				(= gEgoView 0)
				(= gEgoStoppedView 903)
			else
				(= gAnotherEgo 1); Female character
				(= gEgoView 343)
				(= gEgoStoppedView 1)
			)                       ; Haven't drawn it yet
			(if (== gRoomNumber gSwitchedRoomNumber)
				(= [gPrevXY 0] (otherEgo x?))
				(= [gPrevXY 1] (otherEgo y?))
				(if gAnotherEgo
					(otherEgo view: 0 posn: (gEgo x?) (gEgo y?))
				else
					(otherEgo view: 343 posn: (gEgo x?) (gEgo y?))
				)
			)
; Remove and Give Items based on who is being used
			(for ( (= itemIteration 0)) (< itemIteration 0)  ( (++ itemIteration)) (if gAnotherEgo
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
			))
; Get Room Number Variables saved and set up for room switch
			(= roomToggleNumber gSwitchedRoomNumber) ; saves variable for newRoom call
			(= gSwitchedRoomNumber gRoomNumber)    ; saves current room as the new switched room
; Get Ego's X/Y position saved and set up for room switch
			(= [characterXYToggle 0] (gEgo x?))
			(= [characterXYToggle 1] (gEgo y?))
			(gEgo posn: [gPrevXY 0] [gPrevXY 1])        ; wondering if this works without setting Up Ego in this script
			(= [gPrevXY 0] [characterXYToggle 0])
			(= [gPrevXY 1] [characterXYToggle 1])
			; Set up a "waiting" animation for the character not being used (IF IN SAME ROOM ONLY)
			(if otherEgoIdle
				(if gAnotherEgo
					(otherEgo view: 903 loop: 2)
				else
					(otherEgo view: 1 loop: 5 setCycle: Fwd cycleSpeed: 4)
				)
			)
			(= gTeleporting 1) ; This makes it so the New Room's init method won't determind Ego's Posn
			(gRoom newRoom: roomToggleNumber)
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
		; Is the cursor over the EGO
		(if (not buttonOpen)
			(cond 
				(
					(checkEvent
						myEvent
						(gEgo nsLeft?)
						(gEgo nsRight?)
						(gEgo nsTop?)
						(gEgo nsBottom?)
					)
					(SetCursor 988 (HaveMouse))
					(= gCurrentCursor 988)                        ; Switch Cursor
					(= onEgo 1)
				)
				(
					(checkEvent
						myEvent
						(otherEgo nsLeft?)
						(otherEgo nsRight?)
						(otherEgo nsTop?)
						(otherEgo nsBottom?)
					)
					(if otherEgoIdle
						(SetCursor 987 (HaveMouse))
						(= gCurrentCursor 987)
					else                                                  ; Follow Cursor
						(SetCursor 986 (HaveMouse))
						(= gCurrentCursor 986)
					)                                                     ; Stay Cursor
					(= onEgo 2)
				)
				(else
					(SetCursor 999 (HaveMouse))
					(= gCurrentCursor 999)                            ; Normal Cursor
					(= onEgo 0)
				)
			)
		)
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
		y 15
		x 30
		view 573
		loop 2
	)
)

(instance otherEgo of Act
	(properties
		y 1
		x 1
		view 0
	)
)

