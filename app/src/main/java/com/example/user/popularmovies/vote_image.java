package com.example.user.popularmovies;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

/**
 * Created by user on 9/3/2015.
 */
public class vote_image {
    Bitmap image ;
    int width,height;
    int start_i,start_j,all_power;

    public vote_image (Bitmap image)
    {
        this.image=image;
        width=image.getWidth();
        height=image.getHeight();
        all_power=get_all_power();
    }

    private int get_all_power ()
    {
        int p,R,G,B;
        int all_power = 0;
        for(int i=0;i<width;i++)
            for(int j=0;j<height;j++)
            {
                p = image.getPixel(i,j);

                R = (p >> 16) & 0xff;
                G = (p >> 8) & 0xff;
                B = p & 0xff;

                if(R<245)
                {
                    if(all_power==0)
                    {
                        start_i=i;
                        start_j=j;
                    }
                    all_power++;
                    break;
                }
            }
        return all_power;
    }

    public Bitmap get_image (double degree , double total)
    {
        Bitmap result = image.copy(Bitmap.Config.ARGB_8888, true);
        int power = (int) ( (double)(degree/total)*all_power );
        int count=0;
        int p,R,G,B;
        for(int i=start_i;i<width;i++) {
            if(count==power)
                break;
            for (int j = 0; j < height; j++) {
                p = image.getPixel(i, j);

                R = (p >> 16) & 0xff;
                if (R < 245) {
                    count++;
                    for (int k = j; k < height; k++) {
                        p = result.getPixel(i, k);
                        R = (p >> 16) & 0xff;
                        if (R < 245) {
                            Log.d("log1",String.valueOf(i));
                            Log.d("log1",String.valueOf(j));
                            Log.d("log1",String.valueOf(width));
                            Log.d("log1",String.valueOf(height));

                            result.setPixel(i, k, Color.BLUE);
                        }

                    }
                    break;
                }

            }
        }
        return result;
    }
}
