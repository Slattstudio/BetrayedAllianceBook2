;;; Sierra Script 1.0 - (do not remove this comment)
; Score +4
(script# 49)
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
	
	rm049 0
	
)

(local
	
	keyOnWall = 1	
	stealingPaper = 0
	cabinetOpen = 0
	closetOpen = 0

)

(instance rm049 of Rm
	(properties
		picture scriptNumber
		north 0
		east 0
		south 0
		west 48
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript setRegions: 205)
		
		(SetUpEgo)
		(gEgo init:)
		(RunningCheck)
		
		(actionEgo init: hide: ignoreActors: setScript: actionEgoScript)
		(bed init:)
		(bookcase init: ignoreActors: setPri: 9)
		(cabinet init: ignoreActors:)
		(chair init: ignoreActors:)
		(closet init: ignoreActors:)
		(key init: ignoreActors: ignoreControl: ctlWHITE setScript: keyScript)
		(magnet init:)
		(mouse init: hide: ignoreActors: setScript: mouseScript setCycle: Walk xStep: 6 ignoreControl: ctlWHITE)
		(paper init: hide: ignoreActors: setPri: 0)
		(table init: ignoreActors:)
		
		(if (or (gEgo has: 8) (== (IsOwnedBy 8 210) TRUE) (== (IsOwnedBy 8 46) TRUE)) ; if no one has the room key and it's not used
			(key hide:)
			(= keyOnWall 0)	
		)
		(if (or (or (gEgo has: 1) (== (IsOwnedBy 1 210) TRUE)) (== (IsOwnedBy 1 42) TRUE)) ; if the magnet is owned by the characters or the final room
			(magnet loop: 0)	
			(if (or (or (not (gEgo has: 8) ) (== (IsOwnedBy 8 210) TRUE)) (== (IsOwnedBy 8 46) TRUE)); if no one has the room key and it's not used
				(key view: 69 posn: 200 95 loop: 0 cel: 0 setCycle: NULL)
				(= keyOnWall 0)
			)
		)
		
		(switch gPreviousRoomNumber
			(48 
				(PlaceEgo 100 110 0)
				;(gEgo posn: 100 110 loop: 0)
				(if keyOnWall
					(RoomScript changeState: 13)
				)
			)
			(else 
				(PlaceEgo 150 100 1)
				;(gEgo posn: 150 100 loop: 1)
			)
		)		
		
		
		;g49PaperTaken 1 = taken from room 49, 2 = taken back to room 49, 3 = on floor in room 49, 4 = hiding behind oven
		(switch g49PaperTaken
			(0	; normal position, under bed
				(paper show:)
			)
			(1	; take from room by rat
				(paper hide:)	
			)
			(2	; taken back to room because of fire
				(if (== gPreviousRoomNumber 38)
					(mouseScript changeState: 13)
				else
					(paper show:)
					(= g49PaperTaken 0)
				)
			)
			(3	; paper is on the floor
				(if (not [gNotes 5])
					(paper show: posn: 185 135 setPri: 0)
				else
					(paper hide:)
				)	
			)
			(4	; paper in oven room	
				(paper hide:)
			)	
		)
		
		;(if (< g49PaperTaken 3)
		;	(if (not (== g49PaperTaken 0)) 
		;		(paper hide:)
		;		(if (== g38Fire 4)
		;			(if (not [gNotes 5])
		;				(if (== gPreviousRoomNumber 38)
		;					(if (== g49PaperTaken 2)
		;						(mouseScript changeState: 13) ; send mouse back, but drops paper and goes back to hole
		;					else
		;						; paper should be under bed
		;					)
		;				else
		;					;(paper show: posn: 300 90)
		;					;(mouseScript changeState: 10) ; send mouse and paper back to under the bed
		;					(paper show:)	; shows paper under the bed
		;					(= g49PaperTaken 4)
		;				)	
		;			)
		;		)
		;	)
		;)
	)
)

