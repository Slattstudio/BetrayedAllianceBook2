;;; Sierra Script 1.0 - (do not remove this comment)
; Goblin with knife Battle
(script# 100)
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
	
	rm100 0
	
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
	
	oHealth = 160
	enemyDefault = 1
	swappedNum = 0
	enemyAttackSide = 0	
	
	enemyCounterAttack = 0
	
	; 0 attacks on the left
	; 1 attacks on the right
	
	backToDefaultStance = 0
	hitPercent = 40
	stamina = 12
	;dodged = 0
	superStamina = 0
	
	gameWin = 0
	
	textMessage = 0 ; if non-zero, won't double up on message so the saved pixels won't be lost
	lastMessage = 0
	htext1
	htext2
	htext3
	htext4
	
	textVisible = 0
	
)


(instance rm100 of Rm
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
		(enemyHealthBar init: cel: (/ (* (/ (* oHealth 100) 160) 25) 100))
		
		(redWheel init: setPri: 14)
		(greenWheel init: setPri: 15)
		(greyWheel init: setPri: 14)
		
		(playerWeaponTrail init: hide:)
		(enemyWeaponTrail init: hide: setScript: weaponTrailScript)
		
		(criticalHitFlair init: hide: setPri: 6 setScript: enemyFaceScript)
		(ground init: setPri: 6)
		
		(tutor init: loop: 1 setCycle: Fwd cycleSpeed: 8 setScript: tutorScript)
		(speechBubble init: hide:)
		(tutorScript changeState: 5)
		
		(enemyBody init: setPri: 9 setCycle: Fwd cycleSpeed: 4)
		;(enemyHead init: setPri: 9 )
		(enemyArmRight init: setPri: 10 setScript: opponentAttack)
		(enemyArmLeft init: setPri: 10)
		;(enemyRightHand init: setPri: 8 setCycle: Fwd cycleSpeed: 3)
		;(enemyLeftHand init: setPri: 8 setCycle: Fwd cycleSpeed: 3)
		
		(opponentAttack changeState: 3)	; "normal stance"
		
		(= gArcStl 1)
		
		(= gHlth 30)
		(gTheMusic number: 11 loop: -1 priority: -1 play:)	
		 
	)
)

