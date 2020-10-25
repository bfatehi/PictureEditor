package a9;
import java.util.ArrayList;

public class BrushPixel {
	private static Pixel brushPixel;
	private static ArrayList<BrushPixelObserver> observers = new ArrayList<BrushPixelObserver>();
	
	public Pixel getPixel(){
		return brushPixel;
	}
	
	public void setPixel(Pixel p){
		brushPixel = p;
		notifyObservers();
	}
	
	public void addObserver(BrushPixelObserver o){
		observers.add(o);
	}
	
	public void removeObserver(BrushPixelObserver o){
		observers.remove(o);
	}
	
	public void notifyObservers(){
		for(BrushPixelObserver o : observers){
			o.update(this);
		}
	}

}
