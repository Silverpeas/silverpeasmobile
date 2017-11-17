/*
 * Copyright (C) 2000 - 2017 Silverpeas
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * As a special exception to the terms and conditions of version 3.0 of
 * the GPL, you may redistribute this Program in connection with Free/Libre
 * Open Source Software ("FLOSS") applications as described in Silverpeas's
 * FLOSS exception.  You should have received a copy of the text describing
 * the FLOSS exception, and it is also available here:
 * "http://www.silverpeas.org/docs/core/legal/floss_exception.html"
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.silverpeas.mobile.server.helpers;

import com.drew.imaging.jpeg.JpegSegmentType;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifIFD0Directory;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class RotationSupport {
	public static final int LEFT_ROTATED = 6;
	public static final int NOT_ROTATED = 1;
	public static final int RIGHT_ROTATED = 8;

	public static BufferedImage rotateLeft(BufferedImage bi) {
		AffineTransform rot270Transform = AffineTransform
				.getRotateInstance((3 * Math.PI) / 2);
		rot270Transform.translate(-bi.getWidth(), 0);

		RenderingHints hints = new RenderingHints(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_SPEED);
		AffineTransformOp op = new AffineTransformOp(rot270Transform, hints);

		return op.filter(bi, null);
	}

	public static BufferedImage rotateRight(BufferedImage bi) {
		AffineTransform rot90Transform = AffineTransform
				.getRotateInstance(Math.PI / 2);
		rot90Transform.translate(0, -bi.getHeight());

		RenderingHints hints = new RenderingHints(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_SPEED);
		AffineTransformOp op = new AffineTransformOp(rot90Transform, hints);

		return op.filter(bi, null);
	}

	public static ImageIcon adjustOrientation(ImageIcon ii, Metadata meta) {
		if (meta == null) {
			return ii;
		}

		BufferedImage bi = createBufferedImage(ii.getImage(),
				ii.getIconWidth(), ii.getIconHeight());
		bi = adjustOrientation(bi, meta);

		return new ImageIcon(bi);
	}

	public static ImageIcon adjustOrientation(ImageIcon ii, File file) {
		try {
			BufferedImage bi = createBufferedImage(ii.getImage(),
					ii.getIconWidth(), ii.getIconHeight());
			bi = adjustOrientation(bi, getOrientation(file));

			return new ImageIcon(bi);
		} catch (IOException ex) { // Not a JPEG for instance

			return ii;
		}
	}

	private static BufferedImage createBufferedImage(Image img, int iw, int ih) {
		BufferedImage bi = new BufferedImage(iw, ih, BufferedImage.TYPE_INT_RGB); // DF
																					// RGB
		Graphics2D g = bi.createGraphics();
		g.drawImage(img, 0, 0, null);
		g.dispose();

		return bi;
	}

	public static BufferedImage adjustOrientation(BufferedImage bi,
			Metadata meta) {
		if (meta == null) {
			return bi;
		}

		try {
			if (meta.containsDirectoryOfType(ExifIFD0Directory.class)) {
				Directory exif = meta.getFirstDirectoryOfType(ExifIFD0Directory.class);
				int existingOrientation = exif
						.getInt(ExifIFD0Directory.TAG_ORIENTATION);

				return adjustOrientation(bi, existingOrientation);
			}
		} catch (MetadataException ex) {
		}

		return bi;
	}

	public static BufferedImage adjustOrientation(BufferedImage bi, File file) {
		try {
			return adjustOrientation(bi, getOrientation(file));
		} catch (IOException ex) {
			return bi;
		}
	}

	public static int getOrientation(Metadata meta) {
		try {
			if (meta.containsDirectoryOfType(ExifIFD0Directory.class)) {
				Directory exif = meta.getFirstDirectoryOfType(ExifIFD0Directory.class);

				return exif.getInt(ExifIFD0Directory.TAG_ORIENTATION);
			}
		} catch (MetadataException ex) {
		}

		return NOT_ROTATED;
	}

	public static String getOrientationAsString(Metadata meta) {
		return asString(getOrientation(meta));
	}

	/**
	 * Get image orientation without using MetadataExtractor
	 * 
	 * @param file
	 * 
	 * @return
	 * 
	 * @throws IOException
	 */
	public static int getOrientation(File file) throws IOException {
		JpegFile f = null;

		try {
			f = new JpegFile(file);

			Segment s;

			for (s = f.getFirstSegment(); (s != null)
					&& (s.getId() != JpegSegmentType.APP1.byteValue); s = s
					.getNextSegment()) {
				;
			}

			if (s != null) {
				APP1Segment app1 = new APP1Segment(s);

				do {
					for (int i = 0; i < app1.getEntryCount(); i++) {
						APP1Segment.Entry ifd = app1.getEntry(i);

						if (ifd.getTagNr() == ExifIFD0Directory.TAG_ORIENTATION) {
							return ifd.getValue();
						}
					}
				} while (app1.nextIFD());
			}

			return NOT_ROTATED;
		} finally {
			if (f != null) {
				f.close();
			}
		}
	}

	public static String getOrientationAsString(File file) throws IOException {
		return asString(getOrientation(file));
	}

	public static boolean setOrientation(int orientation, File file)
			throws IOException {
		JpegFile f = null;

		try {
			f = new JpegFile(file, "rw");

			Segment s;

			for (s = f.getFirstSegment(); (s != null)
					&& (s.getId() != JpegSegmentType.APP1.byteValue); s = s
					.getNextSegment()) {
				;
			}

			if (s != null) {
				APP1Segment app1 = new APP1Segment(s);

				do {

					for (int i = 0; i < app1.getEntryCount(); i++) {
						APP1Segment.Entry ifd = app1.getEntry(i);

						if (ifd.getTagNr() == ExifIFD0Directory.TAG_ORIENTATION) {

							ifd.setValue(orientation);

							break; // Continue to update orientation info in

						}
					}
				} while (app1.nextIFD());
			}

			return false;
		} finally {
			if (f != null) {
				f.close();
			}
		}
	}

	private static int asCode(String orientation) {
		if ("left".equals(orientation)) {
			return LEFT_ROTATED;
		}

		if ("right".equals(orientation)) {
			return RIGHT_ROTATED;
		}

		return NOT_ROTATED;
	}

	private static String asString(int orientation) {
		switch (orientation) {
		case LEFT_ROTATED:
			return "left";

		case RIGHT_ROTATED:
			return "right";

		default:
			return "normal";
		}
	}

	public static BufferedImage adjustOrientation(BufferedImage bi,
			String existingOrientation) {
		return adjustOrientation(bi, asCode(existingOrientation));
	}

	public static BufferedImage adjustOrientation(BufferedImage bi,
			int existingOrientation) {
		if (existingOrientation == LEFT_ROTATED) {
			return rotateRight(bi);
		} else if (existingOrientation == RIGHT_ROTATED) {
			return rotateLeft(bi);			
		}

		return bi;
	}
}

