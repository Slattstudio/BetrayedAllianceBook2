;;; Sierra Script 1.0 - (do not remove this comment)
(script# 38)
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
	
	rm038 0
	
)
(local
	
	sitting = 0
	myEvent
	helpMode = 0	; 1 = askHelp, 2 = giveHelp
	
	stealingPaper = 0
	
	;strawInOven = 0
	;logInOven = 0
)

(instance rm038 of Rm
	(properties
		picture scriptNumber
		north 0
		east 46
		south 39
		west 0
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript setRegions: 205)
		
		(SetUpEgo)
		(gEgo init:)
		(RunningCheck)
		
		(giveHelp init: hide: setPri: 15)
		(askHelp init: hide: setPri: 15)
		
		(actionEgo init: hide: ignoreActors:)
		(cabinetDoor init: ignoreActors: setPri: 2 setScript: cabinetScript)
		(chair init: ignoreActors:)
		(log init: setPri: 2)
		(oven init: setPri: 1 setScript: fireScript)
		(kindling init: hide: setPri: 2)
		(kindlingBox init: setPri: 1)
		(mouse init: hide: setCycle: Walk ignoreActors: ignoreControl: ctlWHITE setScript: mouseScript)
		(paper init: hide: ignoreActors: setPri: 0)
		
		
		(switch g38Fire
			(1	; straw
				(kindling show: loop: 3)
			)
			(2	; log
				(kindling show: loop: 7)
				(log hide:)
			)
			(3	; straw and log
				(kindling show: loop: 5)
				(log hide:)
			)
			(4	; fire
				(kindling show: loop: 4 setCycle: Fwd cycleSpeed: 2)
				(log hide:)
			)
		)
		
		
		(switch gPreviousRoomNumber
			(39
				(gEgo hide:)
				(actionEgo init: show: posn: (+ (chair x?) 12) (+ (chair y?) 1) view: 374 loop: 3 cel: 3)
				(RoomScript changeState: 5)	; standing up from chair		
			)
			(46 
				(PlaceEgo 233 104 1)
				;(gEgo posn: 233 104 loop: 1)
			)
			
			(else 
				(PlaceEgo 150 100 1)
				;(gEgo posn: 150 100 loop: 1)
			)
		)
		
		
		(if (== gSwitchedRoomNumber 39)
			(dude init: setPri: 15)
		)
		
		;g49PaperTaken 1 = taken from room 49, 2 = taken back to room 49, 3 = on floor in room 49 ; 4 hiding behind oven
		(if (== g49PaperTaken 1)
			(if (not [gNotes 5])
				(mouseScript changeState: 1)
			)
			;(= g49PaperTaken 2)
		)
		;(dude init: setPri: 15)
	)
)

