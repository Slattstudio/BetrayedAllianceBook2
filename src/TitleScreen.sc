;;; Sierra Script 1.0 - (do not remove this comment)
;
; SCI Template Game
; By Brian Provinciano
; ******************************************************************************
; titlescreen.sc
; Contains the title screen room.
(script# TITLESCREEN_SCRIPT)
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
	TitleScreen 0
)




(instance TitleScreen of Rm
	(properties
		picture 900
	)
	
	(method (init)
		; Set up the title screen
		(ProgramControl)
		(= gProgramControl FALSE)
		(gGame setSpeed: 1)
		(SL disable:)
		(TheMenuBar hide:)
		(super init:)
		(self setScript: RoomScript)
		(gEgo init: hide:)
;
;         * Set up the room's music to play here *
		;
		; (send gTheMusic:
		; 	prevSignal(0)
		; 	stop()
		; 	number(scriptNumber)
		; 	loop(-1)
		; 	play()
		; )
;
;         * Add the rest of your initialization stuff here *
	)
)


(instance RoomScript of Script
	(properties)
	
;
	(method (changeState newState)
		(= state newState)
		(switch state
			(0 (= cycles 1))
			(1
				(gRoom newRoom: INITROOMS_SCRIPT)
			)
		)
	)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		(if (not (pEvent claimed?))
			(if
				(and
					(== (pEvent type?) evKEYBOARD)
					(== (pEvent message?) $3c00)
				)
				(ToggleSound)
			else
;
;                 * If the title screen has music, fade it *
				; (send gTheMusic:fade())
				; End the title screen, start the game
				(gRoom newRoom: INITROOMS_SCRIPT)
			)
		)
	)
)
