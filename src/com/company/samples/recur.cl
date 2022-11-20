
class Fibonacci {
  	
  	fibonacci(n: Int) : Int {
        {( let f : Int in
      	 if n=0 then f<-n else
         if n=1 then f<-n else
        	 f<-fibonacci(n-1)+fibonacci(n-2)
         fi fi
       );}
     };
  
  };

class Main inherits IO {
    n: Int <- 10;
  	--facto: Factorial;
  	fibo: Fibonacci;
  
  	main() : SELF_TYPE {
	{
	   -- facto <- new Factorial;
      	fibo <- new Fibonacci;
      	--out_int(facto.factorial(n));
      	out_int(fibo.fibonacci(n));
      	self;
	}
    };
};

