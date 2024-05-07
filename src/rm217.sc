;;; Sierra Script 1.0 - (do not remove this comment)
(script# 217)
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
	rm217 0
)

(local
	
	podTaken = 0
	fallingDown = 0	
	
	slippingDown = 0

)


(instance rm217 of Rm
	(properties
		picture scriptNumber
		north 0
		east 13
		south 0
		west 216
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript)
		(switch gPreviousRoomNumber
			(12
				(PlaceEgo 20 110 0)	
			)
			(216
				(PlaceEgo 20 110 0)
			)
			(else 
				(PlaceEgo 20 110 0)
				;(gEgo posn: 150 100 loop: 1)
			)
		)
		(SetUpEgo)
		(gEgo init:)
		
		(actionEgo init: hide: ignoreControl: ctlWHITE ignoreActors: setScript: fallScript)
		(vine init:)
		(pod init:)
	)
)

(instance RoomScript of Script
	(properties)
	
	(method (doit)
		(super doit:)
		; code executed each game cycle
		(if (& (gEgo onControl:) ctlRED)
			(if (not fallingDown)
				(fallScript changeState: 1)
				(= fallingDown 1)
				(if gAnotherEgo
					(actionEgo view: 447 setPri: 1)		
				else
					(actionEgo view: 229 setPri: 1)
				)
			)
		)
		
		(if (& (gEgo onControl:) ctlSILVER)
			(if (not podTaken)
				(if (not slippingDown)
					(fallScript changeState: 4)
					(= slippingDown 1)	
				)
			)
		)
	)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		; handle Said's, etc...
		(if (Said 'take/vine,leaf,pod')	
			(if (not podTaken)	
				(PrintOK)	
			else
				(PrintAlreadyTookIt)
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
				; slipping down the mud
			(4
				(gEgo hide: setMotion: NULL)
				(if gAnotherEgo
					(actionEgo view: 383)
				else
					(actionEgo view: 235)	
				)
				(actionEgo show: posn: (gEgo x?) (gEgo y?) setCycle: Fwd xStep: 1 setMotion: MoveTo 197 150 self)		
			)
			(5
				(if gAnotherEgo
					(self changeState: 8)	
				else
					(actionEgo view: 411 cel: 0 setCycle: End xStep: 6 yStep: 4 setMotion: MoveTo 255 188 self)
				)	
			)
			(6	(= cycles 30)
				(actionEgo hide:)	
			)
			(7
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
				; sliding down if PC is Leah
			(8	; falling into mud
				(actionEgo view: 362 loop: 1 cel: 0 setMotion: NULL setCycle: End self cycleSpeed: 2)	
			)
			(9	; sliding down face in mud
				(actionEgo view: 363 cel: 0 setCycle: End xStep: 6 yStep: 4 setMotion: MoveTo 245 188 self)	
			)
			(10	; back to hide ego and death
				(self changeState: 6)	
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
(instance vine of Prop
	(properties
		y 68
		x 60
		view 100
		loop 0
	)
)
(instance pod of Prop
	(properties
		y 113
		x 90
		view 100
		loop 2
	)
)
