package com.npdeas.b1k3labapp.Database.Enuns;

public enum Gender {

    MASC(0),
    FEM(1),
    OTHER(2);

    public int value;

    Gender(int value) {
        this.value = value;
    }

    public boolean compare(int i){return value == i;}

    public static Gender getValue(int gender)
    {
        Gender[] as = Gender.values();
        for(int i = 0; i < as.length; i++)
        {
            if(as[i].compare(gender))
                return as[i];
        }
        return null;
    }

}
