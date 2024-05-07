;;; Sierra Script 1.0 - (do not remove this comment)
(script# 203)
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
	rm203 0
)

(local

	fallingDown =  0
	entering = 1
)


(instance rm203 of Rm
	(properties
		picture scriptNumber
		north 0
		east 0
		south 0
		west 206
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript)
		
		(SetUpEgo)
		(gEgo init:)
		(RunningCheck)
		
		(switch gPreviousRoomNumber
			(else 
				(PlaceEgo 32 100 0)
				;(gEgo posn: 150 100 loop: 1)
				(RoomScript changeState: 1)
			)
		)
		(actionEgo init: hide: ignoreControl: ctlWHITE ignoreActors: setScript: fallScript)	
		(waterFall init: setCycle: Fwd cycleSpeed: 2 setPri: 9)
		(waterRipple init: setCycle: Fwd cycleSpeed: 3 setPri: 8)		
	)
)

(instance RoomScript of Script
	(properties)
	
	(method (doit)
		(super doit:)
		; code executed each game cycle
		(if (not entering)
			(if (& (gEgo onControl:) ctlSILVER)
				(gRoom newRoom: 206)	
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
				(gEgo setMotion: MoveTo 71 129 self)	
			)
			(2
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
(instance waterFall of Prop
	(properties
		y 102
		x 207
		view 103
	)
)
(instance waterRipple of Prop
	(properties
		y 125
		x 199
		view 103
		loop 1
	)
)