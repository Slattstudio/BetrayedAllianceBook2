;;; Sierra Script 1.0 - (do not remove this comment)
(script# 972)

(include sci.sh)
(include game.sh)
(use Main)
(use user)
(use SysWindow)
(use obj)

(public
	
	theDeathSheet 0
	
)
(local
	
	deathWindowOpen = 0		; Used as a switch to determine when you can close the deathWindow
	deathWindow 			; Used to open the display window
	textDown = 7 			; Used to place statements progressively lower than earlier statements
	textRight = 30 			; Used to move statements right, once y-axis full - NOT YET IMPLEMENTED				
	[stringA 30]			; String for pulling text resources
	[stringB 2]				; String used to display number of deaths
	
	hiddenDeaths = 0
)


(procedure (deathCountIterator integer)
	(if (> integer 30)
		(if (not hiddenDeaths)
			(= textDown (+ textDown 2))
			(Display "Secret Deaths:"
				dsCOORD (- textRight 10) textDown
				;dsFONT 4
				dsCOLOR 12 
			)
			(= textDown (+ textDown 11))
			(= hiddenDeaths 1)
		)	
		(Display
			(Format @stringA 650 integer)
			dsCOORD textRight textDown
			dsFONT 4
			dsCOLOR 12 
		)
	else
		(Display
			(Format @stringA 650 integer)
			dsCOORD textRight textDown
			dsFONT 4 
		)
	)
	(Format @stringB {%2d} [gDeaths integer])
	(Display @stringB
		dsCOORD (- textRight 14) textDown
		dsFONT 4 
	)
		
	(= textDown (+ textDown 9))
	(if (> textDown 178)
		(= textRight 170)
		(= textDown 19)	
	)
		
		
)


(class deathSheet of Obj
	(properties
		nsTop 0
		nsLeft 0
		nsBottom 179
		nsRight 319
		theWindow 0
	)
	
	(method (init &tmp temp1 i y [str 30] u deathTotal)
		(super init:)
		;(= theWindow
		;	(NewWindow nsTop nsLeft nsBottom nsRight {} 0 -1 clBLUE clWHITE)
		;)
		
		(= deathWindowOpen 1)
			(= theWindow (NewWindow 
					0 
					0 
					190 
					318 
					{Death Count} 
					nwNORMAL 
					16 ; priority
					7 
					15
			)
		)
		(if (not [gDeaths 0])
			(Display 
				(Format @str {So far so good, you've died %u times,} [gDeaths 0])
				dsCOORD 45 20 
				dsFONT 4
			)
			(Display 
				(Format @str {in %u of 30 unique ways.} gUniqueDeaths)
				dsCOORD 45 30 
				dsFONT 4
			)
			(Display 
				{We'll keep a log for you here for fun.}
				dsCOORD 45 45 
				dsFONT 4
			)
			(Display 
				{Can you find them all?}
				dsCOORD 45 55 
				dsFONT 4
			)
		)
		; for loop to determine how many unique deaths
		(= deathTotal 0)
		(for ( (= u 1)) (< u 31)  ( (++ u)) (if (> [gDeaths u] 0)
				(if (and (> u 0) (< u 31))
					(++ deathTotal)
				)
			)
			(= gUniqueDeaths deathTotal)
		)
		; For loop to run through the gDeath array and print a statement of death for each that is true
		
		(for ( (= i 0)) (< i 35)  ( (++ i)) (if (> [gDeaths i] 0)
				(if (== i 0)
					(Display 
						(Format @str {You died %u times, in %u of 30 unique ways.} [gDeaths 0] gUniqueDeaths)
						dsCOORD 20 textDown 
					)
					(= textDown (+ textDown 12))	
				)
				(if (and (> i 0) (< i 35))
					(deathCountIterator i)
				)				
			)
		)			
	)
	
	(method (doit &tmp newEvent newEventType)
		(= newEventType 0)
		(while (not newEventType)
			(GlobalToLocal (= newEvent (Event new:)))
			(= newEventType (newEvent type?))
			(newEvent dispose:)
		)
		(self dispose:)
	)
	
	(method (dispose)
		(DisposeWindow theWindow)
		(super dispose:)
		(DisposeScript 972)
	)
	
)

(instance theDeathSheet of deathSheet
	(properties)
)



