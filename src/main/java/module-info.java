module jmath
{
    requires java.base;
    requires java.net.http;
    opens org.jmath ;
    opens org.jmath.jconvert;
    opens org.jmath.core;
    opens org.jmath.jnum;
    opens org.jmath.jalgebra;
    opens org.jmath.jconvert.currency;
    opens org.jmath.number;
    opens org.jmath.help;
}