(instance RoomScript of Script
	(properties)
	
	(method (doit)
		(super doit:)
		; code executed each game cycle
		(if (& (gEgo onControl:) ctlGREY)
			(gRoom newRoom: 48)
		)
		(if (== stealingPaper 1)
			(paper posn: (+ (mouse x?) 20) (- (mouse y?) 5) )	
		)
		(if (== stealingPaper 2)
			(paper posn: (- (mouse x?) 15) (- (mouse y?) 1) )	
		)
		(if (>= (gEgo distanceTo: cabinet) 35)
			(if cabinetOpen
				(cabinet setCycle: Beg)
				(= cabinetOpen 0)
			)
		)
		(if (>= (gEgo distanceTo: closet) 35)
			(if closetOpen
				(closet setCycle: Beg)
				(= closetOpen 0)
			)
		)
	)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if (& (pEvent modifiers?) emRIGHT_BUTTON)
				
				(if	
					(checkEvent
						pEvent
						(magnet nsLeft?)(magnet nsRight?)(magnet nsTop?)(magnet nsBottom?)
					)
					(if (or (or (gEgo has: 1) (== (IsOwnedBy 1 210) TRUE)) (== (IsOwnedBy 1 42) TRUE)) ; if the magnet is not owned by the characters or the final room
						; do nothing
					else
						(PrintOther 49 3)	
					)
				)
				
				(if	
					(checkEvent
						pEvent
						(key nsLeft?)(key nsRight?)(key nsTop?)(key nsBottom?)
					)
					(if (or (or (gEgo has: 8)  (== (IsOwnedBy 8 210) TRUE)) (== (IsOwnedBy 8 46) TRUE))
						; do nothing
					else
						(if keyOnWall
							(PrintOther 49 6)
						else
							(PrintOther 49 7)
						)	
					)
				else
					(if	
						(checkEvent
							pEvent
							(closet nsLeft?)(closet nsRight?)(closet nsTop?)(closet nsBottom?)
						)
						(if closetOpen
							(if gRightClickSearch
								(if (and (<= (gEgo distanceTo: closet) 35) (> (gEgo y?) (closet y?)) )
									(self changeState: 1)
								else
									(PrintOther 49 15)
								)
							else
								(PrintOther 49 15)
							)
						else
							(if gRightClickSearch
								(if (and (<= (gEgo distanceTo: closet) 35) (> (gEgo y?) (closet y?)) )
									(self changeState: 1)
								else
									(PrintOther 49 14) ; closed message	
								)
							else
								(PrintOther 49 14) ; closed message
							)
						)
					)
				)
				(if	
					(checkEvent
						pEvent
						(bed nsLeft?)(bed nsRight?)(bed nsTop?)(bed nsBottom?)
					)
					(if gRightClickSearch
						(if (< (gEgo distanceTo: bed) 60)
							(mouseScript changeState: 1)	
						else
							(if g49PaperTaken
								(PrintOther 49 38)	
							else
								(PrintOther 49 4)
							)
						)
					else
						(if g49PaperTaken
							(PrintOther 49 38)	
						else
							(PrintOther 49 4)
						)
					)
				)
				
				(if	
					(checkEvent
						pEvent
						(table nsLeft?)(table nsRight?)(table nsTop?)(table nsBottom?)
					)
					(PrintOther 49 10)
					(if (< (gEgo distanceTo: table) 30)
						(PrintOther 49 11)
						(PrintBook 49 12)		
					)
				)
				(if	
					(checkEvent
						pEvent
						(chair nsLeft?)(chair nsRight?)(chair nsTop?)(chair nsBottom?)
					)
					(PrintOther 48 14)
				)
				(if	
					(checkEvent
						pEvent
						(cabinet nsLeft?)(cabinet nsRight?)(cabinet nsTop?)(cabinet nsBottom?)
					)
					(if cabinetOpen
						(if gRightClickSearch
							(if (and (<= (gEgo distanceTo: cabinet) 35) (> (gEgo y?) (cabinet y?)) )
								(self changeState: 5)
							else
								(if (or (gEgo has: 12) (== (IsOwnedBy 12 210) TRUE))
									(PrintOther 49 34)	
								else
									(PrintOther 49 26)
								)
							)
						else
							(if (or (gEgo has: 12) (== (IsOwnedBy 12 210) TRUE))
								(PrintOther 49 34)	
							else
								(PrintOther 49 26)
							)
						)
					else
						(if gRightClickSearch
							(if (and (<= (gEgo distanceTo: cabinet) 35) (> (gEgo y?) (cabinet y?)) )
								(self changeState: 5)
							else
								(PrintOther 49 13) ; closed message	
							)
						else
							(PrintOther 49 13) ; closed message
						)
					)
				)
				
				(if	
					(checkEvent
						pEvent 180 260 150 180	; rubble
					)
					(PrintShort 49 9)
				else
					(if	
						(checkEvent
							pEvent
							(bookcase nsLeft?)(bookcase nsRight?)(bookcase nsTop?)(bookcase nsBottom?)
						)
						(PrintOther 49 16)
					else
						(if (== (OnControl ocSPECIAL (pEvent x?) (pEvent y?)) ctlLIME)	; rat hole
							(PrintOther 49 8)
						)
					)
				)
			)
		)
		
		; handle Said's, etc...
		(if (Said 'run')
			(Print "There's no reason to do that here.")
		)
		(if (Said 'open,search>')
			(if (Said '/door,cabinet')
				(if (< (gEgo y?) 117)
					(if (> (gEgo distanceTo: cabinet) (gEgo distanceTo: closet))
						;(Print "move to closet")
						(self changeState: 1)
					else
						;(Print "move to cabinet")
						(self changeState: 5)
					)
				else
					(PrintNotCloseEnough)
				)	
			)
		)
		(if (Said 'close/cabinet')
			(if cabinetOpen
				(if (< (gEgo distanceTo: cabinet) 30)
					(= cabinetOpen 0)
					(cabinet setCycle: Beg)
				else
					(PrintNotCloseEnough)
				)
			else
				(if closetOpen
					(if (< (gEgo distanceTo: closet) 30)
						(= closetOpen 0)
						(closet setCycle: Beg)
					else
						(PrintNotCloseEnough)
					)
				else
					(PrintItIs)
				) 	
			) 	
		)
		(if (Said 'read,take/book')
			(if (< (gEgo distanceTo: bookcase) 30)	
				(PrintOther 49 16)	
			else
				(if (< (gEgo distanceTo: table) 30)
					(PrintOther 49 11)
					(PrintBook 49 12)
				else
					(PrintOther 49 36)
				)
			)
		)	
		(if (Said 'take,(pick<up)>')
			(if (Said '/flint')
				(if cabinetOpen
					(if (or (not (gEgo has: 12)) (== (IsOwnedBy 12 210) TRUE))
						(PrintOther 49 29)
						(gEgo get: 12)
						(gGame changeScore: 1)
					else
						(PrintAlreadyTookIt)
					)
				else
					(PrintOther 49 30)
				)
			)
			(if (Said '/obsidian')
				(if cabinetOpen
					(PrintOther 49 28)
				else
					(PrintOther 49 30)
				)
			)
			(if (Said '/rock')
				(if cabinetOpen
					(PrintOther 49 27)
				else
					(PrintOther 49 30)
				)
			)
			(if (Said '/key')
				(if keyOnWall
					(actionEgoScript changeState: 9)		
				else
					(if (gEgo has: 8) ; key (need to add or is ownedBy RoomNumber)
						(PrintAlreadyTookIt)
					else
						(if (< (gEgo y?) 117)
							; get key
							(keyScript changeState: 3)
						else
							(PrintNotCloseEnough)
						)
					)
				)
			)
			(if (Said '/magnet,metal,triangle')
				(if (gEgo has: 1)	; magnet	(need to add or is ownedBy RoomNumber)
					(PrintAlreadyTookIt)
				else
					(if (< (gEgo y?) 117)
						(actionEgoScript changeState: 3)
					else
						(PrintNotCloseEnough)
					)
				)
			)
			(if (Said '/paper,note,letter')
				(if (or (not g49PaperTaken) (== g49PaperTaken 2))
					(if (< (gEgo distanceTo: bed) 60)
						(mouseScript changeState: 1)
					else
						(PrintNotCloseEnough)
					)
				else
					(if (not [gNotes 5])
						(if (== g49PaperTaken 3)	; on ground
							(self changeState: 9)	; pick up note	
						else
							(PrintOther 49 17)
						)
					else
						(PrintAlreadyTookIt)
					)
				)
			)
			(if (Said '/*')
				(PrintShort 49 18)	
			)
		)
		(if(Said 'move/bookcase')
			(PrintOther 42 25)
		)	
		(if (Said '(look<under),search,examine/bed')
			(if (and (< (gEgo distanceTo: bed) 60) (< (gEgo y?) 165))
				(mouseScript changeState: 1)	
			else
				(PrintOther 49 19)
			)
		)
		(if (Said '(look<in)/cabinet')
			(if (or closetOpen cabinetOpen)
				(if (> (gEgo x?) 155)
					(Print 49 15)
				else
					(if (or (gEgo has: 12) (== (IsOwnedBy 12 210) TRUE))
						(PrintOther 49 34)	
					else
						(PrintOther 49 26)
					)
				)	
			else
				(PrintOther 49 24)
			)
		)
		(if (Said 'look>')
			(if (Said '/triangle, magnet, metal')
				(if (gEgo has: 1)
					(Print 0 1 #title "Magnet" #icon 620 )	
				else
					(if (or (== (IsOwnedBy 1 210) TRUE) (== (IsOwnedBy 1 42) TRUE))
						(PrintOther 49 21)	
					else
						(PrintOther 49 3)
					)				
				)
			)
			(if (Said '/nail')
				(if (or (== (IsOwnedBy 1 210) TRUE) (== (IsOwnedBy 1 42) TRUE)(gEgo has: 1))
					(PrintOther 49 48)	
				else
					(PrintOther 49 47)
				)				
			)
			(if (Said '/key')
				(if (gEgo has: 8)
					(Print 0 8 #title "Room Key" #icon 617 )	
				else
					(if (or (== (IsOwnedBy 8 210) TRUE) (== (IsOwnedBy 8 46) TRUE))
						(PrintOther 49 21)	
					else
						(if keyOnWall
							(PrintOther 49 6)
						else
							(PrintOther 49 7)	
						)
					)				
				)	
			)
			(if (Said '/rock')
				(if cabinetOpen
					(if (or (gEgo has: 12) (== (IsOwnedBy 12 210) TRUE))
						(PrintOther 49 34)	
					else
						(PrintOther 49 26)
					)	
				else
					(PrintOther 49 19)
				)
			)
			(if (Said '/obsidian')
				(if cabinetOpen
					(PrintOther 49 31)	
				else
					(PrintOther 49 19)
				)
			)
			(if (Said '/flint')
				(if cabinetOpen
					(if (or (gEgo has: 12) (== (IsOwnedBy 8 210) TRUE))
						(PrintOther 49 33)	
					else
						(PrintOther 49 31)
					)	
				else
					(PrintOther 49 19)
				)
			)
			(if (Said '/hole')
				(PrintOther 49 8)	
			)
			(if (Said '/wall')
				
				(if (or (or (gEgo has: 1) (== (IsOwnedBy 1 210) TRUE)) (== (IsOwnedBy 1 42) TRUE))
				else
					(PrintOther 49 3)
				)
				(if keyOnWall
					(PrintOther 49 37)		
				)
				(PrintOther 49 8)	
			)
			(if (Said '/chair')
				(PrintOther 49 25)	
			)
			(if (Said '/door,doorway')
				(PrintOther 49 9)	
			)
			(if (Said '/table')
				(PrintOther 49 10)
				(if (< (gEgo distanceTo: table) 30)
					(PrintOther 49 11)
					(PrintBook 49 12)		
				)	
			)
			(if (Said '/book')
				(if (< (gEgo distanceTo: bookcase) 30)	
					(PrintOther 49 16)	
				else
					(if (< (gEgo distanceTo: table) 30)
						(PrintOther 49 11)
						(PrintBook 49 12)
					else
						(PrintOther 49 36)
					)
				)				
			)
			(if (Said '/cabinet')
				(PrintOther 49 23)	
			)
			(if (Said '/bed')
				(if (or (== g49PaperTaken 1) (== g49PaperTaken 3))
					(PrintOther 49 22)
				else
					(PrintOther 49 4)	
				)	
			)
			(if (Said '/bookcase')
				(PrintOther 49 16)	
			)
			(if (Said '/mouse')
				(PrintOther 49 42)	
			)
			(if (Said '[/!*]')
				(PrintOther 49 39)
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
		(if 
			(or 
				(Said 'reach/hole')
				(Said 'put,reach/hand/hole') 
			)
			(PrintOther 49 41)
		)
	)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0 ; Handle state changes
			)
			; open closet
			(1
				(ProgramControl)
				(gEgo setMotion: MoveTo (closet x?)(+ (closet y?) 3) self)	
			)
			(2	(= cycles 2)
				(gEgo loop: 3)
			)
			(3
				(if (not closetOpen)
					(closet setCycle: End self cycleSpeed: 3)
				else
					(self cue:)
				)	
			)
			(4
				(PrintShort 49 15)
				(= closetOpen 1)
				(PlayerControl)	
			)
			; open cabinet
			(5
				(ProgramControl)
				(gEgo setMotion: MoveTo  (cabinet x?)(+ (cabinet y?) 3) self)	
			)
			(6	(= cycles 2)
				(gEgo loop: 3)
			)
			(7
				(if (not cabinetOpen)
					(cabinet setCycle: End self cycleSpeed: 3)
				else
					(self cue:)
				)	
			)
			(8
				(if (or (gEgo has: 12) (== (IsOwnedBy 12 210) TRUE))
					(PrintOther 49 34)	
				else
					(PrintOther 49 26)
				)				
				(= cabinetOpen 1)
				(PlayerControl)	
			)
			; Pick up note stolen by mouse
			(9
				(ProgramControl)
				(gEgo setMotion: MoveTo (+ (paper x?) 10)(paper y?)self)	
			)
			(10
				(gEgo hide:)
				(actionEgo show: posn: (gEgo x?)(gEgo y?) view: gEgoPickUpView loop: 1 setCycle: End self cycleSpeed: 2)	
			)
			(11
				(PrintOther 49 35)
				(Print 0 71 #font 4)
				(= [gNotes 5] 1)
				(paper hide:)
				(actionEgo setCycle: Beg self)	
			)
			(12
				(actionEgo hide:)
				(gEgo show: loop: 1)
				(PlayerControl)
				(gGame changeScore: 1)	
			)
			(13
				(ProgramControl)
				(gEgo setMotion: MoveTo (+ (gEgo x?) 10) (gEgo y?) self)	
			)
			(14	(= cycles 2)
				(PlayerControl)	
			)
			(15
				(PrintOther 49 44)	
			)
		)
	)
)

