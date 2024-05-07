;;; Sierra Script 1.0 - (do not remove this comment)
(script# 266)
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
	rm266 0
)

(local
	
	birdAnimation = 0
	animation = 0
	paperAirplane = 0 ; when true, paper will posn just under the bird
	paperOnTop = 0 ; true when paper is on top of the building
	paperGone = 0
	
)

(instance rm266 of Rm
	(properties
		picture scriptNumber
		north 41
		east 246
		south 0
		west 22
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript setRegions: 205 setRegions: 200)
		
		(if gKneeHealed
			(if (not gSeparated)
				(leahProp init: setScript: leahScript) 
			)
		)
		(switch gPreviousRoomNumber
			(22 
				(PlaceEgo 20 160 0)
				(leahProp posn: 30 150 view: 343 setCycle: Walk ignoreControl: ctlWHITE cycleSpeed: 0 setMotion: MoveTo 97 144 leahScript)
			)
			(41
				(PlaceEgo 50 150 2) 
				;(gEgo posn: 50 150 loop: 2)
				(gTheMusic number: 10 loop: -1 priority: -1 play:)	
			)
			(246 
				(PlaceEgo 300 160 1)
				;(gEgo posn: 300 160 loop: 1)
				(leahProp posn: 290 150 view: 343 setCycle: Walk ignoreControl: ctlWHITE cycleSpeed: 0 setMotion: MoveTo 265 144 leahScript)
			)
			(else 
				(PlaceEgo 20 160 0)
				;(gEgo posn: 20 160 loop: 0)
			)
		)
		
		(SetUpEgo)
		(gEgo init:)
		(RunningCheck)
		
		
		(paperScript changeState: 1)
		(birdScript changeState: 1)
		
		(actionEgo init: hide: setScript: pickUpScript) ; setPri: 14
		(actionProp init: hide: ignoreActors: ignoreControl: ctlWHITE setScript: buildingScript)
		(roof init: setPri: 1)
		(wall init: ignoreActors: setPri: 9)
		(bird init: cycleSpeed: 2 setScript: birdScript ignoreActors: ignoreControl: ctlWHITE xStep: 4 yStep: 4)
		(paper init: hide: ignoreActors: cycleSpeed: 2 setPri: 2 setScript: paperScript ignoreControl: ctlWHITE)
		
		(switch g266paper
			(0	; default position
				(paper show: ignoreActors: cycleSpeed: 2 setPri: 2 setScript: paperScript ignoreControl: ctlWHITE)
			)
			(1	; on the roof
				(= paperOnTop 1)
				(paper show: posn: 233 70)
				(bird view: 303 posn: 233 65)
				(birdScript changeState: 2)
			)
			(2	; gone
				(paper show: hide:)
				(bird hide:)
				(wall cel: 4)
				(roof hide:)
			)
		)
	)
)

