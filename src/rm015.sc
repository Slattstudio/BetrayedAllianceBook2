;;; Sierra Script 1.0 - (do not remove this comment)
(script# 15)
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
	
	rm015 0	

)

(instance rm015 of Rm
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
		(gEgo init:)
		(alterEgo
			init:
			hide:
			ignoreActors:
			setScript: searchScript
		)
		(deadSoldier init: ignoreActors: setScript: searchScript)
	)
)

(instance RoomScript of Script
	(properties)
	
	(method (doit)
		(super doit:)
		; code executed each game cycle
	)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		;handle Said's, etc...
		(if (Said 'search,examine/body')	
			(searchScript changeState: 1)
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

(instance searchScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(1
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo
					setMotion:
						MoveTo
						(+ (deadSoldier x?) 2)
						(- (deadSoldier y?) 5)
						searchScript
					ignoreControl: ctlWHITE
				)
			)
			(2
				(gEgo hide:)
				(alterEgo
					show:
					view: 232
					loop: 1
					posn: (gEgo x?) (gEgo y?)
					setCycle: End searchScript
					cycleSpeed: 2
				)
			)
			(3
				(= cycles 8)
				(Print "You found...")
				(Print "600 Followers!!!" #font 9)
				(Print "Thank you!")

			)
			(4
				(alterEgo setCycle: Beg searchScript)
			)
			(5
				(gEgo show: observeControl: ctlWHITE)
				(alterEgo hide:)
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
			)
		)
	)
)

(instance alterEgo of Prop
	(properties
		y 164
		x 68
		view 232
		loop 1
	)
)
(instance deadSoldier of Prop
	(properties
		y 147
		x 175
		view 322
		loop 7
		cel 8
	)
)