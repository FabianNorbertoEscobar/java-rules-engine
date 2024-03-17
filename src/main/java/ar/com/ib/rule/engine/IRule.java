package ar.com.ib.rule.engine;

public interface IRule {

    public Boolean check();

    public String getErrorMessage();
    
    public String getSuccessMessage();
}
