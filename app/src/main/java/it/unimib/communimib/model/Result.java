package it.unimib.communimib.model;

public class Result {

    private Result(){

    }

    public boolean isSuccessful(){
        return this instanceof Success;
    }

    public static final class Success extends Result{
        //class representing a generic success
    }

    public static final class Error extends Result{
        //class representing a generic error
    }

}
