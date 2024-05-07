;;; Sierra Script 1.0 - (do not remove this comment)
(script# 103)
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
	
	rm103 0
	
)

(local

	playerStance = 0
	
	; 0 = default
	; 1 = defending
	; 2 = taking damage
	; 3 = stab
	; 4 = swing
	; 5 = dodge left
	; 6 = dodge right
	
	enemyDefault = 1
	enemyAttackSide = 0
	
	oHealth = 100
	
	; 0 attacks on the left
	; 1 attacks on the right
	
	backToDefaultStance = 0
	stamina = 12
	superStamina = 0
	
	
)


(instance rm103 of Rm
	(properties
		picture scriptNumber 
		north 0
		east 0
		south 0
		west 0
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript)
		(switch gPreviousRoomNumber
			(else 
				(gEgo posn: 150 100 loop: 1)
			)
		)
		(SetUpEgo)
		(gEgo init: hide:)
		(player init: setCycle: Fwd cycleSpeed: 2)
		(staminaWheel init: setPri: 15 cel: stamina setScript: staminaScript)
		
		(playerHealthBar init: cel: (/ (* (/ (* gHlth 100) gMaxHlth) 25) 100) )
		(enemyHealthBar init:)
		
		(playerWeaponTrail init: hide:)
		(enemyWeaponTrail init: hide:)
		
		(criticalHitFlair init: hide: setPri: 6)
		
		(enemyHead init: setPri: 9 setScript: enemyFaceScript)
		(enemyArmRight init: setPri: 7 setScript: opponentAttack)
		(enemyArmLeft init: setPri: 7)
		(enemyRightHand init: setPri: 8 setCycle: Fwd cycleSpeed: 3)
		(enemyLeftHand init: setPri: 8 setCycle: Fwd cycleSpeed: 3)
		
		(opponentAttack changeState: 1)
		(= gHlth 10)
	)
)

(instance RoomScript of Script
	(properties)
	
	(method (doit)
		(super doit:)
		; code executed each game cycle
		(if (> enemyDefault 0) ; IS Enemy in Default position?
			(++ enemyDefault)
			(if (> enemyDefault 25)
				(if (> enemyDefault 28)
					(= enemyDefault 1)
				)
				(enemyHead cel: 1 setCycle: NULL)
			else
			(enemyHead cel: 0)
			)
		)
		
		(if (> backToDefaultStance 0)
			(-- backToDefaultStance)
		else
			(if(> playerStance 0)
				(= playerStance 0)
				(playerStanceAnimation)
			)
		)
		(if (< stamina 12)
			(++ stamina)
			(staminaWheel cel: stamina)	
		) 
	)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		
		(if (== (pEvent type?) evJOYSTICK)
			(if (== (pEvent message?) 1)    ; If pressed the UP arrow
				(if (== stamina 12)
					(if (== backToDefaultStance 0)
						(= playerStance 4)
						(playerStanceAnimation)
					)
				else
					(if superStamina
						(if (== backToDefaultStance 0)
							(= playerStance 4)
							(playerStanceAnimation)
							(= superStamina 0)
							(= stamina 12)
						)	
					)
				)
			)
			(if (== (pEvent message?) 5)     ; If pressed the DOWN arrow
				(if (== stamina 12)
					(if (== backToDefaultStance 0)
						(= playerStance 1)
						(playerStanceAnimation)
					)
				else
					(if superStamina
						(if (== backToDefaultStance 0)
							(= playerStance 1)
							(playerStanceAnimation)
							(= superStamina 0)
							(= stamina 12)
						)	
					)
				)
			)
			(if (== (pEvent message?) 7)     ; If pressed the LEFT arrow
				(if (== stamina 12)
					(if (== backToDefaultStance 0)
						(= playerStance 5)
						(playerStanceAnimation)
					)
				else
					(if superStamina
						(if (== backToDefaultStance 0)
							(= playerStance 5)
							(playerStanceAnimation)
							(= superStamina 0)
							(= stamina 12)
						)	
					)
				)
			)
			(if (== (pEvent message?) 3)     ; If pressed the RIGHT arrow
				(if (== stamina 12)
					(if (== backToDefaultStance 0)
						(= playerStance 6)
						(playerStanceAnimation)
					)
				else
					(if superStamina
						(if (== backToDefaultStance 0)
							(= playerStance 6)
							(playerStanceAnimation)
							(= superStamina 0)
							(= stamina 12)
						)	
					)
				)
			)
		)
		
		(if (== (pEvent type?) evMOUSEBUTTON) 
			(if (& (pEvent modifiers?) emRIGHT_BUTTON)
				(if
					(checkEvent pEvent
						(enemyHead nsLeft?)
						(enemyHead nsRight?)
						(enemyHead nsTop?)
						(enemyHead nsBottom?)
					)
					; Set up the weakened Ego animation
				)	
			)
			(switch playerStance
				(0
					; Blocking
					(player x: (- (player x?) 2))
					(player loop: 1 cel: 0 setCycle: End cycleSpeed: 1)
					(= playerStance 1) 
				)
				(1
					; Damaged
					(player x: (+ (player x?) 12))
					(player loop: 4 setCycle: Fwd cycleSpeed: 1)
					(= playerStance 2)	
				)
				(2
					; Stab
					(player x: (- (player x?) 10))
					(player loop: 2 cel: 0 setCycle: CT)
					(= playerStance 3)				
				)
				(3	; Swing Strike
					(player x: (- (player x?) 30))
					(player loop: 3 cel: 0 setCycle: End cycleSpeed: 2)
					(weaponTrailScript changeState: 1)
					(= playerStance 4)
					(enemyFaceScript changeState: 1)
									
				)
				(4	; Neutral Stance
					(player x: (+ (player x?) 30))
					(player view: 200 loop: 0 setCycle: Fwd cycleSpeed: 2)
					(= playerStance 0)					
				)
				(5	; Dodge Left
					(player x: (- (player x?) 30))
					(player setCycle: End cycleSpeed: 2)
					(= playerStance 5)
				)				
			)
		)
		; handle Said's, etc...
	)
	
	(method (changeState newState dyingScript)
		(= state newState)
		(switch state
			(0 ; Handle state changes
			)
			(1	; PLAYER DEATH
				(player view: 204 x: (+ (player x?) 27) loop: 2 cel: 0 setCycle: End self cycleSpeed: 2)			
			)
			(2
				(= dyingScript (ScriptID DYING_SCRIPT))
				(dyingScript
				caller: 706
				register:
					{You have been bested by your brutal opponent. Better shape up before fighting a battle like this again.}
				)
				(gGame setScript: dyingScript)	
			)
		)
	)
)