(instance RoomScript of Script
	(properties)
	
	(method (doit)
		(super doit:)
		; code executed each game cycle
		
		(if (<= (gEgo distanceTo: paper) 60)
			(if (== g266paper 0)
				(if (not animation)
				; bird capture
					(birdScript changeState: 6)
					(paperScript changeState: 4)
					(pickUpScript changeState: 1)
					(= animation 1)
					(= paperGone 1)
				)
			)
		)
	)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if (& (pEvent modifiers?) emRIGHT_BUTTON)
				(if
					(checkEvent pEvent (leahProp nsLeft?) (leahProp nsRight?) (leahProp nsTop?) (leahProp nsBottom?))
					(if gKneeHealed
						(if (not gSeparated)
							(PrintOther 200 29)	
						)
					)
				)
				(if (checkEvent pEvent (bird nsLeft?) (bird nsRight?) (bird nsTop?) (bird nsBottom?))
					(switch g266paper
						(0	; bird in original posn
							(PrintLeah 266 12)
							(PrintMan 266 13)
						)
						(1	; birdn on paper
							(PrintOther 266 14)
						)
					)
				)
				(if
					(checkEvent pEvent (paper nsLeft?) (paper nsRight?) (paper nsTop?) (paper nsBottom?)
						
					)
					(switch g266paper
						(0	; bird in original posn
							(PrintOther 266 15)
						)
						(1	; birdn on paper
							(PrintOther 266 16)
						)
					)
				)
				(if
					(checkEvent pEvent (wall nsLeft?) (wall nsRight?) (wall nsTop?) (wall nsBottom?)	
					)
					(if (== g266paper 2)
						(PrintOther 266 17)	
					else
						(PrintOther 266 18)
					)
				)
				(if
					(checkEvent pEvent (roof nsLeft?) (roof nsRight?) (roof nsTop?) (roof nsBottom?)	
					)
					(if (== g266paper 2)
						(PrintOther 266 26)	
					)
				)
				(if
					(or (checkEvent pEvent 136 180 91 118 ; doorway	
					) (checkEvent pEvent 134 186 119 143))
					(PrintOther 266 19)
				)	
			)
		)
		
		; handle Said's, etc...
		(if (Said 'talk/leah,woman')
			(if gKneeHealed
				(if (not gSeparated)
					(switch g266paper
						(0	; bird in original posn
							(PrintLeah 266 23)
						)
						(1	; birdn on paper
							(PrintLeah 266 9)
						)
						(2	
							(PrintLeah 266 24)	
						)
					)
				else
					(Print 0 20)
				)
			) ; no else, because you shouldn't be here without knee being healed	
		)		
		(if (or (Said '(crawl<in,under),enter,(go<in)/hole,building,wall')
			(Said 'crawl')
			(Said 'climb<in/hole,building,wall'))
			(if gSeparated
				(if (== (gEgo onControl:) ctlMAROON)
					(PrintOK)
					(RoomScript changeState: 3)
				else
					(PrintNotCloseEnough)
				)
			else
				(PrintCantDoThat)
			)
		)
		(if (Said 'open/door')
			(PrintOther 266 25)	
		)
		(if(Said 'take,(pick<up)/paper,note,letter')
			(if (not paperOnTop)
				(if (not paperGone)
					(ProgramControl)
					(gEgo setMotion: MoveTo (paper x?) (paper y?))	
				else
					(PrintOther 266 2)
				)	
			else
				(PrintOther 266 4)
			)	
		)
		(if(Said 'break/wall')
			;(ShakeScreen 1)
			;(wall setCycle: End cycleSpeed: 1)
			(PrintOther 266 3)
		)
		(if (or (Said 'climb,(jump<up,over)/building,roof,wall') 
			(Said 'help,lift/leah,woman')
			(Said 'give/hand, boost/leah, woman')
			(Said 'give/leah, woman/hand, boost'))
			(if paperOnTop
				(buildingScript changeState: 1)
			else
				(if gSeparated
					(PrintOther 266 1)	
				else
					(PrintOther 266 2)
				)
			)
		)
		(if (Said 'look>')
			(if (Said '/rock')
				(PrintOther 266 5)
			)
			(if (Said '/door')
				(PrintOther 266 19)
			)
			(if (Said '/roof')
				(if (== g266paper 2)
					(PrintOther 266 26)	
				else
					(PrintOther 266 22)
				)
			)
			(if (Said '/hole,entrance')
				(if (== g266paper 2)
					(PrintOther 266 17)	
				else
					(PrintOther 266 18)
				)
			)
			(if (Said '/building,house,wall')
					(PrintOther 266 6)
				(if (== g266paper 2)	; holes in building
					(PrintOther 266 7)
				else
					(PrintOther 266 18)
				)
			)
			(if (Said '/bird')
				(switch g266paper
					(0	; bird in original posn
						(PrintLeah 266 12)
						(PrintMan 266 13)
					)
					(1	; birdn on paper
						(PrintOther 266 14)
					)
					(2
						(PrintOther 266 20)			
					)
				)	
			)
			(if (Said '/paper,note,letter')
				(switch g266paper
					(0	; bird in original posn
						(PrintOther 266 15)
					)
					(1	; birdn on paper
						(PrintOther 266 16)
					)
					(2
						(PrintOther 266 20)	
					)
				)		
			)
			(if (Said '/forest,verlorn')
				(PrintOther 0 105)	
			)
			(if (Said '[/!*]')
				(PrintOther 266 0)
				(switch g266paper
					(0	; bird in original posn
						(PrintOther 266 15)
					)
					(1	; birdn on paper
						(PrintOther 266 16)
					)
				)
			)
		)
	)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0 ; Handle state changes
			)
			(3
				(ProgramControl)
				(gEgo setMotion: MoveTo 58 144 self)	
			)
			(4	; Entering the Building
				(actionEgo show: posn: (gEgo x?) (gEgo y?) view: 422 cel: 0 setCycle: End self cycleSpeed: 2)
				(gEgo hide:)		
			)
			(5
				(gRoom newRoom: 41)
			)
		)
	)
)

