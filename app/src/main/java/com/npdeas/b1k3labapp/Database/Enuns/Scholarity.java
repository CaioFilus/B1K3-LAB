package com.npdeas.b1k3labapp.Database.Enuns;

public enum Scholarity {

    SCHOOL_FUN_CPLT(0),
    SCHOOL_INCPLT(1),
    HIGH_CMPLT(2),
    HIGH_INCMPLT(3),
    COLLEGE_CPLT(4),
    COLLEGE_INCPLT(5),
    SPECIALIZATION(6),
    MASTERS(7),
    DOCTORATE(8);

    public int value;

    Scholarity(int value) {
        this.value = value;
    }

    public boolean compare(int i){return value == i;}

    public static Scholarity getValue(int status)
    {
        Scholarity[] as = Scholarity.values();
        for(int i = 0; i < as.length; i++)
        {
            if(as[i].compare(status))
                return as[i];
        }
        return null;
    }
}
