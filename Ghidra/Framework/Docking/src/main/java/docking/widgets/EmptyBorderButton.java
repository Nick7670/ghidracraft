/* ###
 * IP: GHIDRA
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package docking.widgets;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ghidra.docking.util.DockingWindowsLookAndFeelUtils;
import resources.ResourceManager;

import static javax.swing.BorderFactory.createEmptyBorder;

/**
 * Class that is a JButton that has an empty border and adds a mouse listener
 * so that the button looks raised when the mouse pointer enters the button,
 * and looks lowered when the mouse pointer exits the button.e
 */
public class EmptyBorderButton extends JButton {

	private ButtonStateListener emptyBorderButtonChangeListener;

	/**
	 * An empty border.
	 */
	public static final Border NO_BUTTON_BORDER = createEmptyBorder(1, 1, 1, 1);
	public static final Border RAISED_BUTTON_BORDER = NO_BUTTON_BORDER;
	public static final Border LOWERED_BUTTON_BORDER = NO_BUTTON_BORDER;

	/**
	 * Construct a new EmptyBorderButton.
	 *
	 */
	public EmptyBorderButton() {
		super();
		init();
	}

	/**
	 * Construct a new EmptyBorderButton that has the given button text.
	 * @param text text of the button
	 */
	public EmptyBorderButton(String text) {
		super(text);
		init();
	}

	/**
	 * Construct a new EmptyBorderButton that has an associated action.
	 * @param a action for the button
	 */
	public EmptyBorderButton(Action a) {
		super(a);
		init();
	}

	/**
	 * Construct a new EmptyBorderButton that has an icon.
	 * @param icon icon for the button
	 */
	public EmptyBorderButton(Icon icon) {
		super(icon);
		init();
	}

	/**
	 * Construct a new EmptyBorderButton that has text and an icon.
	 * @param text button text
	 * @param icon icon for the button
	 */
	public EmptyBorderButton(String text, Icon icon) {
		super(text, icon);
		init();
	}

	private void init() {
		ToolTipManager.sharedInstance().registerComponent(this);
		installLookAndFeelFix();
		this.setBorder(new EmptyBorder(4, 4, 4, 4));
		clearBackground();
		emptyBorderButtonChangeListener = new ButtonStateListener();
		addChangeListener(emptyBorderButtonChangeListener);
	}

	@Override
	public void setIcon(Icon newIcon) {
		Icon disabledIcon = ResourceManager.getDisabledIcon(newIcon);
		setDisabledIcon(disabledIcon);
		super.setIcon(newIcon);
	}

	public void setFocusBackground(){
		setBackground(new Color(87, 92, 95));
	}

	public void setPressBackground(){
		setBackground(new Color(112, 117, 120));
	}

	public void clearBackground() {
		setBackground(Color.DARK_GRAY);
	}

	public void clearBorder() {
		setBorder(NO_BUTTON_BORDER);
	}

	private void installLookAndFeelFix() {
		// We want our custom buttons to paint themselves blended with the background.  Several 
		// LookAndFeels do not do this (WinXP and Metal), so we override that behavior here.
		setContentAreaFilled(true);
		setOpaque(true);

		// Mac OSX LNF doesn't give us rollover callbacks, so we have to add a mouse listener to
		// do the work
		if (DockingWindowsLookAndFeelUtils.isUsingAquaUI(getUI())) {
			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					if (e.getButton() == MouseEvent.NOBUTTON) {
						setFocusBackground();
					}
				}

				@Override
				public void mouseExited(MouseEvent e) {
					clearBackground();
				}
			});
		}
	}

	protected void updateBorderBasedOnState() {
		if (!isEnabled()) {
			return;
		}

		ButtonModel buttonModel = getModel();
		boolean pressed = buttonModel.isPressed();
		boolean rollover = buttonModel.isRollover();
		boolean armed = buttonModel.isArmed();

		if (pressed && (rollover || armed)) {
			setPressBackground();
		}
		else if (rollover) {
			setFocusBackground();
		}
		else {
			clearBackground();
		}
	}

	public void removeListeners() {
		removeChangeListener(emptyBorderButtonChangeListener);
	}

	private class ButtonStateListener implements ChangeListener {
		@Override
		public void stateChanged(ChangeEvent e) {
			updateBorderBasedOnState();
		}
	}
}
