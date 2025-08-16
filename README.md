# JMaths 🧮

A comprehensive Java mathematics library providing advanced mathematical computations, algebraic simplification, unit conversions, and symbolic mathematics operations. Built with Java 22 and designed for both educational and professional use.

## ✨ Features

### 🔢 Core Mathematical Operations
- **Arithmetic Calculations**: BODMAS/PEMDAS compliant expression evaluation
- **Advanced Math Functions**: Factorial, power, square root, cube root, percentage, modulus
- **High Precision Computing**: BigDecimal and BigInteger support for precise calculations
- **Complex Number Operations**: Full complex number arithmetic support

### 📐 Trigonometric Functions
- **Circular Functions**: sin, cos, tan, sec, cosec, cot
- **Inverse Functions**: asin, acos, atan, asec, acosec, acot
- **Hyperbolic Functions**: sinh, cosh, tanh, sech, cosech, coth
- **Inverse Hyperbolic**: asinh, acosh, atanh, asech, acosech, acoth
- **Multiple Angle Units**: Degrees, Radians, Grades

### 🔍 Logarithmic Functions
- **Natural Logarithm**: ln(x)
- **Common Logarithm**: log(x)
- **Inverse Operations**: Exponential functions

### 🧪 Algebraic Engine
- **Expression Simplification**: Automatic algebraic simplification
- **Polynomial Operations**: Addition, multiplication, factorization
- **Equation Solving**: Linear and polynomial equation solutions
- **Fraction Operations**: Rational number arithmetic with simplification
- **Formula Generation**: Automatic algebraic formula creation

### 🔄 Unit Conversion System
- **Physical Quantities**: Length, Mass, Area, Volume, Temperature, etc.
- **Real-time Currency Conversion**: Live exchange rates via JSON API
- **Metric Prefixes**: Full support for SI prefixes (nano to exa)
- **Comprehensive Units**: 60+ different unit types supported

### 🌍 Supported Unit Categories
- **Length**: Meter, Kilometer, Mile, Inch, Foot, Yard, etc.
- **Mass**: Gram, Kilogram, Pound, Ounce, Ton, etc.
- **Temperature**: Celsius, Fahrenheit, Kelvin, Rankine
- **Area**: Square meter, Acre, Hectare, Square foot, etc.
- **Volume**: Liter, Gallon, Cubic meter, Pint, Quart, etc.
- **Speed**: m/s, km/h, mph, knots, etc.
- **Energy**: Joule, Calorie, BTU, kWh, etc.
- **Pressure**: Pascal, Bar, PSI, Torr, etc.
- **Power**: Watt, Horsepower, BTU/hour, etc.
- **Currencies**: 150+ world currencies with live rates

## 🚀 Getting Started

### Prerequisites

- **Java 22** or higher
- **Maven 3.8+** for building the project

### Installation

1. **Clone the repository**:
   ```bash
   git clone <repository-url>
   cd JMaths
   ```

2. **Build the project**:
   ```bash
   mvn clean compile assembly:single
   ```

3. **Run the calculator**:
   ```bash
   java -jar target/JMaths-1.0-SNAPSHOT-jar-with-dependencies.jar
   ```

   Or using Maven:
   ```bash
   mvn exec:java -Dexec.mainClass="org.jmath.Main"
   ```

## 💻 Usage Examples

### Basic Calculator Mode
```
Welcome to console calculator 
Enter /exit to exit
Enter /help for help
Enter cls for cleaning the screen

> 2 + 3 * 4
Answer : 14

> sin(45)
Answer : 0.7071067811865476

> log(100)
Answer : 2
```

### Algebraic Simplification
```java
// Using the Simplify class
Simplify simplifier = new Simplify("(x^2 + 2*x + 1)/(x + 1)");
Object[] result = simplifier.solve();
// Result: x + 1
```

### Unit Conversion
```java
JConverter converter = new JConverter();

// Convert 100 centimeters to inches
double inches = converter.convertTo(100, Length.CENTIMETER, Length.INCH);
// Result: 39.37 inches

// Convert with metric prefixes
double result = converter.convertTo(1, Metric.KILO, Mass.GRAM, Metric.BASE, Mass.POUND);
// Result: 2.20462 pounds
```

### Advanced Mathematical Operations
```java
JNum calculator = new JNum();

// Evaluate complex expressions
BigDecimal result = calculator.eval("2^10 + sqrt(144) - log(100)", Angle.DEGREE);

// Work with fractions
Fraction frac = calculator.getFraction("22/7");
```

## 🎯 Interactive Commands

### Angle Measurement Commands
- `a/D` - Switch to degrees (default)
- `a/R` - Switch to radians
- `a/G` - Switch to grades
- `a/T` - Display current angle measurement type

### System Commands
- `/help` - Display help information
- `/exit` - Exit the application
- `cls` - Clear screen (Windows)

## 🔧 Mathematical Syntax

### Basic Operations
```
+, -, *, /          # Basic arithmetic
^                   # Power (a^b)
!                   # Factorial (a!)
r(a)               # Square root
cr(a)              # Cube root
mod(a,b)           # Modulus
```

### Trigonometric Functions
```
sin(a), cos(a), tan(a)           # Basic trig functions
sec(a), cosec(a), cot(a)         # Reciprocal functions
asin(a), acos(a), atan(a)        # Inverse functions
sinh(a), cosh(a), tanh(a)        # Hyperbolic functions
asinh(a), acosh(a), atanh(a)     # Inverse hyperbolic
```

