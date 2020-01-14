package co.kr.bluebird.newrfid.app.bbrfidbtdemo.utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import co.kr.bluebird.newrfid.app.bbrfidbtdemo.R;

public class InventoryImageAdapter extends PagerAdapter
{

    private Context mContext;
    private int[] mImagesIds = new int[] {R.drawable.materialcheck};
    private Bitmap[] ArrayImage;

    public InventoryImageAdapter(Context context, Bitmap[] ArrayImage)
    {
        mContext = context;
        this.ArrayImage = ArrayImage;
    }
    @Override
    public int getCount() {
        return ArrayImage.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(mContext);

        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(300,300);
        imageView.setLayoutParams(layoutParams);

        imageView.setLayoutParams(layoutParams);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        /*imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);*/
        imageView.setAdjustViewBounds(true);
        imageView.setImageBitmap(ArrayImage[position]);
        container.addView(imageView,0);
        return imageView;
        //return super.instantiateItem(container, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //super.destroyItem(container, position, object);
        container.removeView((ImageView) object);
    }
}
