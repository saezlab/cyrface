/*******************************************************************************
 * Copyright (c) 2012 Emanuel Goncalves.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Emanuel Goncalves - initial API and implementation
 *     Martijn van Iersel - Initial API
 *     Julio Saez-Rodriguez - Proposed and initiated the project
 ******************************************************************************/
package uk.ac.ebi.cyrface2.internal.utils;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

/**
 * Image panel class used to display all the CellNOptR plots in Cytoscape.
 * 
 * @author emanuel
 *
 */
@SuppressWarnings("serial")
public class ImagesPanel extends JPanel{
	
	protected Image image;

	/**
	 * ImagesPanel constructor.
	 * 
	 * @param image
	 */
	public ImagesPanel(Image image) {
		this.image = image;
		Dimension size = new Dimension(image.getWidth(null), image.getHeight(null));
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
		setSize(size);
		setLayout(null);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 0, 0, null);
	}

	public void setImage(Image image) {
		this.image = image;
		validate();
		repaint();
	}

	public Image getDisplayedImage() {
		return this.image;
	}

	public void update(Graphics g) {
		if ( image != null ) {
			g.drawImage(image, 0,0,this.getSize().width,this.getSize().height, this);
		} else {
			super.update(g);
		}
	}

	public void paint (Graphics g) {
		update(g);
	}
}
