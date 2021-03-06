package val.mx.vbread.adapters;

import android.graphics.Color;

import java.math.BigDecimal;

import val.mx.vbread.Complex;
import val.mx.vbread.containers.Dimension;
import val.mx.vbread.containers.DrawInfo;
import val.mx.vbread.views.FractalView;

import static android.graphics.Color.BLACK;

public class Magnet1 extends FractalView.Adapter {

    @Override
    public int onDraw(DrawInfo info) {

        Complex c = new Complex(info.getX(), info.getY());
        Complex z = c;

        for (int i = 0; i < itera; i++) {
            z = z.pow(2).add(c.subtract(1d)).divide(z.multiply(2).add(c.subtract(2d))).pow(2);
            if (z.abs() > 2) return i;
        }

        return -1;
    }



    @Override
    public Dimension getInitialSize() {
        return new Dimension(new BigDecimal("-2"), new BigDecimal("2"), new BigDecimal("2"), new BigDecimal("-2"));

    }
}
