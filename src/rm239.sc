;;; Sierra Script 1.0 - (do not remove this comment)
(script# 239)
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
	rm239 0
)

(local

	mysteryMusic = 0

	
)


(instance rm239 of Rm
	(properties
		picture scriptNumber
		north 65
		east 0
		south 0
		west 0
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript)
		(switch gPreviousRoomNumber
			(else 
				(gEgo posn: 200 70 loop: 1)
			)
		)
		(SetUpEgo)
		(gEgo init:)
		(bushes init:)
		(shirt init: hide: ignoreActors: setPri: 15)
		
		(waterfall init: setCycle: Fwd cycleSpeed: 3 ignoreActors:)
		(ripple init: setCycle: Fwd cycleSpeed: 3 ignoreActors:)
		
		(alterEgo init: hide: ignoreActors:  ignoreControl: ctlWHITE setScript: removeClothesScript)
	)
)

(instance RoomScript of Script
	(properties)
	
	(method (doit)
		(super doit:)
		(if (& (gEgo onControl:) ctlMAROON)
			(bushes cel: 1)
			(if (not mysteryMusic)
				(gTheSoundFX number: 200 play:)
				(= mysteryMusic 1)
				(gEgo setMotion: NULL)
			)
		else
			(bushes cel: 0)
			(= mysteryMusic 0)
		)
		(if (& (gEgo onControl:) ctlSILVER)
			(gRoom newRoom: 65)	
		)
	)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		; handle Said's, etc...
		(if(Said 'dive,swim,bathe')
			(Print "With no bath in days, you eagerly ready yourself for a dip in the cool, beautiful waters.")
			(removeClothesScript changeState: 1)	
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

(instance removeClothesScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0
			)
			(1
				(gEgo setMotion: MoveTo 237 128 self ignoreControl: ctlWHITE)
			)
			(2	
				
				(gEgo hide:)
				(alterEgo show: view: 316 loop: 0 cel: 0 posn: (gEgo x?)(gEgo y?) setCycle: End self cycleSpeed: 4)
			)
			(3
				(alterEgo loop: 1 cel: 0 setCycle: End self)
				(shirt show: setCycle: End cycleSpeed: 3)	
			)
			(4
				(alterEgo loop: 2 cel: 0 setCycle: End self)	
			)
			(5 ;diving into water
				(alterEgo loop: 4 cel: 0 setCycle: End self)		
			)
			(6
				(alterEgo loop: 5 cel: 0 posn: 212 128 setCycle: End self)		
			)
			(7
				(alterEgo loop: 6 cel: 0 posn: 180 130 setCycle: End self)
			)
			(8
				(alterEgo view: 317 loop: 0 cel: 0 posn: 160 130 setCycle: End self)	
			)
			(9
				(alterEgo loop: 1 cel: 0 posn: 145 134 setCycle: Fwd)	
			)
		)
	)
)

(instance bushes of Prop
	(properties
		y 200
		x 272
		view 803
		;loop 1
	)
)
(instance alterEgo of Act
	(properties
		y 200
		x 272
		view 316
		;loop 1
	)
)

(instance shirt of Prop
	(properties
		y 109
		x 243
		view 316
		loop 3
	)
)
(instance waterfall of Prop
	(properties
		y 99
		x 13
		view 9
	)
)
(instance ripple of Prop
	(properties
		y 145
		x 13
		view 65
		loop 1
	)
)
