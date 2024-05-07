;;; Sierra Script 1.0 - (do not remove this comment)
(script# 202)
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
	rm202 0
)
(local

	mysteryMusic = 0
	entering = 1
	fallingDown = 0
	
)

(instance rm202 of Rm
	(properties
		picture scriptNumber
		north 206
		east 0
		south 401
		west 204
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript)
		
		(SetUpEgo)
		(gEgo init:)
		(RunningCheck)
		
		(switch gPreviousRoomNumber
			(204
				(PlaceEgo 67 48 0)
				(= entering 0)	
			)
			(206
				(PlaceEgo 145 5 2)
				(RoomScript changeState: 1)	
			)
			(401
				(PlaceEgo 177 174 3)
				(RoomScript changeState: 3)		
			)
			(else 
				(PlaceEgo 111 46 2)
				(= entering 0)
				;(gEgo posn: 150 100 loop: 1)
			)
		)
		
		(actionEgo init: hide: ignoreControl: ctlWHITE ignoreActors: setScript: fallScript)	
		(bushes init:)
		
	)
)

(instance RoomScript of Script
	(properties)
	
	(method (doit)
		(super doit:)
		; code executed each game cycle
		
		(if (not fallingDown)
			(if (< (gEgo y?) 70)	
				(gEgo setPri: 10)	
			else
				(gEgo setPri: -1)
			)
		
		)
		
		(if (not entering)
			(if (& (gEgo onControl:) ctlSILVER)	; falling down with priority considerations
				(if (not fallingDown)
					(fallScript changeState: 1)
					(= fallingDown 1)
					(if gAnotherEgo
						(actionEgo view: 448 setPri: 4)		
					else
						(actionEgo view: 229 setPri: 4)	
					) 
				)
			)
			(if (& (gEgo onControl:) ctlBLUE)	; falling while walking up
				(if (not fallingDown)
					(fallScript changeState: 1)
					(= fallingDown 1)
					(if (> (gEgo y?) 115)
						(actionEgo setPri: 4)		
					else
						(actionEgo setPri: 2)
					)
					(if gAnotherEgo
						(actionEgo view: 449)		
					else
						(actionEgo view: 431)
					)
				)
			)
			(if (& (gEgo onControl:) ctlRED)	; falling down 
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
			(if (& (gEgo onControl:) ctlGREY)
				(gRoom newRoom: 401)	
			)
			(if (& (gEgo onControl:) ctlNAVY)
				(gRoom newRoom: 206)	
			)
		)
		(if (& (gEgo onControl:) ctlMAROON)
			(bushes cel: 1)
			(if (not mysteryMusic)
				(gTheSoundFX number: 4 play:)
				(= mysteryMusic 1)
				(gEgo setMotion: NULL)
			)
		else
			(bushes cel: 0)
			(= mysteryMusic 0)
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
				(gEgo setMotion: MoveTo 107 38 self)
			)
			(2
				(gEgo loop: 2)
				(PlayerControl)	
				(= entering 0)
			)
			(3
				(ProgramControl)
				(gEgo setMotion: MoveTo 215 158 self)
			)
			(4
				(gEgo loop: 3)
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

(instance bushes of Prop
	(properties
		y 77
		x 30
		view 803
		loop 4
	)
)