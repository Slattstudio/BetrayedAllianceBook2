;;; Sierra Script 1.0 - (do not remove this comment)
; Score + 2
(script# 267)
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
	
	rm267 0

)

(local
	
	canGet = 0 ; whether or not the rack can be taken from the ground
	
)

(instance rm267 of Rm
	(properties
		picture scriptNumber
		north 0
		east 268
		south 0
		west 0
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript setRegions: 205)
		
		(SetUpEgo)
		(gEgo init:)
		(RunningCheck)
		
		(alterEgo init: hide: setScript: pickUpScript)
		(metal init: ignoreActors: setPri: 0 setScript: metalScript)
		(stick init: ignoreActors: setPri: 0)
		
		(= gInDorm 0)
		
		(if (or (gEgo has: 4) (== (IsOwnedBy 4 268) TRUE))
			(metal hide:)
		)
		(if (or (gEgo has: 7) (== (IsOwnedBy 7 271) TRUE))
			(stick hide:)
		)
		
		(switch gPreviousRoomNumber
			(44 
				(gEgo posn: 33 80 loop: 0)
				(RoomScript changeState: 1)
			)
			(268 
				(gEgo posn: 300 120 loop: 1)
				
			)
			(else 
				(gEgo posn: 150 100 loop: 1)
			)
		)
	)
)

