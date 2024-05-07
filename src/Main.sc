;;; Sierra Script 1.0 - (do not remove this comment)
;
; SCI Template Game
; By Brian Provinciano
; ******************************************************************************
; main.sc
; Contains the game's main instance and inventory items.

(script# MAIN_SCRIPT)
(include sci.sh)
(include game.sh)
(use controls)
(use sound)
(use syswindow)
(use cycle)
(use game)
(use inv)
(use user)
(use menubar)
(use feature)
(use obj)
(use disposeload)


(public
	Template 0
	SetUpEgo 1
	SetUpActor 2
	ProgramControl 3
	PlayerControl 4
	DisposePrintDlg 5
	PrintOK 6
	PrintItIs 7
	PrintYouAre 8
	PrintGoodIdea 9
	PrintNotCloseEnough 10
	PrintAlreadyTookIt 11
	PrintDontHaveIt 12
	PrintCantDoThat 13
	AddViewToPic 14
	SetInvIOwner 15	
	IsOwnedBy 16
	runProc 17
	RunningCheck 18
	PlaceEgo 19
	ViewLetters 20
	ReadTempFile 21
	RunSneakWalk 22
)

(local

	[gDeaths 35] = 	[0
					 0 0 0 0 0 0 0 0 0 0	; 1 - 10
					 0 0 0 0 0 0 0 0 0 0	; 11 - 20
					 0 0 0 0 0 0 0 0 0 0	; 21 - 30
					 0 0 0 0]				; 31 - 34
	; saved in the Game script
	
	fileHandle
	[str 35]	; Used to read from an external file and update several variables
	
	; 0 - Number of deaths
	; 1 - Defeated in battle
	; 2 - Killed by wild animals
	; 3 - Fell off a cliff
	; 4 - Organ failure
	; 5 - Beheaded
	; 6 - Eaten by local flora
	; 7 - Froze to death
	; 8 - Killed by a creature
	; 9 - Defleshed by insects
	; 10- Poisoned
	; 11- Turned to Stone
	; 12 - Disintegrated
	
	gUniqueDeaths = 0 ; Add +1 to this each time ego dies, unless he's already died in that manner before.
	gSaveDeaths = 0
	
	gIconY = 4 	; Used to set the Icon's Y-position in the window. Default is 4. Typically used with Death
	gDeathIconEnd = 0	; Used to determine if the "death icon" will End or go on indefinitely
	gDeathIconTop = 0   ; When TRUE, Death Icon will be on Top, on the left side is DEFAULT
		
	;deathWindowOpen = 0		; Used as a switch to determine when you can close the deathWindow
	;deathWindow 			; Used to open the display window
	;deathCountFori = 0 					; Used in the "for"
	;textDown = 7 			; Used to place statements progressively lower than earlier statements
	;textRight = 20 			; Used to move statements right, once y-axis full - NOT YET IMPLEMENTED
	
	gAskedLeah = 30			; Used for text resources early game when both characters are together
	
	;gInvNum = 0
	;gInvDrop = 0
	
	
; Variables that effect particular rooms

	; Collectible Fruits
	g81Fruit = 0
	g243Fruit = 0
	
	; Collectible Bugs
	g236Bug = 0
	
	; Letters
	;g33Letter = 0
	
	; Mushrooms
	g233Mushroom = 0
	g235Mushroom = 0
	g257Mushroom = 0
	
	; Other Room Specific Variables
	g200AskedAll = 0
	[gSign 5] = [1 0 0 0 0]
	
	g34EyePod = 0
	
	g58LogState = 0 ; 0 in room 58, but not in position
					; 1 in position in room 58
					; 2 in posision in room 59
					; 3 in position in room 61
	
	g210BridgeCollapse = 0
	g227WaterfallBreached = 0
	g233Wurm = 0
	g243GateOpen = 0
	g246KnockedDown = 0
	g255Baptized = 0
	g257Entering = 0
	
	[g261Points 3] = [0 0 0] ; Used for rotating thingie on pedestal
	
	g263RockState = 0	; 0 - on cliff, 1 - on ground, 2 - in the mud
	g266paper = 0		; 0 - paper on ground, 1 - paper on ceiling, 2 - paper in building
	g271Solved = 0
	g303egoHealed = 0
	
	; Dorm Room Variables
	gInDorm = 0
	g38Fire = 0		; 1 = straw alone, 2 = log alone, 3 = both together, 4 = fire
	g39Meeting = 0	; have ego and leah talked in the between space yet?
	g40ChestUnlocked = 0
	g42PaintingDown = 0
	g42ChestUnlocked = 0
	g43DoorOiled = 0
	g44ChestUnlocked = 0
	g48WallBroken = 0 ; this wall is covered by bookcase
	g48Bookshelf = 0	; 1 = room has been accessed, 2 = shelf has been permanently moved
	g44WallBroken = 0 ; 1= observed, but not hit 2 = hit, not broken, 3 = botken - this wall leads to outside
	g47WallBroken = 0 ; this wall leads to central area on ego's side
	g49PaperTaken = 0	; 1 = taken from room 49, 2 = taken back to room 49, 3 = on floor in room 49
	
;
; * These are the global variables. You can access them from any script as long
; * as it "use"es this script
	gEgo
; points to the ego's class
	gGame
; points to the game instance
	gRoom
; points to the current room instance
	gSpeed
; the game speed (delay each interpreter cycle)
	gQuitGame =  FALSE
; if set to TRUE, the game will exit
	gCast
; points to the cast class (list of actors)
	gRegions
; points to the regions class (list of regions)
	gLocales
; points to the locales class (list of locales)
	gTimers
; points to the timers class (list of timers)
	gSounds
; points to the sounds class (list of sounds)
	gInv
; points to the inventory class
	gAddToPics
; points to the add to pics class
	gFeatures
; points to the add to features class
	gSFeatures
; points to the add to sfeatures class
	gRoomNumberExit
; room number exit
	gPreviousRoomNumber
; the number of the previous room
	gRoomNumber
; the number of the current room
	gDebugOnExit =  FALSE
; enter debug mode on room exit
	gScore =  0
; the game score
	gMaxScore =  53
; the maximum game score
	gOldScore
; previous score
	gCurrentCursor
; the number of the current cursor
	gNormalCursor =  999
; the number of the normal cursor (ie. arrow)
	gLoadingCursor =  997
; the number of the loading cursor (ie. hand)
	gDefaultFont =  1
; the number of the default font
	gSaveRestoreFont =  0
; the number of the font for the save/restore dialogs
	gDeadFont =  0
; the number of the font for the dialog when ego dies
	gUserEvent
; points to the user's event object
	gPrintDlg
; points to the current Print() window
	gVolume
; the sound volume
	gVersion
; the version string
	gSaveDirPtr
; points to the save directory string
	gCheckAniWait
; the checkAni delay
	gSetRegions
; a flag -- see User:doit()
	gPicAngle
; the room's pic angle
	gOverlay =  -1
; whether to overlay the picture when drawing
	gDefaultPicAni
; the default pic animation
	gDefaultPalette
; the default palette to use for the pictures (0-3)
	gCastMotionCue
; if set, the cast's motionCue() is called
	gTheWindow
; points to the window class
	gWndColor
; the colour of the game's windows foreground (ie. text)
	gWndBack
; the colour of the game's windows background
	gOldPort
; the previous port
	gEgoView
; ego's current view number
; hh:mm:ss | gTimeHours:gTimeMinutes:gTimeSeconds
; the time elapsed since the game started
	gTimeSeconds
; the seconds
	gTimeMinutes
; the minutes
	gTimeHours
; the hours
	gCurrentTime
; the current time in seconds
	gTheMusic
; points to the music class
	gTheSoundFX
; points to the sound fx class
	gProgramControl



; Collectibles
	gHoneyNum = 0
	gMushrooms = 0
	gBugs = 0
	gOil = 2
	gEyes = 0

; Arcade and Puzzle Variables
	gTimeCh =  0    ; Used in the Menubar Script to disable player's speed customization
	gArcStl =  0    ; Used in the User Script to disable the Spacebar from retyping, allowing for other functions
	gMap =  0       ; Used in the User Script to disable gEgo movement when map is up
	gNoClick =  0   ; Used in the User Script to disable gEgo movement with click (only direction pad allowed.)
	
; OTHER VARIABLES OF MINE

	; Difficulty Settings
	gRightClickSearch = 1
	gEasyParser = 1
	gYellowTips = 1
	
	gAnotherEgo =  1	; FALSE when Ego is main character, TRUE when Ego is a different View/character
	
	gSwitchedRoomNumber =  210	; Stores the Room of Ego and Leah during the Forest-switching part
	gSameRoomSwitch = 0	; used for odd occastions when switching is allowed in the same room
	gSeparated = 0
	gKneeHealed = 0
	gSwitchingAllowed = 0
	gDisableSwitch = 0 	; when true, disable momentary switching of characters
	
	[gAnotherEgoXYL 3] = [1 1 0]	; Leah's x, y, and loop number
	[gPrevXY 3] = [294 114 2]	; Ego's x y, and loop number
	[gArray 6]	; Seems to be used to store coordinates for each when switching movement speeds
	[dirMap 4] = [3 7 5 1] ; Determines direction for ego probably
	
	gVertButtons =  0   ; When TRUE, buttons in Print commands will be Verical. Horizontal is DEFAULT
	
	gTeleporting =  0
	gLookingAhead = 0	; TRUE when Leah is at a height looking at surrounding screens
	
	gFollowed =  0 ; If TRUE, an emeny is following gEgo from across rooms
	gMovementLocked = 0	; If TRUE, player cannot change movement from walk/run/sneak
	
	gEgoOiled = 0
	
; BATTLE SCREEN VARIABLES
	

; VARIABLES FROM BOOK 1 THAT MAY OR MAY NOT NEED TO BE CHANGED
	[gName 15]
	gAg =  15
	gStr =  15
	gLuk =  15
	gDef =  15
	gInt =  15
	gHlth =  30 ; 30 is start
	gMaxHlth =  30
	gExp =  60
	;Collectibles
	gGold =  10
	gApple =  0 ; Actually is for darts (haven't changed the name)
	gFlask =  0
	gFullFlask =  0
	[gNotes 7] = [0 0 0 0 0 0 0]	; first 6 (0-5) are notes from the missing Tellyn book
								; "6" is Letter from 2 treasure hunters, seeking Longeau
								; 0 in room 246
								; 1 in room 41
								; 2 in room 47
								; 3 in room 42 and 44
								; 4 in room 40
								; 5 in room 49
	[gArmor 4] = [0 0 0 0]   

; ego's current view number
	gEgoStoppedView =  903
	gEgoPickUpView = 232
	
	gEgoMovementType = 0	; 0 = walking, 1 = running, 2 = sneaking
	gSwitchedMovement = 0
	
;	gEgoRunning =  0
	gRunClick =  0
;	gEgoSneak = 0
	gEgoDark = 0

)
(instance Template of Game
	(properties)
	
	(method (init)
		; Set up the base window
		(= gTheWindow theWindow)
		(= gWndColor clBLACK)
		(= gWndBack clWHITE)
		(gTheWindow color: gWndColor back: gWndBack)
		; Initialize
		(super init:)
;
;         * Set your game version here *
		(= gVersion {1.0})
		; General initialization stuff
		(= gVolume 15)
		(DoSound sndVOLUME gVolume)
		(SL code: statusCode)
		(TheMenuBar init:)
		(scoreSound owner: self init:)
		(= gTheMusic theMusic)
		(gTheMusic owner: self init:)
		(= gTheSoundFX theSoundFX)
		(gTheSoundFX owner: self init:)
		
		(= gEgo ego)
		(User alterEgo: gEgo blocks: 0 y: 150)
		(Load rsFONT gDeadFont)
		(Load rsFONT gDefaultFont)
		(Load rsFONT gSaveRestoreFont)
		(Load rsCURSOR gNormalCursor)
		(Load rsCURSOR gLoadingCursor)
		(if (HaveMouse)
			(gGame setCursor: gNormalCursor SET_CURSOR_VISIBLE)
		else
			(gGame
				setCursor: gNormalCursor SET_CURSOR_VISIBLE 304 174
			)
		)
;
;         * Initialize the inventory with it's items here *
		(Inv add: 	Nothing
					Magnet
					Honey 
					Hammer					
					Rack 
					Mushroom 
					Bugs 
					Stick 
					RoomKey 
					OilCan 
					CabKey 
					Eye
					Flint
					Journal
					Triangle
					Rope
		)		
		
		; Start the room
		(if (GameIsRestarting)
			(self newRoom: INITROOMS_SCRIPT)
		else
			(self newRoom: TITLESCREEN_SCRIPT)
		)
	)
	
	(method (doit)
		(super doit:)
		(if (gEgo isStopped:)             ; changes ego to standing still view
			(gEgo view: gEgoStoppedView loop: (gEgo loop?) cel: 0 setMotion: NULL)
		else
			(gEgo view: gEgoView)
		)
		
		(if gProgramControl
			(User canControl: false canInput: false)
			(SetCursor 997 1)
			(= gCurrentCursor 997)
		)
		
		(if
			(!=
				gCurrentTime
				(= gCurrentTime (GetTime gtTIME_OF_DAY))
			)
			(if (>= (++ gTimeSeconds) 60)
				(= gTimeSeconds 0)
				(++ gTimeMinutes)
				(if (>= gTimeMinutes 60)
					(= gTimeMinutes 0)
					(++ gTimeHours)
				)
			)
		)
		(if (> gOldScore gScore)
			(= gOldScore gScore)
			(SL doit:)
		)
		(if (< gOldScore gScore)
			(= gOldScore gScore)
			(SL doit:)
		)
	)
	
	(method (replay)
		(TheMenuBar draw:)
		(SL enable:)
; This is where you will import all the variables from the External File
		(= fileHandle (FOpen "persist.tmp" 1)) ;there's a historical typo in SCI.SH
		(if (!= fileHandle -1)
			(ReadTempFile 0)
			(ReadTempFile 1)
			(ReadTempFile 2)
			(ReadTempFile 3)
			(ReadTempFile 4)
			(ReadTempFile 5)
			(ReadTempFile 6)
			(ReadTempFile 7)
			(ReadTempFile 8)
			(ReadTempFile 9)
			;
			(ReadTempFile 10)
			(ReadTempFile 11)
			(ReadTempFile 12)
			(ReadTempFile 13)
			(ReadTempFile 14)
			(ReadTempFile 15)
			(ReadTempFile 16)
			(ReadTempFile 17)
			(ReadTempFile 18)
			(ReadTempFile 19)
			;
			(ReadTempFile 20)
			(ReadTempFile 21)
			(ReadTempFile 22)
			(ReadTempFile 23)
			(ReadTempFile 24)
			(ReadTempFile 25)
			(ReadTempFile 26)
			(ReadTempFile 27)
			(ReadTempFile 28)
			(ReadTempFile 29)
			;
			(ReadTempFile 30)
			(ReadTempFile 31)
			(ReadTempFile 32)
			(ReadTempFile 33)
			(ReadTempFile 34)
		)
		(FClose fileHandle)
		
		(if (and (== [gDeaths 0] 1) gYellowTips)
			(= gWndColor 0)
			(= gWndBack 14)
			(Print 0 106 #font 4 #button "Ok")
			(= gWndColor 0)
			(= gWndBack 15)	
		)
		
		(if (DoSound sndSET_SOUND)
			(SetMenu MENU_TOGGLESOUND #text {Turn Off})
		else
			(SetMenu MENU_TOGGLESOUND #text {Turn On})
		)
		(super replay:)
		
		
	)
	
	(method (newRoom roomNum picAni)
		(DisposePrintDlg)
		(Load rsFONT gDeadFont)
		(Load rsFONT gDefaultFont)
		(Load rsFONT gSaveRestoreFont)
		(Load rsCURSOR gNormalCursor)
		(Load rsCURSOR gLoadingCursor)
		(super newRoom: roomNum)
		(if (< argc 2)
			(= gDefaultPicAni (Random 0 5))
		else
			(= gDefaultPicAni picAni)
		)
		(if (< gRoomNumber 500)
			(SaveGame (gGame name?) 0 "Autosave" gVersion)
		)
		
	)
	
	(method (startRoom roomNum)
		(DisposeLoad
			NULL
			FILEIO_SCRIPT
			JUMP_SCRIPT
			EXTRA_SCRIPT
			WINDOW_SCRIPT
			TIMER_SCRIPT
			FOLLOW_SCRIPT
			REV_SCRIPT
			DCICON_SCRIPT
			DOOR_SCRIPT
			AUTODOOR_SCRIPT
			WANDER_SCRIPT
			AVOID_SCRIPT
			DPATH_SCRIPT
		)
		(DisposeScript DISPOSELOAD_SCRIPT)
		(if gDebugOnExit (= gDebugOnExit FALSE) (SetDebug))
		(gTheSoundFX stop: number: 1)
		(super startRoom: roomNum)
		(if (== gTheSoundFX 11) (gEgo baseSetter: NormalBase))
		
	)
	
	(method (changeScore addScore)
		(= gScore (+ gScore addScore))
		(if (> addScore 0) );(scoreSound playMaybe:))
	)
	
	(method (handleEvent pEvent &tmp i)
		
		; ////////////////////////////////////////////////////////////////////
		; ** This is debug functionality                                   //
		; ** Comment it out if you don't want people to cheat in your game //
		; ////////////////////////////////////////////////////////////////////
		(if (== evKEYBOARD (pEvent type?))
			; Check for ALT keys
			(switch (pEvent message?)
				($2f00 (Show 1)) ; alt-v ; Show visual
				($2e00 (Show 4)) ; alt-c ; Show control
				($1900 (Show 2)) ; alt-p ; Show priority
				($3200      ; alt-m
					; Show memory usage
					(ShowFree)
					(FormatPrint
						{Free Heap: %u Bytes\nLargest ptr: %u Bytes\nFreeHunk: %u KBytes\nLargest hunk: %u Bytes}
						(MemoryInfo miFREEHEAP)
						(MemoryInfo miLARGESTPTR)
						(>> (MemoryInfo miFREEHUNK) 6)
						(MemoryInfo miLARGESTHUNK)
					)
				)
				($1400      ; alt-t
					; teleport to room
					(gRoom newRoom: (GetNumber {Which Room Number?}))
				)
				
				($1700      ; alt-i
					; get inventory
					(gEgo get: (GetNumber {Which inventory#?}))
				)
				($1100      ; alt-w
					; Show cast
					;(gCast eachElementDo: #showSelf)
					(if (gRoom north:)
						(gRoom newRoom: (gRoom north:))
					)
				)
				($1e00      ; alt-a
					; Show cast
					;(gCast eachElementDo: #showSelf)
					(if (gRoom west:)
						(gRoom newRoom: (gRoom west:))
					)
				)
				($1f00      ; alt-s
					; Show cast
					;(gCast eachElementDo: #showSelf)
					(if (gRoom south:)
						(gRoom newRoom: (gRoom south:))
					)
				)
				($2000      ; alt-d
					; Show cast
					;(gCast eachElementDo: #showSelf)
					(if (gRoom east:)
						(gRoom newRoom: (gRoom east:))
					)
				)
			)
		)
		; //////////////////////////////////////////////////
		; End of debug functionality                     //
		; //////////////////////////////////////////////////
		(super handleEvent: pEvent)
		
		(if
		(or (!= (pEvent type?) evSAID) (pEvent claimed?))
			(return TRUE)
		)

; Add global said statements here

		(if (Said 'hi')
			 (Print 0 40) ;{Well hello to you too!})
		)
		(if (Said 'thank')
			 (Print 0 102) ;{Well hello to you too!})
		)
		(if (Said 'inv')
			 (gInv showSelf: gEgo)
		)
		(if (Said 'look>')
			(cond
				((= i (gInv saidMe:))
					(if (i ownedBy: gEgo)
					(i showSelf:)
					else
						(PrintDontHaveIt)
					)
				)
			)
		)
		(if (Said 'look>')
			;(if (Said '/woman,leah')
			;	(if gAnotherEgo
			;		(Print 0 91)	
			;	else
			;		(Print 0 92)		
			;	)	
			;)
			(if (Said '/*')
				(Print 0 30)	
			)
		)
		(if (Said 'search/*')
			(Print 0 94)	
		)
		(if (Said 'ask<about>')
			(Print 0 20)	; no one answers.
		)
		(if (Said 'use/*/*')
			(Print 0 101)	
		)
		(if (Said 'use/*')
			(Print 0 86)	
		)					
					
; This should be removed/changed before launch		
		(if (Said 'switch')
			(if gAnotherEgo
				(= gAnotherEgo 0)
			else
				(= gAnotherEgo 1)
			)
			(RunningCheck)
		)
		
		(if (Said 'walk')
			(RunSneakWalk 0 0 343 (pEvent x?) (pEvent y?) 3 2)		
		)	
		(if (Said 'run')
			(RunSneakWalk 1 230 351 (pEvent x?) (pEvent y?) 5 4)
		)
		(if (Said 'sneak')
			(RunSneakWalk 2 0 352 (pEvent x?) (pEvent y?) 2 1)
			; ego can't sneak, so this extra code:
			(if (and (== gEgoMovementType 2) (not gAnotherEgo))
				(Print	0 45)
				(= gEgoMovementType 0)
				(gEgo xStep: 3 yStep: 2)
				(runProc)
			)
		)
		(if (Said 'move/*')
			(Print 0 88)
		)
		(if (Said 'smell')
			(Print 0 53)
		)
		(if (Said 'kiss,hug/*')
			(Print 0 22)
		)
		(if (Said 'kill,break/*')
			(Print 0 23)
		)
		(if (Said 'take,(pick<up)/*')
			(Print 0 51)
		)
		(if (Said 'climb/*')
			(Print 0 81)	
		)
		(if (Said 'eat/*')
			(Print 0 82)	
		)
		(if (Said 'pee,poop,jump')
			(Print 0 83)	
		)
		(if (Said 'fuck,bitch,damn')
			(Print 0 84)	
		)
		(if (Said '(take<off),remove/clothes,clothing')
			(Print 0 85)	
		)
		(if (Said 'sleep,rest')
			(Print 0 93)	
		)
		(if (Said 'talk>')
			(if (Said '[/!*]')
				(Print 0 95)
			)
			(if (Said '[/*]')
				(Print 0 99)
			)				
		)
		(if (Said 'release,throw,drop/bug')
			(Print 0 96)	
		)
		(if (Said 'throw/*')
			(Print 0 97)	
		)
		(if (Said 'open/*')
			(Print 0 98)	
		)
		(if (Said 'rub,pour/oil>')
			(if (Said '[//!*]')
				(if (& (gEgo has: 9) (> gOil 0))
					(Print 0 104)
				else
					(PrintCantDoThat)
				)
			)
			(if (Said '//*')
				(if (& (gEgo has: 9) (> gOil 0))
					(Print 0 103)
				else
					(PrintCantDoThat)
				)	
			)	
		)
			(return FALSE)
	)
)


(class Iitem of InvI
	(properties
		said 0
		description 0
		owner 0
		view 0
		loop 0
		cel 0
		script 0
		count 0
	)
	
	(method (showSelf &tmp [text1 100] [text2 130] [nounName 30])
		(if (> count -1)
           (Print (Format @text2 description count) #title objectName #icon view loop cel)
		else
			(Print
			0
			description
			#title
			objectName
			#icon
			view
			loop
			cel
			)
		)	   
	)
)


(instance statusCode of Code
	(properties)
	
	(method (doit param1)
		(Format
			param1
			{__$etrayed #lliance $ook *____[score: %d of %-3d]}
			gScore
			gMaxScore
		)
	)
)


(instance ego of Ego
	(properties
		y 1111
		x 0
		z 0
		heading 0
		yStep 2
		view 0
		loop 0
		cel 0
		priority 0
		underBits 0
		signal $2000
		nsTop 0
		nsLeft 0
		nsBottom 0
		nsRight 0
		lsTop 0
		lsLeft 0
		lsBottom 0
		lsRight 0
		brTop 0
		brLeft 0
		brBottom 0
		brRight 0
		cycleSpeed 0
		script 0
		cycler 0
		timer 0
		illegalBits $8000
		xLast 0
		yLast 0
		xStep 3
		moveSpeed 0
		blocks 0
		baseSetter 0
		mover 0
		looper 0
		viewer 0
		avoider 0
		edgeHit 0
	)
)


(instance scoreSound of Sound
	(properties
		state 0
		number SCORE_SOUND
		priority 10
		loop 1
		handle 0
		signal 0
		prevSignal 0
		client 0
		owner 0
	)
)


(instance theMusic of Sound
	(properties
		state 0
		number 1
		priority 0
		loop 1
		handle 0
		signal 0
		prevSignal 0
		client 0
		owner 0
	)
)


(instance theSoundFX of Sound
	(properties
		state 0
		number 1
		priority 5
		loop 1
		handle 0
		signal 0
		prevSignal 0
		client 0
		owner 0
	)
)


(instance theWindow of SysWindow
	(properties)
	
	(method (open)
		(if (< (Graph grGET_COLOURS) 9)
			(if (or (< color 7) (== color 8))
				(= color 0)
				(= back 15)
			else
				(= color 15)
				(= back 0)
			)
		)
		(super open:)
	)
)


(instance NormalBase of Code
	(properties)
	
	(method (doit &tmp temp0)
		(if (== gRoomNumberExit 253)
			(= temp0 22)
		else
			(= temp0 10)
		)
		(gEgo brBottom: (+ (gEgo y?) 1))
		(gEgo brTop: (- (gEgo brBottom?) (gEgo yStep?)))
		(gEgo brLeft: (- (gEgo x?) temp0))
		(gEgo brRight: (+ (gEgo x?) temp0))
	)
)

;
; * THE PUBLIC PROCEDURES
	
(procedure (SetUpEgo theLoop theView)
	(PlayerControl)
	(gEgo edgeHit: EDGE_NONE)
	(switch argc
		(0
			(SetUpActor gEgo (gEgo loop?) gEgoView)
		)
		(1
			(SetUpActor gEgo theLoop gEgoView)
		)
		(2
			(SetUpActor gEgo theLoop theView)
		)
	)
)


(procedure (SetUpActor pActor theLoop theView)
	(if (> argc 1) (pActor loop: theLoop))
	(if (> argc 2) (pActor view: theView))
	(pActor
		setLoop: -1
		setPri: -1
		setStep: 3 2
		setCycle: Walk
		illegalBits: $8000
		cycleSpeed: 0
		moveSpeed: 0
		ignoreActors: 0
	)
)

(procedure (ProgramControl)
	(User canControl: false canInput: false)
	(gEgo setMotion: NULL ignoreControl: ctlWHITE)
	(SetCursor 997 (HaveMouse))
	(= gCurrentCursor 997)
	(= gProgramControl 1)
	(= gDisableSwitch 1)
)


(procedure (PlayerControl)
	(User canControl: true canInput: true)
	(gEgo setMotion: NULL observeControl: ctlWHITE)
	(SetCursor 999 (HaveMouse))
	(= gCurrentCursor 999)
	(= gProgramControl 0)
	(= gDisableSwitch 0)
)


(procedure (DisposePrintDlg)
	(if gPrintDlg (gPrintDlg dispose:))
)


(procedure (PrintOK)
	(Print {O.K.})
)


(procedure (PrintItIs)
	(Print 0 34 ) ; "It is."
)


(procedure (PrintYouAre)
	(Print 0 35) ; {You are.})
)


(procedure (PrintGoodIdea)
	(Print 0 36) ;{Good idea. You might try that again later.})
)


(procedure (PrintNotCloseEnough)
	(Print 0 37) ;{You're not close enough.})
)


(procedure (PrintAlreadyTookIt)
	(Print 0 38) ;{You already took it.})
)


(procedure (PrintDontHaveIt)
	(Print 0 39) ;{You don't have it.})
)


(procedure (PrintCantDoThat mem)
	(if (> (MemoryInfo miLARGESTPTR) mem)
		(return TRUE)
	else
		(Print 0 46) ;{You can't do that here; at least, not now.})
		(return FALSE)
	)
)


(procedure (AddViewToPic pView &tmp hView)
	(if pView
		(= hView (View new:))
		(hView
			view: (pView view?)
			loop: (pView loop?)
			cel: (pView cel?)
			priority: (pView priority?)
			posn: (pView x?) (pView y?)
			addToPic:
		)
		(pView posn: (pView x?) (+ 1000 (pView y?)))
	)
)


(procedure (SetInvIOwner index owner &tmp hInvI)
	(= hInvI (gInv at: index))
	(if (< argc 2)
		(hInvI owner: gRoomNumberExit)
	else
		(hInvI owner: owner)
	)
)
(procedure (IsOwnedBy invItem roomOrActor &tmp checkObject)
	(= checkObject (gInv at: invItem))
	(if (IsObject checkObject)
		(return (checkObject ownedBy: roomOrActor))
	else
		(return 0)
	)
)
(procedure (runProc)
	(if (not (gEgo isStopped:))
		(if gRunClick
			(gEgo setMotion: MoveTo [gArray 0] [gArray 1])
		else
			(gEgo setDirection: [dirMap (gEgo loop?)])
		)
		(= [gArray 0] 0)
		(= [gArray 1] 0)
	)
)
(procedure (RunningCheck)
	(switch gEgoMovementType
		(0	; walking
			(if gAnotherEgo
				(if gEgoDark
					(gEgo view: 7 xStep: 3 yStep: 2)
					(= gEgoView 7)	
				else
					(gEgo view: 343 xStep: 3 yStep: 2)
					(= gEgoView 343)
				)
			else	
				(gEgo view: 0 xStep: 3 yStep: 2)
				(= gEgoView 0)
			)
		)
		(1	; running
			(if gAnotherEgo
				(gEgo view: 351 xStep: 5 yStep: 4)
				(= gEgoView 351)
			else
				(gEgo view: 230 xStep: 5 yStep: 4)
				(= gEgoView 230)
			)
		)
		(2	; sneaking
			(gEgo view: 352 xStep: 2 yStep: 1)
			(= gEgoView 352)	
		)
	)
;	(if gEgoSneak
;		(gEgo view: 352 xStep: 2 yStep: 1)
;	else	
		; RUNNING
;		(if (== gEgoRunning TRUE)
;			(if gAnotherEgo
;				(gEgo view: 351 xStep: 5 yStep: 4)
;				(= gEgoView 351)
;			else
;				(gEgo view: 230 xStep: 5 yStep: 4)
;				(= gEgoView 230)
;			)
;		else
		; WALKING
;			(if gAnotherEgo
;				(if gEgoDark
;					(gEgo view: 7 xStep: 3 yStep: 2)
;					(= gEgoView 7)	
;				else
;					(gEgo view: 343 xStep: 3 yStep: 2)
;					(= gEgoView 343)
;				)
;			else	
;				(gEgo view: 0 xStep: 3 yStep: 2)
;				(= gEgoView 0)
;			)
;		)
;	)
	(if (not gAnotherEgo)
		(= gEgoStoppedView 903)
		(= gEgoPickUpView 232)
	else
		(= gEgoStoppedView 1)
		(= gEgoPickUpView 450)	
	)
)

(procedure (PlaceEgo x y loop)
	(if (not gTeleporting)
		(gEgo posn: x y loop: loop)
		;(RunningCheck)
	else
		(if gAnotherEgo
			(gEgo posn: [gAnotherEgoXYL 0] [gAnotherEgoXYL 1] loop: [gAnotherEgoXYL 2])
		else
			(gEgo posn: [gPrevXY 0] [gPrevXY 1] loop: [gPrevXY 2])	
		)
		(= gTeleporting 0)
	)
)
(procedure (ViewLetters &tmp i)
	;(Print 0 31 #at -1 20)
	;(= total 0)
	(= gDefaultFont 4)
	(for ( (= i 0)) (< i 6)  ( (++ i)) (if (> [gNotes i] 0)
			(switch i
				(0 (Print 0 63))
				(1 (Print 0 64))
				(2 
					(if (or (< [gNotes 2] 2) (== [gNotes 2] 3))
						(Print 0 68)
						(Print 0 69)
					else
						(Print 0 65)
					)
				)
				(3 
					(if (or (== [gNotes 3] 42)(== [gNotes 3] 44))
						(Print 0 72)
						(Print 0 69)
					else
						(if (== [gNotes 3] 2)
							(Print 0 66)
						)
					)
				)
				;(3 (Print 0 66))
				(4 (Print 0 67))
				(5 (Print 0 71))
				;(6 (Print 0 70))
			)
		)
	)
	(if (not [gNotes 0])
		(Print 0 48); "You don't have any letters or notes yet.")
	)
	(= gDefaultFont 6)
)
(procedure (ReadTempFile i)
	(= [gDeaths i] (ReadNumber (FGets @str 8 fileHandle)))
)
(procedure (RunSneakWalk movementNum maleView femaleView whereX whereY xSpeed ySpeed)
	(if gMovementLocked
		(Print 0 31)	; you can't do that rightnow
	else
		(if (== gEgoMovementType movementNum)
			(Print 0 33)	; you already are
		else
			(= gEgoMovementType movementNum)				
			(gEgo xStep: xSpeed yStep: ySpeed)
			(= [gArray 0] whereX)
			(= [gArray 1] whereY)
			(runProc)
			(if gAnotherEgo
				(= gEgoView femaleView)					
			else
				(= gEgoView maleView)
			)
		)
	)	
)

;
; * THE INVENTORY ITEMS                                                        *
(instance Nothing of Iitem
	(properties)
)


(instance Magnet of Iitem ; Item 1
	(properties
		said '/magnet'
		description 1
		owner 0
		view 620
		loop 0
		cel 0
		script 0
		name "Magnet"
		count -1	; count -1 will allow the description to be read from a text resource
	)
)

(instance Honey of Iitem ; Item 2
	(properties
		said '/fruit, honey'
		description {A plump fruit filled with a sticky honey-like goo.\n\nYou have %d.}
		owner 0
		view 610
		loop 0
		cel 0
		script 0
		name "Honey Fruit"
		count 1
		
	)
)
(instance Hammer of Iitem ; Item 3
	(properties
		said '/hammer'
		description 3
		owner 0
		view 612
		loop 0
		cel 0
		script 0
		name "Hammer"
		count -1
	)
)
(instance Rack of Iitem ; Item 4
	(properties
		said '/rack'
		description 4 ; {A rack that was once used to hold mount musical instruments on a wall.}
		owner 0
		view 613
		loop 0
		cel 0
		script 0
		name "Rack"
		count -1
	)
)
(instance Mushroom of Iitem ; Item 5
	(properties
		said '/mushroom'
		description {This rare mushroom is said to have curative powers.\n\nYou have %d.}
		owner 0
		view 614
		loop 0
		cel 0
		script 0
		count 0
		name "Mushroom"
	)
)
(instance Bugs of Iitem ; Item 6
	(properties
		said '/bug'
		description {A bug with a pungent, unpleasant odor.\n\nYou have %d.}
		owner 0
		view 615
		loop 0
		cel 0
		script 0
		count 1
		name "Stinky Bug"
	)
)
(instance Stick of Iitem ; Item 7
	(properties
		said '/stick'
		description 7 ;{The leftover handle of what used to be a sledge hammer.}
		owner 0
		view 616
		loop 0
		cel 0
		script 0
		name "Stick"
		count -1
	)
)
(instance RoomKey of Iitem ; Item 8
	(properties
		said '/key'
		description 8 ;{A small key likely used for one of the doors in the living quarters.}
		owner 0
		view 617
		loop 0
		cel 0
		script 0
		name "Room Key"
		count -1
	)
)
(instance OilCan of Iitem ; Item 9
	(properties
		said '/can, oil'
		description {An olive oil canister. You expect %u more use(s) before it's empty.}
		owner 0
		view 618
		loop 0
		cel 0
		script 0
		count 2
		name "Oil Can"
	)
)
(instance CabKey of Iitem ; Item 10
	(properties
		said '/key'
		description 10 ; {A tiny key with a small diamond shape on the flat tip.}
		owner 0
		view 619
		loop 0
		cel 0
		script 0
		name "Cabinet Key"
		count -1
	)
)
(instance Eye of Iitem ; Item 11
	(properties
		said '/eye'
		description {It's an eyeball, apparently.\n\nYou have %d.}
		owner 0
		view 611
		loop 0
		cel 0
		script 0
		count 1
		name "Eye"
	)
)
(instance Flint of Iitem ; Item 12
	(properties
		said '/flint'
		description 12 ; A stone of flint. When struck upon steel this mineral can produce heated sparks.
		owner 0
		view 621
		loop 0
		cel 0
		script 0
		count -1
		name "Flint"
	)
)
(instance Journal of Iitem ; Item 13
	(properties
		said '/journal,book'
		description 13 ; 
		owner 0
		view 622
		loop 0
		cel 0
		script 0
		count -1
		name "Journal"
	)
)
(instance Triangle of Iitem ; Item 14
	(properties
		said '/triangle'
		description {A small, magical triangle.\n\nYou have %d.}
		owner 0
		view 623
		loop 0
		cel 0
		script 0
		count 1
		name "Triangle"
	)
)
(instance Rope of Iitem ; Item 15
	(properties
		said '/rope'
		description 15
		owner 0
		view 624
		loop 0
		cel 0
		script 0
		count -1
		name "Rope"
	)
)