(instance buildingScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0
			)
			
			(1 ; Ego Moving to support Leah's Jump
				(ProgramControl) (SetCursor 997 (HaveMouse))
				(gEgo setMotion: MoveTo 240 145 self ignoreControl: ctlWHITE)
			)
			(2
				; Ego positions and Leah prepares to jump
				(gEgo hide:)
				; EGO
				(actionEgo show: posn: 240 145 view: 413 loop: 0 cel: 0 setCycle: End cycleSpeed: 2)
				; Leah
				(actionProp show: posn: (leahProp x?)(leahProp y?) view: 343 setCycle: Walk setMotion: MoveTo 230 175 self cycleSpeed: 0)
				(leahProp hide:)
			)
				; get into position to jump
			(3 (= cycles 21) 
				(actionProp loop: 3)
			)
			(4
				; running to leap
				(leahProp hide:)
				(actionProp show: posn: 230 175 view: 351 setMotion: MoveTo 240 156 self)	
			)
			(5
				; prepping jump
				(actionProp view: 361 loop: 0 cel: 0 setCycle: End self cycleSpeed: 2)	
			)
			(6
				; jumping 
				(actionProp yStep: 5 setMotion: MoveTo 240 115 self)
				(actionEgo loop: 1 cel: 0 setCycle: CT setPri: 5)
					
			)
			(7
				; pulling self up
				(actionProp loop: 4 cel: 0 setCycle: End self)
				(actionEgo hide:)
				(gEgo show: loop: 2 observeControl: ctlWHITE)
				; bird fly away
				(birdScript cycles: 0)
				(birdScript changeState: 10)
				(gEgo setMotion: MoveTo (gEgo x?) 160 ignoreControl: ctlWHITE)
			)
			(8	; walking to position
				(gTheMusic fade:)
				
				(actionProp view: 343 posn: (- (actionProp x?) 5) (- (actionProp y?) 37) setCycle: Walk yStep: 2 setMotion: MoveTo 232 67 self setPri: 15)	
			)
			(9 (= cycles 7)	; facing camera
				(actionProp view: 1 loop: 2)
				;(PlayerControl)	(SetCursor 999 (HaveMouse))
				(ShakeScreen 2)
				(gEgo setMotion: NULL observeControl: ctlWHITE loop: 3)
			)
			(10	;(= cycles 10)
				(ShakeScreen 1)
				(roof setCycle: End cycleSpeed: 0)
				(actionProp show: view: 346 posn: 232 67 setCycle: End cycleSpeed: 1 setPri: 0 )
				(paper loop: 3 cel: 0 setCycle: End self cycleSpeed: 2)
					
			)
			(11 (= cycles 10)
				(ShakeScreen 1)
				(wall setCycle: End cycleSpeed: 1)	
			)
			(12	(= cycles 1)
				(PrintMan 266 10)
				(PrintMan 266 11)
			)
			(13
				(= gSeparated 1)
				(= gAnotherEgo 1)
				(= gEgoMovementType 0)
				;(RunningCheck)
				;(= g266paper 1)
				; setting up the x,y, and loop values for the first time separated
				(= [gPrevXY 0] (gEgo x?))
				(= [gPrevXY 1] (gEgo y?))
				(= [gPrevXY 2] (gEgo loop?))
				(gRoom newRoom: 40)	
			)
		)
	)
)

