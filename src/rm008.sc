;;; Sierra Script 1.0 - (do not remove this comment)

(script# 8)
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
	rm008 0
)


(instance rm008 of Rm
	(properties
		picture scriptNumber 
		north 0
		east 221
		south 0
		west 249 ; This takes the player to room 247
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript)
		(switch gPreviousRoomNumber
			(249
				(gEgo posn: 20 140 loop: 0) ;This will place the character near the left side of the room facing RIGHT.	
			)
			(else 
				(gEgo posn: 150 130 loop: 1)
			)
		)
		(SetUpEgo)
		(gEgo init:)
	)
)


(instance RoomScript of Script
	(properties)
	

)