(instance RoomScript of Script
	(properties)
	
	(method (doit)
		(super doit:)
		; code executed each game cycle
		(if (== stealingPaper 1)
			(paper show: posn: (+ (mouse x?) 20) (- (mouse y?) 5) )	
		)
		(if (== stealingPaper 2)
			(paper show: posn: (- (mouse x?) 15) (- (mouse y?) 1) )	
		)
		
		(if (& (gEgo onControl:) ctlMAROON)
			(gRoom newRoom: 46)
		)
		(if sitting 1
			(askHelp show:)
			(giveHelp show:)
			(= gMap 1)	; disallows egomovement
		else
			(askHelp hide:)
			(giveHelp hide:)
			(= gMap 0)
			(= helpMode 0)
		)
		
		
		(= myEvent (Event new: evNULL))
		(if (checkEvent myEvent (- (askHelp nsLeft:) 10) (+ (askHelp nsRight:) 10) (- (askHelp nsTop:) 5) (+ (askHelp nsBottom:) 30))
			(askHelp cel: 1)
			(giveHelp cel: 0)
			(= helpMode 1)
		else
			(if (checkEvent myEvent (- (giveHelp nsLeft:) 10) (+ (giveHelp nsRight:) 10) (- (giveHelp nsTop:) 5) (+ (giveHelp nsBottom:) 30))	
				(askHelp cel: 0)
				(giveHelp cel: 1)
				(= helpMode 2)
			else
				(askHelp cel: 0)
				(giveHelp cel: 0)
				(= helpMode 0)
			)
		)
		
		(myEvent dispose:)
	)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if (& (pEvent modifiers?) emRIGHT_BUTTON)
				(if (checkEvent pEvent (chair nsLeft?) (chair nsRight?) (chair nsTop?) (chair nsBottom?))
					(if (== gSwitchedRoomNumber 39)	; ego in other room
						(PrintOther 38 41)
					else
						(PrintOther 38 40)
					)	
				)
				(if (checkEvent pEvent 55 73 57 105)	; blockaded hallway
					(PrintOther 38 14)
				)
				(if (checkEvent pEvent 243 258 54 111)	; open hallway
					(PrintOther 38 62)
				)
				(if (checkEvent pEvent (cabinetDoor nsLeft?) (cabinetDoor nsRight?) (cabinetDoor nsTop?) (cabinetDoor nsBottom?))
					(if g49PaperTaken
						(PrintOther 38 43)
					else
						(PrintOther 38 42)		
					)
					(PrintOther 38 44)	
				else
					(if (checkEvent pEvent (log nsLeft?) (log nsRight?) (log nsTop?) (log nsBottom?))
						(PrintOther 38 45)
					else
						(if (checkEvent pEvent (oven nsLeft?) (oven nsRight?) (oven nsTop?) (oven nsBottom?))
							(PrintOther 38 46)
						)	
					)		
				)
				(if (checkEvent pEvent (dude nsLeft?) (dude nsRight?) (dude nsTop?) (dude nsBottom?))
					(if (== gSwitchedRoomNumber 39)	; ego in other room
						(PrintOther 38 47)		
					)
				)
				(if (checkEvent pEvent (kindlingBox nsLeft?) (kindlingBox nsRight?) (kindlingBox nsTop?) (kindlingBox nsBottom?))
					(PrintOther 38 55)	
				)
			)
			(if sitting
				(switch helpMode
					(1
						(askHelpProc)
					)
					(2
						(giveHelpProc)
					)
				)
			)
		)
		(if (Said 'run')
			(Print "There's no reason to do that here.")
		)
		(if (Said 'take,(pick<up)/steel,strip')
			(PrintOther 38 39)	
		)
		(if (or (Said 'pour/oil')
				(Said 'put,rub,use,pour/oil/body,self')
				(Said 'cover/body,self/oil'))
			(if (& (gEgo has: 9) (> gOil 0))
			)
		)	
		;(if (Said 'hi')
		;	(FormatPrint "%u" g38Fire)	
		;)
		(if (Said 'look>')
			(if (Said '/wall,hole')
				(PrintOther 38 61)		
			)
			(if (Said '/fire')
				(if (== g38Fire 4)
					(PrintOther 38 51)	
				else
					(PrintOther 38 58)	
				)
			)
			(if (Said '/log,wood')
				(PrintOther 38 45)	
			)
			(if (Said '/oven,stove,fireplace')
				(PrintOther 38 46)
				(switch g38Fire
					(1	; straw
						(PrintOther 38 48)	
					)
					(2	; log
						(PrintOther 38 49)	
					)
					(3	; straw and log
						(PrintOther 38 50)
					)
					(4	; fire
						(PrintOther 38 51)
					)
				)
			)
			(if (Said '/door, board, handle')
				(if g49PaperTaken
					(PrintOther 38 43)
				else
					(PrintOther 38 42)		
				)
				(PrintOther 38 44)		
			)
			(if (Said '/box,straw,kindling')
				(PrintOther 38 55)	
			)
			(if (Said '/mouse')
				;(switch g49PaperTaken	; 1 = taken from room 49, 2 = taken back to room 49, 3 = on floor in room 49
				(if g49PaperTaken
					(if (== g38Fire 4)
						(PrintOther 38 54)		
					else
						(PrintOther 38 53)
					)		
				else
					(PrintOther 38 52)
				)
			)
			(if (Said '[/!*]')
				(PrintOther 38 0)
				(PrintOther 38 55)
			)
		)
		(if (Said '(ask<about)>')
			(if (Said '/man')
				(if (== gSwitchedRoomNumber 39)	; Ego here
					(if g39Meeting
						(giveHelpProc)	
					else
						(PrintLeah 39 15)
						(PrintHero 39 16)
						(PrintLeah 39 17)
						(PrintHero 39 18)
						(PrintLeah 39 19)
						(PrintHero 39 20)
						(= g39Meeting 1)		
					)		
				else
					(PrintOther 39 7)
				)
			)
			(if (Said '/help,advice')
				(if (== gSwitchedRoomNumber 39)	; Ego here
					(askHelpProc)		
				else
					(PrintOther 39 7)
				)
			)
			(if (Said '/*')
				(if (== gSwitchedRoomNumber 39)	; Ego here
					(PrintHero 38 64)		
				else
					(PrintOther 39 7)
				)	
			)		
		)
		(if (Said 'open/door, cabinet')
			(if (<= (gEgo distanceTo: cabinetDoor)70)
				(cabinetScript changeState: 1)	
			else
				(PrintNotCloseEnough)
			)	
		)
		(if (Said 'talk/man,companion')
			(if (== gSwitchedRoomNumber 39)	; leah here
				(askHelpProc)			
			else
				(PrintOther 39 7)
			)
		)
		
		(if (or (Said 'sit')
			(Said 'get<on/chair'))
			(if sitting
				(PrintYouAre)	
			else
				(if (<= (gEgo distanceTo: chair) 60)
					(self changeState: 1)	
				else
					(PrintNotCloseEnough)
				)
			)	
		)
		(if (or (Said 'stand')
			(Said 'get<out/chair')
			(Said 'get/up'))
			(if sitting
				(if (<= (gEgo distanceTo: chair) 60)
					(self changeState: 5)	
				else
					(PrintNotCloseEnough)
				)
			else
				(PrintYouAre)
			)	
		)
		(if (or (Said 'use,pour/oil,can/door,hinges,cabinet')
				(Said 'oil/door,hinges,cabinet'))
			(if (& (gEgo has: 9) (> gOil 0))
				(Print 38 63)	
			else
				(PrintDontHaveIt)
			)
		)
		(if (or (Said 'use,pour/oil,can/oven,fire,straw')
				(Said 'oil/log,straw'))
			(if (& (gEgo has: 9) (> gOil 0))
				(if (< g38Fire 4)
					(PrintOther 38 60)
				else
					(PrintOther 38 59)
				)
			else
				(PrintDontHaveIt)
			)	
		)
		(if 
			(or 
				(Said 'break/wall')
				(Said 'use/hammer/wall') 
				(Said 'hit/wall/hammer')
			)
			(PrintOther 49 40)
		)
		(if 
			(or 
				(Said 'break/rock,debris')
				(Said 'use/hammer/rock,debris') 
				(Said 'hit/rock,debris/hammer')
			)
			(PrintOther 49 45)
		)
		(if (or (Said 'light,start,make/fire,oven') 
				(Said 'burn,light/kindling,log,wood')
				(Said 'use,hit,strike/steel,flint/steel,flint')
				(Said 'kindle/spark')
				(Said 'strike/flint')) 
			(if (== g38Fire 4)
				(PrintLow 38 8)
			else
				(if (gEgo has: 12) ;flint
					(if (> g38Fire 0)
						(fireScript changeState: 1)
					else
						(PrintOther 38 9)
					)
				else
					(PrintLow 38 10)
				)
			)
		)				
		(if (or (Said 'take,move/log,wood') (Said 'put/log,wood/oven'))
			(if (== g38Fire 4)
				(PrintLow 38 8)
			else
				(if (< g38Fire 2)
					(if (<= (gEgo distanceTo: log) 60)
						(self changeState: 14)
					else
						(PrintNotCloseEnough)
					)
				else
					(PrintOther 38 1)
				)
			)
		)
		
		(if (or (Said 'take,move/straw') (Said 'put/straw/oven'))
			(if (== g38Fire 4)
				(PrintLow 38 8)
			else
				(if (or (== g38Fire 1) (== g38Fire 3))
					(PrintOther 38 1)	
				else				
					(if (<= (gEgo distanceTo: kindlingBox) 60)
						(self changeState: 9)
					else
						(PrintNotCloseEnough)
					)
				)
			)
		)
		; asking for items
		(if (Said 'trade/item')
			(if (== gSwitchedRoomNumber 39)
				(PrintOther 38 38)	
			else
				(PrintOther 38 35)
			)	
		)
		(if (Said 'take,(ask<for),request>')
			(if (Said '/item')
				(if (== gSwitchedRoomNumber 39)
					(PrintOther 38 37)	
				else
					(PrintOther 38 35)
				)	
			)
			(if (Said '/hammer')
				(itemTaker 3)
			)
			(if (Said '/rock,flint')
				(itemTaker 12)
			)
			(if (Said '/oil, can')
				(itemTaker 9)
			)
			(if (Said '/key')
				(itemTaker 8)
			)
			(if (Said '/bug')
				(itemTaker 6)
			)
			(if (Said '/magnet,metal,triangle')
				(itemTaker 1)
			)
			(if (Said '/journal')
				(itemTaker 13)
			)			
		)
		
		;giving items
		(if (Said 'give,hand,pass>')
			(if (Said '/item')
				(if (== gSwitchedRoomNumber 39)
					(PrintOther 38 36)	
				else
					(PrintOther 38 35)
				)	
			)
			(if (Said '/hammer')
				(itemGiver 3)
			)
			(if (Said '/rock,flint')
				(itemGiver 12)
			)
			(if (Said '/oil, can')
				(itemGiver 9)
			)
			(if (Said '/key')
				(itemGiver 8)
			)
			(if (Said '/bug')
				(itemGiver 6)
			)
			(if (Said '/magnet,metal,triangle')
				(itemGiver 1)
			)
			(if (Said '/journal')
				(itemGiver 13)
			)
		)
	)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0 ; Handle state changes
			)
			(1
				(ProgramControl)
				(if (> (gEgo x?) (chair x?)) ; further right than chair
					(gEgo setMotion: MoveTo (+ (chair x?) 28) (+ (chair y?) 1) self)
				else
					(gEgo setMotion: MoveTo (- (chair x?) 12) (+ (chair y?) 1) self)
				)	
			)
			(2	; move to chair
				(gEgo setMotion: MoveTo (+ (chair x?) 12) (+ (chair y?) 1) self)
			)
			(3	; sit down animation
				;(chair x: (- (chair x?) 2) view: 408 loop: 1 cel: 0 setCycle: End self cycleSpeed: 2)
				(gEgo hide:)
				(actionEgo show: posn: (gEgo x?)(gEgo y?) view: 374 loop: 3 cel: 0 setCycle: End self cycleSpeed: 2)	
			)
			(4
				(if (== gSwitchedRoomNumber 39)
					(PlayerControl)
					(= sitting 1)
				else
					(PrintOther 39 9)
					(self cue:)
				)
			)
			(5	; standing up
				(ProgramControl)
				(actionEgo setCycle: Beg self cycleSpeed: 2)	
			)
			(6
				(actionEgo hide:)
				(gEgo show: posn: (actionEgo x?)(actionEgo y?) setMotion: MoveTo (+ (chair x?) 20) (- (chair y?) 0) self)	
			)
			(7
				(gEgo setMotion: MoveTo (+ (chair x?) 30) (- (chair y?) 30) self)
			)
			(8
				(gEgo loop: 2)
				(PlayerControl)
				(= sitting 0)		
			)
			(9	; put straw in oven
				(ProgramControl)
				(gEgo setMotion: MoveTo 210 60 self)
			)
			(10 (= cycles 5)
				(gEgo loop: 3)
				;(actionEgo show: view: 450 loop: 4 cel: 0 posn: (gEgo x?)(gEgo y?) setCycle: End self cycleSpeed: 3)
			)
			(11	
				(PrintLow 38 2)
				(gEgo setMotion: MoveTo 160 60 self)	
			)
			(12	(= cycles 5)
				(gEgo loop: 3)
			)
			(13	; show straw in oven
				(PlayerControl)
				(kindling show: loop: 3 cel: 0)
				(if (> g38Fire 1)	; log is in
					(kindling loop: 5)
					(= g38Fire 3)		
				else	
					(= g38Fire 1)
				)
			)
			(14	; get log
				(ProgramControl)
				(gEgo setMotion: MoveTo 185 60 self)
			)
			(15 (= cycles 5)
				(gEgo loop: 3)
				;(actionEgo show: view: 450 loop: 4 cel: 0 posn: (gEgo x?)(gEgo y?) setCycle: End self cycleSpeed: 3)
			)
			(16	
				(PrintLow 38 11)
				(gEgo setMotion: MoveTo 160 60 self)
				(log hide:)	
			)
			(17	(= cycles 5)
				(gEgo loop: 3)
			)
			(18	; show straw in oven
				(PlayerControl)
				(if (== g38Fire 1)
					(kindling show: loop: 5)
					(= g38Fire 3)
				else
					(kindling show: loop: 7)
					(= g38Fire 2)
				)	
			)
		)
	)
)
(instance cabinetScript of Script
	(properties)
	
	(method (changeState newState button)
		(= state newState)
		(switch state
			(0
			)
			(1
				(ProgramControl)
				(gEgo setMotion: MoveTo (cabinetDoor x?)(+ (cabinetDoor y?) 12) self)	
			)
			(2	(= cycles 5)
				(gEgo loop: 3)
			)
			(3
				(PrintOther 38 56)	
				(PrintOther 38 57)
				(PlayerControl)
			)
		)
	)
)
(instance fireScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0
			)
			; straw without log
			(1	; walk to oven
				(ProgramControl)
				(gEgo setMotion: MoveTo 160 60 self)
			)
			(2	(= cycles 5)
				(gEgo loop: 3)	
			)
			(3
				(if (== g38Fire 3) ; straw and log
					(PrintLow 38 4)
					(PrintLow 38 13)
					(kindling loop: 4 setCycle: Fwd cycleSpeed: 2)
					(= g38Fire 4)	; completed!
					(PlayerControl)
					(if (or (== g49PaperTaken 1) (== g49PaperTaken 4))
						(mouseScript changeState: 2)
					)
				else
					(if (== g38Fire 1) ; just straw
						(self changeState: 4)
					else
						(if (== g38Fire 2) ; just log
							(self changeState: 6)
							
						)	
					)
				)	
			)
			(4
				(PrintLow 38 4)
				(kindling loop: 3 cel: 0 setCycle: End self cycleSpeed: 2)
			)
			(5
				(PlayerControl)
				(PrintLow 38 7)	
				(= g38Fire 0)	; straw is burned away
			)
			; log without Straw
			(6
				(PlayerControl)
				(PrintLow 38 5)
				(Print 38 6)		
			)
		)
	)
)
(instance mouseScript of Script
	(properties)
	
	(method (changeState newState button)
		(= state newState)
		(switch state
			(0
			)
			(1
				(= stealingPaper 1)
				(mouse show: setMotion: MoveTo 140 50 self)	
			)
			(2
				(= stealingPaper 0)
				(if (== g38Fire 4)
					(= stealingPaper 2)
					(= g49PaperTaken 2)
					(mouse posn: 140 40 show: setMotion: MoveTo 90 57 self setPri: 1)	
				else
					(= g49PaperTaken 4)
				)	
			)
			(3
				(= stealingPaper 0)	
			)
		)
	)
)