(procedure (playerStanceAnimation)
	(switch playerStance
				(0	; Neutral Stance
					;(player x: (+ (player x?) 30))
					(player view: 200 x: 160 loop: 0 setCycle: Fwd cycleSpeed: 2)
					(= playerStance 0)
									
				)
				(1
					; Blocking
					;(player x: (- (player x?) 2))
					(player x: 160 loop: 1 cel: 0 setCycle: End cycleSpeed: 1)
					(= backToDefaultStance 10)
					(staminaScript changeState: 1) 
				)
				(2
					; Damaged

					(player view: 200 x: 172 loop: 4 setCycle: Fwd cycleSpeed: 1)
					(= playerStance 2)
					(= backToDefaultStance 10) 
					(staminaScript changeState: 1)	
				)
				(3
					; Stab
					;(player x: (- (player x?) 10))
					(player loop: 2 cel: 0 setCycle: CT)
					(= playerStance 3)				
				)
				(4	; Swing Strike
					;(player x: (- (player x?) 30))
					(player x: 145 loop: 3 cel: 0 setCycle: End cycleSpeed: 2)
					(weaponTrailScript changeState: 1)
					(enemyFaceScript changeState: 1)
					(staminaScript changeState: 1)
					
					(dealDamage)
					
					(= backToDefaultStance 10)	
									
				)
				(5
					(player x: (- (player x?) 40))
					(player view: 204 loop: 1 cel: 0 setCycle: End cycleSpeed: 1)
					(staminaScript changeState: 1)
					(= backToDefaultStance 5)	
				)
				(6
					(player x: (+ (player x?) 40))
					(player view: 204 loop: 0 cel: 0 setCycle: End cycleSpeed: 1)
					(staminaScript changeState: 1)
					(= backToDefaultStance 5)	
				)
								
			)	
)
	

(instance enemyFaceScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0 
			)
			(1	(= cycles 20) ; Opponent taking Damage
				(ShakeScreen 1)
				(criticalHitFlair cel: 0 show: setCycle: End)
				(= enemyDefault 0)
				(enemyHead loop: 5 cel: 0 setCycle: Fwd cycleSpeed: 3)
				(enemyArmRight posn: 195 88 loop: 7 cel: 0)
				(enemyRightHand posn: 216 55 loop: 9 cel: 0 setCycle: CT)
				
				(enemyArmLeft posn: 130 88 loop: 8 cel: 0)
				(enemyLeftHand posn: 106 54 loop: 10 cel: 0 setCycle: CT)	
			)
			(2	; End damage Animation
				(= enemyDefault 1)
				(enemyHead loop: 0 setCycle: CT)
				(enemyArmRight posn: 191 113 loop: 1 cel: 1)
				(enemyRightHand posn: 194 108 loop: 3 cel: 0 setCycle: Fwd)
				
				(enemyArmLeft posn: 130 113 loop: 2 cel: 1)
				(enemyLeftHand posn: 130 108 loop: 6 cel: 1 setCycle: Fwd)
			
			)
			
		)
	)
)

(instance staminaScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0
			)
			(1
				(if (not superStamina)
					(staminaWheel loop: 0 setCycle: Beg self cycleSpeed: NULL)
					(++ stamina)	; prevents player from double attacks
				else
					(staminaWheel loop: 0 cel: 12 setCycle: NULL)
					(= superStamina 0)
				)	
			)
			(2
				(= stamina 0)	
			)
		)
	)
)

