;;; Sierra Script 1.0 - (do not remove this comment)
(script# 207)
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
	rm207 0
)
(local
	
	entering = 1
	fallingDown =  0
	
)

(instance rm207 of Rm
	(properties
		picture scriptNumber
		north 0
		east 208
		south 206
		west 0
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript)
		
		(SetUpEgo)
		(gEgo init:)
		(RunningCheck)
		
		(switch gPreviousRoomNumber
			(206
				(PlaceEgo 41 172 0)
				(RoomScript changeState: 3)	
			)
			(208
				(PlaceEgo 303 36 1)
				(RoomScript changeState: 1)	
			)
			(else 
				(PlaceEgo 303 36 1)
				(RoomScript changeState: 1)	
			)
		)
		
		(actionEgo init: hide: ignoreControl: ctlWHITE ignoreActors: setScript: fallScript)
		(deathSplash init: hide: ignoreActors:)
		
	)
)

(instance RoomScript of Script
	(properties)
	
	(method (doit)
		(super doit:)
		; code executed each game cycle
		(if (not entering)
			(if (& (gEgo onControl:) ctlGREY)
				(gRoom newRoom: 208)	
			)
			(if (& (gEgo onControl:) ctlSILVER)
				(gRoom newRoom: 206)	
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
				(gEgo setMotion: MoveTo 233 51 self)
			)
			(2
				(PlayerControl)
				(= entering 0)	
			)
			(3
				(ProgramControl)
				(gEgo setMotion: MoveTo 97 145 self)
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
				(if (> (gEgo x?) 250)
					(actionEgo show: posn: (+ (gEgo x?) 10) (gEgo y?) yStep: 7 setMotion: MoveTo (+ (gEgo x?) 10) 160 self)		
				else
					(actionEgo show: posn: (+ (gEgo x?) 10) (gEgo y?) yStep: 7 setMotion: MoveTo (+ (gEgo x?) 10) 190 self)	
				)
			)
			(2	(= cycles 20)
				(if (> (gEgo x?) 250)
					(deathSplash show: posn: (+ (gEgo x?) 5) 160 setCycle: End setPri: 1 cycleSpeed: 2)	
				)
				(actionEgo hide:)	
			)
			(3
				(if (not (> (gEgo x?) 250))
					(ShakeScreen 1)
				)
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
(instance deathSplash of Prop
	(properties
		y 187
		x 147
		view 102
		loop 1
	)
)
