package val.mx.vbread.views;

import android.graphics.Color;
import android.util.Log;

import java.math.BigDecimal;
import java.util.Random;

import val.mx.vbread.Complex;
import val.mx.vbread.containers.Dimension;
import val.mx.vbread.containers.DrawInfo;

public class MandelBrotAdapter extends FractalView.Adapter {

    int[] colors = new int[] {
            Color.rgb(9, 1, 47),
            Color.rgb(4, 4, 73),
            Color.rgb(0, 7, 100),
            Color.rgb(12, 44, 138),
            Color.rgb(24, 82, 177),
            Color.rgb(57, 125, 209),
            Color.rgb(134, 181, 229),
            Color.rgb(211, 236, 248),
            Color.rgb(241, 233, 191),
            Color.rgb(248, 201, 95),
            Color.rgb(255, 170, 0),
            Color.rgb(204, 128, 0),
            Color.rgb(153, 87, 0),
            Color.rgb(106, 52, 3)
            //SOURCE https://stackoverflow.com/questions/16500656/which-color-gradient-is-used-to-color-mandelbrot-in-wikipedia
    };


    int maxItera = 10;

    @Override
    public DrawInfo onDraw(DrawInfo info) {

        Complex complex = new Complex(info.getX().doubleValue(),info.getY().doubleValue());
        Complex start = complex;
        for (int i = 0; i < maxItera; i++) {
            complex = complex.multiply(complex).add(start);
            if(complex.doubleValue() > 2) {
                if(i!= 0) {
                    info.setColor(colors[i%colors.length]);
                }

                return info;
            }

        }

        return info;
    }

    Dimension dimension = new Dimension(new BigDecimal("-2"),new BigDecimal("2"),new BigDecimal("2"),new BigDecimal("-2"));

    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
    }

    @Override
    public Dimension getSize() {
        return dimension;
    }

    public void setMaxItera(int maxItera) {
        this.maxItera = maxItera;
    }


}