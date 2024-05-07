;;; Sierra Script 1.0 - (do not remove this comment)
(script# 50)
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
	
	rm050 0
	
)
(local
	
	animation = 0 ; if true, player doesn't accidentily die from walking on certain control lines
	
	statueUp = 0
	smashing = 0
	
	fallingDown = 0	

)

(instance rm050 of Rm
	(properties
		picture scriptNumber
		north 0
		east 57
		south 0
		west 213
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript)
		(switch gPreviousRoomNumber
			
			(57 
				(PlaceEgo 290 174 1)
			)
			(213 
				(PlaceEgo 10 47 0)	
			)
			(else 
				(PlaceEgo 10 47 0)
			)
		)
		(SetUpEgo)
		(gEgo init:)
		(RunningCheck)
		
		(actionEgo init: hide: ignoreControl: ctlWHITE ignoreActors: setScript: fallScript)
		(petrasaur init: setScript: petraScript)
		(flash init: hide:)
		(poof init: hide: ignoreActors:)
		(rope init: loop: 0 ignoreActors: setPri: 0 setScript: ropeScript)
		
		(if (== (IsOwnedBy 15 50) FALSE)
			(rope hide:)
		)
	)
)

(instance RoomScript of Script
	(properties)
	
	(method (doit)
		(super doit:)
		; code executed each game cycle
		
		(if (& (gEgo onControl:) ctlRED)	; falling down with priority considerations
			(if (not animation)
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
		)
		
		(if (== statueUp 1)
			(if (<= (gEgo distanceTo: petrasaur) 20)
				(if (== smashing FALSE)
					(petraScript changeState: 4)
					(= smashing TRUE)
				)
			)	
		else
			(if (<= (gEgo distanceTo: petrasaur) 20)
				(if (== statueUp FALSE)
					(petraScript changeState: 2)
					(= statueUp TRUE)
				)
			)
		)
	)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		; handle Said's, etc...
		(if(Said 'hello')
			(petraScript changeState: 2)	
		)
		(if(Said 'tie/rope')
			(if (gEgo has: 15)
				(self changeState: 1)	; tie rope animation
			else
				(if (== (IsOwnedBy 15 50) TRUE)	; rope is already tied
					(PrintOther 50 1)	
				else
					(PrintOther 50 2)
				)
			)	
		)
		(if (Said 'climb')
			(if (== (IsOwnedBy 15 50) FALSE)
				
			else
				(if (< (gEgo y?) 105)	
					; climb down
					(ropeScript changeState: 1)	
				else
					; climb up
					(ropeScript changeState: 5)	
				)
				
			)	
		)
	)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0 ; Handle state changes
				
			)
			(1	; tossing down the rope
				(ProgramControl)
				(= animation 1)
				(gEgo setMotion: MoveTo 48 51 self)	
			)
			(2
				(gEgo hide:)
				(actionEgo show: posn: (gEgo x?)(gEgo y?) view: 456 loop: 0 cel: 0 setCycle: End self cycleSpeed: 3)	
			)
			(3
				(actionEgo view: 1 loop: 2)	
				(rope show: x: 80 loop: 1 cel: 0 setCycle: End self cycleSpeed: 2)
			)
			(4
				(PlayerControl)
				(actionEgo hide:)
				(gEgo show: put: 15 50 loop: 2)	
				(rope loop: 0 cel: 0 x: 48)
				(= animation 0)
			)
		)
	)
)

(instance ropeScript of Script
	(properties)
	
	(method (changeState newState &tmp dyingScript)
		(= state newState)
		(switch state
			(0)
			(1	; climbing down the rope
				(ProgramControl)
				(gEgo setMotion: MoveTo 48 51 self)
			)
			(2
				(gEgo hide:)
				(actionEgo show: posn: (gEgo x?) (+ (gEgo y?) 15) view: 385 setCycle: Walk cycleSpeed: 1 yStep: 3 setMotion: MoveTo (rope x?) 175 self)
				(rope loop: 0 setCycle: Fwd cycleSpeed: 3)
			)
			(3
				(gEgo show: posn: (actionEgo x?)(actionEgo y?) setMotion: MoveTo 99 169 self)
				(actionEgo hide:)
				(rope setCycle: CT)	
			)
			(4
				(PlayerControl)

			)
			; Climbing up the Rope
			(5
				(ProgramControl)
				(gEgo setMotion: MoveTo 48 171 self)
			)
			(6
				(gEgo hide:)
				(actionEgo show: posn: (gEgo x?) (+ (gEgo y?) 15) view: 385 setCycle: Walk cycleSpeed: 1 yStep: 3 setMotion: MoveTo (rope x?) 51 self)
				(rope loop: 0 setCycle: Fwd cycleSpeed: 3)
			)
			(7	(= cycles 2)
				(gEgo show: loop: 2 posn: (actionEgo x?)(actionEgo y?))
				(actionEgo hide:)	
				(rope setCycle: CT)	
			)
			(8
				(PlayerControl)

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
				(actionEgo show: posn: (+ (gEgo x?) 10) (gEgo y?) yStep: 7 setMotion: MoveTo (+ (gEgo x?) 10) 183 self)	
			)
			(2	(= cycles 20)
				(ShakeScreen 1)
				(if gAnotherEgo
					(actionEgo view: 382 loop: 0 cel: 3 setCycle: NULL)
				else
					(actionEgo view: 409 loop: 0 cel: 1 setCycle: NULL)	
				)
			)
			(3
				
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
(instance petraScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)
			(1 
				(gEgo setMotion: MoveTo (- (petrasaur x?) 5)(+ (petrasaur y?) 10) self)
			)
			(2 (= cycles 15)
				(petrasaur setCycle: Fwd cycleSpeed: 2)
			)
			(3
				(petrasaur loop: 1 setCel: 0 setCycle: End)
				(= statueUp 1)		
			)
			(4
				(petrasaur loop: 2 setCel: 0 setCycle: End self cycleSpeed: 4)
			)
			(5 (= cycles 15)
				; time for windup before smash
			)
			(6 (= cycles 25)
				(petrasaur loop: 3 setCel: 0)
				(flash show: cel: 0 setCycle: End cycleSpeed: 1)
				(poof show: cel: 0 setCycle: End cycleSpeed: 1)
				(ShakeScreen 1)	
			)
			(7
				(petrasaur loop: 1 cel: 1 setCycle: End)
				(= smashing 0)
				(= statueUp 1)
			)
		)
	)
)

(procedure (PrintOther textRes textResIndex)
	(if (> (gEgo y?) 120)
		(Print textRes textResIndex	#width 280 #at -1 10)
	else
		(Print textRes textResIndex	#width 280 #at -1 140)
	)
)

(instance actionEgo of Act
	(properties
		y 60
		x 165
		view 229
	)
)

(instance petrasaur of Prop
	(properties
		y 160
		x 265
		view 15
	)
)
(instance flash of Prop
	(properties
		y 140
		x 258
		view 15
		loop 4
	)
)
(instance poof of Prop
	(properties
		y 159
		x 245
		view 15
		loop 5
	)
)
(instance rope of Prop
	(properties
		y 170
		x 48
		view 104
	)
)