(instance RoomScript of Script
	(properties)
	
	(method (doit)
		(super doit:)
		; code executed each game cycle
		
		; set up displays in the doit as restoring seems to make them disappear
		(if (not textVisible)
			(myDisplay {Health} 4 24 18 9)
			;(Display
			;	{Health}
			;	dsFONT 4
			;	dsCOORD 24 18
			;	dsCOLOUR 9 ; blue
			;	dsBACKGROUND clTRANSPARENT
			;)
			(myDisplay {Accuracy} 4 24 39 11)
			;(Display
			;	{Accuracy}
			;	dsFONT 4
			;	dsCOORD 24 39
			;	dsCOLOUR 11 ; Cyan
			;	dsBACKGROUND clTRANSPARENT
			;)
			(myDisplay {Enemy} 4 262 18 12)
			;(Display
			;	{Enemy}
			;	dsFONT 4
			;	dsCOORD 262 18
			;	dsCOLOUR 12 ; red
			;	dsBACKGROUND clTRANSPARENT
			;)
			(= textVisible 1)
		)
		
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
		(if (> gHlth 0)
			(if (> backToDefaultStance 0)
				(-- backToDefaultStance)
			else
				(if (> playerStance 0)
					(= playerStance 0)
					(playerStanceAnimation)
				)
			)
		)
		(if (< stamina 12)
			;(if dodged
			;	(= stamina 12)
			;else
				(++ stamina)				
			;)
			(staminaWheel cel: stamina)	
		)
		(greenWheel cel: (/ hitPercent 4))
		(if (> (greenWheel cel?) 20)
			(greyWheel cel: 25)
			else
			(greyWheel cel: (/ (+ hitPercent 20) 4)) 
		)
		(if (> hitPercent 60)
			(-- hitPercent)	
		)
	)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		
		(if (== (pEvent type?) evJOYSTICK)
			(if (not gameWin)
			(if (== (pEvent message?) 1)    ; If pressed the UP arrow
				(if (== stamina 12)
					(if (== playerStance 0)
						(stanceCheck 4)
						;(if (== backToDefaultStance 0)
						;	(= playerStance 4)
						;	(playerStanceAnimation)
						;)
					)
				else
					(if superStamina
						(if (== backToDefaultStance 0)
							(= playerStance 4)
							(playerStanceAnimation)
							(= superStamina 0)
							;(= stamina 12)
						)	
					)
				)
			)
			(if (== (pEvent message?) 5)     ; If pressed the DOWN arrow
				(if (== stamina 12)
					(stanceCheck 1)
					;(if (== backToDefaultStance 0)
					;	(= playerStance 1)
					;	(playerStanceAnimation)
					;)
				else
					(stanceCheckSuper 1)	
					;(if superStamina
					;	(if (== backToDefaultStance 0)
					;		(= playerStance 1)
					;		(playerStanceAnimation)
					;		(= superStamina 0)
					;		(= stamina 12)
					;	)	
					;)
				)
			)
			(if (== (pEvent message?) 7)     ; If pressed the LEFT arrow
				(if (== stamina 12)
					(stanceCheck 5)
					;(if (== backToDefaultStance 0)
					;	(= playerStance 5)
					;	(playerStanceAnimation)
					;)
				else
					(stanceCheckSuper 5)	
					;(if superStamina
					;	(if (== backToDefaultStance 0)
					;		(= playerStance 5)
					;		(playerStanceAnimation)
					;		(= superStamina 0)
					;		(= stamina 12)
					;	)	
					;)
				)
			)
			(if (== (pEvent message?) 3)     ; If pressed the RIGHT arrow
				(if (== stamina 12)
					(stanceCheck 6)
					;(if (== backToDefaultStance 0)
					;	(= playerStance 6)
					;	(playerStanceAnimation)
					;)
				else
					(stanceCheckSuper 6)	
					;(if superStamina
					;	(if (== backToDefaultStance 0)
					;		(= playerStance 6)
					;		(playerStanceAnimation)
					;		(= superStamina 0)
					;		(= stamina 12)
					;	)	
					;)
				)
			)
			) ; end gameWin condition
		)
	)
	
	(method (changeState newState dyingScript)
		(= state newState)
		(switch state
			(0 (= cycles 1) ; Handle state changes
				
			)
			(1
				(SetCursor 998 (HaveMouse))
				(= gCurrentCursor 998)	
			)
			(3	; PLAYER DEATH
				(ProgramControl)
				(player view: 204 x: (+ (player x?) 27) loop: 2 cel: 0 setCycle: End self cycleSpeed: 2)			
			)
			(4
				(if (not [gDeaths 1])	; Defeated in Battle
					(++ gUniqueDeaths)
				)
				(++ [gDeaths 1])
				
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
			(if superStamina
				(staminaScript changeState: 3)
			else
				(staminaScript changeState: 1) 
			)
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
			(playerAttack)					
			(= backToDefaultStance 10)
			(if superStamina
				(staminaScript changeState: 3)
			else
				(staminaScript changeState: 1) 
			)	
							
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
				;(= enemyStateVar (opponentAttack state?))
				(opponentAttack cycles: 0)
				
				(ShakeScreen 1)
				(criticalHitFlair cel: 0 show: setCycle: End)
				(= enemyDefault 0)
				(enemyBody loop: 13 cel: 0 setCycle: Fwd cycleSpeed: 3)
				
				(if enemyAttackSide
					(enemyArmRight posn: 205 88 loop: 14 cel: 1 setCycle: NULL)
					(enemyArmLeft posn: 105 83 loop: 15 cel: 0 setCycle: NULL)
				else
					(enemyArmRight posn: 205 88 loop: 14 cel: 0 setCycle: NULL)
					(enemyArmLeft posn: 105 83 loop: 15 cel: 1 setCycle: NULL)
				)
			)
			(2	; End damage Animation
				(= enemyDefault 1)
				(enemyBody posn: 160 155 loop: 0 cel: 0 setCycle: Fwd cycleSpeed: 3)
				(if enemyAttackSide ; Right side
					(enemyArmRight posn: 188 113 loop: 2 cel: 0 setCycle: Fwd cycleSpeed: 3) ; holding knife
					(enemyArmLeft posn: 113 107 loop: 4 cel: 0 setCycle: Fwd cycleSpeed: 3)
				else
					(enemyArmLeft posn: 121 111 loop: 1 cel: 0 setCycle: Fwd cycleSpeed: 3)	; holding knife
					(enemyArmRight posn: 198 111 loop: 3 cel: 0 setCycle: Fwd cycleSpeed: 3)
				)
				;(enemyHead loop: 0 setCycle: CT)
				;(enemyArmRight posn: 191 113 loop: 1 cel: 1)
				;(enemyRightHand posn: 194 108 loop: 3 cel: 0 setCycle: Fwd)
				
				;(enemyArmLeft posn: 130 113 loop: 2 cel: 1)
				;(enemyLeftHand posn: 130 108 loop: 6 cel: 1 setCycle: Fwd)
				;(FormatPrint "state %u" enemyStateVar)
				(opponentAttack changeState: 1)
			
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
					;(= dodged 0)
					(= stamina 12)
				)					
			)
			(2
				(= stamina 0)	
			)
			(3
				(staminaWheel loop: 0 setCycle: Beg self cycleSpeed: NULL)
				(++ stamina)	
			)
			(4
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
	
	(method (changeState newState swapVar)
		(= state newState)
		(switch state
			(0
			)
			(1 (= cycles 11) ; winding up
				(= swapVar (Random 1 100))
				
				(if swappedNum	; makes it a super low chance of double or more swaps
					(= swappedNum 0)
					(= swapVar (Random 1 52))	
				)
				(if (> swapVar 50)	; swap weapon
					(self changeState: 5)	
				else	; attack
					(if enemyAttackSide ; Right side
						(enemyArmRight posn: 195 88 loop: 11 cel: 0 setCycle: NULL)
						;(enemyRightHand posn: 216 55 loop: 9 cel: 0 setCycle: NULL)
					else
						(enemyArmLeft posn: 116 80 loop: 12 cel: 0 setCycle: NULL)
						;(enemyRightHand posn: 216 55 loop: 9 cel: 0 setCycle: NULL)
					)
				)
			)
			;(2 (= cycles 16)	; telegraphing attack
				;(enemyArmRight cel: 1)
				;(enemyRightHand loop: 11 cel: 0 posn: 201 56 setCycle: Fwd cycleSpeed: 2)
			;)
			(2 (= cycles 5)		; attack
				(if (> gHlth 0)
				
					(if enemyAttackSide ; Right side
						(enemyArmRight loop: 10 cel: 0 posn: 170 125)
						;(enemyRightHand posn: 216 55 loop: 9 cel: 0 setCycle: NULL)
					else				; left side
						(enemyArmLeft loop: 9 cel: 0 posn: 139 121)
						;(enemyRightHand posn: 216 55 loop: 9 cel: 0 setCycle: NULL)
					)
					;(enemyWeaponTrail show: cel: 0 setCycle: End)				
					;(enemyRightHand loop: 12 cel: 0 posn: 126 134 setCycle: NULL)
					
					(if (not (== playerStance 1)) ; NOT blocking? 
						(if (== playerStance 6) ; dodging left
							(if (not enemyAttackSide) ; enemy attacking on the left
								(= superStamina 1); boost stamina
								;(= dodged 1)
								(= hitPercent 100)
								(staminaWheel loop: 1 setCycle: Fwd cycleSpeed: 1)
							else
								(takeDamage)
							)
						else
							(if (== playerStance 5) ; dodging right
								(if enemyAttackSide 
									(= superStamina 1); boost stamina
									;(= dodged 1)
									(= hitPercent 100)
									(staminaWheel loop: 1 setCycle: Fwd cycleSpeed: 1)
								else
									(takeDamage)
								)	
							else	; if not blocking, and not successfully dodging either direction
								(takeDamage)
							)
						)
					else 	; BLOCKING
						
						(blockDamage)
					)
				)
			)
			(3 	; Back to normal stance
				(= cycles (Random 20 50))
				(if enemyAttackSide ; Right side
					(enemyArmRight show: posn: 188 113 loop: 2 cel: 0 setCycle: Fwd cycleSpeed: 3) ; holding knife
					(enemyArmLeft show: posn: 113 107 loop: 4 cel: 0 setCycle: Fwd cycleSpeed: 3)
				else
					(enemyArmLeft show: posn: 121 111 loop: 1 cel: 0 setCycle: Fwd cycleSpeed: 3)	; holding knife
					(enemyArmRight show: posn: 198 111 loop: 3 cel: 0 setCycle: Fwd cycleSpeed: 3)
				)
				
			)
			(4
				(self changeState: 1)	
			)
			(5	; swapping weapon
				(if enemyAttackSide	; enemy holding right side
					(enemyArmRight posn: 168 116 loop: 6 cel: 0 setCycle: End self cycleSpeed: 3)
				else
					(enemyArmLeft posn: 145 111 loop: 8 cel: 0 setCycle: End self cycleSpeed: 3)
				)
				
			)
			(6 	(= cycles 5)
					
			)
			(7
				(if enemyAttackSide	; toss it to other hand
					(enemyArmRight posn: 166 116 loop: 5 cel: 0 setCycle: End self cycleSpeed: 3)
				else
					(enemyArmLeft posn: 147 112 loop: 7 cel: 0 setCycle: End self cycleSpeed: 3)
				)
				(if enemyAttackSide		; sets varibles for which hand holds knife
					(-- enemyAttackSide)
				else
					(++ enemyAttackSide)
				)
			)
			(8
				(if (not enemyAttackSide)	; receive dagger
					(enemyArmLeft posn: 145 111 loop: 8 cel: 0 setCycle: End self cycleSpeed: 3)
					(enemyArmRight posn: 201 114 loop: 3 cel: 0 setCycle: Fwd cycleSpeed: 3)
				else
					(enemyArmRight posn: 168 116 loop: 6 cel: 0 setCycle: End self cycleSpeed: 3)
					(enemyArmLeft posn: 113 107 loop: 4 cel: 0 setCycle: Fwd cycleSpeed: 3)
				)		
			)
			(9
				;(if enemyAttackSide		; sets varibles for which hand holds knife
				;	(-- enemyAttackSide)
				;else
				;	(++ enemyAttackSide)
				;)
				
				; visually sent enemy back to "normal" position
				(if enemyAttackSide ; Right side
					(enemyArmRight posn: 188 113 loop: 2 cel: 0 setCycle: Fwd cycleSpeed: 3) ; holding knife
				
				else
					(enemyArmLeft posn: 121 111 loop: 1 cel: 0 setCycle: Fwd cycleSpeed: 3)	; holding knife
					
				)
				(= swappedNum 1)
				(self changeState: 1)	
			)
			(10 ; Oppoent dodge and strike
				(player x: 145 loop: 3 cel: 0 setCycle: End cycleSpeed: 2)
				(weaponTrailScript changeState: 1)
				;(enemyFaceScript changeState: 1)
				(staminaScript changeState: 1)
				
				(enemyBody posn: (- (enemyBody x?) 30) (- (enemyBody y?) 10) loop: 17 cel: 0 setCycle: End self cycleSpeed: 1)
				(enemyArmLeft hide:)
				(enemyArmRight hide:)
			)
			(11 (= cycles 5)
				; short pause		
			)
			(12	; enemy swipes
				(enemyBody posn: (+ (enemyBody x?) 50) (+ (enemyBody y?) 20) loop: 18 cel: 0 setCycle: End self)
				(enemyWeaponTrail show: cel: 0 setCycle: End)
				(= enemyCounterAttack 1)
				(takeDamage)
					
			)
			(13 (= cycles 5)
			`	
			)
			(14
				(= enemyCounterAttack 0)
				(self changeState: 3)
				(enemyBody posn: (- (enemyBody x?) 20) (- (enemyBody y?) 10) loop: 0 cel: 0 setCycle: Fwd cycleSpeed: 4)	
			)
			(15 ; Oppoent dodge
				(player x: 145 loop: 3 cel: 0 setCycle: End cycleSpeed: 2)
				(weaponTrailScript changeState: 1)
				;(enemyFaceScript changeState: 1)
				(staminaScript changeState: 1)
				
				(enemyBody posn: (- (enemyBody x?) 3) (- (enemyBody y?) 5) loop: 19 cel: 0 setCycle: End self cycleSpeed: 3)
				(enemyArmLeft hide:)
				(enemyArmRight hide:)
			)
			(16 (= cycles 8)
				; short pause		
			)
			(17
				(self changeState: 3)
				(enemyBody posn: (+ (enemyBody x?) 3) (+ (enemyBody y?) 5) loop: 0 cel: 0 setCycle: Fwd cycleSpeed: 4)
			)
			(18
				(enemyBody view: 210 loop: 8 cel: 0 setCycle: End self cycleSpeed: 2)
				(enemyArmLeft hide:)
				(enemyArmRight hide:)
				(gTheMusic number: 12 loop: 0 priority: -1 play:)
			)
			(19 (= cycles 7)
			;	(Print "You did it!")	
			)
			(20 
				(= gArcStl 0)
				(if (> gHlth (/ (* 75 gMaxHlth) 100))
					(= gWndColor 0)
					(= gWndBack 14)
					(Print 100 0 #at -1 26)
				else
					(if (> gHlth (/ (* 25 gMaxHlth) 100))
						(= gWndColor 0)
						(= gWndBack 13)
						(Print 100 1 #at -1 26)
					else
						(= gWndColor 0)
						(= gWndBack 12)
						(Print 100 2 #at -1 26)
					)
				)
				(= gWndColor 0)
				(= gWndBack 15)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)	
				(gRoom newRoom: gPreviousRoomNumber)	
			)
		)
	)
)

(instance tutorScript of Script 
	(properties)
	
	(method (changeState newState swapVar)
		(= state newState)
		(switch state
			(0
			)
			(1	(= seconds 6)				
				
				(tutor loop: 0 cel: 0 cycleSpeed: 3)
				(speechBubble show:)
				
				(= htext1 (myDisplay {Dodge < or > to} 4 216 45 9)
				)
				(= htext2 (myDisplay {regain accuracy} 4 216 53 9) 
				)
				(= htext3 (myDisplay {$ blocks all angles} 4 216 65 9) 
				)
				(= htext4 (myDisplay "{ reduces damage" 4 216 73 9) 
				)
				(= textMessage 1)	
			)
			(2	
				(removeMessage)		
			)
			(3	(= seconds 6)
				(tutor loop: 0 cel: 0 cycleSpeed: 3)
				(speechBubble show:)
				
				(= htext1 (myDisplay {Enemies counter-} 4 216 47 12) 
				)
				(= htext2 (myDisplay {attack when} 4 216 55 12)  
				)
				(= htext3 (myDisplay {accuracy gauge} 4 216 63 12)  
				)
				(= htext4 (myDisplay {leans red} 4 216 71 12) 
				)
				(= textMessage 2)	
			)
			(4
				(removeMessage)
			)
			(5	(= cycles 1)
				; set up and additional cycle so displays show on restore
			)
			(6
				(= seconds 6)
				(tutor loop: 0 cel: 0 cycleSpeed: 3)
				(speechBubble show:)
				
				(= htext1 (myDisplay {^ Attacks...} 4 216 47 14) )
				(= htext2 (myDisplay {All actions} 4 216 55 14) )
				(= htext3 (myDisplay {deplete your} 4 216 63 14) )
				(= htext4 (myDisplay {stamina wheel} 4 216 71 14) )
				(= textMessage 3)	
			)
			(7
				(removeMessage)
			)
		)
	)
)

(procedure (myDisplay text font coordx coordy color)
	(Display
		text
		dsFONT font
		dsCOORD coordx coordy
		dsCOLOUR color ; yellow
		dsBACKGROUND clTRANSPARENT
		dsSAVEPIXELS
	)	
)
(procedure (removeMessage)
	(tutor loop: 1 cel: 0 cycleSpeed: 8)
	(speechBubble hide:)
	(Display {} dsRESTOREPIXELS htext1)
	(Display {} dsRESTOREPIXELS htext2)	
	(Display {} dsRESTOREPIXELS htext3)	
	(Display {} dsRESTOREPIXELS htext4)
	(= textMessage 0)
)
(procedure (playerAttack var)
	(= var (Random 1 100))
	(if (> var hitPercent)
		(if (>  var (+ hitPercent 20))
		; enemy dodge/block
		(opponentAttack cycles: 0 changeState: 10)
		else
		; opponent blocks but not counter
			(opponentAttack cycles: 0 changeState: 15)
		)		
	else
		(player x: 145 loop: 3 cel: 0 setCycle: End cycleSpeed: 2)
		(weaponTrailScript changeState: 1)
		(enemyFaceScript changeState: 1)
		(staminaScript changeState: 1)
					
		(dealDamage)
	)
	
	
)

(procedure (stanceCheck newStance)	
	(if (== backToDefaultStance 0)
		(= playerStance newStance)
		(playerStanceAnimation)
	)	
)
(procedure (stanceCheckSuper newStance)	
	(if superStamina
		(if (== backToDefaultStance 0)
			(= playerStance newStance)
			(playerStanceAnimation)
			(= superStamina 0)
			(= stamina 12)
		)	
	)	
)	

(procedure (dealDamage)
	
	(= oHealth (- oHealth (+ gStr (/ gStr 2))))
	;(enemyHealthBar cel: (/ (* 25 (/ (* 100 oHealth) gBatNum)) 100))
	(enemyHealthBar cel: (/ (* (/ (* 100 oHealth) 160) 25) 100))
	
	(if (and (== (enemyHealthBar cel?) 0) (> oHealth 0))
		(enemyHealthBar cel: 1)		
	)
	
	(if (< oHealth 1)
		(enemyHealthBar cel: 0)
		(enemyFaceScript cycles: 0)
		(opponentAttack cycles: 0 changeState: 18)
		(staminaWheel hide:)
		
		(= gameWin 1)
		;(Print " YOU WIN!")	
	)
)
(procedure (takeDamage)
	
	
	(ShakeScreen 1)
	
	(= hitPercent 30)
	(= gHlth (- gHlth (Random 2 4)) )
	(playerHealthBar cel: (/ (* (/ (* gHlth 100) gMaxHlth) 25) 100) )
	
; Make sure health bar doesn't visually show nothing is health's division leads to lower than .5, but higher than 0
	(if (and (== (playerHealthBar cel?) 0) (> gHlth 0))
		(playerHealthBar cel: 1)		
	)
	
	(if (not textMessage)
		;(= rando (Random [odds 0] [odds 1]))
		(if enemyCounterAttack
			(if (not lastMessage)
				(tutorScript seconds: 0 cycles: 0 changeState: 3)
				(= lastMessage 1)
			else
				(tutorScript seconds: 0 cycles: 0 changeState: 1)
				(= lastMessage 0)	
			)
		else
			(tutorScript seconds: 0 cycles: 0 changeState: 1)
		)
	)
		
	; PLAYER DEATH
	(if (< gHlth 1)
		(playerHealthBar cel: 0)
		(staminaWheel hide:)
		(= gameWin 2) ; loss
		(RoomScript changeState: 3)	; player falling animation
	else
		(= playerStance 2)
		;(= superStamina 0)
		(playerStanceAnimation)
	)	
)

(procedure (blockDamage)
	(ShakeScreen 1)	
	(= hitPercent (+ hitPercent 10)	); add hit percent with block
	(= gHlth (- gHlth 1))
	(playerHealthBar cel: (/ (* (/ (* gHlth 100) gMaxHlth) 25) 100) )
	
	
; Make sure health bar doesn't visually show nothing is health's division leads to lower than .5, but higher than 0
	(if (and (== (playerHealthBar cel?) 0) (> gHlth 0))
		(playerHealthBar cel: 1)		
	)
	
	; PLAYER DEATH
	(if (< gHlth 1)
		(playerHealthBar cel: 0)
		(RoomScript changeState: 3)	; player falling animation
	else
		;(= playerStance 2)
		;(= superStamina 0)
		;(playerStanceAnimation)
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
(instance redWheel of Prop
	(properties
		y 56
		x 50
		view 196
		loop 1	
	)
)
(instance greenWheel of Prop
	(properties
		y 56
		x 50
		view 196
		loop 0	
	)
)
(instance greyWheel of Prop
	(properties
		y 56
		x 50
		view 195
		loop 0	
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
		loop 3
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
(instance enemyBody of Prop
	(properties
		y 155
		x 160
		view 208
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
		x 200
		view 208
		loop 3
		;cel 1
	)
)
(instance enemyArmLeft of Prop
	(properties
		y 112
		x 119
		view 208
		loop 1
		;cel 1
	)
)
(instance enemyRightHand of Prop
	(properties
		y 108
		x 194
		view 205
		loop 4
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
(instance ground of Prop
	(properties
		y 175
		x 160
		view 199
	)
)
(instance tutor of Prop
	(properties
		y 145
		x 290
		view 194
	)
)
(instance speechBubble of Prop
	(properties
		y 100
		x 261
		view 194
		loop 2
	)
)