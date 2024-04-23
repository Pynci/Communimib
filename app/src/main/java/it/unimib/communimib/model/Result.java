package it.unimib.communimib.model;

public abstract class Result {

    private Result(){
    }

    public boolean isSuccessful(){
        return !(this instanceof Error);
    }

    public static final class Success extends Result {
        //class representing a generic success
    }

    public static final class SignupSuccess extends Result {
        private final String uid;

        public SignupSuccess(String uid){
            this.uid = uid;
        }

        public String getUid() {
            return uid;
        }
    }

    public static final class UserSuccess extends Result {

        private final User user;

        public UserSuccess(User user){
            this.user = user;
        }

        public User getUser(){
            return user;
        }
    }

    public static final class UriSuccess extends Result {
        private final String uri;

        public UriSuccess(String uri){
            this.uri = uri;
        }

        public String getUri(){
            return uri;
        }
    }

    public static final class BooleanSuccess extends Result {

        private final boolean aBoolean;

        public BooleanSuccess(boolean aBoolean){
            this.aBoolean = aBoolean;
        }

        public boolean getBoolean(){
            return aBoolean;
        }

    }

    public static final class ReportSuccess extends Result {
        private final Report report;

        public ReportSuccess(Report report){
            this.report = report;
        }

        public Report getReport(){
            return report;
        }
    }

    public static final class Error extends Result {
        private final String message;

        public Error(String message){
            this.message = message;
        }

        public String getMessage(){
            return message;
        }
    }

}
