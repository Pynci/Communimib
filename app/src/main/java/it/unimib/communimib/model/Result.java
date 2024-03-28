package it.unimib.communimib.model;

public abstract class Result {

    private Result(){
    }

    public boolean isSuccessful(){
        return this instanceof Success
                || this instanceof AuthSuccess;
    }

    public static final class Success extends Result{
        //class representing a generic success
    }

    public static final class AuthSuccess extends Result{
        private final String uid;

        public AuthSuccess(String uid){
            this.uid = uid;
        }

        public String getUid() {
            return uid;
        }
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
