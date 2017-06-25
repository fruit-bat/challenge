# challenge

## Build instructions
<pre>
git clone https://github.com/fruit-bat/challenge.git
cd challenge
mvn install
</pre>

## Running the application
The application can be run using the supplied bash script:
<pre>
./AnatwineBasket Trousers Shirt Shirt Tie Jacket
</pre>
Alternatively, the application can be run using the java command:
<pre>
java -jar target/AnatwineBasket.jar Trousers Shirt Shirt Tie
</pre>

## Comments
I have tried to build the code in a way that could be extended to make a 'real' application. As such the code is split into dependency injected components each with their own currency models. This is a little artificial for the problem at hand but the idea was to demonstrate architecture.

The code has unit tests and Javadoc but time was pressing so they are as complete as I could manage on the weekend. 

The product catalogue and discounts are read from JSON files on the classpath but the service could easily be re-implemented to deal with other database technologies such as MongoDB or JDBC. See products.json and discounts.json if you wish to change values.

Hope you like it!
