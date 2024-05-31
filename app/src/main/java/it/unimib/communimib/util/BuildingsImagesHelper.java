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
                imageView.setImageResource(R.mipmap.edificiou1_foreground);
                break;
            }
            case "U2": {
                imageView.setImageResource(R.mipmap.edificiou2_foreground);
                break;
            }
            case "U3": {
                imageView.setImageResource(R.mipmap.edificiou3_foreground);
                break;
            }
            case "U4": {
                imageView.setImageResource(R.mipmap.edificiou4_foreground);
                break;
            }
            case "U5": {
                imageView.setImageResource(R.mipmap.edificiou5_foreground);
                break;
            }
            case "U6": {
                imageView.setImageResource(R.mipmap.edificiou6_foreground);
                break;
            }
            case "U7": {
                imageView.setImageResource(R.mipmap.edificiou7_foreground);
                break;
            }
            case "U9": {
                imageView.setImageResource(R.mipmap.edificiou9_foreground);
                break;
            }
            case "U10": {
                imageView.setImageResource(R.mipmap.edificiou10_foreground);
                break;
            }
            case "U11": {
                imageView.setImageResource(R.mipmap.edificiou11_foreground);
                break;
            }
            case "U14": {
                imageView.setImageResource(R.mipmap.edificiou14_foreground);
                break;
            }
            case "U16": {
                imageView.setImageResource(R.mipmap.edificiou16_foreground);
                break;
            }
            case "U17": {
                imageView.setImageResource(R.mipmap.edificiou17_foreground);
                break;
            }
            case "U19": {
                imageView.setImageResource(R.mipmap.edificiou19_foreground);
                break;
            }
            case "U22": {
                imageView.setImageResource(R.mipmap.edificiou22_foreground);
                break;
            }
            case "U24": {
                imageView.setImageResource(R.mipmap.edificiou24_foreground);
                break;
            }
            default: {
                imageView.setImageResource(R.mipmap.no_image_foreground);
            }
        }
    }
}
