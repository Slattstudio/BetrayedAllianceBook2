;;; Sierra Script 1.0 - (do not remove this comment)
(script# 381)
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
	
	rm381 0
	
)
(local
	
	;entering = 0
	moving = 0
	fallStopped = 1
	
)

(instance rm381 of Rm
	(properties
		picture scriptNumber
		north 0
		east 380
		south 0
		west 0
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript)
		
		(SetUpEgo)
		(gEgo init:)
		(RunningCheck)
		
		(switch gPreviousRoomNumber
			(380
				(PlaceEgo 314 61 1)
				(RoomScript changeState: 9)	
			)
			(else 
				(PlaceEgo 113 158 0)
				;(gEgo posn: 150 100 loop: 1)
			)
		)
		
		
		(actionEgo init: hide: ignoreActors: ignoreControl: ctlWHITE setScript: fallScript) 
	)
)

(instance RoomScript of Script
	(properties)
	
	(method (doit)
		(super doit:)
		; code executed each game cycle
		(if (& (gEgo onControl:) ctlRED)	; falling down
			(if (not moving)
				(= moving 1)
				(= fallStopped 0)
				(fallScript changeState: 1)	
			)
		)
		(if (& (actionEgo onControl:) ctlMAROON)	; Hit the ground
			(if (not fallStopped)
				(= fallStopped 1)
				(fallScript changeState: 3)	
			)
		)
		(if (& (gEgo onControl:) ctlGREY)	;leaving
			(RoomScript changeState: 9)	
		)
		(if (& (gEgo onControl:) ctlSILVER)	; on the stairs
			(if (not moving)
				(= moving 1)
				(if (> (gEgo y?) 115)	; at the bottom
					(RoomScript changeState: 1)	
				else
					(RoomScript changeState: 5)	
				)
			)
		)
	)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		; handle Said's, etc...
	)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0 ; Handle state changes
			)
			; up the stairs
			(1
				(ProgramControl)
				(gEgo setMotion: MoveTo 223 149 self)	
			)
			(2
				(gEgo setMotion: MoveTo 253 110 self)
			)
			(3
				(gEgo setMotion: MoveTo 271 69 self)	
			)
			(4
				(PlayerControl)
				(= moving 0)	
			)
			; down the stairs
			(5
				(ProgramControl)
				(gEgo setMotion: MoveTo 253 110 self)	
			)
			(6
				(gEgo setMotion: MoveTo 213 149 self)
			)
			(7
				(gEgo setMotion: MoveTo 154 162 self)	
			)
			(8
				(PlayerControl)	
				(= moving 0)
			)
			; entering the room from the east
			(9
				(ProgramControl)
				(gEgo setMotion: MoveTo 260 59 self)	
			)
			(10
				(PlayerControl)
				(gEgo loop: 2)	
			)
		)
	)
)
(instance fallScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0
			)
			; send player straight down
			(1	
				(ProgramControl)
				(gEgo hide:)
				(actionEgo show: view: 229 posn: (gEgo x?)(gEgo y?) yStep: 7 setMotion: MoveTo (gEgo x?) 190)
				(= moving 1)
					
			)
			(3	(= cycles 20)      ; ego hits grounds
				(ShakeScreen 2)
				(actionEgo
					view: 409
					loop: 0
					cel: 0
					setMotion: NULL
					setCycle: Fwd
					cycleSpeed: 3
					;setPri: 6
				)
				
			)
			(4       ; ego stands up
				(actionEgo loop: 1 setCycle: End self)	
			)
			(5
				(= cycles 7)      ; ego gets ready to walk
				(actionEgo hide:)
				(gEgo show: posn: (actionEgo x?) (actionEgo y?))
			)
			(6
				(if (< (gEgo x?) 230)
					(RoomScript changeState: 1)	
				else
					(gEgo setMotion: MoveTo 264 80 self)
				)
			)
			(7
				(gEgo setMotion: MoveTo 271 69 self)	
			)
			(8
				(PlayerControl)
				(= moving 0)	
			)
		)
	)
)
(instance actionEgo of Act
	(properties
		y 1
		x 1
		view 51
	)
)