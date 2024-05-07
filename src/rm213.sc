;;; Sierra Script 1.0 - (do not remove this comment)
(script# 213)
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
	
	rm213 0
	
)
(local

	fallingDown =  0
	entering = 1
)


(instance rm213 of Rm
	(properties
		picture scriptNumber
		north 0
		east 50
		south 216
		west 212
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript)
		
		(SetUpEgo)
		(gEgo init:)
		(RunningCheck)
		
		(switch gPreviousRoomNumber
			(212
				(PlaceEgo 59 86 2)
				(RoomScript changeState: 3)		
			)
			(50
				(PlaceEgo 290 122 1)
				(= entering 0)	
			)
			(216
				(PlaceEgo 200 171 1)
				(RoomScript changeState: 1)	
			)
			(else 
				(PlaceEgo 290 122 1)
				(= entering 0)
				;(gEgo posn: 150 100 loop: 1)
			)
			
		)
				
		(actionEgo init: hide: ignoreControl: ctlWHITE ignoreActors: setScript: fallScript)
		
	)
)

(instance RoomScript of Script
	(properties)
	
	(method (doit)
		(super doit:)
		; code executed each game cycle
		
		; this convoluted code it to keep Ego behind the pine trees when walking on the path
		(if (and (< (gEgo y?) 145)(> (gEgo x?) 222))
			(gEgo setPri: 1)	
		else
			(gEgo setPri: -1)		
		)
		
		(if (& (gEgo onControl:) ctlMAROON)	; falling down
			(if (not fallingDown)
				(fallScript changeState: 1)
				(= fallingDown 1)
				(if gAnotherEgo
					(actionEgo view: 448)		
				else
					(actionEgo view: 229)
				) 
			)
		)
		(if (& (gEgo onControl:) ctlSILVER)	; falling while walking up
			(if (not fallingDown)
				(fallScript changeState: 1)
				(= fallingDown 1)
				(if gAnotherEgo
					(actionEgo view: 449 setPri: 8)		
				else
					(actionEgo view: 431 setPri: 8)
				)
			)
		)
		(if (& (gEgo onControl:) ctlRED)	; falling down with priority considerations
			(if (not fallingDown)
				(fallScript changeState: 1)
				(= fallingDown 1)
				(if gAnotherEgo
					(actionEgo view: 448 setPri: 1)		
				else
					(actionEgo view: 229 setPri: 1)
				)
			)
		)
		(if (not entering)
			(if (& (gEgo onControl:) ctlNAVY)
				(gRoom newRoom: 216)
			)
			(if (& (gEgo onControl:) ctlBLUE)
				(gRoom newRoom: 212)
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
				; entering from the south
			(1
				(ProgramControl)
				(gEgo setMotion: MoveTo 163 155 self)	
			)
			(2
				(gEgo loop: 3)
				(PlayerControl)	
				(= entering 0)
			)
				; entering from the west
			(3
				(ProgramControl)
				(gEgo setMotion: MoveTo 81 115 self)	
			)
			(4
				(gEgo loop: 2)
				(PlayerControl)	
				(= entering 0)
			)
		)
	)
)

(instance fallScript of Script
	(properties)
	
	(method (changeState newState &tmp dyingScript)
		(= state newState)
		(switch state
			(0)
			(1        ; falling in from of rocks
				(ProgramControl)
				(gEgo hide:)
				(actionEgo show: posn: (+ (gEgo x?) 10) (gEgo y?) yStep: 7 setMotion: MoveTo (+ (gEgo x?) 10) 190 self)	
			)
			(2	(= cycles 20)
				(actionEgo hide:)	
			)
			(3
				(ShakeScreen 1)
				(if (not [gDeaths 3])
					(++ gUniqueDeaths)
				)
				(++ [gDeaths 3])
				(= gDeathIconEnd 1)
				(if gAnotherEgo 
				
					(= dyingScript (ScriptID DYING_SCRIPT))
					(dyingScript
						caller: 749
						register:
							{If only cliffs were made of marshmallows and not rocks your final misstep could have been a happy accident instead of a fatal one.}
					)
				else
					(= dyingScript (ScriptID DYING_SCRIPT))
					(dyingScript
						caller: 709
						register:
							{If only cliffs were made of marshmallows and not rocks your final misstep could have been a happy accident instead of a fatal one.}
					)
				)
				(gGame setScript: dyingScript)
			)
		)
	)
)

(instance actionEgo of Act
	(properties
		y 60
		x 165
		view 229
	)
)