class JpegFile {
	private RandomAccessFile file;

	public JpegFile(File f) throws IOException {
		this(f, "r");
	}

	public JpegFile(File f, String mode) throws IOException {
		file = new RandomAccessFile(f, mode);
		file.seek(0);

		byte[] type = new byte[2];
		file.read(type);

		if ((type[0] != -1) || (type[1] != -40)) {
			close();
			throw new IOException("Not a JPEG file: " + f.getName());
		}
	}

	public Segment getFirstSegment() throws IOException {
		return new Segment(file);
	}

	public void close() throws IOException {
		file.close();
	}
}

class Segment {
	private static final byte MARKER_EOI = (byte) 0xD9;
	private static final byte SEGMENT_SOS = (byte) 0xDA;
	byte[] head = new byte[4];
	long offset;
	int length;
	RandomAccessFile file;

	public Segment(RandomAccessFile file) throws IOException {
		this.file = file;
		offset = file.getFilePointer();
		file.read(head);

		if (head[0] != -1) {
			throw new IOException("Not a segment: " + head[0]);
		}

		length = 0xFFFF & ((((head[2] << 8) & 0xFF00) | (head[3] & 0xFF)) - 2);
	}

	public byte getId() {
		return head[1];
	}

	public byte[] getData() throws IOException {
		file.seek(offset + 4);

		byte[] data = new byte[length];
		file.read(data);

		return data;
	}

	public long getDataOffset() {
		return offset + 4;
	}

	public Segment getNextSegment() throws IOException {
		if ((getId() == MARKER_EOI) || (getId() == SEGMENT_SOS)) {
			return null; // We're at last segment
		}

		file.seek(offset + 4 + length);

		return new Segment(file);
	}
}

