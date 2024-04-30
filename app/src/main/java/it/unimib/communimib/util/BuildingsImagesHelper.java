package it.unimib.communimib.util;

import android.widget.ImageView;

import it.unimib.communimib.R;

public class BuildingsImagesHelper {

    private BuildingsImagesHelper() {
        //Costruttore privato vuoto
    }

    public static void setBuildingImage(ImageView imageView, String building){
        switch (building){
            case "U1": {
                imageView.setBackgroundResource(R.mipmap.edificiou1_foreground);
                break;
            }
            case "U2": {
                imageView.setBackgroundResource(R.mipmap.edificiou2_foreground);
                break;
            }
            case "U3": {
                imageView.setBackgroundResource(R.mipmap.edificiou3_foreground);
                break;
            }
            case "U4": {
                imageView.setBackgroundResource(R.mipmap.edificiou4_foreground);
                break;
            }
            case "U5": {
                imageView.setBackgroundResource(R.mipmap.edificiou5_foreground);
                break;
            }
            case "U6": {
                imageView.setBackgroundResource(R.mipmap.edificiou6_foreground);
                break;
            }
            case "U7": {
                imageView.setBackgroundResource(R.mipmap.edificiou7_foreground);
                break;
            }
            case "U9": {
                imageView.setBackgroundResource(R.mipmap.edificiou9_foreground);
                break;
            }
            case "U10": {
                imageView.setBackgroundResource(R.mipmap.edificiou10_foreground);
                break;
            }
            case "U11": {
                imageView.setBackgroundResource(R.mipmap.edificiou11_foreground);
                break;
            }
            case "U14": {
                imageView.setBackgroundResource(R.mipmap.edificiou14_foreground);
                break;
            }
            case "U16": {
                imageView.setBackgroundResource(R.mipmap.edificiou16_foreground);
                break;
            }
            case "U17": {
                imageView.setBackgroundResource(R.mipmap.edificiou17_foreground);
                break;
            }
            case "U19": {
                imageView.setBackgroundResource(R.mipmap.edificiou19_foreground);
                break;
            }
            case "U22": {
                imageView.setBackgroundResource(R.mipmap.edificiou22_foreground);
                break;
            }
            case "U24": {
                imageView.setBackgroundResource(R.mipmap.edificiou24_foreground);
                break;
            }
            default: {
                imageView.setBackgroundResource(R.mipmap.no_image_foreground);
            }
        }
    }
}
