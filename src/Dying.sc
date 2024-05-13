;;; Sierra Script 1.0 - (do not remove this comment)
;
; SCI Template Game
; By Brian Provinciano
; ******************************************************************************
; dying.sc
; Contains a public script instance to handle when the ego dies.
(script# DYING_SCRIPT)
(include sci.sh)
(include game.sh)
(use main)
(use controls)
(use dcicon)
(use cycle)
(use obj)

(public
	DyingScript 0
)




(instance DyingScript of Script
	(properties)
	
	(method (changeState newState &tmp mbResult message)
		(= state newState)
		(cond 
			((== state 0)
				(ProgramControl)
				(gTheMusic fade:)
				(gRoom setScript: 0)
				(Load rsSOUND 2)
				(= seconds 3)
			)
			((== state 1)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
				
				; Add to Death counter
				(= gSaveDeaths 1)
				(++ [gDeaths 0])
				
				(gTheSoundFX stop:)
				(gTheMusic number: 2 loop: 1 priority: -1 play:)
				; The following lines give a typical Sierra style
				; message box telling the player that they have died.
				; You can customize it to your liking.
				(if (!= NULL caller)
					(Load rsVIEW caller)
					(deadIcon view: caller)
				else
					(Load rsVIEW DYING_SCRIPT)
					(deadIcon view: DYING_SCRIPT )
				)
				(if (!= NULL register)
					(= message register)
				else
					(= message {You are dead.})
				)
				(if
					(Print
						message
						;#font
						;gDeadFont
						#icon
						deadIcon
						#button
						{ Okay }
						0
					)
				)
				(if gAutosave
					(repeat
						(= gVertButtons 1)
						(= mbResult
							(Print
								{Remember:\nsave early, save often!}
								#title {Words to the wise:}
								#font gDeadFont
								#button {Retry} 1
								#button {Restore} 2							
								#button {Restart} 3
								#button {__Quit__} 4 )
						)
						(switch mbResult
							(1
								(= gRetry 1)
								(gGame restore:)
								(return)
							)
							(2
								(if (!= (gGame restore:) -1) (return))
							)
							(3 (gGame restart:) (return))
							(4 (= gQuitGame TRUE) (return))
						)
					)
				else
					(repeat
						(= mbResult
							(Print
								977 1
								#title
								{Memento Mori:}
								#button
								{Restore}
								1
								#button
								{Restart}
								2
								#button
								{__Quit__}
								3
							)
						)
						(switch mbResult
							(1
								(if (!= (gGame restore:) -1) (return))
							)
							(2 (gGame restart:) (return))
							(3 (= gQuitGame TRUE) (return))
						)
					)
				)
			)
		)
	)
)


(instance deadIcon of DCIcon
	(properties)
	
	(method (init)
		(super init:)
		(if (== gDeathIconEnd 1)
			(= cycler (End new:))
			(cycler init: self)	
		)
	)
)
