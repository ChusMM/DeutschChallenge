package com.deutchall.utilities;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
//import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.deutchall.activities.R;

public class ImageUtils {

	public static int getRandomImage()
	{
		Random rand = new Random();
		Field[] fields = R.drawable.class.getFields();
	    List<Integer> drawables = new ArrayList<Integer>();
	    
	    for (Field field : fields) {
	        
	    	// Take only those with name starting with "img"
	        if (field.getName().startsWith("img")) {
	            
	        	try {
					drawables.add(field.getInt(null));
				} catch (IllegalArgumentException e) {
					// Should not enter here
				} catch (IllegalAccessException e) {
					// Should not enter here
				}
	        }
	    }
		return rand.nextInt(drawables.size()) + 1;
	}
	
	public static void tileImage(RelativeLayout rLayout,ImageView image) {
		
		BitmapDrawable bd = (BitmapDrawable)image.getDrawable();
        bd.setTileModeXY(TileMode.REPEAT, TileMode.REPEAT);
		rLayout.setBackgroundDrawable(image.getDrawable());
	}
}
	
	/*
	private void setBackGround() {
		
		int rndInt = ImageUtils.getRandomImage();
		String imgName = "img" + rndInt;
		int id = getResources().getIdentifier(imgName, "drawable", getPackageName()); 
		
		Bitmap myBitmap = BitmapFactory.decodeResource(getResources(), id);
		Drawable drawable = new BitmapDrawable(getResources(), myBitmap);
		
		RelativeLayout rLayout = (RelativeLayout) findViewById (R.id.Relative);
		
		BitmapDrawable bd = (BitmapDrawable)drawable;
		bd.setTileModeXY(TileMode.MIRROR, TileMode.MIRROR);
		
		rLayout.setBackgroundDrawable(drawable);
		
		//rLayout.setBackgroundResource(id);
		// The image is coming from resource folder but it could also cursor.getString(cursor.getColumnIndex(SQLiteAdapter.ANS1));load from the internet or whatever
		Bitmap bitmap = ((BitmapDrawable)getResources().getDrawable(id)).getBitmap();
        ImageView image = new ImageView(this);
        
        // Set the bitmap as the ImageView source
        
		image.setImageBitmap(Util.ScaleBitmap(bitmap, this.getBitmapScalingFactor(bitmap))); // Create a new bitmap with the scaling factor
        ImageUtils.tileImage(rLayout,image);
	}*/