### Constants
```
pi, p              # π (22/7)
e                  # Euler's number (2.718...)
```

## 🏗️ Project Architecture

### Core Modules

#### 📁 `org.jmath.core`
- **BigMath**: Extended mathematical operations
- **Fraction**: Rational number operations
- **Function**: Function definition and evaluation
- **Constants**: Mathematical constants
- **Operators**: Operator definitions

#### 📁 `org.jmath.jnum`
- **JNum**: Main numerical computation engine
- **Functions**: Function implementations
- **FunctionFactory**: Function creation and management

#### 📁 `org.jmath.jalgebra`
- **Simplify**: Algebraic expression simplification
- **PolynomialSolver**: Polynomial operations
- **EquationSolver**: Equation solving algorithms
- **Factorization**: Algebraic factorization

#### 📁 `org.jmath.jconvert`
- **JConverter**: Main conversion engine
- **Quantities**: Unit definitions (Length, Mass, Temperature, etc.)
- **CurrencyDataLoader**: Live currency rate fetching

#### 📁 `org.jmath.functions`
- **AlgebraicFunction**: Algebraic function handling
- **MathFunction**: Mathematical function definitions

## 📊 Dependencies

```xml
<!-- High-precision JSON processing -->
<dependency>
    <groupId>com.google.code.gson</groupId>
    <artifactId>gson</artifactId>
    <version>2.11.0</version>
</dependency>

<!-- File I/O operations -->
<dependency>
    <groupId>commons-io</groupId>
    <artifactId>commons-io</artifactId>
    <version>2.16.1</version>
</dependency>
```

## 🌐 Currency Data

JMaths includes real-time currency conversion with:
- **150+ Currencies**: Major and minor world currencies
- **Live Exchange Rates**: Auto-updated from reliable sources
- **Offline Fallback**: Cached rates when network unavailable
- **Currency Names**: Full currency name resolution

Sample currencies supported:
```
USD, EUR, GBP, JPY, CAD, AUD, CHF, CNY, INR, BTC, ETH, ADA, DOGE...
```

## 🔬 Advanced Features

### Function Creation
Create custom mathematical functions dynamically:
```java
// Define custom functions
f(x) = x^2 + 2*x + 1
g(x,y) = sin(x) + cos(y)
```

### Symbolic Mathematics
- Variable substitution
- Expression expansion
- Automatic simplification
- Rational expression handling

### Error Handling
- Domain validation
- Function format checking
- Keyword conflict resolution
- Graceful error recovery

## 🧪 Testing

Run the test suite:
```bash
mvn test
```

Example test scenarios:
```java
// Test basic arithmetic
assertEquals(new BigDecimal("14"), calculator.eval("2 + 3 * 4"));

// Test trigonometry
assertEquals(0.7071, calculator.eval("sin(45)").doubleValue(), 0.0001);

// Test unit conversion
assertEquals(39.37, converter.convertTo(100, Length.CM, Length.INCH), 0.01);
```

## 🛠️ Building and Deployment

### Build Options
```bash
# Standard build
mvn clean compile

# Build with dependencies
mvn clean compile assembly:single

# Run specific main class
mvn exec:java -Dexec.mainClass="org.jmath.jalgebra.Simplify"
```

### Module System
JMaths uses Java modules (module-info.java):
```java
module org.jmath {
    requires java.base;
    requires java.net.http;
    requires com.google.gson;
    requires org.apache.commons.io;
}
```

## 🐛 Troubleshooting

### Common Issues

1. **Build Errors**:
   ```bash
   # Clear Maven cache
   mvn dependency:purge-local-repository
   
   # Verify Java version
   java --version  # Should be 22+
   ```

2. **Currency Data Loading**:
    - Ensure internet connection for live rates
    - Check firewall settings
    - Fallback to cached data if network fails

3. **Memory Issues**:
    - Increase JVM heap size: `-Xmx2g`
    - Use streaming for large computations

### Performance Tips
- Use appropriate precision settings
- Cache frequently used conversions
- Batch algebraic operations when possible

## 🤝 Contributing

We welcome contributions! Here's how you can help:

### Areas for Contribution
- **New Unit Types**: Add more physical quantities
- **Mathematical Functions**: Implement additional functions
- **Performance**: Optimize computation algorithms
- **Documentation**: Improve code documentation
- **Testing**: Add more comprehensive tests

### Development Setup
1. Fork the repository
2. Create a feature branch: `git checkout -b feature-name`
3. Follow Java coding conventions
4. Add tests for new functionality
5. Update documentation as needed
6. Submit a pull request

## 📜 License

This project is open source. Feel free to use, modify, and distribute as needed.

## 🙏 Acknowledgments

- **Java Community**: For the robust platform
- **Apache Commons**: For I/O utilities
- **Google Gson**: For JSON processing
- **Currency API Providers**: For real-time exchange rates
- **Contributors**: Everyone who helps improve JMaths

---

**Happy Calculating! 🧮✨**

For questions, issues, or suggestions, please feel free to open an issue or contribute to the project.

## 📞 Support

- **Documentation**: Check the `/help` command in the application
- **Issues**: Submit bugs and feature requests via GitHub Issues
- **Community**: Join discussions and get help from other users

*JMaths - Making advanced mathematics accessible in Java*
