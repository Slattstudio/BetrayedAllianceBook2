;;; Sierra Script 1.0 - (do not remove this comment)
; Broken Bridge Forest
(script# 210)
(include sci.sh)
(include game.sh)
(use controls)
(use cycle)
(use feature)
(use game)
(use inv)
(use main)
(use obj)
(use window)


(public
	rm210 0
)

(local

)

(instance rm210 of Rm
	(properties
		picture scriptNumber
		north 0
		east 252
		south 0
		west 208
	)
	
	(method (init)
		(super init:)
		(self
			setScript: RoomScript
			setRegions: 205
			; setRegions: 206
		)
		(switch gPreviousRoomNumber
			(208
				(PlaceEgo 20 95 0)		
			)
			(252
				(PlaceEgo 300 113 1)			
			)
			(else
				(PlaceEgo 300 113 1)	
			)
			;(else 
			;	(if (not gTeleporting)
			;		(gEgo init: posn: 32 96)
			;	else
			;		(= gTeleporting 0)
			;	)
			;)
		)
		(SetUpEgo)
		(gEgo init:)
		(RunningCheck)
		
		(bridgeLeft init: ignoreActors: setPri: 4)
		(bridgeRight init: ignoreActors: setPri: 4)
		(ropeLeft init: ignoreActors:)
		(ropeRight init: ignoreActors:)
		
		
	)
)


(instance RoomScript of Script
	(properties)
	
	(method (changeState mainState )
		(= state mainState)

	)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		
	)
)



(instance bridgeLeft of Prop
	(properties
		y 193
		x 122
		view 83
	)
)

(instance bridgeRight of Prop
	(properties
		y 206
		x 230
		view 83
		loop 1
	)
)
(instance ropeLeft of Prop
	(properties
		y 100
		x 122
		view 83
		loop 2
	)
)
(instance ropeRight of Prop
	(properties
		y 103
		x 232
		view 83
		loop 3
	)
)