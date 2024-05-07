;;; Sierra Script 1.0 - (do not remove this comment)
(script# 245)
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
	
	rm245 0
	
)
(local
	
	climbingHill = 0
	onTop = 0
	
)

(instance rm245 of Rm
	(properties
		picture scriptNumber
		north 0
		east 258
		south 0
		west 0
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript)
		(switch gPreviousRoomNumber
			(258 
				(gEgo posn: 305 85 loop: 1)
			)
			(else 
				(gEgo posn: 265 75 loop: 2)
			)
		)
		(SetUpEgo)
		(RunningCheck)
		
		(gEgo init:)
		(alterEgo init: hide: ignoreControl: ctlWHITE ignoreActors: setCycle: Walk)
	)
)

(instance RoomScript of Script
	(properties)
	
	(method (doit)
		(super doit:)
		; code executed each game cycle
		(if (& (gEgo onControl:) ctlMAROON)
			(gRoom newRoom: 237)
		)
		(if (& (gEgo onControl:) ctlGREY) ; moving up hill
			(if climbingHill
					
			else
				(if onTop
					(RoomScript changeState: 5)
					(= climbingHill 1)	
				else
					(RoomScript changeState: 1)
					(= climbingHill 1)
				)
			)
			
		)
		(if (& (gEgo onControl:) ctlSILVER) ; moving down hill

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
			(1	; Going Uphill
				(ProgramControl)
				(gEgo hide:)
				(alterEgo view: 233 show: posn: (gEgo x?)(gEgo y?) setMotion: MoveTo 260 150 self setPri: 4)
			)
			(2
				(alterEgo setMotion: MoveTo 260 110 self) ; Climbing up Hill	
			)
			(3
				(alterEgo hide:)
				(gEgo show: posn: (alterEgo x?)(alterEgo y?) setMotion: MoveTo 260 120 self)
			)
			(4
				(PlayerControl)	
				(= climbingHill 0)
				(= onTop 1)
			)
			(5 ; Going Downhill
				(ProgramControl)
				(gEgo hide:)
				(alterEgo view: 234 show: posn: (gEgo x?)(gEgo y?) setMotion: MoveTo 260 150 self setPri: 4)
			)
			(6
				(alterEgo setMotion: MoveTo 260 110 self ); Climbing down Hill	
			) 
			(7
				(alterEgo hide:)
				(gEgo show: posn: (alterEgo x?)(alterEgo y?) setMotion: MoveTo 260 100 self)
			)
			(8
				(PlayerControl)	
				(= climbingHill 0)
				(= onTop 0)
			)
		)
	)
)
(instance alterEgo of Act
	(properties
		y 80
		x 186
		view 0
	)
)