(procedure (checkEvent pEvent x1 x2 y1 y2)
	(if
		(and
			(> (pEvent x?) x1)
			(< (pEvent x?) x2)
			(> (pEvent y?) y1)
			(< (pEvent y?) y2)
		)
		(return TRUE)
	else
		(return FALSE)
	)
)
(procedure (itemTaker itemNum)
	(if (== (IsOwnedBy itemNum 205) 1)
		(gEgo get: itemNum)
		(PrintOther 39 2) ; Your companion passes the item to you through the hole in the debris.
	else
		(PrintOther 39 3) ; Your companion shakes his head. It looks like he doesn't have that item right now.
	)
)
(procedure (itemGiver itemNum)
	(if (gEgo has: itemNum)
		(gEgo put: itemNum 205)
		(PrintOther 39 0 ) ; You pass the item through the hole to your companion.
	else
		(PrintOther 39 1)	; You don't have that item to give.
	)
)
(procedure (PrintOther textRes textResIndex)
	(if (> (gEgo y?) 90)
		(Print textRes textResIndex
			#width 280
			#at -1 10
		)
	else
			(Print textRes textResIndex
			#width 280
			#at -1 140
		)
	)
)
(procedure (PrintLow textRes textResIndex)
	(Print textRes textResIndex
		#width 280
		#at -1 140
	)
)
(procedure (PrintHero textRes textResIndex)
	(= gWndColor 7)
	(= gWndBack 1)
	(Print textRes textResIndex
		#width 280
		#at -1 140
		#title "You say"		
	)
	(= gWndColor 0)
	(= gWndBack 15)
)
(procedure (PrintLeah textRes textResIndex)
		(= gWndColor 14)
		(= gWndBack 5)
		(Print textRes textResIndex
			#width 280
			#at -1 8
			#title "Leah says"
		)
		(= gWndColor 0)
		(= gWndBack 15)
)
(procedure (askHelpProc)
	(if (== g44WallBroken 3) ; walls actually broken
					(PrintLeah 39 32)
					(PrintHero 39 33)
				else
					(if g42PaintingDown
						(if (== (IsOwnedBy 1 42) 1)	; magnet in room 42
							(PrintLeah 39 36)
						else
							(if (== (IsOwnedBy 1 205) 1)	; ego has magnet
								(PrintLeah 39 27)
								(PrintLeah 39 28)
								(PrintHero 39 29)
								(PrintLeah 39 30)
								(PrintHero 39 31)
							else
								(PrintLeah 39 35)
							)
						)
					else
						(if (== g44WallBroken 2)	; hit, but not broken
							(PrintLeah 39 25)
							(PrintHero 39 26)		
						else
							(if (== g44WallBroken 1)	; observed, but not hit
								(if (gEgo has: 3)	; leah has hammer								
									(PrintLeah 39 24)
								else
									(PrintLeah 39 23)
								)	
							else	; wall has not been observed as weak
								(if (== (IsOwnedBy 8 41) 1)	; if hallway door unlocked
									(PrintLeah 39 34)
								else
									(if (or (gEgo has: 8) (== (IsOwnedBy 8 46) TRUE))	; Leah has key
										(PrintLeah 39 22)		
									else
										(if g39Meeting
											(PrintLeah 39 21)	
										else
											(PrintLeah 39 15)
											(PrintHero 39 16)
											(PrintLeah 39 17)
											(PrintHero 39 18)
											(PrintLeah 39 19)
											(PrintHero 39 20)
											(= g39Meeting 1)		
										)
									)
								)
							)
						)	
					)	
				)
)
(procedure (giveHelpProc)
	(if (not g48Bookshelf)	; has ego gotten past bugs?
		(if (not gEgoOiled)
			(if [gDeaths 9]		; eaten by insects		
				(PrintHero 38 18)
				(PrintLeah 38 19)
			else
				(PrintHero 38 31)
				(PrintLeah 38 19)
			)
		else
			(PrintHero 38 32)
			(PrintLeah 38 33)
		)		
	else
		(if g48WallBroken
			(if (gEgo has: 8)	; key
				(PrintHero 38 22)
			else
				(if g49PaperTaken
					(PrintHero 38 23)
				else
					; if gEgo has all notes
					(PrintLeah 38 26)	
				)
			)
		else
			(PrintHero 38 20)
			(PrintLeah 38 21)	
		)
	)
)
(instance actionEgo of Prop
	(properties
		y 100
		x 190
		view 374
		loop 1
	)
)
(instance chair of Prop
	(properties
		y 145
		x 148
		view 16
		loop 4
	)
)
(instance dude of Prop
	(properties
		y 240
		x 180
		view 220
		cel 4
	)
)
(instance log of Prop
	(properties
		y 45
		x 191
		view 89
		loop 1
	)
)
(instance oven of Prop
	(properties
		y 55
		x 164
		view 89
	)
)
(instance cabinetDoor of Prop
	(properties
		y 44
		x 138
		view 89
		loop 2
		cel 1
	)
)
(instance kindling of Prop
	(properties
		y 45
		x 164
		view 89
		loop 3
	)
)
(instance kindlingBox of Prop
	(properties
		y 50
		x 210
		view 89
		loop 6
	)
)
(instance mouse of Act
	(properties
		y 50
		x 99
		view 81
	)
)
(instance paper of Prop
	(properties
		y 170
		x 100
		view 62
	)
)
(instance askHelp of Prop
	(properties
		y 160
		x 110
		view 998
		loop 0
		cel 0	
	)
)
(instance giveHelp of Prop
	(properties
		y 160
		x 240
		view 998
		loop 1
		cel 0
	)
)

