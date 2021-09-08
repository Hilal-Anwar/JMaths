package org.jmath.jnum;

import org.jmath.exceptions.DomainException;
import org.jmath.exceptions.FunctionFormatException;
import org.jmath.exceptions.KeyWordException;
import org.jmath.jconvert.quantities.Angle;

import java.math.BigDecimal;
import java.math.BigInteger;

public class JNum extends FunctionFactory {
    /**
     * @author Helal Anwar
     * @see BigInteger
     * @see BigDecimal
     */
    public BigDecimal eval(String exp, Angle angle) throws DomainException, FunctionFormatException, KeyWordException {
        if (!super.checkForFunction(exp) && !super.checkAndCreateConstant(exp, angle)) {
            exp = exp.replace("−", "-");
            exp = exp.replace("×", "*");
            exp = exp.replace("÷", "/");
            return super.eval(exp, angle);
        } else {
            if (super.checkForFunction(exp))
            System.out.println("Function created successfully");
            if(super.checkAndCreateConstant(exp, angle))
                System.out.println("Constant created successfully");
            return BigDecimal.valueOf(0);
        }
    }

    public BigDecimal eval(String exp) throws DomainException, FunctionFormatException, KeyWordException {
        return super.eval(exp, Angle.DEGREE);
    }

    @Override
    public String getFinalExpression() {
        return super.getFinalExpression();
    }
}
