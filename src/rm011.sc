;;; Sierra Script 1.0 - (do not remove this comment)

(script# 11)
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
	rm011 0
)




(instance rm011 of Rm
	(properties
		picture scriptNumber
		north 273
		east 87
		south 0
		west 275
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript)
		(switch gPreviousRoomNumber
			(else 
				(gEgo posn: 150 65 loop: 1)
			)
		)
		(SetUpEgo)
		(gEgo init:)
		
		(frog init: ignoreControl: ctlWHITE)

	)
)
                                                             

(instance RoomScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0 ; Handle state changes
				(frog setCycle: Walk setMotion: MoveTo 250 65 RoomScript ignoreActors: cycleSpeed: 2)
			)
			(1
				(frog setMotion: MoveTo 150 65 RoomScript ignoreActors:)	
			)
			(2
				(self changeState: 0)
			)
		)
	)
)

(instance frog of Act
	(properties
		y 65
		x 150
		view 305
		loop 1
	)
)