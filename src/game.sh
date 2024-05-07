;;; Sierra Script 1.0 - (do not remove this comment)
;
; * SCI Game Header
; * By Brian Provinciano
; *************************************************************************
; * Put all the defines specific to your game in here

; Base Scripts
(define MAIN_SCRIPT 0)
(define AVOID_SCRIPT 973)
(define DOOR_SCRIPT 974)
(define AUTODOOR_SCRIPT 975)
(define INITROOMS_SCRIPT 976)
(define DYING_SCRIPT 977)
(define DISPOSELOAD_SCRIPT 978)
(define CONTROLS_SCRIPT 979)
(define DPATH_SCRIPT 980)
(define DCICON_SCRIPT 981)
(define REV_SCRIPT 982)
(define WANDER_SCRIPT 983)
(define FOLLOW_SCRIPT 984)
(define TIMER_SCRIPT 985)
(define WINDOW_SCRIPT 986)
(define GAUGE_SCRIPT 987)
(define EXTRA_SCRIPT 988)
(define SOUND_SCRIPT 989)
(define SYSWINDOW_SCRIPT 990)
(define JUMP_SCRIPT 991)
(define CYCLE_SCRIPT 992)
(define FILEIO_SCRIPT 993)
(define GAME_SCRIPT 994)
(define INVENTORY_SCRIPT 995)
(define USER_SCRIPT 996)
(define MENUBAR_SCRIPT 997)
(define FEATURE_SCRIPT 998)
(define OBJ_SCRIPT 999)
; Game Scripts
(define TITLESCREEN_SCRIPT 800)
; Defaults
(define NORMAL_SPEED 8)
; Sounds
(define DUMMY_SOUND 1)
(define SCORE_SOUND 900)
(define DEATH_SOUND 2)
; Inventory Items
(define INV_NOTHING 0)
(define INV_MAGNET 1)
(define INV_HONEY 2)
(define INV_HAMMER 3)
(define INV_METAL 4)
(define INV_MUSHROOM 5)
(define INV_BUGS 6)
(define INV_STICK 7)
(define INV_DORMKEY 8)
(define INV_OILCAN 9)
(define INV_CABKEY 10)
(define INV_EYE 11)
(define INV_FLINT 12)
(define INV_JOURNAL 13)
(define INV_TRIANGLE 14)
; Fonts
(define DEFAULT_FONT 0)
(define WINDOW_FONT 1)
(define SMALL_FONT 4)
(define LARGE_FONT 9)
(define DEBUG_FONT 999)
; Menu IDs eg. $302 means third menu, second item
(define MENU_ABOUT $0101)
(define MENU_HELP $0102)
(define MENU_SETTINGS $0103)
(define MENU_RESTART $0201)
(define MENU_SAVE $0202)
(define MENU_RESTORE $0203)
(define MENU_QUIT $0205)
(define MENU_ASKABOUT $0301)
(define MENU_RETYPE $0302)
(define MENU_COLOURS $0303)
(define MENU_INVENTORY $0304)
(define MENU_CHARACTER $0305)
(define MENU_NOTES $0306)
(define MENU_SWITCH $0307)
(define MENU_CHANGESPEED $0401)
(define MENU_FASTERSPEED $0403)
(define MENU_NORMALSPEED $0404)
(define MENU_SLOWERSPEED $0405)
(define MENU_VOLUME $0501)
(define MENU_TOGGLESOUND $0502)

;These are death flags. They're used to track which ways the user has died. 
;They will be shown at the end of the game.

; unique deaths
(define DIE_RESTART -2) ;show Restore, Restart, Quit; (same as not specifying anything at all) instead of Retry, Restore, Quit)
(define DIE_RETRY	-1) ;show a Retry, Restore, Quit
(define DIE_NOFLAG 	-1)	; a retry is shown, but nothing is flagged.
;explicitly define the starting number, ending number and death count, which will be used in the DeathSheet script.
(define DIE_START	450)
(define DIE_COUNT	 79)
(define DIE_END		528)
;there are roughly 100 deaths, so we'll reserve flags 450-550 for them (with room for 50 more in expansion up to flag 600)
(enum 450
	DIE_NOSTAMINA
	DIE_PICKNOSE
	DIE_ARRESTED
	DIE_NIGHTGAUNT
)
