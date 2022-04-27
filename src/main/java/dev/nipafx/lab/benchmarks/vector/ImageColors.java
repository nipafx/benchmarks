package dev.nipafx.lab.benchmarks.vector;

import jdk.incubator.vector.ByteVector;
import jdk.incubator.vector.VectorMask;
import jdk.incubator.vector.VectorOperators;
import jdk.incubator.vector.VectorShuffle;
import jdk.incubator.vector.VectorSpecies;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import javax.imageio.ImageIO;
import java.awt.image.DataBufferByte;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.concurrent.TimeUnit;

@Fork(value = 3)
@Warmup(iterations = 3, time = 3, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 3, time = 3, timeUnit = TimeUnit.SECONDS)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Thread)
public class ImageColors {

	private static final String IMAGE_FILE_NAME = "hcmc.jpg";

	private static final VectorSpecies<Byte> RGB_SPECIES = ByteVector.SPECIES_PREFERRED;
	private static final int RGB_STEPS = RGB_SPECIES.length() - RGB_SPECIES.length() % 3;
	private static final VectorShuffle<Byte> COLOR_SHUFFLE = VectorShuffle.fromOp(RGB_SPECIES, ImageColors::rotateRgbValues);
	private static final VectorMask<Byte> PURPLE_SHIFT = VectorShuffle
			// Colors appear in image byte array in order BLUE, GREEN, RED. Only
			// BLUE and RED with indices 0, 3, 6, ... and 2, 5, 8, ... respectively
			// should be set, so indices 1, 4, 7, etc... should be unset in the mask.
			.fromOp(RGB_SPECIES, index -> index % 3 == 1 ? -1 : 1)
			.laneIsValid();

	private final byte[] image;

