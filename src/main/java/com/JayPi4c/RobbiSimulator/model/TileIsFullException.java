package com.JayPi4c.RobbiSimulator.model;

import com.JayPi4c.RobbiSimulator.utils.Messages;

/**
 * Exception to be thrown if robbi attempts to place an item on a tile, that has
 * already an item stored.
 * 
 * @author Jonas Pohl
 *
 */
public class TileIsFullException extends RobbiException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor for a new TileIsFullException with localized message.
	 */
	public TileIsFullException() {
		super(Messages.getString("Exception.TileIsFull"));
	}

}