class APP1Segment {
	boolean bigEndian = false;
	Segment seg;
	byte[] data;
	int entries;
	int firstEntryOffset;

	public APP1Segment(Segment seg) throws IOException {
		if (seg.getId() != JpegSegmentType.APP1.byteValue) {
			throw new IOException("Not an APP1 segment");
		}

		this.seg = seg;
		data = seg.getData();

		String exifId = new String(data, 0, 4);

		if (!"Exif".equals(exifId)) {
			throw new IOException("Couldn't find Exif id in APP1 segment: "
					+ exifId);
		}

		String byteOrder = new String(data, 6, 2);
		bigEndian = "MM".equals(byteOrder);

		int countOffset = get32Bits(10) + 6;
		firstEntryOffset = countOffset + 2;
		entries = get16Bits(countOffset);
	}

	public int getEntryCount() {
		return entries;
	}

	boolean nextIFD() {
		int offsetOfNextIFDOffset = firstEntryOffset + (entries * Entry.SIZE);
		int nextIFDOffset = get32Bits(offsetOfNextIFDOffset) + 6;

		if (nextIFDOffset == 6) {
			return false;
		}

		if (nextIFDOffset >= data.length) {
			return false; // PhotoStudio 5.5 points out of the data section

			// for rotated images. == would be enough, but why
			// chance
		}

		entries = get16Bits(nextIFDOffset);
		firstEntryOffset = nextIFDOffset + 2;

		return true;
	}

	public Entry getEntry(int i) {
		if (i >= entries) {
			throw new IndexOutOfBoundsException();
		}

		return new Entry(firstEntryOffset + (Entry.SIZE * i));
	}

	/**
	 * Get a 16 bit value from file's native byte order. Between 0x0000 and
	 * 0xFFFF.
	 * 
	 * @param offset
	 * 
	 * @return
	 */
	private int get16Bits(int offset) {
		if (bigEndian) {
			// Motorola big first
			return ((data[offset] << 8) & 0xFF00) | (data[offset + 1] & 0xFF);
		} else {
			// Intel ordering
			return ((data[offset + 1] << 8) & 0xFF00) | (data[offset] & 0xFF);
		}
	}

	/**
	 * Set a 16 bit value to file's native byte order. Between 0x0000 and
	 * 0xFFFF.
	 * 
	 * @param value
	 * @param offset
	 */
	private void set16Bits(int value, int offset) {
		if (bigEndian) {
			// Motorola big first
			data[offset] = (byte) ((value >> 8) & 0xFF);
			data[offset + 1] = (byte) (value & 0xFF);
		} else {
			// Intel ordering
			data[offset] = (byte) (value & 0xFF);
			data[offset + 1] = (byte) ((value >> 8) & 0xFF);
		}
	}

	/**
	 * Get a 32 bit value from file's native byte order.
	 * 
	 * @param offset
	 * 
	 * @return
	 */
	private int get32Bits(int offset) {
		if (bigEndian) {
			// Motorola big first
			return ((data[offset] << 24) & 0xFF000000)
					| ((data[offset + 1] << 16) & 0xFF0000)
					| ((data[offset + 2] << 8) & 0xFF00)
					| (data[offset + 3] & 0xFF);
		} else {
			// Intel ordering
			return ((data[offset + 3] << 24) & 0xFF000000)
					| ((data[offset + 2] << 16) & 0xFF0000)
					| ((data[offset + 1] << 8) & 0xFF00)
					| (data[offset] & 0xFF);
		}
	}

	/**
	 * Rough implementation of IFD Entry, only handling data of short type
	 */
	class Entry {
		public static final int SIZE = 12;
		int tagNr;
		int offset;

		public Entry(int offset) {
			this.offset = offset;
			tagNr = get16Bits(offset);
		}

		public int getTagNr() {
			return tagNr;
		}

		public int getValue() {
			return get16Bits(offset + 8);
		}

		public void setValue(int newValue) throws IOException {
			set16Bits(newValue, offset + 8);
			writeToDisk();
		}

		private void writeToDisk() throws IOException {
			seg.file.seek(seg.getDataOffset() + offset);
			seg.file.write(data, offset, Entry.SIZE);
		}
	}
}
