package com.github.lesach.client.raw;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DClientBankAccount {
    public String iban;
    public String bic;
    public String name;
    public String number;
    public String sortCode;
}
