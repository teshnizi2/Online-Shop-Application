package View;

public class OutPut {

    public enum Errors {
        YouAreNotCustomer("You Aren't Customer");










        private String error;
        Errors(String stringError) {
            this.error = stringError;
        }
        public String getError() {
            return error;
        }
    }


    public void setErrors() {
        System.err.println(Errors.YouAreNotCustomer.getError());
    }




}
