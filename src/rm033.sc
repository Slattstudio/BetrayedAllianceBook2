;;; Sierra Script 1.0 - (do not remove this comment)
; score + 1
(script# 33)
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
	
	rm033 0
	
)

(local
	paperGone = 0	
)


(instance rm033 of Rm
	(properties
		picture scriptNumber
		north 243
		east 0
		south 0
		west 0
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript)
		(switch gPreviousRoomNumber
			(243
				(gEgo posn: 187 40 loop: 2)	
			)
			(else 
				(gEgo posn: 150 100 loop: 1)
			)
		)
		(SetUpEgo)
		(gEgo init:)
		(RunningCheck)
		
		(actionEgo init: hide: ignoreActors:)
		(paper init: ignoreActors: cycleSpeed: 2 setPri: 2 setScript: paperScript)
		
		(paperScript cue:)
		(if [gNotes 6]
			;g33Letter
			(paper hide:)
				
		)
	)
)

(instance RoomScript of Script
	(properties)
	
	(method (doit)
		(super doit:)
		(if (& (gEgo onControl:) ctlMAROON)
			(gRoom newRoom: 243)
		)
	)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if (& (pEvent modifiers?) emRIGHT_BUTTON)
				(if
					(checkEvent pEvent 168 206 63 73	; campfire
					)
					(PrintOther 33 0) ; A hint of ash persists in the air from this recently extinguished campfire. Other than that, you find nothing interesting about the wood or ash.
				)
				
				(if
					(checkEvent pEvent
						(paper nsLeft?)
						(paper nsRight?)
						(paper nsTop?)
						(paper nsBottom?)
					)
					(if gRightClickSearch
						(if (<= (gEgo distanceTo: paper) 45)
							(self changeState: 1)	
						else
							(PrintOther 33 1)
						)
					else
						(PrintOther 33 1)
					)
								
				else
					(if (and (== (OnControl ocPRIORITY (pEvent x?) (pEvent y?)) ctlGREEN) (> (pEvent x?) 63))
						(PrintOther 33 2)
					else
						(if
							(checkEvent pEvent 93 154 17 34	; rock wall
							)
							(PrintOther 33 3)	; The rock wall appears to be a manmade structure. Judging by the wear on the rocks, it appears to have been constructed a long time ago.
						)
					)
				)
				
			)
		)
		
		; handle Said's, etc...
		(if (Said 'smell')
			(PrintOther 33 6)		
		)
		(if(Said 'take,(pick<up)/paper,note,letter')
			(if [gNotes 6]
				;g33Letter
				(PrintAlreadyTookIt)
			else
				(if (& (gEgo onControl:) ctlSILVER)
					(self changeState: 1)
				else
					(PrintNotCloseEnough)
				)
			)
		)
		(if (Said 'take/stick,peg')
			(PrintOther 33 8)
		)

		(if (Said 'read/paper,note,letter')
			(if [gNotes 6]
				(Print 33 5 #font 4 #width 120 #at 160 -1)	
			else
				(if (& (gEgo onControl:) ctlSILVER)
					(self changeState: 1)
				else
					(PrintOther 33 1)
				)
			)	
		)
		(if (Said 'look<in/tent')
			(if [gNotes 6]
				(PrintOther 33 11)	
			else
				(PrintOther 33 1)
			)		
		)
		(if (Said 'look>')
			(if (Said '/tent')
				(PrintOther 33 2)
			)
			(if (Said '/paper,note,letter')
				(PrintOther 33 1)
			)
			(if (Said '/fire,campfire,ash')
				(PrintOther 33 0)
			)
			(if (Said '/wall')
				(PrintOther 33 3)
			)
			(if (Said '/ground')
				(PrintOther 33 12)
				(if (not [gNotes 6]	)
					(PrintOther 33 1)
				)
			)
			(if (Said '/forest,verlorn')
				(PrintOther 0 105)	
			)
			(if (Said '[/!*]')	; this will handle just "look" by itself
				(PrintOther 33 10)
			)				
	
		)
		(if (Said 'sleep,rest')
			(Print 33 7)	
		)
	)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0 ; Handle state changes
			)
			; paper pick up 
			(1	;walk to paper
				(ProgramControl)
				(gEgo setMotion: MoveTo 130 62 self ignoreControl: ctlWHITE)
			)
			(2		; pick up animation
				(gEgo hide:)
				(actionEgo show: posn: (gEgo x?)(gEgo y?) view: gEgoPickUpView loop: 1 cel: 0 setCycle: End self cycleSpeed: 3 setPri: 3)	
			)
			(3		; hide paper and get back up
				(paper hide:)
				(actionEgo view: gEgoPickUpView setCycle: Beg self setPri: -1)
				(PrintOther 33 4)	
				(Print 33 5 #font 4 #width 120 #at 160 -1)	
				(= [gNotes 6] 1)	
			)
			(4		; move back to position and put item in inventory
				(actionEgo hide:)
				(gEgo show: setMotion: MoveTo 135 70 self)				
			)
			(5
				;(= g33Letter 1)	
				(gEgo observeControl: ctlWHITE)
				(PlayerControl)
				
				(gGame changeScore: 1)
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
				;(if (== g266paper 0)
					(if (not paperGone)
						(paper setCycle: End self)	
					)
				;)
			)
			(2 (= cycles (Random 40 80))
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

(procedure (PrintOther textRes textResIndex)
	(Print textRes textResIndex		
		#width 290
		#at -1 140
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

(instance actionEgo of Prop
	(properties
		y 67
		x 232
		view 232
	)
)
(instance paper of Prop
	(properties
		y 62
		x 115
		view 62
		loop 5
	)
)
