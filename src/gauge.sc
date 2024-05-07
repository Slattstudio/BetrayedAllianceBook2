;;; Sierra Script 1.0 - (do not remove this comment)
;
; SCI Template Game
; By Brian Provinciano
; ******************************************************************************
; gauge.sc
; Contains a dialog window which contains a custom gauge control. This is used 
; for things such as volume control and the game speed adjustment.
(script# GAUGE_SCRIPT)
(include sci.sh)
(include game.sh)
(use main)
(use controls)
(use syswindow)


(local



; controls
	txtDescription =  NULL
	btnHigher =  NULL
	btnLower =  NULL
	txtGuage =  NULL
	btnOK =  NULL
	btnNormal =  NULL
	btnCancel =  NULL
; text buffer
	[strGauge 41]
)

(class Gauge of Dialog
	(properties
		elements 0
		size 0
		text 0
		window 0
		theItem 0
		nsTop 0
		nsLeft 0
		nsBottom 0
		nsRight 0
		time 0
		busy 0
		seconds 0
		lastSeconds 0
		description 0
		higher {up}
		lower {down}
		normal 7
		minimum 0
		maximum 15
	)
	
	(method (init gaugePos &tmp ctlX ctlY)
		(= window SysWindow)
		(self update: gaugePos)
		(= btnLower (DButton new:))
		(btnLower text: lower moveTo: 4 4 setSize:)
		(self add: btnLower setSize:)
		(= txtGuage (DText new:))
		(txtGuage
			text: @strGauge
			moveTo: (+ (btnLower nsRight?) 4) 4
			font: 0
			setSize:
		)
		(self add: txtGuage setSize:)
		(= btnHigher (DButton new:))
		(btnHigher
			text: higher
			moveTo: (+ (txtGuage nsRight?) 4) 4
			setSize:
		)
		(self add: btnHigher setSize:)
		(= btnOK (DButton new:))
		(= nsBottom (+ nsBottom 8))
		(btnOK text: {OK} setSize: moveTo: 4 nsBottom)
		(= btnNormal (DButton new:))
		(btnNormal
			text: {Normal}
			setSize:
			moveTo: (+ (btnOK nsRight?) 4) nsBottom
		)
		(= btnCancel (DButton new:))
		(btnCancel
			text: {Cancel}
			setSize:
			moveTo: (+ (btnNormal nsRight?) 4) nsBottom
		)
		(self add: btnOK btnNormal btnCancel setSize:)
		(= ctlX (- (- nsRight (btnCancel nsRight?)) 4))
		(= txtDescription (DText new:))
		(txtDescription
			text: description
			font: gSaveRestoreFont
			setSize: (- nsRight 8)
			moveTo: 4 4
		)
		(= ctlY (+ (txtDescription nsBottom?) 4))
		(self add: txtDescription)
		(btnHigher move: 0 ctlY)
		(btnLower move: 0 ctlY)
		(txtGuage move: 0 ctlY)
		(btnOK move: ctlX ctlY)
		(btnNormal move: ctlX ctlY)
		(btnCancel move: ctlX ctlY)
		(self setSize: center: open: nwTITLE 15)
	)
	
	(method (doit gaugePos &tmp btnPressed newGaugePos)
		(self init: gaugePos)
		(= newGaugePos gaugePos)
		(repeat
			(self update: newGaugePos)
			(txtGuage draw:)
			(= btnPressed (super doit: btnOK))
			(if (== btnPressed btnHigher)
				(if (< newGaugePos maximum) (++ newGaugePos))
			else
				(if (== btnPressed btnLower)
					(if (> newGaugePos minimum) (-- newGaugePos))
				)
				(cond 
					((== btnPressed btnOK) (self dispose:) (return newGaugePos))
					((== btnPressed btnNormal) (= newGaugePos normal))
					(
					(or (== btnPressed 0) (== btnPressed btnCancel)) (self dispose:) (return gaugePos))
				)
			)
		)
	)
	
	(method (handleEvent pEvent)
		(switch (pEvent type?)
			(evKEYBOARD
				(switch (pEvent message?)
					(KEY_LEFT
						(pEvent claimed: TRUE)
						(return btnLower)
					)
					(KEY_RIGHT
						(pEvent claimed: TRUE)
						(return btnHigher)
					)
				)
			)
			(evJOYSTICK
				(switch (pEvent message?)
					(JOY_LEFT
						(pEvent claimed: TRUE)
						(return btnLower)
					)
					(JOY_RIGHT
						(pEvent claimed: TRUE)
						(return btnHigher)
					)
				)
			)
		)
		(super handleEvent: pEvent)
	)
	
	(method (update gaugePos &tmp i strLen) ; Draws the guage
		(= strLen (- maximum minimum))
		(= i 0)
		(while (< i strLen)
			(if (< i gaugePos)
				(StrAt @strGauge i 6)
			else                     ; black block character
				(StrAt @strGauge i 7)
			)                        ; white block character
			(++ i)
		)
	)
)
