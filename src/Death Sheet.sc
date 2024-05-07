;;; Sierra Script 1.0 - (do not remove this comment)
(script# 650)
(include keys.sh) (include sci.sh) (include game.sh)
(use Main)
(use inv)
(use sysWindow)
(use controls)
(use obj)
(use feature)
(use user)
(use game)
(use window)

(public
	theDeathSheet 0
)

(define SMALL_LINE_HEIGHT 9)

(procedure (DisplayDeathText death x y &tmp invQty [str 40])
	(Display
		651 (- death DIE_START)
		p_at (+ x 5) y
		p_mode teJustLeft
		p_font smallFont
		p_color (if (== death prevDeathNum) vLRED else vLBLUE)
	)
	;display name of death
)

(procedure (DisplayDeathHeader &tmp [str 40])
	;Displays a different header depending on how many deaths we've had
	;
		(cond
			((== deathCount 0)
				(Display
					650 0
					p_at 10 8
					p_mode teJustLeft
					p_font 300
					p_color vBLUE
				)
				;You haven't died at all. Don't be afraid to explore and try new things.
			)
			((== deathCount 1)
				(Display
					650 1
					p_at 10 8
					p_mode teJustLeft
					p_font 300
					p_color vBLUE
				)
				;You have died once.
			)
			((== deathCount deathCountUnique)
				(Display
					(Format @str 650 2 deathCount)
					p_at 10 8
					p_mode teJustLeft
					p_font 300
					p_color vBLUE
				)
				;You have died %u times.
			)
			(else
				(Display
					(Format @str 650 3 deathCount deathCountUnique)
					p_at 10 8
					p_mode teJustLeft
					p_font 300
					p_color vBLUE
				)
				;You have died %u times, in %u unique ways
			)
		)
)

(procedure (DisplayDeathPage pageStart offset &tmp i x y nextDeathStart)
	(= i pageStart)
	(= x 10)
	(= y (+ 24 (* offset SMALL_LINE_HEIGHT)))
	(= nextDeathStart 0)
	(while (<= i DIE_END) ;there are currently 60 deaths. So that's how many we will show.
		;if this is a true death, and it doesn't match the most recent death
		(if (and (Btst i) (not (== i prevDeathNum)) )
			(if (<= y 160)
				;display the death text
				(DisplayDeathText i x y)
			else
				;otherwise, make note of where to start the next page, and end this page.
				(= nextDeathStart i)
				(= i DIE_END)
			)
			;increment to the next line. If the next line is too far down, shift to the next column
			(= y (+ y SMALL_LINE_HEIGHT))
			(if (and (> y 160) (== x 10))
				(= y 24)
				(= x 170)
			)
		)
		(++ i)
	)
	(return nextDeathStart)
)

(class DeathSheet of Object
	(properties
		nsTop 0
		nsLeft 0
		nsBottom southEdge
		nsRight MAXRIGHT
		theWindow NULL
		state 	0
		register 	0
		more	FALSE
		startAt	0
	)
	
	(method (init &tmp i x y pageStart [str 40])
		(super init:)
		(self clearWindow:)
		(self drawContents:)
	)
	
	(method (drawContents &tmp pageStart offset)
		;decide what header to show
		(DisplayDeathHeader)
		
		;if there's a new death, we'll show that at the top row
		(= offset 0)
		(if prevDeathNum
			(DisplayDeathText prevDeathNum 10 24)
			(= offset 1)
		)
		
		;now we show the individual deaths.
		(= pageStart (DisplayDeathPage (+ DIE_START startAt) offset))

		(if (> pageStart DIE_START)
			(= startAt pageStart)
			(= more TRUE)
			(Display 206 3 p_at 200 172 p_mode teJustRight p_font 300 p_color vBLUE p_width 100)
			;(and more . . .)
		else
			(= more FALSE)
		)
		;now show the second page, then third page, etc. until we're done.

		;clear any "Previous Death" flags.
		(= prevDeathNum 0)
	)
	
	(method (clearWindow &tmp newWindow)
		(= newWindow
			(NewWindow nsTop nsLeft nsBottom nsRight {} nwNORMAL nwON_TOP vBLUE vWHITE)
		)
		(if theWindow
			(DisposeWindow theWindow)
		)
		(= theWindow newWindow)
	)
	
	(method (doit &tmp newEvent evtType)
		(= evtType nullEvt)
		(while (not evtType)
			(GlobalToLocal (= newEvent (Event new:)))
			(cond
				((not more)
					(= evtType (newEvent type?))
				)
				((and (== keyDown (newEvent type?)) (== KEY_ESC (newEvent message?)) )
					;there's more to see, but the user has clicked ESC
					(= evtType (newEvent type?))
				)
				((not (== nullEvt (newEvent type?)))
					;do anything else
					(= evtType (newEvent type?))
					;CI: TODO: Show a second page.
					;(Print {Skip to next page})
					;(self clearWindow:)
				)
			)
			
			;(= evtType (newEvent type?))
			(newEvent dispose:)
		)
		(self dispose:)
	)
	
	(method (dispose)
		(DisposeWindow theWindow)
		(super dispose:)
		(DisposeScript 650)
	)
)

(instance theDeathSheet of DeathSheet
	(properties)
)