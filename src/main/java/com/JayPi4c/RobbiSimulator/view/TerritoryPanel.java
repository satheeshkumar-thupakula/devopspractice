package com.JayPi4c.RobbiSimulator.view;

import com.JayPi4c.RobbiSimulator.model.Accu;
import com.JayPi4c.RobbiSimulator.model.Hollow;
import com.JayPi4c.RobbiSimulator.model.Item;
import com.JayPi4c.RobbiSimulator.model.Nut;
import com.JayPi4c.RobbiSimulator.model.PileOfScrap;
import com.JayPi4c.RobbiSimulator.model.Screw;
import com.JayPi4c.RobbiSimulator.model.Stockpile;
import com.JayPi4c.RobbiSimulator.model.Territory;
import com.JayPi4c.RobbiSimulator.model.Tile;

import javafx.geometry.Bounds;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;

/**
 * 
 * This class draws the territory on a panel to allow interacting with the
 * graphical interface.
 * 
 * last modified 08.11.2021
 * 
 * @author Jonas Pohl
 *
 */
public class TerritoryPanel extends Canvas {

	private Territory territory;

	private Image tileImages[] = new Image[4];
	private Image itemImages[] = new Image[3];
	private Image robbiImage;

	private int TILE = 0;
	private int STOCKPILE = 1;
	private int HOLLOW = 2;
	private int PILEOFSCRAP = 3;
	private int NUT = 0;
	private int SCREW = 1;
	private int ACCU = 2;

	private static final int CELLSIZE = 32;

	public TerritoryPanel(Territory territory) {
		this.territory = territory;
		loadImages();
		init();

	}

	private void loadImages() {

		robbiImage = new Image("img/0Robbi32.png");

		tileImages[TILE] = new Image("img/Tile32.png");
		tileImages[STOCKPILE] = new Image("img/Stockpile32.png");
		tileImages[HOLLOW] = new Image("img/Hollow32.png");
		tileImages[PILEOFSCRAP] = new Image("img/PileOfScrap32.png");

		itemImages[NUT] = new Image("img/Nut32.png");
		itemImages[SCREW] = new Image("img/Screw32.png");
		itemImages[ACCU] = new Image("img/Accu32.png");
	}

	private void init() {
		setWidth(getTerritoryWidth());
		setHeight(getTerritoryHeight());
		paintTerritory();
	}

	private int getTerritoryWidth() {
		return (territory.getNumCols()) * (CELLSIZE + 2);
	}

	private int getTerritoryHeight() {
		return (territory.getNumRows()) * (CELLSIZE + 2);
	}