(instance birdScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0
			)
			(1
				(bird loop: 4 cel: 0 setCycle: End self)
			)
			(2 (= cycles (Random 30 60))
			)
			(3
				(= birdAnimation (Random 0 10))
				(if (> birdAnimation 4)
					(birdScript changeState: 4)		
				else
					(birdScript changeState: 1)	
				)
			)
			(4 (= cycles 10)
				(bird loop: 2 cel: 0 setCycle: Fwd)	
			)
			(5
				(bird setCycle: CT)
				(birdScript changeState: 2)	
			)
			(6 (= cycles 0); bird flying down to paper
				(ProgramControl) (SetCursor 997 (HaveMouse))
				(bird view: 302 setMotion: MoveTo 100 100 self)	
			)
			(7
				; flying animation
				(bird view: 301 setCycle: Walk setMotion: MoveTo 132 157 self)	
			)
			(8
				(bird setMotion: MoveTo 233 65 self)
				(paper xStep: 4 yStep: 4 setMotion: MoveTo 233 70)	
				;(= paperAirplane 1)
			)
			(9	; paper now on building with bird
				(bird view: 303)
				(birdScript changeState: 2)
				(= animation 0)
				(= paperOnTop 1)
				(= g266paper 1) ; paper on roof
				;(= paperAirplane 0)
				(PlayerControl)	(SetCursor 999 (HaveMouse))
				(PrintOther 266 8)
			)
			(10
				(bird view: 301 setCycle: Walk setMotion: MoveTo 320 40 self)
				
			)
			(11
				(bird hide:)	
			)
		)
	)
)

(instance paperScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0
			)
			(1
				(if (== g266paper 0)
					(if (not paperGone)
						(paper setCycle: End self)	
					)
				)
			)
			(2 (= cycles (Random 10 40))
					(if (not paperGone)
						(paper setCycle: CT))
					)
			(3
				(if (not paperGone)
					(paperScript changeState: 1)
				)
			)
			(4
				(paper cel: 0)
			)
		)
	)
)

(instance pickUpScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0
			)
			(1	(= cycles 8)
			)
			(2
				; walk to metal
				(= gEgoMovementType 1)
				(RunningCheck)
				(gEgo setMotion: MoveTo (+ (paper x?) 17) (paper y?) self ignoreControl: ctlWHITE)
			)
			(3
				; bend down 
				(gEgo hide:)
				(actionEgo show: view: gEgoPickUpView loop: 1 cel: 0 posn: (gEgo x?) (gEgo y?) setCycle: End self cycleSpeed: 2)	
			)
			(4	(= cycles 10)
				
			)
			(5	; stand back up
				(actionEgo setCycle: Beg self)				
			)
			(6
				(actionEgo hide:)
				(gEgo show: loop: 0 observeControl: ctlWHITE)
				(= gEgoMovementType 0)
				(RunningCheck)
				(PlayerControl)
			)
		)
	)
)

(instance leahScript of Script
	(properties)
	
	(method (changeState newState rando)
		(= state newState)
		(switch state
			(1	(= cycles (Random 20 150))
				(leahProp view: 1 loop: 5 setCycle: Fwd cycleSpeed: 5)	
			)
			(2
				(leahProp loop: 6 cel: 0 setCycle: End self)	
			)
			(3	(= cycles (Random 20 150))
				(leahProp loop: 2)	
			)
			(4
				(= rando (Random 7 8))
				(leahProp loop: rando setCycle: End self)	
			)
			(5
				(leahProp setCycle: Beg self)
			)
			(6
				(self cycles: 0 changeState: (Random 1 4))	
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
(procedure (PrintOther textRes textResIndex)
	(Print textRes textResIndex		
		#width 280
		#at -1 10
	)
)
(procedure (PrintLeah textRes textResIndex)
	(= gWndColor 14)
	(= gWndBack 5)
	(Print textRes textResIndex
		#width 160
		#at 160 10
		#title "Leah:"
	)
	(= gWndColor 0)
	(= gWndBack 15)
)
(procedure (PrintMan textRes textResIndex)
	(= gWndColor 7)
	(= gWndBack 1)
	(Print textRes textResIndex
		#width 160
		#at 60 140
		#title "You say:"		
	)	
	(= gWndColor 0)
	(= gWndBack 15)
)
(procedure (PrintBottom textRes textResIndex)
	(Print textRes textResIndex		
		#width 280
		#at -1 140
	)
)

(instance bird of Act
	(properties
		y 65
		x 60
		view 303
	)
)
(instance leahProp of Act
	(properties
		y 90
		x 90
		view 1
	)
)
(instance paper of Act
	(properties
		y 157
		x 132
		view 62
	)
)
(instance roof of Prop
	(properties
		y 70
		x 233
		view 62
		loop 1
	)
)
(instance wall of Prop
	(properties
		y 146
		x 58
		view 62
		loop 2
	)
)
(instance actionEgo of Prop
	(properties
		y 67
		x 232
		view 346
	)
)
(instance actionProp of Act
	(properties
		y 170
		x 202
		view 343
	)
)