;;; Sierra Script 1.0 - (do not remove this comment)
(script# 273)
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
	rm273 0
)

(local
	sitting = 0	
)


(instance rm273 of Rm
	(properties
		picture scriptNumber
		north 274
		east 88
		south 11
		west 29
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
		
		(actionEgo init: hide: ignoreActors:)
	)
)

(instance RoomScript of Script
	(properties)
	
	(method (doit)
		(super doit:)
		; code executed each game cycle
		
		; while ego is hidden, and the other view is in its place, make sure the invisible ego can't accidentily walk off screen or onto other control colors.
		(if sitting	
			(gEgo setMotion: NULL)
		)
	)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		; handle Said's, etc...
		(if (Said 'sit')			
			(if (not sitting)
				; if bench is a View you can use: (if (<= (gEgo distanceTo: bench) 30)
				; if bench is not a View, use control colors to dictate if close enough:
				(if (& (gEgo onControl:) ctlSILVER) ; silver chosen at random, could be anything
					(PrintOK)
					(self changeState: 1)	
				else
					(PrintNotCloseEnough)
				)
			else
				(PrintYouAre)
			)
		)
		
	)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0 ; Handle state changes
			)
			(1
				(ProgramControl)
				(gEgo setMotion: MoveTo 122 122 self ignoreControl: ctlWHITE)
			)	
			(2
				; set actionEgo's position and animate
				(gEgo hide:)
				(actionEgo show: posn: (gEgo x?)(gEgo y?) loop: 1 cel: 0 setCycle: End self cycleSpeed: 2)
			)
			(3
				(PlayerControl)
				(= sitting 1)	
			)	
		)
	)
)

(instance actionEgo of Prop
	(properties
		y 170
		x 160
		view 374 ; sitting animation view
		loop 1
	)
)