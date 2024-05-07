;;; Sierra Script 1.0 - (do not remove this comment)
(script# 39)
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
	
	rm039 0
	
)
(local
	
	sitting = 0
	myEvent
	helpMode = 0	; 1 = askHelp, 2 = giveHelp
	
)

(instance rm039 of Rm
	(properties
		picture scriptNumber
		north 38
		east 0
		south 39
		west 47
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript setRegions: 205)
		
		(SetUpEgo)
		(gEgo init:)
		(RunningCheck)
		
		(chair init: ignoreActors:)
		
		(giveHelp init: hide: setPri: 15)
		(askHelp init: hide: setPri: 15)
		
		(switch gPreviousRoomNumber
			(38
				(gEgo hide:)
				(chair show: view: 408 loop: 1 cel: 3)
				(RoomScript changeState: 4)	; standing up from chair		
			)
			(47 
				(PlaceEgo 90 144 0)
				;(gEgo posn: 90 144 loop: 0)
			)
			(else 
				(PlaceEgo 150 130 1)
				;(gEgo posn: 150 100 loop: 1)
			)
		)
		
		
		
		(if (== gSwitchedRoomNumber 38)
			(leah init: setPri: 1)
		)
	)
)

(instance RoomScript of Script
	(properties)
	
	(method (doit)
		(super doit:)
		; code executed each game cycle
		(if (& (gEgo onControl:) ctlGREY)
			(gRoom newRoom: 47)
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
			(if (& (pEvent modifiers?) emRIGHT_BUTTON)
				(if	
					(checkEvent
						pEvent
						(chair nsLeft?)(chair nsRight?)(chair nsTop?)(chair nsBottom?)
					)
					(PrintOther 39 13)
					(return)
				)
				(if (== (OnControl ocSPECIAL (pEvent x?) (pEvent y?)) ctlNAVY)	; hole in rubble
					(if (not (== gSwitchedRoomNumber 38))
						(PrintOther 39 11)
					)	
				else
					
					(if (== (OnControl ocPRIORITY (pEvent x?) (pEvent y?)) ctlGREEN)	; rubble
						(PrintOther 39 12)	
					)
				)
				(if	
					(checkEvent	pEvent 128 192 121 156)	; door to outside
					(PrintOther 39 8)
				)
				(if	
					(checkEvent
						pEvent
						(leah nsLeft?)(leah nsRight?)(leah nsTop?)(leah nsBottom?)
					)
					(if (== gSwitchedRoomNumber 38)
						(PrintOther 39 10)	
					)
				)
				
			)
		)	
				
		; handle Said's, etc...
		(if (Said 'run')
			(Print "There's no reason to do that here.")
		)
		(if 
			(or 
				(Said 'break/wall')
				(Said 'use/hammer/wall') 
				(Said 'hit/wall/hammer')
			)
			(PrintOther 49 40)
		)
		(if (Said 'look>')
			(if (Said '/woman, leah')
				(if (== gSwitchedRoomNumber 38)
					(PrintOther 39 10)	
				else
					(PrintOther 39 37)
				)	
			)
			(if (Said '/rubble, debris, wall')
				(PrintOther 39 12)		
			)
			(if (Said '/chair')
				(PrintOther 39 13)		
			)
			(if (Said '/door')
				(PrintOther 39 8)		
			)				
			(if (Said '/hole')
				(if (not (== gSwitchedRoomNumber 38))
					(PrintOther 39 11)	; no one there
				else
					(PrintOther 39 10)	; leah there
				)		
			)
			(if (Said '[/!*]')
				(PrintOther 39 40)
			)
		)
		(if 
			(or 
				(Said 'break/wall,door,rubble,rock')
				(Said 'use/hammer/wall,door,rubble,rock') 
				(Said 'hit/wall,door,rubble,rock/hammer')
			)
			(PrintOther 39 38)
		)
		(if (Said 'stand/chair')
			(PrintOther 39 39)	
		)
		(if (Said 'talk>')
			(if (Said '/woman,leah,companion')
				(if (== gSwitchedRoomNumber 38)	; leah here
					(askHelpProc)	

				else
					(PrintOther 39 7)
				)
			)		
		)
		(if (Said 'open/door')
			(PrintOther 39 8)		
		)
		(if (Said '(ask<about)/leah,woman')
			(if (== gSwitchedRoomNumber 38)	; leah here
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
			(if (Said '/help,advice')
				(if (== gSwitchedRoomNumber 38)	; leah here
					(askHelpProc)		
				else
					(PrintOther 39 7)
				)
			)
			(if (Said '/*')
				(if (== gSwitchedRoomNumber 38)	; leah here
					(PrintLeah 38 64)		
				else
					(PrintOther 39 7)
				)	
			)		
		)
		
		(if (or (Said 'pour/oil')
			(Said 'put,rub,use,pour/oil/body,self')
			(Said 'cover/body,self/oil'))
			(if (& (gEgo has: 9) (> gOil 0))
				(if (not gEgoOiled)
					(PrintOther 45 3)
					(= gEgoOiled 1)
					(-- gOil)
					((gInv at: 9) count: gOil)
				else
					(Print "You already did that.")
				)
			else
				(Print "You can't do that right now.")
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
					(self changeState: 4)	
				else
					(PrintNotCloseEnough)
				)
			else
				(PrintYouAre)
			)	
		)
		; asking for items
		(if (Said 'trade/item')
			(if (== gSwitchedRoomNumber 38)
				(PrintOther 38 38)	
			else
				(PrintOther 38 35)
			)	
		)
		(if (Said 'take,(ask<for),request>')
			(if (Said '/item')
				(if (== gSwitchedRoomNumber 38)
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
			(if (Said '/oil')
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
				(if (== gSwitchedRoomNumber 38)
					(PrintOther 38 36)	
				else
					(PrintOther 38 35)
				)	
			)
			(if (Said '/magnet,metal,triangle')
				(itemGiver 1)
			)
			(if (Said '/rock,flint')
				(itemGiver 12)
			)
			(if (Said '/hammer')
				(itemGiver 3)
			)
			(if (Said '/oil')
				(itemGiver 9)
			)
			(if (Said '/key')
				(itemGiver 8)
			)
			(if (Said '/bug')
				(itemGiver 6)
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
			(1	; move to sit down
				(ProgramControl)
				(gEgo setMotion: MoveTo (- (chair x?) 11) (- (chair y?) 9) self)
			)
			(2	; sit down animation
				(chair x: (- (chair x?) 2) view: 408 loop: 1 cel: 0 setCycle: End self cycleSpeed: 2)
				(gEgo hide:)	
			)
			(3
				(if (== gSwitchedRoomNumber 38)
					(PlayerControl)
					(= sitting 1)
				else
					(PrintOther 39 9)
					(self cue:)
				)	
			)
			(4	; standing up
				(ProgramControl)
				(chair setCycle: Beg self cycleSpeed: 2)	
			)
			(5
				(gEgo show: loop: 3 posn: (- (chair x?) 11) (- (chair y?) 9))
				(chair x: (+ (chair x?) 2) view: 19 loop: 6)
				(PlayerControl)
				(= sitting 0)
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
	(if (== (IsOwnedBy itemNum 210) 1)
		(gEgo get: itemNum)
		(PrintOther 39 2) ; Your companion passes the item to you through the hole in the debris.
	else
		(PrintOther 39 4) ; Your companion shakes her head. It looks like she doesn't have that item right now. 
	)
)
(procedure (itemGiver itemNum)
	(if (gEgo has: itemNum)
		(gEgo put: itemNum 210)
		(PrintOther 39 0 ) ; You pass the item through the hole to your companion.
	else
		(PrintOther 39 1)	; You don't have that item to give.
	)
)
(procedure (PrintOther textRes textResIndex)
	(Print textRes textResIndex
		#width 280
		#at -1 10
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
(procedure (giveHelpProc)
	(if (== g44WallBroken 3) ; walls actually broken
					(PrintLeah 39 32)
					(PrintHero 39 33)
				else
					(if g42PaintingDown
						(if (== (IsOwnedBy 1 42) 1)	; magnet in room 42
							(PrintLeah 39 36)
						else
							(if (gEgo has: 1)	; ego has magnet
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
								(if (== (IsOwnedBy 3 210) 1)	; leah has hammer								
									(PrintLeah 39 24)
								else
									(PrintLeah 39 23)
								)	
							else	; wall has not been observed as weak
								(if (== (IsOwnedBy 8 41) 1)	; if hallway door unlocked
									(PrintLeah 39 34)
								else
									(if (or (== (IsOwnedBy 8 210) 1) (== (IsOwnedBy 8 46) TRUE))	; Leah has key
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
(procedure (askHelpProc)
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

(instance leah of Prop
	(properties
		y 103
		x 166
		view 1
		loop 5
	)
)
(instance chair of Prop
	(properties
		y 120
		x 176
		view 19
		loop 6
	)
)
(instance askHelp of Prop
	(properties
		y 20
		x 100
		view 998
		loop 0
		cel 0	
	)
)
(instance giveHelp of Prop
	(properties
		y 20
		x 220
		view 998
		loop 1
		cel 0
	)
)

