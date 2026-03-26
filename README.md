**Overview**

This is a travel companion application wherein users can use it for multiple purposes. It has a functionality to convert currency, fuel efficiency, liquid volume, temperature and distance. This application is developed using Java and XML in Android Studio. 

**Features**

Converts between multiple categories:
-> Currency (USD, AUD, EUR, JPY, GBP)
-> Fuel Efficiency (mpg to km/L)
-> Liquid Volume (Gallon to Liters)
-> Temperature (Celsius, Fahrenheit, Kelvin)
-> Distance (Nautical Mile to Kilometers)

Dynamic dropdowns based on selected category

**UI Components Used**

TextView -> Labels and result display
EditText -> User input
Spinner -> Category, source, and destination selection
Button -> Trigger conversion
CardView -> UI styling (rounded corners and shadow)

**Working**

Steps are shown below:- 

1. User selects a category
2. App updates available units dynamically
3. User enters a value
4. User selects source and destination units
5. Clicks Convert
6. Result is displayed instantly

**Validation & Error Handling**

Prevents empty input
Handles non-numeric input using try-catch
Prevents invalid negative values (except temperature)
Handles same unit conversion properly
