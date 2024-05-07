;;; Sierra Script 1.0 - (do not remove this comment)
(script# 241)
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
	
	rm241 0
	
)
(local
	
	fallingDown = 0	
)

(instance rm241 of Rm
	(properties
		picture scriptNumber
		north 242
		east 27
		south 31
		west 0
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript setRegions: 205)
		
		(SetUpEgo)
		(gEgo init:)
		(RunningCheck)
		
		(actionEgo init: hide: ignoreControl: ctlWHITE ignoreActors: setScript: fallScript)	
		(boots init: hide: ignoreActors:)
		(leahSitting init: hide: ignoreControl: ctlWHITE ignoreActors:)	
		
		(switch gPreviousRoomNumber
			
			(27 
				(PlaceEgo 270 170 3)			
			)
			(31
				(PlaceEgo 222 185 3)		
			)
			(242
				(PlaceEgo 153 30 2)	
			)
			(else 
				(RoomScript changeState: 1)	
			)
		)
				
		
	)
)

(instance RoomScript of Script
	(properties)
	
	(method (doit)
		(super doit:)
		(if (& (gEgo onControl:) ctlMAROON)
			(gRoom newRoom: 242)
		)
		
		(if (& (gEgo onControl:) ctlSILVER)	; falling while walking up
			(if (not fallingDown)
				(fallScript changeState: 1)
				(= fallingDown 1)
				(if gAnotherEgo
					(actionEgo view: 449)		
				else
					(actionEgo view: 431)
				)
				(actionEgo setPri: 0)
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
				(actionEgo setPri: 0)
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
			; Leah standing up to put on boots
			(1	(= cycles 10)
				(ProgramControl)
				(gEgo hide:)	
				(boots show:)
				(leahSitting show: posn: 165 188 view: 105 loop: 1 cel: 0)	
			)
			(2
				; standing up
				(leahSitting setCycle: End self cycleSpeed: 4)		
			)
			(3
				; walk to the boots
				(leahSitting view: 106 setCycle: Walk cycleSpeed: 1 setMotion: MoveTo 185 178 self)	
			)
			(4	(= cycles 10) ; stand in front of boots for a moment
				(leahSitting loop: 2 cel: 0)	
			)
			(5	; lift up foot 1
				(leahSitting setCycle: End self cycleSpeed: 4)
			)
			(6	(= cycles 6)
			)
			(7
				(leahSitting loop: 3 cel: 0 setCycle: End self cycleSpeed: 4)	
				(boots cel: 1)
			)
			(8	(= cycles 10) ; stand in front of boots for a moment
			)
			(9
				(leahSitting hide:)
				(gEgo show: posn: (leahSitting x?)(leahSitting y?) loop: 2)
				(boots hide:)
				(PlayerControl)	
			)
		)
	)
)

(procedure (PrintOther textRes textResIndex)
	(if (> (gEgo y?) 120)
		(Print textRes textResIndex #width 280 #at -1 10)
	else
		(Print textRes textResIndex #width 280 #at -1 140)
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
				(actionEgo show: posn: (+ (gEgo x?) 10) (gEgo y?) yStep: 7 setMotion: MoveTo (+ (gEgo x?) 10) 150 self)	
			)
			(2
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
(instance leahSitting of Act
	(properties
		y 60
		x 165
		view 105
	)
)
(instance boots of Prop
	(properties
		y 180
		x 184
		view 105
		loop 2
	)
)