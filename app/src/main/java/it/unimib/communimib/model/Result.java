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
        private final String message;

        public Error(String message){
            this.message = message;
        }

        public String getMessage(){
            return message;
        }
    }

}