	private void paintTerritory() {

		GraphicsContext gc = getGraphicsContext2D();

		for (int i = 0; i < territory.getNumCols(); i++) {
			gc.setStroke(Color.BLACK);
			gc.setLineWidth(1);
			// gc.strokeLine(getPos(i) - 1, 0, getPos(i) - 1,
			// getPos(territory.getNumRows()));
			// gc.strokeLine(0, getPos(i) - 1, getPos(territory.getNumCols()), getPos(i) -
			// 1);

			for (int j = 0; j < territory.getNumRows(); j++) {
				Tile t = territory.getTile(i, j);
				gc.drawImage(tileImages[TILE], getPos(i), getPos(j), CELLSIZE, CELLSIZE);
				if (t instanceof Hollow) {
					gc.drawImage(tileImages[HOLLOW], getPos(i), getPos(j), CELLSIZE, CELLSIZE);

				} else if (t instanceof PileOfScrap) {
					gc.drawImage(tileImages[PILEOFSCRAP], getPos(i), getPos(j), CELLSIZE, CELLSIZE);
				} else if (t instanceof Stockpile) {
					gc.drawImage(tileImages[STOCKPILE], getPos(i), getPos(j), CELLSIZE, CELLSIZE);
					Stockpile stockpile = (Stockpile) t;
					for (Item item : stockpile.getAllItems()) {
						if (item instanceof Nut) {
							gc.drawImage(itemImages[NUT], getPos(i) + CELLSIZE / 2, getPos(j), CELLSIZE / 2,
									CELLSIZE / 2);
						} else if (item instanceof Accu) {
							gc.drawImage(itemImages[ACCU], getPos(i), getPos(j), CELLSIZE / 2, CELLSIZE / 2);
						} else if (item instanceof Screw) {
							gc.drawImage(itemImages[SCREW], getPos(i) + CELLSIZE / 3, getPos(j) + CELLSIZE / 2,
									CELLSIZE / 2, CELLSIZE / 2);
						}
					}
				}
				if (!(t instanceof Stockpile)) {
					Item item = t.getItem();
					if (item instanceof Nut) {
						gc.drawImage(itemImages[NUT], getPos(i), getPos(j), CELLSIZE, CELLSIZE);
					} else if (item instanceof Screw) {
						gc.drawImage(itemImages[SCREW], getPos(i), getPos(j), CELLSIZE, CELLSIZE);
					} else if (item instanceof Accu) {
						gc.drawImage(itemImages[ACCU], getPos(i), getPos(j), CELLSIZE, CELLSIZE);
					}

				}
			}
		}
		// gc.strokeLine(getPos(territory.getNumCols()) - 1, 0,
		// getPos(territory.getNumCols()) - 1,
		// getPos(territory.getNumRows()));
		// gc.strokeLine(0, getPos(territory.getNumRows()) - 1,
		// getPos(territory.getNumCols()) - 1,
		// getPos(territory.getNumRows()));

		double angle = switch (territory.getRobbiDirection()) {
		case NORTH:
			yield 270;
		case WEST:
			yield 180;
		case SOUTH:
			yield 90;
		case EAST:
			yield 0;
		default:
			yield 0;
		};
		drawRotatedImage(gc, robbiImage, angle, territory.getRobbiX() * (CELLSIZE + 1) + 1,
				territory.getRobbiY() * (CELLSIZE + 1) + 1, CELLSIZE, CELLSIZE);

	}

	/**
	 * taken from https://stackoverflow.com/a/18262938/13670629 <br>
	 * Sets the transform for the GraphicsContext to rotate around a pivot point.
	 *
	 * @param gc    the graphics context the transform to applied to.
	 * @param angle the angle of rotation.
	 * @param px    the x pivot co-ordinate for the rotation (in canvas
	 *              co-ordinates).
	 * @param py    the y pivot co-ordinate for the rotation (in canvas
	 *              co-ordinates).
	 */
	private void rotate(GraphicsContext gc, double angle, double px, double py) {
		Rotate r = new Rotate(angle, px, py);
		gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
	}

	/**
	 * taken from https://stackoverflow.com/a/18262938/13670629 <br>
	 * Draws an image on a graphics context.
	 *
	 * The image is drawn at (tlpx, tlpy) rotated by angle pivoted around the point:
	 * (tlpx + image.getWidth() / 2, tlpy + image.getHeight() / 2)
	 *
	 * @param gc    the graphics context the image is to be drawn on.
	 * @param angle the angle of rotation.
	 * @param tlpx  the top left x co-ordinate where the image will be plotted (in
	 *              canvas co-ordinates).
	 * @param tlpy  the top left y co-ordinate where the image will be plotted (in
	 *              canvas co-ordinates).
	 */
	private void drawRotatedImage(GraphicsContext gc, Image image, double angle, double tlpx, double tlpy, double w,
			double h) {
		gc.save(); // saves the current state on stack, including the current transform
		rotate(gc, angle, tlpx + image.getWidth() / 2, tlpy + image.getHeight() / 2);
		gc.drawImage(image, tlpx, tlpy, w, h);
		gc.restore(); // back to original state (before rotation)
	}

	private int getPos(int val) {
		return (val) * (CELLSIZE + 1) + 1;
	}

	public void center(Bounds vpb) {
		double w = vpb.getWidth();
		double h = vpb.getHeight();
		if (w > getTerritoryWidth()) {
			setTranslateX((w - getTerritoryWidth()) / 2);
		} else
			setTranslateX(0);
		if (h > getTerritoryHeight()) {
			setTranslateY((h - getTerritoryHeight()) / 2);
		} else
			setTranslateY(0);
	}

}
