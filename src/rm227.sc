;;; Sierra Script 1.0 - (do not remove this comment)
(script# 227)
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
	rm227 0
)

(instance rm227 of Rm
	(properties
		picture scriptNumber
		north 228
		east 0
		south 0
		west 229
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript setRegions: 205)
		(switch gPreviousRoomNumber
			(else 
				;(gEgo posn: 10 142 loop: 0)
				(PlaceEgo 10 142 0)
			)
		)
		(SetUpEgo)
		(gEgo init:)
		(= gEgoMovementType 0)
		(RunningCheck)
		
		(waterFall
			init:
			setCycle: Fwd
			cycleSpeed: 2
			setPri: 9
		)
		(waterRipple
			init:
			setCycle: Fwd
			cycleSpeed: 3
			setPri: 1
		)
	)
)

(instance RoomScript of Script
	(properties)
	
	(method (doit)
		(super doit:)
		(if (& (gEgo onControl:) (| $0100 $0080))
			(gEgo mirrorEgo: 135)
		)
		(if (not (& (gEgo onControl:) (| $0100 $0080)))
			(gEgo mirrorEgoDispose:)
		)
	)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if (& (pEvent modifiers?) emRIGHT_BUTTON)
				(if (checkEvent pEvent (waterFall nsLeft?)(waterFall nsRight?)(waterFall nsTop?)(waterFall nsBottom?))
					(if (& (gEgo onControl:) (| $0100 $0080))
						(if g227WaterfallBreached
							(PrintOther 227 2)
						else
							(PrintOther 227 1)
						)		
					else
						(PrintOther 227 3)
					)	
				)
			
			)
		)
		; handle Said's, etc...
		(if (Said 'look>')
			(if (Said '/reflection')
				(if (& (gEgo onControl:) (| $0100 $0080))
					(if g227WaterfallBreached
						(PrintOther 227 2)
					else
						(PrintOther 227 1)
					)		
				else
					(PrintOther 227 4)
				)
			)
			(if (Said '/water, waterfall')
				(if (& (gEgo onControl:) (| $0100 $0080))
					(if g227WaterfallBreached
						(PrintOther 227 2)
					else
						(PrintOther 227 1)
					)		
				else
					(PrintOther 227 3)
				)		
			)
			(if (Said '[/!*]')
				(PrintOther 227 0)
			)	
		)
		
		(if (Said 'touch/water,waterfall')
			(PrintOther 227 5)	
		)
		(if (Said 'touch/reflection,self')
			(PrintOther 227 6)	
		)
		
		(if (Said 'run')
			(Print "This isn't a good place to do that.")	
		)
		(if (Said 'sneak,stealth')
			(if gAnotherEgo
				(Print "There's no need to do that here.")
			else
				(Print "You're not the 'sneaky' type.")
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

(procedure (PrintOther textRes textResIndex)
	(Print textRes textResIndex
		#width 280
		#at -1 10
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
(instance waterFall of Prop
	(properties
		y 135
		x 63
		view 9
		loop 0
	)
)
(instance waterRipple of Prop
	(properties
		y 190
		x 30
		view 65
		loop 4
	)
)