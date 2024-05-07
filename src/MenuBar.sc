;;; Sierra Script 1.0 - (do not remove this comment)
;
; SCI Template Game
; By Brian Provinciano
; ******************************************************************************
; menubar.sc
; Contains the customized Menubar class. This is the script you modify if you 
; want to customize the menu.
(script# MENUBAR_SCRIPT)
(include sci.sh)
(include game.sh)
(use main)
(use controls)
(use gauge)
(use sound)
(use user)

(public
	ToggleSound 0
)

(local
	
	armor = 0
	books = 0
	i
	
)


(class TheMenuBar of MenuBar
	(properties
		state 0
	)
	
	(method (init)
		(AddMenu { _} {About Game`^a:Help`#1:Settings`#2})
		(AddMenu
			{ File_}
			{Restart Game`#9:Save Game`#5:Restore Game`#7:--! :Quit`^q}
		)
		(AddMenu
			{ Character -}
			{Ask About`^a:Retype`#3:--!:Inventory`^I:Death Log`^d:Notes`^n:Switch`^x}
		)
		(SetMenu MENU_SWITCH 112 0)	; disable Switch
		
		(AddMenu
			{ Speed_}
			{Change...`^s:--!:Faster`+:Normal`=:Slower`-}
		)
		(if (DoSound sndSET_SOUND)
			(AddMenu { Sound_} {Volume...`^v:Turn Off`#4=1})
		else
			(AddMenu { Sound_} {Volume...`^v:Turn On`#4=1})
		)
		(if (< (Graph grGET_COLOURS) 9)
			(SetMenu MENU_COLOURS 32 0)
		else
			(SetMenu MENU_COLOURS smMENU_SAID '/color')
		)
		(SetMenu MENU_SAVE smMENU_SAID 'save[/game]')
		(SetMenu MENU_RESTORE smMENU_SAID 'restore[/game]')
		(SetMenu MENU_RESTART smMENU_SAID 'restart[/game]')
		(SetMenu MENU_QUIT smMENU_SAID 'done[/game]')
		(SetMenu MENU_QUIT smMENU_SAID 'bye')
		;(SetMenu MENU_PAUSE smMENU_SAID 'delay[/game]')
		(SetMenu MENU_INVENTORY smMENU_SAID 'all')
	)
	
	(method (handleEvent pEvent &tmp menuItem hGauge newSpeed newVolume wndCol wndBack hPause button deathWindow)
		(= menuItem (super handleEvent: pEvent))
		(switch menuItem
			(MENU_ABOUT
				(= gWndColor 11)    ; clCYAN
				(= gWndBack 0)      ; clBLACK
				(Print 997 0 #title {About} #width 230 #font 4 #icon 995 5 0)
				(Print 997 1 #title {Kickstarter Credits:} #font 4 #width 320)
				(Print 997 2 #title {Kickstarter Credits:} #font 4 #width 320)
				;(Print 997 3 #title {Programming} #icon 951)
				;(Print 997 2 #title {Music} #icon 950)
				(= gWndColor 0)     ; clBLACK
				(= gWndBack 15)
			)
			(MENU_HELP
				(= gWndColor 0)
				(= gWndBack 14)			
				(Print 997 7 #title {How To Play} #font 4 #width 280)
				(= gWndColor 0)
				(= gWndBack 15)
			)
			(MENU_SETTINGS
				(= gVertButtons 1)
				(= gWndColor 0)
				(= gWndBack 14)
				(= gYellowTips
					(Print 10 1 #title {Tutorial Boxes:} #button { On_} 1 #button { Off_} 0 #font 4)
				)
				(= gRightClickSearch
					(Print 10 0 #title {Right-Click-Search:} #button { On_} 1 #button { Off_} 0 #font 4)
				)
				(= gVertButtons 0)
				(= gWndColor 0)     ; clBLACK
				(= gWndBack 15)
				; playing with gauges
			)
			(MENU_RESTART
				(if
					(Print
						{Are you serious? You really want to start all the way back at the beginning again?}
						#title
						{Restart}
						#font
						gDefaultFont
						#button
						{Restart}
						1
						#button
						{ Oops_}
						0
					)
					(gGame restart:)
				)
			)
			(MENU_RESTORE (gGame restore:))
			(MENU_SAVE (gGame save:))
			(MENU_QUIT
				(if
					(Print
						{Do you really want to quit?}
						#title
						{Quit}
						#font
						gDefaultFont
						#button
						{ Quit_}
						1
						#button
						{ Oops_}
						0
					)
					(= gQuitGame TRUE)
				)
			)
			(MENU_ASKABOUT
				(StrCpy (User inputLineAddr?) {ask about_})
				(pEvent
					claimed: FALSE
					type: evKEYBOARD
					message: (User echo?)
				)
			)
			(MENU_RETYPE
				(pEvent
					claimed: FALSE
					type: evKEYBOARD
					message: (User echo?)
				)
			)
			(MENU_INVENTORY
				(if (PrintCantDoThat $0400) (gInv showSelf: gEgo))
			)
			(MENU_CHARACTER      ; Really represents death stats
				((ScriptID 972 0) init: doit:)
			)
			(MENU_NOTES
				(= gVertButtons 1)
				(= button
					(Print
						{What would you like to view?}
						#title
						{Your Notes:}
						#button
						{ Images_}
						0
						#button
						{ Letters_}
						1
						#button
						{ Journal Entries_}
						2
						#button
						{ Exit_}
						3
					)
				)
				(switch button
					(0
						(Print 997 12)
					)
					(1 ;(viewBooks)
						(if [gNotes 6]
							(Print 0 70 #font 4)	
						else
							(Print 997 13)	
						
						)
					)
					(2
						
						(ViewLetters)
					)
				)
				(= gVertButtons 0)
			)
			(MENU_SWITCH
				(if gSwitchingAllowed
					(= gSwitchingAllowed 2)
				)
				;((ScriptID 205 0) init: doit:)
				;(if gAnotherEgo
				;	(= gAnotherEgo 0)
				;else
				;	(= gAnotherEgo 1)
				;)
				;(RunningCheck)
			)
			(MENU_CHANGESPEED
				(= hGauge (Gauge new:))
				(= newSpeed
					(hGauge
						text: {Game Speed}
						description:
							{Use the mouse or the left and right arrow keys to select the game speed.}
						higher: {Faster}
						lower: {Slower}
						normal: NORMAL_SPEED
						doit: (- 15 gSpeed)
					)
				)
				(gGame setSpeed: (- 15 newSpeed))
				(DisposeScript GAUGE_SCRIPT)
			)
			(MENU_FASTERSPEED
				(if gSpeed (gGame setSpeed: (-- gSpeed)))
			)
			(MENU_NORMALSPEED
				(if gSpeed (gGame setSpeed: 12))
			)
			(MENU_SLOWERSPEED
				(if (< gSpeed 15) (gGame setSpeed: (++ gSpeed)))
			)
			(MENU_VOLUME
				(= hGauge (Gauge new:))
				(= newVolume
					(hGauge
						text: {Sound Volume}
						description:
							{Use the mouse or the left and right arrow keys to adjust the volume.}
						higher: {Louder}
						lower: {Softer}
						normal: 15
						doit: (DoSound sndVOLUME newVolume)
					)
				)
				(DoSound sndVOLUME newVolume)
				(DisposeScript GAUGE_SCRIPT)
			)
			(MENU_TOGGLESOUND (ToggleSound))
		)
	)
)

(procedure (ToggleSound &tmp SOUND_OFF)
	(= SOUND_OFF (DoSound sndSET_SOUND))
	(= SOUND_OFF (DoSound sndSET_SOUND (not SOUND_OFF)))
	(if SOUND_OFF
		(SetMenu MENU_TOGGLESOUND smMENU_TEXT {Turn On})
	else
		(SetMenu MENU_TOGGLESOUND smMENU_TEXT {Turn Off})
	)
)
;(procedure (ToggleSound &tmp SOUND_OFF)
;	(if(DoSound sndSET_SOUND) ; if true, mute
;           (DoSound sndSET_SOUND FALSE)
;           (SetMenu MENU_TOGGLESOUND smMENU_TEXT "Turn On")

;	else  ; must be false, unmute
;            (DoSound sndSET_SOUND TRUE)
;            (SetMenu MENU_TOGGLESOUND smMENU_TEXT "Turn Off")
;    )
;)
