;;; Sierra Script 1.0 - (do not remove this comment)

(script# 5)
(include sci.sh)
(include game.sh)
(use controls)
(use cycle)
(use feature)
(use game)
(use inv)
(use main)
(use obj)

(public
	rm005 0
)

(local
; Frozen Lake



	followed =  0	; creatures come towards you when true
	fallen =  0		; slipped on ice
	fallAgain =  0	; Second fall you do not get up from
	;slowGhost =  0 
	pulledIn =  0	; Swtich to trigger ego Death only ONCE
	
	; used for when Ego walks off ice to send him in correct direction w/ new x/y steps
	myY
	myX
	currentX
	currentY
)

(instance rm005 of Rm
	(properties
		picture scriptNumber
		north 0
		east 0
		south 6
		west 0
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript)
		(switch gPreviousRoomNumber
			(6
				(gEgo posn: 190 182 loop: 3)	
			)
			(else 
				(gEgo posn: 190 182 loop: 3)
			)
		)
		(SetUpEgo)
		(gEgo init:)
		(shadow
			init:
			hide:
			ignoreActors:
			xStep: 1
			yStep: 1
			setScript: shadowScript
		)
		(shadow2
			init:
			hide:
			ignoreActors:
			xStep: 1
			yStep: 1
			setScript: deathScript
		)
		(shadow3 init: hide: ignoreActors: xStep: 1 yStep: 1)
		(alterEgo
			init:
			ignoreActors:
			ignoreControl: ctlWHITE
			hide:
			setScript: tripScript
		)
		(gTheMusic number: 101  loop: -1 play: 
		)
	)
)


(instance RoomScript of Script
	(properties)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		(if (== (pEvent type?) evMOUSEBUTTON)
			(= myX (pEvent x?))
			(= myY (pEvent y?))
			(if (& (pEvent modifiers?) emRIGHT_BUTTON))
		)
		(if (Said 'run') (Print {no can do.}))
		(if (Said 'hi')
			(DoSound sndVOLUME 0)
		)
	)
	
	(method (doit)
		(super doit:)
		(= currentX (gEgo x?))
		(= currentY (gEgo y?))
		(if
			(or
				(<= (gEgo distanceTo: shadow) 5)
				(<= (gEgo distanceTo: shadow2) 5)
				(<= (gEgo distanceTo: shadow3) 5)
			)
			(if (not pulledIn)
				(= pulledIn 1)
				(deathScript changeState: 1)
			)
		)
		(if
			(or
				(== (gEgo onControl:) ctlSILVER)
				(== (gEgo onControl:) ctlGREY)
			)
			(if (not followed) (shadowScript changeState: 1))
			(if (not fallen) (tripScript changeState: 1))
		else
			(if followed
				(= followed 0)
				(shadow hide: posn: 295 95)
				(shadow2 hide: posn: 82 110)
				(shadow3 hide: posn: 190 88)
			)
			(if fallen (-- fallen))
		)
		(if followed
			(if (== (gEgo onControl:) ctlGREY)
				(if (not fallAgain)
					(tripScript changeState: 4)
					(= fallAgain 1)
				)
			)
			(gEgo xStep: 1 yStep: 1)
		else
			(gEgo xStep: 3 yStep: 2)
			(= [gArray 0] myX)
			(= [gArray 1] myY)
			(runProc)
		)
	)
)
                      ; This is here to have Ego sent in motion after leaving the ice
                      
(instance shadowScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)
			(1
				(= cycles 1)
				(= followed 1)
				(if (not pulledIn) ; hiding the shadow if Ego is dying
					(shadow show: setMotion: MoveTo currentX currentY cel: 0)
					(shadow2 show: setMotion: MoveTo currentX currentY cel: 0)
					(shadow3 show: setMotion: MoveTo currentX currentY cel: 0)
				else
					(shadow hide:)
					(shadow2 hide:)
					(shadow3 hide:)
				)
			)
			(2
				(= cycles 2)
				(if followed
					(shadow
						setMotion: MoveTo (shadow x?) (shadow y?)
						cel: 1
					)
					(shadow2
						setMotion: MoveTo (shadow2 x?) (shadow2 y?)
						cel: 1
					)
					(shadow3
						setMotion: MoveTo (shadow3 x?) (shadow3 y?)
						cel: 1
					)
				)
			)
			(3
				(if followed (shadowScript changeState: 1))
			)
		)
	)
)

(instance tripScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(1
				(gTheSoundFX number: 41 play:)
				(= fallen 10)
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo hide:)
				(alterEgo
					show:
					view: 410
					loop: 1
					cel: 0
					posn: (gEgo x?) (gEgo y?)
					setCycle: End tripScript
					cycleSpeed: 1
					ignoreActors:
				)
			)
			(2
				(alterEgo
					view: 409
					loop: 2
					cel: 0
					setCycle: End tripScript
				)
			)
			(3
				(PlayerControl)
				(alterEgo hide:)
				(gEgo show:)
			)
			(4
				(gTheSoundFX number: 41 play:)
				(= fallen 10)
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo hide:)
				(alterEgo
					show:
					view: 410
					loop: 1
					cel: 0
					posn: (gEgo x?) (gEgo y?)
					setCycle: End tripScript
					cycleSpeed: 1
					ignoreActors:
				)
			)
			(5
				(= cycles 50)
; remove cycles and death - cycles only in for testing purposes
				(alterEgo
					view: 409
					loop: 0
					cel: 0
					setCycle: Fwd
					cycleSpeed: 4
				)
			)
			(6
				(if (not pulledIn)
					(PlayerControl)
					(alterEgo hide:)
					(gEgo show:)
				)
			)
		)
	)
)

(instance deathScript of Script
	(properties)
	
	(method (changeState newState &tmp dyingScript)
		(= state newState)
		(switch state
			(1
				(ShakeScreen 1)
				(ProgramControl)
				(shadow hide:)
				(shadow2 hide:)
				(gEgo hide:)
				(alterEgo
					show:
					posn: (gEgo x?) (gEgo y?)
					view: 84
					loop: 0
					cel: 0
					setCycle: End deathScript
					cycleSpeed: 3
				)
			)
			(2
				(= cycles 5)
				(PlayerControl)
			)
			; (SetCursor(999 HaveMouse()) = gCurrentCursor 999)
			(3
				(= gDeathIconEnd 1)
				(= gIconY 32)
				(= dyingScript (ScriptID DYING_SCRIPT))
				(dyingScript
					caller: 602
					register:
						{\nAs the icy creature grasps you all the warmth drains out of your body. Before your mind fades to a frozen blackness you hear the creature say, 'Forgive me, I'm just so cold.'}
				)
				(gGame setScript: dyingScript)
			)
		)
	)
)

(instance shadow of Act
	(properties
		y 95
		x 295
		view 5
	)
)

(instance shadow2 of Act
	(properties
		y 110
		x 82
		view 5
	)
)

(instance shadow3 of Act
	(properties
		y 88
		x 190
		view 5
	)
)

(instance alterEgo of Act
	(properties
		y 180
		x 27
		view 410
		loop 1
	)
)