(instance keyScript of Script
	(properties)
	
	(method (changeState newState button)
		(= state newState)
		(switch state
			(0
			)
			(1
				; Key falling to the ground
				(key view: 69 setCycle: Fwd yStep: 6 setMotion: MoveTo 200 95 self)
			)
			(2
				(key setCycle: CT)
				(= keyOnWall 0)	
			)
			(3	; moving to key on the ground
				(ProgramControl)
				(gEgo setMotion: MoveTo 185 95 self ignoreControl: ctlWHITE)
			)
			(4	; bending to get key
				(gEgo hide:)
				(actionEgo show: view: 232 posn: 185 95 loop: 0 cel: 0 setCycle: End self cycleSpeed: 3)
			)
			(5	; Stand back up
				(PrintOther 49 1)
				(actionEgo setCycle: Beg self)	
				(key hide:)
			)
			(6
				(actionEgo hide:)
				(gEgo show: setMotion: MoveTo 185 100 self get: 8) 
			)
			(7
				(PlayerControl)
				(gGame changeScore: 1)	
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
				; get closer to bed
				(ProgramControl)
				(gEgo setMotion: MoveTo 100 150 self)
			)
			(2 (= cycles 2)
				; face ego toward screen
				(gEgo loop: 2)	
			)
			(3	
				; kneeling down you see a paper under the bed
				(gEgo hide:)
				(actionEgo show: posn: (gEgo x?)(gEgo y?) view: 405 loop: 0 cel: 0 setCycle: End self cycleSpeed: 3)	
			)
			(4
				(if (or (not g49PaperTaken) (== g49PaperTaken 2))
					(mouse show: setMotion: MoveTo 160 160 self)
					(actionEgo loop: 1 cel: 0 setCycle: End cycleSpeed: 6)
					(= stealingPaper 1)
				else
					(self changeState: 7)
				)					
			)
			(5
				(mouse show: setMotion: MoveTo 245 110 self)
				(actionEgoScript changeState: 1)	
			)
			(6
				(PlayerControl)	
				(= g49PaperTaken 1)
			)
			(7
				(PrintShort 49 2)
				(actionEgo loop: 0 cel: 2 setCycle: Beg self cycleSpeed: 3)
			)
			(8 (= cycles 2)
				(actionEgo hide:)
				(gEgo show:)	
			)
			(9
				(PlayerControl)	
			)
				; mouse running back to bed
			(10	
				(= stealingPaper 2)
				(mouse show: posn: 245 110 setMotion: MoveTo 160 160  self)
				(paper show:)
			)
			(11	
				(mouse show: setMotion: MoveTo 100 160 self)
			)
			(12
				(= g49PaperTaken 0)	
				(= stealingPaper 0)
				(mouse hide:)
				(paper posn: 100 170 setPri: 0)
				
			)
			; mouse running back to bed
			(13	
				(ProgramControl)
				(= stealingPaper 2)
				(gEgo loop: 2)
				(mouse show: posn: 245 110 setMotion: MoveTo 200 130  self)
				(paper show:)
			)
			(14	
				(= g49PaperTaken 3)
				(= stealingPaper 0)	
				(mouse show: setMotion: MoveTo 300 110 self)
				(paper posn: 185 135 setPri: 0)
				
				(PrintOther 49 46)
			)
			(15
				(PlayerControl)
				
			)
		)
	)
)
(instance actionEgoScript of Script
	(properties)
	
	(method (changeState newState button)
		(= state newState)
		(switch state
			(0
			)
			(1	; standing up from looking under the bed
				(actionEgo loop: 0 cel: 2 setCycle: Beg self cycleSpeed: 3)
			)
			(2
				(actionEgo hide:)
				(gEgo show:)	
			)
			; TAKING THE MAGNET
			(3	; moving to magnet
				(ProgramControl)
				(gEgo setMotion: MoveTo 111 89 self ignoreControl: ctlWHITE)
			)
			(4	; lifting hand to take magnet
				(gEgo hide:)
				(actionEgo show: view: 404 posn: 111 89 loop: 1 cel: 0 setCycle: End self cycleSpeed: 3)	
			)
			(5	; taking magnet
				(PrintOther 49 5)
				(actionEgo setCycle: Beg self)
				(magnet loop: 0)
				(keyScript changeState: 1)	
			)
			(6
				(actionEgo hide:)
				(gEgo show: setMotion: MoveTo 115 95 self) 
			)
			(7 (= cycles 2)
				(gGame changeScore: 1)	
				(PlayerControl)
				(gEgo observeControl: ctlWHITE get: 1 loop: 2)	; get magnet	
			)
			(8			
				(PrintOther 49 43)				
			)
			; TRY TO TAKE THE KEY
			(9	; moving to magnet
				(ProgramControl)
				(gEgo setMotion: MoveTo 200 90 self ignoreControl: ctlWHITE)
			)
			(10	; lifting hand to take magnet
				(gEgo hide:)
				(actionEgo show: view: 404 posn: 200 90 loop: 0 cel: 0 setCycle: End self cycleSpeed: 3)	
			)
			(11	; taking magnet
				(PrintOther 49 0)
				(actionEgo setCycle: Beg self)	
			)
			(12
				(actionEgo hide:)
				(gEgo show: setMotion: MoveTo 200 95 self) 
			)
			(13
				(gEgo observeControl: ctlWHITE)
				(PlayerControl)	
			)
		)
	)
)
; Right-click-look Procedure
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
	(if (> (gEgo y?) 120)
		(Print textRes textResIndex	#width 280 #at -1 10)
	else
		(Print textRes textResIndex	#width 280 #at -1 140)
	)
)
(procedure (PrintShort textRes textResIndex)
	(Print textRes textResIndex		
		#width -1
		#at -1 140
	)
)
(procedure (PrintBook textRes textResIndex)
	(Print textRes textResIndex		
		#width 100
		#font 4
		#title "It reads:"
		#at 170 -1
	)
)

(instance actionEgo of Prop
	(properties
		y 130
		x 150
		view 405
	)
)
(instance bed of Prop
	(properties
		y 175
		x 100
		view 16
		loop 0
		cel 2
	)
)
(instance bookcase of Act
	(properties
		y 155
		x 240
		view 46
	)
)
(instance cabinet of Prop
	(properties
		y 87
		x 128
		view 18
		loop 14
	)
)
(instance chair of Prop
	(properties
		y 142
		x 170
		view 16
		loop 3
		cel 2
	)
)
(instance closet of Prop
	(properties
		y 87
		x 187
		view 16
		loop 2
	)
)
(instance key of Act
	(properties
		y 55
		x 206
		view 80
		loop 3
	)
)
(instance magnet of Prop
	(properties
		y 65
		x 100
		view 80
		loop 1
	)
)
(instance mouse of Act
	(properties
		y 160
		x 100
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
(instance table of Prop
	(properties
		y 130
		x 135
		view 16
		loop 5
		cel 2
	)
)