	public ImageColors() {
		try {
			var imageUrl = ImageColors.class.getClassLoader().getResource(IMAGE_FILE_NAME);
			var image = ImageIO.read(imageUrl);
			this.image = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	/*
	 * INVERTING COLORS
	 */

	@Benchmark
	public byte[] invertColors() {
		byte[] newImage = new byte[image.length];

		for (int pixel = 0; pixel + 2 < image.length; pixel += 3) {
			byte blue = image[pixel];
			byte green = image[pixel + 1];
			byte red = image[pixel + 2];

			byte newRed = (byte) -red;
			byte newGreen = (byte) -green;
			byte newBlue = (byte) -blue;

			newImage[pixel] = newBlue;
			newImage[pixel + 1] = newGreen;
			newImage[pixel + 2] = newRed;
		}

		return newImage;
	}

	@Benchmark
	public byte[] invertColors_vectorized() {
		byte[] newImage = new byte[image.length];

		int loopBound = RGB_SPECIES.loopBound(image.length) - RGB_STEPS;
		int pixel = 0;
		for (; pixel < loopBound; pixel += RGB_STEPS) {
			var rgbValues = ByteVector.fromArray(RGB_SPECIES, image, pixel);
			var newRgbValues = rgbValues.neg();
			newRgbValues.intoArray(newImage, pixel);
		}
		for (; pixel + 2 < image.length; pixel += 3) {
			byte blue = image[pixel];
			byte green = image[pixel + 1];
			byte red = image[pixel + 2];

			byte newRed = (byte) -red;
			byte newGreen = (byte) -green;
			byte newBlue = (byte) -blue;

			newImage[pixel] = newBlue;
			newImage[pixel + 1] = newGreen;
			newImage[pixel + 2] = newRed;
		}

		return newImage;
	}

	/*
	 * ROTATING COLORS
	 */

	@Benchmark
	public byte[] rotateColors() {
		byte[] newImage = new byte[image.length];

		for (int pixel = 0; pixel + 2 < image.length; pixel += 3) {
			byte blue = image[pixel];
			byte green = image[pixel + 1];
			byte red = image[pixel + 2];

			newImage[pixel] = red;
			newImage[pixel + 1] = blue;
			newImage[pixel + 2] = green;
		}

		return newImage;
	}

	@Benchmark
	public byte[] rotateColors_vectorized() {
		byte[] newImage = new byte[image.length];

		int loopBound = RGB_SPECIES.loopBound(image.length) - RGB_STEPS;
		int pixel = 0;
		for (; pixel < loopBound; pixel += RGB_STEPS) {
			var rgbValues = ByteVector.fromArray(RGB_SPECIES, image, pixel);
			var newRgbValues = rgbValues.rearrange(COLOR_SHUFFLE);
			newRgbValues.intoArray(newImage, pixel);
		}
		for (; pixel + 2 < image.length; pixel += 3) {
			byte blue = image[pixel];
			byte green = image[pixel + 1];
			byte red = image[pixel + 2];

			newImage[pixel] = red;
			newImage[pixel + 1] = blue;
			newImage[pixel + 2] = green;
		}

		return newImage;
	}

	/**
	 * Rotate RGB values within each triple, but not across triples
	 * @param newIndex the index in the shuffled vector
	 * @return the index in the old vector mapped to the new one
	 */
	private static int rotateRgbValues(int newIndex) {
		if (newIndex >= RGB_STEPS)
			return newIndex;

		int newValueIndexInTriple = newIndex % 3;
		int tripleStartIndex = newIndex - newValueIndexInTriple;

		int oldValueIndexInTriple = Math.floorMod(newValueIndexInTriple - 1, 3);
		return tripleStartIndex + oldValueIndexInTriple;
	}

	/*
	 * SHIFTING COLORS
	 */

	@Benchmark
	public byte[] purpleShift() {
		byte[] newImage = new byte[image.length];

		double imageLength = image.length;
		for (int pixel = 0; pixel + 2 < image.length; pixel += 3) {
			double purpleQuotient = (pixel / imageLength);
			// ignores one-complement and maps [0d...127d; 128d...255d] to [0...127, -128...-1]
			byte purpleIndex = (byte) (255 * purpleQuotient);

			byte blue = image[pixel];
			byte green = image[pixel + 1];
			byte red = image[pixel + 2];

			// boost blue and red to tint purple
			byte newBlue = maxByte(purpleIndex, blue);
			byte newRed = maxByte(purpleIndex, red);

			newImage[pixel] = newBlue;
			newImage[pixel + 1] = green;
			newImage[pixel + 2] = newRed;
		}

		return newImage;
	}

	@Benchmark
	public byte[] purpleShift_vectorized() {
		byte[] newImage = new byte[image.length];

		double imageLength = image.length;
		// see comment in `invertColors_vectorized`
		int loopBound = RGB_SPECIES.loopBound(image.length) - RGB_STEPS;
		int pixel = 0;
		// vectorized loop
		for (; pixel < loopBound; pixel += RGB_STEPS) {
			// Deviating from the classic loop, the quotient is not computed for each pixel,
			// but for each "pixel block" of length `RGB_STEPS`. This means the resulting image
			// differs from the one produced by the classic loop and also across different
			// CPU architectures with different species lengths.
			double purpleQuotient = (pixel / imageLength);
			byte purpleIndex = (byte) (255 * purpleQuotient);

			var rgbValues = ByteVector.fromArray(RGB_SPECIES, image, pixel);
			var purpleRgbValues = (ByteVector) RGB_SPECIES
					.broadcast(0)
					.blend(purpleIndex, PURPLE_SHIFT);
			var purpleMask = rgbValues.compare(VectorOperators.UNSIGNED_LT, purpleRgbValues);
			var newRgbValues = rgbValues.blend(purpleRgbValues, purpleMask);

			newRgbValues.intoArray(newImage, pixel);
		}
		// remainder
		for (; pixel + 2 < image.length; pixel += 3) {
			double purpleQuotient = (pixel / imageLength);
			byte purpleIndex = (byte) (255 * purpleQuotient);

			byte blue = image[pixel];
			byte green = image[pixel + 1];
			byte red = image[pixel + 2];

			// boost blue and red to tint purple
			byte newBlue = maxByte(purpleIndex, blue);
			byte newRed = maxByte(purpleIndex, red);

			newImage[pixel] = newBlue;
			newImage[pixel + 1] = green;
			newImage[pixel + 2] = newRed;
		}

		return newImage;
	}

	private static byte maxByte(byte x, byte y) {
		return Byte.compareUnsigned(x, y) > 0 ? x : y;
	}

}
