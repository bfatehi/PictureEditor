package a9;

public class ImageEditorModel {

	private Picture original;
	private ObservablePicture current;
	
	public ImageEditorModel(Picture f) {
		original = f;
		current = original.copy().createObservable();
	}

	public ObservablePicture getCurrent() {
		return current;
	}

	public Pixel getPixel(int x, int y) {
		return current.getPixel(x, y);
	}

	public void paintAt(int x, int y, Pixel brushColor, int brush_size, double opacity_value) {
		current.suspendObservable();;
		
		double newR = 0;
		double newG = 0;
		double newB = 0;
		for (int xpos=x-brush_size+1; xpos <=x+brush_size-1; xpos++) {
			for (int ypos=y-brush_size+1; ypos <=y+brush_size-1; ypos++) {
				if (xpos >= 0 &&
					xpos < current.getWidth() &&
					ypos >= 0 &&
					ypos < current.getHeight()) {
					
					newR = brushColor.getRed() *opacity_value/100 + brushColor.getRed()*(1-opacity_value/100);
					newG = brushColor.getGreen()*opacity_value/100 + brushColor.getGreen()*(1-opacity_value/100);
					newB = brushColor.getBlue()*opacity_value/100 + brushColor.getBlue()*(1-opacity_value/100);
					
					current.setPixel(xpos, ypos, new ColorPixel(newR, newG, newB));
				}
			}
		}
		
		current.resumeObservable();
	}
	public void blurAt(int x, int y, int blur_intensity, int brush_size) {
		current.suspendObservable();

		//Cycle through pixels brush touches, ensuring they are inside frame
		for (int xpos=Math.max(0, x-brush_size+1); xpos < Math.min(current.getWidth(), x+brush_size-1); xpos++) {
			for (int ypos=Math.max(0, y-brush_size+1); ypos < Math.min(current.getHeight(), y+brush_size-1); ypos++) {
				
				//Get dims of area to be averaged:
				//First, ensure pixels to be averaged are inside frame
				int xTop = Math.max(0, xpos-blur_intensity);
				int yTop = Math.max(0, ypos-blur_intensity);
				int xBott = Math.min(current.getWidth(), xpos+blur_intensity);
				int yBott = Math.min(current.getHeight(), ypos+blur_intensity);
			
				//Determine size of area to be averaged:
				int widthToAvg = xBott-xTop;
				int heightToAvg = yBott-yTop;
				
				//Take average of appropriate number of pixels:
				SubPictureImpl areaToAvg = new SubPictureImpl(current,xTop, yTop, widthToAvg, heightToAvg);
				current.unregisterROIObserver(areaToAvg);
				Pixel newPixel = areaToAvg.getAverage();
				current.setPixel(xpos,ypos, newPixel);
			}
		}
		current.resumeObservable();
	}
}
