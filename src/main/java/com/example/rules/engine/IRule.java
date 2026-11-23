package com.example.rules.engine;

public interface IRule {

    public Boolean check();

    public String getErrorMessage();
    
    public String getSuccessMessage();
}
