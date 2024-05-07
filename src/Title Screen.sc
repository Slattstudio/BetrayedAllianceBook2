;;; Sierra Script 1.0 - (do not remove this comment)
(script# 803)


(include sci.sh)
(include game.sh)
(use main)
(use game)
(use menubar)
(use obj)
(use cycle)
(use user)
(use controls)
(use feature)

(public
	rm803 0
)

(local
	myEvent
	cursorCounter
	selectNewGame = 1
	selectRestore = 0
)

(instance rm803 of Rm
	(properties
		picture 801 ;scriptNumber
		north 0
		east 0
		south 0
		west 0
	)
	
	(method (init)
		
		(ProgramControl)
		(= gProgramControl FALSE)
		(gGame setSpeed: 3)
		(SL disable:)
		(TheMenuBar hide:)
		
		(super init:)
		(self setScript: RoomScript)
		(switch gPreviousRoomNumber
			(else 
				(gEgo posn: 150 100 loop: 1)
			)
		)
		(SetUpEgo)
		(gEgo init: hide: xStep: 8 yStep: 5)
		(fire init: setCycle: Fwd setPri: 13)
		
		(book2 init: setPri: 15)
		(newGame init: setPri: 15)
		(restore init: setPri: 15)
		
		(darkRight init: setPri: 15)
		(darkLeft init: setPri: 15)
		(darkUp init: setPri: 14)
		(darkDown init: setPri: 15)
		(darkUp2 init: setPri: 14)
		(darkDown2 init: setPri: 15)
		
		
	)
)

(instance RoomScript of Script
	(properties)
	
	(method (doit)
		(super doit:)
		; code executed each game cycle
		
		; Used to make the cursor disappear after 200 cycles
		(if (> cursorCounter 0)
			(-- cursorCounter)
			(if(== cursorCounter 0)
				(RoomScript changeState: 1)
			)
		)
		
		; Used to activate which starting option
		(if (< (gEgo x?) 145)
			(= selectNewGame 1)
			(= selectRestore 0)
			(newGame cel: 1)
			(restore cel: 0)
		else
			(if (> (gEgo x?) 200)
				(= selectNewGame 0)
				(= selectRestore 1)
				(newGame cel: 0)
				(restore cel: 1)
			else
				(= selectNewGame 0)
				(= selectRestore 0)
				(newGame cel: 0)
				(restore cel: 0)
			)	
		)
		
		(= myEvent (Event new: evNULL))
		(gEgo setMotion: MoveTo (myEvent x?)(myEvent y?))
		(fire posn: (gEgo x?) (+ (gEgo y?) 15) )
		(myEvent dispose:)
		
		; Setting a Lower limit to the flame
		(if(> (gEgo y?) 155)
			(gEgo posn: (gEgo x?) 155)
		)
		; Setting an Upper Limit to the flame
		(if(< (gEgo y?) 85)
			(gEgo posn: (gEgo x?) 85)
		)
		; Setting a Left limit to the flame
		(if(< (gEgo x?) 60)
			(gEgo posn: 60 (gEgo y?))
		)
		; Setting an Right Limit to the flame
		(if(> (gEgo x?) 255)
			(gEgo posn: 255 (gEgo y?))
		)
						
		; Determining how the shadows overtake the title
		(if (< (gEgo y?) (+ (darkDown y?)60))
			(if(> (darkDown y?) 38) 
				(darkDown posn: (darkDown x?) (- (darkDown y?) 2) ) ; up
				(darkDown2 posn: (darkDown2 x?) (- (darkDown2 y?) 2) )
			)

		else
			(> (gEgo y?) (darkDown y?))
			(if(< (darkDown y?) 80)
				(darkDown posn: (darkDown x?) (+ (darkDown y?) 2) ) ; down
				(darkDown2 posn: (darkDown2 x?) (+ (darkDown2 y?) 2) )
			)
		)
		(if (> (gEgo y?) (- (darkUp y?)60))
			(if(< (darkUp y?) 186) 
				(darkUp posn: (darkUp x?) (+ (darkUp y?) 2) ) ; down
				(darkUp2 posn: (darkUp2 x?) (+ (darkUp2 y?) 2) )
			)

		else
			(< (gEgo y?) (darkUp y?) )
			(if(> (darkUp y?) 147)
				(darkUp posn: (darkUp x?) (- (darkUp y?) 2) ) ; up
				(darkUp2 posn: (darkUp2 x?) (- (darkUp2 y?) 2) )
			)
		)
		(if (< (gEgo x?) (+ (darkLeft x?)150))
			(if(> (darkLeft x?) -8) 
				(darkLeft posn: (- (darkLeft x?) 3) (darkLeft y?) ) ;left
			)

		else
			(> (gEgo x?) (darkLeft x?) )
			(if(< (darkLeft x?) 25)
				(darkLeft posn: (+ (darkLeft x?) 3) (darkLeft y?) ) ;right
			)
		)
		(if (> (gEgo x?) (- (darkRight x?)150))
			(if(< (darkRight x?) 332) 
				(darkRight posn: (+ (darkRight x?) 3) (darkRight y?) ) ;right
			)

		else
			(< (gEgo x?) (darkRight x?) )
			(if(> (darkRight x?) 287)
				(darkRight posn: (- (darkRight x?) 3) (darkRight y?) ) ;left
			)
		)
		
	)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		; handle Said's, etc...
		(if (== (pEvent type?) evMOUSEBUTTON)
			(RoomScript changeState: 2)	
		)
		(if (== (pEvent type?) evKEYBOARD)
			(if (== (pEvent message?) KEY_RETURN)
				(RoomScript changeState: 2)
			)
		)	
	)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0 (= cycles 1) ; Handle state changes
				
			)(1
				(SetCursor 998 (HaveMouse))
				(= gCurrentCursor 998)	
			)(2 (= cycles 1)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
				(= cursorCounter 120)
			)(3
				(cond 
					(selectNewGame (gRoom newRoom: 210))
					(selectRestore
						;(= snd aud)
						;(snd
							;command: {stop}
							;fileName: {music\\intro.mp3}
							;fadeOutMillisecs: {4000}
							;loopCount: {0}
							;init:
						;)
						(gGame restore:)
					)
					; Code This out before release
					(else 
						(gRoom newRoom: 210)
					)
					;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
				)
			 )	
		)
	)
)
(instance book2 of Prop
	(properties
		y 175
		x 160
		view 995
		loop 4
	)
)
(instance newGame of Prop
	(properties
		y 114
		x 90
		view 995
		loop 2
	)
)
(instance restore of Prop
	(properties
		y 96
		x 240
		view 995
		loop 3
	)
)
(instance fire of Prop
	(properties
		y 35
		x 47
		view 429
	)
)
(instance darkLeft of Prop
	(properties
		y 180
		x 20
		view 429
		loop 1
	)
)
(instance darkRight of Prop
	(properties
		y 180
		x 310
		view 429
		loop 2
	)
)
(instance darkUp of Prop
	(properties
		y 170
		x 93 ; 160
		view 429
		loop 3
	)
)
(instance darkUp2 of Prop
	(properties
		y 170
		x 228
		view 429
		loop 3
	)
)
(instance darkDown of Prop
	(properties
		y 60
		x 93
		view 429
		loop 4
	)
)
(instance darkDown2 of Prop
	(properties
		y 60
		x 228
		view 429
		loop 4
	)
)
