;;; Sierra Script 1.0 - (do not remove this comment)
; Score + 1
(script# 81)
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
	
	rm081 0
	
)
(local
	
	[fruitStr 30]
	
)


(instance rm081 of Rm
	(properties
		picture scriptNumber
		north 0
		east 257
		south 0
		west 0
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript)
		(switch gPreviousRoomNumber
			(else 
				(gEgo posn: 300 120 loop: 1)
			)
		)
		(SetUpEgo)
		(gEgo init:)
		
		(RunningCheck)
		
		(if (not g81Fruit)
			(fruit init:)
		)
	)
)

(instance RoomScript of Script
	(properties)
	
	(method (doit)
		(super doit:)
		; code executed each game cycle
	)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if (& (pEvent modifiers?) emRIGHT_BUTTON)
				(if (== (OnControl ocPRIORITY (pEvent x?) (pEvent y?)) ctlNAVY) ; Bush
					(if gRightClickSearch
						(if (not g81Fruit)
							(RoomScript changeState: 1)	
						else
							(if g81Fruit
								(PrintOther 81 2)	
							else
								(PrintOther 81 1)
							)
						)
					else
						(if g81Fruit
							(PrintOther 81 2)	
						else
							(PrintOther 81 1)
						)
					)
				)
			)
		)
		
		; handle Said's, etc...
		
		(if(Said 'take,(pick<up)/fruit')
			(if g81Fruit
				(Print "There's no fruit left to take.")	
			else
				(RoomScript changeState: 1)
			)		
		)
		(if (Said 'look>')
			(if (Said '/bush')
				(if g81Fruit
					(PrintOther 81 2)	
				else
					(PrintOther 81 1)
				)
			)
			(if (Said '/fruit,honey')
				(if g81Fruit
					(if (gEgo has: INV_HONEY)
						(Format @fruitStr "A plump fruit filled with a sticky honey-like goo. You have %d." gHoneyNum)
						(Print @fruitStr #icon 610)
					else
						(PrintOther 81 4)
					)		
				else
					(PrintOther 81 5)
				)
			)
			(if (Said '[/!*]')
				(PrintOther 81 0)
			)
		) 
	)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0 ; Handle state changes
			)
			(1
				;(= animation 1)
				(ProgramControl)
				(gEgo setMotion: MoveTo 275 111 self ignoreControl: ctlWHITE) ; walk to fruit
			)
			(2 (= cycles 2)
				(gEgo loop: 3)
			)	
			(3	(= cycles 2)	; put item in inventory
				(PrintOther 81 3)
				(gEgo get: 2 observeControl: ctlWHITE)	
				
				(= gHoneyNum (+ gHoneyNum 1))
				((gInv at: 2) count: gHoneyNum)
				(= g81Fruit 1)
				(fruit hide:)
			)
			(4
				(PlayerControl)
				(gEgo loop: 2)
				(gGame changeScore: 1)
				;(= animation 0)
			)
		)
	)
)

(procedure (PrintOther textRes textResIndex)
	(if (> (gEgo y?) 120)
		(Print textRes textResIndex
			#width 290
			#at -1 10
		)
	else
		(Print textRes textResIndex
			#width 290
			#at -1 140
		)
	)
)
(instance fruit of Prop
	(properties
		y 96
		x 273
		view 63
		loop 4
	)
)