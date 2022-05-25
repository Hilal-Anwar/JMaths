package org.jmath.jalgebra;

import org.jmath.exceptions.DomainException;

import java.util.ArrayList;

public class SimplestForm {
    private ArrayList<String> n_factors;
    private ArrayList<String> d_factors;

    public SimplestForm(ArrayList<String> n_factors, ArrayList<String> d_factors) {
        this.n_factors = n_factors;
        this.d_factors = d_factors;
    }

    SimplifiedAlgebraicFraction getSimpleForm() throws DomainException {
        var dr=new ArrayList<String>();
        var nr=new ArrayList<String>();
        var d_un=new ArrayList<String>();
        var n_un=new ArrayList<String>();
        System.out.println(d_factors);
        System.out.println(n_factors);
        for (int i = 0; i < Math.max(n_factors.size(),d_factors.size());) {
            if (i<n_factors.size() && d_factors.contains(n_factors.get(i))){
                int j=d_factors.indexOf(n_factors.get(i));
                if (j>0)
                 d_factors.remove(j);
                n_factors.remove(i);
                }
            else {
                if(i<n_factors.size()){
                    System.out.println(n_factors.get(i));
                    var it=new Polynomial(n_factors.get(i));
                    if (it.getPolynomial().size()==1){
                        nr.add(n_factors.get(i));
                    }
                    else n_un.add(n_factors.get(i));
                    n_factors.remove(i);
                }
                if(i<d_factors.size()){
                    var it=new Polynomial(d_factors.get(i));
                    if (it.getPolynomial().size()==1){
                        dr.add(d_factors.get(i));
                    }
                    else d_un.add(d_factors.get(i));
                    d_factors.remove(i);
                }
            }

        }
        System.out.println(n_un);
        System.out.println(d_un);
        System.out.println(dr);
        System.out.println(nr);
        return new SimplifiedAlgebraicFraction(n_factors,d_factors);
    }
}
