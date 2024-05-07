;;; Sierra Script 1.0 - (do not remove this comment)
(script# 650)

(include keys.sh) (include sci.sh) (include game.sh)
(use Main)
(use Inv)
(use SysWindow)
(use Obj)

(public
	;theDeathSheet = 0
)

(local
	deathWindowOpen = 0	
	deathWindow
	i = 0
	textDown = 7
	[str 30]
)

(procedure (OpenDeathCountWindow)

	(= deathWindowOpen 1)
	(= deathWindow (NewWindow 0 0 190 318 {Death Count} nwNORMAL 15 7 15))

	(for ( (= i 0)) (< i 8)  ( (++ i)) (if (> [gDeaths i] 0)
		(switch i
			(0 
				(Display 
					(Format @str {You have died %u times:} [gDeaths 0])
					dsCOORD 10 textDown 
				)
				(= textDown (+ textDown 12))
			)
			(1
				(deathCountIterator)	
			)
			(2
				(deathCountIterator)	
			)
			(3
				(deathCountIterator)	
			)
			(4
				(deathCountIterator)	
			)
			(5
				(deathCountIterator)	
			)
			(6
				(deathCountIterator)	
			)
			(7
				(deathCountIterator)	
			)
			
			
		)
	))
)

(procedure (deathCountIterator)
	(Display
		(Format @str 650 i)
		dsCOORD 10 textDown
		dsFONT 4 
	)
	(= textDown (+ textDown 10))		
)
