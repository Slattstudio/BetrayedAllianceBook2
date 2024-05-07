;;; Sierra Script 1.0 - (do not remove this comment)
(script# 104)
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
	rm104 0
)

(local
	myEvent
	leavingRoom = 0
)

(instance rm104 of Rm
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
		(trigger init: hide: ignoreActors:)
	)
)

(instance RoomScript of Script
	(properties)
	
	(method (doit)
		(super doit:)
		; code executed each game cycle
		(= myEvent (Event new: evNULL))
		(trigger posn: (myEvent x?) (- (myEvent y?) 7))
		
		(if (& (trigger onControl:) ctlMAROON)
			(SetCursor 981 (HaveMouse))
			(= gCurrentCursor 981)
			(= leavingRoom 1)
		else
			(SetCursor 999 (HaveMouse))
			(= gCurrentCursor 999)
			(= leavingRoom 0)
		)
		
		(myEvent dispose:)
	)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		
		(if (== (pEvent type?) evKEYBOARD)
			(if (== (pEvent message?) KEY_RETURN)
				(gRoom newRoom: 229)	
			)
		)
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if (== leavingRoom 1)
				(= gCurrentCursor 999)
				(gRoom newRoom: 229)
			)
		)
		
		(if (Said 'exit,leave')
			(= gCurrentCursor 999)
			(gRoom newRoom: 229)	
		)
		; handle Said's, etc...
		
		(if (Said 'look>')
			(if (Said '/stump,marking,carving,letter,message,writing')
				(PrintOther 104 1)	
			)
			(if (Said '/ring')
				(PrintOther 104 2)	
			)
			(if (Said '[/!*]')
				(PrintOther 104 0)		
			)
		)
	)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0 ; Handle state changes
			)
		)
	)
)
(procedure (PrintOther textRes textResIndex)
	(Print textRes textResIndex
		#width 280
		#at -1 140
	)
)
(instance trigger of Act
	(properties
		y 1
		x 1
		view 981
	)
)