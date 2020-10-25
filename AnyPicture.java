package a9;

abstract public class AnyPicture implements Picture {

	abstract public int getWidth();
	abstract public int getHeight();

	abstract public Pixel getPixel(int x, int y);
	
	@Override
	public Pixel getPixel(Coordinate c) {
		if (c == null) {
			throw new IllegalArgumentException("Coordinate is null");
		}
		return this.getPixel(c.getX(), c.getY());
	}

	abstract public void setPixel(int x, int y, Pixel p);
	
	@Override
	public void setPixel(Coordinate c, Pixel p) {
		if (c == null) {
			throw new IllegalArgumentException("Coordinate is null");
		}
		setPixel(c.getX(), c.getY(), p);
	}
	
	@Override
	public SubPicture extract(int xoff, int yoff, int width, int height) {
		return new SubPictureImpl(this, xoff, yoff, width, height);
	}

	@Override 
	public SubPicture extract(Coordinate corner_a, Coordinate corner_b) {
		if (corner_a == null || corner_b == null) {
			throw new IllegalArgumentException("One or both coordinates is null");
		}
		
		int min_x = corner_a.getX() < corner_b.getX() ? corner_a.getX() : corner_b.getX();
		int min_y = corner_a.getY() < corner_b.getY() ? corner_a.getY() : corner_b.getY();
		int max_x = corner_a.getX() > corner_b.getX() ? corner_a.getX() : corner_b.getX();
		int max_y = corner_a.getY() > corner_b.getY() ? corner_a.getY() : corner_b.getY();
		
		return extract(min_x, min_y, (max_x-min_x)+1, (max_y-min_y)+1);
	}
	
	@Override
	public SubPicture extract(Region r) {
		if (r == null) {
			throw new IllegalArgumentException("Region is null");
		}

		return extract(r.getUpperLeft(), r.getLowerRight());
	}
	
	@Override
	public ObservablePicture createObservable() {
		return new ObservablePictureImpl(this);
	}
	
	protected Region getFrameRegion() {
		return new RegionImpl(new Coordinate(0,0), new Coordinate(getWidth()-1, getHeight()-1));
	}
	
	@Override
	public Picture copy() {
		Picture copy = new PictureImpl(getWidth(), getHeight());
		for (int x=0; x<getWidth(); x++) {
			for (int y=0; y<getHeight(); y++) {
				copy.setPixel(x, y, getPixel(x,y));
			}
		}
		return copy;
	}
	public ColorPixel getAverage() {
		double red_sum = 0.0;
		double green_sum = 0.0;
		double blue_sum = 0.0;

		for (int x=0; x<getWidth(); x++) {
			for (int y=0; y<getHeight(); y++) {
				Pixel p = getPixel(x,y);
				red_sum += p.getRed();
				green_sum += p.getGreen();
				blue_sum += p.getBlue();
			}
		}
		double num_pixels = getWidth() * getHeight();
		return new ColorPixel(red_sum / num_pixels,
				green_sum / num_pixels,
				blue_sum / num_pixels);
	}

}