(instance weaponTrailScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0
			)
			(1
				(playerWeaponTrail show: cel: 0 setCycle: End weaponTrailScript)
			)
			(2
				(playerWeaponTrail hide:)	
			)
		)
	)
)

(instance opponentAttack of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0
			)
			(1 (= cycles 2) ; winding up
				(enemyArmRight posn: 195 88 loop: 7 cel: 0 setCycle: NULL)
				(enemyRightHand posn: 216 55 loop: 9 cel: 0 setCycle: NULL)
				
			)
			(2 (= cycles 16)	; telegraphing attack
				(enemyArmRight cel: 1)
				(enemyRightHand loop: 11 cel: 0 posn: 201 56 setCycle: Fwd cycleSpeed: 2)
			)
			(3 (= cycles 5)		; attack
				(ShakeScreen 1)
				(enemyWeaponTrail show: cel: 0 setCycle: End)
				(enemyArmRight loop: 13 cel: 0 posn: 159 113 setPri: 9)
				(enemyRightHand loop: 12 cel: 0 posn: 126 134 setCycle: NULL)
				
				(if (not (== playerStance 1)) ; NOT blocking? 
					(if (== playerStance 5) ; dodging left
						(if (not enemyAttackSide) ; enemy attacking on the left
							(= superStamina 1); boost stamina
							(staminaWheel loop: 1 setCycle: Fwd cycleSpeed: 1)
						else
							(takeDamage)
						)
					else
						(if (== playerStance 6) ; dodging right
							(if enemyAttackSide 
								(= superStamina 1); boost stamina
								(staminaWheel loop: 1 setCycle: Fwd cycleSpeed: 1)
							else
								(takeDamage)
							)	
						else	; if not blocking, and not successfully dodging either direction
							(takeDamage)
						)
					)
				)
			)
			(4 (= cycles (Random 40 100))
				(enemyArmRight posn: 191 113 loop: 1 cel: 1 setPri: 7)
				(enemyRightHand posn: 194 108 loop: 3 cel: 0 setCycle: Fwd)
			)
			(5
				(self changeState: 1)	
			)
		)
	)
)

(procedure (dealDamage)
	
	(= oHealth (- oHealth (+ gStr (/ gStr 2))))
	;(enemyHealthBar cel: (/ (* 25 (/ (* 100 oHealth) gBatNum)) 100))
	
	(if (< oHealth 1)
		(enemyHealthBar cel: 0)
		(Print " YOU WIN!")	
	)
)
(procedure (takeDamage)
	
	
	
	(= gHlth (- gHlth 3))
	(playerHealthBar cel: (/ (* (/ (* gHlth 100) gMaxHlth) 25) 100) )
	; Make sure health bar doesn't visually show nothing is health's division leads to lower than .5, but higher than 0
	(if (and (== (playerHealthBar cel?) 0) (> gHlth 0))
		(playerHealthBar cel: 1)		
	)
	
	; PLAYER DEATH
	(if (< gHlth 1)
		(playerHealthBar cel: 0)
		(RoomScript changeState: 1)	; player falling animation
	else
		(= playerStance 2)
		(= superStamina 0)
		(playerStanceAnimation)
	)
	
)
	
(procedure (checkEvent pEvent x1 x2 y1 y2)
	(if
		(and
			(> (pEvent x?) x1)
			(< (pEvent x?) x2)
			(> (pEvent y?) y1)
			(< (pEvent y?) y2)
		)
		(return TRUE)
	else
		(return FALSE)
	)
)

(instance enemyHealthBar of Prop
	(properties
		y 35
		x 265
		view 203
		loop 2
		cel 25
	)
)
(instance playerHealthBar of Prop
	(properties
		y 35
		x 50
		view 202
		loop 2
		cel 25
	)
)
(instance staminaWheel of Prop
	(properties
		y 160
		x 230
		view 203
		cel 12
	)
)
(instance playerWeaponTrail of Prop
	(properties
		y 165
		x 143
		view 201
		loop 0
	)
)
(instance enemyWeaponTrail of Prop
	(properties
		y 125
		x 170
		view 201
		loop 2
	)
)
(instance player of Prop
	(properties
		y 179
		x 160
		view 200
		loop 0
	)
)
(instance enemyHead of Prop
	(properties
		y 75
		x 160
		view 205
		loop 0
	)
)
(instance enemyArmRight of Prop
	(properties
		y 113
		x 191
		view 205
		loop 1
		cel 1
	)
)
(instance enemyRightHand of Prop
	(properties
		y 108
		x 194
		view 205
		loop 3
	)
)
(instance enemyArmLeft of Prop
	(properties
		y 113
		x 130
		view 205
		loop 2
		cel 1
	)
)
(instance enemyLeftHand of Prop
	(properties
		y 108
		x 130
		view 205
		loop 6
		cel 1
	)
)
(instance criticalHitFlair of Prop
	(properties
		y 143
		x 158
		view 201
		loop 1
	)
)
(instance bigHand of Prop
	(properties
		y 134
		x 126
		view 205
		loop 12
	)
)
(instance bigArm of Prop
	(properties
		y 113
		x 159
		view 205
		loop 13
	)
)

