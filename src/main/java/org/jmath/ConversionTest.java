package org.jmath;

import org.jmath.jconvert.JConverter;
import org.jmath.jconvert.quantities.Length;
import org.jmath.jconvert.quantities.Mass;
import org.jmath.jconvert.quantities.Metric;

public class ConversionTest {
    public static void main(String[] args) {
        JConverter j=new JConverter();
        var re=j.convertTo(3898, Metric.mega,Mass.gram,Metric.centi,Mass.pound);
        System.out.println(re);
    }
}