(instance RoomScript of Script
	(properties)
	
	(method (doit)
		(super doit:)
		; code executed each game cycle
		(if (& (gEgo onControl:) ctlMAROON)
			;(gRoom newRoom: 44)
			(self changeState: 4)
		)
	)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if (& (pEvent modifiers?) emRIGHT_BUTTON)
				(if	(checkEvent pEvent (stick nsLeft?) (stick nsRight?) (stick nsTop?) (stick nsBottom?))
					(if (or (gEgo has: 7) (== (IsOwnedBy 7 271) TRUE))
					;	(PrintAlreadyTookIt)
					else
						(PrintOther 267 1)	
					)
				)
				(if	(checkEvent pEvent (metal nsLeft?) (metal nsRight?) (metal nsTop?) (metal nsBottom?))
					(if (or (gEgo has: 4) (== (IsOwnedBy 4 268) TRUE))
					;	(PrintAlreadyTookIt)
					else
						(PrintOther 267 2)	
						(PrintOther 267 7)
					)
				)	
			)
		)
		
		; handle Said's, etc...
		(if (Said 'look>')
			(if (Said '/rock,rubble,building')
				(PrintOther 267 3)	
			)
			(if (Said '/stick, hammer, handle')
				(if (or (gEgo has: 7) (== (IsOwnedBy 7 271) TRUE))
					(Print 0 7 #title "Stick" #icon 616)
				else
					(PrintOther 267 1)	
				)
			)
			(if (Said '/prongs')
				(if (or (gEgo has: 4) (== (IsOwnedBy 4 268) TRUE))
					(Print "There is nothing like that here anymore.")
				else
					(PrintOther 267 9)
				)
			)
			(if (Said '/metal, rack')
				(if (or (gEgo has: 4) (== (IsOwnedBy 4 268) TRUE))
					(Print 0 4 #title "Rack" #icon 613)
				else
					(PrintOther 267 2)	
					(PrintOther 267 7)
				)
			)
			(if (Said '/ground')
				(PrintOther 267 5)	
				
				(if (or (gEgo has: 7) (== (IsOwnedBy 7 271) TRUE))
				else
					(PrintOther 267 1)		
				)
				
				(if (or (gEgo has: 4) (== (IsOwnedBy 4 268) TRUE))
				else
					(PrintOther 267 2)	
				)
			)
			(if (Said '[/!*]')
				(PrintOther 267 0)
				(if (or (gEgo has: 7) (== (IsOwnedBy 7 271) TRUE))
				else
					(PrintOther 267 1)		
				)
				
				(if (or (gEgo has: 4) (== (IsOwnedBy 4 268) TRUE))
				else
					(PrintOther 267 2)	
				)
			)
		)
		(if (or (Said 'use/stick,handle/rack,mount,metal,lever')
				(Said '(pry<out,up),pull,dig/metal,rack,mount/stick,handle')
				(Said 'pry,take/metal,rack,mount/stick,handle')
				(Said 'pry//stick'))
				(if (gEgo has: 4)	; rack
				(PrintAlreadyTookIt)
			else
				(if (gEgo has: 7)	; stick
					(= canGet 1)
					(pickUpScript changeState: 1)
				else
					(PrintDontHaveIt)
				)
			)
		
		)
		(if (or (Said '(pry<out,up),pull,dig/metal,rack,mount')
				(Said 'pry/metal'))
				(if (gEgo has: 4)	; rack
					(PrintAlreadyTookIt)
				else
					(PrintOther 267 8)	
				)	
		)
		
		(if (Said 'take,(pick<up),(pull<out)/metal,rack')
			(if (and (not (gEgo has: 4)) (== (IsOwnedBy 4 268) FALSE))
				(if (<= (gEgo distanceTo: metal) 100) 
					(pickUpScript changeState: 1)
				else
					(PrintNotCloseEnough)
				)
			else
				(PrintAlreadyTookIt)
			)
		)

		(if (Said 'take,(pick<up)>')			
			(if (Said '/stick,hammer,handle')
				(if (and (not (gEgo has: 7)) (== (IsOwnedBy 7 271) FALSE))
					(if (<= (gEgo distanceTo: stick) 100) 
						(pickUpScript changeState: 6)
					else
						(PrintNotCloseEnough)
					)
				else
					(PrintAlreadyTookIt)
				)
			)
			(if (Said '/rock')
				(PrintOther 267 4)	
			)
		)
	)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0 ; Handle state changes
			)
			(1 (= cycles 1)
				(gTheMusic fade:)	
			)
			(2
				(ProgramControl)
				(gEgo setMotion: MoveTo 60 (gEgo y?) self ignoreControl: ctlWHITE)	
			)
			(3
				(PlayerControl)
				(gEgo observeControl: ctlWHITE)
				(gTheMusic number: 10 loop: -1 priority: -1 play:)	
			)
			
			(4
				(ProgramControl)
				(gEgo setMotion: MoveTo (+ (gEgo x?) 35) (gEgo y?) self ignoreControl: ctlWHITE)	
			)
			(5	(= cycles 2)
				(PlayerControl)
				(gEgo observeControl: ctlWHITE)			
			)
			(6
				(PrintOther 267 10)	
			)
		)
	)
)
(instance metalScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0	(= cycles (Random 80 160))
			)
			(1
				(metal setCycle: End self cycleSpeed: 1)	
			)
			(2
				(self changeState: 0)	
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
			(1
				; walk to metal
				(ProgramControl)
				(gEgo setMotion: MoveTo (- (metal x?) 10) (metal y?) self ignoreControl: ctlWHITE)
			)
			(2
				; bend down 
				(gEgo hide:)
				(alterEgo show: view: 450 loop: 0 cel: 0 posn: (gEgo x?) (gEgo y?) setCycle: End self cycleSpeed: 2)	
			)
			(3	(= cycles 10)
				;(PrintOK)
				(if (and (gEgo has: INV_STICK) canGet)
					(metal hide:)
					(gEgo get: 4) ; metal
					(PrintOther 267 4)
					(gGame changeScore: 1)
				else
					(PrintOther 267 6)
				)
			)
			(4	; stand back up
				(alterEgo setCycle: Beg self)				
			)
			(5
				(alterEgo hide:)
				(gEgo show: loop: 0 observeControl: ctlWHITE)
				(PlayerControl)
					
			)
			; GETTING THE STICK
			
			(6	; walk to stick
				(ProgramControl)
				(gEgo setMotion: MoveTo (- (stick x?) 10) (stick y?) self ignoreControl: ctlWHITE)
			)
			(7
				; bend down 
				(gEgo hide:)
				(alterEgo show: view: 450 loop: 0 cel: 0 posn: (gEgo x?) (gEgo y?) setCycle: End self cycleSpeed: 2)	
			)
			(8	(= cycles 10)
				(PrintOK)
				(stick hide:)
				(gEgo get: 7) ; stick
			)
			(9	; stand back up
				(alterEgo setCycle: Beg self)				
			)
			(10
				(alterEgo hide:)
				(gEgo show: loop: 0 observeControl: ctlWHITE)
				(PlayerControl)	
				(gGame changeScore: 1)
			)
		)
	)
)
(procedure (PrintOther textRes textResIndex)
	(if (> (gEgo y?) 120)
		(Print textRes textResIndex	#width 280 #at -1 10)
	else
		(Print textRes textResIndex	#width 280 #at -1 140)
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

(instance alterEgo of Act
	(properties
		y 100
		x 130
		view 450
	)
)
(instance metal of Prop
	(properties
		y 100
		x 130
		view 455
		loop 1
	)
)
(instance stick of Act
	(properties
		y 85
		x 110
		view 47
	)
)
