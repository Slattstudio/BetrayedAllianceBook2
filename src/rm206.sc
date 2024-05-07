;;; Sierra Script 1.0 - (do not remove this comment)
(script# 206)
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
	rm206 0
)

(local

	fallingDown =  0
	entering = 1
)


(instance rm206 of Rm
	(properties
		picture scriptNumber
		north 207
		east 203
		south 202
		west 0
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript)
		
		(SetUpEgo)
		(gEgo init:)
		(RunningCheck)
		
		(switch gPreviousRoomNumber
			(202
				(PlaceEgo 75 175 0)
				(RoomScript changeState: 1)		
			)
			(203
				(PlaceEgo 258 170 1)
				(= entering 0)	
			)
			(207
				(PlaceEgo 305 57 1)	
				(RoomScript changeState: 3)	
			)
			(else 
				(PlaceEgo 305 57 1)	
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
		(if (not entering)
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
			(if (& (gEgo onControl:) ctlBLUE)	; falling while walking up
				(if (not fallingDown)
					(fallScript changeState: 1)
					(= fallingDown 1)
					(if gAnotherEgo
						(actionEgo view: 449 setPri: 1)		
					else
						(actionEgo view: 431 setPri: 1)
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
			(if (& (gEgo onControl:) ctlGREY)
				(gRoom newRoom: 207)	
			)
			(if (& (gEgo onControl:) ctlSILVER)
				(gRoom newRoom: 202)	
			)
			(if (& (gEgo onControl:) ctlNAVY)
				(gRoom newRoom: 203)	
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
			(1
				(ProgramControl)
				(gEgo setMotion: MoveTo 118 165 self)
			)
			(2
				(PlayerControl)	
				(= entering 0)
			)
			(3
				(ProgramControl)
				(gEgo setMotion: MoveTo 246 79 self)
			)
			(4
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